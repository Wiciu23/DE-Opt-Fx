module com.witek.deoptfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires commons.math3;

    opens com.witek.deoptfx to javafx.fxml;
    exports com.witek.deoptfx;
}