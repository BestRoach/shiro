package com.github.zhangkaitao.shiro.chapter4;

import junit.framework.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-27
 * <p>Version: 1.0
 */
public class ConfigurationCreateTest {

    // note 从Shiro中获取对应的securityManager实例
    @Test
    public void test() {

        // note IniSecurityFactory 是创建securityManager的工厂
        // note 这里需要一个ini配置文件路径支持的格式有
        // note "classpath:"(类路径),"file:"(文件系统),"url:"(网络)三种路径格式,默认是文件系统;
        Factory<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro-config.ini");

        // note 默认情况下先创建securityManager,自定义需要在ini文件中指定"securityManager = SecurityManager 实现类"
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();

        // note 获取SecurityManager实例
        //将SecurityManager设置到SecurityUtils 方便全局使用
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);

        Assert.assertTrue(subject.isAuthenticated());

    }
}
