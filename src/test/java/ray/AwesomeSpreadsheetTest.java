package ray;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

/**
 * Test case for Speadsheet toolkit
 * 
 * @author Ray LI
 * @date 22 Nov 2019
 * @company ray@dcha.xyz
 */
public class AwesomeSpreadsheetTest {

	@Test
	public void test1() throws IOException {
		verify("test1_in.txt", "test1_out.txt");
	}

	@Test
	public void test2() throws IOException {
		verify("test2_in.txt", "test2_out.txt");
	}

	@Test
	public void test3() throws IOException {
		verify("test3_in.txt", "test3_out.txt");
	}

	@Test
	public void test4() throws IOException {
		verify("test4_in.txt", "test4_out.txt");
	}

	@Test
	public void test5() throws IOException {
		verify("test5_in.txt", "test5_out.txt");
	}

	/**
	 * Verify if Spreadsheet with input request can generate the expected output
	 */
	public void verify(String in, String out) throws IOException {
		BufferedReader input = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(in)));
		BufferedReader output = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(out)));

		AwesomeSpreadsheet spreadsheet = AwesomeSpreadsheet.create(input);

		assertEquals(readAsString(output), spreadsheet.toString());
	}

	/**
	 * Read buffer as String
	 */
	public String readAsString(BufferedReader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		String line = reader.readLine();
		if (line != null) {
			builder.append(line);
		}

		while ((line = reader.readLine()) != null) {
			builder.append("\n");
			builder.append(line);
		}
		return builder.toString();
	}

}
