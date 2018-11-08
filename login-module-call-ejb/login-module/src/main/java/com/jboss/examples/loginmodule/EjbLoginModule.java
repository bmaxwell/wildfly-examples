/**
 * 
 */
package com.jboss.examples.loginmodule;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.logging.Logger;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.AbstractServerLoginModule;

import com.jboss.examples.loginmodule.ejb.EjbAuthenticator;
import com.jboss.examples.loginmodule.ejb.EjbAuthenticatorLocalHome;
import com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemote;
import com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemoteHome;

/*
<security-domain name="ejb" cache-type="default">
    <authentication>
        <login-module code="com.jboss.examples.loginmodule.EjbLoginModule" module="com.jboss.examples.loginmodule" flag="required">
            <module-option name="ejbLookup" value="java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemoteHome"/>
        </login-module>
    </authentication>
</security-domain>
 */
/*
java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemoteHome
java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemote
java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorLocalHome
java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorLocal
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

		if (ejbLookup != null)
			ejbLookup = (String) options.get("ejbLookup");

		log.info("initialize ejbLookup: " + this.ejbLookup);
	}

	private EjbAuthenticatorRemote getEjbAuthenticator() throws Exception {
		Object object = new InitialContext().lookup(ejbLookup);
		log.infof("Retrieved %s from %s", object, ejbLookup);
		logClassInfo(object);
		EjbAuthenticatorRemoteHome home = (EjbAuthenticatorRemoteHome) PortableRemoteObject.narrow(object,
				EjbAuthenticatorRemoteHome.class);
		log.infof("PortableRemoteObject.narrow EjbAuthenticatorLocalHome: %s", home);
		EjbAuthenticatorRemote remote = home.create();
		log.infof("EjbAuthenticatorRemote: %s", remote);
		return remote;
	}

	private void logClassInfo(Object o) {
		log.info("Class: " + o.getClass().getName());
		for (Class iface : o.getClass().getInterfaces()) {
			log.infof("Interface: " + iface.getName());
		}
	}

	@Override
	public boolean login() throws LoginException {
		log.info("Starting login()");
		try {
			String principalName = "ejbuser";
			String roleName = "ejbuser";
			// call ejb but ignoring it for now
			this.identity = getEjbAuthenticator().login(principalName);
//			RunAsIdentity runAsIdentity = new RunAsIdentity(roleName, principalName);
//			this.identity = runAsIdentity;
//			SecurityContextAssociation.pushRunAsIdentity(runAsIdentity);

			log.info("login() - EJB returned principal: " + this.identity);
			super.sharedState.put("javax.security.auth.login.name", "ejbuser");
			super.sharedState.put("javax.security.auth.login.password", "redhat1!");
			log.info("login() returning true");

			loginOk = true;
			return true;
		} catch (Throwable t) {
			log.error("login() - EJB Invocation failed, returning false", t);
			return false;
		}
	}

	public boolean commit() throws LoginException {
		log.info("commit()");
		return super.commit();
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
	
	private String getUsername() {
		return "ejbuser";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getRoleSets()
	 */
	@Override
	protected Group[] getRoleSets() throws LoginException {
		log.info("getRoleSets()");
		
	      try {
	         // The declarative permissions
	         Group roles = new SimpleGroup("Roles");
	         // The caller identity
	         Group callerPrincipal = new SimpleGroup("CallerPrincipal");
	         Group[] groups = {roles, callerPrincipal};
	         log.info("Getting roles for user=" + getUsername());
	         // Add the Echo role
	         roles.addMember(new SimplePrincipal("ejbuser"));
	         // Add the custom principal for the caller
	         //callerPrincipal.addMember(new CustomPrincipalImpl("ImaReadableUsername"));
	         return groups;
	      }
	      catch (Exception e) {
	         log.info("Failed to obtain groups for user=" + getUsername() + " " + e);
	         throw new LoginException(e.toString());
	      }
	}

}
