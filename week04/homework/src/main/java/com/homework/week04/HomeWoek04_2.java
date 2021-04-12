package com.homework.week04;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class HomeWoek04_2 {

    public static void main(String[] args) {

        final CountDownLatch latch = new CountDownLatch(1);
        final int[] result = {0};
        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("子线程执行...");
                    result[0] = sum(20);
                    latch.countDown();//让latch中的值减1
                }
            }).start();
        }
        try {
            latch.await();//阻塞当前线程，直到latch的值为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程执行......");
        System.out.println("异步计算结果为："+result[0]);
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