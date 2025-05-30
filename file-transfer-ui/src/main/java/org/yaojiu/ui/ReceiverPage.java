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

package org.yaojiu.ui;

import org.yaojiu.core.conf.ReceiverConf;
import org.yaojiu.core.receiver.Receiver;
import org.yaojiu.ui.custom.BasePane;
import org.yaojiu.uitils.Log;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReceiverPage extends Application {
    final Receiver receiver = new Receiver(new ReceiverConf());
    final ReceiverConf receiverConf = receiver.getReceiverConf();
    {
        receiver.receive();
    }
    @Override
    public void start(Stage primaryStage) {
        BasePane gridPane = new BasePane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        VBox info = gridPane.getInfoPane().getInfo();
        Text portInfo = new Text("监听端口: "+receiverConf.getListenerPort());
        Text saveInfo = new Text();
        setSaveInfo(saveInfo);

        info.getChildren().addAll(portInfo, saveInfo);
        TextArea logArea = gridPane.getLogArea();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            File logFile = receiverConf.getLogFile();
            StringBuilder content = new StringBuilder();
            try(BufferedReader br = new BufferedReader(new FileReader(logFile))){
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                Log.printErr("读取日志文件失败");
                logArea.setText("读取日志文件失败");
            }
            Platform.runLater(()->{
                logArea.setText(content.toString());
                logArea.positionCaret(content.length());
                logArea.setScrollTop(Double.MAX_VALUE);
            });
        },0,1, TimeUnit.SECONDS);
//        gridPane.getCore().getChildren().add(new TaskListPane(ReceiveTask.tasks));

        primaryStage.setTitle("文件传输助手-接收端");
        primaryStage.setScene(new Scene(gridPane));
        primaryStage.show();
    }
    private void setSaveInfo(Text info) {
        String savePath = receiverConf.getSavePath();
        if (savePath.length() > 20) {
            info.setText("存储位置: "+savePath.substring(0, 20)+"...");
            Tooltip tooltip = new Tooltip("存储位置: "+savePath);
            tooltip.setShowDelay(Duration.ZERO);
            Tooltip.install(info, tooltip);
        }else {
            info.setText(savePath);
        }
    }
}

