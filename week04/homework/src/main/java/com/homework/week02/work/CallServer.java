package com.homework.week02.work;

import com.homework.common.util.HttpClientUtils;

public class CallServer {
    public static void main(String[] args) {
        try {
            String s = HttpClientUtils.sendGet("http://localhost:8802", "utf-8");
            System.out.println("返回结果:="+s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
