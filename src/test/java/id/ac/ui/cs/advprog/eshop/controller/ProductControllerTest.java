package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@TestPropertySource(properties = {
        "spring.thymeleaf.enabled=false"
})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @Test
    void testProductListPage() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ProductList"));
    }

    @Test
    void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"));
    }

    @Test
    void testCreateProductPost() throws Exception {
        mockMvc.perform(post("/product/create"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testEditProductForm() throws Exception {
        Product p = new Product("1", "A", 1);

        when(service.findById("1")).thenReturn(p);

        mockMvc.perform(get("/product/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(get("/product/delete/1"))
                .andExpect(status().is3xxRedirection());
    }
}