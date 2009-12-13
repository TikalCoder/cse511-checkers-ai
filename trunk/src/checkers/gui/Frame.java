package checkers.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import checkers.Game;

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

	public Frame() {
		setTitle("Checkers");
//		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		boardUI = new BoardUI();
		
		getContentPane().add(boardUI, BorderLayout.CENTER);
		getContentPane().add(setUpMenuBar(), BorderLayout.NORTH);
		
		pack();
		setLocationRelativeTo(null); // center the frame on the screen
		setVisible(true);
	}
	
	public BoardUI getBoardUI() {
		return boardUI;
	}

	private JMenuBar setUpMenuBar() {
		JMenuBar menuBar = new JMenuBar();
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
		
		undoMove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.currentGame().undo();
			}
		});
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		return menuBar;
	}
}
