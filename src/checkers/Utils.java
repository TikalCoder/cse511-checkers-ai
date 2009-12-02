package checkers;

import static checkers.Constants.GRID_SIZE;

import java.awt.Point;

import checkers.model.Board;

public class Utils {
	/**
	 * Converts an (x,y) location to a position index as described by {@link Board}. 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return the position index as described by {@link Board}, or -1 if location is invalid
	 */
	public static int gridToPosition(int x, int y) {
		if (!validSquare(x, y))
			return -1;
		return (int) Math.round((y * GRID_SIZE + x + 1) / 2.0);
	}

	public static int gridToPosition(Point p) {
		return gridToPosition((int) Math.round(p.getX()), (int) Math.round(p.getY()));
	}
	
	public static int[] positionToGrid(int pos) {
		int doublePos = pos * 2;
		int y = (doublePos - 2) / GRID_SIZE;
		int x = (doublePos - 1) % GRID_SIZE;
		x -= (y % 2 != 0) ? 1 : 0;
		return new int[] {x, y};
	}
	
	public static Point positionToGridPoint(int pos) {
		int[] grid = positionToGrid(pos);
		return new Point(grid[0], grid[1]);
	}
	
	public static boolean validSquare(int x, int y) {
		return (x + y) % 2 != 0;
	}
	
	public static boolean validSquare(Point p) {
		return validSquare((int) Math.round(p.getX()), (int) Math.round(p.getY()));
	}
}
