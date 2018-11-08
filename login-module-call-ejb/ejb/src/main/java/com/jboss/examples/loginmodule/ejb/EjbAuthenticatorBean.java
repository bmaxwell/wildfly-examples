/**
 * 
 */
package com.jboss.examples.loginmodule.ejb;

import java.security.Principal;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.jboss.logging.Logger;
import org.jboss.security.SimplePrincipal;

/**
 * @author bmaxwell
 *
 */
public class EjbAuthenticatorBean implements SessionBean, EjbAuthenticator {

	private Logger log = Logger.getLogger(EjbAuthenticatorBean.class.getName());
	private SessionContext ctx;
	
	/**
	 * 
	 */
	public EjbAuthenticatorBean() {
	}

	public Principal login(String principalName) {
		log.infof("returning SimplePrincipal(%s)", principalName);
		return new SimplePrincipal(principalName);		
	}

	public void ejbActivate() throws EJBException {
		log.info("activate");		
	}

	public void ejbPassivate() throws EJBException {
		log.info("passivate");		
	}

	public void ejbRemove() throws EJBException {
		log.info("remove");
	}

	public void setSessionContext(SessionContext ctx) throws EJBException {
		log.info("setSessionContext");
		this.ctx = ctx;
	}
}