package idraw.model;

import java.util.Arrays;

public class Ngwords {
	public static void isTaboo(String text) {
		String [] array = new String[8];
		array[0] = "アホ";
		array[1] = "馬鹿";
		array[2] = "間抜け";
		array[3] = "戯け";
		array[4] = "baka";
		array[5] = "aho";
		array[6] = "manuke";
		array[7] = "tawake";
		if(Arrays.asList(array).contains(text)){
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}
}
