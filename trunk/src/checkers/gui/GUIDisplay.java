package checkers.gui;

import checkers.Display;
import checkers.model.GameState;
import checkers.model.Move;

public class GUIDisplay extends Display {
	private Frame gui;
	
	public GUIDisplay(Frame gui) {
		this.gui = gui;
	}

	@Override
	public void init(GameState newState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Move move, GameState newState) {
		gui.repaint();
	}

}
