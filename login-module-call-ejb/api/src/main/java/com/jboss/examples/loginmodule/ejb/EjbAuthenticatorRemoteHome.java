/**
 * 
 */
package com.jboss.examples.loginmodule.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author bmaxwell
 *
 */
public interface EjbAuthenticatorRemoteHome extends EJBHome {
	public EjbAuthenticatorRemote create() throws CreateException, RemoteException;	
}