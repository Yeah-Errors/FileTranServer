
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

package com.yeah.filetran.server;

import com.yeah.filetran.main.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import static com.yeah.filetran.main.Util.printErr;
import static com.yeah.filetran.main.Util.printLog;

public class FileTranReceiver {
    private int listenerPort;
    private boolean keepAlive;
    private String savePath;
    private static final String pwd;
    private static final int DEFAULT_PORT;
    private static final boolean DEFAULT_ALIVE;
    private static final String DEFAULT_PATH;
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
                    System.err.println("没有权限...");
                }
            }

            properties.setProperty("alive","false");
            properties.setProperty("port","4951");
            properties.setProperty("path",new File(Util.jarPath()+File.separator+"r_res").getAbsolutePath());
            properties.setProperty("pwd","yeah");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
                properties.storeToXML(fileOutputStream,"接收端配置文件","utf-8");
                properties.loadFromXML(new FileInputStream(file));
            } catch (IOException ignored) {

            }


        }
        DEFAULT_ALIVE = Boolean.parseBoolean(properties.getProperty("alive"));
        pwd = properties.getProperty("pwd");
        DEFAULT_PORT = Integer.parseInt(properties.getProperty("port"));
        DEFAULT_PATH = properties.getProperty("path");
        System.out.println(DEFAULT_PATH);
    }

    private static FileTranReceiver fileTranReceiver;


    private FileTranReceiver(){
        this.listenerPort = DEFAULT_PORT;
        this.keepAlive = DEFAULT_ALIVE;
        this.savePath =DEFAULT_PATH;
    }
    public static FileTranReceiver getInstance(){
        if(fileTranReceiver==null){
            fileTranReceiver = new FileTranReceiver();
        }
        return fileTranReceiver;
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
        ServerSocket serverSocket = new ServerSocket(listenerPort)
        ){
            System.out.printf("服务启动成功,正在监听%d端口，等待链接》》》\n",listenerPort);
            do {
                Socket accept = serverSocket.accept();
                FileTranReceiverSocket fileTranReceiverSocket = new FileTranReceiverSocket(accept);
                fileTranReceiverSocket.start();
            }while (keepAlive);

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
    public void exit(){
        fileTranReceiver = null;
    }
    private class FileTranReceiverSocket extends Thread{

        Socket socket;
        private FileTranReceiverSocket(Socket socket){
            this.socket=socket;
        }

        @Override
        public void run() {
            try (InputStream inputStream = socket.getInputStream()) {
                byte[] intLength = new byte[4];
                inputStream.read(intLength);
                int length = Util.bytes2int(intLength);
                byte[] header = new byte[length];
                inputStream.read(header);
                String s1 = new String(header);

                if (s1.startsWith("Yeah FILE TRANSMISSION SERVICE:")){
                    SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
                    printLog("来自"+socket.getInetAddress().toString().substring(1) +"的请求头通过...");
                    String[] split = s1.split("[=|]");
                    String fileName = split[1];
                    String MD5 = split[3];
                    printLog("开始传输文件...");
                    InputStream fileStream = socket.getInputStream();

                    byte[] bytes = stream2ByteArray(fileStream);
                    if(bytes2File(bytes,savePath+File.separator+fileName)){
                        printLog("传输完成:"+savePath+File.separator+fileName+" 文件的MD5码为"+MD5+",请自行核验文件完整性");
                    }
                }else if(s1.equals("Yeah FILE TRANSMISSION SERVICE|STOP SERVICE|PWD="+pwd)){
                    printLog("接受到远程停止指令...");
                    socket.close();
                    System.exit(0);
                }
            }catch (IOException e){
                printErr("传输出错...");
            }
        }
        public byte[] stream2ByteArray(InputStream in) throws IOException{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = in.read(b))!=-1) bos.write(b,0,len);
            byte[] array = bos.toByteArray();
            bos.close();
            return array;
        }
        public boolean bytes2File(byte[] bytes,String Path) throws IOException {
            File file = new File(Path);
            if(!file.getParentFile().exists()){
                if(!file.getParentFile().mkdirs()){
                printErr("路径有误或权限不足传输失败");
                return false;
                }
            }
            if(!file.exists()){
                if(!file.createNewFile()){
                    printErr("路径有误或权限不足传输失败");
                    return false;
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(Path);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        }
    }

}
