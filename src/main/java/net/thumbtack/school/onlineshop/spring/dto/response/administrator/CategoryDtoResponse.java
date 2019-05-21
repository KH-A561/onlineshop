package net.thumbtack.school.onlineshop.spring.dto.response.administrator;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDtoResponse {
    private int id;
    private String name;
    private Integer parentId;
    private String parentName;
}
