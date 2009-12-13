package checkers.ai;

import java.util.List;

import checkers.Player;
import checkers.model.GameState;
import checkers.model.Move;
import checkers.model.PlayerId;

public class NegamaxOrderingPlayer extends Player {
	private Integer searchDepth;
	private Integer orderingSearchDepth;
	
	/**
	 * Private constructor with no arguments. Disallows creation of a NegamaxPlayer
	 * if there was no depth specified. Use the NegamaxPlayer(Integer searchDepth)
	 * constructor.
	 */
	@SuppressWarnings("unused")
	private NegamaxOrderingPlayer() {
		super();
	}
	
	/**
	 * Constructs a Player that chooses moves based on a negamax search with a maximum
	 * depth of searchDepth.
	 * @param searchDepth
	 */
	public NegamaxOrderingPlayer(Integer searchDepth, Integer orderingSearchDepth) {
		super();
		this.searchDepth = searchDepth;
		this.orderingSearchDepth = orderingSearchDepth;
	}

	@Override
	public Move chooseMove(GameState state) {
		if (state.gameIsOver()) throw new IllegalArgumentException("Can't make a decision; state is terminal.");
		
		// we expand the first level here so we can keep track of the best move (because
		// the negamax method doesn't keep track of moves, just values).
		Move bestChoice = null;
		Double alpha = Double.NEGATIVE_INFINITY;
		Double beta = Double.POSITIVE_INFINITY;
		for (Move choice : getOrderedMoves(state, searchDepth + orderingSearchDepth, alpha, beta)) {
			GameState successor = (GameState) state.clone();
			successor.makeMove(choice);
			Double util = -negamax(successor, searchDepth - 1, searchDepth + orderingSearchDepth,  -beta, -alpha);
			
			if (util > alpha) {
				alpha = util;
				bestChoice = choice;
			}
			
			// this is sufficient for alpha-beta pruning
			if (alpha > beta) {
				return bestChoice;
			}
		}
		
		return bestChoice;
	}

	private Double negamax(GameState state, Integer depth, Integer interiorSearchDepth, Double alpha, Double beta) {
		if (state.gameIsOver() || depth <= 0) {
			Double util = Utils.utilityOf(state);
			return (state.playerToMove() == PlayerId.WHITE) ? util : -util;
		}
		
		for (Move choice : getOrderedMoves(state, interiorSearchDepth, alpha, beta)) {
			GameState successor = (GameState) state.clone();
			successor.makeMove(choice);
			Double util = -negamax(successor, depth - 1, interiorSearchDepth, -beta, -alpha);
			
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
	
	private List<Move> getOrderedMoves(GameState state, Integer interiorSearchDepth,  Double alpha, Double beta) {
		if (state.gameIsOver()) throw new IllegalArgumentException("Can't make a move; state is terminal.");
		
		// we expand the first level here so we can keep track of the best move (because
		// the negamax method doesn't keep track of moves, just values).
		OrderedMoveList orderedChoices = new OrderedMoveList();
		for (Move choice : state.possibleMoves()) {
			GameState successor = (GameState) state.clone();
			successor.makeMove(choice);
			
			Double util = -negamax(successor, interiorSearchDepth - 1, interiorSearchDepth + orderingSearchDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			orderedChoices.add(choice, util);
			
		}
		
		return orderedChoices.toList(); //TODO avoid converting to list
	}
	
}
