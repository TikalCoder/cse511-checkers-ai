package checkers.model;

import java.util.ArrayList;

/**
 * Abstract base class representing a complete move (either a walk, or a
 * complete jump sequence).
 *
 * @author Kurt Glastetter
 */
public abstract class Move {
	protected ArrayList<Integer> sequence;

	private boolean movingKing;

	protected Move(int startPos, int nextPos, boolean movingKing) {
		sequence = new ArrayList<Integer>(10);
		sequence.add(startPos);
		sequence.add(nextPos);
		this.movingKing = movingKing;
	}

	public abstract boolean isJump();

	public boolean movingKing() {
		return movingKing;
	}

	public int startPos() {
		return sequence.get(0);
	}

	public int endPos() {
		return sequence.get(sequence.size() - 1);
	}

	public ArrayList<Integer> getSequence() {
		return sequence;
	}

	public String toString() {
		char delim = isJump() ? 'x' : '-';

		String ret = (movingKing ? "K" : "") + sequence.get(0).toString();

		for (int pos = 1; pos < sequence.size(); ++pos) {
			ret += delim;
			ret += sequence.get(pos);
		}

		return ret;
	}

	public boolean equals(Object other) {
		if (!(other instanceof Move)) return false;
		Move otherMove = (Move)other;
		return sequence.equals(otherMove.sequence)
				&& movingKing == otherMove.movingKing;
	}

	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + sequence.hashCode();
		hash = hash * 31 + (movingKing ? 2 : 1 );
		return hash;
	}
}
