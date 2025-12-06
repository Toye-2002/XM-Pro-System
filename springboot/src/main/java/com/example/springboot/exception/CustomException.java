package com.example.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomException extends RuntimeException {
    private String code;
    private String msg;

}
