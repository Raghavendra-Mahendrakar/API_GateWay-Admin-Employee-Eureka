package com.pws.employee;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiSuccess {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private Object data;

    private ApiSuccess() {
        timestamp = LocalDateTime.now();
    }

    public ApiSuccess(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiSuccess(HttpStatus status, Object object) {
        this();
        this.status = status;
        this.data = object;
    }

}

