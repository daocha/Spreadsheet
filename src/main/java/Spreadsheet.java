import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ray.AwesomeSpreadsheet;

/**
 * @author Ray LI
 * @date 22 Nov 2019
 * @company ray@dcha.xyz
 */
public class Spreadsheet {
	public static void main(String[] args) throws IOException {
		// BufferedReader input = new BufferedReader(
		// new InputStreamReader(new
		// FileInputStream("/home/daocha/myworkspace/GetStarted/src/spreadsheet3.txt")));
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		AwesomeSpreadsheet spreadsheet = AwesomeSpreadsheet.create(input);

		spreadsheet.print();

	}
}
