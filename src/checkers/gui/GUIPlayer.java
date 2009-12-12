package checkers.gui;

import checkers.Player;
import checkers.model.GameState;
import checkers.model.Move;

public class GUIPlayer extends Player {
	private BoardUI gui;
	
	public GUIPlayer(BoardUI boardUI) {
		this.gui = boardUI;
	}

	@Override
	public synchronized Move chooseMove(GameState state) {
		gui.allowedToMove(state, this);
		try {
			gui.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("done waiting");
		return null;
	}
}
