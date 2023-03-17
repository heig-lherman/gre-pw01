package gre.lab1.gui.impl;

import gre.lab1.graph.GraphObserver;
import javafx.application.Platform;

import java.util.concurrent.CompletableFuture;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class MazeAnimation implements GraphObserver {
  private final MazePainter painter;
  private final Supplier<CompletableFuture<Void>> pauseControl;
  private final IntSupplier dynamicDelay;

  public MazeAnimation(MazePainter painter, Supplier<CompletableFuture<Void>> pauseControl, IntSupplier dynamicDelay) {
    this.painter = painter;
    this.pauseControl = pauseControl;
    this.dynamicDelay = dynamicDelay;
  }

  @Override
  public void onEdgeAdded(int u, int v) {
    Platform.runLater(() -> painter.removeWall(u, v));
    pause();
  }

  @Override
  public void onEdgeRemoved(int u, int v) {
    Platform.runLater(() -> painter.addWall(u, v));
    pause();
  }

  @Override
  public void onVertexChanged(int v) {
    Platform.runLater(() -> painter.drawCell(v));
    pause();
  }
  private void pause() {
    pauseControl.get().join();
    try {
      Thread.sleep(dynamicDelay.getAsInt());
    } catch (InterruptedException e) {
      throw new CanceledAnimationException();
    }
  }
}
