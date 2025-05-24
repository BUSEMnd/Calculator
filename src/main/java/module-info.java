module com.buse.proje {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.buse.proje to javafx.fxml;
    exports com.buse.proje;
}