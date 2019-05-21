package net.thumbtack.school.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    private int id;
    private String name;
    private int price;
    private Integer count;
    private List<Category> categories;
    private Integer version;

    public Product(int id) {
        this.id = id;
        this.name = null;
        this.price = 0;
        this.count = 0;
        categories = null;
        version = 0;
    }

    public Product(String name, int price, Integer count) {
        this.id = 0;
        this.name = name;
        this.price = price;
        this.count = count;
        categories = null;
        version = 0;
    }

    public Product(int id, String name, int price, Integer count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = null;
        version = 0;
    }

    public Product(int id, String name, int price, Integer count, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
        version = 0;
    }

    public Product(int id, String name, int price, Integer count, Integer version) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = null;
        this.version = version;
    }

    public Product(int id, int price, Integer count) {
        this.id = id;
        this.price = price;
        this.count = count;
    }

    public void increaseCount(Integer amount) {
        this.count += amount;
    }

    public void increasePrice(Integer amount) {
        this.price += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getId() == product.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getCount(), getCategories());
    }
}
