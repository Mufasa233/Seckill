//为所有的ajax请求设置默认的属性
$.ajaxSetup({
    crossDomain: true,//允许跨域
    xhrFields: {withCredentials: true} //允许携带跨域的cookie
});
//展示loading
function showLoading(){
	var idx = layer.msg('处理中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: '0px', time:100000}) ;  
	return idx;
}
//salt
var passsword_salt="1a2b3c4";
//获取地址栏参数
function getQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null) return unescape(r[2]);
    return null;
}
function getUuid(userId){
    var date = new Date();
    var uuid = ""+userId+date.getDay()+ date.getHours()+ date.getMinutes()+date.getSeconds()+date.getMilliseconds();
    return uuid;
}
