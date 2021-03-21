package com.homework.work01;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader{
    public static void main(String[] args) {

        try{
            Object hello = new HelloClassLoader().findClass("Hello").newInstance();
            Class<?> aClass = hello.getClass();
            Method method = aClass.getMethod("hello");
            Object obj1 = method.invoke(aClass.newInstance());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        //读取本地文件
        String filePath="C:\\Users\\李仝康\\Desktop\\java进阶\\第一周\\作业相关\\Hello\\Hello.xlass";
        //转换为字节数组
        byte[] bytesByFile = getBytesByFile(filePath);
        for (int i=0;i<bytesByFile.length;i++){
            bytesByFile[i]=(byte)(255-bytesByFile[i]);
        }
        return defineClass(name,bytesByFile,0,bytesByFile.length);
    }
    //将文件转换成Byte数组
    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
