package checkers.print;

import checkers.model.Board;

/**
 * SimpleBoardPrinter
 *
 * @author Kurt Glastetter
 * @author Scott Bressler
 */
public class SimpleBoardPrinter implements BoardPrinter {
	@Override
	public void print(Board b) {
		System.out.println(b);
	}
}
