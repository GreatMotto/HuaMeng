package com.xckevin.download;

import java.util.List;

public interface DownloadProvider {

	public void saveDownloadTask(DownloadTask task);
	
	public void updateDownloadTask(DownloadTask task);
	
	public void deleteDownloadTask(DownloadTask task);
	
	public DownloadTask findDownloadTaskById(String id);
	
	public DownloadTask findDownloadTask(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);
	
	public List<DownloadTask> getAllDownloadTask();

	public List<DownloadTask> getDownloadTaskByCourseId(String courseId);
	
	public void notifyDownloadStatusChanged(DownloadTask task);

	public List<DownloadTask> getDownloadTaskByFisished(String courseId);

	public void saveCourseInfo(CourseInfo course);

	public void updateCourse(CourseInfo course);

	public void deleteCourse(String id);

	public List<CourseInfo> getCourseByStatus(int status);

	public CourseInfo getCourseById(int id);

	public List<CourseInfo> getCourseByDownloading();
}