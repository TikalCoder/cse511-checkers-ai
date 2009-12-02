package checkers.model;

import java.util.ArrayList;

public abstract class Move {
	protected ArrayList<Integer> sequence;

	protected Move(int startPos, int nextPos) {
		sequence = new ArrayList<Integer>(2);
		sequence.add(startPos);
		sequence.add(nextPos);
	}

	public abstract boolean isJump();

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

		String ret = sequence.get(0).toString();

		for (int pos = 1; pos < sequence.size(); ++pos) {
			ret += delim;
			ret += sequence.get(pos);
		}

		return ret;
	}
}
