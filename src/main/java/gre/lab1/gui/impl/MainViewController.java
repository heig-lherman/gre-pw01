package gre.lab1.gui.impl;

import gre.lab1.gui.InstanceProvider;
import gre.lab1.gui.MazeGenerator;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public final class MainViewController implements Initializable {
  @FXML private Label gridSizeLabel;
  @FXML private Slider gridSizeSlider;
  @FXML private Button generateBtn;
  @FXML private Button pauseBtn;
  @FXML private Button stopBtn;
  @FXML private Slider delaySlider;
  @FXML private Pane canvasArea;
  @FXML private Canvas canvas;

  private InstanceProvider instanceProvider;
  private MazePainter painter;

  private ObservableMaze maze;

  private Thread worker;

  private CompletableFuture<Void> pause = CompletableFuture.completedFuture(null);

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    canvasArea.heightProperty().addListener(this::onResize);
    canvasArea.widthProperty().addListener(this::onResize);
  }

  @FXML
  private void onGenerate() {
    if (instanceProvider == null) return;

    setControlDisable(true);

    int side = (int) gridSizeSlider.getValue();
    maze = new ObservableMaze(instanceProvider.topology(side, side), instanceProvider.builderDelegate(side, side));

    painter = new MazePainter(maze, canvas.getGraphicsContext2D())
          .setWallColor(StaticConfig.wallColor())
          .setCellColorF(v -> StaticConfig.cellColor(maze, v))
          .setGapColorF((u,v) -> StaticConfig.gapColor(maze, u, v));
    displayMaze();

    MazeAnimation animation = new MazeAnimation(painter,
          () -> pause,
          () -> (int) Math.pow(
                delaySlider.getMax(),
                (delaySlider.getMax() - delaySlider.getValue()) / delaySlider.getMax()));
    maze.subscribe(animation);

    worker = new Thread(() -> {
      try {
        MazeGenerator builder = instanceProvider.generator();
        builder.generate(maze, StaticConfig.startPoint(maze));
      } catch (CanceledAnimationException ignored) {
      } finally {
        maze.unsubscribe(animation);
        Platform.runLater(() -> setControlDisable(false));
      }
    });
    worker.setDaemon(true);
    worker.start();
  }

  public void onPlayPause() {
    setPause(pause.isDone());
  }

  private void setPause(boolean paused) {
    if (paused) {
      pause = new CompletableFuture<>();
      pauseBtn.textProperty().setValue("⏵");
    } else {
      pause.complete(null);
      pauseBtn.textProperty().setValue("⏸");
    }
  }

  public void onStop() {
    setPause(false);
    worker.interrupt();
  }

  private void setControlDisable(boolean disabled) {
    gridSizeLabel.setDisable(disabled);
    gridSizeSlider.setDisable(disabled);
    generateBtn.setDisable(disabled);
    pauseBtn.setDisable(!disabled);
    stopBtn.setDisable(!disabled);
  }

  private void onResize(Observable observable) {
    if (maze == null) return;

    displayMaze();
  }

  private void displayMaze() {
    int side = (int) Math.min(canvasArea.getWidth(), canvasArea.getHeight());
    int approximateCellSide = side / maze.width();
    int wallThickness = StaticConfig.wallThickness(approximateCellSide);
    int cellSide = (side - wallThickness) / maze.width() - wallThickness;
    side = (cellSide + wallThickness) * maze.width() + wallThickness;

    canvas.setWidth(side);
    canvas.setHeight(side);

    painter.setWallThickness(wallThickness)
          .setCellSide(cellSide);
    painter.repaint();
  }

  public void setInstanceProvider(InstanceProvider instanceProvider) {
    this.instanceProvider = instanceProvider;
  }
}