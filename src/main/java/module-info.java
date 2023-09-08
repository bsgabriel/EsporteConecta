module com.ucs.esporteconecta {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires jakarta.persistence;

    opens com.ucs.esporteconecta to javafx.fxml;
    opens com.ucs.esporteconecta.model to org.hibernate.orm.core;

    exports com.ucs.esporteconecta;
}