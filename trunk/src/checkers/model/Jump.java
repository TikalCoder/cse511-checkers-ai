package checkers.model;

//TODO: replace assertions with IllegalArgumentExceptions

public class Jump extends Move {
	public Jump(int startPos, int landPos) {
		super(startPos, landPos);
		assert Board.areJumpable(startPos, landPos);
	}

	public boolean isJump() {
		return true;
	}

	public void jumpAgain(int landPos) {
		assert Board.areJumpable(sequence.get(sequence.size() - 1), landPos);
		sequence.add(landPos);
	}
}
