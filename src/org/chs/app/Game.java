package org.chs.app;

import org.chs.Defines;
import org.chs.Defines.Action;
import org.chs.Defines.ActionResult;
import org.chs.Defines.GamePhase;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Clase que gestiona la lógica del juego
 */
public class Game {

    private int minPits;
    private int maxPits;

    private Point startingPos;

    private int numCells;
    private int numPits;
    private int numArrows;

    GamePhase phase;

    private boolean isWumpusDead;

    private Board board;
    private Hunter hunter;

    /**
     * Constructor
     */
    public Game() {
        numCells = -1;
        numPits = -1;
        numArrows = -1;

        minPits = -1;
        maxPits = -1;

        startingPos = new Point(-1, -1);

        phase = GamePhase.NONE;

        isWumpusDead = false;

        board = new Board();
        hunter = new Hunter();
    }

    /**
     * Pregunta al usuario acerca de los parámetros de configuración del juego
     */
    public void promptGameParams() {
        if (phase.equals(GamePhase.NONE)) {
            System.out.print("¿Iniciar partida? (YES/NO) ");
        } else {
            if (numCells == -1) {
                System.out.print(
                        "Número de celdas (por lado) [ " + Defines.MIN_CELLS + " - " + Defines.MAX_CELLS + " ]: ");
            } else if (numPits == -1) {
                System.out.print("Número de pozos [ " + minPits + " - " + maxPits + " ]: ");
            } else if (numArrows == -1) {
                System.out.print("Número de flechas [ " + Defines.MIN_ARROWS + " - " + Defines.MAX_ARROWS + " ]: ");
            } else if (phase.equals(GamePhase.STARTED)) {
                System.out.println();
                System.out.print("> ");
            }
        }
    }

    /**
     * Procesa los comandos de texto
     * 
     * @param input El texto del comando
     * @return true, si el usuario quiere salir del juego; false, en caso contrario
     */
    public boolean processCommand(String command) {
        // Si no hay un juego en curso
        if (phase.equals(GamePhase.NONE)) {
            Action action = Utils.parseAction(command);
            if (action.equals(Action.NONE)) {
                System.out.println(String.format(Defines.INVALID_COMMAND_STRING_FORMAT, "YES y NO"));
                System.out.println();
                return false;
            }

            if (action.equals(Action.YES)) {
                phase = GamePhase.ACCEPTED;
            } else {
                phase = GamePhase.QUIT;
            }
        } else {
            // Si el usuario no ha introducido el número de celdas
            if (numCells == -1) {
                int num = Utils.parseNumber(command, Defines.MIN_CELLS, Defines.MAX_CELLS);

                if (num == -1) {
                    System.out.println(
                            String.format(Defines.INVALID_NUMBER_STRING_FORMAT, Defines.MIN_CELLS, Defines.MAX_CELLS));
                    System.out.println();
                    return false;
                }

                numCells = num;
                minPits = (int) (Math.pow(numCells, 2) * Defines.MIN_RATIO_PITS + 0.5);
                maxPits = (int) (Math.pow(numCells, 2) * Defines.MAX_RATIO_PITS + 0.5);
                startingPos = new Point(0, numCells - 1); // La casilla de salida estï¿½ en la esquina
                                                          // inferior izquierda
            }
            // Si el usuario no ha introducido el número de pozos
            else if (numPits == -1) {
                int num = Utils.parseNumber(command, minPits, maxPits);

                if (num == -1) {
                    System.out.println(String.format(Defines.INVALID_NUMBER_STRING_FORMAT, minPits, maxPits));
                    System.out.println();
                    return false;
                }

                numPits = num;
            }
            // Si el usuario no ha introducido el número de flechas
            else if (numArrows == -1) {
                int num = Utils.parseNumber(command, Defines.MIN_ARROWS, Defines.MAX_ARROWS);
                if (num == -1) {
                    System.out.println(String.format(Defines.INVALID_NUMBER_STRING_FORMAT, Defines.MIN_ARROWS,
                            Defines.MAX_ARROWS));
                    System.out.println();
                    return false;
                }

                numArrows = num;
                initGame();
                // Si el juego ha empezado, procesar los comandos del usuario
            } else if (phase.equals(GamePhase.STARTED)) {
                processPlayerCommnand(command);
            }
        }

        // Si el usaurio no quiere jugar, salimos del juego
        return phase.equals(GamePhase.QUIT);
    }

    /**
     * Inicia el juego, construye el tablero e inicializa el cazador
     */
    private void initGame() {
        System.out.println();
        System.out.println("Construyendo el tablero. Espera, por favor...");

        board.build(numCells, numPits);
        hunter.init(numArrows, startingPos);

        phase = GamePhase.STARTED;

        System.out.println();

        // Imprime la información del tablero
        if (Defines.DEBUG_ENABLED) {
            board.print();
        }

        System.out.println("INSTRUCCIONES:");
        System.out.println("==============");
        System.out.println("1. Encuentra el lingote de oro y sal de la cueva.");
        System.out.println("2. Evita ser comido por el Wumpus o caer en un pozo.");
        System.out.println();
        System.out.println("+ El cazador empieza en la casilla inferior izquierda, mirando hacia la derecha.");
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("| Comandos disponibles                    |");
        System.out.println("-------------------------------------------");
        System.out.println("| MOVE         | Mover una casilla        |");
        System.out.println("| ROTATE LEFT  | Girar 90º a la izquierda |");
        System.out.println("| ROTATE RIGHT | Girar 90º a la derecha   |");
        System.out.println("| THROW ARROW  | Lanzar una flecha        |");
        System.out.println("| EXIT         | Salir de la cueva        |");
        System.out.println("-------------------------------------------");
        System.out.println("| QUIT         | Salir del juego          |");
        System.out.println("-------------------------------------------");
    }

