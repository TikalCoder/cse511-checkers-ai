package checkers.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.MatchResult;

import checkers.model.Board;
import checkers.model.GameState;
import checkers.model.PlayerId;
import checkers.model.Board.PositionState;

/**
 * Allow FEN input and output with parseFen and outputFen.
 * 
 * http://en.wikipedia.org/wiki/Portable_Draughts_Notation#Tag_Pairs
 * 
 * @author Scott Bressler
 * @author Kurt Glastetter
 */
public class FenIO {
	public static GameState parseFen(String filename) throws FileNotFoundException {
		GameState startingState;
		PositionState[] positionStates = new PositionState[32];
		for (int i = 0; i < positionStates.length; i++)
			positionStates[i] = PositionState.EMPTY;
		PlayerId playerToMove;
		int jumper = 0;
		
		Scanner sc = new Scanner(new FileReader(filename));
		sc.findInLine("([WB]):(.+?):(.+?)\\.");
		MatchResult result = sc.match();
		
		boolean whiteToMove = result.group(1).toUpperCase().charAt(0) == 'W';
		playerToMove = (whiteToMove) ? PlayerId.WHITE: PlayerId.BLACK;
		
		System.out.println(playerToMove);
		
		boolean whitesPieces = result.group(2).toUpperCase().charAt(0) == 'W';
		PlayerId whosePieces = (whitesPieces) ? PlayerId.WHITE: PlayerId.BLACK;
			
		parsePieces(positionStates, result, whosePieces, 2);
		parsePieces(positionStates, result, whosePieces.opponent(), 3);
		
		startingState = new GameState(playerToMove, jumper, new Board(positionStates));
		return startingState;
	}
	
	public static String outputFen(GameState state) {
		// TODO by Kurt
		return null;
	}

	private static void parsePieces(PositionState[] positionStates, MatchResult result,
			PlayerId whosePieces, int resultGroup) {
		Scanner pieceScanner;
		pieceScanner = new Scanner(result.group(resultGroup).substring(1)).useDelimiter(",");
		while (pieceScanner.hasNext()) {
			String loc = pieceScanner.next();
			boolean isKing = loc.toUpperCase().charAt(0) == 'K';
			int position = (isKing) ? Integer.parseInt(loc.substring(1)) : Integer.parseInt(loc);
			
			positionStates[position-1] = PositionState.createPieceForPlayer(whosePieces, isKing);
		}
	}
}
