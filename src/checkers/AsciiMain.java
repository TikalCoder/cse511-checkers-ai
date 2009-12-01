package checkers;

import java.util.ArrayList;

import checkers.model.Board;
import checkers.model.Move;
import checkers.model.Board.PlayerId;
import checkers.print.PrettyBoardPrinter;

public class AsciiMain {
	public static void main(String[] args) {
        Board board = new Board();

        System.out.println("\n");
        new PrettyBoardPrinter().print(board);

        System.out.print("  ___\n" +
                         " (   )\n" +
                         "  \"\"\"  to move.  Possible moves are: ");

        ArrayList<Move> possibleMoves = board.possibleMoves(PlayerId.BLACK);
        for (int i = 0; i < possibleMoves.size(); ++i) {
            if (i != 0) System.out.print(", ");
            System.out.print(possibleMoves.get(i));
        }
        System.out.println(".");
    }
}
