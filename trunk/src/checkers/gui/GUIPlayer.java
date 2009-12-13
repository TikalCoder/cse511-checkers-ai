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
		GameState stateClone = null;
		try {
			 stateClone = (GameState) state.clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
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
		
		return moveSequenceToMove(moveSequence);
	}

	public void setNextMove(int startPos, int endPos) {
		this.nextStartPos = startPos;
		this.nextEndPos = endPos;
	}

	private static Move moveSequenceToMove(ArrayList<Integer> moveSequence) {
		if (Board.areJumpable(moveSequence.get(0), moveSequence.get(1))) {
			return moveSequenceToJump(moveSequence);
		} else if (Board.areWalkable(
				moveSequence.get(0), moveSequence.get(1))) {
			return moveSequenceToWalk(moveSequence);
		} else {
			throw new IllegalArgumentException("invalid move sequence");
		}
	}

	private static Walk moveSequenceToWalk(ArrayList<Integer> moveSequence) {
		if (moveSequence.size() != 2) {
			throw new IllegalArgumentException("walk move can only have length 2");
		}
		return new Walk(moveSequence.get(0), moveSequence.get(1));
	}

	private static Jump moveSequenceToJump(ArrayList<Integer> moveSequence) {
		Jump jump = new Jump(moveSequence.get(0), moveSequence.get(1));

		for (int i = 2; i < moveSequence.size(); ++i) {
			jump.jumpAgain(moveSequence.get(i));
		}

		return jump;
	}
}
