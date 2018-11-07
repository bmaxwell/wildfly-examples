package com.jboss.examples.loginmodule.ejb;

import java.security.Principal;

public interface EjbAuthenticator {
	public Principal login(String principalName);
}