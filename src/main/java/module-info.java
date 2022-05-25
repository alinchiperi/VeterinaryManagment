module ro.usv.veterinarymanagment {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires ojdbc14;

    opens ro.usv.veterinarymanagment to javafx.fxml;
    exports ro.usv.veterinarymanagment;
    exports ro.usv.veterinarymanagment.UserInterface;
    opens ro.usv.veterinarymanagment.UserInterface to javafx.fxml;
    //add
    opens ro.usv.veterinarymanagment.DataModel to javafx.base;
}