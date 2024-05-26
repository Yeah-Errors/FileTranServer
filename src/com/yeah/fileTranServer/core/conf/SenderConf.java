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

package com.yeah.fileTranServer.core.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static com.yeah.fileTranServer.uitils.Log.printErr;
import static com.yeah.fileTranServer.uitils.Log.printLog;
import static com.yeah.fileTranServer.uitils.Util.*;

public class SenderConf {
    private String remoteHost;
    private int remotePort;
    private String logFilePath;
    private static final int DEFAULT_REMOTE_PORT;
    private static final String DEFAULT_REMOTE_HOST;
    private static final String DEFAULT_LOG_FILE_PATH;

    static  {
        Properties properties = new Properties();
        File file = new File(jarPath()+File.separator+"conf"+File.separator+"yfts.conf.xml");
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            properties.loadFromXML(fileInputStream);
        } catch (IOException e) {
            checkFile(file);
            properties.setProperty("host","localhost");
            properties.setProperty("port","4951");
            properties.setProperty("logPath",new File(jarPath()+File.separator+"s_log.log").getAbsolutePath());
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
                properties.storeToXML(fileOutputStream,"文件发送配置文件","utf-8");
                properties.loadFromXML(new FileInputStream(file));
            } catch (IOException ignored) {

            }
        }
        DEFAULT_REMOTE_HOST = properties.getProperty("host");
        DEFAULT_REMOTE_PORT = Integer.parseInt(properties.getProperty("port"));
        DEFAULT_LOG_FILE_PATH = properties.getProperty("logPath");
        printLog("初始化成功...");
    }
    public SenderConf() {
        remoteHost = DEFAULT_REMOTE_HOST;
        remotePort = DEFAULT_REMOTE_PORT;
        logFilePath = DEFAULT_LOG_FILE_PATH;
    }

    public SenderConf(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }
    public File getLogFile() {
        return new File(logFilePath);
    }
    public void setLogFile(File logFile) {
        this.logFilePath = logFile.getAbsolutePath();
    }
}
