package checkers.gui;

import checkers.Player;
import checkers.model.GameState;
import checkers.model.Move;

public class GUIPlayer extends Player {
	private Frame gui;
	
	public GUIPlayer(Frame gui) {
		this.gui = gui;
	}

	@Override
	public Move chooseMove(GameState state) {
		// TODO Auto-generated method stub
		return null;
	}
}
