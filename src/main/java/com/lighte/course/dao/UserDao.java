package com.lighte.course.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.lighte.course.meta.User;

public interface UserDao {
	
	@Results({
		@Result(property="userPassword", column="password")
	})
	@Select("select * from person where userName=#{userName}")
	public User select(@Param("userName") String userName);
	
}
