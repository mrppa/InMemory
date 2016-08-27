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
public class InMemoryFlatFileTest {
	private static Logger log = Logger.getLogger(InMemoryFlatFileTest.class.getName());

	@Test
	public void test01GetInstance() {
		log.info("START test01GetInstance");
		InMemory.resetInstance();
		InMemoryProperties.PROP_FILE_NAME = "InMemoryFlat.properties";
		InMemory inMem = InMemory.getInstance();
		log.info("END test01GetInstance");
	}

	@Test
	public void test02PrintMemory() {
		log.info("START test02PrintMemory");
		InMemory inMem = InMemory.getInstance();
		inMem.printMemory("flatdata");
		log.info("END test02PrintMemory");
	}

	@Test
	public void test03GetDataValue() {
		log.info("START test03GetDataValue");
		InMemory inMem = InMemory.getInstance();
		assertEquals("  REC2", inMem.getDataValue("flatdata", "0002", "ABC"));
		log.info("END test03GetDataValue");

	}

	@Test
	public void test04ResetDataSet() {
		log.info("START test04ResetDataSet");
		InMemory inMem = InMemory.getInstance();
		assertEquals("  REC2", inMem.getDataValue("flatdata", "0002", "ABC"));
		inMem.ResetDataSet("flatdata");
		assertEquals("  REC2", inMem.getDataValue("flatdata", "0002", "ABC"));
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
