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

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class MyLogging extends Pane {
    private final Arc arc = new Arc();
    private boolean run = false;

    {
        arc.setType(ArcType.OPEN);
        this.getChildren().add(arc);
        setCursor(Cursor.WAIT);
        arc.setFill(null);
        arc.setSmooth(true);
        arc.setStroke(Color.BLUE);
    }

    public MyLogging() {
        arc.centerXProperty().bind(this.widthProperty().divide(2));
        arc.centerYProperty().bind(this.heightProperty().divide(2));

        arc.setRadiusX(20);
        arc.setRadiusY(20);
        arc.setStrokeWidth(3);
        arc.setLength(300);

    }

    MyLogging(double centerX, double centerY, double radius, double startAngle, double length) {
        arc.setCenterX(centerX);
        arc.setCenterY(centerY);
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
        arc.setStartAngle(startAngle);
        arc.setLength(length);
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void Run() {
        Runnable runnable = () -> {
            double i = 1;
            while (run) {
                try {
                    Thread.sleep(6);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                arc.setRotate(i++);
                if (i == 360) i = 0;
            }
        };
        new Thread(runnable).start();
    }
}
