package com.homework.test;

import java.util.Base64;

public class HelloClassLoader extends ClassLoader{
    public static void main(String[] args) {

        try{
            new HelloClassLoader().findClass("com.homework.test.Hello").newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        String base64="yv66vgAAADQAHwoABgARCQASABMIABQKABUAFgcAFwcAGAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAZTGNvbS9ob21ld29yay90ZXN0L0hlbGxvOwEACDxjbGluaXQ+AQAKU291cmNlRmlsZQEACkhlbGxvLmphdmEMAAcACAcAGQwAGgAbAQAGaGVsbG8gBwAcDAAdAB4BABdjb20vaG9tZXdvcmsvdGVzdC9IZWxsbwEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYAIQAFAAYAAAAAAAIAAQAHAAgAAQAJAAAALwABAAEAAAAFKrcAAbEAAAACAAoAAAAGAAEAAAADAAsAAAAMAAEAAAAFAAwADQAAAAgADgAIAAEACQAAACUAAgAAAAAACbIAAhIDtgAEsQAAAAEACgAAAAoAAgAAAAUACAAGAAEADwAAAAIAEA==";
        byte[] bytes=decode(base64);

        return defineClass(name,bytes,0,bytes.length);
    }
    public byte[] decode(String base64){
        return Base64.getDecoder().decode(base64);
    }
}
