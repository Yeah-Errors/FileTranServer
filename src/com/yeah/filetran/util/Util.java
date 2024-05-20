/*
 * Copyright (c) 2023 Yeah-Errors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package com.yeah.filetran.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Date;

public class Util {
    public static String jarPath()  {

            String Path = Util.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String parsePath = Path.substring(0, Path.lastIndexOf("/"));

            return new File(parsePath).getAbsolutePath();

    }
    public static void printLog(String msg){
        System.out.printf("[info]%s : %s\n",new Date(),msg);
    }
    public static void printErr(String msg){
        System.out.printf("[info]%s : %s\n",new Date(),msg);
    }
    public static String getFileMD5(File file){
        String md5 = "";
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[8192];
            int length;
            while((length = fileInputStream.read(bytes))!=-1){
                MD5.update(bytes,0,length);
            }
            byte[] digest = MD5.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest) {
                stringBuilder.append(String.format("%02x",b));
            }
            md5 = stringBuilder.toString();
        } catch (Exception ignored) {

        }
        return md5;
    }
}
