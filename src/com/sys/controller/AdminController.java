package com.sys.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.sys.dao.CheckInOutManageImpl;
import com.sys.dao.EmployeeManageImpl;
import com.sys.dao.GradeManageImpl;
import com.sys.model.CheckInOut;
import com.sys.model.Grade;
import com.sys.model.UserInfo;
import com.sys.util.ClientIp;
import com.sys.util.TimeCheck;
import com.sys.util.utilDay;

/**
 * Servlet implementation class a
 */
@SuppressWarnings("unchecked")
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String employeeListPath     = "/admin/employeeList";
	private String employeeAddPth       = "/admin/employeeAdd";
	private String employeeUpdatePath   = "/admin/employeeUpdate";
	private String employeeDeletePath   = "/admin/employeeDelete";
	private String gradeListPath        = "/admin/gradeList";
	private String gradeAddPath         = "/admin/gradeAdd";
	private String gradeUpdatePath      = "/admin/gradeUpdate";
	private String gradeDeletePath      = "/admin/gradeDelete";
	private String checkInOutSelectPath = "/admin/checkInOutSelect";
	private String checkInOutAddPath    = "/admin/checkInOutAdd";
	private String checkInOutUpdatePath = "/admin/checkInOutUpdate";	
	private String searchPath           = "/admin/search";	
	private String path  = "";
	private String param = "";
	private String ip    = "";

	private ClientIp ci                           = new ClientIp();
	private EmployeeManageImpl employeeManage     = new EmployeeManageImpl();
	private GradeManageImpl gradeManage           = new GradeManageImpl();
	private CheckInOutManageImpl checkInOutManage = new CheckInOutManageImpl();		
	private TimeCheck timeCheck                   = new TimeCheck();
	private utilDay uiilDay = new utilDay();
	private Object obj                      = null;
	private LocalDate today                 = null;                 		
	private JSONArray jsonarr               = null;
	private List<CheckInOut> checkInOutList = null;
	private List<Grade> gradeSelect         = null;
	private List<UserInfo> userList         = null;
	private ArrayList<JSONObject> list      = null;

	private String month = "";
	private String day   = "";
	
	private int checkIdx     = 0;
	private int userIdx      = 0;
	private int gradeIdx     = 0;
	private String checkInTime  = "";
	private String checkOutTime = "";
	private String checkMemo    = "";				
	private String checkInIp    = "";
	private String checkOutIp   = "";
	private String userName     = "";
	private String userId       = "";
	private String userPw       = "";
	private String gradeName    = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			init(request,response);			
			// 직원 조회
			if (path.equals(employeeListPath)) {									
				for (UserInfo userInfo : userList) {
					if(!userInfo.getUserName().equals("관리자"))
					list.add(setUserInfoParserJsonObj(userInfo));					
				}
				response.getWriter().println(list);
			// 직원 추가
			} else if (path.equals(employeeAddPth)) {
				employeeQuery(response, "insert");
			// 직원 수정
			} else if (path.equals(employeeUpdatePath)) {						
				employeeQuery(response, "update");
			// 직원 삭제
			} else if (path.equals(employeeDeletePath)) {
				employeeQuery(response, "delete");
			// 직급 조회
			} else if (path.equals(gradeListPath)) {
				for (Grade grade : gradeSelect) {
					JSONObject Jobj = new JSONObject();
					Jobj.put("gradeIdx", grade.getGradeIdx());
					Jobj.put("gradeName", grade.getGradeName());
					list.add(Jobj);
				}
				response.getWriter().println(list);
			// 직급 추가
			} else if (path.equals(gradeAddPath)) {
				response.getWriter().println(AfterExecuteGetGradeJsonList("insert"));
			// 직급 수정
			} else if (path.equals(gradeUpdatePath)) {												
				response.getWriter().println(AfterExecuteGetGradeJsonList("update"));
			// 직급 삭제
			} else if (path.equals(gradeDeletePath)) {												
				response.getWriter().println(AfterExecuteGetGradeJsonList("delete"));
			// 출퇴근 기록 조회
			} else if (path.equals(checkInOutSelectPath)) {
				String year = Integer.toString(today.getYear());						
				response.getWriter().println(checkInOutSelectConvertJSONList(year, month, day, "select", null));
			// 출퇴근 기록 추가
			} else if (path.equals(checkInOutAddPath)) {			
				checkInsertAndUpdate(response, "insert");
			// 출퇴근 수정
			} else if (path.equals(checkInOutUpdatePath)) {				
				checkInsertAndUpdate(response, "update");
			// 출퇴근 검색
			} else if (path.equals(searchPath)) {
				response.getWriter().println(checkInOutSelectConvertJSONList(null,null, null, "conditionSelect", request));
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void init(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, UnknownHostException {

		// 파라미터로 전달받은 값을 json배열로 변환한다.
		if (request.getParameter("models") != null) {
			this.param   = request.getParameter("models");
			this.obj     = JSONValue.parse(param);
			this.jsonarr = (JSONArray)obj;
		}		
		this.list        = new ArrayList<>();
		int length       = request.getContextPath().length();
		this.path        = request.getRequestURI().substring(length);
		this.ip          = ci.getClientIp(request);
		this.gradeSelect = gradeManage.select();
		this.userList    = employeeManage.selectList();
		this.today       = LocalDate.now();
		this.month       = today.getMonthValue() < 10 ? "0" + today.getMonthValue() : Integer.toString(today.getMonthValue());
		this.day         = today.getDayOfMonth() < 10 ? "0" + today.getDayOfMonth() : Integer.toString(today.getDayOfMonth());
		response.setContentType("text/html; charset=utf-8");		
	}

	// 클라이언트에서 넘어오는 JSON 값을 변환하여 전역변수에 넣어준다.
	public void setJsonObjParser(JSONObject jsonobj) {
		this.checkIdx     = (jsonobj.get("checkIdx")     == null || jsonobj.get("checkIdx").toString().length() == 0)     ? 0  : Integer.parseInt(jsonobj.get("checkIdx").toString());
		this.userIdx      = (jsonobj.get("userIdx")      == null || jsonobj.get("userIdx").toString().length() == 0)      ? 0  : Integer.parseInt(String.valueOf(jsonobj.get("userIdx")));
		this.gradeIdx     = (jsonobj.get("gradeIdx")     == null || jsonobj.get("gradeIdx").toString().length() == 0)     ? 0  : Integer.parseInt(String.valueOf(jsonobj.get("gradeIdx")));
		this.checkMemo    = (jsonobj.get("checkMemo")    == null || jsonobj.get("checkMemo").toString().length() == 0)    ? "" : jsonobj.get("checkMemo").toString();
		this.checkInTime  = (jsonobj.get("checkInTime")  == null || jsonobj.get("checkInTime").toString().length() == 0)  ? "" : jsonobj.get("checkInTime").toString();
		this.checkOutTime = (jsonobj.get("checkOutTime") == null || jsonobj.get("checkOutTime").toString().length() == 0) ? "" : jsonobj.get("checkOutTime").toString();		
		this.checkInIp    = (jsonobj.get("checkInIp")    == null || jsonobj.get("checkInIp").toString().length() == 0)    ? "" : jsonobj.get("checkInIp").toString();
		this.checkOutIp   = (jsonobj.get("checkOutIp")   == null || jsonobj.get("checkOutIp").toString().length() == 0)   ? "" : jsonobj.get("checkInIp").toString();
		this.userName     = (jsonobj.get("userName")     == null || jsonobj.get("userName").toString().length() == 0)     ? "" : jsonobj.get("userName").toString();
		this.userId       = (jsonobj.get("userId")       == null || jsonobj.get("userId").toString().length() == 0)       ? "" : jsonobj.get("userId").toString();
		this.userPw       = (jsonobj.get("userPw")       == null || jsonobj.get("userPw").toString().length() == 0)       ? "" : jsonobj.get("userPw").toString();		
		this.gradeName    = (jsonobj.get("gradeName")    == null || jsonobj.get("gradeName").toString().length() == 0)    ? "" : jsonobj.get("gradeName").toString();
	}

	// type에 따라 추가, 수정, 삭제를 하고 처리한 user 정보를 JsonObject list에 담아 리스폰해준다.
	private void employeeQuery(HttpServletResponse response, String type) throws SQLException, IOException {
		System.out.println(">> jsonarr " + jsonarr);
		for (int i=0; i<jsonarr.size(); i++) {
			UserInfo user = getUserInfo(gradeSelect, i);

			switch (type) {
				case "insert":
					employeeManage.insert(user);
					break;

				case "update":				
					employeeManage.update(user);
					break;

				case "delete":
					JSONObject jsonobj = (JSONObject)jsonarr.get(i);
					setJsonObjParser(jsonobj);
					employeeManage.delete(userIdx);
					break;
			}

		    list.add(setUserInfoParserJsonObj(user));
		}
		response.getWriter().println(list);
	}

	// type에 따라 기본 조회, 조건으로 조회하고 그 결과를 JSONObject 리스트로 반환 
	private ArrayList<JSONObject> checkInOutSelectConvertJSONList(String year, String month, String day, String type, HttpServletRequest request) throws Exception{
		// 전역에 선언된 list를 사용하지 않고 지역 변수 list를 생성해 사용한다.
		ArrayList<JSONObject> list = new ArrayList<>();

		switch (type) {
			case "select":
				checkInOutList = checkInOutManage.defaultSelectAll(year, month, day);
				break;

			case "conditionSelect":
				String employee  = request.getParameter("employeeSelect");			
				String startDate = request.getParameter("startDate");
				String endDate   = request.getParameter("endDate");				
				String lastState = request.getParameter("lastState");			
				String overCheck = request.getParameter("overCheck");

				employee =  employee.equals("default")  ? "" : employee;				
				lastState = lastState.equals("default") ? "" : lastState;
				overCheck = overCheck.equals("default") ? "" : overCheck;

				checkInOutList = checkInOutManage.ConditionSelectAll(employee, startDate, endDate, lastState, overCheck);	
				break;
		}
		
		for (CheckInOut check : checkInOutList) {
			JSONObject Jobj     = new JSONObject();
			String checkInTime  = check.getCheckInHour()  + ":" + check.getCheckInMinute();					
			String checkOutTime = check.getCheckOutHour() + ":" + check.getCheckOutMinute();

			if (check.getCheckOutHour() == null) {
				checkOutTime = null;				
			}			
			
			String dateDay = uiilDay.getDateDay(check.getCheckInYear()  + check.getCheckInMonth() + check.getCheckInDay(), "yyyyMMdd");			
			Jobj.put("date", check.getCheckInMonth() + "/" + check.getCheckInDay() + " (" + dateDay + ")");
			Jobj.put("userIdx", check.getUser().getUserIdx());
			Jobj.put("userName", check.getUser().getUserName());
			Jobj.put("gradeName", check.getUser().getGrade().getGradeName());
			Jobj.put("checkIdx", check.getCheckIdx());
			Jobj.put("checkInTime", checkInTime);
			Jobj.put("checkOutTime", checkOutTime == null ? "00:00" : checkOutTime);
			Jobj.put("checkInIp", check.getCheckInIp());
			Jobj.put("checkOutIp", check.getCheckOutIp());
			Jobj.put("checkMemo", check.getCheckMemo());
			Jobj.put("workTime", timeCheck.workAndOverCheck(checkInTime, checkOutTime,"근무시간"));					
			Jobj.put("checkLateTime", timeCheck.lateCheck(checkInTime));					
			Jobj.put("checkOverTime", timeCheck.workAndOverCheck(checkInTime, checkOutTime,"야근"));

			list.add(Jobj);
		}
		return list;
	}

	// 유저 정보를 받아서 JSONObject로 변환
	private JSONObject setUserInfoParserJsonObj(UserInfo user) throws SQLException{		
	    JSONObject Jobj = new JSONObject();
	    
	    // userIdx가 없을 경우 디비에서 조회하여 인덱스를 가져와 JSONObject에 넣어준다.
		Jobj.put("userIdx",   user.getUserIdx() == null ? employeeManage.select(user).getUserIdx() : user.getUserIdx());
		Jobj.put("userName",  user.getUserName());
		Jobj.put("userId",    user.getUserId());
		Jobj.put("userPw",    user.getUserPw());
		Jobj.put("gradeIdx",  user.getGrade().getGradeIdx());
		Jobj.put("gradeName", user.getGrade().getGradeName());

		return Jobj;
	}

	// 직급리스트, 인덱스, 클라이언트에서 전달받은 값들을 UserInfo 객체에 세팅하여 반환
	private UserInfo getUserInfo(List<Grade> gradeSelect, int index) {
		JSONObject jsonobj = (JSONObject)jsonarr.get(index);
	    UserInfo user      = new UserInfo();
	    Grade grade        = new Grade();

	    setJsonObjParser(jsonobj);

	    // 클라이언트에서 넘어온 유저 인덱스가 있으면 user 객체에 set한다.
	    if (userIdx != 0) {
	    	user.setUserIdx((userIdx));
	    } else {
	    	// 클라이언트에서 넘어온 유저 인덱스가 없을 경우 클라이언트에서 넘어온 유저 이름과 DB에 저장된 유저 이름을 비교하여 값은 이름의 유저 인덱스를 가져와 user 객체에 set한다. 
	    	for (UserInfo userInfo : userList) {	    		
				if(userInfo.getUserName().equals(userName)) {
					user.setUserIdx(userInfo.getUserIdx());
				}
			}
	    }	    

	    // 직급에 해당하는 인덱스를 user 객체에 set한다.
	    for (Grade gradeItem : gradeSelect) {	    	
	    	if (gradeName.equals(gradeItem.getGradeName())){
	    		grade.setGradeIdx(gradeItem.getGradeIdx());
	    		grade.setGradeName(gradeName);	    		
	    	}
		}
	    user.setUserName(userName);
	    user.setUserId(userId);
	    user.setUserPw(userPw);	    
	    user.setGrade(grade);
	    
		return user;
	}

	// 타입을 받아 타입에 맞춰 실행 후 클라이언트에서 전달받은 값을 JSONObject로 리턴
	private ArrayList<JSONObject> AfterExecuteGetGradeJsonList(String type) throws SQLException {
		ArrayList<JSONObject> list = new ArrayList<>();
		
		for (int i=0; i<jsonarr.size(); i++) {					
		    JSONObject jsonobj = (JSONObject)jsonarr.get(i);
		    JSONObject Jobj    = new JSONObject();
		    Grade grade        = new Grade();
		    
		    setJsonObjParser(jsonobj);

		    if (gradeIdx != 0) {		    	
		    	grade.setGradeIdx(gradeIdx);
		    }
		    grade.setGradeName(gradeName);

		    Jobj.put("gradeIdx" ,  gradeIdx);
			Jobj.put("gradeName", gradeName);

			list.add(Jobj);

			switch (type) {
				case "insert":
					gradeManage.insert(grade);			
					break;

				case "update":
					gradeManage.update(grade);			
					break;

				case "delete":
					gradeManage.delete(gradeIdx);			
					break;
			}
		}
		return list;
	}	

	// type에 따라 출퇴근 추가 및 수정을 하고 작업한 데이터를 JSONObject list에 담아 클라이언트로 전달
	private void checkInsertAndUpdate(HttpServletResponse response, String type) throws SQLException, ParseException, IOException {
		
		for (int i = 0; i < jsonarr.size(); i++) {
			JSONObject jsonobj    = (JSONObject)jsonarr.get(i);
			JSONObject Jobj       = new JSONObject();
			CheckInOut checkInOut = new CheckInOut();
			List<String> times    = new ArrayList<String>();
			UserInfo user         = getUserInfo(gradeSelect, i);

			setJsonObjParser(jsonobj);

	    	switch (type) {
				case "update":
					// 수정에만 들어가는 로직
				    if (!checkInTime.equals("")) {
				    	checkInOut.setCheckInHour(checkInTime.substring(0,2));
				    	checkInOut.setCheckInMinute(checkInTime.substring(3,5));
				    }
				    if (!checkOutTime.equals("")) {				    	
				    	checkInOut.setCheckOutHour(checkOutTime.substring(0,2));
				    	checkInOut.setCheckOutMinute(checkOutTime.substring(3,5));				    	
				    }
				    checkInOut.setCheckIdx(checkIdx);

					if (checkOutIp.equals("")) checkInOut.setCheckOutIp(ip);

					if (!checkMemo.equals("")) {
					   	checkInOut.setCheckMemo(checkMemo);
					} else {
					   	checkInOut.setCheckMemo("");
					}		
					checkInOutManage.update(checkInOut);
					
					Jobj.put("userIdx",    userIdx);
					Jobj.put("checkIdx",   checkIdx);
					Jobj.put("checkInIp",  checkInIp);
					Jobj.put("checkOutIp", checkOutIp);
					break;
	
				case "insert":				
					// 추가에만 들어가는 로직
					if(checkInTime  != null) times.add(checkInTime);
			    	if(checkOutTime != null && !checkOutTime.equals("00:00")) times.add(checkOutTime);
			    				    	
					checkInOut.setUser(user);
					checkInOut.setCheckMemo(checkMemo);
					
					for (String string : times) {
						if(string.length() == 0) break;

			    		checkInOut.setCheckInYear(Integer.toString(today.getYear()));
			    		checkInOut.setCheckInMonth(month);
			    		checkInOut.setCheckInDay(day);
			    		checkInOut.setCheckInHour(string.substring(0, 2));
			    		checkInOut.setCheckInMinute(string.substring(3, 5));
			    		checkInOut.setCheckInIp(ip);
						checkInOutManage.Insert(checkInOut);
					}

					CheckInOut singleselect = checkInOutManage.SingleSelect(checkInOut);
					Jobj.put("userIdx", singleselect.getUser().getUserIdx());
					Jobj.put("checkIdx", singleselect.getCheckIdx());
					Jobj.put("checkInIp", singleselect.getCheckInIp());
					Jobj.put("checkOutIp", singleselect.getCheckOutIp());		
					break;
			}

			// 공통 로직
			Jobj.put("userName",  userName);
			Jobj.put("gradeName", gradeName);
			Jobj.put("checkInTime", checkInTime);
			Jobj.put("checkOutTime", checkOutTime);
			Jobj.put("checkMemo", checkMemo);
			Jobj.put("workTime", timeCheck.workAndOverCheck(checkInTime, checkOutTime,"근무시간"));				
			Jobj.put("checkLateTime", timeCheck.lateCheck(checkInTime));
			Jobj.put("checkOverTime", timeCheck.workAndOverCheck(checkInTime, checkOutTime, "야근"));

			list.add(Jobj);
		}
		response.getWriter().println(list);
	}
}