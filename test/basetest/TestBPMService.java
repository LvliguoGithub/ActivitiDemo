package basetest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.DiagramNode;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.tplink.demo.activiti.service.BpmService;

import base.TestTool;

public class TestBPMService {
	private final static String PROCESS_BASR_DIR = "E:/DocumentsandSettings/Workspaces/Eclipse/ActivitiDemo/test/process/";
	private static BpmService bpmService = TestTool.getBean("bpmService", BpmService.class);
	
	
	public static void testDeploy(String xmlName, String deploymentName) {
		bpmService.deploy(PROCESS_BASR_DIR + xmlName + ".bpmn20.xml", deploymentName, "Demo");
	}
	
	public static void testUnDeploy(String deploymentId){
		bpmService.unDeploy(deploymentId, "Demo");
	}
	
	public static void testUnDeployByKey(String key, Integer ver) {
		bpmService.unDeplyByKeyAndVer(key, ver, "Demo");
	}
	
	private static InputStream getDiagram(String type, String id) {
		if ("Instance".equals(type)) {
			return bpmService.getDefineDiagramByInstance(id);
		} else {
			return bpmService.getDefineDiagram(id);
		}
	}
	
	public static void testGetDiagram(String type, String id, String fileName) {
		InputStream is = getDiagram(type, id);
		try (FileOutputStream fos = new FileOutputStream(new File(fileName));
				){
			byte[] buffer = new byte[1024];
			while (is.read(buffer) > -1){
				fos.write(buffer, 0, buffer.length);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static ProcessInstance testStartInst(String key) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("person", "chenxinjie");
		ProcessInstance inst = bpmService.startInstanceByKey(key, "Demo", variables);
		System.out.println(inst.getProcessInstanceId());
		return inst;
	}
	
	public static void testsearchProcessDefine(String filter){
		List<ProcessDefinition> list = bpmService.searchProcessDefine(0, 2, "Demo", filter);
		for (ProcessDefinition d : list){
			System.out.println(d.getId() + ":" + d.getKey());
		}
	}
	
	public static void testFindActivityCoordinates(String instId){
		List<DiagramNode> nodes = bpmService.findActivityCoordinates(instId);
		for (DiagramNode node : nodes) {
			printNode(node);
		}
	}
	
	private static void printNode(DiagramNode node ){
		System.out.println(node.getId() + ", X:" + node.getX()  
				+ ", Y:" + node.getY() + ", Width:" + node.getWidth() 
				+ ", Height:" + node.getHeight());
	}
	
	public static void testFindCoordinates(String exeId) {
		DiagramNode node = bpmService.findCoordinates(exeId);
		if (node != null) {
			printNode(node);
		}
	}
	
	public static void testSearchUserTask() {
		List<Task> taskList = bpmService.searchUserTask(0, 2);
		printList(taskList);
	}
	
	public static void testSearchUserTaskByUser(String assignee) {
		List<Task> taskList = bpmService.searchUserTaskByUser(0, 2, assignee);
		printList(taskList);
	}
	
	private static void printList(List<Task> taskList){
		for (Task task : taskList){
			System.out.println(task.getId() + "," + task.getAssignee());
		}
	}
	
	public static void testCompleteTask(String taskId, String assignee) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("approver", "chenxinjie");
		variables.put("result", "Y");
		bpmService.completeTask(taskId, assignee, variables);
	}
	
	public static void testSubmit(String key, Map<String, Object> variables) {
		ProcessInstance instance = testStartInst(key);
		List<Task> taskList = bpmService.searchTaskByInst(instance.getProcessInstanceId());
		for (Task task : taskList) {
			bpmService.completeTask(task.getId(), task.getAssignee(), variables);
		}
	}
	
	public static void main(String[] args) {
		testDeploy("MultiTask", "MultiTask.bpmn20.xml");
		//testUnDeploy("72501");
		//testUnDeployByKey("UserTask", null);
		//testGetDiagram("DefineId", "UserTask:1:30004", "G:/demo.png");
		//testStartInst("UserTaskWithVacation");
		//testGetDiagram("Instance", "32501", "G:/demo2.png");
		//testsearchProcessDefine(null);
		//testFindActivityCoordinates("32501");
		//testFindCoordinates("32501");
		//testSearchUserTask();
		//testSearchUserTaskByUser("chenxinjie");
		//testCompleteTask("110002", "chenxinjie");
		//testCompleteTask("110005", "chenxinjie");
		
		/*Map<String, Object> variables = new HashMap<>();
		List<String> approverList = new ArrayList<>();
		approverList.add("chenxinjie");
		approverList.add("zhangyoumao");
		approverList.add("anfeng");
		variables.put("approverList", approverList);
		testSubmit("MultiTask", variables);*/
	}
	
	
}
