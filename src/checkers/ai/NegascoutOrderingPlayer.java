package checkers.ai;

import java.util.List;

import checkers.ai.AIPlayer;
import checkers.ai.OrderedMoveList;
import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

public class NegascoutOrderingPlayer extends AIPlayer {
	private int searchDepth;
	private int differential;
	
	/**
	 * Private constructor with no arguments. Disallows creation of a NegascoutOrderingPlayer
	 * if there was no depth specified. Use the 
	 * 
	 * <code>NegascoutOrderingPlayer(Integer searchDepth, Integer searchDifferential)</code>
	 * 
	 * constructor instead.
	 */
	@SuppressWarnings("unused")
	private NegascoutOrderingPlayer() {
		super();
	}
	
	/**
	 * Constructs a Player that chooses moves based on a negascout search with a maximum
	 * depth of searchDepth and uses internal searches <code>current depth - differential</code> plies
	 * shallower to order the moves.
	 * 
	 * @param searchDepth How deep to search.
	 * @param searchDifferential How many fewer plies to search for internal ordering searches.
	 */
	public NegascoutOrderingPlayer(int searchDepth, int searchDifferential) {
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
		// the negascout method doesn't keep track of moves, just values).
		Move bestChoice = null;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		double b = beta;
		for (Move choice : getOrderedMoves(state, searchDepth - differential, -beta, -alpha)) {
			state.makeMoveUnchecked(choice);
			double util = -negascout(state, searchDepth - 1,  -b, -alpha);
			state.undoMoveUnchecked(choice);

			if (util > alpha) {
				alpha = util;
				bestChoice = choice;
			}
			
			// this is sufficient for alpha-beta pruning
			if (alpha >= beta) {
				return bestChoice;
			}
			
			// re-search if failed high
			if (alpha >= b) {
				state.makeMoveUnchecked(choice);
				alpha = -negascout(state, searchDepth - 1, -beta, -alpha);
				state.undoMoveUnchecked(choice);
				bestChoice = choice;
				if (alpha >= beta) {
					return bestChoice;
				}
			}
			
			b = alpha + 1; // set new window
		}
		
		return bestChoice;
	}

	private double negascout(GameState state, Integer depth, Double alpha, Double beta) {
		searches++;
		
		if (state.gameIsOver() || depth <= 0) {
			evals++;
			double util = Utils.utilityOf(state);
			return (state.playerToMove() == PlayerId.WHITE) ? util : -util;
		}

		double b = beta;
		for (Move choice : getOrderedMoves(state, depth - differential, -beta, -alpha)) {
			state.makeMoveUnchecked(choice);
			double util = -negascout(state, depth - 1, -b, -alpha);
			state.undoMoveUnchecked(choice);

			if (util > alpha) {
				alpha = util;
			}
			
			// this is sufficient for alpha-beta pruning
			if (alpha >= beta) {
				return alpha;
			}
			
			// re-search if failed high
			if (alpha >= b) {
				state.makeMoveUnchecked(choice);
				alpha = -negascout(state, depth - 1, -beta, -alpha);
				state.undoMoveUnchecked(choice);
				if (alpha >= beta) {
					return alpha;
				}
			}
			
			b = alpha + 1; // set new window
		}
		return alpha;
	}
	
	private OrderedMoveList getOrderedMoves(GameState state, Integer interiorSearchDepth,  Double alpha, Double beta) {
		searches++;
		
		if (state.gameIsOver()) throw new IllegalArgumentException("Can't make a move; state is terminal.");
		
		// we expand the first level here so we can keep track of the moves (because
		// the negascout method doesn't keep track of moves, just values).
		OrderedMoveList orderedChoices = new OrderedMoveList();
		double b = beta;
		for (Move choice : state.possibleMoves()) {

			// This is our "pruning." If alpha >= beta, we use alpha as the util value.
			// This may save us some shallow searches, and keeps the best node at the
			// front of the list.
			double util;
			if( alpha >= beta) {
				util = alpha;
			} else {
				state.makeMoveUnchecked(choice);
				util = -negascout(state, interiorSearchDepth - 1, -b, -alpha);
				state.undoMoveUnchecked(choice);
			}

			orderedChoices.add(choice, util);
			
			if (util > alpha) {
				alpha = util;
			}
			
			// re-search if failed high
			if (alpha >= b) {
				state.makeMoveUnchecked(choice);
				alpha = -negascout(state, interiorSearchDepth - 1, -beta, -alpha);
				state.undoMoveUnchecked(choice);
			}
			
			b = alpha + 1; // set new window
			
		}
		
		return orderedChoices;
	}
	
	@Override
	public String toString() {
		return "Ordering negascout player with depth " + searchDepth + ", differential " + differential;
	}
	
}