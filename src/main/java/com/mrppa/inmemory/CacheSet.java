package com.mrppa.inmemory;

import java.util.HashMap;

import com.mrppa.inmemory.dataloader.DataLoader;
import com.mrppa.inmemory.maintainer.Maintainer;
import com.mrppa.inmemory.CacheKey;

public class CacheSet {
	private String cacheId;
	private DataLoader dataLoader;
	private boolean locked = true;
	private Maintainer maintainer;
	private HashMap<CacheKey, String> dataMap = new HashMap<CacheKey, String>();

	public String getCacheId() {
		return cacheId;
	}

	public DataLoader getDataLoader() {
		return dataLoader;
	}

	public HashMap<CacheKey, String> getDataMap() {
		return dataMap;
	}

	public Maintainer getMaintainer() {
		return maintainer;
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

	public void reloadData() {
		this.lockCacheSet();
		this.getDataMap().clear();
		this.dataLoader.doReadData(this);
		this.releaseLockCacheSet();
	}

	protected void setCacheId(String casheId) {
		this.cacheId = casheId;
	}

	protected void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	protected void setDataMap(HashMap<CacheKey, String> dataMap) {
		this.dataMap = dataMap;
	}

	protected void setMaintainer(Maintainer maintainer) {
		this.maintainer = maintainer;
	}

}
