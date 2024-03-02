package com.yeah.filetran.main;

import java.io.File;
import java.util.Date;

public class Util {
    public static String jarPath()  {
        try {
            String resource = Class.forName("com.yeah.filetran.main.Util").getResource("").getPath();

            return new File(resource.substring(6, resource.indexOf("/yfth.jar"))).getAbsolutePath();
        } catch (ClassNotFoundException ignored) {

        }
        return "";
    }
    public static byte[] int2bytes(int n){
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (n >> 24);
        bytes[1] =(byte)(n >> 16);
        bytes[2] =(byte)(n >> 8);
        bytes[3] =(byte)(n);
        return bytes;
    }
    public static int bytes2int(byte[] bytes){
        return (bytes[0]<<24)|(bytes[1]<<16)|(bytes[2]<<8)|(bytes[3]);
    }
    public static void printLog(String msg){
        System.out.printf("[info]%s : %s\n",new Date(),msg);
    }
    public static void printErr(String msg){
        System.out.printf("[info]%s : %s\n",new Date(),msg);
    }
}
