package com.mrppa.inmemory.dataloaderImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;

import com.mrppa.inmemory.CacheKey;
import com.mrppa.inmemory.CacheSet;
import com.mrppa.inmemory.InMemoryProperties;
import com.mrppa.inmemory.dataloader.DataLoader;

public class CSVLoader implements DataLoader {

	private static Logger log = Logger.getLogger(CSVLoader.class.getName());

	public void doReadData(CacheSet cacheSet) {
		log.debug("MTD-doReadData()");

		String fileName = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.filename");

		String delimiter = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.delimiter");

		String[] keyColumnIndArr = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.key_columns").split(",");

		String valColumn = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.val_column");

		int firstLineNumber = InMemoryProperties.getInstance()
				.getIntPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.first_line");

		try {
			File file = new File(fileName);
			int lineNo = 1;
			FileInputStream fstream = new FileInputStream(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				log.trace(strLine);
				if (lineNo >= firstLineNumber) {
					try {
						String[] colArr = strLine.split(delimiter);
						CacheKey cacheKey = new CacheKey();
						for (String keyInd : keyColumnIndArr) {
							String keyVal = colArr[Integer.parseInt(keyInd)-1];
							cacheKey.getKeys().add(keyVal);
						}
						String dataVal = colArr[Integer.parseInt(valColumn)-1];
						cacheSet.getDataMap().put(cacheKey, dataVal);
					} catch (Exception e) {
						log.error("Data reading error", e);
					}

				}
				lineNo++;
			}

			br.close();

		} catch (FileNotFoundException e) {
			log.fatal("File reading error", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.fatal("File reading error", e);
			e.printStackTrace();
		}
	}

}
