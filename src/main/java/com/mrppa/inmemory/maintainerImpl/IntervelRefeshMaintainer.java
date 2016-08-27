package com.mrppa.inmemory.maintainerImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.apache.log4j.Logger;

import com.mrppa.inmemory.CacheSet;
import com.mrppa.inmemory.InMemoryProperties;
import com.mrppa.inmemory.maintainer.Maintainer;

/**
 * Periodically Refresh Data Properties InMemory.
 * 
 * <pre>
 * [Cache ID].maintainer.delay=[Delay in Milli seconds]
 * </pre>
 * 
 * @author Pasindu
 *
 */
public class IntervelRefeshMaintainer implements Maintainer {

	private static Logger log = Logger.getLogger(IntervelRefeshMaintainer.class.getName());
	private boolean alreadyRun = false;
	private CacheSet cachesetObj;
	protected Timer timer = null;

	public void doMaintain(CacheSet cacheset) {
		cachesetObj = cacheset;
		log.debug("MTD-doMaintain()");
		if (alreadyRun == false) {
			cacheset.reloadData();
		}
		alreadyRun = true;
		int delay = InMemoryProperties.getInstance()
				.getIntPropertyValue("InMemory." + cacheset.getCacheId() + ".maintainer.delay"); // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				log.info("Maintain task started");
				cachesetObj.reloadData();
				log.info("Maintain task Ended");
			}
		};
		timer=new Timer(delay, taskPerformer);
		timer.start();
	}
}
