package checkers.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
	public static GameState parseFenFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = "";
		String file = "";
		while ((line = reader.readLine()) != null)
			file += line + "\n";
		return parseFen(file);
	}
	
	public static GameState parseFen(String fen) {
		GameState startingState;
		PositionState[] positionStates = new PositionState[32];
		for (int i = 0; i < positionStates.length; i++)
			positionStates[i] = PositionState.EMPTY;

		PlayerId playerToMove;
		int jumper = 0;

		Scanner sc = new Scanner(fen);
		sc.findInLine("([WB]):(.+?):(.+?)\\.");
		MatchResult result = sc.match();

		playerToMove = playerCharToPlayerId(result.group(1));

		parsePieces(positionStates, result.group(2), playerCharToPlayerId(result.group(2)));
		parsePieces(positionStates, result.group(3), playerCharToPlayerId(result.group(3)));

		startingState = new GameState(playerToMove, jumper, new Board(positionStates));
		return startingState;
	}

	public static String outputFen(GameState state) {
		if (state.isJumping())
			throw new IllegalArgumentException("can't output FEN mid-jump");

		return playerIdToPlayerChar(state.playerToMove()) + ":"
				+ outputPieces(state.getBoard(), PlayerId.WHITE) + ":"
				+ outputPieces(state.getBoard(), PlayerId.BLACK) + ".";
	}

	private static PlayerId playerCharToPlayerId(String group) {
		boolean whiteToMove = group.toUpperCase().charAt(0) == 'W';
		return (whiteToMove) ? PlayerId.WHITE: PlayerId.BLACK;
	}

	private static String playerIdToPlayerChar(PlayerId id) {
		return (id == PlayerId.BLACK) ? "B" : "W";
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

	private static String outputPieces(Board board, PlayerId whosePieces) {
		String ret = (whosePieces == PlayerId.BLACK) ? "B" : "W";

		boolean hasPrintedPosition = false;

		for (int i = 1; i <= 32; ++i) {
			if (board.hasPlayersPieceAt(i, whosePieces)) {
				if (hasPrintedPosition)
					ret += ",";
				if (board.hasPlayersKingAt(i, whosePieces))
					ret += "K";
				ret += i;

				hasPrintedPosition = true;
			}
		}

		return ret;
	}
}
