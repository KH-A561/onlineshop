package net.thumbtack.school.onlineshop.spring.dto.request.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductClientDtoRequest {
    @NotNull
    private int id;

    @NotBlank
    private String name;

    @Min(value = 1)
    private int price;

    private Integer count;
}
