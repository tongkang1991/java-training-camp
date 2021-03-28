package com.homework.week02.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer01 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(8802);
        while (true){
            try{
                Socket socket=serverSocket.accept();
                service(socket);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static void service(Socket socket){
        PrintWriter printWriter=null;
        try{
            printWriter=new PrintWriter(socket.getOutputStream(),true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:application/json;charset=utf-8");
            String body="hello";
            printWriter.println("Content-Length:"+body.getBytes().length);
            printWriter.println();
            printWriter.write(body);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            printWriter.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
