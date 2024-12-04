const url = "http://localhost:80"
async function doUpload() {
    const fileInput = document.getElementById('file'); // 获取文件输入元素
    const file = fileInput.files[0]; // 获取文件对象

    const formData = new FormData(); // 创建表单数据对象
    formData.append('file', file); // 将文件对象添加到表单数据中

    fetch(url + '/upload', {
        method: 'POST', // 请求方法
        body: formData // 请求体
    }).then(response => {
        if (response.ok) {
            // 成功上传文件
            console.log('文件上传成功！');
            location.reload();
        } else {
            // 上传文件失败
            console.log('文件上传失败：' + response.statusText);
        }
    }).catch(error => {
        // 捕获异常
        console.log('发生错误：' + error.message);
    });

    return false; // 阻止表单默认提交行为
}

async function doDownload (button) {
    const filename = button.parentNode.parentNode.getElementsByTagName('td').item(1).innerHTML
    console.log('doDownload ' + filename)
    // const filename = document.getElementById(id).textContent
    console.log(filename)
    const href = "/download?filename=" + filename + ""
    download(href, filename);
}

function download(href, title) {
    const a = document.createElement('a');
    a.setAttribute('href', href);
    a.setAttribute('download', title);
    a.click();
}

async function doDelete(button) {
    const filename = button.parentNode.parentNode.getElementsByTagName('td').item(1).innerHTML
    console.log('doDownload ' + filename)
    // const filename = document.getElementById(id).textContent
    const tUrl = url + '/delete?filename=' + filename
    fetch(tUrl, {
        method: 'GET', // 请求方法
    }).then(response =>{
        if (response.ok) {
            // 成功上传文件
            console.log('文件删除成功！');
            alert('文件删除成功！')
            location.reload();
        } else {
            // 上传文件失败
            console.log('文件删除失败：' + response.statusText);
        }
    }).catch(error => {
        // 捕获异常
        console.log('发生错误：' + error.message);
    });
}
