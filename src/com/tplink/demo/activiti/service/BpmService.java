package com.tplink.demo.activiti.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.activiti.engine.repository.DiagramNode;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 
 * 参数字段统一说明：
 * 		deploymentId: 部署Id(deployment表的id字段)
 * 		key:		     流程Key(procdef表中的key字段)
 * 		defineId:	     流程定义Id(procdef表的Id字段)
 * 		instId:	               流程Id(execution中的procInstId字段)
 * 		exeId:		     分支Id(execution中的Id字段)
 * 		appName:	  tenantId
 */
public interface BpmService {
	

	/**
	 * 部署流程
	 * @param processFilePath	流程文件路径(文件全路径)
	 * @param deploymentName	部署的流程文件名(必须以bpmn20.xml结尾)
	 * @param appName			项目名称(tenantId)
	 */
	void deploy(String processFilePath, String deploymentName, String appName);
	
	/**
	 * 删除流程部署
	 * @param deploymentId	部署Id(deployment表的id字段)
	 * @param appName
	 */
	void unDeploy(String deploymentId, String appName);
	
	/**
	 * 根据流程定义Key删除流程
	 * @param key			流程Key(procdef表中的key字段)
	 * @param version		版本，null时为最新版本
	 * @param appName		项目名称
	 */
	void unDeplyByKeyAndVer(String key, Integer version, String appName);
	
	/**
	 * 查找流程图
	 * @param defineId	流程定义Id(procdef表的Id字段)
	 * @return
	 */
	InputStream getDefineDiagram(String defineId);
	
	/**
	 * 根据流程Id获取流程图
	 * @param instId	流程Id(execution中的procInstId字段)
	 * @return
	 */
	InputStream getDefineDiagramByInstance(String instId);
	
	/**
	 * 获取当前流程活动的坐标位置
	 * @param instId	
	 * @return
	 */
	List<DiagramNode> findActivityCoordinates(String instId);
	
	/**
	 * 查找当前分支的坐标位置
	 * @param exeId
	 * @return
	 */
	DiagramNode findCoordinates(String exeId);
	
	/**
	 * 分页查找流程定义
	 * @param start
	 * @param limit
	 * @param appName
	 * @param filter
	 * @return
	 */
	List<ProcessDefinition> searchProcessDefine(int start, int limit,  String appName, String filter);
	
	/**
	 * 启动流程
	 * @param key			
	 * @param appName
	 * @param variables		参数列表
	 * @return
	 */
	ProcessInstance startInstanceByKey(String key, 
			String appName, Map<String, Object> variables);
	
	/**
	 * 根据Id查找流程
	 * @param instId
	 * @return
	 */
	ProcessInstance findInstanceById(String instId);
	
	/**
	 * 删除流程实例
	 * @param instId
	 */
	void deleteInstance(String instId);
	
	/**
	 * 根据任务Id查找任务
	 * @param taskId
	 * @return
	 */
	Task findTaskById(String taskId);
	
	/**
	 * 分页查找用户任务
	 * @param start
	 * @param limit
	 * @return
	 */
	List<Task> searchUserTask(int start, int limit);
	
	/**
	 * 分页查找单个用户的任务
	 * @param start
	 * @param limit
	 * @param assignee	任务分配人
	 * @return
	 */
	List<Task> searchUserTaskByUser(int start, int limit, String assignee);
	
	/**
	 * 查找流程实例的任务
	 * @param instId
	 * @return
	 */
	List<Task> searchTaskByInst(String instId);
	
	/**
	 * 完成任务
	 * @param taskId
	 * @param assignee
	 * @param variables
	 */
	void completeTask(String taskId, String assignee, Map<String, Object> variables);
}
