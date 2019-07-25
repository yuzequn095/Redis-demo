package com.user.login.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;

@Controller
@RequestMapping("/redis")
public class LoginController {

	@Autowired
	StringRedisTemplate redis;

	/**
	 * @Description: JSON字符串转MAP
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> toMap(String data) {
		Map<String, String> maps = (Map<String, String>) JSONUtils.parse(data);
		for (Object map : maps.entrySet()) {
			System.out.println(((Map.Entry) map).getKey() + "     " + ((Map.Entry) map).getValue());
		}
		return maps;
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public Object testLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String loginInfo = request.getParameter("loginInfo");
		Map<String, String> user = toMap(loginInfo);
		System.out.println(user.get("userName"));
		if (redis.opsForHash().hasKey("hash:user", user.get("userName"))) {
			if (redis.opsForHash().get("hash:user", user.get("userName")).equals(user.get("userPassword"))) {
				return "Login";
			} else {
				return "Incorrect pwd";
			}
		}

		return "user-name doesn't exist";
	}
	// GET
	// public String testLogin(@RequestParam(value = "userName", required =
	// true) String userName,
	// @RequestParam(value = "userPassword", required = true) String
	// userPassword) {
	//
	// if (redis.opsForHash().hasKey("hash:user", userName)) {
	// if (redis.opsForHash().get("hash:user", userName).equals(userPassword)) {
	// return "Login";
	// } else {
	// return "Incorrect password";
	// }
	// }
	//
	// return "User-name doesn't exist";
	// }

}
