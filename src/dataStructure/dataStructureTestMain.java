package dataStructure;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class dataStructureTestMain {

	public static void main(String[] args) {
		ArrayList<String> allShoroBorno = new Letters().shoroBorno;
		System.out.println(allShoroBorno);

		for(int i = 0; i < allShoroBorno.size(); i++) {
			System.out.println("ক" + new Letters().symbolToKar.get(allShoroBorno.get(i)));
		}

		System.out.println();

	}

}
