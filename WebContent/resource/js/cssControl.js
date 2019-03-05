/**
 * 
 */
function loginOk() {
	$('.loginArea').css('display', 'none');
	hideDim();
}

function showInfoUpdate() {
	showDim();
	$('.updateArea').css('display', 'block');
}

function hideInfoUpdate() {
	hideDim();
	$('.updateArea').css('display', 'none');
}

function showDim() {
	$('#dimmed').css('display', 'block');
	$('#contnet').css('display', 'none');
}

function hideDim() {
	$('#dimmed').css('display', 'none');
	$('#contnet').css('display', 'block');
}

function getServerTime(){			
	$.ajax({
		url : "user/serverTime",
		type:"GET",
		dataType : "html", 
		cache: false,
		success : function(result) {														
			time = result;
		},
		error : function(result) {								
			console.log("실패"+ JSON.stringify(result));				
		}
	});		
} getServerTime();

function check(){
	$(".UserCheck").submit();
}
