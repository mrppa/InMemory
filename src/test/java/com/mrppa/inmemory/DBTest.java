package com.mrppa.inmemory;

import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class DBTest extends TestCase {

	@Test
	public void test1() throws IOException {
		InMemory inMem = InMemory.getInstance();

		System.out.println("##########################################");
		inMem.printMemory("product");
		System.out.println("##########################################");

		System.out.println(inMem.getDataValue("product", "P1", "BRANCH1"));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inMem.printMemory("status");
		System.out.println(inMem.getDataValue("status", "P2", "BRANCH1"));

	}

}
