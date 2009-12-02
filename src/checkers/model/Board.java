package checkers.model;

import java.util.ArrayList;

/**
 * Represents the checkers board, and where all the pieces are, at a
 * given time.
 * <p>
 * This information, combined with whose turn it is, will result in the
 * complete game state.
 * <p>
 * This code's notion of the orientation of the board, and of the
 * position numbers:
 * <code>
 *               UP (where black pieces start)
 *    row +----------------+
 *      1 |01  02  03  04  |----(white's king row)
 *      2 |  05  07  08  06|
 *   L  3 |09  10  11  12  | R
 *   E  4 |  13  15  16  14| I
 *   F  5 |17  18  19  20  | G
 *   T  6 |  21  23  24  22| H
 *      7 |25  26  27  28  | T
 *      8 |  29  31  32  30|----(black's king row)
 *        +----------------+
 *              DOWN (white black pieces start)
 * </code>
 *
 * @author Kurt Glastetter
 */
public class Board {
	private PositionState[] positionStates;

	/**
	 * Enumeration identifying the different diagonal directions that
	 * pieces can move in.  Directions correspond to the orientation of
	 * the board given in the Board description above.
	 */
	static enum Direction {
		UP_LEFT    { public boolean isUp() { return true; } },
		UP_RIGHT   { public boolean isUp() { return true; } },
		DOWN_LEFT  { public boolean isUp() { return false; } },
		DOWN_RIGHT { public boolean isUp() { return false; } };

		public abstract boolean isUp();
		public boolean isDown() { return !isUp(); }
	}

	/**
	 * Enumeration identifying the state of a given valid position
	 * square (what kind of piece it has, or no piece).
	 * <p>
	 * Provides several boolean accessors meant to make logic easier.
	 */
	public static enum PositionState {
		EMPTY       { public String toString() { return "."; } },
		BLACK_MAN   { public String toString() { return "b"; } },
		BLACK_KING  { public String toString() { return "B"; } },
		WHITE_MAN   { public String toString() { return "w"; } },
		WHITE_KING  { public String toString() { return "W"; } };

		/**
		 * toString is just for debug purposes.
		 */
		public abstract String toString();

		public boolean hasPiece()      { return this != EMPTY; }
		public boolean hasMan()        { return hasBlackMan() || hasWhiteMan(); }
		public boolean hasKing()       { return hasBlackKing() || hasWhiteKing(); }
		public boolean hasBlackPiece() { return hasBlackMan() || hasBlackKing(); }
		public boolean hasBlackMan()   { return this == BLACK_MAN; }
		public boolean hasBlackKing()  { return this == BLACK_KING; }
		public boolean hasWhitePiece() { return hasWhiteMan() || hasWhiteKing(); }
		public boolean hasWhiteMan()   { return this == WHITE_MAN; }
		public boolean hasWhiteKing()  { return this == WHITE_KING; }

		public boolean hasPlayersPiece(PlayerId p) {
			return hasPlayersMan(p) || hasPlayersKing(p);
		}
		public boolean hasPlayersMan(PlayerId p) {
			return (
				(p == PlayerId.BLACK && hasBlackMan()) ||
				(p == PlayerId.WHITE && hasWhiteMan())
			);
		}
		public boolean hasPlayersKing(PlayerId p) {
			return (
				(p == PlayerId.BLACK && hasBlackKing()) ||
				(p == PlayerId.WHITE && hasWhiteKing())
			);
		}
	}

	/**
	 * Constructor initializes the board to the start of a new game.
	 */
	public Board() {
		positionStates = new PositionState[32];

		for (int i =  0; i < 12; ++i)
			positionStates[i] = PositionState.BLACK_MAN;
		for (int i = 12; i < 20; ++i)
			positionStates[i] = PositionState.EMPTY;
		for (int i = 20; i < 32; ++i)
			positionStates[i] = PositionState.WHITE_MAN;
	}

