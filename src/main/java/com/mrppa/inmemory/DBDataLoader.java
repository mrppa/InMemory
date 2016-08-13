package com.mrppa.inmemory;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBDataLoader implements DataLoader {
	
	private static Logger log = Logger.getLogger(Maintainer.class.getName());
	private String cacheSetId=null;


	public void doReadData() {
		log.debug("MTD-doReadData()");
		int NuOfKeys=InMemoryProperties.getInstance().getIntPropertyValue("InMemory."+cacheSetId+".dataloader.nu_of_keys");
		
		String sql=InMemoryProperties.getInstance().getPropertyValue("InMemory."+cacheSetId+".dataloader.sql");
		
		
		List<String[]> hybernateList=InMemoryProperties.getInstance().getPropList("InMemory."+cacheSetId+".dataloader.hibernate");
		
		Configuration cfg = new Configuration();
	
		for(String[] propArr:hybernateList)
		{
			cfg.setProperty(propArr[0].replace("InMemory."+cacheSetId+".dataloader.hibernate", "hibernate"), propArr[1]);
		}
		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
		SessionFactory sessions = cfg.buildSessionFactory(serviceRegistry);
		Session session = sessions.openSession();		
		@SuppressWarnings("unchecked")
		List<Object[]> resultList=(List<Object[]>)session.createSQLQuery(sql).list();
		for(Object[] res:resultList)
		{
			CacheKey keys=new CacheKey();
			for(int i=0;i<NuOfKeys;i++)
			{
				String key=(String)res[i];
				if(key==null)
				{
					key="";
				}
				keys.getKeys().add(key);
			}
			String value=(String)res[NuOfKeys];
			DataMemory.getInstance().addEntity(this.cacheSetId, keys, value);
		}
		
	}

	public void setCacheSetId(String cacheSetId) {
		this.cacheSetId=cacheSetId;
	}




}
