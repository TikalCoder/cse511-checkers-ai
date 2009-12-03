package checkers.model;

/**
* Maintains the complete information about the current state of a
* checkers game.  User interfaces and AI players should be able to query
* this object for whatever they need to know.
*/
class GameState {
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
	GameState() {
		playerToMove = PlayerId.BLACK;
		jumper = 0;
		board = new Board();
	}

	//TODO: add lots of accessors to query the game state, what moves
	//      are valid, etc., and mutators to make the moves.
}
