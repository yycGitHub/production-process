package com.surekam.modules.agro.videomain.entity;

import java.util.List;

public class SnapshotByTimeOffset {
	private String TaskId;
	private String FileId;
	private String Definition;
	private List<SnapshotInfo> SnapshotInfoSet;
	public String getTaskId() {
		return TaskId;
	}
	public void setTaskId(String taskId) {
		TaskId = taskId;
	}
	public String getFileId() {
		return FileId;
	}
	public void setFileId(String fileId) {
		FileId = fileId;
	}
	public String getDefinition() {
		return Definition;
	}
	public void setDefinition(String definition) {
		Definition = definition;
	}
	public List<SnapshotInfo> getSnapshotInfoSet() {
		return SnapshotInfoSet;
	}
	public void setSnapshotInfoSet(List<SnapshotInfo> snapshotInfoSet) {
		SnapshotInfoSet = snapshotInfoSet;
	}
	
	
}
