package com.mrppa.inmemory;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;

public class IntervalRefreshMaintainerTest {
	private static Logger log = Logger.getLogger(IntervalRefreshMaintainerTest.class.getName());

	@Test
	public void test() throws Exception {
		log.info("START test01GetInstance");
		InMemory.resetInstance();
		InMemoryProperties.PROP_FILE_NAME = "InMemoryIntervalRefresh.properties";
		InMemory inMem = InMemory.getInstance();
		inMem.printMemory("spdata");
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
		log.info("WAIT TO CHECK THE RELOADING PART...");
		Thread.sleep(3000);
		assertEquals("Data 3", inMem.getDataValue("spdata", "FBC", "2563"));
		log.info("END test01GetInstance");
		InMemory.resetInstance();
		Thread.sleep(3000);//IN order to thread to drop before next run
	}

}
