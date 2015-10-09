package com.xckevin.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


public class SqlLiteDownloadProvider implements DownloadProvider {
	
	private static SqlLiteDownloadProvider instance;
	
	private DownloadManager manager;
	
	private String DOWNLOAD_TABLE = "tb_download";

	private SQLiteDatabase db;
	
	private SqlLiteDownloadProvider(DownloadManager manager) {
		this.manager = manager;
		File dbFile = new File(manager.getConfig().getDownloadSavePath()+File.separator+"db", "download.db");
		if(dbFile.exists()) {
			db = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
		} else {
			if(!dbFile.getParentFile().isDirectory()) {
				dbFile.getParentFile().mkdirs();
			}
			try {
				dbFile.createNewFile();
				db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
//				createCourseTable();
				createTables();
			} catch (IOException e) {
				e.printStackTrace();
//				throw new IllegalAccessError("cannot create database file of path: " + dbFile.getAbsolutePath());
				Log.e("download:","cannot create database file of path: " + dbFile.getAbsolutePath());
			}
		}
	}
	
	public static synchronized SqlLiteDownloadProvider getInstance(DownloadManager manager) {
		if(instance == null) {
			instance = new SqlLiteDownloadProvider(manager);
		}
		
		return instance;
	}
	
	private void createTables() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CREATE TABLE IF NOT EXISTS ").append(DOWNLOAD_TABLE);
		buffer.append("(");
		buffer.append("`").append(DownloadTask.ID).append("` VARCHAR PRIMARY KEY,");
		buffer.append("`").append(DownloadTask.URL).append("` VARCHAR,");
		buffer.append("`").append(DownloadTask.MIMETYPE).append("` VARCHAR,");
		buffer.append("`").append(DownloadTask.SAVEPATH).append("` VARCHAR,");
		buffer.append("`").append(DownloadTask.NAME).append("` VARCHAR,");
		buffer.append("`").append(DownloadTask.FINISHEDSIZE).append("` LONG,");
		buffer.append("`").append(DownloadTask.TOTALSIZE).append("` LONG,");
		buffer.append("`").append(DownloadTask.STATUS).append("` int,");
		buffer.append("`").append(DownloadTask.COURSE_ID).append("` VARCHAR,");
		buffer.append("`").append(DownloadTask.STREAM_PATH).append("` VARCHAR");
		buffer.append(")");
		db.execSQL(buffer.toString());

