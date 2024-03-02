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

import com.yeah.filetran.client.FileTranSender;
import com.yeah.filetran.server.FileTranReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
     static final String URL = "https://github.com/Yeah-Errors/FileTranServer";
     static final String VERSION = "1.0";

    public static void main(String[] args) {
        if(args.length==0){
            printHelp();
        }else{
        switch (args[0]){
            case "-S" : {
                ArrayList<File> files = new ArrayList<>();
                FileTranSender fileTranSender = FileTranSender.getInstance();
                for (int i = 1; i < args.length; i+=2) {
                    switch (args[i]){
                        case "-h":{
                            fileTranSender.setRemoteHost(args[i+1]);
                            break;
                        }
                        case "-p":{
                            fileTranSender.setRemotePort(Integer.parseInt(args[i+1]));
                            break;
                        }
                        case "-f":{
                             File file = new File(args[i+1]);
                            if(!file.exists()){
                                System.out.println("文件不存在...");
                                System.exit(1);

                            }
                            files.add(file);
                            break;
                        }
                        case "-s" : {
                            fileTranSender.remoteShut(args[i+1]);
                            System.exit(0);
                        }

                    }
                }
                if(files.isEmpty()){
                    boolean exit = false;
                    while (!exit){
                        System.out.print("请输入文件路径(-1退出)：");
                        String s = new Scanner(System.in).nextLine();
                        if (s.equals("-1")) exit = true;
                        else {
                        File file = new File(s);
                        if(!file.exists()){
                            System.out.println("文件不存在...");
                        }else fileTranSender.run(file);
                    }}
                }else files.forEach(fileTranSender::run);
                break;
            }
            case "-R" :{
                FileTranReceiver fileTranReceiver = FileTranReceiver.getInstance();
                for (int i = 1;i<args.length;i+=2){
                    switch (args[i]){
                        case "-p" : {
                            fileTranReceiver.setListenerPort(Integer.parseInt(args[i+1]));
                            break;
                        }
                        case "-a" : {
                            fileTranReceiver.setKeepAlive(Boolean.parseBoolean(args[i+1]));
                            break;
                        }
                        case "-f" :{
                            fileTranReceiver.setSavePath(args[i+1]);
                            break;
                        }
                    }
                }
                fileTranReceiver.run();
                break;
            }
            case "-I":{
                System.out.println("暂不支持...");
                break;
            }
            case "-H":printHelp();
        }
        }

    }
    public static void printHelp(){
        System.out.printf("欢迎使用 YeahFileTranHelper\n\n" +
                "这是一个简单的文件传输工具（目前不支持过大文件的传输）\n\n" +
                "用法:\njava -jar yfth.jar -S(-R,-I,-H) [-para value]...\n\t" +
                "-S 去发送文件\n\t\t" +
                    "-h host : 链接至指定域\n\t\t" +
                    "-p port : 通过指定端口链接\n\t\t" +
                    "-f filePath :必须参数（可重复），发送指定文件\n\t\t" +
                    "-s pwd : 远程结束接收端服务\n\t" +
                "-R 去接受收文件\n\t\t" +
                    "-p port : 使用指定端口启动\n\t\t" +
                    "-a true|false : 是否保持存活？（只接收一次？）可通过发送端进行远程停止\n\t\t" +
                    "-f filePath : 将接收到的文件保存至filePath\n\t"+
                "-I 安装这个服务（安装接收端）\n\t" +
                "-H 打印帮助信息\n\n" +
                "Version %s\n" +
                "Source_code url:%s",VERSION,URL);

    }
}
