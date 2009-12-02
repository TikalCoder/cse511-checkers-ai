package checkers.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import checkers.Constants;
import checkers.model.Board.PositionState;

/**
 * This class represents a Checkers piece on a Checkers board.
 * 
 * @author Scott Bressler
 */
public class Piece extends JPanel {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	private int size;
	private int player;
	private boolean selected;
	private PositionState state;

	public Piece(final int size) {
		this.size = size;
		this.state = PositionState.EMPTY;
		
		setPreferredSize(new Dimension(size, size));
		
		// Make pieces transparent so that underlying board will appear below
		setOpaque(false);
	}

	public Piece(final int size, final int player) {
		this.size = size;
		this.player = player;
		
		setPreferredSize(new Dimension(size, size));
		
		// Make pieces transparent so that underlying board will appear below
		setOpaque(false);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D g = (Graphics2D) graphics;

		// If the piece being painted belongs to a valid player, paint it.
		// Only paint pieces every other square, following the rules of Checkers.
		if (player > -1) {
			// Set the color of the piece to be red for player 0 and white for player 1
//			if (selected)
//				g.setColor(new Color(0, 255, 0));
//			else
			

			if (selected) {
//				if (state.hasBlackPiece())
				if (player == 1)
					g.setColor(new Color(255, 0, 0));
//				else if (state.hasWhitePiece())
				else
					g.setColor(new Color(255, 255, 0));
				
				final int outlinePadding = 3;
				g.fill(new Ellipse2D.Double(Constants.PADDING / 2.0 - outlinePadding, Constants.PADDING / 2.0 - outlinePadding, size + 2 * outlinePadding, size + 2 * outlinePadding));
			}
			
			g.setColor(new Color(255, player == 0 ? 0 : 255, player == 0 ? 0 : 255));
			g.fill(new Ellipse2D.Double(Constants.PADDING / 2.0, Constants.PADDING / 2.0, size, size));
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}
}
