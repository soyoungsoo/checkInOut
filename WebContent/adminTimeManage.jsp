<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>	
	<jsp:include page="menu.jsp" flush="false" />	
		<div id="contents">
			<div>
				<div class="pageTitle">
					<h1>시간 관리</h1>
				</div>
				<div class="searchArea">
					<form class="searchForm" action="admin/search" method="get">
						<p>검색 조건</p>
						<div class="boxing">
							<select class="selectList mr_20" id="employee" name="employeeSelect">
								<option value="default" selected>전체</option>							
							</select> 
							<input type="text" id="datepicker" name="startDate" class="datepickers"> <span>~</span>
							<input type="text" id="datepicker2" name="endDate" class="datepickers"> 
							<select class="selectList mg_20" name="lateCheck">
								<option value="default" selected>지각 여부 선택</option>
								<option value="false">지각 안함</option>
								<option value="true">지각 함</option>
							</select>
							<select class="selectList mg_20" name="overCheck">
								<option value="default" selected>야근 여부 선택</option>
								<option value="false">야근 안함</option>
								<option value="true">야근 함</option>
							</select>
							<a href="#" id="searchIcon" class="button seach">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</a>
						</div>
						<div class="sal-calculator">
							<span>시급 : <input type="text" id="salary" onkeyup="numValidator(this)"></span>						
							<a href="#" id="calculator">
								<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
							</a>
						</div>
					</form>
				</div>
			</div>
			<div id="grid"></div>
		</div>
		<script>
		
		$( ".datepickers" ).datepicker({
	    	dateFormat: 'yy-mm-dd' //Input Display Format 변경,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
	        ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
	        ,changeYear: true //콤보박스에서 년 선택 가능
	        ,changeMonth: true //콤보박스에서 월 선택 가능
	        ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시
	        ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
	        ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
	        ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트
	        ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
	        ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
	        ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
	        ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
	        ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트	        
	    });
		
		  var myDate = new Date();
		  var month = myDate.getMonth() + 1;
		  var prettyDate =  myDate.getFullYear() + '-' + n(month) + '-' + n(myDate.getDate());
		  $(".datepickers").val(prettyDate);
		  
		  
		
		$(document).ready(function() {
			$(".menu>li:nth-child(1)").addClass("active");
			$(".menu>li:nth-child(2)").removeClass("active");
			
			
			$("#calculator").click(function(){
				if($("#salary").val().length == 0) {
					alert("시급을 입력해주세요.");
					return false;
				}
	            var workHour = 0, workMinute = 0, lateHour = 0, lateMinute = 0, overHour = 0, overMinute = 0;
	            var data =$("#grid").data("kendoGrid").dataSource.data();

	            for(i = 0; i < data.length; i++){
	            	var work = data[i].workTime;
	            	var late = data[i].checkLateTime;
	            	var over = data[i].checkOverTime;

	               	workHour   += Number(work.substring(0, work.indexOf(":")));
	               	workMinute += Number(work.substring(work.indexOf(":") + 1));

	               	lateHour   += Number(late.substring(0, late.indexOf(":")));
	               	lateMinute += Number(late.substring(late.indexOf(":") + 1));

	               	overHour   += Number(over.substring(0, over.indexOf(":")));
	               	overMinute += Number(over.substring(over.indexOf(":") + 1));
                }

                workHour += div(workMinute);
                workMinute = rest(workMinute);

                lateHour += div(lateMinute);
                lateMinute = rest(lateMinute);

                overHour += div(overMinute);
                overMinute = rest(overMinute);

                var inputSal = $("#salary").val();
                var salary = inputSal * workHour;
                var grid = $("#grid").data("kendoGrid");
                grid.dataSource.add ({
                						userName : "합계", gradeName : "", workTime : n(workHour) + ":" + n(workMinute), 
                						checkLateTime :  n(lateHour) + ":" + n(lateMinute), checkOverTime : n(overHour) + ":" + n(overMinute), etc : comma(salary)
                					});
              });

			$.ajax({
				url : "admin/employeeList",
				dataType : "json", 				
				cache: false,
				success : function(result) {		
					for(var i = 0; i < result.length; i++){				
						$('#employee').append("<option value='" + result[i].userName + "' >" + result[i].userName + "</option>");
					}					 			
				},
				error : function(result) {																				
					console.log(">> 실패" + JSON.stringify(result));
				}
			});
		});
				
		$(document).ready(function() {
			var fileName = "출퇴근기록";
			var crudServiceBaseUrl = "admin",
			
			dataSource = new kendo.data.DataSource({
				transport : {
					read : function(options) {
						$.ajax({
							url : crudServiceBaseUrl + "/checkInOutSelect",
							dataType : "json", 
							cache: false,
							success : function(result) {																	
								options.success(result);									
							},
							error : function(result) {																	
								options.error(result);
							}
						});
					},
					update: function(options) {
						$.ajax({
							url: crudServiceBaseUrl + "/checkInOutUpdate",
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
							url: crudServiceBaseUrl + "/checkInOutAdd",
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
						id : "checkIdx",
						fields : {
							date          : { editable : false},
							userName      : { editable : true, validation : { required : true, title: "이름을 입력해주세요" } },
							gradeName     : { editable : true, validation : { required : true, title: "직급을 입력해주세요" } },
							checkInTime   : { editable : true, type: "string", defaultValue: "00:00", validation : { required : true, title: "출근 시간을 입력해주세요",
								checkInTimevalidation: function (input) {
									if (input.is("[name='checkInTime']") && input.val() != "") {
										input.attr("data-checkInTimevalidation-msg", "형식이 올바르지 않습니다. (형식 : HH:mm)");
	                                    return /^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$/.test(input.val());
	                                }
									return true;
								}
							}},
							checkOutTime  : { editable : true, type: "string", defaultValue: "00:00", validation : { required : false, title: "퇴근 시간을 입력해주세요",
								checkOutTimevalidation: function (input) {
									if (input.is("[name='checkOutTime']") && input.val() != "") {
										input.attr("data-checkOutTimevalidation-msg", "형식이 올바르지 않습니다. (형식 : HH:mm)");
										return /^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$/.test(input.val());
									}
										return true;
								}
							}},
							workTime  	  : { editable : false },
							checkLateTime : { editable : false },
							checkOverTime : { editable : false },
							etc           : { editable : false },
							checkInIp     : { editable : false },
							checkOutIp    : { editable : false },
							checkMemo     : { editable : true  }
						}
					}
				}
			});

			$("#searchIcon").click(function() {
				var employee  = $("#employee").val();
				var startDate = $("input[name=startDate]").val();
				var endDate   = $("input[name=endDate]").val();
				var lateState = $("select[name=lateCheck]").val();
				var overCheck = $("select[name=overCheck]").val();

	            var newDS = new kendo.data.DataSource({
					transport : {
						read : function(options) {
							$.ajax({
								url : "admin/search",
								dataType : "json",
								cache: false,
								data: 'employeeSelect=' + encodeURI(employee) + '&startDate=' + startDate +'&endDate=' + endDate + '&lastState=' + lateState + '&overCheck=' + overCheck,
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
								url: crudServiceBaseUrl + "/checkInOutUpdate",
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
								url: crudServiceBaseUrl + "/checkInOutAdd",
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
						id : "checkIdx",
						fields: {
							date  	             : { editable : false },
							userName      		 : { editable : true, validation : { required : true,  title: "이름을 입력해주세요" } },
							gradeName     		 : { editable : true, validation : { required : true,  title: "직급을 입력해주세요" } },
							checkInTime			 : { editable : true, type: "string", defaultValue: "00:00", validation : { required : true,  title: "출근시간을 입력해주세요" , 
								productnamevalidation: function (input) {
									if (input.is("[name='checkInTime']") && input.val() != "") {
										input.attr("data-productnamevalidation-msg", "형식이 올바르지 않습니다. (형식 : HH:mm)");
										return /^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$/.test(input.val());
									}
									return true;
	                            } 
							}},
							checkOutTime  : { editable : true, type: "string", defaultValue: "00:00", validation : { required : true,  title: "퇴근시간을 입력해주세요", 
								productnamevalidation: function (input) {
									if (input.is("[name='checkOutTime']") && input.val() != "") {
										input.attr("data-productnamevalidation-msg", "형식이 올바르지 않습니다. (형식 : HH:mm)");
										return /^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$/.test(input.val());
									}
                                    return true;
                                }
							}},
							workTime  	  : { editable : false },
							checkLateTime : { editable : false },
							checkOverTime : { editable : false },
							etc           : { editable : false },
							checkInIp     : { editable : false },
							checkOutIp    : { editable : false },
							checkMemo     : { editable : true  }
						}
						}
					},					
				});
				$("#grid").data("kendoGrid").setDataSource(newDS);
	        });

			$("#grid").kendoGrid({
				dataSource : dataSource,
				pageable : {
					pageSize : 10,
					alwaysVisible : true,
					buttonCount : 10,
				},
				sortable : {
					//mode: "multiple",
					allowUnsort : true,
					showIndexes : true
				},
				toolbar : [ "create", "excel" ],
				excel : {
					fileName : fileName + ".xlsx",
					allPages : true
				},
				scrollable: true,
				noRecords: {
				    template: "해당하는 기록이 없습니다.",
				  },
				columns : [
						    { field : "date",  	         title : "날짜",      width: "130px"},
							{ field : "userName",  	     title : "성명",      width: "130px", editor: userNameDropDownEditor},
							{ field : "gradeName",  	 title : "직급", 	     width: "150px", editor: gradeNameDropDownEditor},
							{ field : "checkInTime", 	 title : "출근시간",   width: "100px"}, 
							{ field : "checkOutTime", 	 title : "퇴근시간",   width: "100px"},
							{ field : "workTime", 		 title : "근무시간",   width: "100px"}, 
							{ field : "checkLateTime",   title : "지각시간",   width: "100px", template:'#=lateShow(checkLateTime)#'},
							{ field : "checkOverTime",   title : "야근시간",   width: "100px", template:'#=overShow(checkOverTime)#'},
							{ field : "etc",		     title : "비고",      width: "120px"},
							{ command: [{ name: "edit", text: { edit: "수정", cancel: "취소", update: "저장" } }],width: "120px" },
							{ field : "checkInIp", 		 title : "출근IP",    width: "120px"},
							{ field : "checkOutIp", 	 title : "퇴근IP",    width: "120px"},
							{ field : "checkMemo",		 title : "메모",      width: "600px"}
			       		 ],
				messages : {
					commands : { create : "추가하기", save : "변경사항 저장", excel : "엑셀로 저장", cancel : "변경 취소", update : "추가", canceledit : "취소"}
				},
				editable: {
					mode : "inline",
					createAt : "top"
				}
			});
		});

		function gradeNameDropDownEditor(container, options) {
						
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

		function userNameDropDownEditor(container, options) {
			$('<input required name="' + options.field + '"/>').appendTo(container).kendoDropDownList({
				autoBind: false,
				dataTextField: "userName",
				dataValueField: "userName",				
				dataSource: {
				type: "json",
					transport: {
						read: function(options) {
							$.ajax({
								url : "admin/employeeList",
								dataType : "json",
								cache: false,
								success : function(result) {	    											
									options.success(result);
								},
								error : function(result) {
									options.error(result);
								}
							})
						}
					 }
				}
			});
        }
		
		function lateShow(value) {			 			
			var hour = value.substring(0,2);
			var minute = value.substring(3,5);
	         
	        if (Number(hour) > 0 || Number(minute) > 0)	        	 
	        	return "<b style='color:red'>"+ value+"</b>";	             
	        else 
	        	return "00:00";
		}
		
		function overShow(value) {			 			
			var hour = value.substring(0,2);
			var minute = value.substring(3,5);
	         
	        if (Number(hour) > 0 || Number(minute) > 0)	        	 
	        	return "<b style='color:blue'>"+ value+"</b>";	             
	        else
	        	return "00:00";
	   	}
		</script>
	</div>
</body>
</html>
