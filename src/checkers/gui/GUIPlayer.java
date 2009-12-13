package checkers.gui;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;

import checkers.Player;
import checkers.model.Board;
import checkers.model.GameState;
import checkers.model.Jump;
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
		GameState stateClone = (GameState) state.clone();
		ArrayList<Integer> moveSequence = new ArrayList<Integer>();
		do {
			synchronized (this) {
				gui.allowedToMove(stateClone, this);
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (moveSequence.isEmpty())
				moveSequence.add(nextStartPos);
			moveSequence.add(nextEndPos);
		} while (!stateClone.makeSingleMove(nextStartPos, nextEndPos));

		return state.generateMove(moveSequence);
	}

	public void setNextMove(int startPos, int endPos) {
		this.nextStartPos = startPos;
		this.nextEndPos = endPos;
	}
}
