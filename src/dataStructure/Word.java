package dataStructure;

import util.Constant;

public class Word {

	private String first_word;
	private String second_word;

	private String selected_word;

	private int upper_line_index;
	private int middle_line_index;
	private int lower_line_index;

	private int edit_distance;

	public Word(String first_word, String second_word, int upper_line_index, int middle_line_index, int lower_line_index) {
		this.first_word = first_word;
		this.second_word = second_word;

		this.upper_line_index = upper_line_index;
		this.middle_line_index = middle_line_index;
		this.lower_line_index = lower_line_index;
		selected_word = "*";
	}

	public void print_all_information_of_word() {
		Constant.OUTPUT_LIST.add("line index:: " + upper_line_index + " " + middle_line_index + " " + lower_line_index);
		Constant.OUTPUT_LIST.add("first word :: " + first_word + " ---  second word :: " + second_word);
		Constant.OUTPUT_LIST.add("selected word::  " + selected_word);
	}

	public void set_selected_word(String word) {
		selected_word = word;
	}

	public String get_selected_word() {
		return selected_word;
	}

	public void set_edit_distance(int distance) {
		edit_distance = distance;
	}

	public int get_edit_distance() {
		return edit_distance;
	}

	public String get_first_word() {
		return first_word;
	}

	public String get_second_word() {
		return second_word;
	}

}
