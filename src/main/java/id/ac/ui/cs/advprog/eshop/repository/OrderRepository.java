package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {
    private List<Order> orderData = new ArrayList<>();

    // ← ADD THE IMPLEMENTATIONS HERE, replacing the empty skeleton methods

    public Order save(Order order) {
        int index = -1;
        for (int i = 0; i < orderData.size(); i++) {
            if (orderData.get(i).getId().equals(order.getId())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            orderData.set(index, order);
        } else {
            orderData.add(order);
        }
        return order;
    }

    public Order findById(String id) {
        for (Order order : orderData) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public List<Order> findAllByAuthor(String author) {
        List<Order> result = new ArrayList<>();
        for (Order order : orderData) {
            if (order.getAuthor().equals(author)) {
                result.add(order);
            }
        }
        return result;
    }
}