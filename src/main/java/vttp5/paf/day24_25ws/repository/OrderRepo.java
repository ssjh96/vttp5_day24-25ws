package vttp5.paf.day24_25ws.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import vttp5.paf.day24_25ws.model.Order;

@Repository
public class OrderRepo 
{
    @Autowired
    JdbcTemplate template;
    
    private static final String INSERT_ORDER_SQL = 
        """
            INSERT INTO orders 
                (order_date, customer_name, ship_address, notes, tax)
            VALUES
                (?, ?, ?, ?, ?)
        """;    
        // orderId : not required since DB generates a unique value for order_id column when a new row is inserted (due to AUTO_INCREMENT property)

        // List<OrderDetails> has a FK (order_id) REF orders(order_id)
        // To insert data into order_details:
            // order_id from orders table is required
            // it ensures order_details rows are linked to the correct orders row
        // Insertion of orderdetails happens in a separate query after order_id is generated

    public int insertOrder(Order order)
    {
        // KeyHolder for capturing the generated key
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                PreparedStatement ps = con.prepareStatement(
                        INSERT_ORDER_SQL, // SQL QUery
                        new String[]{"order_id"}); // Column to capture auto-generated key

                // Set values for placeholders(?)
                // Need to convert java.util.date to java.sql.date
                java.util.Date utilDate = order.getOrderDate();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                ps.setDate(1, sqlDate);
                ps.setString(2, order.getCustomerName());
                ps.setString(3, order.getShipAddress());
                ps.setString(4, order.getNotes());
                ps.setDouble(5, order.getTax());

                return ps;
            }
            
        };

        // Execute insertion and capture the generated key
        int rowsInserted = template.update(psc, keyHolder);

        // return generated order_id
        return keyHolder.getKey().intValue();

        // int rowsInserted = template.update(INSERT_ORDER_SQL, 
        //                                     order.getOrderDate(),
        //                                     order.getCustomerName(),
        //                                     order.getShipAddress(),
        //                                     order.getNotes(),
        //                                     order.getTax());

        // return rowsInserted;
    }
}
