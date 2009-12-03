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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import checkers.Constants;
import checkers.Utils;
import checkers.model.Board;
import checkers.model.Jump;
import checkers.model.Move;
import checkers.model.PlayerId;
import checkers.model.Walk;
import checkers.model.Board.PositionState;

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
	 * Board model.
	 */
	private Board board;

	private PlayerId playerToMove;

	private BufferedImage kingImg;
	
	public BoardUI() {
		this.board = new Board();
		this.playerToMove = PlayerId.BLACK;

		setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		kingImg = null;
		try {
		    kingImg = ImageIO.read(new File("crown-smaller.png"));
		} catch (IOException e) {}

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				// Keep track of last selection
				Point oldSquare = selectedSquare;
				PositionState oldState = null;
				int oldIndex = -1;
				if (oldSquare != null) {
					oldIndex = gridToPosition(oldSquare);
					oldState = board.stateAt(oldIndex);
				}
				
				// Determine the newly selected square
				int cellWidth = getWidth() / GRID_SIZE;
				int cellHeight = getHeight() / GRID_SIZE;
				int x = me.getX() / cellWidth;
				int y = me.getY() / cellHeight;
				Point selection = new Point(x, y);
				int selectedIndex = gridToPosition(selection);
				if (selectedIndex == -1 ||
						board.stateAt(selectedIndex).hasPlayersPiece(playerToMove.opponent())) // ignore clicks on invalid locations
					return;
				selectedSquare = selection;
				PositionState selectedState = board.stateAt(selectedIndex);
				
				if (selectedSquare.equals(oldSquare)) {
					clearSelection();
				} else if (validSquare(x, y) && oldSquare != null
						&& oldState != PositionState.EMPTY
						&& selectedState == PositionState.EMPTY) {
					// try to make a move from oldSquare to selectedSquare in Board (model)...
					System.out.println("Make move from: " + oldIndex + " to: " + selectedIndex);
					if (board.makeSingleMove(oldIndex, selectedIndex)) {
						playerToMove = playerToMove.opponent();
						clearSelection();
					} /* if no move was made at all, change selected square back to oldSquare.
					   * This will happen if clicking from a square with a piece to a square that
					   * cannot be reached from that piece should not change the selection.
					   * Note: will not work until there is some way to know the user is in the middle of a jump
					else if (!inMiddleOfJump)
						selectedSquare = oldSquare;*/
					
//					new PrettyBoardPrinter().print(board);
				}

				// Update the display
				repaint();
			}

			private void clearSelection() {
				selectedSquare = null;
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

		// Draw the board squares
		drawBoard(g, cellWidth, cellHeight);

		// Draw the possible moves
		drawPossibleMoves(g, cellWidth, cellHeight, pieceWidth, pieceHeight);

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (validSquare(i, j) && board.stateAt(gridToPosition(i, j)) != PositionState.EMPTY) { // if looking at a cell with a piece
					highlightSelectedPiece(g, cellWidth, cellHeight, pieceWidth, pieceHeight, i, j);
					drawAllPieces(g, cellWidth, cellHeight, pieceWidth, pieceHeight, i, j);
				}
			}
		}
	}

	private void drawBoard(Graphics2D g, int cellWidth, int cellHeight) {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				int color = validSquare(i, j) ? 152 : 0;
				g.setColor(new Color(color, color, color));
				g.fillRect(cellWidth * i, cellHeight * j, cellWidth, cellHeight);
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
		
		if (state.hasKing()) {
			g.drawImage(kingImg, cellWidth * i + cellWidth / 2 - kingImg.getWidth() / 2, cellHeight * j + cellHeight / 2 - kingImg.getHeight() / 2, null);
		}
	}

	private void drawPossibleMoves(Graphics2D g, int cellWidth, int cellHeight, int pieceWidth, int pieceHeight) {
		if (selectedSquare == null) {
			ArrayList<Move> possibleMoves = board.possibleMoves(playerToMove);
			for (Move move : possibleMoves) {
				drawPossibleMove(g, cellWidth, cellHeight, pieceWidth, pieceHeight, move);
			}
		} else {
			int selectedIndex = gridToPosition(selectedSquare);
			if (board.possibleJumps(playerToMove).isEmpty()) { // if no possible jumps possible on board, draw walks from selected piece
				ArrayList<Walk> walks = board.possibleWalks(selectedIndex);
				for (Walk walk : walks) {
					drawPossibleMove(g, cellWidth, cellHeight, pieceWidth, pieceHeight, walk);	
				}
			} else { // draw jumps from selected piece
				ArrayList<Jump> jumps = board.possibleJumps(selectedIndex);
				for (Jump jump : jumps)
					drawPossibleMove(g, cellWidth, cellHeight, pieceWidth, pieceHeight, jump);
			}
		}
	}

	private void drawPossibleMove(Graphics2D g, int cellWidth, int cellHeight, int pieceWidth, int pieceHeight, Move move) {
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
