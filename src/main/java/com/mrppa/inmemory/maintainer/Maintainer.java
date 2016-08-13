package com.mrppa.inmemory.maintainer;

import com.mrppa.inmemory.CacheSet;
import com.mrppa.inmemory.dataloader.DataLoader;

public interface Maintainer {
	
	public void doMaintain(CacheSet cacheSet);	
}
