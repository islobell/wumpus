package org.chs.app;

import org.chs.Defines.Action;
import org.chs.Defines.ActionResult;
import org.chs.Defines.Direction;
import org.chs.Defines.Rotation;

import java.awt.Point;
import java.util.Random;
import java.util.Set;

/**
 * Clase utilidad
 */
final class Utils {

    /**
     * Devuelve un n�mero a partir de un texto, si se encuentra dentro del rango
     * v�lido
     * 
     * @param str El texto que representa el n�mero
     * @param min El l�mite inferior del rango
     * @param max El l�mite superior del rango
     * @return Un n�mero, si el texto se encuentra dentro del rango v�lido; -1, en
     *         caso contrario
     */
    public static int parseNumber(String str, int min, int max) {
        try {
            int num = Integer.parseInt(str);
            if (num < min || num > max) {
                return -1;
            }
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Devuelve un entero entre 0 y max - 1
     * 
     * @param max El l�mite superior del rango v�lido (no incluido)
     * @return Un n�mero entero ente 0 y max -1
     */
    public static int getRandomNumber(int max) {
        return new Random(System.currentTimeMillis()).nextInt(max);
    }

    /**
     * Obtiene el comando en base al texto del comando
     * 
     * @param str El texto del comando
     * @return La acci�n correspondiente; NONE, si no existe el comando
     */
    public static Action parseAction(String str) {
        try {
            String action = str.trim().replace(" ", "_");
            return Action.valueOf(action);
        } catch (IllegalArgumentException e) {
            return Action.NONE;
        } catch (NullPointerException e) {
            return Action.NONE;
        }
    }

    /**
     * Desplaza la posici�n el n�mero de incrementos en la direcci�n indicada
     * 
     * @param pos La posici�n inicial
     * @param inc El incremento a aplicar
     * @param dir La direcci�n establecida
     */
    public static Point move(Point pos, int inc, Direction dir) {
        Point newPos = new Point(pos);

        switch (dir) {
            case LEFT:
                newPos.x -= inc;
                break;
            case TOP:
                newPos.y -= inc;
                break;
            case RIGHT:
                newPos.x += inc;
                break;
            case BOTTOM:
                newPos.y += inc;
                break;
            default:
                newPos = new Point(-1, -1);
                break;
        }

        return newPos;
    }

    /**
     * Actualiza la direcci�n tras rotar 90 grados en el sentido indicado (izquierda
     * o derecha)
     * 
     * @param dir La direcci�n
     * @param rot El sentido indicado (izquierda o derecha)
     */
    public static Direction rotate(Direction dir, Rotation rot) {
        Direction newDir = dir;

        switch (dir) {
            case LEFT:
                newDir = (rot == Rotation.LEFT ? Direction.BOTTOM : Direction.TOP);
                break;
            case TOP:
                newDir = (rot == Rotation.LEFT ? Direction.LEFT : Direction.RIGHT);
                break;
            case RIGHT:
                newDir = (rot == Rotation.LEFT ? Direction.TOP : Direction.BOTTOM);
                break;
            case BOTTOM:
                newDir = (rot == Rotation.LEFT ? Direction.RIGHT : Direction.LEFT);
                break;
            default:
                newDir = Direction.NONE;
                break;
        }

        return newDir;
    }

    /**
     * Imprime uno o varios mensajes en funci�n de los resultados obtenidos
     * 
     * @param results Los resultados obtenidos
     */
    public static void printMessage(Set<ActionResult> results) {
        if (results.contains(ActionResult.HUNTER_WINS)) {
            System.out.println("FELICIDADES! Has consegido salir con el lingote de oro.");
            System.out.println();
        }

        if (results.contains(ActionResult.WUMPUS_FOUND)) {
            System.out.println("FIN DE LA PARTIDA! El Wumpus te ha comido.");
            System.out.println();
        }

        if (results.contains(ActionResult.PIT_FOUND)) {
            System.out.println("FIN DE LA PARTIDA! Has ca�do en un pozo.");
            System.out.println();
        }

        if (results.contains(ActionResult.GOLD_FOUND)) {
            System.out.println("C�mo brilla ese lingote de oro! A la saca...");
        }

        if (results.contains(ActionResult.WALL_FOUND)) {
            System.out.println("Hay una pared delante de mi. Necesito cambiar de direcci�n para poder avanzar...");
        }

        if (results.contains(ActionResult.WUMPUS_DEAD)) {
            System.out.println("La flecha ha matado al Wumpus. He o�do c�mo gritaba...");
        }

        if (results.contains(ActionResult.NO_ARROWS)) {
            System.out.println("Me he quedado sin flechas.");
        }

        if (results.contains(ActionResult.ARROW_FAILED)) {
            System.out.println("La flecha ha chocado contra la pared.");
        }

        if (results.contains(ActionResult.WUMPUS_ODOR)) {
            System.out.println("Aqu� huele a Wumpus.");
        }

        if (results.contains(ActionResult.PIT_BREEZE)) {
            System.out.println("Se ha levantado una ligera brisa.");
        }

        if (results.contains(ActionResult.GOLD_REQUIRED)) {
            System.out.println("No puedo salir sin el oro. Creo que seguir� buscando...");
        }

        if (results.contains(ActionResult.NO_EXIT)) {
            System.out.println("Esto no es la salida.");
        }
    }
}
