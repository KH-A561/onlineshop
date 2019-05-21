package net.thumbtack.school.onlineshop.spring.dto.request.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketPurchaseClientDtoRequest {
    @NotBlank
    private List<ProductClientDtoRequest> products;
}
