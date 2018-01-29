package com.github.zhangkaitao.shiro.chapter3;

import junit.framework.Assert;
import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Test;

import java.util.Arrays;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-26
 * <p>Version: 1.0
 */
public class RoleTest extends BaseTest {

    // note Shiro只是判断用户是否有该角色,不负责维护用户-角色信息
    // note Shiro提供了hasRole用来判断用户是否具有某个角色或者某些权限,但是没有提供方法判断是否为某些权限中的某一个
    @Test
    public void testHasRole() {
        login("classpath:shiro-role.ini", "zhang", "123");
        // 断言拥有角色：role1
        Assert.assertTrue(subject().hasRole("role1"));
        // 断言拥有角色：role1 and role2
        Assert.assertTrue(subject().hasAllRoles(Arrays.asList("role1", "role2")));
        // 断言拥有角色：role1 and role2 and !role3
        boolean[] result = subject().hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertEquals(true, result[0]);
        Assert.assertEquals(true, result[1]);
        Assert.assertEquals(false, result[2]);

    }

    // note 使用hasRole和checkRole的区别在于判断为假的时候,checkRole会抛出UnauthorizedException异常
    @Test(expected = UnauthorizedException.class)
    public void testCheckRole() {
        login("classpath:shiro-role.ini", "zhang", "123");
        //断言拥有角色：role1
        System.out.println("前");
        subject().checkRole("role1");
        System.out.println("中");
        //断言拥有角色：role1 and role3 失败抛出异常
        subject().checkRoles("role1", "role3");
        System.out.println("后");
    }

}
