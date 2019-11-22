package ray;

/**
 * Toolkit for generating large test case
 * 
 * @author Ray LI
 * @date 22 Nov 2019
 * @company ray@dcha.xyz
 */
public class TestCaseGenerator {

	public static void main(String[] args) {
		double val = 0.5;
		for (char c = 65; c < 91; c++) {
			for (int i = 1; i <= 100; i++) {
				// System.out.println(String.valueOf(c) + i + " 0.5 +");
				System.out.println(String.format("%.5f", val += 0.5));
			}
		}
	}
}
