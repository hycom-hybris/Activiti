package org.activiti.rest.conf.factory.manager.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.rest.conf.factory.manager.HybrisRESTManager;
import org.activiti.rest.conf.factory.manager.HybrisUserManager;

public class HybrisUserManagerWrapper extends HybrisUserManager {

    @Override
    public User findUserById(String userId) {
    	List<User> list = HybrisRESTManager.getHybrisUserById(userId);
    	return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
    	return HybrisRESTManager.getHybrisUserById(query.getId());
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
    	return HybrisRESTManager.getHybrisUserById(query.getId()).size();
    }
    
    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
    	
    	if (parameterMap != null && parameterMap.containsKey("id")) {
    		return HybrisRESTManager.getHybrisUserById((String) parameterMap.get("id"));
    	}
    	
        return new ArrayList<User>();
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
    	if (parameterMap != null && parameterMap.containsKey("id")) {
    		return HybrisRESTManager.getHybrisUserById((String) parameterMap.get("id")).size();
    	}
    	
        return 0L;
    }
    
    @Override
    public boolean isNewUser(User user) {
    	return false;
    }
    
    @Override
    public User createNewUser(String userId) {
    	// TODO Auto-generated method stub
    	return super.createNewUser(userId);
    }
    
    @Override
    public UserQuery createNewUserQuery() {
    	// TODO Auto-generated method stub
    	return super.createNewUserQuery();
    }
    
    @Override
    public void deleteUser(String userId) {
    	// TODO Auto-generated method stub
    	super.deleteUser(userId);
    }
    
    @Override
    public List<Group> findGroupsByUser(String userId) {
    	// TODO Auto-generated method stub
    	return super.findGroupsByUser(userId);
    }
    
    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
    	// TODO Auto-generated method stub
    	return super.findUserInfoByUserIdAndKey(userId, key);
    }
    
    @Override
    public Boolean checkPassword(String userId, String password) {
    	// TODO Auto-generated method stub
    	return super.checkPassword(userId, password);
    }
    
    @Override
    public List<User> findPotentialStarterUsers(String proceDefId) {
    	// TODO Auto-generated method stub
    	return super.findPotentialStarterUsers(proceDefId);
    }
    
    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
    	// TODO Auto-generated method stub
    	return super.findUserInfoKeysByUserIdAndType(userId, type);
    }
    
}
