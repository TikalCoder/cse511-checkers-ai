package checkers.model;

public class Jump extends Move {
	public Jump(int startPos, int landPos) {
		super(startPos, landPos);
		assert Board.areJumpable(startPos, landPos);
	}

	public boolean isJump() {
		return true;
	}

	public void jumpAgain(int landPos) {
		sequence.add(landPos);
	}
}
