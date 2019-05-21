package net.thumbtack.school.onlineshop.spring.handler.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerError {
    @NotNull
    private ErrorCode errorCode;
    private String field;
    private String message;
}
