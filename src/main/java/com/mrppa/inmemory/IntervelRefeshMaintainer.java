package com.mrppa.inmemory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.apache.log4j.Logger;

public class IntervelRefeshMaintainer implements Maintainer {

	private boolean alreadyRun = false;
	private String cacheSetId = null;
	private DataLoader dataLoader = null;
	private static Logger log = Logger.getLogger(Maintainer.class.getName());

	public void doMaintain() {
		log.debug("MTD-doMaintain()");
		if (alreadyRun == false) {
			dataLoader.doReadData();
		}
		alreadyRun = true;
		int delay = InMemoryProperties.getInstance().getIntPropertyValue("InMemory."+cacheSetId+".maintainer.delay"); // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				DataMemory.getInstance().setCacheSetLocked(cacheSetId, true);
				DataMemory.getInstance().clearCacheSetData(cacheSetId);
				dataLoader.doReadData();
				DataMemory.getInstance().setCacheSetLocked(cacheSetId, false);
			}
		};
		new Timer(delay, taskPerformer).start();
	}

	public void setCacheSetId(String cacheSetId) {
		this.cacheSetId = cacheSetId;
	}

	public void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

}
