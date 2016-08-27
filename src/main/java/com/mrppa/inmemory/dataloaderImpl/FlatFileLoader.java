package com.mrppa.inmemory.dataloaderImpl;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.log4j.Logger;

import com.mrppa.inmemory.CacheKey;
import com.mrppa.inmemory.CacheSet;
import com.mrppa.inmemory.InMemoryProperties;
import com.mrppa.inmemory.dataloader.DataLoader;

/**
 * Load Data from given Flat File. Properties
 * 
 * <pre>
 *   InMemory.[Cache ID].dataloader.filename=[FLAT FILE PATH]
 *   InMemory.[Cache ID].dataloader.rec_seperator=[Flat file line separator]
 *   InMemory.[Cache ID].dataloader.key_records=[comma separated key  ranges] ex-=1|4,5|7 means 1 to 4 and 5 to 7
 *   InMemory.[Cache ID].dataloader.val_record=[comma separated value ranges]
 *   InMemory.[Cache ID].dataloader.first_line=[Line number of the first valid record from file]
 * </pre>
 * 
 * @author Pasindu Ariyarathna
 *
 */
public class FlatFileLoader implements DataLoader {

	private static Logger log = Logger.getLogger(FlatFileLoader.class.getName());

	public void doReadData(CacheSet cacheSet) {
		log.debug("MTD-doReadData()");

		String fileName = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.filename");

		String recSeperator = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.rec_seperator");

		String[] keyColumnIndArr = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.key_records").split(",");

		String valColumn = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.val_record");

		int firstLineNumber = InMemoryProperties.getInstance()
				.getIntPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.first_line");

		try {
			File file = new File(fileName);
			int lineNo = 1;
			FileInputStream fstream = new FileInputStream(file);

			Reader reader = new InputStreamReader(fstream);
			StringBuilder line = new StringBuilder();

			int intChar;
			while ((intChar = reader.read()) != -1) {
				char fetchedCharacter = (char) intChar;
				line.append(fetchedCharacter);
				if (recSeperator.charAt(0)==fetchedCharacter) {// Line End found
					log.trace("LINE\t:"+line.toString());
					if (lineNo >= firstLineNumber) {
						CacheKey cacheKey = new CacheKey();
						for (String keyInd : keyColumnIndArr) {
							String[] colLenDef = keyInd.split("\\|");
							String keyVal = line.toString().substring(Integer.parseInt(colLenDef[0]) - 1,
									Integer.parseInt(colLenDef[1]));
							cacheKey.getKeys().add(keyVal);
						}
						String[] dataLenDef = valColumn.split("\\|");
						String dataVal = line.toString().substring(Integer.parseInt(dataLenDef[0]) - 1,
								Integer.parseInt(dataLenDef[1]));
						cacheSet.getDataMap().put(cacheKey, dataVal);
					}
					line = new StringBuilder();
					lineNo++;
				}

			}

			reader.close();

		} catch (FileNotFoundException e) {
			log.fatal("File reading error", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.fatal("File reading error", e);
			e.printStackTrace();
		}
	}

}
