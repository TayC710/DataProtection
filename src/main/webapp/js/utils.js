import * as webauthnJson from '@github/webauthn-json'

const url = "http://localhost:80"

window.doRegister = async function () {
    const uname = document.forms["registerForm"]["username"].value;
    if (uname == null || uname === "") {
        alert("需要输入名字。");
        return false;
    }
    const code = document.forms["registerForm"]["verification-code"].value;
    if (code == null || code === "") {
        alert("需要输入验证码。");
        return false;
    }

    // console.log(code)

    const user = {
        username: uname, inputCode: code
    };
    // console.log(user.username + "/" + JSON.stringify(user))

    // Make the call that returns the credentialCreateJson above
    const credentialCreateOptions = await fetch(url + "/register", {
        method: 'POST', body: JSON.stringify(user)
    }).then(resp => resp.json());

    console.log(credentialCreateOptions)
    // Call WebAuthn ceremony using webauthn-json wrapper
    const publicKeyCredential = await webauthnJson.create(credentialCreateOptions);

    // Return encoded PublicKeyCredential to server
    await fetch(url + "/getCredential", {
        method: 'POST', body: JSON.stringify(publicKeyCredential)
    }).then(result => {
        alert("注册成功！");
    });
}
window.doAuth = async function () {

    const uname = document.forms["LoginForm"]["username"].value;
    if (uname == null || uname === "") {
        alert("需要输入名字。");
        return false;
    }

    const user = {
        username: uname
    };

    const credentialGetOptions = await fetch(url + "/AuthenticStep1", {
        method: 'POST', body: JSON.stringify(user)
    }).then(resp => resp.json());

    // Call WebAuthn ceremony using webauthn-json wrapper
    const publicKeyCredential = await webauthnJson.get(credentialGetOptions);

    // Return encoded PublicKeyCredential to server
    await fetch(url + "/AuthenticStep2", {
        method: 'POST', body: JSON.stringify(publicKeyCredential)
    });

    console.log("redirecting");
    window.location.replace("http://localhost/fileList");
}

