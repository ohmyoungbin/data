package com.fcm.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fcm.test.service.commonService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private commonService commonService;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "push.do")
//	public String test(Model model, @RequestParam(value = "message") String message) throws IOException {
	public String test(Model model, HttpServletRequest request) throws IOException {

		// Map map = new HashMap();
		// List list = commonService.selectList(map);
		//
		// logger.info("결과----------------------"+list);
		//
		// model.addAttribute("list", list);
		// return "home";

//		List<MobileTokenVO> tokenList = fcmService.loadFCMInfoList(vo);
//
//		String token = tokenList.get(count).getDEVICE_ID();
		
		final String apiKey = "AAAAUuqjwnE:APA91bG1NiXIqOmVLlL2LMMwNtlO7OAaO7y-rvliXgDwJXJ9rWMsQuNfVhwdnjpx40P7Yhkpu-lwh55kj3c4oNEBCyGDqyJn0Cv-DHWvaIlmzucKQBnWPNGr2_OF47nMEwejWb2oTBps";
		URL url = new URL("https://fcm.googleapis.com/fcm/send");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "key=" + apiKey);

		conn.setDoOutput(true);

		//String userId = (String) request.getSession().getAttribute("ssUserId");

		// 이렇게 보내면 주제를 ALL로 지정해놓은 모든 사람들한테 알림을 날려준다.
		String input = "{\"notification\" : {\"title\" : \"여기다 제목 넣기 \", \"body\" : \""+request.getParameter("message")+"\"}, \"to\":\"/topics/ALL\"}";

		// 이걸로 보내면 특정 토큰을 가지고있는 어플에만 알림을 날려준다 위에 둘중에 한개 골라서 날려주자
		//String input = "{\"notification\" : {\"title\" : \" 여기다 제목넣기 \", \"body\" : \"여기다 내용 넣기\"}, \"to\":\" 여기가 받을 사람 토큰  \"}";

		OutputStream os = conn.getOutputStream();

		// 서버에서 날려서 한글 깨지는 사람은 아래처럼 UTF-8로 인코딩해서 날려주자
		os.write(input.getBytes("UTF-8"));
		os.flush();
		os.close();

		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + input);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		// print result
		System.out.println(response.toString());

		return "home";

	}

	@RequestMapping(value = "t.do")
	public ModelAndView t() {

		Map map = new HashMap();
		ModelAndView mav = new ModelAndView();
		List list = commonService.selectList(map);

		logger.info("결과-------------------111---" + list);

		mav.addObject("list", list);
		mav.setViewName("home");
		return mav;
	}

}
