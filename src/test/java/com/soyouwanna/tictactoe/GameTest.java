package com.soyouwanna.tictactoe;

import com.soyouwanna.tictactoe.exceptions.CantPlayException;
import com.soyouwanna.tictactoe.exceptions.CantPlayTwiceException;
import com.soyouwanna.tictactoe.exceptions.CantPlayWhereComputerPlayedException;
import com.soyouwanna.tictactoe.exceptions.GameEndedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.soyouwanna.tictactoe.TicTac.O;
import static com.soyouwanna.tictactoe.TicTac.X;
import static com.soyouwanna.tictactoe.Winner.YOU;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game g;

    @BeforeEach
    void setUp() {
        g = new Game();
    }

    @Nested
    class OnEmptyField {

        @Test
        void playTopLeft() {
            int playPosition = 1;
            assertDoesNotThrow(() -> g.play(playPosition));
            assertTrue(g.getYouPlayed(playPosition));
        }

        @Test
        void playCenter() {
            int playPosition = 5;
            assertDoesNotThrow(() -> g.play(playPosition));
            assertTrue(g.getYouPlayed(playPosition));
        }

        @Test
        void playBottomRight() {
            int playPosition = 9;
            assertDoesNotThrow(() -> g.play(playPosition));
            assertTrue(g.getYouPlayed(playPosition));
        }

        @AfterEach
        void tearDown() {
            assertFalse(g.gameEnded());
        }
    }

    @Test
    void cantPlaySameCellTwice() {
        int playPosition = 1;
        Executable moveToPlayTwice = () -> g.play(playPosition);
        assertDoesNotThrow(moveToPlayTwice);
        assertThrows(CantPlayTwiceException.class, moveToPlayTwice);
    }

    @Test
    void cantPlayWhereComputerHasPlayed() {
        int playPosition = 1;
        TicTac[] board = g.getBoard();
        board[playPosition-1] = TicTac.O;
        assertThrows(CantPlayWhereComputerPlayedException.class, () -> g.play(playPosition));
    }

    @Test
    void whenBoardFull_gameEnds() {
        assertDoesNotThrow(this::playDraw);
        assertTrue(g.gameEnded());
        assertThrows(GameEndedException.class, () -> g.play(1));
        assertThrows((GameEndedException.class), () -> g.computerPlays());
    }

    private void playDraw() throws CantPlayTwiceException, GameEndedException {
        TicTac[] board = g.getBoard();
        board[0] = X;
        board[1] = X;
        board[2] = O;

        board[3] = O;
        board[4] = X;
        board[5] = X;

        board[6] = X;
        board[7] = O;
        board[8] = O;
    }


    @Nested
    class DetermineWhoseTurn {

        @Test
        void newGame_yourTurn() {
            assertTrue(g.yourTurn());
        }

        @Test
        void youMovedOnce_ComputerTurn() throws CantPlayException {
            g.play(1);
            assertTrue(g.computerTurn());
        }

        @Test
        void youPlay_ThenComputerPlays_yourTurnNext() throws CantPlayException {
            g.play(1);
            g.computerPlays();
            assertTrue(g.yourTurn());
        }
    }

    @Nested
    class CheckDiffBetweenMoves {

        @Test
        void movedOnce() throws CantPlayException {
            TicTac[] boardBefore = cloneBoard();
            g.play(1);
            TicTac[] boardAfter = cloneBoard();

            assertEquals(1, diffCount(boardBefore, boardAfter));
        }

        @Test
        void movedTwice() throws CantPlayException {   // shouldn't be able to do this, but perhaps we don't need to test it.
            TicTac[] boardBefore = cloneBoard();
            g.play(1);
            g.play(5);
            TicTac[] boardAfter = cloneBoard();

            assertEquals(2, diffCount(boardBefore, boardAfter));
        }
    }

    @Test
    void computerMoves_OneExtraTacAppearsOnBoard() throws CantPlayException {
        g.play(1);
        TicTac[] boardBefore = cloneBoard();
        g.computerPlays();
        TicTac[] boardAfter = cloneBoard();

        assertEquals(1, getToeDiff(TicTac.O, boardBefore, boardAfter));
    }

    @Nested
    class CheckWinner {

        @Test
        void gamePlayedPastVictory() throws CantPlayException {
            g.play(1);
            g.play(2);
            g.play(3);
            assertTrue(g.gameEnded());
            assertThrows(GameEndedException.class, () -> g.play(4));
        }

        @Test
        void newGame_noWinner() {
            assertEquals(Winner.DRAW, g.getWinner());
        }

        @ParameterizedTest
        @CsvSource({"1, 2, 3",
                    "4, 5, 6",
                    "7, 8, 9",
                    "1, 4, 7",
                    "2, 5, 8",
                    "3, 6, 9",
                    "1, 5, 9",
                    "3, 5, 7"})
        void winner_testAll8WinningWays(int one, int two, int three) throws CantPlayException {
            g.play(one);
            g.play(two);
            g.play(three);
            assertTrue(g.gameEnded());
            assertEquals(YOU, g.getWinner());
        }
    }

    private int getToeDiff(TicTac toeType, TicTac[] boardBefore, TicTac[] boardAfter) {
        int toeCountBefore = Game.getToeCount(toeType, boardBefore);
        int toeCountAfter = Game.getToeCount(toeType, boardAfter);
        return toeCountAfter - toeCountBefore;
    }

    private TicTac[] cloneBoard() {
        TicTac[] boardSnapshot = new TicTac[9];
        System.arraycopy(g.getBoard(), 0, boardSnapshot, 0, 9);
        return boardSnapshot;
    }

    private int diffCount(TicTac[] boardBefore, TicTac[] boardAfter) {
        int diffCount = 0;
        for (int i=0; i<9; i++) {
            if (cellsDiffer(boardBefore[i], boardAfter[i])) {
                diffCount++;
            }
        }
        return diffCount;
    }

    private boolean cellsDiffer(TicTac one, TicTac another) {
        return one != another;
    }
}