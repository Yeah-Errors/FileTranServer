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

package com.yeah.fileTranServer.ui;

import com.yeah.fileTranServer.core.conf.SenderConf;
import com.yeah.fileTranServer.core.sender.Sender;
import com.yeah.fileTranServer.ui.custom.BasePane;
import com.yeah.fileTranServer.ui.custom.FileChooserArea;
import com.yeah.fileTranServer.uitils.Log;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SenderPage extends Application {
    SenderConf senderConf =  new SenderConf();
    Sender sender = new Sender(senderConf);
    @Override
    public void start(Stage primaryStage) {

        BasePane gridPane = new BasePane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        VBox info = gridPane.getInfoPane().getInfo();
        Text hostInfo = new Text("目标地址: "+senderConf.getRemoteHost()+":"+senderConf.getRemotePort());
        info.getChildren().add(hostInfo);

        VBox core = gridPane.getCore();

        core.setSpacing(10);
        HBox selectFile = new HBox();
        selectFile.setSpacing(30);

        FileChooserArea fileChooserArea = new FileChooserArea();
        selectFile.getChildren().add(fileChooserArea);
        Button button = new Button("发送");
        button.setOnAction(x->{
            String FilePath = fileChooserArea.getText();
            File file = new File(FilePath);
            if(file.isFile()){
                sender.send(file);
            }else {
                Log.printErr("请选择一个合法文件，以进行传输操作",senderConf.getLogFile(),true);
            }
        });

        selectFile.getChildren().add(button);

        core.getChildren().add(selectFile);
        TextArea logArea = gridPane.getLogArea();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            File logFile = senderConf.getLogFile();
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
//        core.getChildren().add(new TaskListPane(SendTask.tasks));

        primaryStage.setTitle("文件传输助手-发送端");
        primaryStage.setScene(new Scene(gridPane));
        primaryStage.show();
    }
}
