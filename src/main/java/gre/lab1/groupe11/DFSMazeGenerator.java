package gre.lab1.groupe11;

import gre.lab1.gui.MazeBuilder;
import gre.lab1.gui.MazeGenerator;
import gre.lab1.gui.Progression;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Générateur de labyrinthe par parcours en profondeur (DFS) sur un graphe non
 * orienté en deux dimensions, implémenté par {@link GridGraph}.
 */
public final class DFSMazeGenerator implements MazeGenerator {
  @Override
  public void generate(MazeBuilder builder, int from) {
    Objects.requireNonNull(builder, "Maze builder must not be null");
    if (!builder.topology().vertexExists(from)) {
      throw new IllegalArgumentException("Vertex " + from + " does not exist");
    }

    builder.progressions().setLabel(from, Progression.PROCESSING);

    List<Integer> neighbors = builder.topology().neighbors(from);
    Collections.shuffle(neighbors);
    for (int neighbor : neighbors) {
      if (Progression.PENDING == builder.progressions().getLabel(neighbor)) {
        builder.removeWall(from, neighbor);
        generate(builder, neighbor);
      }
    }

    builder.progressions().setLabel(from, Progression.PROCESSED);
  }
}
