package net.thumbtack.school.onlineshop.spring.dto.request.administrator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdminDtoRequest implements Serializable {
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
    private String position;

    @NotBlank
    @Pattern(regexp = "[A-Za-zА-Яа-я0-9]+")
    @Size(max = 50)
    private String login;

    @NotBlank
    @Size(min = 8)
    private String password;
}
