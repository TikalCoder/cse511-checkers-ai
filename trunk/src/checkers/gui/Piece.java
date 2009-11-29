package checkers.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import checkers.Constants;

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
			g.setColor(new Color(255, player == 0 ? 0 : 255, player == 0 ? 0 : 255));
			g.fill(new Ellipse2D.Double(Constants.PADDING / 2, Constants.PADDING / 2, size, size));
		}
	}
}
