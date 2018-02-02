package com.github.zhangkaitao.shiro.chapter3.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-26
 * <p>Version: 1.0
 */
// note 定义BitAndWildPermissionResolver及BitPermission
public class BitAndWildPermissionResolver implements PermissionResolver {

    // note BitAndWildPermissionResolver 实现了 PermissionResolver 接口
    // note 并根据权限字符串是否以 “+” 开头来解析权限字符串为 BitPermission 或 WildcardPermission
    public Permission resolvePermission(String permissionString) {
        if (permissionString.startsWith("+")) {
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}
