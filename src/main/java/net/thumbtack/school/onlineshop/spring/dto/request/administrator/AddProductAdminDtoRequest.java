package net.thumbtack.school.onlineshop.spring.dto.request.administrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.onlineshop.model.Category;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductAdminDtoRequest {
    @NotBlank
    private String name;
    @Min(value = 1)
    private int price;
    @PositiveOrZero
    private Integer count;
    private List<Integer> categories;
}
