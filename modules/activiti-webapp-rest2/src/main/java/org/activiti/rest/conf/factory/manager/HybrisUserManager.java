package org.activiti.rest.conf.factory.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

public class HybrisUserManager extends AbstractManager implements UserIdentityManager {

    @Override
    public User createNewUser(String userId) {
        return new UserEntity(userId);
    }

    @Override
    public void insertUser(User user) {
        getDbSqlSession().insert((PersistentObject) user);

        if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
            getProcessEngineConfiguration().getEventDispatcher()
                    .dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_CREATED, user));
            getProcessEngineConfiguration().getEventDispatcher()
                    .dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_INITIALIZED, user));
        }
    }

    @Override
    public void updateUser(User updatedUser) {
        CommandContext commandContext = Context.getCommandContext();
        DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
        dbSqlSession.update((PersistentObject) updatedUser);

        if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
            getProcessEngineConfiguration().getEventDispatcher()
                    .dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_UPDATED, updatedUser));
        }
    }

    @Override
    public User findUserById(String userId) {
        return (UserEntity) getDbSqlSession().selectOne("selectUserById", userId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteUser(String userId) {
        UserEntity user = (UserEntity) findUserById(userId);
        if (user != null) {
            List<IdentityInfoEntity> identityInfos = getDbSqlSession().selectList("selectIdentityInfoByUserId", userId);
            for (IdentityInfoEntity identityInfo : identityInfos) {
                getIdentityInfoManager().deleteIdentityInfo(identityInfo);
            }
            getDbSqlSession().delete("deleteMembershipsByUserId", userId);

            user.delete();

            if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
                getProcessEngineConfiguration().getEventDispatcher()
                        .dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_DELETED, user));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        return getDbSqlSession().selectList("selectUserByQueryCriteria", query, page);
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        return (Long) getDbSqlSession().selectOne("selectUserCountByQueryCriteria", query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> findGroupsByUser(String userId) {
        return getDbSqlSession().selectList("selectGroupsByUserId", userId);
    }

    @Override
    public UserQuery createNewUserQuery() {
        return new UserQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutor());
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("userId", userId);
        parameters.put("key", key);
        return (IdentityInfoEntity) getDbSqlSession().selectOne("selectIdentityInfoByUserIdAndKey", parameters);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("userId", userId);
        parameters.put("type", type);
        return (List) getDbSqlSession().getSqlSession().selectList("selectIdentityInfoKeysByUserIdAndType", parameters);
    }

    @Override
    public Boolean checkPassword(String userId, String password) {
        User user = findUserById(userId);
        if ((user != null) && (password != null) && (password.equals(user.getPassword()))) {
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findPotentialStarterUsers(String proceDefId) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("procDefId", proceDefId);
        return (List<User>) getDbSqlSession().selectOne("selectUserByQueryCriteria", parameters);

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        return getDbSqlSession().selectListWithRawParameter("selectUserByNativeQuery", parameterMap, firstResult, maxResults);
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        return (Long) getDbSqlSession().selectOne("selectUserCountByNativeQuery", parameterMap);
    }

    @Override
    public boolean isNewUser(User user) {
        return ((UserEntity) user).getRevision() == 0;
    }

    @Override
    public Picture getUserPicture(String userId) {
        UserEntity user = (UserEntity) findUserById(userId);
        return user.getPicture();
    }

    @Override
    public void setUserPicture(String userId, Picture picture) {
        UserEntity user = (UserEntity) findUserById(userId);
        if (user == null) {
            throw new ActivitiObjectNotFoundException("user " + userId + " doesn't exist", User.class);
        }

        user.setPicture(picture);
    }


}
