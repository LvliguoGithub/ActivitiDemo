package com.tplink.demo.activiti.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.DiagramLayout;
import org.activiti.engine.repository.DiagramNode;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tplink.demo.activiti.service.BpmService;


@Service("bpmService")
public class BpmServiceImpl implements BpmService {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	private Logger log = LoggerFactory.getLogger(BpmServiceImpl.class);
	
	public BpmServiceImpl() {
	}

	@Override
	public void deploy(String processFilePath, String deploymentName, String appName) {
		log.debug("deploymenting file: [{}]  and appName is [{}]", processFilePath , appName);
		File file = new File(processFilePath);
		if (file.exists()) {
			try {
				DeploymentBuilder builder = repositoryService.createDeployment().
						addInputStream(deploymentName, new FileInputStream(file));
				if (StringUtils.isNotBlank(appName)) {
					builder.tenantId(appName);
				}
				builder.deploy();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else {
			log.error("process file [{}] not found", processFilePath);
		}
	}

	@Override
	public void unDeploy(String deploymentId, String appName) {
		DeploymentQuery query = repositoryService.createDeploymentQuery();
		if (StringUtils.isNotBlank(appName)) {
			query.deploymentTenantId(appName);
		}
		List<Deployment> deploymentList = query.deploymentId(deploymentId).list();
		if (deploymentList.size() == 0) {
			log.error("Could not find a deployment with id [{}]", deploymentId);
		} else {
			repositoryService.deleteDeployment(deploymentList.get(0).getId(), true);
		}
	}
	
	@Override
	public void unDeplyByKeyAndVer(String key, Integer version, String appName) {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		if (StringUtils.isNotBlank(appName)) {
			query.processDefinitionTenantId(appName);
		}
		query.processDefinitionKey(key);
		if (version == null) {
			query.orderByProcessDefinitionVersion().desc();
		} else {
			query.processDefinitionVersion(version);
		}
		List<ProcessDefinition> definitionList = query.list();
		if (definitionList.size() == 0) {
			log.error("Could not find a deployment with key [{}]", key);
		} else {
			repositoryService.deleteDeployment(definitionList.get(0).getDeploymentId(), true);
		}
	}
	
	@Override
	public ProcessInstance findInstanceById(String instId) {
		return runtimeService.createProcessInstanceQuery().
				processInstanceId(instId).singleResult();
	}

	@Override
	public InputStream getDefineDiagram(String defineId) {
		return repositoryService.getProcessDiagram(defineId);
	}

	@Override
	public InputStream getDefineDiagramByInstance(String instId) {
		ProcessInstance inst = findInstanceById(instId);
		if (inst == null) {
			log.error("Could not find instance with Id [{}]", instId);
			return null;
		} else {
			return getDefineDiagram(inst.getProcessDefinitionId());
		}
	}

	@Override
	public List<DiagramNode> findActivityCoordinates(String instId) {
		ProcessInstance inst = findInstanceById(instId);
		if (inst == null){
			log.error("Could not find instance with Id [{}]", instId);
			return null;
		} else {
			DiagramLayout layout = repositoryService.getProcessDiagramLayout(inst.getProcessDefinitionId());
			List<Execution> exeList = runtimeService.createExecutionQuery().processInstanceId(instId).list();
			List<DiagramNode> activityNodes = new ArrayList<>();
			for (Execution exe : exeList) {
				List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
				for (String id : ids){
					DiagramNode node = layout.getNode(id);
					if (node != null) {
						activityNodes.add(node);
					}
				}
			}
			return activityNodes;
		}
	}
	
	@Override
	public DiagramNode findCoordinates(String exeId) {
		Execution exe = runtimeService.createExecutionQuery().executionId(exeId).singleResult();
		if (exe == null) {
			log.error("Could not find Execution with Id [{}]", exeId);
			return null;
		} else {
			ProcessInstance inst = findInstanceById(exe.getProcessInstanceId());
			DiagramLayout layout = repositoryService.getProcessDiagramLayout(inst.getProcessDefinitionId());
			return layout.getNode(exe.getActivityId());
		}
	}

	@Override
	public List<ProcessDefinition> searchProcessDefine(int start, int limit, String appName, String filter) {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		if (StringUtils.isNotBlank(appName)) {
			query.processDefinitionTenantId(appName);
		}
		if (StringUtils.isNotBlank(filter)) {
			query.processDefinitionKeyLike(filter);
		}
		return query.orderByProcessDefinitionId().desc().listPage(start, limit);
	}
	
	@Override
	public ProcessInstance startInstanceByKey(String key, String appName, Map<String, Object> variables) {
		if (StringUtils.isNotBlank(appName)) {
			return runtimeService.startProcessInstanceByKeyAndTenantId(key, variables, appName);
		} else {
			return runtimeService.startProcessInstanceByKey(key, variables);
		}
	}

	@Override
	public void deleteInstance(String instId) {
		runtimeService.deleteProcessInstance(instId, null);
	}
	
	@Override
	public Task findTaskById(String taskId) {
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	@Override
	public List<Task> searchUserTask(int start, int limit) {
		return taskService.createTaskQuery().orderByTaskCreateTime()
				.desc().listPage(start, limit);
	}
	
	@Override
	public List<Task> searchTaskByInst(String instId) {
		return taskService.createTaskQuery().processInstanceId(instId).list();
	}

	@Override
	public List<Task> searchUserTaskByUser(int start, int limit, String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee)
				.orderByTaskCreateTime()
				.desc().listPage(start, limit);
	}
	
	@Override
	public void completeTask(String taskId, String assignee, Map<String, Object> variables) {
		Task task = findTaskById(taskId);
		if (task == null) {
			log.error("Could not find Task with Id [{}]", taskId);
		} else {
			if (!assignee.equals(task.getAssignee())) {
				log.error("{} is not the Assignee of this task", assignee);
			} else {
				if (variables == null) {
					taskService.complete(taskId);
				} else {
					taskService.complete(taskId, variables);
				}
			}
		}
	}
	
}
