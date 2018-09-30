<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>报表系统</title>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="报表">
<meta http-equiv="description" content="报表系统">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="<%=basePath%>/report/common/images/favicon.png" type="image/png" />
<script src="<%=basePath%>/report/lib/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/report/login/js/sweetalert.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/report/login/css/normalize.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/report/login/css/styles.css">
<style type="text/css">
</style>
</head>
<body>
	<div id="container">
		<video id="background_video" loop muted></video>
		<div id="video_cover"></div>
		<div id="overlay"></div>
		<div id="video_controls">
			<span id="play"> <%-- <img src="<%=basePath%>/login/img/play.png"> --%>
			</span> <span id="pause"> <%-- <img src="<%=basePath%>/login/img/pause.png"> --%>
			</span>
		</div>
	</div>
	<div class="panel-body">
		<form action="login.action" method="post">
			<div class="panel-lite">
				<!-- <div class="thumbur">
					<div class="icon-lock"></div>
				</div> -->
				<div class="header-img"></div>
				<h4>报表系统登录</h4>
				<div class="form-group">
					<input required="required" name="username" class="form-control" value="admin"/>
					<label class="form-label">用户名 </label>
				</div>
				<div class="form-group">
					<input required="required" type="password" name="password" class="form-control" value=""/> 
					<label class="form-label">密 码</label>
				</div>
				<small class="pwd-tips">密码：111111</small>
				<!-- <a href="#">忘记密码 ? </a> -->
				<button title="登录" type="button" id="loginBtn" class="floating-btn">
					<i class="icon-arrow"></i>
				</button>
			</div>
		</form>
	</div>
	<script type="text/javascript" src="<%=basePath%>/report/login/js/bideo.js"></script>
	<script type="text/javascript">
		(function() {
			var bv = new Bideo();
			bv.init({
				// Video element
				videoEl : document.querySelector('#background_video'),
				// Container element
				container : document.querySelector('body'),
				resize : true,
				// autoplay: false,
				isMobile : window.matchMedia('(max-width: 768px)').matches,
				playButton : document.querySelector('#play'),
				pauseButton : document.querySelector('#pause'),
				src : [
					{
						src : '<%=basePath%>/report/login/video/bg.mp4',
						type : 'video/mp4'
					}
				],
				onLoad : function() {
					document.querySelector('#video_cover').style.display = 'none';
				}
			});
			var $username = $('input[name=username]');
			var $password = $('input[name=password]');
			$("#loginBtn").bind("click",loginFun);
			$username.on('input propertychange',function(){
				if($(this).val()){
					if($(this).hasClass("required")){
						$(this).removeClass("required");
						$(this).attr("placeholder","");
					}
				}else{
					$(this).attr("placeholder","用户名不能为空！");
					if(!$(this).hasClass("required")){
						$(this).addClass("required");
					}
				}
				
			});
			$password.on('input propertychange',function(){
				if($(this).val()){
					if($(this).hasClass("required")){
						$(this).removeClass("required");
						$(this).attr("placeholder","");
					}
				}else{
					$(this).attr("placeholder","密码不能为空！");
					if(!$(this).hasClass("required")){
						$(this).addClass("required");
					}
				}
			});
			$(document).keyup(function (e) {//捕获文档对象的按键弹起事件  
			    if (e.keyCode == 13) {//按键信息对象以参数的形式传递进来了  
			        //此处编写用户敲回车后的代码  
			        loginFun();
			    }  
			}); 
			
			function loginFun(){
				var username = $username.val();
				var password = $password.val();
				if(!username){
					$username.addClass("required");
					$username.attr("placeholder","用户名不能为空！");
					$username.focus();
					return false;
				}
				if(!password){
					$password.addClass("required");
					$password.attr("placeholder","密码不能为空！");
					$username.focus();
					return false;
				}
				
				$.ajax({
					url:"/LoginActionAjax_loginAjax.action",
					data:{
						username:username,
						password:password
					},
					datatype:"json",
					success:function(result){
						if(result.flag){
							location.href="index.jsp";
							/* swal({
								  title: " ",
								  text: "",
								  icon: "success",
								  timer:1000,
								  button: false
								}).then(function(){
									location.href="index.jsp";
								}); */
							
						}else{
							swal({ 
							  title: result.msg, 
							  text: "2秒后自动关闭。", 
							  timer: 2000, 
							  button: false 
							});
						}
				    }
			    });
			}
		}());
	</script>
</body>
</html>
