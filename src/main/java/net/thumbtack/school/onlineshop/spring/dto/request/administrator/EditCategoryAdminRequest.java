package net.thumbtack.school.onlineshop.spring.dto.request.administrator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditCategoryAdminRequest {
    private String name;
    private Integer parentId;
}
