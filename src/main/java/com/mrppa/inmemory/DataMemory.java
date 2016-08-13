package com.mrppa.inmemory;

import java.util.HashMap;

/**
 * Hold Memory Data
 * @author Pasindu
 *
 */
public class DataMemory {
	
	private static DataMemory dataMemory=null;
	
	private HashMap<String,CacheSet> memoryMap=null;
	
	private DataMemory(){};
	
	
	protected static DataMemory getInstance() 
	{
		if(dataMemory==null)
		{
			synchronized (DataMemory.class) {
				dataMemory=new DataMemory();
				dataMemory.memoryMap=new HashMap<String,CacheSet>(); 
			}
		}
		return dataMemory;
	}
	
	
	protected boolean isCacheSetLocked(String cacheSetId)
	{
			return memoryMap.get(cacheSetId).isLocked();
	}
	
	protected void setCacheSetLocked(String cacheSetId,boolean locked)
	{
			memoryMap.get(cacheSetId).setLocked(locked);
	}
	
	protected void addCacheSet(CacheSet cacheSet)
	{
		synchronized(memoryMap)
		{
			memoryMap.put(cacheSet.getCasheId(), cacheSet);
		}
	}
	
	protected void clearCacheSetData(String cacheSetId)
	{
		synchronized(memoryMap)
		{
			memoryMap.get(cacheSetId).getKeyList().clear();
			memoryMap.get(cacheSetId).getValueList().clear();
		}
	}
	
	protected void addEntity(String cacheSetId,CacheKey cacheKey,String value)
	{
		synchronized(memoryMap)
		{
			this.removeEntity(cacheSetId, cacheKey);
			this.memoryMap.get(cacheSetId).getKeyList().add(cacheKey);
			this.memoryMap.get(cacheSetId).getValueList().add(value);
		}
	}
	
	protected void removeEntity(String cacheSetId,CacheKey cacheKey)
	{
		synchronized(memoryMap)
		{
			int pos=-1;
			for(int i=0;i<this.memoryMap.get(cacheSetId).getKeyList().size();i++)
			{
				if(this.memoryMap.get(cacheSetId).getKeyList().get(i).equals(cacheKey))
				{
					pos=i;
				}
			}
			
			try
			{
				this.memoryMap.get(cacheSetId).getKeyList().remove(pos);
				this.memoryMap.get(cacheSetId).getValueList().remove(pos);
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				
			}
		}
	}
	
	protected String getEntity(String cacheSetId,CacheKey cacheKey)
	{
		int pos=-1;
		for(int i=0;i<this.memoryMap.get(cacheSetId).getKeyList().size();i++)
		{
			if(this.memoryMap.get(cacheSetId).getKeyList().get(i).equals(cacheKey))
			{
				pos=i;
			}
		}
		
		String value=null;
		try
		{
			value=this.memoryMap.get(cacheSetId).getValueList().get(pos);
		}
		catch(ArrayIndexOutOfBoundsException ex1)
		{
			value=null;
		}
		return value;
	}

	protected void printMemory(String cacheSetId)
	{
		
		System.out.println("PRINT MEMORY-"+cacheSetId);
		for(int i=0;i<this.memoryMap.get(cacheSetId).getKeyList().size();i++)
		{
			for(int j=0;j<this.memoryMap.get(cacheSetId).getKeyList().get(i).getKeys().size();j++)
			{
				System.out.print(this.memoryMap.get(cacheSetId).getKeyList().get(i).getKeys().get(j)+"\t");
			}
			System.out.println(this.memoryMap.get(cacheSetId).getValueList().get(i));
		}
		System.out.println("END PRINT MEMORY-"+cacheSetId);
	}
	
}
