package checkers.print;

import checkers.model.Board;

public class SimpleBoardPrinter implements BoardPrinter {
	@Override
	public void print(Board b) {
		System.out.println(b);
	}
}
