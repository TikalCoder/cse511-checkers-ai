package checkers;

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
	 * Filename of crown image.
	 */
	public static final String CROWN_IMG = "crown-smaller.png";
	
	/**
	 * Filename of spinner image.
	 * Image generated at http://www.ajaxload.info/ with "Squares"
	 * spinner - white background and #D33E3E foreground.
	 */
	public static final String SPINNER_IMG = "spinner.gif";

	/**
	 * Default negamax search depth.
	 */
	public static final int DEFAULT_NEGAMAX_SEARCH_DEPTH = 5;

	/**
	 * Default negamax extension search depth.
	 */
	public static final int DEFAULT_NEGAMAX_EXTENSION_SEARCH_DEPTH = 5;
}
