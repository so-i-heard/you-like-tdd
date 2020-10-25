package com.soyouwanna.tictactoe.exceptions;

public class CantPlayTwiceException extends CantPlayException {
    public CantPlayTwiceException() {
        super("You have already made this move.");
    }
}
