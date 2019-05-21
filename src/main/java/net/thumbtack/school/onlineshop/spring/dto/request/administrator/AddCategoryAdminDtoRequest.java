package net.thumbtack.school.onlineshop.spring.dto.request.administrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryAdminDtoRequest {
    @NotBlank
    private String name;
    private Integer parentId;

    public AddCategoryAdminDtoRequest(@NotBlank String name) {
        this.name = name;
    }
}
