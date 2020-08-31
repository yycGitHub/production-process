package com.surekam.modules.common.service;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.surekam.common.persistence.Page;
import com.surekam.common.service.BaseService;
import com.surekam.modules.common.dao.FileInfoDao;
import com.surekam.modules.common.entity.FileInfo;

/**
 * 文件信息Service
 * @author l
 * @version 2017-09-30
 */
@Component
@Transactional(readOnly = true)
public class FileInfoService extends BaseService {

	@Autowired
	private FileInfoDao fileInfoDao;
	
	public FileInfo get(String id) {
		return fileInfoDao.get(id);
	}
	
	public Page<FileInfo> find(Page<FileInfo> page, FileInfo fileInfo) {
		DetachedCriteria dc = fileInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ne(FileInfo.FIELD_DEL_FLAG, FileInfo.DEL_FLAG_NORMAL));
		return fileInfoDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public void save(FileInfo fileInfo) {
		fileInfoDao.save(fileInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(String root,String id) {
		FileInfo fileInfo = fileInfoDao.get(id);
		if(fileInfo!=null){
			File file = new File(root+fileInfo.getAbsolutePath());  
		    // 路径为文件且不为空则进行删除  
		    if (file.isFile() && file.exists()) {  
		        file.delete();  
		    }
			fileInfoDao.deleteById(id);
		}
	}
	
	/**
	 * 根据业务表主键ID和类型查找对应的所有附件
	 * @param ywzbId
	 * @param ywzbType
	 * @param fieldMark 字段标记
	 * @return
	 */
	public List<FileInfo> findAllfilesByYwb(String ywzbId, String ywzbType,String fieldMark) {
		DetachedCriteria dc = fileInfoDao.createDetachedCriteria();
		dc.add(Restrictions.ne("delFlag", FileInfo.DEL_FLAG_DELETE));
		dc.add(Restrictions.eq("ywzbId", ywzbId));
		dc.add(Restrictions.eq("ywzbType", ywzbType));
		if (StringUtils.isNotBlank(fieldMark)) {
			dc.add(Restrictions.eq("fieldMark", fieldMark));
		}
		return fileInfoDao.find(dc);
	}
}
