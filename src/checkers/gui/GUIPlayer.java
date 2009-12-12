package checkers.gui;

import checkers.Player;
import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.Walk;

public class GUIPlayer extends Player {
	private BoardUI gui;
	
	private int nextStartPos;
	private int nextEndPos;

	public GUIPlayer(BoardUI boardUI) {
		this.gui = boardUI;
		this.nextStartPos = 0;
		this.nextEndPos = 0;
	}

	@Override
	public Move chooseMove(GameState state) {
		synchronized (this) {
			gui.allowedToMove(state, this);
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("done waiting");
		}
		return new Walk(nextStartPos, nextEndPos);
	}

	public void setNextMove(int startPos, int endPos) {
		this.nextStartPos = startPos;
		this.nextEndPos = endPos;
	}
}
