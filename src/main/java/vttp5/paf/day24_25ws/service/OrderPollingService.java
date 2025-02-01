package vttp5.paf.day24_25ws.service;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp5.paf.day24_25ws.model.Order;
import vttp5.paf.day24_25ws.model.OrderDetails;

@Component
public class OrderPollingService 
{
    @Autowired
    @Qualifier("myredis")
    RedisTemplate<String, String> template;

    @Autowired
    private OrderService orderService;

    @Value("${customer.name}")
    private String customerName;

    
    @Async
    public void startPolling()
    {
        Runnable poller = () -> {
            ListOperations<String, String> orderList = template.opsForList();

            while(true)
            {
                try
                {
                    Optional<String> opt = Optional.ofNullable(
                        orderList.rightPop(customerName, Duration.ofSeconds(5))
                    );

                    if(opt.isPresent())
                    {
                        String jOrderStr = opt.get();

                        System.out.printf("Processing order for %s: %s \n", customerName, jOrderStr);

                        JsonReader jReader = Json.createReader(new StringReader(jOrderStr));
                        JsonObject jOrder = jReader.readObject();

                        // Create Order object
                        Order order = new Order();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        sdf.setLenient(false);

                        order.setOrderDate(sdf.parse(jOrder.getString("orderDate")));
                        order.setCustomerName(jOrder.getString("customerName"));
                        order.setShipAddress(jOrder.getString("shipAddress"));
                        order.setNotes(jOrder.getString("notes"));
                        order.setTax(jOrder.getJsonNumber("tax").doubleValue());

                        JsonArray jOrderDetailsArray = jOrder.getJsonArray("lineItems");
                        List<OrderDetails> lineItems = new ArrayList<>();
                        
                        for (int i = 0; i<jOrderDetailsArray.size(); i++)
                        {
                            JsonObject jOrderDetail = jOrderDetailsArray.get(i).asJsonObject();

                            // Create Order object
                            OrderDetails orderDetails = new OrderDetails();
                            
                            orderDetails.setProduct(jOrderDetail.getString("product"));
                            orderDetails.setUnitPrice(jOrderDetail.getJsonNumber("unitPrice").doubleValue());
                            orderDetails.setDiscount(jOrderDetail.getJsonNumber("discount").doubleValue());
                            orderDetails.setQuantity(jOrderDetail.getInt("quantity"));

                            lineItems.add(orderDetails);
                        }

                        order.setLineItems(lineItems);

                        // Save order
                        Boolean isCreated = orderService.createOrder(order);

                        if (!isCreated)
                        {
                            System.out.println("Order not created in mysql..");
                        }

                        System.out.println("Order created succesfully in mysql");
                    }
                }
                catch (Exception e)
                {
                    System.err.println("Error processing: " + e.getMessage());
                }
            }   
        };

        Executors.newSingleThreadExecutor().execute(poller);
    }
}
