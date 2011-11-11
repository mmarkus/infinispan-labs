package org.infinispan.labs.lab1.service;

import org.infinispan.cdi.ConfigureCache;
import org.infinispan.cdi.OverrideDefault;
import org.infinispan.config.Configuration;
import org.infinispan.config.Configuration.CacheMode;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.labs.lab1.transactions.JBoss7TransactionManagerLookup;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;
/**
 * Cache definitions
 *
 * @author Kevin Pollet <pollet.kevin@gmail.com> (C) 2011
 */
public class Resources {

   /**
    * <p>This producer defines the ticket allocation cache configuration.</p>
    */
   @TicketAllocationCache
   @ConfigureCache("ticket-allocation-cache")
   @Produces
   public Configuration configureCache() {
      return new Configuration().fluent()
         .mode(CacheMode.DIST_SYNC)
         .l1()
         .transaction().transactionManagerLookup(new JBoss7TransactionManagerLookup())
         .jmxStatistics()
         .build();
   }
   
   @Produces @OverrideDefault
   public EmbeddedCacheManager configureCacheManager() {
      return new DefaultCacheManager(
            GlobalConfiguration
               .getClusteredDefault().fluent()
                  .transport()
                     .addProperty("configurationFile", "jgroups.xml")
                  .build());
   }
   
   @Produces
   public Logger getLogger(InjectionPoint ip) {
      return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
   }
   
}