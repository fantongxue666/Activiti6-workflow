package com.ftx.springboot.utils;

import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName EmailUtil.java
 * @Description TODO
 * @createTime 2020年05月14日 13:42:00
 */
@Component
public class EmailUtil {
    /**
     * todo 发送QQ邮箱
     */
    public void sendQQEmail(String emailAddress){
        //做链接前的准备工作  也就是参数初始化
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host","smtp.qq.com");//发送邮箱服务器
        properties.setProperty("mail.smtp.port","465");//发送端口
        properties.setProperty("mail.smtp.auth","true");//是否开启权限控制
        properties.setProperty("mail.debug","true");//true 打印信息到控制台
        properties.setProperty("mail.transport","smtp");//发送的协议是简单的邮件传输协议
        properties.setProperty("mail.smtp.ssl.enable","true");
        //建立两点之间的链接
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("675361896@qq.com","wbomvmxtifjybcbb");
            }
        });
        //创建邮件对象
        Message message = new MimeMessage(session);
        //设置发件人
        try {
            message.setFrom(new InternetAddress("675361896@qq.com"));

            //设置收件人
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(emailAddress));//收件人
            //设置主题
            message.setSubject("Activiti工作流审核结果通知");
            //设置邮件正文  第二个参数是邮件发送的类型
            message.setContent("尊敬的用户您好，您的请假申请已通过，请知悉！","text/html;charset=UTF-8");
            //发送一封邮件
            Transport transport = session.getTransport();
            transport.connect("675361896@qq.com","wbomvmxtifjybcbb");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * todo 发送网易163邮箱
     */
    // 发件人 账号和密码
    public static final String MY_EMAIL_ACCOUNT = "fantongxue666@163.com";
    public static final String MY_EMAIL_PASSWORD = "PCRAVAFQNZVIJDOI";// 密码,是你自己的设置的授权码

    // SMTP服务器(这里用的163 SMTP服务器)
    public static final String MEAIL_163_SMTP_HOST = "smtp.163.com";
    public static final String SMTP_163_PORT = "25";// 端口号,这个是163使用到的;QQ的应该是465或者875

    // 收件人
    public static final String RECEIVE_EMAIL_ACCOUNT = "fantongxue666@163.com";

    @Test
    public void sendWY163Email(String emailAddress) throws Exception{
        Properties p = new Properties();
        p.setProperty("mail.smtp.host", MEAIL_163_SMTP_HOST);
        p.setProperty("mail.smtp.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.socketFactory.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");

        Session session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MY_EMAIL_ACCOUNT, MY_EMAIL_PASSWORD);
            }
        });
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress(MY_EMAIL_ACCOUNT));
        // 收件人和抄送人
        message.setRecipients(Message.RecipientType.TO, RECEIVE_EMAIL_ACCOUNT);
//		message.setRecipients(Message.RecipientType.CC, MY_EMAIL_ACCOUNT);

        // 内容(这个内容还不能乱写,有可能会被SMTP拒绝掉;多试几次吧)
        message.setSubject("Activiti工作流审核结果通知");
        message.setContent("尊敬的用户您好，您的请假申请已通过，请知悉！", "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        Transport.send(message);

    }
}
