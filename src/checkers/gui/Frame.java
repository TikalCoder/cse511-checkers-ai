package checkers.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import checkers.Game;
import checkers.Player;
import checkers.ai.AIPlayer;
import checkers.ai.NegamaxPlayer;
import checkers.model.Board;

import static checkers.Constants.DEFAULT_NEGAMAX_SEARCH_DEPTH;

/**
 * This class sets up the {@link JFrame} operations, adding a {@link Board} to
 * the frame.
 * 
 * @author Scott Bressler
 */
public class Frame extends JFrame {
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	private BoardUI boardUI;
	
	private JCheckBoxMenuItem blackPlayer, whitePlayer;

	public Frame() {
		setTitle("Checkers");
//		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(setUpMenuBar());
		
		boardUI = new BoardUI();
		getContentPane().add(boardUI, BorderLayout.CENTER);
		
		pack();
		setLocationRelativeTo(null); // center the frame on the screen
		setVisible(true);
	}
	
	public BoardUI getBoardUI() {
		return boardUI;
	}

	private JMenuBar setUpMenuBar() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem newGame = new JMenuItem("New...");
		newGame.setMnemonic('N');
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		fileMenu.add(newGame);
		
		JMenu editMenu = new JMenu("Edit");
		JMenuItem undoMove = new JMenuItem("Undo");
		undoMove.setMnemonic('U');
		undoMove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		editMenu.add(undoMove);

		JMenu optionsMenu = new JMenu("Options");
		blackPlayer = new JCheckBoxMenuItem("Player 1 AI");
		whitePlayer = new JCheckBoxMenuItem("Player 2 AI");
		optionsMenu.add(blackPlayer);
		optionsMenu.add(whitePlayer);
		
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.newGame();
			}
		});
		
		blackPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Player newPlayer = blackPlayer.isSelected() ? new NegamaxPlayer(DEFAULT_NEGAMAX_SEARCH_DEPTH) : new GUIPlayer(boardUI);
				Game.currentGame().setBlackPlayer(newPlayer);
			}
		});
		
		whitePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Player newPlayer = whitePlayer.isSelected() ? new NegamaxPlayer(DEFAULT_NEGAMAX_SEARCH_DEPTH) : new GUIPlayer(boardUI);
				Game.currentGame().setWhitePlayer(newPlayer);
			}
		});
		
		undoMove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.currentGame().undo();
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(optionsMenu);
		return menuBar;
	}
	
	public void setPlayerTypes() {
		blackPlayer.setSelected(Game.currentGame().getBlackPlayer() instanceof AIPlayer);
		whitePlayer.setSelected(Game.currentGame().getWhitePlayer() instanceof AIPlayer);
	}
}
