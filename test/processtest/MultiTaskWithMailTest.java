package processtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.tplink.demo.activiti.service.BpmService;

import base.TestTool;
import basetest.TestBPMService;

public class MultiTaskWithMailTest {
	private static BpmService bpmService = TestTool.getBean("bpmService", BpmService.class);

	public static void submit() {
		TestBPMService.testSubmit("MultiTaskWithMail", getSubmitVariables());
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
		approverList.add("anfeng");
		variables.put("approverList", approverList);
		return variables;
	}
	
	public static void completeTask(String taskId, String assignee, String result) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("result", result);
		if ("N".equals(result)) {
			variables.put("reverter", assignee);
		}
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	public static void completeApproveTasks(String instId, boolean hasFail) {
		List<Task> taskList = bpmService.searchTaskByInst(instId);
		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if (hasFail && i % 2 == 1) {
				completeTask(task.getId(), task.getAssignee(), "N");
			} else {
				completeTask(task.getId(), task.getAssignee(), "Y");
			}
		}
	}
	
	public static void main(String[] args) {
		//TestBPMService.testDeploy("MultiTaskWithMail", "MultiTaskWithMail.bpmn20.xml");
		//submit();
		completeApproveTasks("147501", true);
		//submitAgain("147501");
		//completeApproveTasks("125005", false);
		//bpmService.deleteInstance("145005");
	}
}
