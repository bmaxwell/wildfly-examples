/**
 * 
 */
package com.jboss.examples.loginmodule.ejb;

import java.rmi.RemoteException;
import java.security.Principal;

import javax.ejb.EJBObject;

/**
 * @author bmaxwell
 *
 */
public interface EjbAuthenticatorRemote extends EJBObject {
	public Principal login(String principalName) throws RemoteException;
}