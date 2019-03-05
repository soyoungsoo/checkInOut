<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<jsp:include page="menu.jsp" flush="false" />
		<div id="contents">
			<div class="pageTitle">
				<h1>직원 관리</h1>
			</div>
		<div class="toolArea">
			<ul class="nav nav-tabs">
				<li role="presentation"><a href="adminEmployeeManage.jsp">직원관리</a></li>
				<li role="presentation"><a href="adminGradeManage.jsp">직급관리</a></li>
			</ul>
		</div>
		<div id="grid"></div>			
		<script>				
		$(document).ready(function() {
			$(".nav-tabs>li:nth-child(1)").addClass("active");
			
			var crudServiceBaseUrl = "admin", 
			dataSource = new kendo.data.DataSource({
				transport : {							
					read : function(options) {
						$.ajax({
							url : crudServiceBaseUrl + "/employeeList",
							dataType : "json",
							cache: false,
							success : function(result) {
								options.success(result);
							},
							error : function(result) {
								options.error(result);
							}
						})
					},
					update: function(options) {
						$.ajax({
							url: crudServiceBaseUrl + "/employeeUpdate",
							dataType: "json",
							cache: false,
							data: {
								models: kendo.stringify(options.data.models)
							},
							success: function(result) {		
								options.success(result);
							},
							error: function(result) {
								options.error(result);
							}
						});								    
					},
					destroy : function(options) {
						$.ajax({
							url: crudServiceBaseUrl + "/employeeDelete",
							dataType: "json",
							cache: false,
							data: {
								models: kendo.stringify(options.data.models)
							},
							success: function(result) {									          
							    options.success(result);
							},
							error: function(result) {									          
								options.error(result);
							}
						});								    
					},
					create: function(options) {  									
						$.ajax({
							url: crudServiceBaseUrl + "/employeeAdd",
							dataType: "json",
							cache: false,
							data: {
								models: kendo.stringify(options.data.models)
							},
							success: function(result) {	
								if(result.error == '중복'){
									alert(msg);
								}
								options.success(result);											       
							},
							error: function(result) {
								options.error(result);
							}
						});
					},
					parameterMap : function(options, operation) {
						if (operation !== "read" && options.models) {
							return {
								models : kendo.stringify(options.models)
							};
						}
					}
				},
				batch : true,
				pageSize : 10,
				schema : {
					model : {
						id : "userIdx",
							fields : {
								gradeName : { editable : true,  validation : { required : true, title: "직급을 입력해주세요" }},
								userName  : { editable : true,  validation : { required : true, title: "이름을 입력해주세요",
									userNamevalidation: function (input) {
										if (input.is("[name='userName']") && input.val() != "") {
											input.attr("data-userNamevalidation-msg", "형식이 올바르지 않습니다.");
												return /^[가-힣]{2,3}$/.test(input.val());
										}
										return true;
									}
								}},
								userId    : { editable : true,  validation : { required : true, title: "아이디를 입력해주세요",
									userIdvalidation: function (input) {
										if (input.is("[name='userId']") && input.val() != "") {
											input.attr("data-userIdvalidation-msg", "공백은 입력하실 수 없습니다.");
		                                    return !(/[\s]/g.test(input.val()));
		                                }
		                                return true;
									}
								}},
								userPw    : { editable : true,  validation : { required : true, title: "비밀번호를 입력해주세요", 
									userPwvalidation: function (input) {
										if (input.is("[name='userPw']") && input.val() != "") {
											input.attr("data-userPwvalidation-msg", "영문자와 숫자만 입력 가능합니다.");
		                                    return /^[A-Za-z0-9+]*$/.test(input.val());
										}
										return true;
									}
								}},
							}
						}
					}
				});					
						
			$("#grid").kendoGrid({
				dataSource : dataSource,
				pageable : {
					pageSize : 10,
					alwaysVisible : true,
					buttonCount : 10,
				},
				sortable : {							
					allowUnsort : true,
					showIndexes : true
				},
				toolbar : [ "create" ],
				navigatable: true,
				//selectable: true,
				messages : {
					commands : {
						create : "직원 추가",						
						update : "추가",
						canceledit : "취소"
					}
				},
				columns : [
						{ field : "gradeName", editor: categoryDropDownEditor,  title : "직급" },
						{ field : "userName", title : "성명"},
						{ field : "userId", title : "아이디"},
						{ field : "userPw", title : "비밀번호", },
						{ command : [ { name: "edit", text: { edit: "수정", cancel: "취소", update: "저장" } }, {name : "destroy",text : "삭제"} ],title : "&nbsp;",width : "250px"}, ],
				noRecords : {
					template : "현재 페이지에 데이터가 없습니다."
				},
				editable : {
					mode : "inline",							
					confirmation: "해당 정보를 삭제하시겠습니까?",
				},
			});
		});
					
		function categoryDropDownEditor(container, options) {
			$('<input required name="' + options.field + '"/>').appendTo(container).kendoDropDownList({
				autoBind: false,				
				dataTextField: "gradeName",
				dataValueField: "gradeName",						
				dataSource: {
				type: "json",
					transport: {
						read: function(options) {
							$.ajax({
								url : "admin/gradeList",
								dataType : "json",
								cache: false,
								success : function(result) {	    											
									options.success(result);
									//alert(">> 성공"+ JSON.stringify(result));
								},
								error : function(result) {
									options.error(result);
									alert(">> 실패"+ JSON.stringify(result));
								}
							})
						}
					}
				}
			});
		}
		</script>
		</div>
	</div>
</body>
</html>