package com.fcm.test.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fcm.test.dao.commonDao;

@Service
public class commonService {
	
	@Autowired
	private commonDao commonDao;
	
	public List selectList(Map param){
		List list = commonDao.selectList(param);
		return list;
	}

}
