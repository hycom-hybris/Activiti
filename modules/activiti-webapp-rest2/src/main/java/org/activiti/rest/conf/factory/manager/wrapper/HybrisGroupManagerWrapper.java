package org.activiti.rest.conf.factory.manager.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.activiti.rest.conf.factory.manager.HybrisGroupManager;
import org.activiti.rest.conf.factory.manager.HybrisRESTManager;

public class HybrisGroupManagerWrapper extends HybrisGroupManager {

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        return HybrisRESTManager.getHybrisGroupById(query.getId());
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
    	return HybrisRESTManager.getHybrisGroupById(query.getId()).size();
    }

    @Override
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
    	
    	if (parameterMap != null && parameterMap.containsKey("id")) {
    		return HybrisRESTManager.getHybrisGroupById((String) parameterMap.get("id"));
    	}
    	
        return new ArrayList<Group>();
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
    	
    	if (parameterMap != null && parameterMap.containsKey("id")) {
    		return HybrisRESTManager.getHybrisGroupById((String) parameterMap.get("id")).size();
    	}
    	
        return 0L;
    }

    @Override
    public boolean isNewGroup(Group group) {
    	return false;
    }
    
    @Override
    public Group createNewGroup(String groupId) {
    	// TODO Auto-generated method stub
    	return super.createNewGroup(groupId);
    }
    
    @Override
    public GroupQuery createNewGroupQuery() {
    	// TODO Auto-generated method stub
    	return super.createNewGroupQuery();
    }
    
    @Override
    public void deleteGroup(String groupId) {
    	// TODO Auto-generated method stub
    	super.deleteGroup(groupId);
    }
    
    @Override
    public List<Group> findGroupsByUser(String userId) {
    	// TODO Auto-generated method stub
    	return super.findGroupsByUser(userId);
    }
}
