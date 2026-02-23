package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ProductTest {

    Product product;

    @BeforeEach
    void setUp() {
        this.product = new Product();
        this.product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        this.product.setProductName("Sampo Cap Bambang");
        this.product.setProductQuantity(100);
    }

    @Test
    void testGetProductId() {
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", this.product.getProductId());
    }

    @Test
    void testGetProductName() {
        assertEquals("Sampo Cap Bambang", this.product.getProductName());
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(100, this.product.getProductQuantity());
    }

    @Test
    void testEmptyConstructor() {
        Product product = new Product();

        product.setProductId("1");
        product.setProductName("Laptop");
        product.setProductQuantity(10);

        assertEquals("1", product.getProductId());
        assertEquals("Laptop", product.getProductName());
        assertEquals(10, product.getProductQuantity());
    }

    @Test
    void testParameterizedConstructor() {
        Product product = new Product("2", "Mouse", 5);

        assertEquals("2", product.getProductId());
        assertEquals("Mouse", product.getProductName());
        assertEquals(5, product.getProductQuantity());
    }
}
