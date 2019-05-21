package net.thumbtack.school.onlineshop.spring.dto.request.administrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditAccountAdminDtoRequest implements Serializable {
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
    private String position;

    @NotBlank
    @Size(min = 8)
    private String oldPassword;

    @NotBlank
    @Size(min = 8)
    private String newPassword;
}
