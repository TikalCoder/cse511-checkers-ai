package checkers;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Global Constants package.
 *
 * @author Scott Bressler
 */
public final class Constants {
	/**
	 * Board size for Checkers game
	 */
	public static final int GRID_SIZE = 8;

	/**
	 * Width (in pixels) for Checkers GUI
	 */
	public static final int WIDTH = 600;

	/**
	 * Height (in pixels) for Checkers GUI
	 */
	public static final int HEIGHT = 600;

	/**
	 * Padding (in pixels) for Checkers pieces (ovals)
	 */
	public static final int PADDING = 14;

	/**
	 * Directory of images used in the game.
	 */
	public static final String IMG = "img/";

	/**
	 * Filename of crown image.
	 */
	public static final String CROWN_IMG = IMG + "crown-small.png";
	
	/**
	 * Filename of spinner image.
	 * Image generated at http://www.ajaxload.info/ with "Squares"
	 * spinner - white background and #D33E3E foreground.
	 */
	public static final String SPINNER_IMG = IMG + "spinner.gif";
	
	/**
	 * 3-pixel-wide dashed stroke.
	 */
	public static final Stroke DASHED_STROKE = new BasicStroke(3.0f, BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND, 10.0f, new float[] {10.0f}, 0.0f);

	/**
	 * Default negamax search depth.
	 */
	public static final int DEFAULT_NEGAMAX_SEARCH_DEPTH = 5;

	/**
	 * Default negamax extension search depth.
	 */
	public static final int DEFAULT_NEGAMAX_EXTENSION_SEARCH_DEPTH = 5;
}
