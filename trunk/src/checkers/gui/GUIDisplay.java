package checkers.gui;

import checkers.Display;
import checkers.model.GameState;
import checkers.model.Move;

public class GUIDisplay extends Display {
	private BoardUI gui;
	
	public GUIDisplay(BoardUI boardUI) {
		this.gui = boardUI;
	}

	@Override
	public void init(GameState newState) {
		gui.setGameState(newState);
		gui.repaint();
	}

	@Override
	public void update(Move move, GameState newState) {
		gui.setGameState(newState);
		gui.repaint();
	}

}
