package checkers;

import static checkers.Constants.GRID_SIZE;

import java.awt.Point;

public class Utils {
	public static int gridToPosition(int x, int y) {
		return (int) Math.round((y * GRID_SIZE + x + 1) / 2.0);
	}

	public static int gridToPosition(Point p) {
		return (int) Math.round((p.getY() * GRID_SIZE + p.getX() + 1) / 2.0);
	}
	
	public static int[] positionToGrid(int pos) {
//		return (int) Math.round((y * GRID_SIZE + x + 1) / 2.0);
		int doublePos = pos * 2;
		int y = (doublePos - 2) / GRID_SIZE;
		int x = (doublePos - 1) % GRID_SIZE;
		x -= (y % 2 == 0) ? 1 : 0;
		return new int[] {x, y};
	}
	
	public static Point positionToGridPoint(int pos) {
		int[] grid = positionToGrid(pos);
		return new Point(grid[0], grid[1]);
	}
}
