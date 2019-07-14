package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;

//import com.sun.javafx.scene.traversal.WeightedClosestCorner;

import dataStructure.Dot;
import dataStructure.Point;

public class Temp {

	public static void main(String[] args) {

		ArrayList<String> arrayList = new FileReadWriter().readStringsFromFile("data/uniqueFile.txt");
		System.out.println(arrayList.size());
		arrayList.addAll(new FileReadWriter().readStringsFromFile("data/dict.utf8.txt"));
		System.out.println(arrayList.size());

		Collections.sort(arrayList);
//		Set<String> words = new TreeSet<>(arrayList);

		ArrayList<String> uniqueWords = new ArrayList<String>();
		for(int i = 0; i < arrayList.size(); i++)
			if(!uniqueWords.contains(arrayList.get(i)))
				uniqueWords.add(arrayList.get(i));

		System.out.println(uniqueWords.size());
		new FileReadWriter().writeOutput(uniqueWords, "data/braille.dict.txt");

	}
}
