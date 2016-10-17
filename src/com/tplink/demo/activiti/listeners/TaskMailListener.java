package com.tplink.demo.activiti.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tplink.demo.activiti.service.MailService;

@Component("taskMailListener")
public class TaskMailListener implements TaskListener {
	@Autowired
	private MailService mailService;
	private Expression type;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("task mail listener called");
		String assignee = delegateTask.getAssignee();
		String mailType = type.getValue(delegateTask).toString();
		if ("approve".equals(mailType)) {
			mailService.sendMail(assignee, "新的审核任务", "您有新的审核任务需要处理");
		} else if ("mtlLeader".equals(mailType)) {
			mailService.sendMail(assignee, "新的分配任务需要处理", "您有新的替代料评估选用任务需要处理");
		} else if ("mtlAssignPick".equals(mailType)) {
			mailService.sendMail(assignee, "新的分配选用任务", "您有新的分配选用任务需要处理");
		} else if ("summary".equals(mailType)) {
			mailService.sendMail(assignee, "新的汇总任务", "您有新的汇总任务需要处理");
		} else if ("evaInterface".equals(mailType)) {
			mailService.sendMail(assignee, "新的分配评估负责人任务", "您有新的任务，内容是分配评估负责人");
		} else if ("evaAssignEnginee".equals(mailType)) {
			mailService.sendMail(assignee, "新的分配评估工程师任务", "您有新的任务，内容是分配评估工程师");
		} else if ("evaluate".equals(mailType)) {
			mailService.sendMail(assignee, "新的评估任务", "您有新的任务，内容是评估替代料");
		} else if ("pickInterface".equals(mailType)) {
			mailService.sendMail(assignee, "新的分配选用负责人任务", "您有新的任务，内容是分配选用负责人");
		} else if ("pickAssignEnginee".equals(mailType)) {
			mailService.sendMail(assignee, "新的分配选用工程师任务", "您有新的任务，内容是分配选用工程师");
		} else if ("pick".equals(mailType)) {
			mailService.sendMail(assignee, "新的选用任务", "您有新的任务，内容是选用替代料");
		}
	}

	public void setType(Expression type) {
		this.type = type;
	}
}
