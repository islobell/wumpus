package org.chs.test;

import org.chs.Defines.Direction;

import java.awt.Point;
import java.util.Random;

/**
 * Clase base para los tests.
 */
public class BaseTest {

    protected final int MAX_CELLS = 100;
    protected int numCellsBySide = -1;
    // protected int numPits = -1;

    protected int getRandomInt(int max) {
	return new Random(System.currentTimeMillis()).nextInt(max);
    }

    protected boolean isInsideBoard(Point pt, int numCellsBySide) {
	return (pt.x >= 0 && pt.x <= numCellsBySide - 1 && pt.y >= 0 && pt.y <= numCellsBySide - 1);
    }

    protected void calculatePosition(Point pos, Direction dir, boolean isMovement, boolean isRotationL,
	    boolean isRotationR) {
	if (isMovement) {
	    move(pos, dir);
	}
	if (isRotationL) {
	    rotateL(dir);
	}
	if (isRotationR) {
	    rotateR(dir);
	}
    }

    protected Point move(Point pos, Direction dir) {
        Point newPos = new Point(pos);
        
	switch (dir) {
	case LEFT:
	    newPos.x--;
	    break;
	case TOP:
	    newPos.y--;
	    break;
	case RIGHT:
	    newPos.x++;
	    break;
	case BOTTOM:
	    newPos.y++;
	    break;
	default:
	    newPos = new Point(-1, -1);
	    break;
	}
	
	return newPos;
    }

    protected Direction rotateL(Direction dir) {
        Direction newDir = Direction.NONE;
        
	switch (dir) {
	case LEFT:
	    newDir = Direction.BOTTOM;
	    break;
	case TOP:
	    newDir = Direction.LEFT;
	    break;
	case RIGHT:
	    newDir = Direction.TOP;
	    break;
	case BOTTOM:
	    newDir = Direction.RIGHT;
	    break;
	default:
	    break;
	}
	
	return newDir;
    }

    protected Direction rotateR(Direction dir) {
        Direction newDir = Direction.NONE;
        
	switch (dir) {
	case LEFT:
	    newDir = Direction.TOP;
	    break;
	case TOP:
	    newDir = Direction.RIGHT;
	    break;
	case RIGHT:
	    newDir = Direction.BOTTOM;
	    break;
	case BOTTOM:
	    newDir = Direction.LEFT;
	    break;
	default:
	    break;
	}
        
        return newDir;
    }
}
