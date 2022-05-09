package com.example.demo.services;

import com.example.demo.common.Result;

public interface CalcService {
    Result add(int a, int b);

    Result minus(int a, int b);

    Result multi(int a, int b);

    Result divide(int a, int b);
}
