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

package com.yeah.fileTranServer.core.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class SendTask extends Task{
    public final static LinkedList<SendTask> taskTable = new LinkedList<>();
    public final static ObservableList<SendTask> tasks = FXCollections.observableList(taskTable);


    public SendTask(String remoteAddress,int schedule) {
        this.taskInformation = new SendTaskInformation(remoteAddress,schedule);
        taskTable.add(this);
        tasks.add(this);
    }
    @Override
    public void removeTask() {
        taskTable.remove(this);
        tasks.remove(this);
    }
    public String getRemoteAddress(){
        return ((SendTaskInformation)taskInformation).remoteAddress;
    }
    @Override
    public int getSchedule(){
        return ((SendTaskInformation)taskInformation).schedule;
    }
    @Override
    public void setSchedule(int schedule){
        ((SendTaskInformation)taskInformation).schedule = schedule;
    }

    @Override
    public String toString() {
        return getRemoteAddress();
    }
}
class SendTaskInformation implements TaskInformation{
    public String remoteAddress;
    public int schedule;
    public SendTaskInformation( String remoteAddress, int schedule) {
        this.remoteAddress = remoteAddress;
        this.schedule = schedule;
    }
}
