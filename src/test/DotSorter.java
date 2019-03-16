package test;

import java.util.Comparator;

import dataStructure.Dot;

public class DotSorter implements Comparator<Dot> {

	@Override
	public int compare(Dot arg0, Dot arg1) {

		if(arg0.getCenter().getY() > arg1.getCenter().getY())
			return 1;


		return 0;
	}


}
