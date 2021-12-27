package com.example.review.config;

import lombok.Getter;

@Getter
public class TripleException extends RuntimeException {

    private Code code;

    public TripleException(Code code) {
        super(code.getMessage());
        this.code = code;
    }
}
