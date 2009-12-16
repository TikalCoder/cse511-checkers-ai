package checkers.ai;

import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

public class NegamaxPlayer extends AIPlayer implements Cloneable {
	private int searchDepth;
	
	/**
	 * Private constructor with no arguments. Disallows creation of a NegamaxPlayer
	 * if there was no depth specified. Use the NegamaxPlayer(Integer searchDepth)
	 * constructor.
	 */
	@SuppressWarnings("unused")
	private NegamaxPlayer() {
		super();
	}
	
	/**
	 * Constructs a Player that chooses moves based on a negamax search with a maximum
	 * depth of searchDepth.
	 * @param searchDepth
	 */
	public NegamaxPlayer(int searchDepth) {
		super();
		this.searchDepth = searchDepth;
		
		// statistics for each search
		this.searches = 0;
		this.evals = 0;
	}

	@Override
	public Move chooseMove(GameState state) {
		searches = 1;
		evals = 0;
		
		if (state.gameIsOver()) throw new IllegalArgumentException("Can't make a decision; state is terminal.");
		
		// we expand the first level here so we can keep track of the best move (because
		// the negamax method doesn't keep track of moves, just values).
		Move bestChoice = null;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		
		for (Move choice : state.possibleMoves()) {
			state.makeMoveUnchecked(choice);
			double util = -negamax(state, searchDepth - 1,  -beta, -alpha, choice);
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
	
	private Double negamax(GameState state, int depth, double alpha, double beta, Move lastMove) {
		searches++;
		
		if (state.gameIsOver() || depth <= 0) {
			evals++;
			double util = Utils.utilityOf(state);
			return (state.playerToMove() == PlayerId.WHITE) ? util : -util;
		}
		
		for (Move choice : state.possibleMoves()) {
			state.makeMoveUnchecked(choice);
			double util = -negamax(state, depth - 1, -beta, -alpha, choice);
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

	@Override
	public Object clone() throws CloneNotSupportedException {
		NegamaxPlayer clone =  (NegamaxPlayer) super.clone();
		clone.evals = 0;
		clone.searches = 0;
		return clone;
	}
	
	@Override
	public String toString() {
		return "Negamax player with depth " + searchDepth;
	}

}
