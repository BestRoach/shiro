package com.github.zhangkaitao.shiro.chapter4;

import com.alibaba.druid.pool.DruidDataSource;
import junit.framework.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.util.Arrays;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-27
 * <p>Version: 1.0
 */
public class NonConfigurationCreateTest {

    // note 这是纯java写法
    // note Shiro是从SecurityManager进行身份验证和授权的
    // note SecurityManager是线程安全的,所以Shiro提供了SecurityUtil让我们绑定为全局
    // note Shiro是POJO的,所以是很容易放到IoC容器中管理的
    @Test
    public void test() {

        DefaultSecurityManager securityManager = new DefaultSecurityManager();

        // note 认证器,负责认证主体
        //设置authenticator
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);

        // note Authorizer的职责是进行授权(访问控制)
        //设置authorizer
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        securityManager.setAuthorizer(authorizer);

        // note 域,Shiro从Realm获取安全数据(如用户,角色,权限)
        //设置Realm
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://192.168.1.30:3306/shiro");
        ds.setUsername("root");
        ds.setPassword("root");

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(ds);
        jdbcRealm.setPermissionsLookupEnabled(true);
        securityManager.setRealms(Arrays.asList((Realm) jdbcRealm));

        //将SecurityManager设置到SecurityUtils 方便全局使用
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);

        Assert.assertTrue(subject.isAuthenticated());

    }
}
