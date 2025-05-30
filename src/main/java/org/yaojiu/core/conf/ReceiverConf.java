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

package org.yaojiu.core.conf;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.yaojiu.uitils.Log.printErr;
import static org.yaojiu.uitils.Util.checkFile;
import static org.yaojiu.uitils.Util.jarPath;


public class ReceiverConf {
    private String savePath;
    private int listenerPort;
    private String logFilePath;
    private static final String DEFAULT_SAVE_PATH;
    private static final int DEFAULT_LISTING_PORT;
    private static final String DEFAULT_LOG_FILE_PATH;

    static {
        Properties properties = new Properties();
        File file = new File(jarPath()+File.separator+"conf"+File.separator+"yftr.conf.xml");
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            properties.loadFromXML(fileInputStream);
        } catch (IOException e) {
            checkFile(file);
            properties.setProperty("port","4951");
            properties.setProperty("path",new File(jarPath()+File.separator+"r_res").getAbsolutePath());
            properties.setProperty("logPath",new File(jarPath()+File.separator+"r_log.log").getAbsolutePath());
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
                properties.storeToXML(fileOutputStream,"接收端配置文件","utf-8");
                properties.loadFromXML(new FileInputStream(file));
            } catch (IOException exception) {
                printErr("写入配置文件失败,没有对"+jarPath()+"路径的读写权限");
            }

        }

        DEFAULT_SAVE_PATH = properties.getProperty("path");
        DEFAULT_LISTING_PORT = Integer.parseInt(properties.getProperty("port"));
        DEFAULT_LOG_FILE_PATH = properties.getProperty("logPath");
    }

    public ReceiverConf() {
        savePath = DEFAULT_SAVE_PATH;
        listenerPort = DEFAULT_LISTING_PORT;
        logFilePath = DEFAULT_LOG_FILE_PATH;
    }

    public ReceiverConf(int listenerPort, String savePath) {
        this.listenerPort = listenerPort;
        this.savePath = savePath;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public int getListenerPort() {
        return listenerPort;
    }

    public void setListenerPort(int listenerPort) {
        this.listenerPort = listenerPort;
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
