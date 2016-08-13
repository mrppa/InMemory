package com.mrppa.inmemory;

import java.util.ArrayList;

public class CacheKey {
	private ArrayList<String> keys ;

	public CacheKey()
	{
		keys=new ArrayList<String>();
	}
	public ArrayList<String> getKeys() {
		return keys;
	}

	protected void setKeys(ArrayList<String> keys) {
		this.keys = keys;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == this)
	    {
	        return true;
	    }
	    if (obj == null)
	    {
	        return false;
	    }
	    if (obj instanceof CacheKey)
	    {
	    	CacheKey OtherObj=(CacheKey)obj;
	    	boolean dataEquals=true;
	    	for(int i=0;i<keys.size();i++)
	    	{
	    		if(!(keys.get(i)).equals(OtherObj.getKeys().get(i)))
	    		{
	    			dataEquals=false;
	    		}
	    	}
	        return dataEquals;
	    }
	    else
	    {
	    	return false;
	    }
		
	}
	
}
