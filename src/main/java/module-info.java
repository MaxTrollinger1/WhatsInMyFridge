module com.example.whatsinmyfridge {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.example.whatsinmyfridge to javafx.fxml;
    opens com.example.whatsinmyfridge.storage.data to com.google.gson;

    exports com.example.whatsinmyfridge;
}