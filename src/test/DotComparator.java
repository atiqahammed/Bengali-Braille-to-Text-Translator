package test;

import java.util.Comparator;

import dataStructure.Dot;

public class DotComparator implements Comparator<Dot> {

	private Dot dot;

	public DotComparator(Dot dot) {
		this.dot = dot;
	}

	@Override
	public int compare(Dot arg0, Dot arg1) {
		int distance0 = (int) dot.getCenter().getDistance(arg0.getCenter());
		int distance1 = (int) dot.getCenter().getDistance(arg1.getCenter());
		if(distance0 > distance1) return 1;
		return -1;
 	}
}