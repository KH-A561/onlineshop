package net.thumbtack.school.onlineshop.spring.handler.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Errors {
    @NotNull
    private List<ServerError> errors;

    public void addError(ServerError error) {
        errors.add(error);
    }
}
