package com.tplink.demo.activiti.activity;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @abstract:
 * 	该实例只会初始化一次，所以不允许有属性变量和非线程安全的方法。且实例化动作是发生在使用而不是流程部署阶段。
 * 	实现JavaDelegate或者ActivityBehavior均可，不过最佳实践是JavaDelegate，
 * 	因为ActivityBehavior功能更强大，所以更易出错，只有在JavaDelegate无法满足需求时才使用。
 * 	配置1：activiti:delegateExpression="${customActivity}"
 * 	配置2: activiti:class="com.tplink.demo.activiti.activity.CustomActivity"
 *  
 * @author mis_cxj
 */
@Deprecated
@Component("customActivity")
public class CustomActivity implements JavaDelegate {
	@Autowired
	private TaskService taskService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<String> approverList = (List<String>)execution.getVariable("approveList");
		if (approverList != null && approverList.size() > 0) {
			for (String approver : approverList) {
				Task task = taskService.newTask();
				task.setAssignee(approver);
			}
		}
	}
	
}
