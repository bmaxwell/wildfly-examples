/**
 * 
 */
package com.jboss.examples.loginmodule;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;

import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.logging.Logger;
import org.jboss.security.auth.spi.AbstractServerLoginModule;

import com.jboss.examples.loginmodule.ejb.EjbAuthenticator;
import com.jboss.examples.loginmodule.ejb.EjbAuthenticatorLocalHome;


/*
<security-domain name="ejb" cache-type="default">
    <authentication>
        <login-module code="com.jboss.examples.loginmodule.EjbLoginModule" module="com.jboss.examples.loginmodule" flag="required">
            <module-option name="ejbLookup" value="java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemoteHome"/>
        </login-module>
    </authentication>
</security-domain>
 */

/**
 * @author bmaxwell
 *
 */
public class EjbLoginModule extends AbstractServerLoginModule {

	private Logger log = Logger.getLogger(EjbLoginModule.class.getName());
	
	/** The login identity */
	private Principal identity;
	
	private String ejbLookup = "java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemoteHome";

	/**
	 * 
	 */
	public EjbLoginModule() {
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
		super.initialize(subject, callbackHandler, sharedState, options);
						
		if(ejbLookup != null)			
			ejbLookup = (String) options.get("ejbLookup");
		
		log.info("initialize ejbLookup: " + ejbLookup);
	}
	
	private EjbAuthenticator getEjbAuthenticator() throws Exception {
		Object object = new InitialContext().lookup(ejbLookup);
		log.infof("Retrieved %s from %s", object, ejbLookup);
		EjbAuthenticatorLocalHome home = (EjbAuthenticatorLocalHome) PortableRemoteObject.narrow(object, EjbAuthenticatorLocalHome.class);
		log.infof("PortableRemoteObject.narrow EjbAuthenticatorLocalHome: %s", home);
		EjbAuthenticator local = home.create();
		log.infof("EjbAuthenticatorLocal: %s", local);
		return local;
	}

	@Override
	public boolean login() throws LoginException {
		log.info("Starting login()");
		try {
      String principalName = "ejbuser";
			this.identity = getEjbAuthenticator().login(principalName);
			return true;
		} catch(Throwable t) {
			log.error("EJB Invocation failed, returning false", t);
			return false;
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getIdentity()
	 */
	@Override
	protected Principal getIdentity() {
    log.info("returning identity: " + identity);
		return identity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getRoleSets()
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
    log.info("returning empty Group array");
		return new Group[0];
	}

}
