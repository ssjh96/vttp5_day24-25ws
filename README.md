# vttp5_day24-25ws
{
    "orderDate": "2025-01-17",
    "customerName": "Jane Doe",
    "shipAddress": "456 Oak Street",
    "notes": "Gift wrap this order",
    "tax": 0.07,
    "lineItems": [
        {
            "product": "Product A",
            "unitPrice": 10.00,
            "discount": 1.00,
            "quantity": 2
        },
        {
            "product": "Product B",
            "unitPrice": 15.00,
            "discount": 0.95,
            "quantity": 3
        }
    ]
}

1. Reset Auto-Increment Counter with TRUNCATE
If you are okay with deleting all data in the table:
TRUNCATE TABLE orders;

This removes all rows from the table and resets the auto-increment counter to 1.
This is the easiest and cleanest way to reset the counter.
Caution: TRUNCATE is irreversible and will delete all data in the table.

2. Reset Auto-Increment Counter Manually
If you want to reset the counter without deleting existing data:
Find the current maximum value of the order_id:
SELECT MAX(order_id) FROM orders;

Set the next auto-increment value manually:
ALTER TABLE orders AUTO_INCREMENT = <next_value>;

Replace <next_value> with the desired starting point (e.g., MAX(order_id) + 1).
ALTER TABLE orders AUTO_INCREMENT = 8;
