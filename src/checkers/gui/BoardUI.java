package checkers.gui;

import static checkers.Constants.CROWN_IMG;
import static checkers.Constants.DASHED_STROKE;
import static checkers.Constants.GRID_SIZE;
import static checkers.Constants.SPINNER_IMG;
import static checkers.Utils.gridToPosition;
import static checkers.Utils.positionToGridPoint;
import static checkers.Utils.validSquare;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import checkers.Constants;
import checkers.Game;
import checkers.Player;
import checkers.Utils;
import checkers.ai.AIPlayer;
import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.SingleMove;
import checkers.model.Board.PositionState;

/**
 * This class represents a Checkers board with pieces on it, displaying the
 * possible moves for the current player.
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

	private BufferedImage kingImg;
	
	/**
	 * Label with a spinning image to show that the AI is thinking.
	 */
	private JLabel spinnerImg;

	/**
	 * Maintain the current game state.
	 */
	private GameState gameState;

	/**
	 * The player that chooses moves for this UI.
	 */
	private GUIPlayer player;

	/**
	 * True if the possible moves a computer player can make should be hidden.
	 */
	private boolean hidePossibleAIMoves;
	
	/**
	 * True if piece position indices should be hidden.
	 */
	private boolean hideIndices;

	public BoardUI() {
		hideIndices = true;
		
		setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		kingImg = null;
		try {
		    kingImg = ImageIO.read(new File(CROWN_IMG));
		} catch (IOException e) {}
		
		setLayout(new BorderLayout());
		add(spinnerImg = new JLabel(new ImageIcon(SPINNER_IMG)), BorderLayout.CENTER);

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				// Keep track of last selection
				Point oldSquare = selectedSquare;
				PositionState oldState = null;
				int oldIndex = 0;
				if (oldSquare != null) {
					oldIndex = gridToPosition(oldSquare);
					oldState = gameState.getBoard().stateAt(oldIndex);
				}

				// Determine the newly selected square
				int cellWidth = getWidth() / GRID_SIZE;
				int cellHeight = getHeight() / GRID_SIZE;
				int x = me.getX() / cellWidth;
				int y = me.getY() / cellHeight;
				Point selection = new Point(x, y);
				int selectedIndex = gridToPosition(selection);

				ArrayList<SingleMove> possibleMoves = gameState.possibleSingleMoves();
				boolean validStart = false;
				boolean validEnd = false;
				for (SingleMove singleMove : possibleMoves) {
					if (singleMove.startPos == selectedIndex)
						validStart = true;
					if (singleMove.endPos == selectedIndex)
						validEnd = true;
				}

				if (Game.currentGame().getPlayerToMove() instanceof AIPlayer
						|| !(validStart
							|| oldState != null
							&& oldState.hasPlayersPiece(gameState.playerToMove())
							&& validEnd)) // ignore clicks on invalid locations
					return;

//				PositionState selectedState = gameState.getBoard().stateAt(selectedIndex);
				selectedSquare = selection;


				if (selectedSquare.equals(oldSquare)) {
					clearSelection();
				} else if (validSquare(x, y) && oldSquare != null
						&& oldState.hasPlayersPiece(gameState.playerToMove())
						&& gameState.isPossibleSingleMove(oldIndex, selectedIndex)) {
					// try to make a move from oldSquare to selectedSquare in Board (model)...
					String move = gameState.getBoard().singleJumpIsPossible(oldIndex, selectedIndex) ? "jump" : "walk";
					System.out.println(gameState.playerToMove() + " " + move + " from: " + oldIndex + " to: " + selectedIndex);
					synchronized (player) {
						player.setNextMove(oldIndex, selectedIndex);
						clearSelection();
						player.notify();
					}
//					if (board.makeSingleMove(oldIndex, selectedIndex)) {
//						playerToMove = playerToMove.opponent();
//						clearSelection();
				/*	}*/ /* If no move was made at all, change selected square back to oldSquare.
					   * This will happen if clicking from a square with a piece to a square that
					   * cannot be reached from that piece should not change the selection.
					   * Note/TODO: will not work until there is some way to know the user is in the middle of a jump
				*/
//					if (!gameState.isJumping())
//						selectedSquare = oldSquare;

				} else if (oldSquare != null)
					selectedSquare = oldSquare;


				// Update the display
				repaint();
			}

			private void clearSelection() {
				selectedSquare = null;
			}
		});
	}

	public boolean hidePossibleAIMoves() {
		return hidePossibleAIMoves;
	}

	public void setHidePossibleAIMoves(boolean hidePossibleAIMoves) {
		this.hidePossibleAIMoves = hidePossibleAIMoves;
	}

	public boolean hideIndices() {
		return hideIndices;
	}

	public void setHideIndices(boolean hideIndices) {
		this.hideIndices = hideIndices;
	}

	/**
	 * Draw the board with black and light gray cells.
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;

		if (gameState == null)
			return;

		int cellWidth = getWidth() / GRID_SIZE;
		int cellHeight = getHeight() / GRID_SIZE;
		int pieceWidth = (getWidth() - (Constants.PADDING * 8)) / GRID_SIZE;
		int pieceHeight = (getHeight() - (Constants.PADDING * 8)) / GRID_SIZE;

		// Draw the board squares
		drawBoard(g, cellWidth, cellHeight);

		// Highlight the last move made
		highlightLastMove(g, cellWidth, cellHeight, pieceWidth, pieceHeight, Game.currentGame().getState().getLastMoveEndPos());
		
		// Draw the possible moves from the current game state for non-AI players
		Player currPlayer = Game.currentGame().getPlayerToMove();
		if (currPlayer instanceof AIPlayer && !hidePossibleAIMoves || !(currPlayer instanceof AIPlayer))
			drawPossibleMoves(g, cellWidth, cellHeight, pieceWidth, pieceHeight);
		
		if (currPlayer instanceof AIPlayer && !Game.currentGame().allAIGame() && !Game.currentGame().isOver())
			spinnerImg.setVisible(true);
		else
			spinnerImg.setVisible(false);

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				int position = gridToPosition(i, j);
				if (position != 0) {
					g.setColor(Color.white);
					if (!hideIndices())
						g.drawString("" + position, cellWidth * i, cellHeight * j + cellHeight / 5);
				}
				
				if (validSquare(i, j) && gameState.getBoard().hasPieceAt(position)) { // if looking at a cell with a piece
					highlightSelectedPiece(g, cellWidth, cellHeight, pieceWidth, pieceHeight, i, j);
					drawAllPieces(g, cellWidth, cellHeight, pieceWidth, pieceHeight, i, j);
				}
			}
		}

		if (Game.currentGame().isOver())
			drawEndGameMessage(g);
	}

	private void drawEndGameMessage(Graphics2D g) {
		String winner = Game.currentGame().getWinner().toString();
		g.setFont(new Font("Arial", Font.BOLD, getWidth() / 10));
		g.setColor(Color.green);
		g.drawString(winner + " wins!!!", getWidth() / 6, getHeight() / 2 + getHeight() / 30);
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
			PositionState state = gameState.getBoard().stateAt(gridToPosition(x, y));
			if (state.hasBlackPiece())
				g.setColor(new Color(0, 255, 255));
			else if (state.hasWhitePiece())
				g.setColor(new Color(0, 0, 255));
			final int outlinePadding = 3;
			g.fill(new Ellipse2D.Double(cellWidth * x + Constants.PADDING / 2.0 - outlinePadding, cellHeight * y + Constants.PADDING / 2.0 - outlinePadding, pieceWidth + 2 * outlinePadding, pieceHeight + 2 * outlinePadding));
		}
	}
	
	/**
	 * Helper method for paintComponent that highlights the previous move.
	 * 
	 * Note: Does not work with current undo functionality.
	 */
	private void highlightLastMove(Graphics2D g, int cellWidth, int cellHeight, int pieceWidth, int pieceHeight, int lastMoveEndPos) {
		Point p = positionToGridPoint(lastMoveEndPos);
		if (lastMoveEndPos > 0) {
			g.setColor(new Color(153, 0, 51));
			final int outlinePadding = 3;
			g.setStroke(DASHED_STROKE);
			g.draw(new Ellipse2D.Double(cellWidth * p.getX() + Constants.PADDING / 2.0 - outlinePadding, cellHeight * p.getY() + Constants.PADDING / 2.0 - outlinePadding, pieceWidth + 2 * outlinePadding, pieceHeight + 2 * outlinePadding));
		}
	}
	/**
	 * Helper method for paintComponent that draws all the pieces on the board.
	 */
	private void drawAllPieces(Graphics2D g, int cellWidth, int cellHeight,int pieceWidth, int pieceHeight, int i, int j) {
		PositionState state = gameState.getBoard().stateAt(gridToPosition(i, j));
		if (state.hasBlackPiece())
			g.setColor(new Color(0, 0, 0));
		else if (state.hasWhitePiece())
			g.setColor(new Color(255, 255, 255));

		double x = cellWidth * i + Constants.PADDING / 2.0;
		double y = cellHeight * j + Constants.PADDING / 2.0;
		g.fill(new Ellipse2D.Double(x, y, pieceWidth, pieceHeight));

		if (state.hasKing()) {
			g.drawImage(kingImg,
						cellWidth * i + cellWidth / 2 - kingImg.getWidth() / 2,
						cellHeight * j + cellHeight / 2 - kingImg.getHeight() / 2,
						null);
		}
	}

	private void drawPossibleMoves(Graphics2D g, int cellWidth, int cellHeight, int pieceWidth, int pieceHeight) {
		ArrayList<? extends Move> moves = null;
		if (selectedSquare == null)
			moves = gameState.possibleMoves();
		else {
			int selectedIndex = gridToPosition(selectedSquare);
			if (gameState.getBoard().possibleJumps(gameState.playerToMove()).isEmpty()) // if no jumps possible anywhere on board, draw walks from selected piece
				moves = gameState.getBoard().possibleWalks(selectedIndex);
			else // draw jumps from selected piece
				moves = gameState.getBoard().possibleJumps(selectedIndex);
		}

		drawPossibleMoves(g, cellWidth, cellHeight, pieceWidth, pieceHeight, moves);
	}

	private void drawPossibleMoves(Graphics2D g, int cellWidth, int cellHeight, int pieceWidth, int pieceHeight, ArrayList<? extends Move> moves) {
		for (Move move : moves) {
			ArrayList<Integer> sequence = move.getSequence();
			for (int i = 0; i < sequence.size(); i++) {
				int positionIndex = sequence.get(i);
				Point p = Utils.positionToGridPoint(positionIndex);
				final int outlinePadding = 3;
				if (i == 0) {
					g.setColor(new Color(255, 140, 0));
					g.fill(new Ellipse2D.Double(cellWidth * p.getX() + Constants.PADDING / 2.0 - outlinePadding,
												cellHeight * p.getY() + Constants.PADDING / 2.0 - outlinePadding,
												pieceWidth + 2 * outlinePadding,
												pieceHeight + 2 * outlinePadding));
				} else if (i >= 1) {
					g.setColor(new Color(255, 0, 0));
					g.fill(new Ellipse2D.Double(cellWidth * p.getX() + Constants.PADDING / 2.0 - outlinePadding,
												cellHeight * p.getY() + Constants.PADDING / 2.0 - outlinePadding,
												pieceWidth + 2 * outlinePadding,
												pieceHeight + 2 * outlinePadding));

					g.setColor(new Color(152, 152, 152));
					g.fill(new Ellipse2D.Double(cellWidth * p.getX() + Constants.PADDING / 2.0,
												cellHeight * p.getY() + Constants.PADDING / 2.0,
												pieceWidth,
												pieceHeight));
				}
			}
		}
	}

	public void allowedToMove(GameState gameState, GUIPlayer player) {
		this.gameState = gameState;
		this.player = player;
	}

	public void setGameState(GameState newState) {
		gameState = newState;
		repaint();
	}
}
