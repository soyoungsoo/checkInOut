<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>출퇴근 관리</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resource/css/admin.css" />
<link rel="stylesheet" href="https://kendo.cdn.telerik.com/2018.3.1017/styles/kendo.common-material.min.css" />
<link rel="stylesheet" href="https://kendo.cdn.telerik.com/2018.3.1017/styles/kendo.material.min.css" />
<link rel="stylesheet" href="https://kendo.cdn.telerik.com/2018.3.1017/styles/kendo.material.mobile.min.css" />

<script src="https://kendo.cdn.telerik.com/2018.3.1017/js/jquery.min.js"></script>
<script src="https://kendo.cdn.telerik.com/2018.3.1017/js/kendo.all.min.js"></script>
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/jszip/2.4.0/jszip.js"></script>
<script src="http://kendo.cdn.telerik.com/2018.3.1017/js/kendo.all.min.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/validator.js"></script>

<!--  datepicker cdn -->
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  
<style type="text/css">
#datepicker, #datepicker2 {
	line-height: 15px;
	padding: 0.5em 0 0.58em 0.1em;	
	text-align: center;
	width: 130px;
	padding: 11px;
}
@media screen and (max-width: 1580px) {
	.sal-calculator {
		display: inline-block;
		left: 79.8%;
		bottom: 38px;			
		position: relative;
		top: 30px;
	} 
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		if(${UserInfo eq null || UserInfo.userName ne "관리자"} ){
			alert("관리자 로그인 후 이용해 주세요.");
			location.href = 'userScene.jsp';
		}

	});		
</script>
</head>
	<body>
		<div id="wrap">
			<div id="left_content">
				<div class="left_header">
					<h2>${UserInfo.userName}님</h2>
					<p>
						<a href="user/logout">로그아웃</a>
					</p>
				</div>
				<div class="tabs">
					<ul class="nav nav-pills nav-stacked menu">
						<li role="presentation" class="managee"><a href="adminTimeManage.jsp">시간 관리</a></li>
						<li role="presentation" class="manage active"><a href="adminEmployeeManage.jsp">직원 관리</a></li>
					</ul>
				</div>
			</div>