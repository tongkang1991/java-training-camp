package com.homework.week04;

import java.util.concurrent.Callable;

/**
 * @author ltk
 * @date 2021/4/12
 */
public class Task implements Callable<Integer> {
    private Integer number;
    public Task(Integer number){
        this.number=number;
    }
    @Override
    public Integer call() throws Exception {
        int sum = sum(number);
        return sum;
    }
    private static int sum(Integer number) {
        return fibo(number);
    }

    private static int fibo(int a) {
        if ( a < 2) {
            return 1;
        }else{
            return fibo(a-1) + fibo(a-2);
        }
    }
}