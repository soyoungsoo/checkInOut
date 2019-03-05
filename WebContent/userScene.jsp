<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>AIN 출퇴근 관리</title>
</head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resource/css/user.css" />
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/validator.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/cssControl.js"></script>
<script type="text/javascript">
	// 서버 시간을 담을 전역 변수
	var time;
	var hour, minute;	
	
	var filter = "win16|win32|win64|mac";

	if(navigator.platform){
		if(0 > filter.indexOf(navigator.platform.toLowerCase())){
			alert("모바일 환경에서는 접속할 수 없습니다.");
			self.close();
		}
	}
	
	setInterval("timeSetting()", 1000);	
	
	function timeSetting() {	
		// 전역변수 time에 서버 시간을 담는다.	
		getServerTime();	
		
		var hours = time.substring(0,2);
		var minute = time.substring(3,5);
		var serverTime = time.substring(0,8);				
		var am_pm = "오전";
		
		if (hours > 11)
			am_pm = "오후";				
		$("#ServerTime").html(am_pm + " " + serverTime);		
		
	}
	$(function() {				
		var date = new Date();
		hour = date.getHours();
		minutes = date.getMinutes();
		
		if (hour > 17)
			document.body.style.background = "url('resource/img/퇴근.jpg') no-repeat center center fixed";	
		else 
			document.body.style.background = "url('resource/img/커피2.jpg') no-repeat center center fixed";
	});
	
	function noActive(id) {
		$("#" + id).css('pointer-events', 'none').css('cursor', 'default')
		.css('border-color', 'rgba(255, 255, 255, 0.75)')
		.css('background-color', 'rgba(255, 255, 255, 0.15)')
		.css('color', '#fff');
	}
	
	function txtShow(id, msg) {
		$("." + id).text(msg);
	}
	function init() {	
		
		if(${UserInfo eq null} ){
			showDim();
			$('.loginArea').css('display', 'block');			
		}
		$.ajax({
			type: "GET",
			url: 'user/btnMsg',
			dataType:'text',
			cache: false,
			success : function(result) {				
				var btnMsg = result.trim();				
				
				if (${UserInfo.userName eq "관리자"} ) {
					$("#inOutCheck").text("관리자 로그인");
					noActive("inOutCheck");
					return false;
				}
				
				$("#inOutCheck").text(result);
				
				if (btnMsg == '출근 체크') {					
					if (hour > 8) {
						txtShow('lastText','오늘은 좀 늦으셨네요. 어서 출근 체크하세요.');						
					} else {
						txtShow('lastText','좋은 아침입니다. 출근 체크 부탁드립니다.');						
					}
				}
				if(btnMsg == '퇴근 체크')
					if (hour > 18) {
						txtShow('lastText','오늘은 늦게까지 있으셨네요. 어서 퇴근 체크하세요.');						
					} else {
						txtShow('lastText','고생하셨습니다. 퇴근 체크 부탁드립니다.');
					}
					
				if(btnMsg == '체크 완료'){
					txtShow('lastText','오늘 하루도 고생하셨습니다.');					
					noActive("inOutCheck");		
				}
			},
			error : function(result) {					
				console.log("실패"+ JSON.stringify(result));		
			}
		});
	}	
	
	function loginCheck() {				

		var resultVal = loginValidator();			
		if (!(resultVal)) return false;

		var action = $(".loginForm").attr('action');
		var form_data = {
			userId: $("#loginId").val(),
			userPw: $("#loginPw").val(),				
		};
		$.ajax({
			type: "POST",
			url: action,
			data: form_data,
			dataType:'text',
			cache: false,
			success : function(result) {				
				if (result.trim() == "관리자"){
					alert("관리자로 로그인하셨습니다.");
					location.href = 'adminTimeManage.jsp';					
				} else if (result.trim() != '미등록회원') {
					alert("로그인하셨습니다.");
					$("#userName").text(result.trim());
					$("#inOutCheck").text(result.status);
					init();
					loginOk();
				} else {
					alert("아이디나 비밀번호가 일치하지 않습니다.");
					return false;				
				}
			},
			error : function(result) {
				console.log(">> 실패"+ JSON.stringify(result));
			}
		});					
	}

</script>
<body onload="init()">

	<div id="dimmed"></div>
	<div class="loginArea" style="display: none;">
		<form class="loginForm" action="user/login" method="post">
			<ul class="List">
				<li><input type="text" id="loginId" name="userId" onkeyup="spaceTrim(this)" placeholder="아이디"></li>
				<li><input type="password" id="loginPw" name="userPw" onkeyup="spaceTrim(this)" placeholder="비밀번호"></li>
				<li><a href="#" id="loginBtn" class="button" onclick="loginCheck()"><b>로그인</b></a></li>
			</ul>
		</form>
	</div>	
	<div class="updateArea">
		<form class="updateForm" action="user/update" method="post">
			<ul class="infoList">
				<li class="target"><input type="password" name="curPw"  onkeyup="spaceTrim(this)" placeholder="현재 비밀번호 "></li>
				<li class="target2"><input type="password" name="changePwd" onkeyup="spaceTrim(this)" placeholder="변경할 비밀번호"></li>
				<li><input type="password" name="changePwdCheck" onkeyup="spaceTrim(this)" placeholder="변경할 비밀번호 확인"></li>
				<li class="updateBtnArea1 "><a href="#" class="button confirm" onclick="updateValidator('.updateForm')"><b>수정</b></a></li>
				<li class="updateBtnArea2"><a href="#" class="cancle" onclick="hideInfoUpdate()">취소</a></li>
			</ul>
		</form>
	</div>
	<div id="page">
		<h1></h1>
		<div class="Header">
			<h1>SYS CORPORATION</h1>
			<h2>A Smart Dev</h2>
		</div>
		<div id="contnet">
			<p>서버 시간 : <span id="ServerTime"></span>
			</p>
			<h2>안녕하세요. <span id="userName">${UserInfo.userName}</span>님</h2>
			<a href="#" class="logout" onclick="showInfoUpdate()"><span>정보변경</span></a>
			<a href="user/logout" class="logout"><span>로그아웃</span></a>
			<p class="lastText"></p>
			<div class="checkArea">
				<form class="UserCheck" action="user/check" method="get">					
					<a href="#" id="inOutCheck" class="button" onclick="check()"></a>
				</form>
			</div>
		</div>
	</div>
</body>
</html>

