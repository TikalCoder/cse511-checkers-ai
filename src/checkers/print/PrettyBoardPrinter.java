package checkers.print;

import checkers.model.Board;

public class PrettyBoardPrinter implements BoardPrinter {
	private static final String INDENT        = "    ";
	private static final String LEFT_BORDER   = INDENT + "| ";
	private static final String RIGHT_BORDER  = " |";
	private static final String TOP_BORDER    = INDENT +
	"+------------------------------------------------------------------+";
	private static final String BOTTOM_BORDER = INDENT +
	"+------------------------------------------------------------------+";

	public void print(Board b) {
		System.out.println(TOP_BORDER);
		for (int r = 7; r >= 0; --r) {
			//for (int r = 0; r < 8; ++r) {
			System.out.print(LEFT_BORDER);
			for (int c = 7; c >= 0; --c) {
				//for (int c = 0; c < 8; ++c) {
				if ((r + c) % 2 == 0) {
					System.out.print("::::::::");
				} else {
					int pos = r * 4 + c / 2 + 1;
					if (b.hasKingAt(pos))
						System.out.printf("%2d ___  ", pos);
					else
						System.out.printf("%2d      ", pos);
				}
			}
			System.out.print(RIGHT_BORDER + "\n" + LEFT_BORDER);
			for (int c = 7; c >= 0; --c) {
				//for (int c = 0; c < 8; ++c) {
				if ((r + c) % 2 == 0) {
					System.out.print("::::::::");
				} else {
					switch (b.stateAt(r * 4 + c / 2 + 1)) {
					case EMPTY:       System.out.print("        "); break;
					case BLACK_MAN:   System.out.print("   ___  "); break;
					case BLACK_KING:  System.out.print("  (   ) "); break;
					case WHITE_MAN:   System.out.print("   ___  "); break;
					case WHITE_KING:  System.out.print("  (###) "); break;
					default: assert false;
					}
				}
			}
			System.out.print(RIGHT_BORDER + "\n" + LEFT_BORDER);
			for (int c = 7; c >= 0; --c) {
				//for (int c = 0; c < 8; ++c) {
				if ((r + c) % 2 == 0) {
					System.out.print("::::::::");
				} else {
					switch (b.stateAt(r * 4 + c / 2 + 1)) {
					case EMPTY:       System.out.print("        "); break;
					case BLACK_MAN:   System.out.print("  (   ) "); break;
					case BLACK_KING:  System.out.print("  (\"\"\") "); break;
					case WHITE_MAN:   System.out.print("  (###) "); break;
					case WHITE_KING:  System.out.print("  (###) "); break;
					default: assert false;
					}
				}
			}
			System.out.print(RIGHT_BORDER + "\n" + LEFT_BORDER);
			for (int c = 7; c >= 0; --c) {
				//for (int c = 0; c < 8; ++c) {
				if ((r + c) % 2 == 0) {
					System.out.print("::::::::");
				} else {
					switch (b.stateAt(r * 4 + c / 2 + 1)) {
					case EMPTY:       System.out.print("        "); break;
					case BLACK_MAN:   System.out.print("   \"\"\"  "); break;
					case BLACK_KING:  System.out.print("   \"\"\"  "); break;
					case WHITE_MAN:   System.out.print("   \"\"\"  "); break;
					case WHITE_KING:  System.out.print("   \"\"\"  "); break;
					default: assert false;
					}
				}
			}
			System.out.print(RIGHT_BORDER + "\n");
		}
		System.out.println(BOTTOM_BORDER);
	}
}
