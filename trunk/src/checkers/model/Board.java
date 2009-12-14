package checkers.model;

import java.util.ArrayList;
import static java.lang.System.arraycopy;

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
 *      2 |  05  06  07  08|
 *   L  3 |09  10  11  12  | R
 *   E  4 |  13  14  15  16| I
 *   F  5 |17  18  19  20  | G
 *   T  6 |  21  22  23  24| H
 *      7 |25  26  27  28  | T
 *      8 |  29  30  31  32|----(black's king row)
 *        +----------------+
 *              DOWN (white black pieces start)
 * </code>
 *
 * @author Kurt Glastetter
 */
public class Board implements Cloneable {
	private PositionState[] positionStates;

	@Override
	public Object clone() {
		Board clone = new Board();
		PositionState[] positionStatesClone = new PositionState[positionStates.length];

		arraycopy(positionStates, 0, positionStatesClone, 0, 32);

		clone.positionStates = positionStatesClone;
		return clone;
	}

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

		public PlayerId playerOfPiece() {
			assert hasPiece();
			return hasBlackPiece() ? PlayerId.BLACK : PlayerId.WHITE;
		}

		public static PositionState createPieceForPlayer(PlayerId player, boolean isKing) {
			if (player == PlayerId.BLACK)
				return isKing ? BLACK_KING : BLACK_MAN;
			else
				return isKing ? WHITE_KING : WHITE_MAN;
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
	 * Copy constructor.
	 *
	 * @param toCopy The Board to copy.
	 */
	public Board(Board toCopy) {
		positionStates = new PositionState[32];
		arraycopy(toCopy.positionStates, 0, positionStates, 0, 32);
	}

	public Board(PositionState[] positionStates) {
		this.positionStates = positionStates;
	}


	/**
	 * Constructor initializes the board to a game in progress using
	 * with both a double-jump and a single jump possible.
	 */
	public Board(boolean b) {
		positionStates = new PositionState[32];

		for (int i =  0; i < 32; ++i)
			positionStates[i] = PositionState.EMPTY;

		positionStates[8] = PositionState.BLACK_MAN;
		positionStates[9] = PositionState.BLACK_MAN;

		positionStates[13] = PositionState.WHITE_MAN;
		positionStates[14] = PositionState.WHITE_MAN;
		positionStates[21] = PositionState.WHITE_MAN;
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

	/**
	 * Sets the PositionState enum of a given position.
	 */
	public void setStateAt(int pos, PositionState state) {
		assert isValidPos(pos);
		positionStates[pos - 1] = state;
	}

	public int numPositionStates() {
		return positionStates.length;
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

	public PlayerId playerOfPieceAt(int pos) { return stateAt(pos).playerOfPiece(); }

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
	public ArrayList<Jump> possibleJumps(PlayerId p) {
		ArrayList<Jump> ret = new ArrayList<Jump>();

		for (int i = 1; i <= 32; ++i)
			if (hasPlayersPieceAt(i, p))
				ret.addAll(possibleJumps(i));

		return ret;
	}
	
	/**
	 * Returns true if a move can be made from fromPos to toPos.
	 */
	public boolean possibleSingleMove(int fromPos, int toPos) {
		return hasPieceAt(fromPos) && (areWalkable(fromPos, toPos) || areJumpable(fromPos, toPos));
	}

	/**
	 * Makes a single move, from one position to another; this may be a
	 * walk move, or it may be one step of a jump sequence; returns true
	 * if the move constitutes a complete turn, and false if either
	 * there are more jumps to make, or the move failed.
	 */
	public boolean makeSingleMove(int fromPos, int toPos) {
		if (!areWalkable(fromPos, toPos) && !areJumpable(fromPos, toPos)) { // TODO: replace with possibleSingleMove from above?
			// impossible move; should probably throw exception?
			assert false;
			return false;
		}
		if (!hasPieceAt(fromPos)) {
			// no piece to move; should probably throw exception?
			assert false;
			return false;
		}

		PlayerId movingPlayer = playerOfPieceAt(fromPos);
		boolean moveIsComplete = false;

		if (areWalkable(fromPos, toPos)) {
			// this is a walk move

			// make sure there are no jumps available
			if (possibleJumps(movingPlayer).size() > 0) {
				// forced jump! throw excepton?
				assert false;
				return false;
			}

			if (hasPieceAt(toPos)) {
				// can't move to occupied square; throw exception?
				assert false;
				return false;
			}

			if ((hasBlackManAt(fromPos) && toPos < fromPos) ||
					(hasWhiteManAt(fromPos) && toPos > fromPos)) {
				// can't move a man backwards; throw exception?
				assert false;
				return false;
			}

			// one last double-check
			if (!walkIsInList(new Walk(fromPos, toPos),
					possibleWalks(movingPlayer))) {
				// we must have missed something...  exception?
				assert false;
				return false;
			}

			// ok, safe to move
			setStateAt(toPos, stateAt(fromPos));
			setStateAt(fromPos, PositionState.EMPTY);

			// since this is not a jump, our move is definitely complete
			moveIsComplete = true;

		} else if (areJumpable(fromPos, toPos)) {
			int jumpOverPos = jumpOverPos(fromPos, toPos);

			if (!hasPlayersPieceAt(jumpOverPos, movingPlayer.opponent())) {
				// no opposing checker to jump; throw exception?
				assert false;
				return false;
			}

			// can probably do more checks here...

			// one last double-check
			if (!singleJumpIsPossible(fromPos, toPos)) {
				// throw exception?
				assert false;
				return false;
			}

			// see if this is the end of the jump sequence
			if (singleJumpIsComplete(fromPos, toPos)) {
				moveIsComplete = true;
			}

			// ok, safe to jump
			setStateAt(toPos, stateAt(fromPos));
			setStateAt(fromPos, PositionState.EMPTY);
			setStateAt(jumpOverPos, PositionState.EMPTY);

		} else {
			assert false;
			return false;
		}

		// assuming we got here, everthing went fine.  Need to check and
		// see if a crowning needs to occur.
		if (posIsInBlacksKingRow(toPos) && hasBlackManAt(toPos)) {
			setStateAt(toPos, PositionState.BLACK_KING);
		} else if (posIsInWhitesKingRow(toPos) && hasWhiteManAt(toPos)) {
			setStateAt(toPos, PositionState.WHITE_KING);
		}

		return moveIsComplete;
	}

	public void makeMove(Move move) {
		//TODO: fill this in (Kurt)
	}

	public void makeMoveUnchecked(Move move) {
		setStateAt(move.endPos(), stateAt(move.startPos()));
		setStateAt(move.startPos(), PositionState.EMPTY);

		if (move.isJump()) {
			for (int i = 1; i < move.getSequence().size(); ++i) {
				setStateAt(jumpOverPos(
							move.getSequence().get(i - 1),
							move.getSequence().get(i)), PositionState.EMPTY);
			}
		}
	}

	//TODO: undo move

	static boolean posIsInBlacksKingRow(int pos) {
		return 29 <= pos && pos <= 32;
	}

	static boolean posIsInWhitesKingRow(int pos) {
		return 1 <= pos && pos <= 4;
	}

	/**
	 * Returns the position that is jumped over when jumping from one
	 * given position to the other.
	 */
	static int jumpOverPos(int fromPos, int toPos) {
		assert areJumpable(fromPos, toPos);
		for (Direction dir : Direction.values())
			if (hasJumpPos(fromPos, dir) && jumpPos(fromPos, dir) == toPos)
				return new SingleJumpInfo(fromPos, dir).jumpedPos();
		return 0;
	}

	/**
	 * Returns whether or not a given Walk move is in the given Walk
	 * list.
	 */
	static boolean walkIsInList(Walk walk, ArrayList<Walk> list) {
		for (Walk w : list) {
			if (w.startPos() == walk.startPos() &&
					w.endPos() == walk.endPos()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether or not a single jump move is possible, based on
	 * all the currently possible jump move sequences.  Note that this
	 * single jump move may be part of a longer sequence of jumps.
	 */
	public boolean singleJumpIsPossible(int fromPos, int toPos) {
		for (Jump j : possibleJumps(fromPos))
			if (j.startPos() == fromPos && j.getSequence().get(1) == toPos)
				return true;

		return false;
	}

	/**
	 * Returns whether or not a single jump move is both possible and a
	 * complete jump sequence in itself, based on all the currently
	 * possible jump move sequences.
	 */
	boolean singleJumpIsComplete(int fromPos, int toPos) {
		for (Jump j : possibleJumps(fromPos))
			if (j.startPos() == fromPos && j.getSequence().get(1) == toPos)
				return j.getSequence().size() == 2;

		return false;
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

	/**
	 * Returns true if there is a position that can be walked to from
	 * the given position, in the given direction; else false.
	 */
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

	/**
	 * Returns true if there is a position that can be jumped to from
	 * the given position, in the given direction; else false.
	 */
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

	/**
	 * Returns the position that can be walked to from the given
	 * position, in the given direction.
	 */
	static int walkPos(int pos, Direction dir) {
		assert isValidPos(pos);
		assert hasWalkPos(pos, dir);
		switch (dir) {
		// sorry about this ugliness :( ------- (e.g. pos 9) (e.g. pos 6)
		case UP_LEFT:    return isInOddRow(pos) ? (pos - 4) : (pos - 5);
		case UP_RIGHT:   return isInOddRow(pos) ? (pos - 3) : (pos - 4);
		case DOWN_LEFT:  return isInOddRow(pos) ? (pos + 4) : (pos + 3);
		case DOWN_RIGHT: return isInOddRow(pos) ? (pos + 5) : (pos + 4);
		default: assert false;
		}
		return 0;
	}

	/**
	 * Returns the position that can be jumped to from the given
	 * position, in the given direction.
	 */
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

	/**
	 * Returns true if the two positions can be walked to from one
	 * another; else false.
	 */
	public static boolean areWalkable(int pos1, int pos2) {
		for (Direction dir : Direction.values())
		    if (hasWalkPos(pos1, dir) && walkPos(pos1, dir) == pos2)
				return true;

		return false;
	}

	/**
	 * Returns true if the two positions can be jumped to from one
	 * another; else false.
	 */
	public static boolean areJumpable(int pos1, int pos2) {
		for (Direction dir : Direction.values())
		    if (hasJumpPos(pos1, dir) && jumpPos(pos1, dir) == pos2)
				return true;

		return false;
	}

	/**
	 * Returns true if the piece in a given position can walk in the
	 * given direction, considering the current board state.  Returns
	 * false if for some reason the piece can't move in that direction,
	 * or if there is no such piece in the given position.
	 */
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

	/**
	 * Returns true if the piece in a given position can jump in the
	 * given direction, considering the current board state.  Returns
	 * false if for some reason the piece can't move in that direction,
	 * or if there is no such piece in the given position.
	 */
	boolean canJump(int pos, Direction dir) {
		assert isValidPos(pos);
		return hasPieceAt(pos) && couldJump(stateAt(pos), pos, dir);
	}

	/**
	 * Returns true if, in a hypothetical situation in which the
	 * specified position had the specified state, but the board state
	 * was otherwise the same as it really is, a jump could be made from
	 * that position in the given direction.  Returns false otherwise.
	 * <p>
	 * This can be used to help look ahead at a complete sequence of
	 * jumps, without actually changing or duplicating the Board.
	 */
	boolean couldJump(
			PositionState hypotheticalPosState, int pos, Direction dir
	) {
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
			return stateAt(walkPos(pos, dir)).hasPlayersPiece(PlayerId.WHITE);

		case WHITE_MAN:
			// men have to jump in the correct direction
			if (dir.isDown()) return false;
			// fall thru
		case WHITE_KING:
			// pieces must jump over an opponent piece
			return stateAt(walkPos(pos, dir)).hasPlayersPiece(PlayerId.BLACK);

		default:
			assert false;
		}

		return false;
	}

	/**
	 * Returns a list of all the possible walk moves that can be made
	 * from the given position, in the current board state.  Assumes
	 * that there are no jump moves available!
	 */
	public ArrayList<Walk> possibleWalks(int pos) {
		assert isValidPos(pos);
		ArrayList<Walk> ret = new ArrayList<Walk>();

		for (Direction dir : Direction.values())
			if (canWalk(pos, dir))
				ret.add(new Walk(pos, walkPos(pos, dir)));

		return ret;
	}

	/**
	 * Returns a list of all the possible jump moves (full jump
	 * sequences) that can be made from the given position, in the
	 * current board state.
	 */
	public ArrayList<Jump> possibleJumps(int pos) {
		assert isValidPos(pos) : pos;

		ArrayList<Jump> ret = new ArrayList<Jump>();
		ArrayList<SingleJumpInfo> jumpSequence =
			new ArrayList<SingleJumpInfo>();

		for (Direction dir : Direction.values()) {
			if (canJump(pos, dir)) {
				// temporarily "pick up" the piece we are considering
				// jumping with, such that its position appears empty,
				// such that the piece can conceivably jump around in a
				// circle and land in the original position.
				PositionState origPosState = stateAt(pos);
				setStateAt(pos, PositionState.EMPTY);

				jumpSequence.add(new SingleJumpInfo(pos, dir));
				expandJumpSequence(origPosState, jumpSequence, ret);
				jumpSequence.clear();

				// put the piece back (we haven't really jumped yet)
				setStateAt(pos, origPosState);
			}
		}

		return ret;
	}

	/**
	 * Somewhat klugey class that temporarily maintains information
	 * about a single, individual jump (part of a complete jump
	 * sequence).
	 */
	private static class SingleJumpInfo {
		public SingleJumpInfo(int startPos, Direction dir) {
			this.startPos = startPos;
			this.dir = dir;
		}

		int startPos() { return startPos; }
		int jumpedPos() { return walkPos(startPos, dir); }
		int endPos() { return jumpPos(startPos, dir); }

		private int startPos;
		private Direction dir;
	}

	/**
	 * Returns true if the given jump sequence already contains a jump
	 * that happened over the given position; false otherwise.
	 */
	private static boolean hasAlreadyJumpedOver(
			ArrayList<SingleJumpInfo> jumpSequence, int pos
	) {
		assert isValidPos(pos);

		for (int i = 0; i < jumpSequence.size(); ++i)
			if (jumpSequence.get(i).jumpedPos() == pos)
				return true;

		return false;
	}

	/**
	 * Recursive method performs a depth-first search on the possible
	 * jump sequences that can be made from the given partial jump
	 * sequence, happening on the current board state.  If it reaches
	 * the end of a jump sequence, it adds the complete sequence to the
	 * list of jumps.
	 *
	 * @param startPosState state of the position that the jumpSequence
	 *                      started from; this is just used to identify
	 *                      the piece that is actually doing the jumping
	 * @param jumpSequence  partial sequence of jumps; this acts as a
	 *                      stack that maintains the current state of
	 *                      our depth-first traversal of the jump
	 *                      sequence tree
	 * @param jumps         the output list of complete jump moves
	 */
	private void expandJumpSequence(
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
			boolean jumpedKingFirst =
					hasKingAt(jumpSequence.get(0).jumpedPos());

			Jump jump = new Jump(pos0, pos1, jumpedKingFirst);

			for (int i = 1; i < jumpSequence.size(); ++i)
				jump.jumpAgain(
						jumpSequence.get(i).endPos(),
						hasKingAt(jumpSequence.get(i).jumpedPos()));

			jumps.add(jump);
		}
	}

	/**
	 * Converts a sequence of position integers into a Move object.
	 */
	public Move generateMove(ArrayList<Integer> positionSequence) {
		if (positionSequence.size() < 2)
			throw new IllegalArgumentException(
					"position sequence must have at least 2 positions to be"
					+ " considered a move");

		if (areWalkable(positionSequence.get(0), positionSequence.get(1)))
			return generateWalk(positionSequence);
		else if (areJumpable(positionSequence.get(0), positionSequence.get(1)))
			return generateJump(positionSequence);
		else
			throw new IllegalArgumentException(
					"postion sequence does not represent a possible move");
	}

	public Walk generateWalk(ArrayList<Integer> posSequence) {
		if (posSequence.size() != 2)
			throw new IllegalArgumentException("can only walk one step at a time");

		return new Walk(posSequence.get(0), posSequence.get(1));
	}

	public Jump generateJump(ArrayList<Integer> posSequence) {
		Jump jump = new Jump(posSequence.get(0), posSequence.get(1),
				hasKingAt(jumpOverPos(
						posSequence.get(0), posSequence.get(1))));

		for (int i = 2; i < posSequence.size(); ++i) {
			jump.jumpAgain(
					posSequence.get(i),
					hasKingAt(jumpOverPos(
							posSequence.get(i - 1), posSequence.get(i))));
		}

		return jump;
	}

	public void set(Board board) {
		for (int i = 0; i < positionStates.length; i++) {
			positionStates[i] = board.positionStates[i];
		}
	}
}
