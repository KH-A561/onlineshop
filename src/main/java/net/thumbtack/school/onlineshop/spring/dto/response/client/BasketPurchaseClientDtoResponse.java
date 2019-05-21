package net.thumbtack.school.onlineshop.spring.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.onlineshop.model.Product;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketPurchaseClientDtoResponse {
    private List<Product> bought;
    private List<Product> remaining;
}
