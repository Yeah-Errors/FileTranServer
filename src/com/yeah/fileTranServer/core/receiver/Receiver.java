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

package com.yeah.fileTranServer.core.receiver;

import com.yeah.fileTranServer.core.conf.ReceiverConf;
import com.yeah.fileTranServer.core.header.Header;


import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


import static com.yeah.fileTranServer.uitils.Log.printErr;
import static com.yeah.fileTranServer.uitils.Log.printLog;

public class Receiver {
   private final ReceiverConf receiverConf;
   private final File logFile;
   public Receiver(ReceiverConf receiverConf) {
       this.receiverConf = receiverConf;
       logFile = new File(receiverConf.getLogFilePath());
   }

   public ReceiverConf getReceiverConf() {
       return receiverConf;
   }

    public void receive() {
       new Thread(()->{
            try(ServerSocketChannel fileSocketChanel = ServerSocketChannel.open()){
                InetSocketAddress inetSocketAddress = new InetSocketAddress(receiverConf.getListenerPort());
                fileSocketChanel.socket().bind(inetSocketAddress);
                printLog("正在监听"+receiverConf.getListenerPort()+",等待链接>>>",logFile,true);
                do {
                    SocketChannel accept = fileSocketChanel.accept();
                    FileReceiverThread fileTranReceiverSocket = new FileReceiverThread(accept);
                    fileTranReceiverSocket.start();
                }while (true);
            }catch(IOException e){
                printErr(String.format("启动失败，可能的原因:%d端口被占用,请更换端口后重试",receiverConf.getListenerPort()),logFile,true);
            }
        }).start();
    }
    private class FileReceiverThread extends Thread{
       final SocketChannel fileSocketChanel;
       FileReceiverThread(SocketChannel fileSocketChanel){
           this.fileSocketChanel = fileSocketChanel;
       }
       @Override
       public void run(){

           try {
               printLog("接收到远端链接,正在接收并核验文件头",logFile,true);
               ByteBuffer allocate = ByteBuffer.allocate(4);
               fileSocketChanel.read(allocate);
               allocate.flip();//转为读模式
               int headLength = allocate.getInt();
               allocate = ByteBuffer.allocate(headLength);
               fileSocketChanel.read(allocate);
               byte[] array = allocate.array();
               Header header = Header.fromByteArray(array);
               printLog("文件头核验完毕，开始文件传输",logFile,true);
               String remoteAddress = fileSocketChanel.getRemoteAddress().toString().substring(1);

               long fileSize = header.getFileSize();
               long startSize = fileSize;

               checkSaveFilePath();
               try(FileChannel fileChannel = FileChannel.open(Paths.get(receiverConf.getSavePath() + File.separator + header.getFileName()), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE)){
               allocate = ByteBuffer.allocate(1024 * 1024 * 10);
               printLog("文件传输已开启，当前进度 0%,当前速率 0 Mb/s",logFile,true);
               int k=0;
               long startTime = System.currentTimeMillis();
               while (fileSize > 0) {
                   int read = fileSocketChanel.read(allocate);
                   if (read < 0) break;
                   allocate.flip();
                   fileChannel.write(allocate);
                   allocate.clear();
                   fileSize -= read;
                   if((k++)%5==0) printLog(String.format("正在传输，当前进度%s，平均速率%.2f Mb/s",(startSize-fileSize)*100/startSize+"%",(startSize-fileSize)/((System.currentTimeMillis()-startTime)/1000.0)/(1024*1024)),logFile,true);
               }
                   printLog(String.format("传输完成，平均速率%.2f Mb/s",startSize/((System.currentTimeMillis()-startTime)/1000.0)/(1024*1024)),logFile,true);
               }
               printLog("传输完成: " + receiverConf.getSavePath() + File.separator + header.getFileName() + " 文件的MD5码为" + header.getMD5() + ",请自行核验文件完整性",logFile,true);
           } catch (NoSuchFileException ex){
               printErr("传输中断...", logFile,true);

           } catch (IOException e) {
               printErr("传输中断...", logFile,true);
               //链接中断，传输失败
           } catch(RuntimeException ex){
               printErr("非法的文件头信息，已过滤此次传输", logFile,true);
           }
       }
       private void checkSaveFilePath(){
           File file = new File(receiverConf.getSavePath());
           if(!file.exists()){
               if(file.mkdirs()){
                   printLog(String.format("文件存储目录 %s \\创建成功",receiverConf.getSavePath()),logFile,true);
               }else {
                   printErr(String.format("文件存储目录%s创建失败,请检查该路径的读写权限",receiverConf.getSavePath()),logFile,true);
               }
           }
       }
    }
}
