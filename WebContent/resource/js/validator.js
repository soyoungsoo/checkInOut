/**
 * 
 */

function spaceTrim(e) {
	var re = /\s/g;
	if (e.value.match(re)) {
		$(e).val(e.value.trim());
		alert("공백은 입력할 수 없습니다.");
		return false;
	} 
	return true;
}
function numValidator(e) {
	var reg = /^[0-9]*$/;
	var result = spaceTrim(e);
	
	if(result == false) return false;
	
	if (!e.value.match(reg)) {
		$(e).val(e.value.slice(0,-1));
		alert("숫자만 입력해주세요.");
		return false;
	}
}
function loginValidator() {
	var lenId = $("#loginId").val().length;
	var lenPw = $("#loginPw").val().length;

	if (lenId == 0) {
		alert("아이디를 입력해주세요.");
		return false;
	} else if (lenPw == 0) {
		alert("비밀번호를 입력해주세요.");
		return false;
	}
	return true;
}

function updateValidator(action) {
	var curPw = $("input[name=curPw]").val();
	var pw = $("input[name=changePwd]").val();
	var pwCheck = $("input[name=changePwdCheck]").val();

	if (curPw.length == 0) {
		alert("현재 비밀번호를 입력해주세요.");
		return false;
	}
	if (pw.length == 0) {
		alert("변경할 비밀번호를 입력해주세요.");
		return false;
	}
	if (pwCheck.length == 0) {
		alert("변경할 비밀번호 확인을 입력해주세요.");
		return false;
	}
	if (pw != pwCheck) {
		alert("변경할 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		return false;
	}
	$(action).submit();
}

