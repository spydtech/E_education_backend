package com.Eonline.Education.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse {
    private int code;
    private String message;
    private Object payload;
    private boolean status;
    private HttpStatus httpStatus;

    public ApiResponse(int code, String message, Object payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }
    public ApiResponse( Object payload) {
        this.payload = payload;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(String message,boolean status) {
        this.message = message;
        this.status = status;
    }
    public ApiResponse(String message,boolean status, Object payload) {
        this.message = message;
        this.status = status;
        this.payload=payload;
    }

    public ApiResponse(String message, HttpStatus httpStatus) {
        this.message=message;
        this.httpStatus=httpStatus;
    }


}
