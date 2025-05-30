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

package org.yaojiu.console;

import org.yaojiu.ApplicationVariousInfo;
import org.yaojiu.core.conf.ReceiverConf;
import org.yaojiu.core.conf.SenderConf;
import org.yaojiu.core.receiver.Receiver;
import org.yaojiu.core.sender.Sender;
import org.yaojiu.uitils.Log;
import org.yaojiu.uitils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static org.yaojiu.uitils.Util.jarPath;

public class Console {
    {
        initFile();
    }

    private final String[] userArgs;

    public Console(String[] userArgs) {
        this.userArgs = userArgs;
    }
    public void start() {
        String[] args = userArgs;
        if (args.length == 0) {
            printHelp();
        }
        else {
            switch (args[0]) {
            case "-S": {
                ArrayList<File> files = new ArrayList<>();
                SenderConf senderConf = new SenderConf();
                for (int i = 1; i < args.length; i += 2) {
                    switch (args[i]) {
                        case "-h": {
                            senderConf.setRemoteHost(args[i + 1]);
                            break;
                        }
                        case "-p": {
                            senderConf.setRemotePort(Integer.parseInt(args[i + 1]));
                            break;
                        }
                        case "-f": {
                            File file = new File(args[i + 1]);
                            if (!file.exists()) {
                                System.out.println("文件不存在...");
                                System.exit(1);
                            }
                            files.add(file);
                            break;
                        }
                    }
                }
                Sender sender = new Sender(senderConf);
                if (files.isEmpty()) {
                    boolean exit = false;
                    while (!exit) {
                        System.out.print("请输入文件路径(-1退出)：");
                        String s = new Scanner(System.in).nextLine();
                        if (s.equals("-1")) exit = true;
                        else {
                            File file = new File(s);
                            if (!file.exists()) {
                                System.out.println("文件不存在...");
                            } else sender.send(file);
                        }
                    }
                } else files.forEach(sender::send);
                break;
            }
            case "-R": {
                ReceiverConf receiverConf = new ReceiverConf();
                for (int i = 1; i < args.length; i += 2) {
                    switch (args[i]) {
                        case "-p": {
                            receiverConf.setListenerPort(Integer.parseInt(args[i + 1]));
                            break;
                        }
                        case "-f": {
                            receiverConf.setSavePath(args[i + 1]);
                            break;
                        }
                    }
                }
                Receiver receiver = new Receiver(receiverConf);
                receiver.receive();
                break;
            }
            case "-H":
                printHelp();
                break;
            default:
                Log.printErr("错误的指令");
                printHelp();
        }
        }
    }

    public void printHelp(){
        System.out.printf("欢迎使用 %s\n\n" +
                "这是一个简单的文件传输工具（采用NIO,分段传输）\n\n" +
                "用法:\njava -jar yfth.jar -S(-R,-H,-UI) [-para value]...\n\t" +
                "-S 去发送文件\n\t\t" +
                "-h host : 链接至指定域\n\t\t" +
                "-p port : 通过指定端口链接\n\t\t" +
                "-f filePath :必须参数（可重复），发送指定文件\n\t\t" +
                "-s pwd : 远程结束接收端服务(暂不支持)\n\t\t" +
                "-c configPath : 加载指定路径的配置文件(暂不支持)\n\t" +
                "-R 去接受收文件\n\t\t" +
                "-p port : 使用指定端口启动\n\t\t" +
                "-a true|false : 是否保持存活？（只接收一次？）(暂不支持：目前为持续存活)\n\t\t" +
                "-f filePath : 将接收到的文件保存至filePath\n\t" +
                "-c configPath : 加载指定路径的配置文件(暂不支持)\n\t" +
                "-UI 启动UI界面(fx支持;停止维护，仅为简单页面)\n\t" +
                "相关配置文件参考路径:" + jarPath() + File.separator + "conf" + File.separator + "\n" +
                "-H 打印帮助信息\n\n" +
                "Version %s(%s)\n" +
                "Source_code url:%s\n", ApplicationVariousInfo.ApplicationName_EN,ApplicationVariousInfo.Version,ApplicationVariousInfo.VersionCode, ApplicationVariousInfo.PROJECT_URL);
    }
    private void initFile(){
        SenderConf senderConf = new SenderConf();
        ReceiverConf receiverConf = new ReceiverConf();
        Util.checkFile(senderConf.getLogFile());
        Util.checkFile(receiverConf.getLogFile());
    }
}
