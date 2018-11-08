package com.jboss.examples.loginmodule.ejb.test;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

public class TestClient {

	private static Logger log = Logger.getLogger(TestClient.class.getName());
	private static final String WFIF = "org.wildfly.naming.client.WildFlyInitialContextFactory";
	private static final String RNIF = "org.jboss.naming.remote.client.InitialContextFactory";
	private static boolean isEJB = true;
	private static String lookup = "ejb-login-module-test-ejb/TestBean!com.jboss.examples.loginmodule.ejb.test.TestRemote";
	
	public static void main(String[] args) {
		try {
			System.out.println("jboss-ejb-client.properties: "+ Thread.currentThread().getContextClassLoader().getResource("jboss-ejb-client.properties"));
			Context ctx = getInitialContext("localhost", 8080, "ejbuser", "redhat1!");
			System.out.printf("ejb lookup: %s", getLookup());
			TestRemote client = (TestRemote) ctx.lookup(getLookup());
			System.out.println(client.test("client"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Context getInitialContext(String host, Integer port, String username, String password)
			throws NamingException {
		Hashtable props = scoped(host, port);			
//		if (username != null && password != null) {
//			props.put(Context.SECURITY_PRINCIPAL, username);
//			props.put(Context.SECURITY_CREDENTIALS, password);
//		}
		log.info("*** InitialContext Properties ***");
		System.out.println("*** InitialContext Properties ***");
		props.forEach((k,v) -> System.out.printf("%s -> %s\n", k, v));
		return new InitialContext(props);
	}
	
	private static String getLookup() {
		if(isEJB)
			return "ejb:/" + lookup;
		return lookup;
	}
	
	private static Properties scoped(String host, Integer port) {
		Properties p = new Properties();
		String user = "ejbuser";
		String pass = "redhat1!";
		
//        Hashtable env = new Hashtable();
//        env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//        env.put("endpoint.name", "client-endpoint");
//        env.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
//        env.put("remote.connections", user);
//        env.put("remote.connection." + user + ".connect.options.org.xnio.Options.SSL_STARTTLS", "false");
//        env.put("remote.connection." + user + ".connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
//        env.put("remote.connection." + user + ".connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
//        env.put("remote.connection." + user + ".connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
//        env.put("remote.connection." + user + ".host", host);
//        env.put("remote.connection." + user + ".port", port);
//        env.put("remote.connection." + user + ".username", user);
//        env.put("remote.connection." + user + ".password", pass);
//        env.put("org.jboss.ejb.client.scoped.context",  true);
//		return env;
		
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//        p.put("endpoint.name", "client-endpoint");
//        p.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
//        p.put("remote.connections", "default");
//        p.put("remote.connection.default.connect.options.org.xnio.Options.SSL_STARTTLS", "false");
//        p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
//        p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
//        p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");	               
//        p.put("remote.connection.default.host", host);
//        p.put("remote.connection.default.hostname", host);
//        p.put("remote.connection.default.port", port);
//        p.put("remote.connection.default.username", "ejbuser");
//        p.put("remote.connection.default.password", "redhat1!");
//        p.put("org.jboss.ejb.client.scoped.context",  "true");
		return p;
	}

	
	private static Properties selectAvailableInitialContextFactory(String host, Integer port) {
		Properties p = new Properties();		
		try {
			Class.forName(WFIF);			
			p.put(Context.INITIAL_CONTEXT_FACTORY, WFIF);
			p.put(Context.PROVIDER_URL, String.format("%s://%s:%d", "remote+http", host, port));
		} catch(ClassNotFoundException cnfe) {
			
	        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
	        p.put("endpoint.name", "client-endpoint");
	        p.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
	        p.put("remote.connections", "default");
	        p.put("remote.connection.default.connect.options.org.xnio.Options.SSL_STARTTLS", "false");
	        p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
	        p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
	        p.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");	               
	        p.put("remote.connection.default.host", host);
	        p.put("remote.connection.default.hostname", host);
	        p.put("remote.connection.default.port", port);
	        p.put("remote.connection.default.username", "ejbuser");
	        p.put("remote.connection.default.password", "redhat1!");
	        p.put("org.jboss.ejb.client.scoped.context",  true);

			
//			p.put(Context.INITIAL_CONTEXT_FACTORY, RNIF);
//			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//			p.put("jboss.naming.client.ejb.context", "true");
//			p.put(Context.PROVIDER_URL, String.format("%s://%s:%d", "http-remoting", host, port));
			isEJB = true;
		}
		return p;
	}
}