package com.mrppa.inmemory.maintainerImpl;

import org.apache.log4j.Logger;

import com.mrppa.inmemory.CacheSet;
import com.mrppa.inmemory.maintainer.Maintainer;

/**
 * Load Data One time only Special Properties NA
 * 
 * @author Pasindu Ariyarathna
 *
 */
public class OneTimeMaintainer implements Maintainer {

	private static Logger log = Logger.getLogger(OneTimeMaintainer.class.getName());
	private boolean alreadyRun = false;

	public void doMaintain(CacheSet cacheSet) {
		log.debug("MTD-doMaintain()");
		if (alreadyRun == false) {
			cacheSet.reloadData();
		}
		alreadyRun = true;
	}

}
