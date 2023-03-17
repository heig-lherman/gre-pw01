package gre.lab1.gui.impl;

import gre.lab1.graph.Graph;
import gre.lab1.graph.VertexLabelling;
import gre.lab1.gui.Progression;
import javafx.scene.paint.Color;

public final class StaticConfig {
  private StaticConfig(){}

  public static int startPoint(Graph graph) {
    return 0;
  }

  public static int wallThickness(int cellSide) {
    return Math.max(cellSide / 10, 1);
  }

  public static Color wallColor() {
    return Color.BLACK;
  }

  public static Color cellColor(VertexLabelling<Progression> progressions, int v) {
    return switch(progressions.getLabel(v)) {
      case PROCESSED -> Color.WHITE;
      case PENDING -> Color.BLACK;
      case PROCESSING -> Color.RED;
    };
  }

  public static Color gapColor(VertexLabelling<Progression> progressions, int u, int v) {
    Progression pU = progressions.getLabel(u);
    Progression pV = progressions.getLabel(v);
    if (pU == pV) {
      return cellColor(progressions, v);
    } else if (pU == Progression.PENDING || pV == Progression.PENDING) {
      return Color.BLACK;
    } else if (pU == Progression.PROCESSING || pV == Progression.PROCESSING) {
      return Color.RED;
    }

    return Color.WHITE;
  }
}
