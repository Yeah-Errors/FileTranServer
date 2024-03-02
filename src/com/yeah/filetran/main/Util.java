/*
 * Copyright (c) 2023 Yeah-Errors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * *You may not use the Software for any commercial purposes.*
 */

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
