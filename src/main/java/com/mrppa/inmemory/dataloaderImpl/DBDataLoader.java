package com.mrppa.inmemory.dataloaderImpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.mrppa.inmemory.CacheKey;
import com.mrppa.inmemory.CacheSet;
import com.mrppa.inmemory.InMemoryProperties;
import com.mrppa.inmemory.dataloader.DataLoader;
import com.mrppa.inmemory.maintainer.Maintainer;

/**
 * Load Data from given database
 * Properties
 *   InMemory.<Cache ID>.dataloader.nu_of_keys=<NUMBER OF KEYS IN ORDER>
 *   InMemory.<Cache ID>.dataloader.sql=<SQL SCRIPT ex-SELECT key1,key2,....,value from table1>
 *   InMemory.<Cache ID>.dataloader.hibernate.connection.driver_class=<JDBC DRIVER>
 *   InMemory.<Cache ID>.dataloader.hibernate.connection.url=<DB CONN URL>
 *   InMemory.<Cache ID>.dataloader.hibernate.connection.username=<DB USERID>
 *   InMemory.<Cache ID>.dataloader.hibernate.connection.password=<DB Password>
 * @author Pasindu Ariyarathna
 *
 */
public class DBDataLoader implements DataLoader {
	
	private static Logger log = Logger.getLogger(Maintainer.class.getName());

	public void doReadData(CacheSet cacheSet) {
		log.debug("MTD-doReadData()");
		int NuOfKeys=InMemoryProperties.getInstance().getIntPropertyValue("InMemory."+cacheSet.getCacheId()+".dataloader.nu_of_keys");
		
		String sql=InMemoryProperties.getInstance().getPropertyValue("InMemory."+cacheSet.getCacheId()+".dataloader.sql");
		
		
		List<String[]> hybernateList=InMemoryProperties.getInstance().getPropList("InMemory."+cacheSet.getCacheId()+".dataloader.hibernate");
		
		Configuration cfg = new Configuration();
	
		for(String[] propArr:hybernateList)
		{
			cfg.setProperty(propArr[0].replace("InMemory."+cacheSet.getCacheId()+".dataloader.hibernate", "hibernate"), propArr[1]);
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
			cacheSet.getDataMap().put(keys, value);
		}
		
	}

}
