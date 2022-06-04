function $(id) {
    return document.getElementById(id);
}

function preRegist() {
    //BOM: Browser
    //document.forms[0].uname
    //DOM: Document
    var unameTxt = $("unameTxt");
    //用戶名稱不得為空且應為6~16位數字和字母組成
    var unameReg = /[0-9a-zA-z]{6,16}/;
    var unameSpan = $("unameSpan");
    var uname = unameTxt.value;
    if(!unameReg.test(uname)) {
        unameSpan.style.visibility="visible";
        return false;
    } else {
        unameSpan.style.visibility="hidden";
    }

    //密碼的長度至少為8位
    var pwdTxt = $("pwdTxt");
    var pwdSpan = $("pwdSpan");
    var pwd = pwdTxt.value;
    var pwdReg = /.{8,}/;
    if(!pwdReg.test(pwd)) {
        pwdSpan.style.visibility="visible";
        return false;
    } else {
        pwdSpan.style.visibility="hidden";
    }

    //密碼兩次輸入不一致
    var pwdVerify = $("pwdVerifyTxt").value;
    var pwdVerifySpan = $("pwdVerifySpan");
    if(pwdVerify !== pwd) {
        pwdVerifySpan.style.visibility="visible";
        return false;
    } else {
        pwdVerifySpan.style.visibility="hidden";
    }

    //請輸入正確的信箱格式
    var email = $("emailTxt").value;
    var emailSpan = $("emailSpan");
    var emailReg = /^[a-zA-Z0-9_\.-]+@([a-zA-Z0-9-]+[\.]{1})+[a-zA-Z]+$/;
    if (!emailReg.test(email)) {
        emailSpan.style.visibility="visible";
        return false;
    } else {
        emailSpan.style.visibility="hidden";
    }

    return true;
}