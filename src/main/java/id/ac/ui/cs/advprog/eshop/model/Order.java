package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class Order {
    private String id;
    private List<Product> products;
    private Long orderTime;
    private String author;
    private String status;

    public Order(String id, List<Product> products, Long orderTime, String author) {
        this(id, products, orderTime, author, OrderStatus.WAITING_PAYMENT.name());
    }

    public Order(String id, List<Product> products, Long orderTime, String author, String status) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one product");
        }
        this.id = id;
        this.products = products;
        this.orderTime = orderTime;
        this.author = author;

        if (OrderStatus.contains(status)) {
            this.status = status;
        } else {
            this.status = OrderStatus.WAITING_PAYMENT.name();
        }
    }

    public void setStatus(String status) {
        if (OrderStatus.contains(status)) {
            this.status = status;
        }
    }
}