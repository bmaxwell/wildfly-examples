package com.jboss.examples.loginmodule.ejb.test;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

@Stateless(name="TestBean")
@RolesAllowed({"ejbuser"})
public class TestBean implements TestRemote {
	
	private Logger log = Logger.getLogger(TestBean.class.getName());
	
	@Resource
	private SessionContext ctx;
	
    public String test(String name) {
    	String response = String.format("test(%s) from caller %s", name, getCaller());
    	log.info(response);
        return response;
    }
    
    private String getCaller() {
    	return ctx.getCallerPrincipal() == null ? null : ctx.getCallerPrincipal().getName();
    }
}
