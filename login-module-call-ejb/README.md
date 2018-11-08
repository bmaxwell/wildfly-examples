Building
----------
mvn clean install

Deploying artifacts
-------------------
cp -R module/com $JBOSS_HOME/modules/
cp -R api/target/*.jar $JBOSS_HOME/modules/com/jboss/examples/loginmodule/main/
cp -R login-module/target/*.jar $JBOSS_HOME/modules/com/jboss/examples/loginmodule/main/

cp -R ear/target/*.ear $JBOSS_HOME/standalone/deployments/
cp -R test-ejb/target/*.jar $JBOSS_HOME/standalone/deployments/


Configuring server
------------------
Configure security domain in the standalone.xml something like this:


/subsystem=security/security-domain=ejb:add(cache-type=default)
/subsystem=security/security-domain=ejb/authentication=classic:add(login-modules=[{code=com.jboss.examples.loginmodule.EjbLoginModule, module=com.jboss.examples.loginmodule,flag=required,module-options={ejbLookup=java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemoteHome}}])


<security-domain name="ejb" cache-type="default">
    <authentication>
        <login-module code="com.jboss.examples.loginmodule.EjbLoginModule" module="com.jboss.examples.loginmodule" flag="required">
            <module-option name="ejbLookup" value="java:global/ejb-login-module/ejb-login-module-ejb/EjbAuthenticator!com.jboss.examples.loginmodule.ejb.EjbAuthenticatorRemoteHome"/>
        </login-module>
    </authentication>
</security-domain>



/core-service=management/security-realm=ejb-realm:add()
/core-service=management/security-realm=ejb-realm/authentication=jaas:add(name=ejb)

        <security-realms>
            <security-realm name="ejb-realm">
                <authentication>
                    <jaas name="ejb"/>
                </authentication>
            </security-realm>
...


/subsystem=remoting/http-connector=http-remoting-connector:write-attribute(name=security-realm, value=ejb-realm)

            <http-connector name="http-remoting-connector" connector-ref="default" security-realm="ejb-realm"/>

Running client
---------------
./run-client.sh
