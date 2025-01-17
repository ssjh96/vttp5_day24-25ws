package vttp5.paf.day24_25ws.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp5.paf.day24_25ws.model.Order;
import vttp5.paf.day24_25ws.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class OrderController 
{
    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        Boolean isCreated = orderService.createOrder(order);
        
        if (!isCreated)
        {
            return ResponseEntity.status(500).body("Order creation failed..");
        }
    
        return ResponseEntity.ok("Order created succesfully!");
    }
    
}
