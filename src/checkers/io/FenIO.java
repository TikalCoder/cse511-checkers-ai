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
		
		playerToMove = playerCharToPlayerId(result.group(1));
		
		parsePieces(positionStates, result.group(2), playerCharToPlayerId(result.group(2)));
		parsePieces(positionStates, result.group(3), playerCharToPlayerId(result.group(3)));
		
		startingState = new GameState(playerToMove, jumper, new Board(positionStates));
		return startingState;
	}
	
	public static String outputFen(GameState state) {
		// TODO by Kurt
		return null;
	}

	private static PlayerId playerCharToPlayerId(String group) {
		boolean whiteToMove = group.toUpperCase().charAt(0) == 'W';
		return (whiteToMove) ? PlayerId.WHITE: PlayerId.BLACK;
	}

	private static void parsePieces(PositionState[] positionStates, String group,
			PlayerId whosePieces) {
		Scanner pieceScanner;
		pieceScanner = new Scanner(group.substring(1)).useDelimiter(",");
		while (pieceScanner.hasNext()) {
			String loc = pieceScanner.next();
			boolean isKing = loc.toUpperCase().charAt(0) == 'K';
			int position = (isKing) ? Integer.parseInt(loc.substring(1)) : Integer.parseInt(loc);
			
			positionStates[position-1] = PositionState.createPieceForPlayer(whosePieces, isKing);
		}
	}
}
