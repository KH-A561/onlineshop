package net.thumbtack.school.onlineshop.spring.dto.response.exception;

import lombok.NoArgsConstructor;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.Errors;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
public class ExceptionResponse {
    @NotNull
    private Errors errors;

    public ExceptionResponse(@NotNull Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
