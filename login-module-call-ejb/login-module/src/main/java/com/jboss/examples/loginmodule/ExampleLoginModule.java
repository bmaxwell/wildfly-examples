/**
 * 
 */
package com.jboss.examples.loginmodule;

import java.util.Map;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * @author bmaxwell
 *
 */
public class ExampleLoginModule implements LoginModule {

	private Logger log = Logger.getLogger(ExampleLoginModule.class.getSimpleName());
	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;
	
	/**
	 * 
	 */
	public ExampleLoginModule() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */	
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#login()
	 */	
	public boolean login() throws LoginException {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */	
	public boolean commit() throws LoginException {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		return false;
	}
}