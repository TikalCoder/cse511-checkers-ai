package checkers.gui;

import static checkers.Constants.GRID_SIZE;
import static checkers.Utils.gridToPosition;
import static checkers.Utils.validSquare;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import checkers.Constants;
import checkers.Utils;
import checkers.model.Board;
import checkers.model.Move;
import checkers.model.PlayerId;
import checkers.model.Board.PositionState;
import checkers.print.PrettyBoardPrinter;

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
	 * Keep track of the square selected.
	 */
	private Point selectedSquare;

	/**
	 * Keep track of the piece at the square selected, if any.
	 */
	private PositionState selectedState;

	/**
	 * Board model.
	 */
	private Board board;
	
	private PlayerId playerToMove;

	public BoardUI() {
		this.board = new Board();
		this.playerToMove = PlayerId.BLACK;

		setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				// Keep track of last selection
				Point oldSquare = selectedSquare;
				PositionState oldState = selectedState;

				// Determine the newly selected square
				int cellWidth = getWidth() / GRID_SIZE;
				int cellHeight = getHeight() / GRID_SIZE;
				int x = me.getX() / cellWidth;
				int y = me.getY() / cellHeight;
				selectedSquare = new Point(x, y);
				int selectedIndex = gridToPosition(selectedSquare);
				if (selectedIndex == -1) // ignore clicks on invalid locations
					return;
				selectedState = board.stateAt(selectedIndex);


				if (selectedSquare.equals(oldSquare)) {
					selectedState = null;
					selectedSquare = null;
				} else if (validSquare(x, y) && oldSquare != null
						&& oldState != PositionState.EMPTY
						&& selectedState == PositionState.EMPTY) {
					// try to make a move from oldSquare to selectedSquare in Board (model)...
					int oldIndex = gridToPosition(oldSquare);
					System.out.println("Make move from: " + oldIndex + " to: " + selectedIndex);
					if (board.makeSingleMove(oldIndex, selectedIndex))
						playerToMove = playerToMove.opponent();
//					new PrettyBoardPrinter().print(board);
				}

				// Update the display
				repaint();
			}
		});
	}

	/**
	 * Draw the board with black and light gray cells.
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;

		int cellWidth = getWidth() / GRID_SIZE;
		int cellHeight = getHeight() / GRID_SIZE;
		int pieceWidth = (getWidth() - (Constants.PADDING * 8)) / GRID_SIZE;
		int pieceHeight = (getHeight() - (Constants.PADDING * 8)) / GRID_SIZE;
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				int color = validSquare(i, j) ? 152 : 0;
				g.setColor(new Color(color, color, color));
				g.fillRect(cellWidth * i, cellHeight * j, cellWidth, cellHeight);
			}
		}

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				drawPossibleMoves(g, cellWidth, cellHeight, pieceWidth, pieceHeight);
			}
		}

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				int color = validSquare(i, j) ? 152 : 0;
				g.setColor(new Color(color, color, color));
//				g.fillRect(cellWidth * i, cellHeight * j, cellWidth, cellHeight);

				if (validSquare(i, j) && board.stateAt(gridToPosition(i, j)) != PositionState.EMPTY) { // if looking at a cell with a piece
//					drawPossibleMoves(g, cellWidth, cellHeight, pieceWidth, pieceHeight);
					
					highlightSelectedPiece(g, cellWidth, cellHeight, pieceWidth, pieceHeight, i, j);

					drawAllPieces(g, cellWidth, cellHeight, pieceWidth, pieceHeight, i, j);
				}
			}
		}
		
		
	}

	/**
	 * Helper method for paintComponent that draws the selected piece, if there is one. 
	 */
	private void highlightSelectedPiece(Graphics2D g, int cellWidth, int cellHeight, int pieceWidth, int pieceHeight, int x, int y) {
		if (new Point(x, y).equals(selectedSquare)) {
			PositionState state = board.stateAt(gridToPosition(x, y));
			if (state.hasBlackPiece())
				g.setColor(new Color(0, 255, 255));
			else if (state.hasWhitePiece())
				g.setColor(new Color(0, 0, 255));
			final int outlinePadding = 3;
			g.fill(new Ellipse2D.Double(cellWidth * x + Constants.PADDING / 2.0 - outlinePadding, cellHeight * y + Constants.PADDING / 2.0 - outlinePadding, pieceWidth + 2 * outlinePadding, pieceHeight + 2 * outlinePadding));
		}
	}

	/**
	 * Helper method for paintComponent that draws all the pieces on the board. 
	 */
	private void drawAllPieces(Graphics2D g, int cellWidth, int cellHeight,int pieceWidth, int pieceHeight, int i, int j) {
		PositionState state = board.stateAt(gridToPosition(i, j));
		if (state.hasBlackPiece())
			g.setColor(new Color(0, 0, 0));
		else if (state.hasWhitePiece())
			g.setColor(new Color(255, 255, 255));

		double x = cellWidth * i + Constants.PADDING / 2.0;
		double y = cellHeight * j + Constants.PADDING / 2.0;
		g.fill(new Ellipse2D.Double(x, y, pieceWidth, pieceHeight));
	}
	
	private void drawPossibleMoves(Graphics2D g, int cellWidth, int cellHeight, int pieceWidth, int pieceHeight) {
		ArrayList<Move> possibleMoves = board.possibleMoves(playerToMove);
		for (Move move : possibleMoves) {
			ArrayList<Integer> sequence = move.getSequence();
			for (int i = 0; i < sequence.size(); i++) {
				int positionIndex = sequence.get(i);
				Point p = Utils.positionToGridPoint(positionIndex);
				final int outlinePadding = 3;
				if (i == 0) {
					g.setColor(new Color(255, 140, 0));
					g.fill(new Ellipse2D.Double(cellWidth * p.getX() + Constants.PADDING / 2.0 - outlinePadding, cellHeight * p.getY() + Constants.PADDING / 2.0 - outlinePadding, pieceWidth + 2 * outlinePadding, pieceHeight + 2 * outlinePadding));
				} else if (i >= 1) {
					g.setColor(new Color(255, 0, 0));
					g.fill(new Ellipse2D.Double(cellWidth * p.getX() + Constants.PADDING / 2.0 - outlinePadding, cellHeight * p.getY() + Constants.PADDING / 2.0 - outlinePadding, pieceWidth + 2 * outlinePadding, pieceHeight + 2 * outlinePadding));
					
					g.setColor(new Color(152, 152, 152));
					g.fill(new Ellipse2D.Double(cellWidth * p.getX() + Constants.PADDING / 2.0, cellHeight * p.getY() + Constants.PADDING / 2.0, pieceWidth, pieceHeight));
				}
			}
		}
	}
}