	/**
	 * toString is not a pretty output; it's just for debug purposes
	 */
	public String toString() {
		String ret = "";
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 4; ++j) {
				if (i % 2 == 0) ret += " ";
				ret += positionStates[i * 4 + j];
				if (i % 2 != 0 && j < 3) ret += " ";
			}
			ret += "\n";
		}
		return ret;
	}

	/**
	 * Tells if an int is a valid position ID.  Position ID's
	 * (<code>pos</code>) are <code>int</code>s from 1 thru 32
	 * inclusive.  This matches the usual checkers notation.
	 */
	public static boolean isValidPos(int pos) {
		return 1 <= pos && pos <= 32;
	}

	/**
	 * Accesses the PositionState enum of a given position.  Useful if
	 * you want to do a switch statement, or if you don't want to use
	 * the boolean accessors provide below.
	 */
	public PositionState stateAt(int pos) {
		assert isValidPos(pos);
		return positionStates[pos - 1];
	}

	public boolean hasPieceAt      (int pos) { return stateAt(pos).hasPiece(); }
	public boolean hasManAt        (int pos) { return stateAt(pos).hasMan(); }
	public boolean hasKingAt       (int pos) { return stateAt(pos).hasKing(); }
	public boolean hasBlackPieceAt (int pos) { return stateAt(pos).hasBlackPiece(); }
	public boolean hasBlackManAt   (int pos) { return stateAt(pos).hasBlackMan(); }
	public boolean hasBlackKingAt  (int pos) { return stateAt(pos).hasBlackKing(); }
	public boolean hasWhitePieceAt (int pos) { return stateAt(pos).hasWhitePiece(); }
	public boolean hasWhiteManAt   (int pos) { return stateAt(pos).hasWhiteMan(); }
	public boolean hasWhiteKingAt  (int pos) { return stateAt(pos).hasWhiteKing(); }

	public boolean hasPlayersPieceAt (int pos, PlayerId p) { return stateAt(pos).hasPlayersPiece(p); }
	public boolean hasPlayersManAt   (int pos, PlayerId p) { return stateAt(pos).hasPlayersMan(p); }
	public boolean hasPlayersKingAt  (int pos, PlayerId p) { return stateAt(pos).hasPlayersKing(p); }

	/**
	 * Returns a list of all the possible moves from the current board
	 * state that can be made by the specified player (whose turn it
	 * presumably is).
	 * <p>
	 * Note: Maybe should return a set instead?
	 */
	public ArrayList<Move> possibleMoves(PlayerId p) {
		ArrayList<Move> ret = new ArrayList<Move>();

		ret.addAll(possibleJumps(p));

		if (ret.size() == 0)
			ret.addAll(possibleWalks(p));

		return ret;
	}

	/**
	 * Returns a list of all the possible walk moves that can be made
	 * from the current board state.  Assumes that there are no jump
	 * moves available!
	 */
	ArrayList<Walk> possibleWalks(PlayerId p) {
		ArrayList<Walk> ret = new ArrayList<Walk>();

		for (int i = 1; i <= 32; ++i)
			if (hasPlayersPieceAt(i, p))
				ret.addAll(possibleWalks(i));

		return ret;
	}

	/**
	 * Returns a list of all the possible jump moves that can be made
	 * from the current board state, where a jump move is a complete
	 * jump sequence until no more individual jumps can be made.
	 */
	ArrayList<Jump> possibleJumps(PlayerId p) {
		ArrayList<Jump> ret = new ArrayList<Jump>();

		for (int i = 1; i <= 32; ++i)
			if (hasPlayersPieceAt(i, p))
				ret.addAll(possibleJumps(i));

		return ret;
	}

	/**
	 * Returns true if the given position is a square in row 1, 3, 5, or
	 * 7 (in other words, in 1-4, 9-12, 17-20, or 25-28); else false.
	 */
	static boolean isInOddRow(int pos) {
		assert isValidPos(pos);
		return ((pos - 1) / 4) % 2 == 0;
	}

	/**
	 * Returns true if the given position is a square in row 2, 4, 6, or
	 * 8 (in other words, in 5-8, 13-16, 21-24, or 29-32); else false.
	 */
	static boolean isInEvenRow(int pos) {
		assert isValidPos(pos);
		return !isInOddRow(pos);
	}

	static boolean hasWalkPos(int pos, Direction dir) {
		assert isValidPos(pos);
		switch (dir) {
		case UP_LEFT:    return (pos >  4) && (pos % 8 != 5);
		case UP_RIGHT:   return (pos >  4) && (pos % 8 != 4);
		case DOWN_LEFT:  return (pos < 29) && (pos % 8 != 5);
		case DOWN_RIGHT: return (pos < 29) && (pos % 8 != 4);
		default: assert false;
		}
		return false;
	}

	static boolean hasJumpPos(int pos, Direction dir) {
		assert isValidPos(pos);
		switch (dir) {
		case UP_LEFT:    return (pos >  8) && (pos % 4 != 1);
		case UP_RIGHT:   return (pos >  8) && (pos % 4 != 0);
		case DOWN_LEFT:  return (pos < 25) && (pos % 4 != 1);
		case DOWN_RIGHT: return (pos < 25) && (pos % 4 != 0);
		default: assert false;
		}
		return false;
	}

	static int walkPos(int pos, Direction dir) {
		assert isValidPos(pos);
		assert hasWalkPos(pos, dir);
		int upLeftPosDiff  = isInOddRow(pos) ? 4 : 5;
		int upRightPosDiff = isInOddRow(pos) ? 3 : 4;
		switch (dir) {
		case UP_LEFT:    return pos - upLeftPosDiff;
		case UP_RIGHT:   return pos - upRightPosDiff;
		case DOWN_LEFT:  return pos + upRightPosDiff;
		case DOWN_RIGHT: return pos + upLeftPosDiff;
		default: assert false;
		}
		return 0;
	}

	static int jumpPos(int pos, Direction dir) {
		assert isValidPos(pos);
		assert hasJumpPos(pos, dir);
		switch (dir) {
		case UP_LEFT:    return pos - 9;
		case UP_RIGHT:   return pos - 7;
		case DOWN_LEFT:  return pos + 7;
		case DOWN_RIGHT: return pos + 9;
		default: assert false;
		}
		return 0;
	}

	static boolean areWalkable(int pos1, int pos2) {
		int upLeftPosDiff  = isInOddRow(pos1) ? 4 : 5;
		int upRightPosDiff = isInOddRow(pos1) ? 3 : 4;
		return (
			(pos1 - upLeftPosDiff == pos2 && hasWalkPos(pos1, Direction.UP_LEFT)) ||
			(pos1 - upRightPosDiff == pos2 && hasWalkPos(pos1, Direction.UP_RIGHT)) ||
			(pos1 + upRightPosDiff == pos2 && hasWalkPos(pos1, Direction.DOWN_LEFT)) ||
			(pos1 + upLeftPosDiff == pos2 && hasWalkPos(pos1, Direction.DOWN_RIGHT))
		);
	}

	static boolean areJumpable(int pos1, int pos2) {
		return (
			(pos1 - 9 == pos2 && hasJumpPos(pos1, Direction.UP_LEFT)) ||
			(pos1 - 7 == pos2 && hasJumpPos(pos1, Direction.UP_RIGHT)) ||
			(pos1 + 7 == pos2 && hasJumpPos(pos1, Direction.DOWN_LEFT)) ||
			(pos1 + 9 == pos2 && hasJumpPos(pos1, Direction.DOWN_RIGHT))
		);
	}

	boolean canWalk(int pos, Direction dir) {
		assert isValidPos(pos);
		return (
			// make sure the walk-to position exists and is empty
			hasWalkPos(pos, dir) && !hasPieceAt(walkPos(pos, dir)) && (
				// kings can walk in any direction
				hasKingAt(pos) ||
				// men have to walk in the correct direction
				(hasBlackManAt(pos) && dir.isDown()) ||
				(hasWhiteManAt(pos) && dir.isUp())
			)
		);
	}

	boolean couldJump(PositionState hypotheticalPosState, int pos, Direction dir) {
		assert isValidPos(pos);

		// make sure the landing position exists and is empty
		if (!hasJumpPos(pos, dir) || hasPieceAt(jumpPos(pos, dir)))
			return false;

		switch (hypotheticalPosState) {
		case EMPTY:
			// need a piece to jump with
			return false;

		case BLACK_MAN:
			// men have to jump in the correct direction
			if (dir.isUp()) return false;
			// fall thru
		case BLACK_KING:
			// pieces must jump over an opponent piece
			return stateAt(walkPos(pos, dir)).hasPlayersPiece(
					PlayerId.WHITE);

		case WHITE_MAN:
			// men have to jump in the correct direction
			if (dir.isDown()) return false;
			// fall thru
		case WHITE_KING:
			// pieces must jump over an opponent piece
			return stateAt(walkPos(pos, dir)).hasPlayersPiece(
					PlayerId.BLACK);

		default:
			assert false;
		}

		return false;
	}

	boolean canJump(int pos, Direction dir) {
		assert isValidPos(pos);
		return couldJump(stateAt(pos), pos, dir);
	}

	ArrayList<Walk> possibleWalks(int pos) {
		assert isValidPos(pos);
		ArrayList<Walk> ret = new ArrayList<Walk>();

		for (Direction dir : Direction.values())
			if (canWalk(pos, dir))
				ret.add(new Walk(pos, walkPos(pos, dir)));

		return ret;
	}

	static class SingleJumpInfo {
		public SingleJumpInfo(int startPos, Direction dir) {
			this.startPos = startPos;
			this.dir = dir;
		}

		int startPos() { return startPos; }
		int jumpedPos() { return walkPos(startPos, dir); }
		int endPos() { return jumpPos(startPos, dir); }

		Direction dir() { return dir; }

		private int startPos;
		private Direction dir;
	}

	static boolean hasAlreadyJumpedOver(
			ArrayList<SingleJumpInfo> jumpSequence, int pos
	) {
		assert isValidPos(pos);

		for (int i = 0; i < jumpSequence.size(); ++i)
			if (jumpSequence.get(i).jumpedPos() == pos)
				return true;

		return false;
	}

	void expandJumpSequence(
			PositionState startPosState,
			ArrayList<SingleJumpInfo> jumpSequence,
			ArrayList<Jump> jumps
	) {
		boolean atEndOfJump = true;

		for (Direction dir : Direction.values()) {
			int pos = jumpSequence.get(jumpSequence.size() - 1).endPos();
			if (couldJump(startPosState, pos, dir)) {
				SingleJumpInfo jump = new SingleJumpInfo(pos, dir);
				if (!hasAlreadyJumpedOver(jumpSequence, jump.jumpedPos())) {
					atEndOfJump = false;
					jumpSequence.add(jump);
					expandJumpSequence(startPosState, jumpSequence, jumps);
					jumpSequence.remove(jumpSequence.size() - 1);
				}
			}
		}

		if (atEndOfJump) {
			int pos0 = jumpSequence.get(0).startPos();
			int pos1 = jumpSequence.get(0).endPos();

			Jump jump = new Jump(pos0, pos1);

			for (int i = 2; i < jumpSequence.size(); ++i)
				jump.jumpAgain(jumpSequence.get(i).endPos());

			jumps.add(jump);
		}
	}

	ArrayList<Jump> possibleJumps(int pos) {
		assert isValidPos(pos);

		ArrayList<Jump> ret = new ArrayList<Jump>();
		ArrayList<SingleJumpInfo> jumpSequence =
			new ArrayList<SingleJumpInfo>();

		for (Direction dir : Direction.values()) {
			if (canJump(pos, dir)) {
				jumpSequence.add(new SingleJumpInfo(pos, dir));
				expandJumpSequence(stateAt(pos), jumpSequence, ret);
				jumpSequence.clear();
			}
		}

		return ret;
	}
}
