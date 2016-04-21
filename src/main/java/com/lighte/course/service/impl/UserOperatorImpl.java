package com.lighte.course.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.lighte.course.dao.UserDao;
import com.lighte.course.meta.User;
import com.lighte.course.service.UserOperator;

@Component("userOperator")
public class UserOperatorImpl implements UserOperator {

	@Autowired
	private UserDao dao;
	
	@Override
	public User getUser(String userName) {
		return dao.select(userName);
	}
	
	/**
	 * This is a test program
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		UserOperator verifyPerson = context.getBean("userOperator", UserOperatorImpl.class);
		User user = verifyPerson.getUser("seller");
		if(user != null) {
			System.out.println(user.getId()+":"+user.getUserName()+"-"+user.getUserPassword()+"-"+user.getUserType());
		}
		System.out.println(verifyPerson.getUser("one"));
		((ConfigurableApplicationContext)context).close();
	}

}
