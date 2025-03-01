module JavaChat {
    exports cs50.alfvag;
    exports cs50.alfvag.models;

    requires javafx.fxml;
    requires javafx.controls;
    requires transitive javafx.graphics;

    requires java.net.http;
    requires java.desktop;

    opens cs50.alfvag.controllers to javafx.fxml;
}