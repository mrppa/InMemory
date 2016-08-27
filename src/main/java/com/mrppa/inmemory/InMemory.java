package com.mrppa.inmemory;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.mrppa.inmemory.dataloader.DataLoader;
import com.mrppa.inmemory.maintainer.Maintainer;

/**
 * This class service the memory requests
 * 
 * <pre>
 * Usage -
 * 		//Configure the property File first
 * 		InMemory inMem = InMemory.getInstance();//Initialize Inmemory Instance
 * 		String sKeyVal = inMem.getDataValue("[CacheSetId]", "[Key1]", "[Key2]",...)//Find the value for a single or composit key
 * </pre>
 * 
 * @author Pasindu Ariyarathna
 *
 */
public class InMemory {

	private static InMemory inMemoryInstance = null;
	private static Logger log = Logger.getLogger(InMemory.class.getName());

	private static HashMap<String, CacheSet> memoryMap = null;

	/**
	 * To Get the single instance Of InMemeory
	 * 
	 * @return single instance Of InMemory Object
	 */
	public static synchronized InMemory getInstance() {
		if (inMemoryInstance == null) {
			log.info("InMemory New Instance");
			inMemoryInstance = new InMemory();
			memoryMap = new HashMap<String, CacheSet>();
			inMemoryInstance.initAll();
		}
		return inMemoryInstance;
	}

	/**
	 * This is highly inadvisable to use. Only for testing purposes.
	 */
	public static synchronized void resetInstance() {
		inMemoryInstance = null;
	}

	private InMemory() {
	}

	private CacheSet findCacheSetByID(String cacheSetId) {
		return memoryMap.get(cacheSetId);
	}

	/**
	 * To get the memory value of given keys
	 * 
	 * @param cacheSetId
	 *            set id given in configuration
	 * @param keys
	 *            key parameters
	 * @return the respective memory value
	 */
	public String getDataValue(String cacheSetId, String... keys) {
		CacheSet cacheSet = this.findCacheSetByID(cacheSetId);
		String value = null;

		if (cacheSet != null) {
			if (cacheSet.isLocked()) {
				log.info("Lock Detected.Waiting");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return getDataValue(cacheSetId, keys);
			}

			CacheKey ckeySet = new CacheKey();
			for (String key : keys) {
				ckeySet.getKeys().add(key);
			}
			value = cacheSet.getDataMap().get(ckeySet);
		}
		return value;
	}

	private void initAll() {
		int setCount = InMemoryProperties.getInstance().getIntPropertyValue("InMemory.TotSetIdCount");
		// Load for each dataset Configuration
		for (int i = 0; i < setCount; i++) {
			this.initDataSet(i + 1);
		}

	}

	private void initDataSet(int setIdVal) {
		try {
			String setIdPropVal = "InMemory.SetId_" + String.valueOf(setIdVal);
			String setId = InMemoryProperties.getInstance().getPropertyValue(setIdPropVal);
			if (setId != null) {
				log.info(new StringBuffer("set-").append(setId));

				// Create CacheSet
				CacheSet cacheset = new CacheSet();
				cacheset.setCacheId(setId);

				// Load Maintainer Class
				String maintainerClassName = InMemoryProperties.getInstance()
						.getPropertyValue("InMemory." + setId + ".maintainer");
				Maintainer maintainer = null;
				try {
					maintainer = (Maintainer) Maintainer.class.getClassLoader().loadClass(maintainerClassName)
							.newInstance();
				} catch (InstantiationException e) {
					log.fatal("Error instanciate " + maintainerClassName, e);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					log.fatal("Error instanciate " + maintainerClassName, e);
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					log.fatal("Error instanciate " + maintainerClassName, e);
					e.printStackTrace();
				}
				cacheset.setMaintainer(maintainer);

				// Load Data Loader Class
				String dataLoaderClassName = InMemoryProperties.getInstance()
						.getPropertyValue("InMemory." + setId + ".dataloader");
				DataLoader dataLoader = null;
				try {
					dataLoader = (DataLoader) Maintainer.class.getClassLoader().loadClass(dataLoaderClassName)
							.newInstance();
				} catch (InstantiationException e) {
					log.fatal("Error instanciate " + dataLoaderClassName, e);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					log.fatal("Error instanciate " + dataLoaderClassName, e);
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					log.fatal("Error instanciate " + dataLoaderClassName, e);
					e.printStackTrace();
				}
				cacheset.setDataLoader(dataLoader);

				InMemory.memoryMap.put(setId, cacheset);

				// Start Maintaining
				maintainer.doMaintain(cacheset);
			}
		} catch (Exception e) {
			log.fatal("Error while initializing", e);
		}
	}

	/**
	 * print the memory value of a set
	 * 
	 * @param cacheSetId
	 *            Cache Set Id
	 */
	public void printMemory(String cacheSetId) {
		CacheSet cacheSet = this.findCacheSetByID(cacheSetId);
		if (cacheSet != null) {
			log.info("PRINT MEMORY-" + cacheSet.getCacheId());
			Iterator<Entry<CacheKey, String>> it = cacheSet.getDataMap().entrySet().iterator();
			while (it.hasNext()) {
				String record = "";
				CacheKey cacheKey = it.next().getKey();
				for (String key : cacheKey.getKeys()) {
					record += key + "\t";
				}
				record += "\t-\t" + cacheSet.getDataMap().get(cacheKey);
				log.info(record);
			}
			log.info("END PRINT MEMORY-" + cacheSet.getCacheId());
		} else {
			log.info("CACHE SET NOT FOUND-" + cacheSetId);
		}
	}

	/**
	 * Reset and read data again
	 * 
	 * @param cacheSetId
	 *            set id
	 */
	public void ResetDataSet(String cacheSetId) {
		log.info(new StringBuffer("ResetDataSet:\t").append(cacheSetId));

		CacheSet cacheSet = this.findCacheSetByID(cacheSetId);
		if (cacheSet != null) {
			cacheSet.reloadData();
		}

	}

	/**
	 * Dump Data .For troubleshooting purposes only
	 */
	public void dumpData() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		File file = new File("DUMP_" + sdf.format(date) + ".dat");
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();

		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			out.println("*************************************************************************");
			out.println("DATA DUMP " + sdf.format(date));
			out.println("\tTOTAL MEMORY\t:"+format.format(runtime.totalMemory()/1024));
			out.println("\tMAX MEMORY\t:"+format.format(runtime.maxMemory()/1024));
			out.println("\tFREE MEMORY\t:"+format.format(runtime.freeMemory()/1024));
			out.println("*************************************************************************");
			Iterator it = memoryMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				CacheSet cacheSet = (CacheSet) pair.getValue();
				out.println("------------------------------------------------------------------------------");
				out.println("\tCACHE SET\t:" + cacheSet.getCacheId());
				
				Iterator<Entry<CacheKey, String>> it1 = cacheSet.getDataMap().entrySet().iterator();
				while (it1.hasNext()) {
					String record = "\t\t";
					CacheKey cacheKey = it1.next().getKey();
					for (String key : cacheKey.getKeys()) {
						record += key + "\t";
					}
					record += "\t-\t" + cacheSet.getDataMap().get(cacheKey);
					out.println(record);
				}
				
				out.println("------------------------------------------------------------------------------");
				out.flush();
			}
		} catch (IOException e) {
			log.fatal("Error while wring dump file");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

}
