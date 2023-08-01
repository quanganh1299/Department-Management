package com.vti.configuration.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ErrorResponse {
    private String timestamp = LocalDate.now().toString();
    private String message;
    private Object errors;
    private int code;
    private String moreInformation;
    private String detailMessage;

    public ErrorResponse(int code, String message, String detailMessage) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
        this.moreInformation = "http://localhost:8080/api/v1/exceptions/" + code; //giả lập đường dẫn
    }

    public ErrorResponse(int code, String message, String detailMessage, Object errors) {
        this(code, message, detailMessage); // dùng constructor trên
        this.errors = errors; //kèm với errors thêm
    }
}


