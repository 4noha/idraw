package idraw.model;

import java.util.Arrays;

public class Ngwords {
	public static void isTaboo(String text) {
		String[] array={
			"阿呆",
			"馬鹿",
			"間抜け",
			"戯け",
			"ahou",
			"baka",
			"manuke",
			"tawake"};
		if(Arrays.asList(array).contains(text)){
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}
}