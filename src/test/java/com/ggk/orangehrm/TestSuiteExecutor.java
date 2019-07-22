package com.ggk.orangehrm;


import java.util.ArrayList;

import org.testng.TestNG;
import org.testng.xml.XmlSuite.ParallelMode;

import com.ggk.utils.ExcelOperations;

public class TestSuiteExecutor{

	public static void main(String[] args) {

		int rowNumber,classNumber = -1;
		String genericClassName,className,statusMessage,runMessage;
		ArrayList<Class> arrayList = new ArrayList<Class>();
		arrayList.add(AddVerifyDeleteUser.class);
		//arrayList.add(AddVerifyUser.class);
		//arrayList.add(AddUser.class);
		//arrayList.add(UserLogin.class);
		Class[] classes = new Class[arrayList.size()];		
		ExcelOperations excel = new ExcelOperations();
		excel.loadExcel(System.getProperty("user.dir")+"\\src\\test\\resources\\data\\TestsResults.xls", "Sheet1");
		for(int i=0;i<arrayList.size();i++)
		{
			genericClassName = arrayList.get(i).toString();
			className = genericClassName.substring(genericClassName.lastIndexOf('.')+1);
			rowNumber = excel.getRowNumber(className,"TestCaseName");
			if(rowNumber!=-1)
			{
				runMessage = excel.readData(rowNumber, excel.getColumnNumber("run"));
				statusMessage = excel.readData(rowNumber, excel.getColumnNumber("status"));
				if(runMessage.equals("Y") && statusMessage.equals("NIL"))
				{
					classNumber++;
					classes[classNumber] = arrayList.get(i);
				}
			}
		}
		if(classNumber != -1)
		{
			Class[] runClasses = new Class[classNumber+1];
			System.arraycopy(classes, 0, runClasses, 0, classNumber+1);
			TestNG testSuite = new TestNG();
			testSuite.setTestClasses(runClasses);
			//testSuite.setSuiteThreadPoolSize(5);
			//testSuite.setThreadCount(3);
			//testSuite.setParallel(ParallelMode.CLASSES);
			testSuite.run();
		}
		else
		{
			System.out.println("There are no classes to run");
		}
		
	}

}
