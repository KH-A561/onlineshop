package net.thumbtack.school.onlineshop.spring.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class LogInDtoRequest {
    @NotBlank
    @Pattern(regexp = "[A-Za-zА-Яа-я0-9]+")
    @Size(max = 50)
    private String login;

    @NotBlank
    @Size(min = 8)
    private String password;


}
