package com.mrppa.inmemory;

import java.util.ArrayList;

public class CacheSet {
	private String casheId;
	private Maintainer maintainer;
	private DataLoader dataLoader;
	private boolean locked=true;
	private ArrayList<CacheKey> keyList=new ArrayList<CacheKey>();
	private ArrayList<String> valueList=new ArrayList<String>();


	protected String getCasheId() {
		return casheId;
	}

	protected void setCasheId(String casheId) {
		this.casheId = casheId;
	}

	protected Maintainer getMaintainer() {
		return maintainer;
	}

	protected void setMaintainer(Maintainer maintainer) {
		this.maintainer = maintainer;
	}

	protected boolean isLocked() {
		return locked;
	}

	protected synchronized void  setLocked(boolean locked) {
		this.locked = locked;
	}

	protected DataLoader getDataLoader() {
		return dataLoader;
	}

	protected void setDataLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	protected ArrayList<CacheKey> getKeyList() {
		return keyList;
	}

	protected void setKeyList(ArrayList<CacheKey> keyList) {
		this.keyList = keyList;
	}

	protected ArrayList<String> getValueList() {
		return valueList;
	}

	protected void setValueList(ArrayList<String> valueList) {
		this.valueList = valueList;
	}
	
	

}
