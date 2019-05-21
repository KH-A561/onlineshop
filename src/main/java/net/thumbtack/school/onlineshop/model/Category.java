package net.thumbtack.school.onlineshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int id;
    private String name;
    private Category parentCategory;
    private List<Category> subcategories;

    public Category(int id, String name, Category parentCategory) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
        subcategories = null;
    }

    public Category(String name, Category parentCategory) {
        this.id = 0;
        this.name = name;
        this.parentCategory = parentCategory;
        subcategories = null;
    }

    public Category(int id) {
        this.id = id;
        this.name = null;
        this.parentCategory = null;
        subcategories = null;
    }

    public Category(String id) {
        this.id = Integer.valueOf(id);
        this.name = null;
        this.parentCategory = null;
        subcategories = null;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.parentCategory = null;
        subcategories = null;
    }

    public Category(int id, String name, List<Category> subcategories) {
        this.id = id;
        this.name = name;
        this.parentCategory = null;
        this.subcategories = subcategories;
    }



    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }
}
