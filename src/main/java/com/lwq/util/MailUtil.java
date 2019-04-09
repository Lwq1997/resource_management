package com.lwq.util;

import com.lwq.beans.MailInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MailUtil {

    //邮箱
    private static String mailServerHost = "smtp.qq.com";
    private static String mailSenderAddress = "1306080729@qq.com";
    private static String mailSenderNick = "lwq";
    private static String mailSenderUsername = "1306080729@qq.com";
    private static String mailSenderPassword = "kynnahhmerwchgga";

    /**
     * 发送 邮件方法 (Html格式，支持附件)
     *
     * @return void
     */
    public static void sendEmail(MailInfo mailInfo) {

        try {
            HtmlEmail email = new HtmlEmail();
            // 配置信息
            email.setHostName(mailServerHost);
            email.setSmtpPort(465);
            email.setFrom(mailSenderAddress,mailSenderNick);
            email.setAuthenticator(new DefaultAuthenticator(mailSenderUsername,mailSenderPassword));
            email.setSSLOnConnect(true); // 是否启用SSL
            email.setCharset("UTF-8");   //发送的时候如果乱码,配置相应的编码
            email.setSubject(mailInfo.getSubject());
            email.setHtmlMsg(mailInfo.getContent());

            // 收件人
            List<String> toAddress = mailInfo.getToAddress();
            if (null != toAddress && toAddress.size() > 0) {
                for (int i = 0; i < toAddress.size(); i++) {
                    email.addTo(toAddress.get(i));
                }
            }
            email.send();
            System.out.println("邮件发送成功！");
        } catch (EmailException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws EmailException {
        MailInfo mailInfo = new MailInfo();
        List<String> toList = new ArrayList<String>();
        toList.add("943904454@qq.com");


        mailInfo.setToAddress(toList);//收件人

        mailInfo.setSubject("测试主题");
        mailInfo.setContent("内容：<h1>test,测试</h1>");

        MailUtil.sendEmail(mailInfo);
    }
}

