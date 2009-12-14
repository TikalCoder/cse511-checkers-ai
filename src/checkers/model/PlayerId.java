package checkers.model;

/**
 * Enumeration identifying each of the two opposing players.
 * 
 * @author Kurt Glastetter
 */
public enum PlayerId {
	BLACK {
		public PlayerId opponent() {
			return WHITE;
		}

		public String toString() {
			return "Black";
		}
	},
	WHITE {
		public PlayerId opponent() {
			return BLACK;
		}

		public String toString() {
			return "White";
		}
	};

	/**
	 * Returns this player's opponent.
	 */
	public abstract PlayerId opponent();
}
