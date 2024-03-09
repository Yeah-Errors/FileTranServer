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

package com.yeah.filetran.client;

import com.yeah.filetran.util.Util;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Properties;

public class FileTranSender {
    private static final String HEADER ="Yeah FILE TRANSMISSION SERVICE";
    private static final int DEFAULT_REMOTE_PORT;

    private static final String DEFAULT_REMOTE_HOST;

    private static FileTranSender fileTranSender;
    private String remoteHost;

    private int remotePort;
    static {
        Properties properties = new Properties();

        File file = new File(Util.jarPath()+File.separator+"conf"+File.separator+"yfts.conf.xml");
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            properties.loadFromXML(new FileInputStream(file));

        } catch (IOException e) {
            if (!file.getParentFile().exists())file.getParentFile().mkdirs();
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    System.err.println("没有权限...");
                }
            }
            properties.setProperty("host","localhost");
            properties.setProperty("port","4951");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
                properties.storeToXML(fileOutputStream,"文件发送配置文件","utf-8");
                properties.loadFromXML(new FileInputStream(file));
            } catch (IOException ignored) {

            }
        }
        DEFAULT_REMOTE_HOST = properties.getProperty("host");
        DEFAULT_REMOTE_PORT = Integer.parseInt(properties.getProperty("port"));
    }
    private FileTranSender(){
        this.remoteHost=DEFAULT_REMOTE_HOST;
        this.remotePort=DEFAULT_REMOTE_PORT;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
    public static FileTranSender getInstance(){
        if(fileTranSender==null){
            fileTranSender = new FileTranSender();
        }
        return fileTranSender;
    }
    public void run(File file){
        try (Socket socket = new Socket(remoteHost, remotePort)) {
            OutputStream outputStream = socket.getOutputStream();

            String FILE_NAME = file.getName();
            String MD5 = Util.getFileMD5(file);
            int length = (HEADER + ":FILE_NAME="+FILE_NAME + "|MD5=" + MD5).getBytes(StandardCharsets.UTF_8).length;
            outputStream.write(Util.int2bytes(length));
            outputStream.write((HEADER + ":FILE_NAME="+FILE_NAME + "|MD5=" + MD5).getBytes(StandardCharsets.UTF_8));

            outputStream.write(file2Byte(file));
            outputStream.flush();
            outputStream.close();

        }catch (Exception ex){
            System.out.println("连接异常，请检测远端地址是否正确，或本地dns是否正常解析至远端设备");
        }
    }
    public void remoteShut(String pwd){
        try(Socket socket = new Socket(remoteHost, remotePort)){
            OutputStream outputStream = socket.getOutputStream();
            int length = ((HEADER+"|STOP SERVICE|PWD="+pwd).getBytes(StandardCharsets.UTF_8)).length;
            outputStream.write(Util.int2bytes(length));
            outputStream.write((HEADER+"|STOP SERVICE|PWD="+pwd).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        }catch(Exception e){
            System.out.println("失败");
        }
    }

    public static byte[] file2Byte(File file){

        byte[] byteArray;
        try (FileInputStream fileInputStream = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int i;
            byte[] bytes = new byte[1024];
            while((i=fileInputStream.read(bytes))!=-1){
                bos.write(bytes,0, i);
            }
            byteArray = bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArray;
    }
}
