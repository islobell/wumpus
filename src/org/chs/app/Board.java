package org.chs.app;

import org.chs.Defines;
import org.chs.Defines.Direction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona el tablero de juego
 */
public class Board {

    private int numCellsBySide;
    private int numCells;
    private int numPits;
    private Point goldPosition;
    private Point wumpusPosition;
    private List<Point> pitPositions;

    /**
     * Constructor
     */
    public Board() {
        numCellsBySide = -1;
        numCells = -1;
        numPits = -1;
        goldPosition = new Point(-1, -1);
        wumpusPosition = new Point(-1, -1);
        pitPositions = new ArrayList<>();
    }

    /**
     * Obtiene el número de celdas por lado
     */
    public int getNumCellsBySide() {
        return numCellsBySide;
    }

    /**
     * Obtiene la número de celdas total
     */
    public int getNumCells() {
        return numCells;
    }

    /**
     * Obtiene el número de pozos
     */
    public int getNumPits() {
        return numPits;
    }

    /**
     * Obtiene la posición del lingote de oro
     */
    public Point getGoldPosition() {
        return goldPosition;
    }

    /**
     * Obtiene la posición del Wumpus
     */
    public Point getWumpusPosition() {
        return wumpusPosition;
    }

    /**
     * Obtiene la posición de los pozos
     */
    public List<Point> getPitPositions() {
        return pitPositions;
    }

    /**
     * Construye el tablero
     * 
     * @param numCells
     * @param numPits
     */
    public void build(int numCellsBySide, int numPits) {
        this.numCellsBySide = numCellsBySide; // El tablero es cuadrado
        numCells = numCellsBySide * numCellsBySide; // El tablero es cuadrado
        this.numPits = numPits;
        goldPosition = new Point(-1, -1);
        wumpusPosition = new Point(-1, -1);
        pitPositions = new ArrayList<>();

        boolean[] cells = new boolean[numCells];

        // Reseteamos la matriz de celdas que nos ayudarán a posicionar los elementos
        // del tablero
        for (int y = 0; y < numCellsBySide; y++) {
            for (int x = 0; x < numCellsBySide; x++) {
                // Las casillas de la esquina inferior izquierda no se pueden ocupar para
                // que el cazador pueda moverse
                cells[y * numCellsBySide + x] = ((x == 0 && y == numCellsBySide - 1)
                        || (x == 0 && y == numCellsBySide - 2) || (x == 1 && y == numCellsBySide - 1));
            }
        }

        // Calculamos la posición de los elementos del juego
        goldPosition = calculateGoldPosition(cells);
        wumpusPosition = calculateWumpusPosition(cells);
        pitPositions = calculatePitPositions(cells);

        // Imprime la información de las celdas
        if (Defines.DEBUG_ENABLED) {
            printDebugInfo();
        }
    }

    /**
     * Calcula la posición del lingote de oro
     * 
     * @param cells Las celdas temporales
     * @return La posición del oro
     */
    private Point calculateGoldPosition(boolean[] cells) {
        Point pt;

        do {
            pt = new Point(Utils.getRandomNumber(numCellsBySide), Utils.getRandomNumber(numCellsBySide));
        } while (cells[pt.y * numCellsBySide + pt.x]);

        cells[pt.y * numCellsBySide + pt.x] = true;
        return pt;
    }

    /**
     * Calcula la posición del Wumpus
     * 
     * @param cells Las celdas temporales
     * @return La posición del Wumpus
     */
    private Point calculateWumpusPosition(boolean[] cells) {
        Point pt;

        do {
            pt = new Point(Utils.getRandomNumber(numCellsBySide), Utils.getRandomNumber(numCellsBySide));
        } while (cells[pt.y * numCellsBySide + pt.x]);

        cells[pt.y * numCellsBySide + pt.x] = true;
        return pt;
    }

    /**
     * Calcula la posición de los pozos
     * 
     * @param cells Las celdas temporales
     * @return La posición de los pozos
     */
    private List<Point> calculatePitPositions(boolean[] cells) {
        List<Point> pits = new ArrayList<>();

        int count = 0;

        do {
            Point pt = new Point(Utils.getRandomNumber(numCellsBySide), Utils.getRandomNumber(numCellsBySide));
            if (!cells[pt.y * numCellsBySide + pt.x] && !arePitsAroundGold(pt, pits)) {
                pits.add(pt);
                cells[pt.y * numCellsBySide + pt.x] = true;
                count++;
            }
        } while (count < numPits);

        return pits;
    }

