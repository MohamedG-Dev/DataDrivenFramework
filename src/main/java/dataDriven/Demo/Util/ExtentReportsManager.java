package dataDriven.Demo.Util;

import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportsManager {

	public static ExtentReports getInstance() {
		Date date = new Date();
		String fileNameType = date.toString().replace(":", "_").replace(" ", "_") + ".html";
		String filePath = "./reports/" + fileNameType;
		ExtentReports report = new ExtentReports();
		ExtentSparkReporter spark=new ExtentSparkReporter(filePath);
		report.attachReporter(spark);
		return report;
	}

}
