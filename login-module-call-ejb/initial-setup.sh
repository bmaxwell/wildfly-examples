#!/bin/bash

mvn clean install
cp -R module/com $JBOSS_HOME/modules/
cp -R api/target/*.jar $JBOSS_HOME/modules/com/jboss/examples/loginmodule/main/
cp -R login-module/target/*.jar $JBOSS_HOME/modules/com/jboss/examples/loginmodule/main/

cp -R ear/target/*.ear $JBOSS_HOME/standalone/deployments/
cp -R test-ejb/target/*ejb.jar $JBOSS_HOME/standalone/deployments/

$JBOSS_HOME/bin/jboss-cli.sh --file=config.cli
