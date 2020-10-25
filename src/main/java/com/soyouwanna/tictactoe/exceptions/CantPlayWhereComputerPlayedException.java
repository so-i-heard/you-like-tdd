package com.soyouwanna.tictactoe.exceptions;

public class CantPlayWhereComputerPlayedException extends CantPlayException {
    public CantPlayWhereComputerPlayedException() {
        super("Computer has already played this square.");
    }
}
