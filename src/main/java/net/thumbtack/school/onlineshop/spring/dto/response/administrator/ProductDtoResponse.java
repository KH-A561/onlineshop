package net.thumbtack.school.onlineshop.spring.dto.response.administrator;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDtoResponse<T> {
    private int id;
    private String name;
    private int price;
    private Integer count;
    private List<T> categories;
    private Integer profit;

    public ProductDtoResponse(int id, String name, int price, Integer count, List<T> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
    }
}
