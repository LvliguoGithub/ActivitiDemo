package com.tplink.demo.activiti.service;

public interface MailService {

	/**
	 * 模拟邮件发送
	 * @param to
	 * @param subject
	 * @param content
	 */
	void sendMail(String to, String subject, String content);
}
