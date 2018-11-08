package org.chs.app;

import java.util.Scanner;

public class Wumpus {

    public static void main(String[] args) {

        System.out.println();
        System.out.println("=========================");
        System.out.println("###  HUNT THE WUMPUS  ###");
        System.out.println("=========================");
        System.out.println();

        Game game = new Game();

        Scanner keyboard = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            game.promptGameParams();

            String input = keyboard.nextLine();
            if (input != null) {
                String command = input.toUpperCase();

                exit = game.processCommand(command);
                
                if (exit) {
                    System.out.println();
                    System.out.println("Juego terminado ------------------");
                    System.out.println();
                }
            }
        }

        keyboard.close();
    }
}
