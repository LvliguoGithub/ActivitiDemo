package com.tplink.demo.activiti.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tplink.demo.activiti.service.VacationService;

/**
 * 
 * @abstract: 
 * 	假期规则，配置：delegateExpression="${vacationRuleListener}"
 * @author mis_cxj
 */
@Component("vacationRuleListener")
public class VacationRuleListener implements TaskListener {
	@Autowired
	private VacationService vacationService;
	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("vacation rule listener called");
		String assignee = delegateTask.getAssignee();
		delegateTask.setAssignee(vacationService.findProxyer(assignee));
	}

}
