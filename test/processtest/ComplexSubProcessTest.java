package processtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.tplink.demo.activiti.service.BpmService;

import base.TestTool;
import basetest.TestBPMService;

public class ComplexSubProcessTest {
	private static BpmService bpmService = TestTool.getBean("bpmService", BpmService.class);
	
	public static void submit() {
		TestBPMService.testSubmit("ComplexSubProcess", getSubmitVariables());
	}
	
	public static void submitAgain(String instId) {
		List<Task> taskList = bpmService.searchTaskByInst(instId);
		for (Task task : taskList) {
			bpmService.completeTask(task.getId(), task.getAssignee(), getSubmitVariables());
		}
	}
	
	private static Map<String, Object> getSubmitVariables() {
		Map<String, Object> variables = new HashMap<>();
		variables.put("firstApprover", "chenxinjie");
		variables.put("boss", "anfeng");
		variables.put("mtlLeader", "chenxinjie");
		return variables;
	}
	
	public static void completeTask(String taskId, String assignee, 
			String result) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", result);
		if ("N".equals(result)) {
			variables.put("reverter", assignee);
		}
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	/**
	 * 物料负责人处理
	 * @param taskId
	 * @param assignee
	 * @param type
	 */
	public static void assign(String taskId, String assignee, String type) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", type);
		List<String> list1 = new ArrayList<>();
		list1.add("nongshuyu");
		list1.add("lvliguo");
		List<String> list2 = new ArrayList<>();
		list2.add("huangzhixiang");
		list2.add("lutiantian");
		if ("EvaAndPick".equals(type)) {
			variables.put("evaInterfacerList", list1);
			variables.put("pickInterfacerList", list2);
		} else if ("EvaThenPick".equals(type)) {
			variables.put("evaInterfacerList", list1);
		} else {
			variables.put("pickInterfacerList", list2);
		}
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	/**
	 * 分配评估负责人
	 * @param taskId
	 * @param assignee
	 * @param result
	 */
	public static void assignEvaLeader(String taskId, String assignee, String result) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", result);
		if ("Y".equals(result)) {
			List<String> leaders = new ArrayList<>();
			leaders.add("fengkan");
			leaders.add("lutiantian");
			variables.put("evaLeaderList", leaders);
		}
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	/**
	 * 分配选用负责人
	 * @param taskId
	 * @param assignee
	 * @param result
	 */
	public static void assignPickLeader(String taskId, String assignee, String result) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", result);
		if ("Y".equals(result)) {
			List<String> leaders = new ArrayList<>();
			leaders.add("zhangyoumao");
			leaders.add("huangbasen");
			variables.put("pickLeaderList", leaders);
		}
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	/**
	 * 分配评估工程师
	 * @param taskId
	 * @param assignee
	 * @param result
	 */
	public static void assignEvaEnginee(String taskId, String assignee, String result) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", result);
		if ("Y".equals(result)) {
			List<String> engineeList = new ArrayList<>();
			engineeList.add("hehaowen");
			engineeList.add("neiwei");
			variables.put("evaEngineerList", engineeList);
		}
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	/**
	 * 分配选用工程师
	 * @param taskId
	 * @param assignee
	 * @param result
	 */
	public static void assignPickEnginee(String taskId, String assignee, String result) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", result);
		if ("Y".equals(result)) {
			List<String> engineeList = new ArrayList<>();
			engineeList.add("fangquan");
			engineeList.add("miaoyi");
			variables.put("pickEngineerList", engineeList);
		}
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	/**
	 * 物料负责人分配产线选用
	 * @param taskId
	 * @param assignee
	 * @param result
	 */
	public static void assignPick(String taskId, String assignee, String result) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", result);
		if ("Pick".equals(result)) {
			List<String> list2 = new ArrayList<>();
			list2.add("huangzhixiang");
			list2.add("lutiantian");
			variables.put("pickInterfacerList", list2);
		}
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	public static void main(String[] args) {
		//部署父流程和子流程
		//TestBPMService.testDeploy("Evaluate", "Evaluate.bpmn20.xml");
		//TestBPMService.testDeploy("Pick", "Pick.bpmn20.xml");
		//TestBPMService.testDeploy("ComplexSubProcess", "ComplexSubProcess.bpmn20.xml");
		//submit();
		/*************************先评估后选用*********************************/
		//assign("212504", "fengkan", "EvaThenPick");
		//分配评估负责人
		//assignEvaLeader("220027", "lvliguo", "N");
		//assignEvaLeader("220018", "nongshuyu", "N");
		//分配选用
		//assignPick("222508", "fengkan", "Pick");
		//分配选用负责人
		//assignPickLeader("225027", "lutiantian", "Y");
		//assignPickLeader("225018", "huangzhixiang", "Y");
		//完成分配工程师
		//assignPickEnginee("227553", "huangbasen", "N");
		//assignPickEnginee("227546", "zhangyoumao", "Y");
		//assignPickEnginee("227526", "huangbasen", "Y");
		//assignPickEnginee("227519", "zhangyoumao", "N");
		//完成选用任务
		/*completeTask("230041", "miaoyi", "Y");
		completeTask("230036", "fangquan", "Y");
		completeTask("230021", "miaoyi", "Y");
		completeTask("230016", "fangquan", "Y");*/
		//汇总任务
		//completeTask("232506", "fengkan", "Y");
		/************************评估及选用************************************/
		//submit();
		//completeTask("240004", "chenxinjie", "Y");
		//completeTask("230036", "anfeng", "Y");
		//assign("242504", "fengkan", "EvaAndPick");
		//分配评估及选用负责人
		/*assignEvaLeader("245033", "lvliguo", "N");
		assignEvaLeader("245024", "nongshuyu", "Y");
		assignPickLeader("245057", "lutiantian", "Y");
		assignPickLeader("245048", "huangzhixiang", "Y");*/
		//分配评估及选用工程师
		/*assignEvaEnginee("247522", "fengkan", "Y");
		assignEvaEnginee("247529", "lutiantian", "Y");
		assignPickEnginee("247583", "huangbasen", "N");
		assignPickEnginee("247576", "zhangyoumao", "Y");
		assignPickEnginee("247556", "huangbasen", "Y");
		assignPickEnginee("247549", "zhangyoumao", "N");*/
		//完成评估及选用任务
		/*completeTask("250076", "miaoyi", "Y");
		completeTask("250071", "fangquan", "Y");
		completeTask("250056", "miaoyi", "Y");
		completeTask("250051", "fangquan", "Y");
		completeTask("250035", "neiwei", "Y");
		completeTask("250031", "hehaowen", "Y");
		completeTask("250019", "neiwei", "Y");
		completeTask("250014", "hehaowen", "Y");*/
		//汇总
		//completeTask("252511", "fengkan", "Y");
		
		/*********************只选用********************************/
		//submit();
		//completeTask("257514", "chenxinjie", "Y");
		//completeTask("260004", "anfeng", "Y");
		//assign("262504", "fengkan", "Pick");
		//assignPickLeader("265027", "lutiantian", "Y");
		//assignPickLeader("265018", "huangzhixiang", "Y");
		/*assignPickEnginee("267553", "huangbasen", "N");
		assignPickEnginee("267546", "zhangyoumao", "N");
		assignPickEnginee("267526", "huangbasen", "Y");
		assignPickEnginee("267519", "zhangyoumao", "N");*/
		/*completeTask("270024", "miaoyi", "Y");
		completeTask("270019", "fangquan", "Y");*/
		//completeTask("272504", "fengkan", "Y");
	}
}
