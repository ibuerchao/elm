package com.buerc.permission.config;

import com.buerc.permission.util.IpUtil;
import com.buerc.security.holder.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AutoFillInterceptor implements Interceptor {

    private final static String FIELD_ID = "id";
    private final static String FIELD_OPERATE_ID = "operateId";
    private final static String FIELD_OPERATE_NAME = "operateName";
    private final static String FIELD_OPERATE_TIME = "operateTime";
    private final static String FIELD_OPERATE_IP = "operateIp";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            if (SqlCommandType.UPDATE.equals(sqlCommandType) || SqlCommandType.INSERT.equals(sqlCommandType)) {
                Object parameter = invocation.getArgs()[1];
                if (parameter instanceof HashMap) {
                    invokeMap(parameter, sqlCommandType);
                } else {
                    invoke(parameter, sqlCommandType);
                }

            }
        } catch (Exception e) {
            log.error("通用设置值时出错", e);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private void invoke(Object parameter, SqlCommandType sqlCommandType) throws IllegalAccessException {
        Class classParameter = parameter.getClass();
        Field[] fields = classParameter.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(parameter);
            if (FIELD_OPERATE_ID.equalsIgnoreCase(fieldName)) {
                if (value == null) {
                    field.set(parameter, SecurityContextHolder.getUserId());
                }
            } else if (FIELD_OPERATE_NAME.equalsIgnoreCase(fieldName)) {
                if (value == null) {
                    field.set(parameter, SecurityContextHolder.getUserName());
                }
            } else if (FIELD_OPERATE_TIME.equalsIgnoreCase(fieldName)) {
                if (value == null) {
                    field.set(parameter, new Date());
                }
            } else if (FIELD_OPERATE_IP.equalsIgnoreCase(fieldName)) {
                if (value == null) {
                    field.set(parameter, IpUtil.getRemoteAddr());
                }
            } else if (FIELD_ID.equalsIgnoreCase(fieldName)) {
                if (value == null) {
                    if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                        field.set(parameter, UUID.randomUUID().toString());
                    }
                }
            }
        }
    }

    private void invokeMap(Object parameter, SqlCommandType sqlCommandType) throws IllegalAccessException {
        HashMap<String, Object> map = (HashMap<String, Object>) parameter;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof List) {
                List<Object> list = (List<Object>) value;
                for (Object o : list) {
                    invoke(o, sqlCommandType);
                }
            } else {
                invoke(value, sqlCommandType);
            }
        }
    }
}