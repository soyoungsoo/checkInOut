package com.sys.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sys.dao.CheckInOutManageImpl;
import com.sys.dao.EmployeeManageImpl;
import com.sys.model.CheckInOut;
import com.sys.model.Grade;
import com.sys.model.UserInfo;
import com.sys.util.ClientIp;
import com.sys.util.Message;


/**
 * Servlet implementation class test
 */
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String rootPath       = "../";
	private String loginPath      = "/user/login";
	private String updatePath     = "/user/update";	
	private String serverTimePath = "/user/serverTime";
	private String logoutPath     = "/user/logout";
	private String checkPath      = "/user/check";
	private String btnMsgPath     = "/user/btnMsg";
	private String path = "";
	private String ip =   "";

	private UserInfo user         = null;
	private CheckInOut check      = null;
	private HttpSession session   = null;
	private LocalTime currentTime = null;
	private LocalDate today = null;

	private ClientIp ci                      = new ClientIp();
	private Message message                  = new Message();	
	private EmployeeManageImpl employee      = new EmployeeManageImpl();
	private CheckInOutManageImpl checkManage = new CheckInOutManageImpl();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() {
        super(); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			// 전역변수 초기화
			init(request,response);
			// 서버 시간 프론트에 전달
			if (path.equals(serverTimePath)) {
				response.getWriter().print(currentTime);			
			// 로그아웃
			} else if (path.equals(logoutPath)) {
				session.removeAttribute("UserInfo");
				message.sendMsg(response, "정상적으로 로그아웃 하셨습니다.", rootPath);				
			// 출 퇴근 체크
			} else if (path.equals(checkPath)) {		
				String resultMsg = checkQeury("insert");
				if(resultMsg.equals("성공")) message.sendMsg(response, "정상적으로 체크하셨습니다.", rootPath);
				else message.sendMsg(response, "체크에 실패하였습니다.", rootPath);
			// 버튼 텍스트 전달
			} else if (path.equals(btnMsgPath)) {
				if (user == null) return;
				String btnMsg = checkQeury("select");
				if(btnMsg != null) response.getWriter().println(btnMsg);
				else message.sendMsg(response, "오류가 발생하였습니다. 관리자에게 문의하여 주세요.", rootPath);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			// 전역변수 초기화
			init(request,response);

			if (path.equals(loginPath)) {
				List<UserInfo> select = employee.selectList();
				String id = request.getParameter("userId");
				String pw = request.getParameter("userPw");				
				for (UserInfo userInfo : select) {
					if (id.equals(userInfo.getUserId()) && pw.equals(userInfo.getUserPw())){
						session.setAttribute("UserInfo", userInfo);
						session.setMaxInactiveInterval( 60*60 );
						if (id.equals("admin") && pw.equals("ainc0705")) {
							response.getWriter().println("관리자");
							return;
						} else {							
							response.getWriter().println(userInfo.getUserName());
							return;
						}
					} 						
				}	
				response.getWriter().println("미등록회원");
				return;
			} else if(path.equals(updatePath)) {				
				String curPw      = request.getParameter("curPw");
				String changePwd  = request.getParameter("changePwd");				
				UserInfo userInfo = (UserInfo) session.getAttribute("UserInfo");
				
				if(userInfo == null) { 
					message.sendMsg(response, "로그인 후 이용하실 수 있습니다.", rootPath);
					return;
				}

				if (curPw.equals(userInfo.getUserPw())) {
					UserInfo user = new UserInfo();
					Grade grade   = new Grade();
					grade.setGradeIdx(userInfo.getGrade().getGradeIdx());
					user.setUserId(userInfo.getUserId());
					user.setUserName(userInfo.getUserName());
					user.setUserPw(changePwd);
					user.setGrade(grade);
					user.setUserIdx(userInfo.getUserIdx());
					
					employee.update(user);
					message.sendMsg(response, "성공적으로 비밀번호를 변경하셨습니다.", rootPath);
				} else {
					message.sendMsg(response, "비밀번호를 변경하셨습니다.", rootPath);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
	}
	
	private void init(HttpServletRequest request, HttpServletResponse response) throws SQLException, UnknownHostException {

		int length       = request.getContextPath().length();
		this.ip          = ci.getClientIp(request);
		this.path        = request.getRequestURI().substring(length);
		this.session     = request.getSession();
		this.user        = (UserInfo) session.getAttribute("UserInfo");
		this.check       = new CheckInOut();
		this.currentTime = LocalTime.now();
		this.today       = LocalDate.now();
		response.setContentType("text/html; charset=utf-8");				
	}

	private String checkQeury(String type) throws UnknownHostException, ParseException, SQLException {

		String month  = today.getMonthValue()   < 10 ? "0" + today.getMonthValue()   : Integer.toString(today.getMonthValue());
		String day    = today.getDayOfMonth()   < 10 ? "0" + today.getDayOfMonth()   : Integer.toString(today.getDayOfMonth());
	    String hour   = currentTime.getHour()   < 10 ? "0" + currentTime.getHour()   : Integer.toString(currentTime.getHour());	    
	    String minute = currentTime.getMinute() < 10 ? "0" + currentTime.getMinute() : Integer.toString(currentTime.getMinute());
	    String result = "";
	    check.setCheckInYear(Integer.toString(today.getYear()));
		check.setCheckInMonth(month);
		check.setCheckInDay(day);
		check.setUser(user);

	    switch (type) {

			case "select":
				return checkManage.select(check);

			case "insert":
				check.setCheckInHour(hour);
				check.setCheckInMinute(minute);
				check.setCheckInIp(ip);				
				checkManage.Insert(check);
				result ="성공";
				break;
	
			default:
				result = "실패";
				break;
		}
		return result;	    
	}
}