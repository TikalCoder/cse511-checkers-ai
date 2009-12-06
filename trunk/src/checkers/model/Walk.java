package checkers.model;

import java.lang.IllegalArgumentException;

public class Walk extends Move {
	public Walk(int startPos, int endPos) {
		super(startPos, endPos);

		if (!Board.areWalkable(startPos, endPos))
			throw new IllegalArgumentException(
					"invalid walk move " + toString());
	}

	public boolean isJump() {
		return false;
	}
}
