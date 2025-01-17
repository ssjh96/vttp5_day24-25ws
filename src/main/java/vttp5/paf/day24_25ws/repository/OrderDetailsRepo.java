package vttp5.paf.day24_25ws.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp5.paf.day24_25ws.model.OrderDetails;

@Repository
public class OrderDetailsRepo {

    @Autowired
    JdbcTemplate template;

    private static final String INSERT_ORDER_DETAILS_SQL =
        """
            INSERT INTO order_details 
                (product, unit_price, discount, quantity, order_id)
            VALUES
                (?, ?, ?, ?, ?)        
        """;

    public int insertOrderDetails(int orderId, List<OrderDetails> lineItems)
    {
        int rowsInserted = 0;

        for (OrderDetails orderDetails : lineItems)
        {
            // Increment rowInserted counter for every successful insertion
            rowsInserted += template.update(INSERT_ORDER_DETAILS_SQL, 
            orderDetails.getProduct(),
            orderDetails.getUnitPrice(),
            orderDetails.getDiscount(),
            orderDetails.getQuantity(),
            orderId); 
        }

        return rowsInserted;
    }
    
}
