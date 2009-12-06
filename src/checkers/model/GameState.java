package checkers.model;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;

/**
* Maintains the complete information about the current state of a
* checkers game.  User interfaces and AI players should be able to query
* this object for whatever they need to know.
*/
public class GameState {
	/**
	* This is whose turn it is.
	*/
	private PlayerId playerToMove;

	/**
	* If this state is currently in the middle of a sequence of jumps,
	* this will identify the position of the currently jumping piece.
	* In other words, it will be the position of a piece that is the
	* only one allowed to move (jump).  Otherwise, it will be zero.
	*/
	private int jumper;

	/**
	* The state of the board, and all the pieces.
	*/
	private Board board;

	/**
	* Constructor initializes to the start of a new game.
	*/
	public GameState() {
		playerToMove = PlayerId.BLACK;
		jumper = 0;
		board = new Board();
	}

	public boolean gameIsOver() {
		return board.possibleMoves(playerToMove).size() == 0;
	}

	public PlayerId playerToMove() {
		return playerToMove;
	}

	public ArrayList<Move> possibleMoves() {
		return board.possibleMoves(playerToMove);
	}

	public void makeMove(Move move) {
		if (!board.possibleMoves(playerToMove).contains(move))
			throw new IllegalArgumentException("impossible move " + move);

		//TODO: simply board.makeMove(move) ?
		ArrayList<Integer> sequence = move.getSequence();
		for (int i = 0; i < sequence.size() - 1; ++i)
		{
			board.makeSingleMove(sequence.get(i), sequence.get(i + 1));
		}

		playerToMove = playerToMove.opponent();
	}

	public Board getBoard() {
		return board;
	}
}
