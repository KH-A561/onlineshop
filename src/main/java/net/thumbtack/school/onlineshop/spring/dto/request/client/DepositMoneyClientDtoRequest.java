package net.thumbtack.school.onlineshop.spring.dto.request.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositMoneyClientDtoRequest {
    @Min(value = 1)
    private int deposit;
}
