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

import org.yaojiu.ApplicationVariousInfo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Text text = new Text("欢迎使用文件传输助手");
        Text text_info = new Text("由于fx适配问题，ui端将停止更新~(后续可能适配web端)");
        text.setFont(Font.font(40));
        text_info.setFont(Font.font(20));
        text_info.setFill(Color.GREEN);

        Button sender = new Button("发送");
        Button receiver = new Button("接收");
        String ButtonCss = "-fx-pref-width: 90; -fx-pref-height: 40; -fx-border-style:solid ; -fx-border-radius: 10; -fx-font-size: 25;-fx-background-radius: 10";
        sender.setStyle(ButtonCss);
        receiver.setStyle(ButtonCss);
        sender.setOnAction(x-> new SenderPage().start(primaryStage));
        receiver.setOnAction(x-> new ReceiverPage().start(primaryStage));
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(60,0,0,0));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(sender, 0, 0);
        gridPane.add(receiver, 1, 0);
        gridPane.setHgap(90);

        VBox outer = new VBox();
        outer.setPadding(new Insets(80, 0, 0, 0));
        outer.setAlignment(Pos.TOP_CENTER);
        outer.getChildren().add(text);
        outer.getChildren().add(text_info);
        outer.getChildren().add(gridPane);


        Scene scene = new Scene(outer, 600, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle(ApplicationVariousInfo.ApplicationName_CN);
        primaryStage.show();
    }

}

