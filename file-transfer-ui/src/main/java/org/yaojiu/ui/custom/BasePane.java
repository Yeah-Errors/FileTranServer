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

package org.yaojiu.ui.custom;

import org.yaojiu.ApplicationVariousInfo;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Text;


public class BasePane extends GridPane {
    private CommonInfoPane infoPane = new CommonInfoPane();
    private TextArea logArea =  new TextArea();
    private VBox Core = new VBox();
    public BasePane() {
        ColumnConstraints left = new ColumnConstraints(400);
        ColumnConstraints right = new ColumnConstraints(200);
        left.setHgrow(Priority.SOMETIMES);
        right.setHgrow(Priority.SOMETIMES);
        getColumnConstraints().addAll(left, right);
        add(infoPane, 0, 0);
        infoPane.setPadding(new Insets(0, 10, 10, 0));
        add(new Text(ApplicationVariousInfo.Version), 1, 0);
        add(Core, 0, 1,2,1);
        Core.setPadding(new Insets(0, 10, 10, 10));
        add(logArea, 0,2,2,1);

        logArea.setEditable(false);
    }

    public VBox getCore() {
        return Core;
    }

    public void setCore(VBox core) {
        Core = core;
    }

    public CommonInfoPane getInfoPane() {
        return infoPane;
    }

    public TextArea getLogArea() {
        return logArea;
    }

    public void setInfoPane(HBox infoPane) {
        this.infoPane = (CommonInfoPane) infoPane;
    }

    public void setLogArea(TextArea logArea) {
        this.logArea = logArea;
    }
}
