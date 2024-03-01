package com.neosoft.transactionalexample.exception;

public class InsufficientFundException extends RuntimeException
{
    public InsufficientFundException(String msg)
    {
        super(msg);
    }
}
