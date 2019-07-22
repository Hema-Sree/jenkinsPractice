package com.ggk.utils;


import org.testng.TestNG;

import com.ggk.orangehrm.AddVerifyDeleteUser;
import com.ggk.orangehrm.AddVerifyUser;

public class TestSuiteExecutor {

	public static void main(String[] args) {

		TestNG testSuite = new TestNG();
		testSuite.setTestClasses(new Class[] { AddVerifyDeleteUser.class });
		testSuite.run();
	}

}
