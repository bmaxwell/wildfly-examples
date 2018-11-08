#!/bin/bash

echo "using $JBOSS_HOME"
jars=test-ejb/target/ejb-login-module-test-ejb-client.jar:test-client/target/ejb-login-module-test-client.jar
class=com.jboss.examples.loginmodule.ejb.test.TestClient
cp=$JBOSS_HOME/bin/client/jboss-client.jar:$JBOSS_HOME/bin/client/jboss-cli-client.jar:$jars

echo "java -Djava.util.logging.manager=org.jboss.logmanager.LogManager -cp $cp $class"
java -Djava.util.logging.manager=org.jboss.logmanager.LogManager -cp $cp $class
