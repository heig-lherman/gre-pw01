package gre.lab1;

import gre.lab1.graph.GridGraph2D;
import gre.lab1.groupe11.GridGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridGraphTests {
  private static void testNeighbors(GridGraph2D g, int v, Integer[] expected){
    var neighbors = g.neighbors(v);
    neighbors.sort(Integer::compareTo);
    assertArrayEquals(expected, neighbors.toArray());
  }

  @Test
  public void testAddEdge() {
    GridGraph2D g = new GridGraph(3);

    for (int v : new int[]{1,3,5,7}) {
      assertDoesNotThrow(() -> g.addEdge(4, v));
      assertTrue(g.areAdjacent(4, v));
    }

    testNeighbors(g, 4, new Integer[]{1,3,5,7});
  }

  @Test
  public void testRemoveEdge() {
    GridGraph2D g = new GridGraph(3);

    for (int v : new int[]{1,3,5,7}) {
      g.addEdge(4 ,v);
      assertDoesNotThrow(() -> g.removeEdge(4, v));
      assertFalse(g.areAdjacent(4, v));
    }

    testNeighbors(g, 4, new Integer[]{});
  }

  @Test
  public void testBindAll() {
    GridGraph g = new GridGraph(3);
    GridGraph.bindAll(g);
    testNeighbors(g, 0, new Integer[]{1,3});
    testNeighbors(g, 2, new Integer[]{1,5});
    testNeighbors(g, 6, new Integer[]{3,7});
    testNeighbors(g, 8, new Integer[]{5,7});
    testNeighbors(g, 4, new Integer[]{1,3,5,7});
  }
}
