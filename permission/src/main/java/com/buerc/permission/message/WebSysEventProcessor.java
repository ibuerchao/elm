package com.buerc.permission.message;

import com.buerc.common.utils.JSONUtil;
import com.buerc.permission.service.SysUserService;
import com.buerc.redis.Event;
import com.buerc.redis.EventProcessor;
import com.buerc.redis.constants.EventConstants;
import com.buerc.security.holder.SecurityContextHolder;
import com.buerc.sys.bo.UserInfo;
import com.buerc.sys.dto.RoleResFormParam;
import com.buerc.sys.dto.RoleUserFormParam;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class WebSysEventProcessor implements EventProcessor {

    @Resource
    private SysUserService sysUserService;

    @Override
    public Integer getModule() {
        return EventConstants.Module.WEB_SYS;
    }

    @Override
    public void process(Event event) {
        switch (event.getType()) {
            case EventConstants.Type.ROLE_USER:
                roleUser(event.getData());
                break;
            case EventConstants.Type.ROLE_RES:
                roleRes(event.getData());
                break;
            case EventConstants.Type.ROLE_CHANGE:
                roleChange(event.getData());
                break;
            default:
        }
    }

    private void roleUser(Object o){
        RoleUserFormParam data = JSONUtil.toObject(JSONUtil.toStr(o), new TypeReference<RoleUserFormParam>() {
        });
        refresh(data.getUserIds());
    }

    private void roleRes(Object o){
        RoleResFormParam data = JSONUtil.toObject(JSONUtil.toStr(o), new TypeReference<RoleResFormParam>() {
        });
        refresh(data.getUserIds());
    }

    private void roleChange(Object o){
        Set<String> data = JSONUtil.toObject(JSONUtil.toStr(o), new TypeReference<Set<String>>() {
        });
        refresh(data);
    }

    private void refresh(Set<String> userIds){
        for (String userId:userIds){
            UserInfo userInfo = sysUserService.infoByUserId(userId);
            SecurityContextHolder.refreshUserInfo(userId,userInfo);
        }
    }
}
