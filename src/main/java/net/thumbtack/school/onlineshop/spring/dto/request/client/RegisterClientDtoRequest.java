package net.thumbtack.school.onlineshop.spring.dto.request.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class RegisterClientDtoRequest implements Serializable {
    //max_name_length
    //min_password_length
    //config file
    @NotBlank
    @Pattern(regexp = "[А-Яа-я -]+")
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Pattern(regexp = "[А-Яа-я -]+")
    @Size(max = 50)
    private String lastName;

    @Pattern(regexp = "[А-Яа-я -]+")
    @Size(max = 50)
    private String patronymic;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    @Pattern(regexp = "^((8|\\+7)[\\-]?)(9\\d{2}[\\-]?)(\\d{3}[\\-]?)(\\d{2}[\\-]?)(\\d{2})")
    private String phone;

    @NotBlank
    @Pattern(regexp = "[A-Za-zА-Яа-я0-9]+")
    @Size(max = 50)
    private String login;

    @NotBlank
    @Size(min = 8)
    private String password;
}
