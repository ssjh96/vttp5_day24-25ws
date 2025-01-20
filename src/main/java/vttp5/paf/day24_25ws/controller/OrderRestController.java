package vttp5.paf.day24_25ws.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import vttp5.paf.day24_25ws.model.Order;
import vttp5.paf.day24_25ws.model.OrderDetails;
import vttp5.paf.day24_25ws.service.OrderService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(path = "/order", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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

    // @PostMapping("/order2")
    // public ResponseEntity<String> createOrder(@RequestBody Order order) {
    //     Boolean isCreated = orderService.createOrder(order);
        
    //     if (!isCreated)
    //     {
    //         return ResponseEntity.status(500).body("Order creation failed..");
    //     }
    
    //     return ResponseEntity.ok("Order created succesfully!");
    // }

   
    
}
