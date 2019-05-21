package net.thumbtack.school.onlineshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    private int id;
    private Client client;
    private Product product;
    private String name;
    private int price;
    private Integer count;

    public Purchase(Client client, Product product, String name, int price, Integer count) {
        this.id = 0;
        this.client = client;
        this.product = product;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public Purchase(int id, Product product, String name, int price, Integer count) {
        this.id = 0;
        this.client = null;
        this.product = product;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
