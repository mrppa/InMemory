package com.mrppa.inmemory.maintainerImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.apache.log4j.Logger;

import com.mrppa.inmemory.CacheSet;
import com.mrppa.inmemory.InMemoryProperties;
import com.mrppa.inmemory.maintainer.Maintainer;

/**
 * Periodically Refresh Data
 * Properties
 *   InMemory.<Cache ID>.maintainer.delay=<Delay in Milli seconds>
 * @author Pasindu
 *
 */
public class IntervelRefeshMaintainer implements Maintainer {

	private boolean alreadyRun = false;
	private static Logger log = Logger.getLogger(IntervelRefeshMaintainer.class.getName());
	private CacheSet cachesetObj;

	public void doMaintain(CacheSet cacheset) {
		cachesetObj=cacheset;
		log.debug("MTD-doMaintain()");
		if (alreadyRun == false) {
			cacheset.reloadData();
		}
		alreadyRun = true;
		int delay = InMemoryProperties.getInstance().getIntPropertyValue("InMemory."+cacheset.getCacheId()+".maintainer.delay"); // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				log.info("Maintain task started");
				cachesetObj.reloadData();
				log.info("Maintain task Ended");
			}
		};
		new Timer(delay, taskPerformer).start();
	}
}
