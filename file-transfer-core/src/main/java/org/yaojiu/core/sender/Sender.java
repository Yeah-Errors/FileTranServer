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

package org.yaojiu.core.sender;

import org.yaojiu.core.conf.SenderConf;
import org.yaojiu.core.header.Header;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.yaojiu.uitils.Log.printErr;
import static org.yaojiu.uitils.Log.printLog;

public class Sender {
    private final SenderConf senderConf;
    private final File logFile;
    public Sender(SenderConf senderConf) {
        this.senderConf = senderConf;
        logFile=new File(senderConf.getLogFilePath());
    }

    public SenderConf getSenderConf() {
        return senderConf;
    }
    public void send(File file){
        new Thread(()->{
            try {
                SocketChannel channel = SocketChannel.open(new InetSocketAddress(senderConf.getRemoteHost(), senderConf.getRemotePort()));
                printLog("链接远端设备成功，正在发送文件头信息...",senderConf.getLogFile(),true);
                //发送头部文件信息
                Header header = new Header(file);
                byte[] byteArray = header.toByteArray();
                ByteBuffer allocate = ByteBuffer.allocate(4);
                allocate.putInt(byteArray.length);
                allocate.flip();
                channel.write(allocate);
                allocate = ByteBuffer.wrap(byteArray);

                channel.write(allocate);
                printLog("文件头信息发送成功...",logFile,true);
                //传输文件主体
                printLog("开始传输文件，当前进度 0%，当前速率 0kb/s",logFile,true);
                long fileSize = file.length();
                long startSize = fileSize;

                FileChannel tran = FileChannel.open(Paths.get(file.getPath()), StandardOpenOption.READ, StandardOpenOption.WRITE);
                ByteBuffer byteBuffer = ByteBuffer.allocate(10485760);
                long startTime = System.currentTimeMillis();
                int k=0;
                while(fileSize>0){
                    int read = tran.read(byteBuffer);
                    if (read<0) break;
                    byteBuffer.flip();
                    channel.write(byteBuffer);
                    byteBuffer.clear();
                    fileSize -= read;
                    if((k++)%5==0) printLog(String.format("正在传输，当前进度%s，平均速率%.2f Mb/s",(startSize-fileSize)*100/startSize+"%",(startSize-fileSize)/((System.currentTimeMillis()-startTime)/1000.0)/(1024*1024)),logFile,true);
                }
                printLog(String.format("传输完成，平均速率%.2f Mb/s",startSize/((System.currentTimeMillis()-startTime)/1000.0)/(1024*1024)),logFile,true);
                channel.close();
            } catch (IOException e) {

                printErr("链接异常，可能远程端口尚未启用",logFile,true);//链接异常
            }
        }).start();
    }

}
