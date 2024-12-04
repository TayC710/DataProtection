// 获取文件列表
fetch("/fileList?display=1", {method: 'GET'})
    .then(response => response.text())
    .then(result => {
        let files = result.split(',');
        files.pop();
        let out = "<thead><tr><th class='text-primary text-center'>#</th><th class='text-info'>文件名</th><th class='text-info'>操作</th></tr></thead><tbody>";
        for (let i = 0; i < files.length; i++) {
            out += "<tr><td class='text-center ' style='vertical-align: middle;'>" + (i + 1) + "</td><td style='vertical-align: middle;' id = 'file" + (i + 1) + "'>" + files[i] + "</td><td><button id='DownBtm' class='btn btn-primary btn-sm' style=\"margin:5px\" type=\"submit\" onclick='doDownload(this)'>下载</button>" +"<button id='dltBtm' class='btn btn-danger btn-sm' type=\"submit\" onclick='doDelete(this)'>删除</button>" + "</td></tr>";
        }
        out += "</tbody>";
        document.getElementById("file_list").innerHTML = out;
    })
    .catch(error => console.log('error', error));