package processtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.tplink.demo.activiti.service.BpmService;

import base.TestTool;
import basetest.TestBPMService;

public class SubProcessTest {
	private static BpmService bpmService = TestTool.getBean("bpmService", BpmService.class);
	
	public static void submit() {
		TestBPMService.testSubmit("SubProcess", getSubmitVariables());
	}
	
	public static void submitAgain(String instId) {
		List<Task> taskList = bpmService.searchTaskByInst(instId);
		for (Task task : taskList) {
			bpmService.completeTask(task.getId(), task.getAssignee(), getSubmitVariables());
		}
	}
	
	private static Map<String, Object> getSubmitVariables() {
		Map<String, Object> variables = new HashMap<>();
		List<String> approverList = new ArrayList<>();
		approverList.add("chenxinjie");
		approverList.add("zhangyoumao");
		variables.put("approverList", approverList);
		variables.put("boss", "liugong");
		return variables;
	}
	
	public static void firstApprove(String instId, boolean hasFail) {
		List<Task> taskList = bpmService.searchTaskByInst(instId);
		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if (hasFail && i == taskList.size() - 1) {
				completeTask(task.getId(), task.getAssignee(), "N", null);
			} else {
				completeTask(task.getId(), task.getAssignee(), "Y", "anfeng");
			}
		}
	}
	
	public static void completeTask(String taskId, String assignee, 
			String result, String secondApprover) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", result);
		if ("Y".equals(result)) {
			variables.put("secondApprover", secondApprover);
		} else {
			variables.put("reverter", assignee);
		}
		variables.put("boss", "liugong");
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	public static void sencondApprove(String instId, boolean hasFail) {
		List<Task> taskList = bpmService.searchTaskByInst(instId);
		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if (hasFail && i == taskList.size() - 1) {
				completeTask(task.getId(), task.getAssignee(), "N");
			} else {
				completeTask(task.getId(), task.getAssignee(), "Y");
			}
		}
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
	
	public static void main(String[] args) {
		//TestBPMService.testDeploy("SubProcess", "SubProcess.bpmn20.xml");
		//submit();
		//firstApprove("182501", false);
		//sencondApprove("182501", false);
		//completeTask("175005", "liugong", "Y");
		//completeTask("185005", "anfeng", "N");
		//submitAgain("182501");
		//completeTask("200005", "liugong", "N");
	}
}
