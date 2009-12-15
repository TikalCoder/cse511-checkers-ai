package checkers.ai;

import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

public class NegamaxOrderingPlayer extends AIPlayer {
	private int searchDepth;
	private int differential;

	/**
	 * Private constructor with no arguments. Disallows creation of a NegamaxOrderingPlayer
	 * if there was no depth specified. Use the
	 *
	 * <code>NegamaxOrderingPlayer(Integer searchDepth, Integer orderingSearchDepth)</code>
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
	 * @param searchDepth
	 */
	public NegamaxOrderingPlayer(int searchDepth, int orderingSearchDepth) {
		super();
		this.searchDepth = searchDepth;
		this.differential = orderingSearchDepth;
	}

	@Override
	public Move chooseMove(GameState origState) {
		if (origState.gameIsOver()) throw new IllegalArgumentException("Can't make a decision; state is terminal.");

		GameState state = (GameState) origState.clone();

		// we expand the first level here so we can keep track of the best move (because
		// the negamax method doesn't keep track of moves, just values).
		Move bestChoice = null;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		for (Move choice : state.possibleMoves()) {
			state.makeMoveUnchecked(choice);
			double util = -orderingNegamax(state, searchDepth - 1, searchDepth - differential,  -beta, -alpha);
			state.undoMoveUnchecked(choice);

			if (util > alpha) {
				alpha = util;
				bestChoice = choice;
			}

			// this is sufficient for alpha-beta pruning
			if (alpha > beta) {
				return bestChoice;
			}
		}

		if (!state.equals(origState))
			throw new RuntimeException("internal error: game state inconsistent");

		//FIXME: Kluge put in by Kurt.  Sometimes bestChoice is null due to
		// util (above) never increasing above negative infinity.  Another way
		// to fix it is to use >= instead of > in the comparison between util
		// and alpha, but this leads to very different games being played.
		// There may be other ways to fix it as well.  To reproduce the
		// problem, use FEN "B:WK5,6,17,24:BK2.", which was created by playing
		// Negamax(4) as white against Negamax(1) as black.
		//
		if (bestChoice == null)
			bestChoice = state.possibleMoves().get(0);

		return bestChoice;
	}

	private double negamax(GameState state, Integer depth, Double alpha, Double beta) {
		if (state.gameIsOver() || depth <= 0) {
			double util = Utils.utilityOf(state);
			return (state.playerToMove() == PlayerId.WHITE) ? util : -util;
		}

		for (Move choice : state.possibleMoves()) {
			state.makeMoveUnchecked(choice);
			double util = -negamax(state, depth - 1, -beta, -alpha);
			state.undoMoveUnchecked(choice);

			if (util > alpha) {
				alpha = util;
			}

			// this is sufficient for alpha-beta pruning
			if (alpha > beta) {
				return alpha;
			}
		}
		return alpha;
	}

	private double orderingNegamax(GameState state, Integer depth, Integer interiorSearchDepth, Double alpha, Double beta) {
		if (state.gameIsOver() || depth <= 0) {
			double util = Utils.utilityOf(state);
			return (state.playerToMove() == PlayerId.WHITE) ? util : -util;
		}

		for (Move choice : getOrderedMoves(state, interiorSearchDepth, -beta, -alpha)) {
			state.makeMoveUnchecked(choice);
			double util = -orderingNegamax(state, depth - 1, interiorSearchDepth - differential, -beta, -alpha);
			state.undoMoveUnchecked(choice);

			if (util > alpha) {
				alpha = util;
			}

			// this is sufficient for alpha-beta pruning
			if (alpha > beta) {
				return alpha;
			}
		}
		return alpha;
	}

	private OrderedMoveList getOrderedMoves(GameState state, Integer interiorSearchDepth,  Double alpha, Double beta) {
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
			if( alpha > beta) {
				util = alpha;
			} else {
				util = -orderingNegamax(state, interiorSearchDepth - 1, interiorSearchDepth - differential, -beta, -alpha);
			}

			state.undoMoveUnchecked(choice);

			orderedChoices.add(choice, util);

			if (util > alpha) {
				alpha = util;
			}

		}

		return orderedChoices; //TODO avoid converting to list
	}

}
