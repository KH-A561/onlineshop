package net.thumbtack.school.onlineshop.spring.dto.response.administrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.thumbtack.school.onlineshop.model.Administrator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDtoResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
}
