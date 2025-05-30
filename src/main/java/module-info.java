module org.yaojiu{
    requires javafx.controls;
    requires javafx.fxml;

    opens org.yaojiu to javafx.fxml;
    exports org.yaojiu.ui to javafx.graphics;
    exports org.yaojiu;
}