package com.tplink.demo.activiti.service;

/**
 * 
 * @abstract: 
 * 		模拟假期规则
 * @author mis_cxj
 */
public interface VacationService {

	/**
	 * 查找当前人员的工作代理人
	 * @param assignee
	 * @return
	 */
	String findProxyer(String assignee);
}
