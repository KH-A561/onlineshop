package net.thumbtack.school.onlineshop.spring.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseClientDtoResponse {
    private int id;

    private String name;

    private int price;

    private Integer count;
}
