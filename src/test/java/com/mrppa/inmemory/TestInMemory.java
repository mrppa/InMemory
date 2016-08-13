 package com.mrppa.inmemory;

 import java.io.IOException;

 import org.junit.Test;

 import junit.framework.TestCase;

 public class TestInMemory extends TestCase {

 @Test
 public void test1() throws IOException {
 InMemory inMem = InMemory.getInstance();

 System.out.println("##########################################");
 inMem.printMemory("product");
 System.out.println("##########################################");

 System.out.println(inMem.getDataValue("product", "1111111112", "0001"));
 try {
 Thread.sleep(10000);
 } catch (InterruptedException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
 inMem.printMemory("status");
 }

 }
