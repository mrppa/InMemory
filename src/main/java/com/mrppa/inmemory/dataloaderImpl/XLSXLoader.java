package com.mrppa.inmemory.dataloaderImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mrppa.inmemory.CacheKey;
import com.mrppa.inmemory.CacheSet;
import com.mrppa.inmemory.InMemoryProperties;
import com.mrppa.inmemory.dataloader.DataLoader;

/**
 * Load Data from given XLSX File .
 * Properties
 * 
 * <pre>
 *   InMemory.[Cache ID].dataloader.filename=[XLSX FILE PATH]
 *   InMemory.[Cache ID].dataloader.key_columns=[Key Column numbers in comma separated manner]
 *   InMemory.[Cache ID].dataloader.val_column=[Column Value number]
 *   InMemory.[Cache ID].dataloader.first_line=[Line number of the first column of dataset]
 * </pre>
 * 
 * @author Pasindu Ariyarathna
 *
 */
public class XLSXLoader implements DataLoader {

	private static Logger log = Logger.getLogger(XLSXLoader.class.getName());

	public void doReadData(CacheSet cacheSet) {
		log.debug("MTD-doReadData()");

		String fileName = InMemoryProperties.getInstance()
				.getPropertyValue("InMemory." + cacheSet.getCacheId() + ".dataloader.filename");

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
			Workbook myWorkBook = new XSSFWorkbook(fstream);
			Sheet mySheet = myWorkBook.getSheetAt(0);

			int recordCount = mySheet.getLastRowNum() + 1;

			for (lineNo = 1; lineNo <= recordCount; lineNo++) {
				Row row = mySheet.getRow(lineNo - 1);
				if (lineNo >= firstLineNumber) {
					try {
						CacheKey cacheKey = new CacheKey();
						for (String keyInd : keyColumnIndArr) {
							String keyVal = this.getStringCellVal(row, Integer.parseInt(keyInd) - 1);
							cacheKey.getKeys().add(keyVal);
						}
						String dataVal = this.getStringCellVal(row, Integer.parseInt(valColumn) - 1);
						cacheSet.getDataMap().put(cacheKey, dataVal);
					} catch (Exception e) {
						log.error("Data reading error", e);
					}

				}
			}

			myWorkBook.close();

		} catch (FileNotFoundException e) {
			log.fatal("File reading error", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.fatal("File reading error", e);
			e.printStackTrace();
		}
	}

	private String getStringCellVal(Row row, int index) {
		String val = "";
		Cell cell = row.getCell(index);
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				val = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				val = Boolean.toString(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				DecimalFormat decimalFormat=new DecimalFormat("#.#");
				val = decimalFormat.format(cell.getNumericCellValue());
				break;
			}
		}
		return val;
	}

}
