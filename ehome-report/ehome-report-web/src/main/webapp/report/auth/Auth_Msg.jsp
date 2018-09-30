﻿<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>授权信息</title>
<script type="text/javascript" src="../lib/jquery.min.js"></script>
<script type="text/javascript" src="../lib/layer/layer.js"></script>
<link rel="stylesheet" href="../lib/layer/theme/default/layer.css" />
<style type="">
	.btn{
		height: 38px;
	    line-height: 38px;
	    padding: 0 18px;
	    background-color: #009688;
	    color: #fff;
	    white-space: nowrap;
	    text-align: center;
	    font-size: 14px;
	    border: none;
	    border-radius: 2px;
	    cursor: pointer;
	    width: 150px;
	    margin-bottom: 15px;
	}
	.show{
		background: white;
	    width: 300px;
	    border-radius: 4px;
	    margin: auto;
	    -moz-box-shadow:1px 2px 12px #333333; 
   		-webkit-box-shadow:1px 2px 12px #333333; 
   		box-shadow:1px 2px 12px #333333;
	}
	.title{
		text-align: center;
	    padding: 20px;
	    font-size: 16px;
	    font-weight: 500;
	}
	.content{
		width: 90%;
   		margin: auto;
    	height: 260px;	
	}
	.button{
		text-align: center;
	}
	.un_auth{
		color:red;
	}
	.success_auth{
		color:green;
	}
	.downJihuo {
	    border: 1px solid #e1e2e2;
	    padding: 1.5em 2em;
	    margin: 0em 2em 2em 2em;
	    color: #000;
	    font-size: 14px;
	    line-height: 30px;
	}
	h1, h2, h3, h4, h5, h6 {
	    font-weight: normal;
	    font-size: 100%;
	    margin: 0;
	    padding: 0;
	    color: #000;
	}
	.downZhu {
	    color: #ff4650;
	}
	.downZhu a {
	    color: #ff4650;
	    font-weight: bold;
	}
</style>
<script type="text/javascript">
	function activation(){
		layer.open({
		  type: 2,
		  title: '激活处理页面',
		  shadeClose: true,
		  shade: 0.8,
		  area: ['580px', '320px'],
		  content: ['../../ActivationServletReport','no'] //iframe的url
		});
	};	
</script>
</head>
<body style="min-width:1250px">
	<div style="position: fixed; width: 500px;z-index: -1;">
		<div class="downJihuo">
			<p>报表授权开发使用时,可以不授权，在部署生产系统再进行授权。</p>
			<h4 style="font-weight: bold;">报表授权步骤：</h4>
			<p>① 注册并登陆，然后在线购买报表工具，下载报表工具通用版。（限时免费活动，免费获取授权码）</p>
			<p>② 在报表工具中获取机器码，将机器码、订单号、用户名发送到官方邮箱(support@mftcc.cn)</p>
			<p>③ 订单信息审核通过后，并将激活码发送至注册账号时的邮箱中</p>
			<p>④ 使用"步骤③中的激活码"进行激活（如不激活报表展现时会有水印信息）</p>
			<p>⑤ 开始使用</p>
			<p class="downZhu">* 注：RDP报表工具学习交流群：
				<a target="_blank" href="https://jq.qq.com/?_wv=1027&amp;k=5QTurbk">608126991（点击进群）</a>
			</p>
		</div>
	</div>
	<div class="show">
	   <div class="title">授权信息</div>
	   <div class="content">
	   		<div>
				<ul style="line-height: 28px;">
					<li>授权名称：报表工具</li>
					<li>授权版本：v2.0.3</li>
					<li>系&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;统：Windows</li>
					<li>授权类型：永久授权</li>
					<li>激活状态：<lable class="auth success_auth">未激活</lable></li>
					<li>机器码：65-erawMV1F604000FFBFB8F0</li>
				</ul>	   		
	   		</div>
	   </div>
	   <div class="button">
	   		<input type="button" class="btn" value="激活" onclick="activation()">
	   </div>
	</div>
</body>
</html>