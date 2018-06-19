package com.fcm.test.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class commonDao {

	@Autowired
	@Resource(name="sqlSession")
	private SqlSessionTemplate sqlSession;
	
	@SuppressWarnings("unchecked")
	public List selectList(Map param){
		List list = sqlSession.selectList("fcmsql.selectList", param);
		return list;
	}
}
