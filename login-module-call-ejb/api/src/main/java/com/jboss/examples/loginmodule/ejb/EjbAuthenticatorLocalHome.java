/**
 * 
 */
package com.jboss.examples.loginmodule.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author bmaxwell
 *
 */
public interface EjbAuthenticatorLocalHome extends EJBLocalHome {
    EjbAuthenticatorLocal create() throws CreateException;
}