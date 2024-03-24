
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

package com.yeah.filetran.server;

import com.yeah.filetran.util.Util;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import static com.yeah.filetran.util.Util.*;

public class FileTranReceiver {
    private int listenerPort;
    private boolean keepAlive;
    private String savePath;
    private String pwd;

    private static final String DEFAULT_PWD;
    private static final int DEFAULT_PORT;
    private static final boolean DEFAULT_ALIVE;
    private static final String DEFAULT_PATH;
    private int allowCount = 1;

     static {
        Properties properties = new Properties();
        File file = new File(Util.jarPath()+File.separator+"conf"+File.separator+"yftr.conf.xml");
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            properties.loadFromXML(fileInputStream);

        } catch (IOException e) {
            System.out.println("生成配置文件>>>");
            if (!file.getParentFile().exists())file.getParentFile().mkdirs();
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    printErr("创建配置文件失败,没有对"+jarPath()+"路径的读写权限...");
                    System.exit(0x1);
                }
            }
            properties.setProperty("alive","false");
            properties.setProperty("port","4951");
            properties.setProperty("path",new File(Util.jarPath()+File.separator+"r_res").getAbsolutePath());
            properties.setProperty("pwd","yeah");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
                properties.storeToXML(fileOutputStream,"接收端配置文件","utf-8");
                properties.loadFromXML(new FileInputStream(file));
            } catch (IOException exception) {
                printErr("写入配置文件失败,没有对"+jarPath()+"路径的读写权限");
            }

        }
             DEFAULT_ALIVE = Boolean.parseBoolean(properties.getProperty("alive"));
             DEFAULT_PWD = properties.getProperty("pwd");
             DEFAULT_PORT = Integer.parseInt(properties.getProperty("port"));
             DEFAULT_PATH = properties.getProperty("path");
             printLog("初始化文件成功...");
     }


    private static FileTranReceiver fileTranReceiver;


    private FileTranReceiver(){
        this.listenerPort = DEFAULT_PORT;
        this.keepAlive = DEFAULT_ALIVE;
        this.savePath =DEFAULT_PATH;
        this.pwd=DEFAULT_PWD;
    }
    public static FileTranReceiver getInstance(){
        if(fileTranReceiver==null){
            fileTranReceiver = new FileTranReceiver();
        }
        File file = new File(fileTranReceiver.savePath);
        file.mkdirs();
        if(file.exists()) return fileTranReceiver;
        printErr("没有对"+fileTranReceiver.savePath+"的读写权限...");
        System.exit(0x4);
        return null;
    }
    public void loadConfig(String Path){
        Properties properties = new Properties();
        File file = new File(Path);

        try {
            properties.loadFromXML(new FileInputStream(file));
        } catch (IOException e) {
            Util.printErr("加载配置文件失败...");
            System.exit(1);
        }

        keepAlive = Boolean.parseBoolean(properties.getProperty("alive"));
        pwd = properties.getProperty("pwd");
        listenerPort = Integer.parseInt(properties.getProperty("port"));
        savePath = properties.getProperty("path");
        printLog("配置文件加载成功...");
    }


    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setSavePath(String savePath) {
        this.savePath = new File(savePath).getAbsolutePath();
    }
    public void setListenerPort(int listenerPort){
        this.listenerPort=listenerPort;
    }

    public void run(){

        try(
                ServerSocketChannel fileSocketChanel = ServerSocketChannel.open();
        ){
            InetSocketAddress inetSocketAddress = new InetSocketAddress(listenerPort);
            fileSocketChanel.bind(inetSocketAddress);
            System.out.printf("服务启动成功,正在监听%d端口，等待链接>>>\n",listenerPort);
            do {
                SocketChannel accept = fileSocketChanel.accept();
                FileTranReceiverSocket fileTranReceiverSocket = new FileTranReceiverSocket(accept);
                fileTranReceiverSocket.start();
            }while (keepAlive||allowCount!=0);

        }catch (IOException e) {
            if (listenerPort!=4951){
                printErr("启动失败，可能的原因端口"+listenerPort+"被占用");
                System.out.println("是否更换端口?(请输入指定端口号,-1为随机端口): ");
                int input;
                try {
                    input = new Scanner(System.in).nextInt();
                    if (input==-1){
                        setListenerPort();
                    }else if(input>0&&input<65535){
                        setListenerPort(input);
                    }else{
                        System.err.println(("端口号非法..."));
                    }
                } catch (Exception ex) {
                    System.err.println(("非法输入"));
                }


            }else{
                setListenerPort();
            }
            run();
        }
    }

    public void setListenerPort(){
        Random random = new Random();
        int i = random.nextInt(65535 - 1024)+1024;
        while (!checkPort(i)){
            i = random.nextInt(65535 - 1024)+1024;
        }
        this.listenerPort = i;

    }
    private boolean checkPort(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private class FileTranReceiverSocket extends Thread {
        SocketChannel socketChannel;

        private FileTranReceiverSocket(SocketChannel socket) {
            this.socketChannel = socket;
        }

        @Override
        public void run() {
            try {
                ByteBuffer allocate = ByteBuffer.allocate(4);
                socketChannel.read(allocate);
                allocate.flip();
                int headLength = allocate.getInt();
                allocate = ByteBuffer.allocate(headLength);
                socketChannel.read(allocate);
                String header = new String(allocate.array(), StandardCharsets.UTF_8);

                if (header.startsWith("Yeah FILE TRANSMISSION SERVICE:")) {
                    SocketAddress remoteSocketAddress = socketChannel.getRemoteAddress();
                    printLog("来自" + remoteSocketAddress.toString().substring(1) + "的请求头通过...");
                    String[] split = header.split("[=|]");
                    String fileName = split[1];
                    String MD5 = split[3];
                    long fileSize = Long.parseLong(split[5]);
                    FileChannel fileChannel = FileChannel.open(Paths.get(savePath + File.separator + fileName), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
                    allocate = ByteBuffer.allocate(1024 * 1024 * 10);
                    printLog("开始传输文件...");
                    while (fileSize > 0) {
                        int read = socketChannel.read(allocate);
                        if (read < 0) break;
                        allocate.flip();
                        fileChannel.write(allocate);
                        allocate.clear();
                        fileSize -= read;
                    }
                    printLog("传输完成:" + savePath + File.separator + fileName + " 文件的MD5码为" + MD5 + ",请自行核验文件完整性");
                } else if (header.startsWith("Yeah FILE TRANSMISSION SERVICE|")) {
                    if (header.endsWith("CONNECT")) {
                        printLog("来自" + socketChannel.getRemoteAddress().toString().substring(1) + "的首次链接请求通过");
                        byte[] bytes = "YEAH FILE TRANSMISSION SERVICE|PASS".getBytes(StandardCharsets.UTF_8);
                        ByteBuffer wrap = ByteBuffer.wrap(bytes);
                        socketChannel.write(wrap);
                        socketChannel.close();
                        allowCount--;
                    } else if (header.endsWith("PWD=" + pwd)) {
                        printLog("接受到远程停止指令...");
                        System.exit(0);
                    }
//
                }
            } catch (Exception e) {
                printErr("传输出错...");
            }catch (OutOfMemoryError ome){
                try {
                    printLog("来自"+socketChannel.getRemoteAddress().toString().substring(1)+"的错误请求，以拒绝...");
                } catch (IOException e) {
                    printErr("未知连接发送了一条错误请求");
                }
            }
        }
    }
}