package com.github.zhangkaitao.shiro.chapter3.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Arrays;
import java.util.Collection;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-26
 * <p>Version: 1.0
 */
public class MyRolePermissionResolver implements RolePermissionResolver {
    // 用于根据角色字符串来解析的到权限集合
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        if ("role1".equals(roleString)) {
            // 如果用户拥有role1,那么就返回一个"menu:*"的权限
            return Arrays.asList((Permission) new WildcardPermission("menu:*"));
        }
        return null;
    }
}
