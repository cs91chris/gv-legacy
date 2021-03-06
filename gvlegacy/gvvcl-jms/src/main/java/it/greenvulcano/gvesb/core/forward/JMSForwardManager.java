/*
 * Copyright (c) 2009-2012 GreenVulcano ESB Open Source Project.
 * All rights reserved.
 * 
 * This file is part of GreenVulcano ESB.
 * 
 * GreenVulcano ESB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GreenVulcano ESB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with GreenVulcano ESB. If not, see <http://www.gnu.org/licenses/>.
 */
package it.greenvulcano.gvesb.core.forward;

import it.greenvulcano.configuration.XMLConfig;
import it.greenvulcano.gvesb.core.forward.jms.JMSForwardData;
import it.greenvulcano.gvesb.core.forward.jms.JMSForwardListenerPool;
import it.greenvulcano.gvesb.core.forward.preprocess.ValidatorManager;
import it.greenvulcano.log.NMDC;
import it.greenvulcano.util.xpath.XPathFinder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @version 4.0.0 15/mar/2018
 * @author GreenVulcano Developer Team
 */
public class JMSForwardManager {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JMSForwardManager.class);
    
    /**
     * the object JMX descriptor
     */
    public static final String  DESCRIPTOR_NAME = "ServiceOperationInfoManager";
    
    /**
     * JMS Forward configuration file name.
     */
    public static String  JMS_FORWARD_FILE_NAME = "GVJMSForward.xml";
   
    private final static ConcurrentMap<String, JMSForwardListenerPool> jmsListeners = new ConcurrentHashMap<>();
   
    private final static AtomicBoolean running = new AtomicBoolean(false);

    private JMSForwardManager() {        
    }
   
    /**
     * @throws Exception 
    *
    */
    static void init() {
    	
    	if (running.compareAndSet(false, true)) {
    		logger.debug("Initializing JMSForwardManager");
	        NMDC.push();
	        NMDC.clear();
	       
	        NMDC.setSubSystem(JMSForwardData.SUBSYSTEM);
	        try {
	            NodeList nl = XMLConfig.getNodeList(JMS_FORWARD_FILE_NAME,
	                    "/GVForwards/ForwardConfiguration[@enabled='true']");
	            for (int i = 0; i < nl.getLength(); i++) {
	                Node n = nl.item(i);
	                JMSForwardListenerPool jmsLP = new JMSForwardListenerPool();
	                jmsLP.init(n);
	                jmsListeners.putIfAbsent( XPathFinder.buildXPath(n) ,jmsLP);
	                logger.debug("Configured JMSForwardListenerPool[" + jmsLP.getName() + "/" + jmsLP.getForwardName() + "]");
	               
	            }
	        }  catch (Exception exc) {
	            logger.error("Error initializing JMSForwardManager", exc);
	            
	        } finally {
	            NMDC.pop();
	        }
    	} else {
    		logger.debug("JMSForwardManager already inizialied");
    	}
    }

    static void destroy() {
    	if (running.compareAndSet(true, false)) {
	        NMDC.push();
	        NMDC.clear();
	        
	        NMDC.setSubSystem(JMSForwardData.SUBSYSTEM);
	        try {
	            logger.debug("BEGIN - Destroing JMSForwardManager");
	            ValidatorManager.instance().reset();
	            for (JMSForwardListenerPool pool : jmsListeners.values()) {
	                String forwardName = pool.getName() + "/" + pool.getForwardName();
	                try {
	                  
	                    pool.destroy();
	                }
	                catch (Exception exc) {
	                    logger.error("Error destroing JMSForwardListenerPool[" + forwardName + "]", exc);
	                }
	            }
	            jmsListeners.clear();
	            logger.debug("END - Destroing JMSForwardManager");
	    	}
	        finally {
	            NMDC.pop();
	        }
    	}
    }
 
}
