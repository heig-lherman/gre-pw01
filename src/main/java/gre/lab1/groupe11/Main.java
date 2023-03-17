package gre.lab1.groupe11;

import gre.lab1.graph.GridGraph2D;
import gre.lab1.gui.InstanceProvider;
import gre.lab1.gui.MazeGenerator;
import gre.lab1.gui.impl.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class Main extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(InstanceProvider.class.getResource("mainView.fxml"));
    Parent parent = fxmlLoader.load();
    Scene scene = new Scene(parent, 800, 600);
    stage.setTitle("Shining generator");
    stage.setScene(scene);

    MainViewController controller = fxmlLoader.getController();
    controller.setInstanceProvider(new InstanceProvider() {
      @Override
      public MazeGenerator generator() {
        return new DFSMazeGenerator();
      }

      @Override
      public GridGraph2D builderDelegate(int width, int height) {
        return new GridGraph(width, height);
      }

      @Override
      public GridGraph2D topology(int width, int height) {
        GridGraph graph = new GridGraph(width, height);
        GridGraph.bindAll(graph);
        return graph;
      }
    });

    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}