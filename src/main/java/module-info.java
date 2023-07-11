module com.witek.deoptfx {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.witek.deoptfx to javafx.fxml;
    exports com.witek.deoptfx;
}