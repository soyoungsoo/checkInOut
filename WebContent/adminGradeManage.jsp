<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		$(".nav-tabs>li:nth-child(2)").addClass("active");
			var crudServiceBaseUrl = "admin", 
			dataSource = new kendo.data.DataSource({
				transport : {
					read : function(options) {
						$.ajax({
							url : crudServiceBaseUrl + "/gradeList",
							dataType : "json",
							cache: false,
							success : function(result) {
								options.success(result);
							},
							error : function(result) {
								options.error(result);
								alert(">> 실패"+ JSON.stringify(result));
							}
						})
					},
					update: function(options) {
						$.ajax({
							url: crudServiceBaseUrl + "/gradeUpdate",
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
							url: crudServiceBaseUrl + "/gradeDelete",
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
							url: crudServiceBaseUrl + "/gradeAdd",
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
					parameterMap : function(options, operation) {
						if (operation !== "read" && options.models) {
							return { models : kendo.stringify(options.models) };
						}
					}
				},
				batch : true,
				pageSize : 10,
				schema : {
					model : {
					id : "gradeIdx",
					fields: {
						gradeName : { editable : true, validation : { required : true, title: "직급을 입력해주세요", 
							gradeNamevalidation: function (input) {
								if (input.is("[name='gradeName']") && input.val() != "") {
									input.attr("data-gradeNamevalidation-msg", "형식이 올바르지 않습니다.");
									return (!(/[\s]/g.test(input.val())) && (/^[가-힣]{2,8}$/.test(input.val())));
								}
								return true;
							}
						}}
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
					toolbar : [ "create", "save", "cancel" ],
					messages : {
						commands : {
							create : "직원 추가",
							save : "변경사항 저장",
							cancel : "변경 취소",
							update : "추가",
							canceledit : "취소"
						}
					},
					columns : [
							{ field : "gradeName", title : "직급"},								
							{ command : [ {name : "destroy",text : "삭제하기"} ],title : "&nbsp;",width : "250px"} ],
					noRecords : {
						template : "현재 페이지에 데이터가 없습니다."
					},
						editable : true,
					editable : {
						mode : "incell",		
						window : {
		                      title: "직원 추가",
		                 },
		                 confirmation: "해당 정보를 삭제하시겠습니까?",
					},
				});
			});			
			</script>
			</div>
		</div>
	</body>
</html>