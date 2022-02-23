module com.mathematti.plugpack {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mathematti.plugpack to javafx.fxml;
    opens com.mathematti.plugpack.gui to javafx.graphics;
    exports com.mathematti.plugpack;
}