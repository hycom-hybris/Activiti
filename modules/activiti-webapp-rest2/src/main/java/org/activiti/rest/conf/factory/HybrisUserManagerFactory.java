package org.activiti.rest.conf.factory;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

import org.activiti.rest.conf.factory.manager.HybrisUserManager;
import org.activiti.rest.conf.factory.manager.wrapper.HybrisUserManagerWrapper;

public class HybrisUserManagerFactory implements SessionFactory {

    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return new HybrisUserManagerWrapper();
    }

}
