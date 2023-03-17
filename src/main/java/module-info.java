module gre.lab1 {
  requires javafx.controls;
  requires javafx.fxml;

  exports gre.lab1.graph;

  exports gre.lab1.gui;
  opens gre.lab1.gui to javafx.fxml;
  exports gre.lab1.gui.impl;
  opens gre.lab1.gui.impl to javafx.fxml;
  exports gre.lab1.groupe11;
  opens gre.lab1.groupe11 to javafx.fxml;
}