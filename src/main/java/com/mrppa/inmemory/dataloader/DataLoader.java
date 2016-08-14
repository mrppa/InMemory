package com.mrppa.inmemory.dataloader;

import com.mrppa.inmemory.CacheSet;

/**
 * This Class hold the interface to the Data loading implementation
 * @author Pasindu Ariyarathna
 *
 */
public interface DataLoader {

	/**
	 * Read And Store data to the CacheSet Object
	 * @param cacheSet CacheSet object
	 */
	public void doReadData(CacheSet cacheSet);

}
