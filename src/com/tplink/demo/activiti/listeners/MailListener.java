package com.tplink.demo.activiti.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tplink.demo.activiti.service.MailService;

@Component("mailListener")
public class MailListener implements ExecutionListener {
	@Autowired
	private MailService mailService;
	private Expression type;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("mailListener called");
		String submitter = execution.getVariable("person").toString();
		String typeVal = type.getValue(execution).toString();
		if ("revert".equals(typeVal)) {
			mailService.sendMail(submitter, "任务被退回", 
					"您提交的任务被退回，退回人是：" + execution.getVariable("reverter").toString());
		} else if ("approved".equals(typeVal)) {
			mailService.sendMail(submitter, "任务被审核通过", 
					"您提交的任务已经被审核通过，请知悉");
		} else if ("EvaFail".equals(typeVal)) {
			mailService.sendMail(submitter, "评估选用任务评估失败", "您提交的评估选用任务经过评估确认失败，无法引入");
		} else if ("completeEvaAndPick".equals(typeVal)) {
			mailService.sendMail(submitter, "评估选用任务完成", "您提交的评估选用任务已经处理完成，请知悉");
		}
	}
	
	public void setType(Expression type) {
		this.type = type;
	}

}
