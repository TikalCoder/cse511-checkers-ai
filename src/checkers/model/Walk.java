package checkers.model;

public class Walk extends Move {
	public Walk(int startPos, int endPos) {
		super(startPos, endPos);
		assert Board.areWalkable(startPos, endPos);
	}

	public boolean isJump() {
		return false;
	}
}
