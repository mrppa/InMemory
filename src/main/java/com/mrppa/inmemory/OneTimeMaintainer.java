package com.mrppa.inmemory;

import org.apache.log4j.Logger;

public class OneTimeMaintainer implements Maintainer {
	
	private boolean alreadyRun=false;
	private String cacheSetId=null;
	private DataLoader dataLoader=null;
	private static Logger log = Logger.getLogger(Maintainer.class.getName());


	public void doMaintain() {
		log.debug("MTD-doMaintain()");
		if(alreadyRun==false)
		{
			dataLoader.doReadData();
		}
		alreadyRun=true;
	}

	public void setCacheSetId(String cacheSetId) {
		this.cacheSetId=cacheSetId;
	}

	public void setDataLoader(DataLoader dataLoader) {
		this.dataLoader=dataLoader;		
	}

}
