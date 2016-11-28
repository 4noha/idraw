package test.idraw.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import idraw.model.Ngwords;

public class NgwordsTest {

	@Test
	public void test() {
		String text = "ahou";
		boolean expected = true;
		boolean actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "アホ";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "阿呆";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "ドアホ";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "馬鹿め";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "大馬鹿者";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "ア";
		expected = false;
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "b";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "ha";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "bak";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "manuk";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));

		text = "omanu";
		actual = Ngwords.isTaboo(text);
		assertThat(actual, is(expected));
	}

}