		db.execSQL("CREATE TABLE IF NOT EXISTS courseinfo(`id` INTEGER PRIMARY KEY AUTOINCREMENT,`name` varchar(100),`imagePath` varchar(500),`hasCacheCount` INTEGER,`courseInfo` varchar(10000))");
	}

	private void createCourseTable() {
		db.execSQL("CREATE TABLE IF NOT EXISTS courseinfo(id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(100),imagePath varchar(500),hasCacheCount INTEGER,courseInfo varchar(10000))");
	}

	public void saveDownloadTask(DownloadTask task) {
		ContentValues values = createDownloadTaskValues(task);
		db.insert(DOWNLOAD_TABLE, null, values);
		
		notifyDownloadStatusChanged(task);
	}
	
	public void updateDownloadTask(DownloadTask task) {
		ContentValues values = createDownloadTaskValues(task);
		db.update(DOWNLOAD_TABLE, values, DownloadTask.ID + "=?", new String[]{task.getId()});
		
		notifyDownloadStatusChanged(task);
	}
	
	public DownloadTask findDownloadTaskById(String id) {
		if(TextUtils.isEmpty(id)) {
			return null;
		}
		DownloadTask task = null;
		Cursor cursor = db.query(DOWNLOAD_TABLE, null, DownloadTask.ID + "=?", new String[]{id}, null, null, null);
		if(cursor.moveToNext()) {
			task = restoreDownloadTaskFromCursor(cursor);
		}
		cursor.close();
		
		return task;
	}
	
	public void deleteDownloadTask(DownloadTask task){
		db.delete(DOWNLOAD_TABLE, DownloadTask.ID + "=?", new String[]{task.getId()});
		notifyDownloadStatusChanged(task);
	}
	
	public DownloadTask findDownloadTask(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		DownloadTask task = null;
		Cursor cursor = db.query(DOWNLOAD_TABLE, columns, selection, selectionArgs, groupBy, having, orderBy);
		if(cursor.moveToNext()) {
			task = restoreDownloadTaskFromCursor(cursor);
		}
		cursor.close();
		
		return task;
	}
	
	public void notifyDownloadStatusChanged(DownloadTask task) {
		manager.notifyDownloadTaskStatusChanged(task);
	}
	
	public List<DownloadTask> getAllDownloadTask() {
		List<DownloadTask> list = new ArrayList<DownloadTask>();
		DownloadTask task = null;
		Cursor cursor = db.query(DOWNLOAD_TABLE, null, null, null, null, null, DownloadTask.STATUS);
		while(cursor.moveToNext()) {
			task = restoreDownloadTaskFromCursor(cursor);
			list.add(task);
		}
		cursor.close();
		return list;
	}

	@Override
	public List<DownloadTask> getDownloadTaskByCourseId(String courseId) {
		List<DownloadTask> list = new ArrayList<DownloadTask>();
		DownloadTask task = null;
		Cursor cursor = db.query(DOWNLOAD_TABLE, null, DownloadTask.STATUS + "!=? and "+DownloadTask.STATUS + "!=? and "+DownloadTask.STATUS + "!=? and " +DownloadTask.COURSE_ID +"=?", new String[]{String.valueOf(DownloadTask.STATUS_CANCELED),String.valueOf(DownloadTask.STATUS_ERROR),String.valueOf(DownloadTask.STATUS_FINISHED),courseId}, null, null, DownloadTask.STATUS);
		while(cursor.moveToNext()) {
			task = restoreDownloadTaskFromCursor(cursor);
			list.add(task);
		}
		cursor.close();
		return list;
	}

	public List<DownloadTask> getDownloadTaskByFisished(String courseId) {
		List<DownloadTask> list = new ArrayList<DownloadTask>();
		DownloadTask task = null;
		Cursor cursor = db.query(DOWNLOAD_TABLE, null, DownloadTask.STATUS + "=? and " +DownloadTask.COURSE_ID +"=?", new String[]{String.valueOf(DownloadTask.STATUS_FINISHED),courseId}, null, null, DownloadTask.STATUS);
		while(cursor.moveToNext()) {
			task = restoreDownloadTaskFromCursor(cursor);
			list.add(task);
		}
		cursor.close();
		return list;
	}

	@Override
	public void saveCourseInfo(CourseInfo courseInfo) {
		db.beginTransaction();
		try {
			String sql = "INSERT INTO courseinfo(id,name,imagePath,hasCacheCount,courseInfo) VALUES ("+courseInfo.getId()+",'"+courseInfo.getName()+"','"+courseInfo.getImagePath()+"',"+courseInfo.getHasCacheCount()+",'"+courseInfo.getCourseInfo()+"')";
			db.execSQL(sql);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void updateCourse(CourseInfo courseInfo) {
		db.beginTransaction();
		try {
			db.execSQL("update courseinfo set hasCacheCount="+courseInfo.getHasCacheCount()+" where id="+courseInfo.getId());

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void deleteCourse(String id) {
		db.beginTransaction();
		try {
			String sql = "delete from courseinfo where id = "+id;
			db.execSQL(sql);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public List<CourseInfo> getCourseByStatus(int status) {
		Cursor c = db.rawQuery("SELECT distinct(c.id),c.name,c.imagePath,c.hasCacheCount,c.courseInfo FROM courseinfo as c inner join "+DOWNLOAD_TABLE+" as d where c.id = d._course_id and d._status="+status, null);
		LinkedList<CourseInfo> list = new LinkedList<CourseInfo>();
		while (c.moveToNext()) {
			CourseInfo info = new CourseInfo();
			info.setId(c.getInt(c.getColumnIndex("id")));
			info.setName(c.getString(c.getColumnIndex("name")));
			info.setImagePath(c.getString(c.getColumnIndex("imagePath")));
			info.setHasCacheCount(c.getInt(c.getColumnIndex("hasCacheCount")));
			info.setCourseInfo(c.getString(c.getColumnIndex("courseInfo")));
			list.add(info);
		}
		c.close();
		return list;
	}

	@Override
	public CourseInfo getCourseById(int id) {
		Cursor c = db.rawQuery("SELECT * FROM courseinfo where id=" + id, null);
		CourseInfo info = new CourseInfo();
		while (c.moveToNext()) {
			info.setId(c.getInt(c.getColumnIndex("id")));
			info.setName(c.getString(c.getColumnIndex("name")));
			info.setImagePath(c.getString(c.getColumnIndex("imagePath")));
			info.setHasCacheCount(c.getInt(c.getColumnIndex("hasCacheCount")));
			info.setCourseInfo(c.getString(c.getColumnIndex("courseInfo")));
		}
		c.close();
		return info;
	}

	@Override
	public List<CourseInfo> getCourseByDownloading() {
		Cursor c = db.rawQuery("SELECT distinct(c.id),c.name,c.imagePath,c.hasCacheCount,c.courseInfo FROM courseinfo as c inner join "+DOWNLOAD_TABLE+" as d where c.id = d._course_id and d._status="+DownloadTask.STATUS_RUNNING+" or d._status="+DownloadTask.STATUS_PAUSED+" or d._status="+DownloadTask.STATUS_PENDDING+" order by d._status desc", null);
		LinkedList<CourseInfo> list = new LinkedList<CourseInfo>();
		while (c.moveToNext()) {
			CourseInfo info = new CourseInfo();
			info.setId(c.getInt(c.getColumnIndex("id")));
			info.setName(c.getString(c.getColumnIndex("name")));
			info.setImagePath(c.getString(c.getColumnIndex("imagePath")));
			info.setHasCacheCount(c.getInt(c.getColumnIndex("hasCacheCount")));
			info.setCourseInfo(c.getString(c.getColumnIndex("courseInfo")));
			list.add(info);
		}
		c.close();
		return list;
	}

	private ContentValues createDownloadTaskValues(DownloadTask task) {
		ContentValues values = new ContentValues();
		values.put(DownloadTask.ID, task.getId());
		values.put(DownloadTask.URL, task.getUrl());
		values.put(DownloadTask.MIMETYPE, task.getMimeType());
		values.put(DownloadTask.SAVEPATH, task.getDownloadSavePath());
		values.put(DownloadTask.FINISHEDSIZE, task.getDownloadFinishedSize());
		values.put(DownloadTask.TOTALSIZE, task.getDownloadTotalSize());
		values.put(DownloadTask.NAME, task.getName());
		values.put(DownloadTask.STATUS, task.getStatus());
		values.put(DownloadTask.COURSE_ID,task.getCourseId());
		values.put(DownloadTask.STREAM_PATH,task.getStreamingPath());
		
		return values;
	}
	
	private DownloadTask restoreDownloadTaskFromCursor(Cursor cursor) {
		DownloadTask task = new DownloadTask();
		task.setId(cursor.getString(cursor.getColumnIndex(DownloadTask.ID)));
		task.setName(cursor.getString(cursor.getColumnIndex(DownloadTask.NAME)));
		task.setUrl(cursor.getString(cursor.getColumnIndex(DownloadTask.URL)));
		task.setMimeType(cursor.getString(cursor.getColumnIndex(DownloadTask.MIMETYPE)));
		task.setDownloadSavePath(cursor.getString(cursor.getColumnIndex(DownloadTask.SAVEPATH)));
		task.setDownloadFinishedSize(cursor.getLong(cursor.getColumnIndex(DownloadTask.FINISHEDSIZE)));
		task.setDownloadTotalSize(cursor.getLong(cursor.getColumnIndex(DownloadTask.TOTALSIZE)));
		task.setStatus(cursor.getInt(cursor.getColumnIndex(DownloadTask.STATUS)));
		task.setCourseId(cursor.getString(cursor.getColumnIndex(DownloadTask.COURSE_ID)));
		task.setStreamingPath(cursor.getString(cursor.getColumnIndex(DownloadTask.STREAM_PATH)));
		return task;
	}
}
