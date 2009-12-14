package checkers.ai;

import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

public class NegamaxPlayer extends AIPlayer {
	private Integer searchDepth;
	private Integer searches;
	private Integer evals;
	
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
	public NegamaxPlayer(Integer searchDepth) {
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
		Double alpha = Double.NEGATIVE_INFINITY;
		Double beta = Double.POSITIVE_INFINITY;
		for (Move choice : state.possibleMoves()) {
			GameState successor = (GameState) state.clone();
			successor.makeMove(choice);
			Double util = -negamax(successor, searchDepth - 1,  -beta, -alpha, choice);
			
			if (util > alpha) {
				alpha = util;
				bestChoice = choice;
			}
			
			// this is sufficient for alpha-beta pruning
			if (alpha > beta) {
				return bestChoice;
			}
		}
		
		System.out.println("Depth: " + searchDepth + " plies");
		System.out.println("Searches: " + searches);
		System.out.println("Evals: " + evals);
		
		return bestChoice;
	}
	
	private Double negamax(GameState state, Integer depth, Double alpha, Double beta, Move lastMove) {
		searches++;
		
		if (state.gameIsOver() || (depth <= 0 && !lastMove.isJump())) {
			evals++;
			Double util = Utils.utilityOf(state);
			return (state.playerToMove() == PlayerId.WHITE) ? util : -util;
		}
		
		for (Move choice : state.possibleMoves()) {
			GameState successor = (GameState) state.clone();
			successor.makeMove(choice);
			Double util = -negamax(successor, depth - 1, -beta, -alpha, choice);
			
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

}
