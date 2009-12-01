package checkers.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(new BoardUI(), BorderLayout.CENTER);
		
		// TODO: Add a menu bar with options
		
		pack();
		setLocationRelativeTo(null); // center the frame on the screen
		setVisible(true);
	}
}
