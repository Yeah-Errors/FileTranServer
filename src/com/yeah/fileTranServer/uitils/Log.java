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

package com.yeah.fileTranServer.uitils;

import com.yeah.fileTranServer.ApplicationVariousInfo;
import com.yeah.fileTranServer.ui.custom.WarringAlert;

import java.io.*;
import java.util.Date;

import static com.yeah.fileTranServer.uitils.Util.checkFile;

public class Log {
    public static void printLog(String msg) {
        System.out.printf("[info]%s : %s\n",new Date(),msg);
    }

    public static void printErr(String msg){
        System.out.printf("[Error]%s : %s\n",new Date(),msg);
        if(ApplicationVariousInfo.isStartByUI) WarringAlert.showAlert(msg);
    }

    public synchronized static void printLog(String msg,File LogFile,boolean append) {
        checkFile(LogFile);
        try(PrintWriter printWriter = new PrintWriter(new FileOutputStream(LogFile,append))){
            printWriter.printf("[info ]%s : %s\n",new Date(),msg);
        } catch (FileNotFoundException e) {
            printErr("日志输出发生异常");
        }
        printLog(msg);
    }

    public synchronized static void printErr(String msg,File LogFile,boolean append) {
        checkFile(LogFile);
        try(PrintWriter printWriter = new PrintWriter(new FileOutputStream(LogFile,append))){
            printWriter.printf("[Error]%s : %s\n",new Date(),msg);
        } catch (FileNotFoundException e) {
            printErr("日志输出发生异常");
        }
        printErr(msg);
    }

}
