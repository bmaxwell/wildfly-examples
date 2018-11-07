package com.jboss.examples.loginmodule.ejb.test;

import javax.ejb.Remote;

@Remote
public interface TestRemote {
    public String test(String name);
}
