package vttp5.paf.day24_25ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp5.paf.day24_25ws.model.Order;
import vttp5.paf.day24_25ws.model.OrderDetails;
import vttp5.paf.day24_25ws.repository.OrderDetailsRepo;
import vttp5.paf.day24_25ws.repository.OrderRepo;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailsRepo orderDetailsRepo;

    @Transactional
    public void createOrder(Order order)
    {
        // Get order_id key
        int orderId = orderRepo.insertOrder(order);

        List<OrderDetails> lineItems = order.getLineItems();
    }
}
