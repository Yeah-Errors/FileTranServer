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

import com.yeah.fileTranServer.ApplicationVariousInfo;
import com.yeah.fileTranServer.core.conf.ReceiverConf;
import com.yeah.fileTranServer.core.conf.SenderConf;
import com.yeah.fileTranServer.core.receiver.Receiver;
import com.yeah.fileTranServer.ui.custom.MyLogging;
import com.yeah.fileTranServer.uitils.Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Init extends Application {
    @Override
    public void start(Stage primaryStage) {
        MyLogging myLogging = new MyLogging();
        myLogging.setRun(true);
        Text text = new Text("正在加载配置文件...");
        FlowPane Pane = new FlowPane(myLogging);
        myLogging.setPadding(new Insets(10, 10, 20, 10));
        Pane.setAlignment(Pos.CENTER);
        Pane.setOrientation(Orientation.VERTICAL);
        Pane.getChildren().add(text);
        myLogging.Run();
        Scene scene = new Scene(Pane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle(ApplicationVariousInfo.ApplicationName_CN);
        initFile();
        primaryStage.setOnCloseRequest(e->{
            Alert alert = new Alert(Alert.AlertType.WARNING, "确定要退出程序?", new ButtonType("确定", ButtonBar.ButtonData.YES), new ButtonType("取消", ButtonBar.ButtonData.NO));
           alert.showAndWait().ifPresent(res->{
               if(res==ButtonType.OK) {
                   Platform.exit();
                   System.exit(0);
               }
               else primaryStage.show();
           });
        });
        primaryStage.show();

        primaryStage.setResizable(false);

        new Main().start(primaryStage);

//        primaryStage.close();
//        someCode

    }
    private void initFile(){
        SenderConf senderConf = new SenderConf();
        ReceiverConf receiverConf = new ReceiverConf();
        Util.checkFile(senderConf.getLogFile());
        Util.checkFile(receiverConf.getLogFile());
    }


}