    /**
     * Comprueba si el oro está casi rodeado de pozos
     * 
     * @param pits La lista de pozos
     * @return Si el oro está casi rodeado de pozos
     */
    private boolean arePitsAroundGold(Point pt, List<Point> pits) {
        int count = 0;

        for (Point pit : pits) {
            Point pos1 = new Point(goldPosition.x - 1, goldPosition.y);
            Point pos2 = new Point(goldPosition.x, goldPosition.y - 1);
            Point pos3 = new Point(goldPosition.x + 1, goldPosition.y);
            Point pos4 = new Point(goldPosition.x, goldPosition.y + 1);

            if (pit.equals(pos1) || pit.equals(pos2) ||
                    pit.equals(pos3) || pit.equals(pos4)) {
                count++;
            }

            if (count == 3 && (pt.equals(pos1) || pt.equals(pos2) ||
                    pt.equals(pos3) || pt.equals(pos4))) {
                count++;
            }
        }

        return (count == 4);
    }

    /**
     * Comprueba si el lingote de oro está en la celda
     * 
     * @param pt Coordenadas de la celda
     * @return true, si el oro está en la celda; false, en caso contrario
     */
    public boolean isGoldInCell(Point pt) {
        return isGoldInCell(pt.x, pt.y);
    }

    /**
     * Comprueba si el oro está en la celda
     * 
     * @param x Coordenada X de la celda
     * @param y Coordenada Y de la celda
     * @return true, si el oro está en la celda; false, en caso contrario
     */
    public boolean isGoldInCell(int x, int y) {
        return (x == goldPosition.x && y == goldPosition.y);
    }

    /**
     * Comprueba si el Wumpus está en la celda
     * 
     * @param pt Coordenadas de la celda
     * @return true, si el Wumpus está en la celda; false, en caso contrario
     */
    public boolean isWumpusInCell(Point pt) {
        return isWumpusInCell(pt.x, pt.y);
    }

    /**
     * Comprueba si el Wumpus está en la celda
     * 
     * @param x Coordenada X de la celda
     * @param y Coordenada Y de la celda
     * @return true, si el Wumpus está en la celda; false, en caso contrario
     */
    public boolean isWumpusInCell(int x, int y) {
        return (x == wumpusPosition.x && y == wumpusPosition.y);
    }

    /**
     * Comprueba si hay un pozo en la celda
     * 
     * @param pt Coordenadas de la celda
     * @return true, si hay un pozo en la celda; false, en caso contrario
     */
    public boolean isPitInCell(Point pt) {
        return isPitInCell(pt.x, pt.y);
    }

    /**
     * Comprueba si hay un pozo en la celda
     * 
     * @param x Coordenada X de la celda
     * @param y Coordenada Y de la celda
     * @return true, si hay un pozo en la celda; false, en caso contrario
     */
    public boolean isPitInCell(int x, int y) {
        for (Point pt : pitPositions) {
            if (x == pt.x && y == pt.y) {
                return true;
            }
        }

        return false;
    }

    /**
     * Comprueba si delante de nosotros hay una pared o estamos fuera del tablero
     * 
     * @param pt Coordenadas de la celda
     * @return true, si hemos topado con una pared; false, en caso contrario
     */
    public boolean isWallOrOutside(Point pt) {
        return isWallOrOutside(pt.x, pt.y);
    }

    /**
     * Comprueba si delante de nosotros hay una pared o estamos fuera del tablero
     * 
     * @param x Coordenada X de la celda
     * @param y Coordenada Y de la celda
     * @return true, si hemos topado con una pared; false, en caso contrario
     */
    public boolean isWallOrOutside(int x, int y) {
        return (x < 0 || x > numCellsBySide - 1 || y < 0 || y > numCellsBySide - 1);
    }

    /**
     * Comprueba si estamos cerca del Wumpus
     * 
     * @param pt Coordenadas de la celda
     * @return true, si estamos cerca del Wumpus; false, en caso contrario
     */
    public boolean isWumpusNear(Point pt) {
        return isWumpusNear(pt.x, pt.y);
    }

