package com.neosoft.transactionalexample.utils;

import com.neosoft.transactionalexample.exception.InsufficientFundException;

import java.util.HashMap;
import java.util.Map;

public class PaymentUtil
{
    private static Map<String, Double> paymentMap = new HashMap<>();

    static {
        paymentMap.put("acc1", 10000.00);
        paymentMap.put("acc2", 20000.00);
        paymentMap.put("acc3", 15000.00);
        paymentMap.put("acc4", 18000.00);
    }

    public static  Boolean validateCreditLimit(String accNo, double paidAmount)
    {
        if(paidAmount > paymentMap.get(accNo))
        {
            throw new InsufficientFundException("Insufficient balance");
        }
        else return true;
    }
}