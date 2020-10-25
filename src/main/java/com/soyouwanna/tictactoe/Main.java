package com.soyouwanna.tictactoe;

import com.soyouwanna.tictactoe.exceptions.CantPlayException;
import com.soyouwanna.tictactoe.exceptions.GameEndedException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        printIntroStuffs(game);

        Scanner scanner = new Scanner(System.in);
        while (!game.gameEnded()) {
            if (youPlayOrBreakIfExit(game, scanner)) break;
            if (computerPlaysOrBreakIfGameOver(game)) break;
            printPostRoundBoardState(game);
        }
        printEndGameMessage(game);
    }

    private static boolean youPlayOrBreakIfExit(Game game, Scanner scanner) {
        System.out.println("Your turn:");
        try {
            readInputAndPlay(scanner, game);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
        return false;
    }

    private static boolean computerPlaysOrBreakIfGameOver(Game game) {
        if (game.gameEnded()) {
            return true;
        } else {
            try {
                game.computerPlays();
            } catch (GameEndedException e) {
                System.out.println("Please inform developer they've reached 'should never reach this point' point.");
            }
        }
        return false;
    }

    private static void printIntroStuffs(Game game) {
        printGameTitle();
        System.out.print(game.getInitialBoardUI());
        System.out.println("Why don't you begin. Positions are printed above.");
    }

    private static void printEndGameMessage(Game game) {
        System.out.println();
        if (!game.gameEnded()) {
            System.out.println("See you next time, human.");
        } else {
            printResults(game);
        }
    }

    private static void printResults(Game game) {
        System.out.println("                         " + "--= Final board =--");
        System.out.println(game.getGameBoardGUI());
        if (game.getWinner() == Winner.COMPUTER) {
            System.out.println("                         " + "--= WOPR wins =--");
        } else if (game.getWinner() == Winner.YOU) {
            System.out.println("                         " + "--= You win! =--");
        } else {
            System.out.println("                         " + "--= It's a draw =--");
        }
    }

    private static void printPostRoundBoardState(Game game) {
        System.out.println();
        System.out.println("Computer has made a move.");
        System.out.print(game.getGameBoardGUI());
    }

    private static void printGameTitle() {
        System.out.println();
        System.out.println("                 ----============================----                 ");
        System.out.println("  ----==========================================================----  ");
        System.out.println("--==  Hello, my name is WOPR. Let's play a game of Tic Tac Toe  ==--");
        System.out.println("  ----==========================================================----  ");
        System.out.println("                 ----============================----                 ");
    }

    private static void readInputAndPlay(Scanner scanner, Game game) {
        Integer position = null;
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if ("exit".equalsIgnoreCase(nextLine)) {
                throw new RuntimeException("Game terminated by human.");
            } else {
                try {
                    position = getPosition(nextLine);
                    try {
                        game.play(position);
                        break;
                    } catch (CantPlayException e) {
                        System.out.println(e.getMessage() + " Please try again:");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage() + " Please try again:");
                }
            }
        }
    }

    private static int getPosition(String line) {
        try {
            int pos = Integer.parseInt(line);
            if (pos < 1 || pos > 9)
                throw new NumberFormatException("You entered a number out of [1..9] range.");
            return pos;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Please enter a number 1 to 9. " + e.getMessage(), e);
        }
    }
}