    /**
     * Procesa los mensajes durante la partida
     * 
     * @param input El texto del comando
     */
    public void processPlayerCommnand(String input) {
        switch (Utils.parseAction(input)) {
            case MOVE:
                if (!isHunterInFrontOfWall()) {
                    hunter.move();
                    checkHunterPosition();
                }
                break;
            case ROTATE_LEFT:
                hunter.rotateL();
                if (!isHunterInFrontOfWall()) {
                    checkHunterPosition();
                }
                break;
            case ROTATE_RIGHT:
                hunter.rotateR();
                if (!isHunterInFrontOfWall()) {
                    checkHunterPosition();
                }
                break;
            case THROW_ARROW:
                throwArrow();
                break;
            case EXIT:
                exit();
                break;
            case QUIT:
                phase = GamePhase.QUIT;
                break;
            default:
                System.out.println(String.format(Defines.INVALID_COMMAND_STRING_FORMAT,
                        "MOVE, ROTATE LEFT, ROTATE RIGHT, THROW ARROW, EXIT y QUIT"));
                break;
        }
    }

    /**
     * Comprueba si el cazador se encuentra delante de un pared
     * 
     * @return true, si el cazador se encuentra delante de una pared; false, en caso contrario
     */
    private boolean isHunterInFrontOfWall() {
        if (board.isWallOrOutside(hunter.getNextPos())) {
            Utils.printMessage(EnumSet.of(ActionResult.WALL_FOUND));
            return true;
        }

        return false;
    }

    /**
     * Mï¿½todo que gestiona la lógica de las acciones MOVER y ROTAR
     */
    private void checkHunterPosition() {
        Point pos = hunter.getCurrPos();

        // Si el Wumpus no ha muerto y el cazador ha caï¿½do en su casilla, la partida
        // termina
        if (!isWumpusDead && board.isWumpusInCell(pos)) {
            gameFinished(ActionResult.WUMPUS_FOUND);
            return;
        }

        // Si el cazador ha caído en un pozo, la partida termina
        if (board.isPitInCell(pos)) {
            gameFinished(ActionResult.PIT_FOUND);
            return;
        }

        List<ActionResult> results = new ArrayList<>();

        // Si el cazador no tiene el lingote de oro y ha caído en la casilla del lingote
        // de oro
        if (!hunter.hasGold() && board.isGoldInCell(pos)) {
            results.add(ActionResult.GOLD_FOUND);
            hunter.takeGold();
        }

        // Si el cazador ha caído en una casilla que huele a Wumpus
        if (!isWumpusDead && board.isWumpusNear(pos)) {
            results.add(ActionResult.WUMPUS_ODOR);
        }

        // Si el cazador ha caído en una casilla con un pozo
        if (board.isPitNear(pos.x, pos.y)) {
            results.add(ActionResult.PIT_BREEZE);
        }

        // Si hay mensajes que montrar, los mostramos
        if (!results.isEmpty()) {
            Utils.printMessage(EnumSet.copyOf(results));
        }
    }

    /**
     * Método que gestiona la lógica de la acción LANZAR FLECHA
     */
    private void throwArrow() {
        // Si el cazador no tiene flechas...
        if (!hunter.throwArrow()) {
            Utils.printMessage(EnumSet.of(ActionResult.NO_ARROWS));
            return;
        }

        // Si el Wumpus no está muerto y la flecha lo alcanza...
        if (!isWumpusDead && board.arrowKillsWumpus(hunter.getCurrPos(), hunter.getDirection())) {
            Utils.printMessage(EnumSet.of(ActionResult.WUMPUS_DEAD));
            isWumpusDead = true;
            return;
        }

        // La flecha ha chocado contra una pared
        Utils.printMessage(EnumSet.of(ActionResult.ARROW_FAILED));
    }

    /**
     * Método que gestiona la lógica de la acción SALIR
     */
    private void exit() {
        // Si el cazador está en la casilla de salida...
        if (hunter.getCurrPos().equals(startingPos)) {
            // ...no tiene el lingote de oro
            if (!hunter.hasGold()) {
                gameFinished(ActionResult.GOLD_REQUIRED);
                return;
            }

            // ...y tiene el lingote de oro
            gameFinished(ActionResult.HUNTER_WINS);
            return;
        }

        Utils.printMessage(EnumSet.of(ActionResult.NO_EXIT));
    }

    /**
     * Método que finaliza el juego
     * 
     * @param result Resultado de la acción
     */
    private void gameFinished(ActionResult result) {
        phase = GamePhase.NONE;
        isWumpusDead = false;
        numCells = -1;
        numPits = -1;
        numArrows = -1;

        Utils.printMessage(EnumSet.of(result));

        board.print();
    }
}
