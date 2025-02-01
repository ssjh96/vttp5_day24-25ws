package vttp5.paf.day24_25ws.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp5.paf.day24_25ws.model.Order;
import vttp5.paf.day24_25ws.model.OrderDetails;
import vttp5.paf.day24_25ws.service.OrderService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class OrderRestController 
{
    @Autowired
    private OrderService orderService;

    @Autowired
    @Qualifier("myredis")
    private RedisTemplate<String, String> template;

    @PostMapping(path = "/order24", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> createOrderMVM(@RequestBody MultiValueMap<String, String> formData) 
    {
        // formData must match names in html form, not the columns in mysql db, snake case names in DB are handled by repo

        try
        {
            // Parse form data
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            // .get() returns a list of values: .get(email) > ["x@email.com", "y@email.com"]
            String orderDateStr = formData.getFirst("orderDate"); // return first value
            Date orderDate = sdf.parse(orderDateStr);

            String customerName = formData.getFirst("customerName"); 
            String shipAddress = formData.getFirst("shipAddress");
            String notes = formData.getFirst("notes");
            Double tax = Double.valueOf(formData.getFirst("tax"));

            // Create Order object
            Order order = new Order();

            order.setOrderDate(orderDate);
            order.setCustomerName(customerName);
            order.setShipAddress(shipAddress);
            order.setNotes(notes);
            order.setTax(tax);
            
            // Parse line items
            List<OrderDetails> lineItems = new ArrayList<>();
            int i = 0;

            while (formData.containsKey("lineItems[" + i + "].product"))
            {
                String product = formData.getFirst("lineItems[" + i + "].product");
                Double unitPrice = Double.valueOf(formData.getFirst("lineItems[" + i + "].unitPrice"));
                Double discount = Double.valueOf(formData.getFirst("lineItems[" + i + "].discount"));
                Integer quantity = Integer.valueOf(formData.getFirst("lineItems[" + i + "].quantity"));

                // Create new order details object
                OrderDetails orderDetails = new OrderDetails();

                orderDetails.setProduct(product);
                orderDetails.setUnitPrice(unitPrice);
                orderDetails.setDiscount(discount);
                orderDetails.setQuantity(quantity);

                lineItems.add(orderDetails);

                i++;
            }

            order.setLineItems(lineItems);

            // Save order
            Boolean isCreated = orderService.createOrder(order);

            if (!isCreated)
            {
                return ResponseEntity.status(500).body("Order Creation Failed");
            }

            return ResponseEntity.ok("Order created succesfully!");
        }

        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }             

    }

    



    @PostMapping(path = "/order", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> createOrderMVM_24(@RequestBody MultiValueMap<String, String> formData) 
    {
        // formData must match names in html form, not the columns in mysql db, snake case names in DB are handled by repo

        try
        {
            // WS25
            String customerName = formData.getFirst("customerName"); // get from dropdown

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            String orderDateStr = formData.getFirst("orderDate"); // return first value
            Date orderDate = sdf.parse(orderDateStr);

            String shipAddress = formData.getFirst("shipAddress");
            String notes = formData.getFirst("notes");
            Double tax = Double.valueOf(formData.getFirst("tax"));

            // // Generate unique orderId using Redis Incr command
            // long orderId = template.opsForValue().increment("orderIdCounter");

            // Create Json Object
            JsonObjectBuilder job = Json.createObjectBuilder()
                // .add("orderId", orderId)
                .add("orderDate", sdf.format(orderDate))
                .add("customerName", customerName)
                .add("shipAddress", shipAddress)
                .add("notes", notes)
                .add("tax", tax);

        
            JsonArrayBuilder jab = Json.createArrayBuilder();
            int i = 0;

            while (formData.containsKey("lineItems[" + i + "].product"))
            {
                String product = formData.getFirst("lineItems[" + i + "].product");
                Double unitPrice = Double.valueOf(formData.getFirst("lineItems[" + i + "].unitPrice"));
                Double discount = Double.valueOf(formData.getFirst("lineItems[" + i + "].discount"));
                Integer quantity = Integer.valueOf(formData.getFirst("lineItems[" + i + "].quantity"));

                // // Generate unique orderDetailsId using Redis Incr command
                // long orderDetailsId = template.opsForValue().increment("orderDetailsIdCounter");

                JsonObject jLineItem = Json.createObjectBuilder()
                                            // .add("id", orderDetailsId)
                                            .add("product", product)
                                            .add("unitPrice", unitPrice)
                                            .add("discount", discount)
                                            .add("quantity", quantity)
                                            .build();

                jab.add(jLineItem);
                i++;
            }

            job.add("lineItems", jab);
            JsonObject jOrder = job.build();

            template.opsForList().leftPush(customerName, jOrder.toString());

            return ResponseEntity.ok("Order published succesfully!");
            }

        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }             

    }
   
    
}
