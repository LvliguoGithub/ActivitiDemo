package com.tplink.demo.activiti.service.impl;

import org.springframework.stereotype.Service;

import com.tplink.demo.activiti.service.VacationService;

@Service("vacationService")
public class VacationServiceImpl implements VacationService {

	@Override
	public String findProxyer(String assignee) {
		if ("chenxinjie".equals(assignee)) {
			return "fengkan";
		}
		return assignee;
	}
}
