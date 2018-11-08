package org.chs.test;

import org.chs.app.Board;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.Point;
import java.util.List;

/**
 * Comprueba que el tablero se genera correctamente, atendiendo a los siguientes
 * factores:
 * 
 * - El tablero se crea con el número de celdas y pozos que se le indican.
 * 
 * - Cada elemento tiene su posición: no hay elementos superpuestos o fuera del
 * tablero.
 */
public class BoardTest extends BaseTest {

    private int numPits = -1;
    Board board = null;

    @Test
    public void doTest() {
	numCellsBySide = getRandomInt(MAX_CELLS);
	numPits = getRandomInt((int) (MAX_CELLS * 0.25f + 0.5));

	board = new Board();
	board.build(numCellsBySide, numPits);

	Point goldPosition = board.getGoldPosition();
	Point wumpusPosition = board.getWumpusPosition();
	List<Point> pitPositions = board.getPitPositions();

        // 1. Comprobamos el posición de celdas de un lado
        Assert.assertTrue(board.getNumCellsBySide() == numCellsBySide, "El número de celdas de un lado es incorrecto.");
	
        // 2. Comprobamos el posición de celdas en total (debe ser cuadrado)
        Assert.assertTrue(board.getNumCells() == numCellsBySide * numCellsBySide,
                "El número de celdas de un lado es incorrecto.");
	
        // 3. Comprobamos el posición de pozos
        Assert.assertTrue(board.getNumPits() == numPits, "El número de pozos es incorrecto.");
	
        // 4. Comprobamos que el oro está dentro del tablero
	Assert.assertTrue(isInsideBoard(goldPosition, numCellsBySide), "El lingote de oro no se encuentra dentro del tablero.");
	
        // 5. Comprobamos que el Wumpus está dentro del tablero
	Assert.assertTrue(isInsideBoard(wumpusPosition, numCellsBySide), "El Wumpus no se encuentra dentro del tablero.");

        // 6. Comprobamos que el oro y el Wumpus no está en la misma celda
        Assert.assertTrue(!goldPosition.equals(wumpusPosition),
                "El lingote de oro y en Wumpus se encuentran en la misma posición del tablero.");

	// 7. Comprobamos los pozos
	Point prevPit = new Point(-1, -1);
	for (int i = 0; i < pitPositions.size(); i++) {
	    Point pit = pitPositions.get(i);
	    
            // 9. Para cada pozo, comprobamos que está en el tablero
            Assert.assertTrue(isInsideBoard(pit, numCellsBySide), "Al menos un pozo no se encuentra dentro del tablero.");
            
	    if (i > 0) {
                // 8. Para cada pozo, comprobamos ocupan una posición distinta
                Assert.assertTrue(!prevPit.equals(pit),
                        "Al menos 2 pozos se encuentran en la misma posición del tablero.");
	    }

            // 10. Para cada pozo, comprobamos que no están en la misma celda que el oro
            Assert.assertTrue(!pit.equals(goldPosition),
                    "Al menos un pozo y el lingote de oro se encuentran en la misma posición del tablero.");
	    
            // 11. Para cada pozo, comprobamos que no están en la misma celda que el Wumpus
            Assert.assertTrue(!pit.equals(wumpusPosition),
                    "Al menos un pozo y en Wumpus se encuentran en la misma posición del tablero.");
	    
	    prevPit = pit;
	}
    }
}
