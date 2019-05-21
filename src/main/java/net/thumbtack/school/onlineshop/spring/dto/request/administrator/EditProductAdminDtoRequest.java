package net.thumbtack.school.onlineshop.spring.dto.request.administrator;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.onlineshop.model.Category;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
public class EditProductAdminDtoRequest {
    private String name;
    @Min(value = 1)
    private Integer price;
    @PositiveOrZero
    private Integer count;
    private List<Category> categories;
}
