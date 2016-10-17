package com.tplink.demo.activiti.service.impl;

import org.springframework.stereotype.Service;

import com.tplink.demo.activiti.service.MailService;

@Service("mailService")
public class MailServiceImpl implements MailService {
	
	public void sendMail(String to, String subject, String content) {
		System.out.println("*********************************");
		System.out.println("Send Mail From : OAMailer");
		System.out.println("To :" + to);
		System.out.println("Subject :[" + subject + "]");
		System.out.println("Mail Content:" + content);
		System.out.println("*********************************");
	}
}
