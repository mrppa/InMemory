package com.mrppa.inmemory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import junit.framework.TestCase;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FullTest extends TestCase {

	public void test1() {
		System.out.println("Test1 Start");
		InMemory.resetInstance();
		InMemoryProperties.PROP_FILE_NAME="InMemory.properties";
		InMemory inMem = InMemory.getInstance();

		System.out.println("WAIT TO CHECK THE RELOADING PART...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Test1 Completed");

	}

	@Test
	public void test2_DBTest() {
		System.out.println("test2_DBTest Start");
		InMemory inMem = InMemory.getInstance();
		inMem.printMemory("product");
		inMem.printMemory("status");
		assertEquals("Branch 1 Product 1", inMem.getDataValue("product", "P1", "BRANCH1"));
		inMem.ResetDataSet("product");
		assertEquals("Branch 1 Product 1", inMem.getDataValue("product", "P1", "BRANCH1"));
		assertEquals("Branch 1 Product 2", inMem.getDataValue("status", "P2", "BRANCH1"));
		assertNull(inMem.getDataValue("product", "PN", "BRANCHN"));

		System.out.println("Negative Test");
		inMem.printMemory("NonExist");
		assertNull(inMem.getDataValue("product", "P1", "NonExistKey2"));
		assertNull(inMem.getDataValue("NonExist", "NonExistKey1", "NonExistKey2"));
		System.out.println("test2_DBTest Completed");
	}

	@Test
	public void test3_CSVTest() {
		System.out.println("test2_DBTest Start");
		InMemory inMem = InMemory.getInstance();
		inMem.printMemory("spdata");
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));

		System.out.println("Negative Test");
		assertNull(inMem.getDataValue("spdata", "NonExistKey1", "NonExistKey2"));
		assertNull(inMem.getDataValue(null, null, null));

		System.out.println("test2_DBTest Completed");

	}
	
	@Test
	public void test4_XLSXTest() {
		System.out.println("test4_XLSXTest Start");
		InMemory inMem = InMemory.getInstance();
		inMem.printMemory("xlsxdata");
		assertEquals("Alfie", inMem.getDataValue("xlsxdata", "65949", "Sydney"));
		System.out.println("test4_XLSXTest Completed");
	}

}
