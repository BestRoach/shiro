package com.github.zhangkaitao.shiro.chapter3;

import junit.framework.Assert;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.Test;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-26
 * <p>Version: 1.0
 */
public class PermissionTest extends BaseTest {

    // note Shiro提供了isPermitted和isPermittedAll用于判断某个用户是否拥有某个权限或者所有权限,也没有提供用于判断某一特定权限的接口
    @Test
    public void testIsPermitted() {
        // create update delete
        login("classpath:shiro-permission.ini", "zhang", "123");
        //判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user:create"));
        //判断拥有权限：user:update and user:delete
        Assert.assertTrue(subject().isPermittedAll("user:update", "user:delete"));
        //判断没有权限：user:view
        Assert.assertFalse(subject().isPermitted("user:view"));
    }

    // note 和checkRole方法一样,错误时抛出异常
    @Test(expected = UnauthorizedException.class)
    public void testCheckPermission() {
        login("classpath:shiro-permission.ini", "zhang", "123");
        //断言拥有权限：user:create
        subject().checkPermission("user:create");
        //断言拥有权限：user:delete and user:update
        subject().checkPermissions("user:delete", "user:update");
        //断言拥有权限：user:view 失败抛出异常
        subject().checkPermissions("user:view");
    }

    @Test
    public void testWildcardPermission1() {
        login("classpath:shiro-permission.ini", "li", "123");
        // note 字符串通配符权限
        // note 规则:"资源标识符:操作:对象实例ID"即对哪个资源的哪个实例可以进行什么操作.
        // note “:”表示资源/操作/实例的分割；“,”表示操作的分割；“*”表示任意资源/操作/实例
        // 用户拥有资源"system:user"的"update"权限,单个资源单个权限
        subject().checkPermissions("system:user:update", "system:user:delete");
        // 单个资源多个权限
        // note 通过"system:user:update,delete"可以判断"system:user:update"和"system.user.delete"
        // note 但是反过来是无法判断的.
        subject().checkPermissions("system:user:update,delete");
    }

    @Test
    public void testWildcardPermission2() {
        login("classpath:shiro-permission.ini", "li", "123");
        // note 单个资源全部权限
        subject().checkPermissions("system:user:create,delete,update,view");

        // note 单个资源全部权限推荐写法
        // note 通过system:user:* 可以验证"system:user:create,delete,update,view"
        subject().checkPermissions("system:user:*");
        subject().checkPermissions("system:user");
    }

    @Test
    public void testWildcardPermission3() {
        // note role61=*:view
        // note 表示所有用户的view权限
        // note 如果是"system:user:view"那么简写就会变成"role5=*:*:view"
        login("classpath:shiro-permission.ini", "li", "123");
        subject().checkPermissions("user:view");

        // note 以下的这种方式和subject().checkPermission(new WildcardPermission("system:user:view"));是等价的
        subject().checkPermissions("system:user:view");
    }

    // note 内部和上面的基本相同,以下是单例级别的权限
    // note 使用"*"通配所有的实例
    // note 对于使用的匹配而言,Shiro中的匹配是前缀匹配,即"user:view"等价于"user:view:*"
    // note *可以匹配所有,不加*可以进行前缀匹配,但是后缀匹配必须指定前缀,多个冒号就需要多个*来匹配.
    // todo 可以考虑比如在sql查询时加上权限字符串之类的方式在查询时就完成了权限匹配
    @Test
    public void testWildcardPermission4() {
        login("classpath:shiro-permission.ini", "li", "123");
        subject().checkPermissions("user:view:1");

        subject().checkPermissions("user:delete,update:1");
        subject().checkPermissions("user:update:1", "user:delete:1");

        subject().checkPermissions("user:update:1", "user:delete:1", "user:view:1");

        subject().checkPermissions("user:auth:1", "user:auth:2");

    }

    @Test
    public void testWildcardPermission5() {
        login("classpath:shiro-permission.ini", "li", "123");
        subject().checkPermissions("menu:view:1");

        subject().checkPermissions("organization");
        subject().checkPermissions("organization:view");
        subject().checkPermissions("organization:view:1");

    }

    @Test
    public void testWildcardPermission6() {
        login("classpath:shiro-permission.ini", "li", "123");
        subject().checkPermission("menu:view:1");
        subject().checkPermission(new WildcardPermission("menu:view:1"));

    }

}
