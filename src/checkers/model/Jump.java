package checkers.model;

import java.lang.IllegalArgumentException;

/**
 * A Move that represents a complete jump sequence.
 *
 * @author Kurt Glastetter
 */
public class Jump extends Move {
	public Jump(int startPos, int landPos) {
		super(startPos, landPos);

		if (!Board.areJumpable(startPos, landPos))
			throw new IllegalArgumentException(
					"invalid jump beginning " + toString());
	}

	public boolean isJump() {
		return true;
	}

	public void jumpAgain(int landPos) {
		if (!Board.areJumpable(sequence.get(sequence.size() - 1), landPos))
			throw new IllegalArgumentException(
					"invalid jump continuation " + toString());

		sequence.add(landPos);
	}
}
