package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {

    private String productId;
    private String productName;
    private int productQuantity;

    public Product() {
    }

    public Product(String id, String name, int quantity) {
        this.productId = id;
        this.productName = name;
        this.productQuantity = quantity;
    }
}
