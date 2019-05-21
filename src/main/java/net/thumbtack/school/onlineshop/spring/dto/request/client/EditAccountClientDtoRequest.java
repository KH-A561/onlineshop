package net.thumbtack.school.onlineshop.spring.dto.request.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAccountClientDtoRequest {
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
    @Size(min = 8)
    private String oldPassword;

    @NotBlank
    @Size(min = 8)
    private String newPassword;
}