    /**
     * Comprueba si estamos cerca del Wumpus
     * 
     * @param x Coordenada X de la celda
     * @param y Coordenada Y de la celda
     * @return true, si estamos cerca del Wumpus; false, en caso contrario
     */
    public boolean isWumpusNear(int x, int y) {
        return ((x == wumpusPosition.x - 1 && y == wumpusPosition.y)
                || (x == wumpusPosition.x && y == wumpusPosition.y - 1)
                || (x == wumpusPosition.x + 1 && y == wumpusPosition.y)
                || (x == wumpusPosition.x && y == wumpusPosition.y + 1));
    }

    /**
     * Comprueba si estamos cerca de un pozo
     * 
     * @param pt Coordenadas de la celda
     * @return true, si estamos cerca de un pozo; false, en caso contrario
     */
    public boolean isPitNear(Point pt) {
        return isPitNear(pt.x, pt.y);
    }

    /**
     * Comprueba si estamos cerca de un pozo
     * 
     * @param x Coordenada X de la celda
     * @param y Coordenada Y de la celda
     * @return true, si estamos cerca de un pozo; false, en caso contrario
     */
    public boolean isPitNear(int x, int y) {
        for (Point pt : pitPositions) {
            if ((x == pt.x - 1 && y == pt.y) || (x == pt.x && y == pt.y - 1) ||
                    (x == pt.x + 1 && y == pt.y) || (x == pt.x && y == pt.y + 1)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Comprueba si una flecha ha matado al Wumpus
     * 
     * @param pos Coordenadas de la posición de la que parte la flecha
     * @param dir Dirección de la flecha
     * @return true, si estamos cerca de un pozo; false, en caso contrario
     */
    public boolean arrowKillsWumpus(Point pos, Direction dir) {
        boolean wumpusFound = false;
        boolean exit = false;

        do {
            pos = Utils.move(pos, 1, dir);

            if (isWumpusInCell(pos)) {
                wumpusFound = true;
                exit = true;
            } else if (isWallOrOutside(pos)) {
                exit = true;
            }
        } while (!exit);

        return wumpusFound;
    }

    /**
     * Imprime en pantalla la información del tablero
     */
    private void printDebugInfo() {
        System.out.println();
        System.out.println("-------------");
        System.out.println("Created cells");
        System.out.println("-------------");

        int count = 0;
        for (int y = 0; y < numCellsBySide; y++) {
            for (int x = 0; x < numCellsBySide; x++) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("*** Cell #%02d: [ ", count));
                sb.append(String.format("pos: { x: %2d - y: %2d } ", x, y));
                if (isGoldInCell(x, y)) {
                    sb.append("- type: GOLD   ");
                } else if (isWumpusInCell(x, y)) {
                    sb.append("- type: WUMPUS ");
                } else if (isPitInCell(x, y)) {
                    sb.append("- type: PIT    ");
                } else {
                    sb.append("- type: EMPTY  ");
                }
                sb.append(String.format("- isWumpusNear: %-5s ", isWumpusNear(x, y)));
                sb.append(String.format("- isPitNear: %-5s ", isPitNear(x, y)));
                sb.append("]");

                System.out.println(sb.toString());
                count++;
            }
        }

        System.out.println("-------------");
    }

    /**
     * Imprime el tablero
     */
    public void print() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < numCellsBySide; y++) {
            sb.append(printHLine());

            for (int x = 0; x < numCellsBySide; x++) {
                sb.append(printCell(x, y));
            }
        }

        sb.append(printHLine());

        System.out.println(sb.toString());
    }

    /**
     * Imprime una línea horizontal
     */
    private String printHLine() {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < numCellsBySide; x++) {
            if (x == numCellsBySide - 1) {
                sb.append("-----\n");
            } else {
                sb.append("----");
            }
        }

        return sb.toString();
    }

    /**
     * Imprime una celda
     * 
     * @param x Coordenada X de la celda
     * @param y Coordenada Y de la celda
     */
    private String printCell(int x, int y) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");

        if (x == 0 && y == numCellsBySide - 1) {
            sb.append(" > ");
        } else if (isGoldInCell(x, y)) {
            sb.append(" G ");
        } else if (isWumpusInCell(x, y)) {
            sb.append(" W ");
        } else if (isPitInCell(x, y)) {
            sb.append(" P ");
        } else {
            sb.append("   ");
        }

        if (x == numCellsBySide - 1) {
            sb.append("|\n");
        }

        return sb.toString();
    }
}
