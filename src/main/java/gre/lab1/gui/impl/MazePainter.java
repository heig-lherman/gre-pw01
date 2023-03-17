package gre.lab1.gui.impl;

import gre.lab1.graph.GridGraph2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class MazePainter {

  private final GridGraph2D maze;
  private final GraphicsContext context;
  private Function<Integer, Color> cellColorF = v -> Color.WHITE;
  private BiFunction<Integer, Integer, Color> gapColorF = (u, v) -> Color.WHITE;
  private int wallThickness = 2;

  private int cellSide = 100;
  private Color wallColor = Color.BLACK;

  public MazePainter(GridGraph2D maze, GraphicsContext context) {
    this.maze = maze;
    this.context = context;
  }

  public void addWall(int u, int v) {
    drawWall(u, v, wallColor);
  }

  public void removeWall(int u, int v) {
    drawWall(u, v);
  }

  private void drawWall(int u, int v) {
    if (! maze.areAdjacent(u, v)) {
      drawWall(u, v, wallColor);
    } else {
      drawWall(u, v, gapColorF.apply(u, v));
    }
  }

  private void drawWall(int u, int v, Color color) {
    context.setFill(color);
    int xu = col(u);
    int yu = row(u);
    int xv = col(v);
    int yv = row(v);


    if (xu == xv) {
      // Séparation horizontale
      context.fillRect(cellOffset(xu), cellSide + cellOffset(Math.min(yu, yv)), cellSide, wallThickness);
    } else {
      // Séparation verticale
      context.fillRect(cellSide + cellOffset(Math.min(xu, xv)), cellOffset(yu), wallThickness, cellSide);
    }
  }

  public void drawCell(int v) {
    context.setFill(cellColorF.apply(v));
    context.fillRect(cellOffset(col(v)), cellOffset(row(v)), cellSide, cellSide);

    for(int u : maze.neighbors(v)) {
      drawWall(u, v);
    }
  }

  public void repaint() {
    // Arrière-plan
    context.setFill(wallColor);
    context.fillRect(0 ,0, context.getCanvas().getWidth(), context.getCanvas().getHeight());

    // Cases et murs
    for (int u = 0; u < maze.nbVertices(); ++u) {
      drawCell(u);
      for (int v : maze.neighbors(u)) {
        if (u > v)
          drawWall(u, v);
      }
    }
  }

  // Fluent setters

  public MazePainter setCellColorF(Function<Integer, Color> cellColorF) {
    this.cellColorF = cellColorF;
    return this;
  }

  public MazePainter setGapColorF(BiFunction<Integer, Integer, Color> gapColorF) {
    this.gapColorF = gapColorF;
    return this;
  }

  public MazePainter setWallColor(Color wallColor) {
    this.wallColor = wallColor;
    return this;
  }

  public MazePainter setWallThickness(int wallThickness) {
    this.wallThickness = wallThickness;
    return this;
  }

  public MazePainter setCellSide(int cellSide) {
    this.cellSide = cellSide;
    return this;
  }

  // Helpers

  private int cellOffset(int pos) {
    return wallThickness + pos * (cellSide + wallThickness);
  }

  private int row(int v) {
    return v / maze.width();
  }

  private int col(int v) {
    return v % maze.width();
  }
}


