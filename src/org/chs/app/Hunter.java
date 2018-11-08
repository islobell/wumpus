package org.chs.app;

import org.chs.Defines;
import org.chs.Defines.Direction;
import org.chs.Defines.Rotation;

import java.awt.Point;

/**
 * Clase que gestiona el cazador
 */
public class Hunter {

    private int numArrows;
    private Point currPos;
    private Direction direction;
    private boolean hasGold;

    /**
     * Constructor
     */
    public Hunter() {
        this.numArrows = 0;
        this.currPos = new Point(-1, -1);
        this.direction = Direction.NONE;
        this.hasGold = false;
    }

    /**
     * Inicializa el objeto
     */
    public void init(int numArrows, Point startingPos) {
        this.numArrows = numArrows;
        this.currPos = startingPos;
        this.direction = Direction.RIGHT;
        this.hasGold = false;
    }

    /**
     * Coge el oro
     */
    public void takeGold() {
        hasGold = true;
    }

    /**
     * Comprueba si tiene el lingote de oro
     */
    public boolean hasGold() {
        return hasGold;
    }

    /**
     * Obtiene la posición actual
     */
    public Point getCurrPos() {
        return currPos;
    }

    /**
     * Obtiene la siguiente posición en la dirección actual
     */
    public Point getNextPos() {
        return Utils.move(currPos, 1, direction);
    }

    /**
     * Obtiene la dirección actual
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Obtiene el número de flechas
     */
    public int getNumArrows() {
        return numArrows;
    }

    /**
     * Mueve una posición en la dirección actual
     */
    public void move() {
        currPos = Utils.move(currPos, 1, direction);

        // Imprime la información del cazador
        if (Defines.DEBUG_ENABLED) {
            printDebugInfo();
        }
    }

    /**
     * Rota a la izquierda 90 grados
     */
    public void rotateL() {
        direction = Utils.rotate(direction, Rotation.LEFT);

        // Imprime la información del cazador
        if (Defines.DEBUG_ENABLED) {
            printDebugInfo();
        }
    }

    /**
     * Rota a la derecha 90 grados
     */
    public void rotateR() {
        direction = Utils.rotate(direction, Rotation.RIGHT);

        // Imprime la información del cazador
        if (Defines.DEBUG_ENABLED) {
            printDebugInfo();
        }
    }

    /**
     * Lanza una flecha
     * 
     * @return true, si ha podido lanzar una flecha; false, en caso contrario
     */
    public boolean throwArrow() {
        if (numArrows == 0) {
            return false;
        }

        numArrows--;

        if (Defines.DEBUG_ENABLED) {
            printDebugInfo();
        }

        return true;
    }

    /**
     * Imprime en pantalla la información del cazador
     */
    private void printDebugInfo() {
        Point nextPos = getNextPos();

        StringBuilder sb = new StringBuilder();
        sb.append("*** Hunter: [ ");
        sb.append(String.format("currPos: { x: %d - y: %d } ", currPos.x, currPos.y));
        sb.append(String.format("- nextPos: { x: %d - y: %d } ", nextPos.x, nextPos.y));
        sb.append(String.format("- direction: %s } ", direction.toString()));
        sb.append(String.format("- arrows: %d ", numArrows));
        sb.append("]");

        System.out.println(sb.toString());
    }
}
