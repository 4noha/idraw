package idraw.model;

import java.util.Arrays;

public class Ngwords {
	public static boolean isTaboo(String text) {
		String[] array={
			"阿呆"
			,"馬鹿"
			,"間抜け"
			,"戯け"
			,"ahou"
			,"baka"
			,"manuke"
			,"tawake"
			,"あほ"
			,"アホ"
			,"バカ"
			,"マヌケ"
			,"たわけ"
			};
		if(text.matches(".*"+Arrays.asList(array).toString()+".*")){
			return true;
		} else {
			return false;
		}
	}
}