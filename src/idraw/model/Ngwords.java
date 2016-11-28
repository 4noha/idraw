package idraw.model;

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
		for(String a:array){
			if(text.indexOf(a) != -1){
				return true;
			}
		}
		return false;
	}
}