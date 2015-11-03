package org.activiti.rest.conf.factory;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.activiti.rest.conf.factory.manager.wrapper.HybrisGroupManagerWrapper;

public class HybrisGroupManagerFactory implements SessionFactory {

    @Override
    public Class<?> getSessionType() {
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return new HybrisGroupManagerWrapper();
    }

}
