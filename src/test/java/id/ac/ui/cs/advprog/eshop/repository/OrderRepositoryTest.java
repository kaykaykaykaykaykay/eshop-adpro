package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository orderRepository;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();

        List<Product> products1 = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products1.add(product1);

        List<Product> products2 = new ArrayList<>();
        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);
        products2.add(product2);

        orders = new ArrayList<>();
        orders.add(new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products1, 1708560000L, "Safira Sudrajat"));
        orders.add(new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078",
                products1, 1708570000L, "Safira Sudrajat"));
        orders.add(new Order("e334ef40-9eff-4da8-9487-8ee697ecbf1e",
                products2, 1708570000L, "Bambang Sudrajat"));
    }

    @Test
    void testSaveCreate() {
        Order order = orders.get(0);
        Order result = orderRepository.save(order);

        Order findResult = orderRepository.findById(orders.get(0).getId());
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getId(), findResult.getId());
        assertEquals(order.getAuthor(), findResult.getAuthor());
        assertEquals(order.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        Order order = orders.get(0);
        orderRepository.save(order);

        Order newOrder = new Order(order.getId(), order.getProducts(),
                order.getOrderTime(), order.getAuthor(),
                OrderStatus.SUCCESS.name());
        Order updatedOrder = orderRepository.save(newOrder);

        Order findResult = orderRepository.findById(orders.get(0).getId());
        assertEquals(order.getId(), updatedOrder.getId());
        assertEquals(OrderStatus.SUCCESS.name(), findResult.getStatus());
    }

    @Test
    void testFindByIdFound() {
        for (Order order : orders) {
            orderRepository.save(order);
        }
        Order findResult = orderRepository.findById(orders.get(1).getId());
        assertEquals(orders.get(1).getId(), findResult.getId());
        assertEquals(orders.get(1).getAuthor(), findResult.getAuthor());
    }

    @Test
    void testFindByIdNotFound() {
        Order findResult = orderRepository.findById("non-existent-id");
        assertNull(findResult);
    }

    @Test
    void testFindAllByAuthorFound() {
        for (Order order : orders) {
            orderRepository.save(order);
        }
        List<Order> findResult = orderRepository.findAllByAuthor("Safira Sudrajat");
        assertEquals(2, findResult.size());
    }

    @Test
    void testFindAllByAuthorNotFound() {
        for (Order order : orders) {
            orderRepository.save(order);
        }
        // Author name is lowercase — case-sensitive, should not match
        List<Order> findResult = orderRepository.findAllByAuthor("safira sudrajat");
        assertEquals(0, findResult.size());
    }
}