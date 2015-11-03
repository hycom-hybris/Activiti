package org.activiti.rest.conf.factory.manager;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;

public class HybrisGroupManager extends AbstractManager implements GroupIdentityManager {


    @Override
    public Group createNewGroup(String groupId) {
        return new GroupEntity(groupId);
    }

    @Override
    public void insertGroup(Group group) {
        getDbSqlSession().insert((PersistentObject) group);

        if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
            getProcessEngineConfiguration().getEventDispatcher()
                    .dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_CREATED, group));
            getProcessEngineConfiguration().getEventDispatcher()
                    .dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_INITIALIZED, group));
        }
    }

    @Override
    public void updateGroup(Group updatedGroup) {
        CommandContext commandContext = Context.getCommandContext();
        DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
        dbSqlSession.update((GroupEntity) updatedGroup);

        if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
            getProcessEngineConfiguration().getEventDispatcher()
                    .dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_UPDATED, updatedGroup));
        }
    }

    @Override
    public void deleteGroup(String groupId) {
        GroupEntity group = getDbSqlSession().selectById(GroupEntity.class, groupId);

        if (group != null) {
            if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
                getProcessEngineConfiguration().getEventDispatcher()
                        .dispatchEvent(ActivitiEventBuilder.createMembershipEvent(ActivitiEventType.MEMBERSHIPS_DELETED, groupId, null));
            }

            getDbSqlSession().delete("deleteMembershipsByGroupId", groupId);
            getDbSqlSession().delete(group);

            if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
                getProcessEngineConfiguration().getEventDispatcher()
                        .dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_DELETED, group));
            }
        }
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        return new GroupQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutor());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        return getDbSqlSession().selectList("selectGroupByQueryCriteria", query, page);
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        return (Long) getDbSqlSession().selectOne("selectGroupCountByQueryCriteria", query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> findGroupsByUser(String userId) {
        return getDbSqlSession().selectList("selectGroupsByUserId", userId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> findGroupsByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
        return getDbSqlSession().selectListWithRawParameter("selectGroupByNativeQuery", parameterMap, firstResult, maxResults);
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        return (Long) getDbSqlSession().selectOne("selectGroupCountByNativeQuery", parameterMap);
    }

    @Override
    public boolean isNewGroup(Group group) {
        return ((GroupEntity) group).getRevision() == 0;
    }
}
