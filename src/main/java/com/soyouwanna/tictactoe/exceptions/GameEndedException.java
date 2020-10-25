package com.soyouwanna.tictactoe.exceptions;

public class GameEndedException extends CantPlayException {
    public GameEndedException() {
        super("Game has ended.");
    }
}
