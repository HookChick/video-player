<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注   册</title>
<link type="text/css" rel="stylesheet" href="css/style.css" />
</head>
<body>
<div id="header" class="wrap">
	<img src="images/logo.gif" />
</div>
<div id="register" class="box">
	<h2>新用户注册</h2>
	<div class="content">
	   <form method="post" action="doRegist">
			<dl>
				<dt>用户名：</dt>
				<dd><input type="text" class="input-text" name="userName" value=""/></dd>
				<dt>密码：</dt>
				<dd><input type="password" class="input-text" name="password" value=""/></dd>
				<dt>确认密码：</dt>
				<dd><input type="password" class="input-text" name="confirmPassword" value=""/></dd>
				<dt></dt>
				<dd><input type="submit" class="input-button" name="submit" value="" /></dd>
			</dl>
		</form>
		<div class="error">  </div>
	</div>
</div>
<div id="footer" class="wrap">
	Copyright &copy 2013 南京农业大学版权所有<br>地址：中国南京卫岗1号  邮编：210095  校办电话：86-25-84395366   招办电话：400-1120-200
</div>
</body>
</html>