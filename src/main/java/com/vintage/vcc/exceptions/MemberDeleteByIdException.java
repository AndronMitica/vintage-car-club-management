package com.vintage.vcc.exceptions;

public class MemberDeleteByIdException extends RuntimeException{

    public MemberDeleteByIdException(String message) {
        super(message);
    }
}