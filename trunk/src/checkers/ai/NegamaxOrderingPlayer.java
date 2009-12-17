package checkers.ai;

import java.util.List;

import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

/**
 * Negamax player with move ordering.
 *
 * @author Andrew Duffey
 */
public class NegamaxOrderingPlayer extends AIPlayer {
	private int searchDepth;
	private int differential;

	/**
	 * Private constructor with no arguments. Disallows creation of a NegamaxOrderingPlayer
	 * if there was no depth specified. Use the
	 *
	 * <code>NegamaxOrderingPlayer(Integer searchDepth, Integer searchDifferential)</code>
	 *
	 * constructor instead.
	 */
	@SuppressWarnings("unused")
	private NegamaxOrderingPlayer() {
		super();
	}

	/**
	 * Constructs a Player that chooses moves based on a negamax search with a maximum
	 * depth of searchDepth and uses internal searches <code>differential</code> plies
	 * shallower to order the moves.
	 *
	 * @param searchDepth How deep to search.
	 * @param searchDifferential How many fewer plies to search for internal ordering searches.
	 */
	public NegamaxOrderingPlayer(int searchDepth, int searchDifferential) {
		super();
		this.searchDepth = searchDepth;
		this.differential = searchDifferential;

		// statistics for each search
		this.searches = 0;
		this.evals = 0;
	}

	@Override
	public Move chooseMove(GameState state) {
		searches = 1;
		evals = 0;

		if (state.gameIsOver()) throw new IllegalArgumentException("Can't make a decision; state is terminal.");

		List<? extends Move> choices = state.possibleMoves();
		if (choices.size() == 1) {
			return choices.get(0);
		}

		// we expand the first level here so we can keep track of the best move (because
		// the negamax method doesn't keep track of moves, just values).
		Move bestChoice = null;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		for (Move choice : getOrderedMoves(state, searchDepth - differential, -beta, -alpha)) {
			state.makeMoveUnchecked(choice);
			double util = -negamax(state, searchDepth - 1,  -beta, -alpha);
			state.undoMoveUnchecked(choice);

			if (util > alpha) {
				alpha = util;
				bestChoice = choice;
			}

			// this is sufficient for alpha-beta pruning
			if (alpha >= beta) {
				return bestChoice;
			}
		}

		return bestChoice;
	}

	private double negamax(GameState state, Integer depth, Double alpha, Double beta) {
		searches++;

		if (state.gameIsOver() || depth <= 0) {
			evals++;
			double util = Utils.utilityOf(state);
			return (state.playerToMove() == PlayerId.WHITE) ? util : -util;
		}

		for (Move choice : getOrderedMoves(state, depth - differential, -beta, -alpha)) {
			state.makeMoveUnchecked(choice);
			double util = -negamax(state, depth - 1, -beta, -alpha);
			state.undoMoveUnchecked(choice);

			if (util > alpha) {
				alpha = util;
			}

			// this is sufficient for alpha-beta pruning
			if (alpha >= beta) {
				return alpha;
			}
		}
		return alpha;
	}

	private OrderedMoveList getOrderedMoves(GameState state, Integer interiorSearchDepth,  Double alpha, Double beta) {
		searches++;

		if (state.gameIsOver()) throw new IllegalArgumentException("Can't make a move; state is terminal.");

		// we expand the first level here so we can keep track of the moves (because
		// the negamax method doesn't keep track of moves, just values).
		OrderedMoveList orderedChoices = new OrderedMoveList();
		for (Move choice : state.possibleMoves()) {
			state.makeMoveUnchecked(choice);

			// This is our "pruning." If alpha > beta, we use alpha as the util value.
			// This may save us some shallow searches, and keeps the best node at the
			// front of the list.
			double util;
			if( alpha >= beta) {
				util = alpha;
			} else {
				util = -negamax(state, interiorSearchDepth - 1, -beta, -alpha);
			}

			state.undoMoveUnchecked(choice);

			orderedChoices.add(choice, util);

			if (util > alpha) {
				alpha = util;
			}

		}

		return orderedChoices;
	}

	@Override
	public String toString() {
		return "Ordering negamax player with depth " + searchDepth + ", differential " + differential;
	}

}
