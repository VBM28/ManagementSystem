module ManagementSystem {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.base;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.fxml;


    requires java.sql;

    opens login;
    opens db;
    opens adminPackage;
    opens userPackage;

}