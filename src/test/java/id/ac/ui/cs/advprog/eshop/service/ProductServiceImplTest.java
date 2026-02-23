package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

class ProductServiceImplTest {

    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAssignsId() {
        Product product = new Product();

        when(repository.create(any())).thenReturn(product);

        Product result = service.create(product);

        assertNotNull(result.getProductId());
        verify(repository).create(product);
    }

    @Test
    void testFindAll() {
        Product p = new Product("1","A",1);

        Iterator<Product> iterator =
                List.of(p).iterator();

        when(repository.findAll()).thenReturn(iterator);

        List<Product> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        Product p = new Product("1","A",1);

        when(repository.findById("1")).thenReturn(p);

        assertEquals(p, service.findById("1"));
    }

    @Test
    void testUpdate() {
        Product p = new Product("1","A",1);

        service.update(p);

        verify(repository).update(p);
    }

    @Test
    void testDeleteById() {
        service.deleteById("1");

        verify(repository).deleteById("1");
    }
}