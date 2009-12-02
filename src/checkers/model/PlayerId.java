package checkers.model;

/**
 * Enumeration identifying each of the two opposing players.
 *
 * @author Kurt Glastetter
 */
public enum PlayerId {
	BLACK { public PlayerId opponent() { return WHITE; } },
	WHITE { public PlayerId opponent() { return BLACK; } };

	/**
	 * Returns this player's opponent.
	 */
	public abstract PlayerId opponent();
}
