package vttp5.paf.day24_25ws.model;

import java.util.Date;
import java.util.List;

public class Order {
    
    private int orderId;

    private Date orderDate;

    private String customerName;

    private String shipAddress;

    private String notes;

    private Double tax;
    
    private List<OrderDetails> lineItems;

    public Order() {
    }

    public Order(int orderId, Date orderDate, String customerName, String shipAddress, String notes, Double tax,
            List<OrderDetails> lineItems) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.shipAddress = shipAddress;
        this.notes = notes;
        this.tax = tax;
        this.lineItems = lineItems;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public List<OrderDetails> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderDetails> lineItems) {
        this.lineItems = lineItems;
    }

    
}
