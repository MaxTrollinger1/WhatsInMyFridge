module com.example.whatsinmyfridge {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.whatsinmyfridge to javafx.fxml;
    exports com.example.whatsinmyfridge;
}