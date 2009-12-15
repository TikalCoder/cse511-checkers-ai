package checkers.model;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;

/**
 * A Move that represents a complete jump sequence.
 *
 * @author Kurt Glastetter
 */
public class Jump extends Move {
	private ArrayList<Boolean> jumpedKings;

	public Jump(int startPos, int landPos,
				boolean movingKing, boolean jumpedKing) {
		super(startPos, landPos, movingKing);

		if (!Board.areJumpable(startPos, landPos))
			throw new IllegalArgumentException(
					"invalid jump beginning " + this);

		jumpedKings = new ArrayList<Boolean>(9);
		jumpedKings.add(jumpedKing);
	}

	public boolean isJump() {
		return true;
	}

	public void jumpAgain(int landPos, boolean jumpedKing) {
		if (!Board.areJumpable(sequence.get(sequence.size() - 1), landPos))
			throw new IllegalArgumentException(
					"invalid jump continuation " + this + "x" + landPos);

		sequence.add(landPos);
		jumpedKings.add(jumpedKing);
	}

	public ArrayList<Boolean> jumpedKings() {
		return jumpedKings;
	}
}
