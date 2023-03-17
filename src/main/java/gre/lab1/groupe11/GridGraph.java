package gre.lab1.groupe11;

import gre.lab1.graph.GridGraph2D;

import java.util.*;

/**
 * Implémentation d'un graphe simple non orienté mutable en grille sur deux
 * dimensions.
 */
public final class GridGraph implements GridGraph2D {
  /**
   * Largeur de la grille.
   */
  private final int width;

  /**
   * Hauteur de la grille.
   */
  private final int height;

  private final List<List<Integer>> adjacencyList;

  /**
   * Construit une grille carrée.
   *
   * @param side Côté de la grille.
   */
  public GridGraph(int side) {
    this(side, side);
  }

  /**
   * Construit une grille rectangulaire.
   *
   * @param width  Largeur de la grille.
   * @param height Hauteur de la grille.
   * @throws IllegalArgumentException si {@code width} ou {@code length} sont
   *                                  négatifs ou nuls.
   */
  public GridGraph(int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width: " + width + " and height: " + height + " must be positive");
    }

    this.width = width;
    this.height = height;

    adjacencyList = new ArrayList<>(width * height);
    for (int i = 0; i < width * height; i++) {
      adjacencyList.add(new ArrayList<>(4));
    }
  }

  @Override
  public List<Integer> neighbors(int v) {
    assertWithinBounds(v);
    return new ArrayList<>(adjacencyList.get(v));
  }

  @Override
  public boolean areAdjacent(int u, int v) {
    assertWithinBounds(u);
    assertWithinBounds(v);
    return adjacencyList.get(u).contains(v);
  }

  @Override
  public void addEdge(int u, int v) {
    if (areAdjacent(u, v)) {
      throw new IllegalArgumentException("Edge (" + u + ", " + v + ") already exists");
    }

    adjacencyList.get(u).add(v);
    adjacencyList.get(v).add(u);
  }

  @Override
  public void removeEdge(int u, int v) {
    if (!areAdjacent(u, v)) {
      throw new IllegalArgumentException("Edge (" + u + ", " + v + ") does not exist");
    }

    adjacencyList.get(u).remove((Integer) v);
    adjacencyList.get(v).remove((Integer) u);
  }

  @Override
  public int nbVertices() {
    return width * height;
  }

  @Override
  public boolean vertexExists(int v) {
    return v >= 0 && v < nbVertices();
  }

  @Override
  public int width() {
    return width;
  }

  @Override
  public int height() {
    return height;
  }

  /**
   * Lie chaque sommet du graphe donné à tous ses voisins dans la grille.
   *
   * @param graph Un graphe.
   * @implNote Cette méthode ajoute seulement les arêtes dans une seule direction,
   *           car la méthode {@link #addEdge(int, int)} ajoute automatiquement
   *           l'arête inverse.
   */
  public static void bindAll(GridGraph graph) {
    for (int i = 1; i <= graph.nbVertices(); i++) {
      if ((i % graph.width) > 0) {
        graph.addEdge(i - 1, i);
      }

      if ((i + graph.width) <= graph.nbVertices()) {
        graph.addEdge(i - 1, i - 1 + graph.width);
      }
    }
  }

  private void assertWithinBounds(int v) {
    if (!vertexExists(v)) {
      throw new IndexOutOfBoundsException("Vertex " + v + " does not exist");
    }
  }
}
