package com.jboss.examples.loginmodule.ejb.test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

public class TestClient {

	private static Logger log = Logger.getLogger(TestClient.class.getName());

	public static void main(String[] args) {
		try {
			Context ctx = getInitialContext("localhost", 8080, "ejbuser", "redhat1!");
			TestRemote client = (TestRemote) ctx.lookup("ejb:/ejb-login-module-test-ejb/TestBean!com.jboss.examples.loginmodule.ejb.test.TestRemote");
			log.info(client.test("client"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Context getInitialContext(String host, Integer port, String username, String password)
			throws NamingException {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		props.put(Context.PROVIDER_URL, String.format("%s://%s:%d", "remote+http", host, port));
		if (username != null && password != null) {
			props.put(Context.SECURITY_PRINCIPAL, username);
			props.put(Context.SECURITY_CREDENTIALS, password);
		}
		log.info("*** InitialContext Properties ***");
		System.out.println("*** InitialContext Properties ***");
		props.forEach((k,v) -> log.infof("%s -> %s\n", k, v));
		return new InitialContext(props);
	}
}