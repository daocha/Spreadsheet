package ray;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A toolkit simulating a Spreadsheet functionality
 * 
 * @author Ray LI
 * @date 22 Nov 2019
 * @company ray@dcha.xyz
 */
public class AwesomeSpreadsheet {

	/***
	 * Initialize a Spreadsheet instance with specified input stream
	 */
	public static AwesomeSpreadsheet create(BufferedReader input) throws IOException {
		String line = input.readLine();
		String[] settings = line.split(" ");

		if (settings.length != 2) {
			throw new RuntimeException(String.format(
					"Wrong format, expecting [2] integers: [width] [height] but got[%d]: %s", settings.length, line));
		}

		int cols, rows;

		try {
			cols = Integer.parseInt(settings[0]);
			rows = Integer.parseInt(settings[1]);
		} catch (NumberFormatException e) {
			throw new RuntimeException(String.format("Expecting [2] integers, but got: %s", line));
		}

		AwesomeSpreadsheet spreadsheet = new AwesomeSpreadsheet(cols, rows);

		int i = 0;
		while ((line = input.readLine()) != null) {
			spreadsheet.write(line, i);
			i++;
		}

		return spreadsheet;
	}

	/** supported operators */
	private static final String SUPPORTED_OPERATORS = "\\+\\-\\*/";

	/** RegEx pattern for cell Expressions/Formula */
	private static final String PATTERN_EXPRESSION = String
			.format("([a-zA-Z]*(-?\\d*\\.?\\d+)\\s[a-zA-Z]*(-?\\d*\\.?\\d+)\\s[%s])(?!\\d)", SUPPORTED_OPERATORS);

	/** RegEx pattern for Numeric cell */
	private static final String PATTERN_NUMBER = "^-?\\d*\\.?\\d+$";

	private static final String PATTERN_COLUMN_NAME = "^([a-zA-Z]{1})(\\d+)$";

	/**
	 * storage for all the cells
	 */
	String[][] cells;

	/**
	 * store plain numbers only (no expressions) for fast accessing
	 */
	Double[][] plain_cells;

	/**
	 * number of columns
	 */
	int cols;

	/**
	 * number of rows
	 */
	int rows;

	/** mark down currently working index, to avoid dead loop */
	Set<String> visitedCells = new LinkedHashSet<>();

	/**
	 * Instantiate a Spreadsheet
	 */
	public AwesomeSpreadsheet(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
		this.cells = new String[rows][cols];
		this.plain_cells = new Double[rows][cols];
	}

	/**
	 * Write cell into the Spreadsheet
	 * 
	 * @param cell
	 *            the cell content
	 * @param index
	 *            the order of the text input
	 */
	private void write(String cell, int index) {
		int rowIndex = index / cols;
		int colIndex = index % cols;
		if (rowIndex < rows && colIndex < cols) {
			cells[rowIndex][colIndex] = cell;
			if (isNumber(cell)) {
				plain_cells[rowIndex][colIndex] = getNumber(cell);
			}
		}
	}

	/**
	 * get the formatted string of spreadsheet structure
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d %d\n", cols, rows));
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				builder.append(String.format("%.5f", getValue(i, j)));
				if (i != rows - 1 || j != cols - 1) {
					builder.append("\n");
				}
			}
		}

		return builder.toString();
	}

	/**
	 * print the spreadsheet structure to console
	 */
	public void print() {
		System.out.println(this.toString());
	}

	/**
	 * get value from the index
	 */
	private double getValue(int rowIndex, int colIndex) {

		if (visitedCells.contains(String.format("%d-%d", rowIndex, colIndex))) {
			return postReturn(Double.NaN);
		}

		visitedCells.add(String.format("%d-%d", rowIndex, colIndex));

		// comment this will dramatically slow down the performance
		if (plain_cells[rowIndex][colIndex] != null) {
			return postReturn(plain_cells[rowIndex][colIndex]);
		}

		String cell = cells[rowIndex][colIndex];

		if (cell == null || cell.trim().isEmpty()) {
			return postReturn(0d);
		}

		double val = Double.NaN;

		// assume this is expression
		if (cell.contains(" ")) {
			// matching expressions: i.e. (A2 39 *) or (A3 B2 /)
			Pattern pattern = Pattern.compile(PATTERN_EXPRESSION);
			Matcher matcher = pattern.matcher(cell);
			while (matcher.find()) {
				// resolve the expressions one by one
				String expr = matcher.group(1);
				String[] elements = expr.split(" ");
				double cellLeft = getValue(elements[0]);
				double cellRight = getValue(elements[1]);
				String operator = elements[2];
				double result = evaluate(cellLeft, cellRight, operator.charAt(0));
				cell = matcher.replaceAll(String.valueOf(result));
				matcher = pattern.matcher(cell);
			}
			// eventually should get number value or cell reference
			val = getValue(cell);

		} else {
			// assume this is cell reference
			val = getValue(cell);
		}

		plain_cells[rowIndex][colIndex] = val;

		return postReturn(val);

	}

	/**
	 * get value of the reference column
	 * 
	 * @param cell
	 *            i.e. A1 or 24.5
	 * @return
	 */
	private double getValue(String cell) {
		if (isNumber(cell)) {
			return postReturn(getNumber(cell));
		} else {
			Pattern pattern = Pattern.compile(PATTERN_COLUMN_NAME);
			Matcher matcher = pattern.matcher(cell);
			if (matcher.find()) {
				// this should be A-Z
				String rowName = matcher.group(1);
				try {
					// transform to row index
					int rowIndex = rowName.toUpperCase().charAt(0) - 65;

					// find column index
					int colIndex = Integer.parseInt(matcher.group(2));
					colIndex--;
					return getValue(rowIndex, colIndex);
				} catch (NumberFormatException e) {
					// invalid cellName
					return postReturn(Double.NaN);
				}
			}
			// invalid cellName
			return postReturn(Double.NaN);
		}

	}

	/**
	 * evaluate the formula: num1 [operator] num2
	 */
	private double evaluate(double num1, double num2, char operator) {
		switch (operator) {
		case '+':
			return num1 + num2;
		case '-':
			return num1 - num2;
		case '*':
			return num1 * num2;
		case '/':
			return num1 / num2;
		}
		return Double.NaN;
	}

	/**
	 * return if the cell content is number
	 */
	private boolean isNumber(String cell) {
		return cell.matches(PATTERN_NUMBER);
	}

	/**
	 * get numeric value from the cell content
	 */
	private double getNumber(String cell) {
		try {
			return Double.parseDouble(cell);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	/**
	 * post action after returning, clearing the history of visited cells
	 */
	private double postReturn(double result) {
		visitedCells.clear();
		return result;
	}

}
