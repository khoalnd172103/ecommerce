package com.ecommerce.library.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseException extends RuntimeException {

    private String code;

    private String message;
}
