/**
 * 
 */
package com.mrppa.inmemory;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Pasindu
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InMemoryTest {

	@Test
	public void test01GetInstance() {
		InMemory.resetInstance();
		InMemoryProperties.PROP_FILE_NAME="InMemoryTest.properties";
		InMemory inMem = InMemory.getInstance();
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
	}


	@Test
	public void test02PrintMemory() {
		InMemory inMem = InMemory.getInstance();
		inMem.printMemory("Data 3");
	}
	
	
	@Test
	public void test03GetDataValue() {
		InMemory inMem = InMemory.getInstance();
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
	}



	@Test
	public void test04ResetDataSet() {
		InMemory inMem = InMemory.getInstance();
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
		inMem.ResetDataSet("Data 3");
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
	}
	
	@Test
	public void test05DumpData() {
		InMemory inMem = InMemory.getInstance();
		inMem.dumpData();
	}

}
