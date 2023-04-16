package org.collignon.restapi.controller;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import org.collignon.restapi.exception.NotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ResponseBody
@ControllerAdvice
@ConditionalOnClass(RestController.class)
public class CoreExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorMessage handleNotFoundException(NotFoundException e) {
        return new ErrorMessage(e.getMessage(), "NOT_FOUND");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MissingFieldErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var fields = e.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        it -> ((FieldError) it).getField(),
                        it -> Optional.ofNullable(it.getDefaultMessage()).orElse("")
                ));
        return new MissingFieldErrorMessage("MISSING_FIELD", fields);
    }



    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(MongoWriteException.class)
    public ErrorMessage handleMongoWriteException(MongoWriteException e) {
        if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
            return new ErrorMessage(e.getMessage(), "DUPLICATE_KEY");
        } else {
            throw e;
        }
    }

    public record ErrorMessage(String message, String code) {
    }

    public record MissingFieldErrorMessage(String code, Map<String, String> fields) {
    }
}

