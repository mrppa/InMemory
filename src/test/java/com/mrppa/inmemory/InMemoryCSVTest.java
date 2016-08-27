/**
 * 
 */
package com.mrppa.inmemory;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Pasindu
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InMemoryCSVTest {
	private static Logger log = Logger.getLogger(InMemoryCSVTest.class.getName());

	@Test
	public void test01GetInstance() {
		log.info("START test01GetInstance");
		InMemory.resetInstance();
		InMemoryProperties.PROP_FILE_NAME = "InMemoryCSV.properties";
		InMemory inMem = InMemory.getInstance();
		log.info("END test01GetInstance");
	}

	@Test
	public void test02PrintMemory() {
		log.info("START test02PrintMemory");
		InMemory inMem = InMemory.getInstance();
		inMem.printMemory("spdata");
		log.info("END test02PrintMemory");

	}

	@Test
	public void test03GetDataValue() {
		log.info("START test03GetDataValue");
		InMemory inMem = InMemory.getInstance();
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
		log.info("END test03GetDataValue");

	}

	@Test
	public void test04ResetDataSet() {
		log.info("START test04ResetDataSet");
		InMemory inMem = InMemory.getInstance();
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
		inMem.ResetDataSet("spdata");
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
		log.info("END test04ResetDataSet");

	}

	@Test
	public void test05DumpData() {
		log.info("START test05DumpData");
		InMemory inMem = InMemory.getInstance();
		inMem.dumpData();
		log.info("END test05DumpData");

	}

}
