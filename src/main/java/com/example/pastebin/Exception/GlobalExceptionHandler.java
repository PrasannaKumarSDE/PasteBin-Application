package com.example.pastebin.Exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleRuntime(RuntimeException ex) {
        return Map.of(
                "error", ex.getMessage()
        );
    }
}
