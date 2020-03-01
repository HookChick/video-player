<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录首页</title>
<link type="text/css" rel="stylesheet" href="css/style.css" />
<script type="text/javascript">
	window.onload = function() {
		var input = document.getElementById("loginBox").getElementsByTagName(
				"input");
		for (var i = 0; i < input.length; i++) {
			var type = input[i].getAttribute("type");
			if (type == "text" || type == "password") {
				input[i].onfocus = function() {
					this.className += " input-text-over";
				};
				input[i].onblur = function() {
					this.className = this.className.replace(/input-text-over/,
							"");
				};
			} else if (type == "submit") {
				input[i].onmouseover = function() {
					this.className += " input-submit-over";
				};
				input[i].onmouseout = function() {
					this.className = this.className.replace(
							/input-button-over/, "");
				};
			}
		}
	};
</script>
</head>
<body>
	<div id="header" class="wrap">
		<img src="images/logo.gif" />
	</div>
	<div id="login" class="wrap">
		<div class="main">
			<div class="corner"></div>
			<div class="introduce">
				<h2>张灿</h2>
				<p>南京农业大学毕业设计...</p>
			</div>
			<div class="login">
				<h2>用户登录</h2>
				<form method="post" action="userServlet?method=doLogin">
					<dl id="loginBox">
						<dt>用户名：</dt>
						<dd>
							<input type="text" class="input-text" name="username" value="" />
						</dd>
						<dt>密 码：</dt>
						<dd>
							<input type="password" class="input-text" name="password"
								value="" />
						</dd>
						<dt></dt>
						<dd>
							<input type="submit" class="input-button" name="submit"
								value="登录" /> <a href="register.jsp">新用户注册</a>
						</dd>
					</dl>
				</form>
				<div class="error"> </div>
			</div>
		</div>
	</div>
	<div class="wrap"></div>
	<div id="footer" class="wrap">Copyright &copy 2013 南京农业大学版权所有<br>地址：中国南京卫岗1号  邮编：210095  校办电话：86-25-84395366   招办电话：400-1120-200</div>
</body>
</html>