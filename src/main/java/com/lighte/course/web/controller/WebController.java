package com.lighte.course.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lighte.course.meta.Product;
import com.lighte.course.meta.User;
import com.lighte.course.service.ProductOperator;
import com.lighte.course.service.UserOperator;

@Controller
public class WebController {
	
	@Autowired
	private UserOperator userOp;
	
	@Autowired
	private ProductOperator operator;
	
	@RequestMapping("/login")
	public String login(HttpSession session, ModelMap map) {
		//如果从session中获取到了user，就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
		}
		
		return "login";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session, ModelMap map) {
		//如果session中有user，就删除
		if(session.getAttribute("user") != null) {
			session.removeAttribute("user");
		}
		
		//如果从session中获取到了user，就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
		}
		
		return "login";
	}
	
	@RequestMapping("/")
	public String index(HttpSession session, ModelMap map) {
		//如果从session中获取到了user，就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
		}
		
		//如果从数据库中获取到了productList，就传入
		List<Product> productList = this.operator.findMiniProducts();
		if(productList != null) { 
			map.addAttribute("productList", productList); 
		}
		
		return "index";
	}
	
	@RequestMapping("/show")
	public String showOne(@RequestParam("id") int id, HttpSession session,
			ModelMap map) throws UnsupportedEncodingException {
		//如果从session中获取到了user，就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
		}
		
		//如果从数据库中获取到了product，就传入
		Product product = this.operator.findMoreFilds(id); 
		if(product != null) { 
			map.addAttribute("product", product);
		}
		
		return "show";
	}
	
	@RequestMapping("/account")
	public String account(HttpSession session, ModelMap map) {
		//如果从session中获取到了user，就传入
		//同时，根据userId从数据库中查找buyList(包含image),如果有就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
			User userInSession = (User) session.getAttribute("user");
			List<Product> buyList = this.operator.fdUserBuyList(userInSession.getId());
			if(buyList != null) {
				map.put("buyList", buyList);
			}
		}
		
		return "account";
	}
	
	@RequestMapping("/public")
	public String publicItems(HttpSession session, ModelMap map) {
		//如果从session中获取到了user，就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
		}
		
		return "public";
	}
	
	@RequestMapping("/publicSubmit")
	public String publicSubmit(HttpServletRequest request, HttpSession session,
			ModelMap map) throws UnsupportedEncodingException {
		//如果从session中获取到了user，就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
		}
		
		//设置request的解码字符集，并从中获取对应的参数
		request.setCharacterEncoding("utf-8");
		String title = request.getParameter("title");
		String summary = request.getParameter("summary");
		String image = request.getParameter("image");
		String detail = request.getParameter("detail");
		int price = Integer.valueOf(request.getParameter("price"));
		
		//向数据库存入一行数据，同时获取其主键，根据主键查找数据并传入
		int id = this.operator.createProduct(title, summary, image, detail, price); 
		if(id > 0) { 
			Product product = this.operator.findProduct(id); 
			map.addAttribute("product", product);
		}
		
		return "publicSubmit";
	}
	
	@RequestMapping("/edit")
	public String edit(@RequestParam("id") int id, HttpSession session, ModelMap map) {
		//如果从session中获取到了user，就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
		}
		
		//如果从数据库中获取到了product，就传入
		Product product = this.operator.findProduct(id); 
		if(product != null) { 
			map.addAttribute("product", product); 
		}
		
		return "edit";
	}
	
	@RequestMapping("/editSubmit")
	public String editSubmit(HttpServletRequest request, HttpSession session,
			ModelMap map) throws UnsupportedEncodingException {
		//如果从session中获取到了user，就传入
		Map<String, Object> user = isUserInSession(session);
		if(user != null) {
			map.addAttribute("user", user);
		}
		
		//设置request的解码字符集，并从中获取对应的参数
		request.setCharacterEncoding("utf-8"); 
		int id = Integer.valueOf(request.getParameter("id"));
		String title = request.getParameter("title");
		String summary = request.getParameter("summary");
		String image = request.getParameter("image");
		String detail = request.getParameter("detail");
		int price = Integer.valueOf(request.getParameter("price"));
		
		//先根据id向数据库更新一行数据，再根据id查找此行数据并返回
		if(this.operator.changeProduct(id, title, summary, image, detail, price) > 0) { 
			Product product = this.operator.findProduct(id); 
			if(product != null) { 
				map.addAttribute("product", product); 
			}
		}
		
		return "editSubmit";
	}
	
	@RequestMapping("/api/login")
	public Map<String, Object> loginVerify(@RequestParam("userName") String userName,
			@RequestParam("password") String userPassword, HttpSession session) {
		//初始化结果集及相关数据
		Map<String, Object> map = new HashMap<>();
		int code = 0;
		String message = "用户名有误";
		boolean result = false;
		
		User one = userOp.getUser(userName); //查找数据库
		if(one != null) { //如果有对应的数据
			String password = one.getUserPassword(); //获取数据库中的密码
			if(userPassword.equals(password)) { //如果两个密码相同
				code = 200;
				message = "登陆成功";
				result = true;
				//设置有效期30分钟，添加数据
				session.setMaxInactiveInterval(60*30);
				session.setAttribute("user", one);
			}else {
				message = "密码有误";
			}
		}else {
			Map<String, Object> user = isUserInSession(session);
			if(user != null) {
				code = 200;
				message = "欢迎回来";
				result = true;
			}
		}
		
		map.put("code", code);
		map.put("message", message);
		map.put("result", result);
		return map;
	}
	
	@RequestMapping("/api/delete")
	public Map<String, Object> delete(@RequestParam("id") int id) {
		//初始化结果集及相关数据
		Map<String, Object> map = new HashMap<>();
		int code = 0;
		String message = "删除失败";
		boolean result = false;
		
		if(this.operator.deleteProduct(id)) {
			code = 200;
			message = "删除成功";
			result = true;
		}
		
		map.put("code", code);
		map.put("message", message);
		map.put("result", result);
		return map;
	}
	
	@RequestMapping("/api/buy")
	public Map<String, Object> buy(@RequestParam("id") int id, HttpSession session) {
		//初始化结果集及相关数据
		Map<String, Object> map = new HashMap<>();
		int code = 0;
		String message = "购买失败";
		boolean result = false;
		
		int productId = -1;
		int userId = -1;
		int price = -1;
		
		User userInSession = (User) session.getAttribute("user");
		Product product = this.operator.findProduct(id);
		
		if(userInSession != null && product != null) {
			userId = userInSession.getId();
			productId = product.getId();
			price = product.getPrice();
			
			if(this.operator.buy(productId, userId, price, System.currentTimeMillis()) > 0) {
				code = 200;
				message = "购买成功";
				result = true;
			}
		}
		
		map.put("code", code);
		map.put("message", message);
		map.put("result", result);
		return map;
	}
	
	
	/**
	 * 如果是有效session，从数据库查找对应数据，封装进user表并返回
	 * @param request
	 * @return user表；如果session无效或者没有session，返回null
	 */
	private Map<String, Object> isUserInSession(HttpSession session) {
		if(!session.isNew()) { //如果session不是新建的
			User userInSession = (User) session.getAttribute("user");
			if(userInSession != null) {//如果从session中获取的值不为空，即有效session
				Map<String, Object> user = new HashMap<>();
				user.put("username", userInSession.getNickName());
				user.put("usertype", userInSession.getUserType());
				return user;
			}
		}
		
		return null;
	}
	
}
