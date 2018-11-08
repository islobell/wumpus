package org.chs.test;

import org.chs.Defines.Direction;
import org.chs.app.Hunter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.Point;

/**
 * Comprueba que comportamiento del cazador es el esperado, atendiendo a los
 * siguientes factores:
 * 
 * - El movimiento y las rotaciones.
 * 
 * - El lanzamiento de flechas.
 */
public class HunterTest extends BaseTest {

    private final int MAX_ACTIONS = 500;
    private final int MAX_ARROWS = 10;
    private int numActions = -1;
    private int numArrows = -1;
    Hunter hunter = null;

    @Test
    public void doTest() {
	numCellsBySide = getRandomInt(MAX_CELLS);

	numActions = getRandomInt(MAX_ACTIONS);
	numArrows = getRandomInt(MAX_ARROWS);

	Point startingPos = new Point(getRandomInt(MAX_CELLS), getRandomInt(MAX_CELLS));

	hunter = new Hunter();
	hunter.init(numArrows, startingPos);
	
	int curAction = 0;

	do {
	    boolean isMovement = getRandomInt(Integer.MAX_VALUE) % 2 == 0;
	    boolean isRotationL = getRandomInt(Integer.MAX_VALUE) % 2 == 0;
	    boolean isRotationR = getRandomInt(Integer.MAX_VALUE) % 2 == 0;
	    
	    Point pos = hunter.getCurrPos();
	    Direction dir = hunter.getDirection();

	    if (isMovement) {
		hunter.move();
                pos = move(pos, dir);
	    }

	    if (isRotationL) {
		hunter.rotateL();
		dir = rotateL(dir);
	    }

	    if (isRotationR) {
		hunter.rotateR();
                dir = rotateR(dir);
	    }

            // 1. Comprobamos que la posición y dirección son correctas
            Assert.assertTrue(pos.equals(hunter.getCurrPos()), "La posición del cazador es incorrecta.");
            Assert.assertTrue(dir.equals(hunter.getDirection()), "La dirección del cazador es incorrecta.");
	    
            curAction++;
	} while (curAction < numActions);

	// 2.

	do {
	    boolean throwArrow = getRandomInt(Integer.MAX_VALUE) % 2 == 0;
	    
	    if (throwArrow) {
		hunter.throwArrow();
		numArrows--;
	    }

            // 2. Comprobamos que el número de flechas es correcto
	    Assert.assertTrue(hunter.getNumArrows() == numArrows, "El nï¿½mero de flechas del cazador es incorrecto.");
	} while (numArrows > 0);
    }
}
