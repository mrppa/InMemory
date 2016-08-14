package com.mrppa.inmemory.maintainer;

import com.mrppa.inmemory.CacheSet;

/**
 * This Class hold the interface to the maintainng implementations
 * @author Pasindu Ariyarathna
 *
 */
public interface Maintainer {

	/**
	 * Startup maintain logic for given CacheSet
	 * @param cacheSet CacheSet Object
	 */
	public void doMaintain(CacheSet cacheSet);
}
