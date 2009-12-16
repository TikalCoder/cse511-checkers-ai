package checkers.model;

import java.lang.IllegalArgumentException;

import checkers.model.Board;

/**
 * Represents a single move of a piece from one square to another.  This may
 * be a walk move, or it may be a single jump within a complete jump move.
 */
public class SingleMove {
	public int startPos;
	public int endPos;

	public SingleMove(int startPos, int endPos) {
		if (!Board.areWalkable(startPos, endPos)
				&& !Board.areJumpable(startPos, endPos)) {
			throw new IllegalArgumentException("invalid SingleMove specification");
		}

		this.startPos = startPos;
		this.endPos = endPos;
	}

	boolean isJump() {
		return Board.areJumpable(startPos, endPos);
	}

	int startPos() { return startPos; }
	int endPos() { return endPos; }
	
	public String toString() {
		return startPos + (isJump() ? "x" : "-") + endPos;
	}
}
