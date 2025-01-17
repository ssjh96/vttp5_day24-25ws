drop database if exists d24;

create database d24;
use d24;

create table orders (
    order_id int auto_increment,
    order_date date not null,
    customer_name varchar(128) not null,
    ship_address varchar(128) not null,
    notes text not null,
    tax decimal(2,2) default 0.05,

    constraint pk_order_id primary key(order_id)
);

create table order_details (
    id int auto_increment,
    product varchar(64) not null,
    unit_price decimal(3,2) not null,
    discount decimal(3,2) default 1.00,
    quantity int not null,
    
    order_id int not null,

    constraint pk_id primary key(id),
    constraint fk_order_id foreign key(order_id) references orders(order_id)
);

grant all privileges on d24.* to 'fred'@'%';
flush privileges;