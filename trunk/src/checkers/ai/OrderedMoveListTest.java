package checkers.ai;

import static org.junit.Assert.*;

import org.junit.Test;

import checkers.ai.OrderedMoveList;
import checkers.model.Move;
import checkers.model.Walk;

public class OrderedMoveListTest {
	@Test
	public void testInsertion() {
		OrderedMoveList t = new OrderedMoveList();
		Move move0 = new Walk(1, 6, false);
		Move move1 = new Walk(7, 10, false);
		Move move2 = new Walk(18, 22, false);
		Move move3 = new Walk(18, 22, false);
		t.add(move0, 4.0);
		t.add(move1, 3.0);
		t.add(move2, 5.0);
		t.add(move3, 3.0); //Another 3.0 to make sure the ordering is stable
		
		assertEquals(t.get(0), move2);
		assertEquals(t.get(1), move0);
		assertEquals(t.get(2), move1);
		assertEquals(t.get(3), move3);
	}
	
}
