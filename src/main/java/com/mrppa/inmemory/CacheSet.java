package com.mrppa.inmemory;

import java.util.HashMap;

import com.mrppa.inmemory.dataloader.DataLoader;
import com.mrppa.inmemory.maintainer.Maintainer;

public class CacheSet {
	private String cacheId;
	private DataLoader dataLoader;
	private boolean locked = true;
	private Maintainer maintainer;
	private HashMap<CacheKey, String> dataMap = new HashMap<CacheKey, String>();

	public String getCacheId() {
		return cacheId;
	}

	protected void setCacheId(String casheId) {
		this.cacheId = casheId;
	}

	public boolean isLocked() {
		return locked;
	}

	protected synchronized void lockCacheSet() {
		this.locked = true;
	}

	protected synchronized void releaseLockCacheSet() {
		this.locked = false;
	}

	public DataLoader getDataLoader() {
		return dataLoader;
	}

	protected void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	public Maintainer getMaintainer() {
		return maintainer;
	}

	protected void setMaintainer(Maintainer maintainer) {
		this.maintainer = maintainer;
	}

	public HashMap<CacheKey, String> getDataMap() {
		return dataMap;
	}

	protected void setDataMap(HashMap<CacheKey, String> dataMap) {
		this.dataMap = dataMap;
	}

	public void reloadData() {
		this.lockCacheSet();
		this.getDataMap().clear();
		this.dataLoader.doReadData(this);
		this.releaseLockCacheSet();
	}

}
