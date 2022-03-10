package dataDriven.Demo.Util;

import java.util.HashMap;

import org.testng.annotations.DataProvider;

public class DataUtil {

	@DataProvider(name = "hashDataProvider")
	public static Object[][] getTestData(ExcelReader xls_received, String testName, String sheetName) {
		ExcelReader xls = xls_received;
		String testCaseName = testName;
		String testDataSheet = sheetName;
		int testStartRowNumber = 1;
		while (!(xls.getCellData(testDataSheet, 1, testStartRowNumber).equals(testCaseName))) {
			testStartRowNumber++;
		}

		int colStartRowNumber = testStartRowNumber + 1;
		int dataStartRowNumber = testStartRowNumber + 2;
		int rows = 0;
		while (!(xls.getCellData(testDataSheet, 1, dataStartRowNumber + rows).equals(""))) {
			rows++;
		}
		// Total number of columns in the required test
		int columns = 1;
		while (!(xls.getCellData(testDataSheet, columns, colStartRowNumber).equals(""))) {
			columns++;
		}
		Object[][] obj = new Object[rows][1];
		HashMap<String, String> map = null;

		// Reading the data in the test
		for (int i = 0, row = dataStartRowNumber; row < dataStartRowNumber; row++, i++) {
			map = new HashMap<String, String>();
			for (int j = 0, column = 1; column < columns; column++, j++) {
				String key = xls.getCellData(testDataSheet, column, colStartRowNumber);
				String value = xls.getCellData(testDataSheet, column, row);
				map.put(key, value);
			}
			obj[i][0] = map;
		}
		return obj;
	}

	public static boolean isRunnable(ExcelReader xls_received, String testName, String sheet) {
		String sheetName = sheet;
		ExcelReader xls = xls_received;
		int rows = xls.getRowCount(sheetName);
		for (int r = 2; r < rows; r++) {
			String testCaseName = xls.getCellData(sheetName, 1, r);
			if (testCaseName.equals(testName)) {
				String runMode = xls.getCellData(sheetName, "Runmode", r);
				if (runMode.equals("Y"))
					return true;
				else
					return false;
			}
		}

		return false;
	}
}
