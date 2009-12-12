package checkers.gui;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

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

	public Frame() {
		setTitle("Checkers");
//		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().add(new BoardUI(), BorderLayout.CENTER);
		getContentPane().add(setUpMenuBar(), BorderLayout.NORTH);
		
		pack();
		setLocationRelativeTo(null); // center the frame on the screen
		setVisible(true);
	}

	private JMenuBar setUpMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newGame = new JMenuItem("New...");
		newGame.setMnemonic('N');
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		fileMenu.add(newGame);
		menuBar.add(fileMenu);
		return menuBar;
	}
}
