package org.chs;

/**
 * Clase para la definición de tipos
 */
public final class Defines {

    // Indica si hay que mostrar la información de depuración
    public static final boolean DEBUG_ENABLED = false;

    // Texto que se muestra cuando el usuario introduce un parámetro inválido o
    // fuera de rango
    public static final String INVALID_NUMBER_STRING_FORMAT = "Debes introducir un número entre %d y %d.";

    // Texto que se muestra cuando el usuario introduce un comando inválido
    public static final String INVALID_COMMAND_STRING_FORMAT = "Comando incorrecto! Los comandos permitidos son: %s.";

    // Rango válido para el número de celdas (por lado)
    public final static int MIN_CELLS = 4;
    public final static int MAX_CELLS = 10;

    // Rango válido para el número de pozos (porcentaje del total de celdas)
    public final static float MIN_RATIO_PITS = 0.10f;
    public final static float MAX_RATIO_PITS = 0.25f;

    // Rango válido para el número de flechas
    public final static int MIN_ARROWS = 1;
    public final static int MAX_ARROWS = 5;

    /**
     * Fases del juego
     */
    public enum GamePhase {
        NONE,
        ACCEPTED,
        STARTED,
        FINISHED,
        QUIT
    }

    /**
     * Acciones disponibles
     */
    public enum Action {
        NONE,
        YES,
        NO,
        MOVE,
        ROTATE_LEFT,
        ROTATE_RIGHT,
        THROW_ARROW,
        EXIT,
        QUIT
    }

    /**
     * Resultados posibles de las acciones
     */
    public enum ActionResult {
        NONE,
        HUNTER_WINS,
        WUMPUS_FOUND,
        PIT_FOUND,
        GOLD_FOUND,
        WALL_FOUND,
        WUMPUS_DEAD,
        NO_ARROWS,
        ARROW_FAILED,
        WUMPUS_ODOR,
        PIT_BREEZE,
        GOLD_REQUIRED,
        NO_EXIT
    }

    /**
     * Rotaciones disponibles
     */
    public enum Rotation {
        NONE,
        LEFT,
        RIGHT
    }

    /**
     * Direcciones posibles
     */
    public enum Direction {
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }
}
