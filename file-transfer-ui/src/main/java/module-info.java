module file.transfer.ui {
    requires file.transfer.common;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires file.transfer.core;
    exports org.yaojiu.ui to javafx.graphics;
}