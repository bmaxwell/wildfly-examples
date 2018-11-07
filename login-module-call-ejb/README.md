
mvn clean install

cp -R module/com $JBOSS_HOME/modules/
cp -R api/target/*.jar $JBOSS_HOME/modules/com/jboss/examples/loginmodule/main/
cp -R ear/target/*.ear $JBOSS_HOME/modules/com/jboss/examples/loginmodule/main/

Configure security domain in the standalone.xml something like this:

<security-domain name="ejb" cache-type="default">
    <authentication>
        <login-module code="com.jboss.examples.loginmodule.EjbLoginModule" module="com.jboss.examples.loginmodule" flag="required">
            <module-option name="ejbLookup" value="java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemoteHome"/>
        </login-module>
    </authentication>
</security-domain>

TODO:
- create an EJB that uses this security domain
- create a client to invoke the EJB that uses this security domain
