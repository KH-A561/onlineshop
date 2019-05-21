package net.thumbtack.school.onlineshop.spring.handler;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.school.onlineshop.spring.dto.response.exception.ExceptionResponse;
import net.thumbtack.school.onlineshop.spring.handler.exception.ServerException;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ServerError;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;
import net.thumbtack.school.onlineshop.spring.handler.exception.error.Errors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ExceptionResponse handleFieldValidation (MethodArgumentNotValidException e) {
        log.error("Error {}", e.getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.setErrors(new Errors(new ArrayList<>()));
        e.getBindingResult().getFieldErrors().forEach(er -> response.getErrors().addError(new ServerError(ErrorCode.INVALID_REQUEST_ARGUMENT, er.getField(), er.getDefaultMessage())));
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ExceptionResponse handleBodyValidatonNotReadable (HttpMessageNotReadableException e) {
        log.error("Error {}", e.getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.setErrors(new Errors(new ArrayList<>()));
        ErrorCode.REQUEST_BODY_NOT_READABLE.setErrorDescription(e.getCause().getMessage());
        response.getErrors().addError(new ServerError(ErrorCode.REQUEST_BODY_NOT_READABLE,
                                                ErrorCode.REQUEST_BODY_NOT_READABLE.getErrorDescription(),
                                                ErrorCode.REQUEST_BODY_NOT_READABLE.getField()));
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class,
                       MissingServletRequestPartException.class,
                       IllegalArgumentException.class})
    @ResponseBody
    public ExceptionResponse handleBodyValidationInvalidParameters() {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrors(new Errors(new ArrayList<>()));
        response.getErrors().addError(new ServerError(ErrorCode.REQUEST_BODY_INVALID_PARAMETERS,
                                                ErrorCode.REQUEST_BODY_INVALID_PARAMETERS.getErrorDescription(),
                                                ErrorCode.REQUEST_BODY_INVALID_PARAMETERS.getField()));
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ExceptionResponse handleNoHandler (NoHandlerFoundException e) {
        log.error("Error {}", e.getMessage());
        ExceptionResponse response = new ExceptionResponse();
        response.setErrors(new Errors(new ArrayList<>()));
        ErrorCode.HANDLER_NOT_FOUND.setErrorDescription("Page " + e.getRequestURL() + ", " + e.getHttpMethod() + "not found");
        response.getErrors().addError(new ServerError(ErrorCode.REQUEST_BODY_INVALID_PARAMETERS,
                                                ErrorCode.REQUEST_BODY_INVALID_PARAMETERS.getErrorDescription(),
                                                ErrorCode.REQUEST_BODY_INVALID_PARAMETERS.getField()));
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ExceptionResponse handleInternalServerError () {
        log.error("Internal Server Error has occurred");
        ExceptionResponse response = new ExceptionResponse();
        response.setErrors(new Errors(new ArrayList<>()));
        response.getErrors().addError(new ServerError(ErrorCode.INTERNAL_ERROR,
                                          ErrorCode.INTERNAL_ERROR.getErrorDescription(),
                                          ErrorCode.INTERNAL_ERROR.getField()));
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerException.class)
    @ResponseBody
    public ExceptionResponse handleException(ServerException e) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrors(new Errors(new ArrayList<>()));
        response.getErrors().addError(new ServerError(e.getErrorCode(), e.getErrorCode().getField(),
                                                      e.getErrorCode().getErrorDescription()));
        return response;
    }
}
