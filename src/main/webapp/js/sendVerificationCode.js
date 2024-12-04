async function sendVerificationCode() {
    const http = "http://";
    const domain = document.domain;
    const port = location.port;
    const mail = document.forms["registerForm"]["username"].value;
    if (mail == null || mail === "") {
        alert("需要输入邮箱。");
        return false;
    }
    await fetch(http + domain + ":" + port + "/getVerification", {
        method: 'POST',
        body: JSON.stringify({username: mail})
    });
    console.log("sendVerificationCode Done");
}