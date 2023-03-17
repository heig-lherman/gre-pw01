package gre.lab1.gui;

import gre.lab1.graph.GridGraph2D;

/**
 * Permet d'injecter différentes implémentations dans le controller.
 */
public interface InstanceProvider {
    /**
     * @return Une implémentation concrète de {@link MazeGenerator}.
     */
    MazeGenerator generator();

    /**
     * Fournit la structure de données (correctement configurée) utilisée par un {@link MazeBuilder}
     * pour représenter le labyrinthe en cours de construction.
     *
     * @param width Largeur de la grille.
     * @param height Hauteur de la grille.
     * @return Un {@link GridGraph2D}.
     * @throws IllegalArgumentException si {@code width} ou {@code height} négatifs.
     */
    GridGraph2D builderDelegate(int width, int height);

    /**
     * Fournit la structure de données indiquant la topologie sous-jacente au labyrinthe.
     *
     * @param width Largeur de la grille.
     * @param height Hauteur de la grille.
     * @return Un {@link GridGraph2D}.
     * @throws IllegalArgumentException si {@code width} ou {@code height} négatifs.
     */
    GridGraph2D topology(int width, int height);
}
