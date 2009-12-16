package checkers.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import checkers.model.Move;


public class OrderedMoveList implements Iterable<Move> {
	
	private List<Pair> list;
	
	public OrderedMoveList() {
		list = new ArrayList<Pair>();
	}
	
	public void add(Move m, Double val) {
		ListIterator<Pair> iter = list.listIterator();
		while (iter.hasNext()) {
			if (iter.next().getVal() < val) {
				iter.previous();
				break;
			}
		}
		iter.add(new Pair(m, val));
	}
	
	public Move get(int index) {
		return list.get(index).getMove();
	}
	
	public Iterator<Move> iterator() {
		return new OrderedMoveListIterator();
	}
	
	public List<Move> toList() {
		List<Move> result = new ArrayList<Move>();
		for (Pair p : list) {
			result.add(p.getMove());
		}
		return result;
	}
	
	private class Pair {
		private Move m;
		private Double val;
		
		public Pair(Move m, Double val) {
			this.m = m;
			this.val = val;
		}
		
		public Move getMove() { return m; };
		public Double getVal() { return val; };
	}
	
	private class OrderedMoveListIterator implements Iterator<Move> {
		private Integer index;
		
		public OrderedMoveListIterator() {
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < list.size();
		}

		@Override
		public Move next() {
			Move result = list.get(index).getMove();
			index++;
			return result;
		}

		@Override
		public void remove() {
			list.remove(index);			
		}
		
	}

}
