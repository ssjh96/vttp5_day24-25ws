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
    public Boolean createOrder(Order order)
    {
        // Insert order (order_date, customer_name, ship_address, notes, tax) and get the pri key (order_id) back
        int orderId = orderRepo.insertOrder(order);

        // Insert order details (that are linked to order_id
        List<OrderDetails> lineItems = order.getLineItems();
        int rowsInserted = orderDetailsRepo.insertOrderDetails(orderId, lineItems);

        if (rowsInserted != lineItems.size())
        {
            return false;
        }

        return true;
    }
}
