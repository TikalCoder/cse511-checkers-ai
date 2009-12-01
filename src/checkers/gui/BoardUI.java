package checkers.gui;

import static checkers.Constants.GRID_SIZE;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import checkers.Constants;

/**
 * This class represents a Checkers board with Piece objects on it.
 * 
 * @author Scott Bressler
 */
public class BoardUI extends JPanel {
	
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Keep a 2D-array of the Piece objects on the board.
	 */
	private Piece[][] pieces;
	
	/**
	 * Keep track of the piece selected, if any.
	 */
	private int lastClickX, lastClickY;

	public BoardUI() {
		setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		setLayout(new GridBagLayout());
		
		pieces = new Piece[GRID_SIZE][GRID_SIZE];
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.ipadx = Constants.PADDING;
		constraints.ipady = Constants.PADDING;
		
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				// set the location of each piece in the GridBagConstraints
				constraints.gridx = i;
				constraints.gridy = j;
				
				// set player to 0 if in top 3 rows, 1 if in bottom 3 rows, and -1
				// otherwise
				final int player = (j <= 2) ? 0 : (j >= 5) ? 1 : -1;
				
				int numerator = Constants.WIDTH - (Constants.PADDING * 8);
				add(pieces[i][j] = new Piece(numerator/GRID_SIZE, player), constraints);
			}
		}
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				// determine the selected piece
				// TODO: add logic to move pieces around
				int cellSize = Constants.WIDTH / GRID_SIZE;
				int lastClickX = me.getX() / cellSize;
				int lastClickY = me.getY() / cellSize;
			}
		});
	}
	
	/**
	 * Draw the board with black and light gray cells.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// TODO: make individual pieces resize in Piece.java, then allow grid cells on
		// board to resize by uncommenting below code. 
//		int cellWidth = getWidth() / GRID_SIZE;
//		int cellHeight = getHeight() / GRID_SIZE;

		// TODO: when above commented code is uncommented, comment this out
		int cellWidth = Constants.WIDTH / GRID_SIZE;
		int cellHeight = Constants.WIDTH / GRID_SIZE;;
		
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				int color = (i + j) % 2 != 0 ? 0 : 192;
				g.setColor(new Color(color, color, color));
				g.fillRect(cellWidth * i, cellHeight * j, cellWidth, cellHeight);
			}
		}
	}
}
