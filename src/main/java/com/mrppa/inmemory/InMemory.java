package com.mrppa.inmemory;

import java.io.IOException;

import org.apache.log4j.Logger;

public class InMemory {

	private static InMemory inMemoryInstance = null;
	private static Logger log = Logger.getLogger(InMemory.class.getName());
	private DataMemory dataMemory;

	private InMemory() {
	}

	/**
	 * To Get the single instance Of InMemeory
	 * 
	 * @return single instance Of InMemory Object
	 * @throws IOException
	 */
	public static InMemory getInstance() throws IOException {
		if (inMemoryInstance == null) {
			synchronized (InMemory.class) {
				log.info("InMemory New Instance");
				inMemoryInstance = new InMemory();
				inMemoryInstance.dataMemory = DataMemory.getInstance();
				inMemoryInstance.loadData();
			}
		}
		return inMemoryInstance;
	}

	/**
	 * To get the memory value of given keys
	 * 
	 * @param setId
	 * @param key
	 * @return
	 * @throws DataNotReady 
	 */
	public String getDataValue(String setId, String... keys)  {
		if(this.dataMemory.isCacheSetLocked(setId))
		{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return getDataValue(setId, keys);
		}
		
		CacheKey ckeySet = new CacheKey();
		for (String key : keys) {
			ckeySet.getKeys().add(key);
		}
		String value = this.dataMemory.getEntity(setId, ckeySet);
		return value;
	}

	public void printMemory(String cacheSetId) {
		this.dataMemory.printMemory(cacheSetId);
	}

	private void loadData() throws IOException {
		int setCount = InMemoryProperties.getInstance().getIntPropertyValue("InMemory.TotSetIdCount");
		for (int i = 0; i < setCount; i++) {
			try {
				String setIdPropVal = "InMemory.SetId_" + String.valueOf(i + 1);
				String setId = InMemoryProperties.getInstance().getPropertyValue(setIdPropVal);
				if (setId != null) {
					log.info(new StringBuffer("set-").append(setId));
					CacheSet cacheset = new CacheSet();
					cacheset.setLocked(true);
					cacheset.setCasheId(setId);
					String maintainerClassName = InMemoryProperties.getInstance().getPropertyValue("InMemory." + setId + ".maintainer");
					Maintainer maintainer = null;
					try {
						maintainer = (Maintainer) Maintainer.class.getClassLoader().loadClass(maintainerClassName).newInstance();
					} catch (InstantiationException e) {
						log.fatal("Error instanciate " + maintainerClassName, e);
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						log.fatal("Error instanciate " + maintainerClassName, e);
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						log.fatal("Error instanciate " + maintainerClassName, e);
						e.printStackTrace();
					}

					String dataLoaderClassName = InMemoryProperties.getInstance().getPropertyValue("InMemory." + setId + ".dataloader");
					DataLoader dataLoader = null;
					try {
						dataLoader = (DataLoader) Maintainer.class.getClassLoader().loadClass(dataLoaderClassName).newInstance();
					} catch (InstantiationException e) {
						log.fatal("Error instanciate " + dataLoaderClassName, e);
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						log.fatal("Error instanciate " + dataLoaderClassName, e);
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						log.fatal("Error instanciate " + dataLoaderClassName, e);
						e.printStackTrace();
					}
					dataLoader.setCacheSetId(setId);
					cacheset.setDataLoader(dataLoader);

					maintainer.setCacheSetId(setId);
					maintainer.setDataLoader(dataLoader);
					cacheset.setMaintainer(maintainer);

					this.dataMemory.addCacheSet(cacheset);

					maintainer.doMaintain();
					cacheset.setLocked(false);

				}
			} catch (Exception e) {
				log.fatal("Error while initializing", e);
			}
		}

	}

}
