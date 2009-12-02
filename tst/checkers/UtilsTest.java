package checkers;

import static checkers.Utils.*;
import static org.junit.Assert.assertEquals;

import java.awt.Point;

import org.junit.Test;

public class UtilsTest {
	@Test
	public void testGridToPosition() {
		assertEquals("Position index was not as expected", 1, gridToPosition(0, 0));
		assertEquals("Position index was not as expected", 3, gridToPosition(4, 0));
		assertEquals("Position index was not as expected", 5, gridToPosition(1, 1));
		assertEquals("Position index was not as expected", 8, gridToPosition(7, 1));
		assertEquals("Position index was not as expected", 10, gridToPosition(2, 2));
		assertEquals("Position index was not as expected", 15, gridToPosition(5, 3));
		assertEquals("Position index was not as expected", 28, gridToPosition(6, 6));
		assertEquals("Position index was not as expected", 32, gridToPosition(7, 7));
	}
	
	@Test
	public void testGridToPositionPoint() {
		assertEquals("Position index was not as expected", 1, gridToPosition(new Point(0, 0)));
		assertEquals("Position index was not as expected", 3, gridToPosition(new Point(4, 0)));
		assertEquals("Position index was not as expected", 5, gridToPosition(new Point(1, 1)));
		assertEquals("Position index was not as expected", 8, gridToPosition(new Point(7, 1)));
		assertEquals("Position index was not as expected", 10, gridToPosition(new Point(2, 2)));
		assertEquals("Position index was not as expected", 15, gridToPosition(new Point(5, 3)));
		assertEquals("Position index was not as expected", 28, gridToPosition(new Point(6, 6)));
		assertEquals("Position index was not as expected", 32, gridToPosition(new Point(7, 7)));
	}
	
	@Test
	public void testPointToGridPoint() {
		assertEquals("Grid location was not as expected", new Point(0, 0), positionToGridPoint(1));
		assertEquals("Grid location was not as expected", new Point(4, 0), positionToGridPoint(3));
		assertEquals("Grid location was not as expected", new Point(1, 1), positionToGridPoint(5));
		assertEquals("Grid location was not as expected", new Point(7, 1), positionToGridPoint(8));
		assertEquals("Grid location was not as expected", new Point(2, 2), positionToGridPoint(10));
		assertEquals("Grid location was not as expected", new Point(5, 3), positionToGridPoint(15));
		assertEquals("Grid location was not as expected", new Point(6, 6), positionToGridPoint(28));
		assertEquals("Grid location was not as expected", new Point(7, 7), positionToGridPoint(32));
	}
}
