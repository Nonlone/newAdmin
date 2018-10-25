<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="./common/import-tags.jsp"%>
<title><spring:eval expression="@webConf['admin.title']" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="./common/import-static.jsp"%>
<style type="text/css">
body {
	background: url('${ctx}/static/bui/img/login_bg.png') no-repeat center top #068abb;
}

#loginForm {
	
}

.loginBox {
	color: #333;
	font-family: 'Microsoft YaHei';
	font-size: 14px;
	height: 460px;
	left: 50%;
	margin-left: 150px;
	padding: 0 0px;
	position: absolute;
	top: 215px;
	width: 420px;
	background: #ffffff;
	/* 	background-color: #e6f2ff; */
}

#submitCode {
	width: 40px;
}

#loginForm {
	width: 320px;
	height: auto;
	float: left;
	margin: 0 50px;
	float: left;
}

.login-container {
	width: 1200px;
	height: auto;
	min-height: 768px;
	margin: 0 auto;
	position: relative;
}

.log-title {
	width: 100%;
	height: 40px;
	line-height: 40px;
	text-align: left;
	float: left;
	font-size: 20px;
	color: #333;
	margin-top: 40px;
	letter-spacing: 0.5px;
}

.log-title span {
	width: 4px;
	height: 18px;
	display: block;
	float: left;
	margin-right: 46px;
	background: #1BB3C9;
	margin-top: 11px;
}

.log-input {
	width: 278px!important;
	height: 44px!important;
	line-height: 44px!important;
	border: 1px solid #D5D7DD!important;
	font-size: 14px!important;
	padding: 0 0 0 42px!important;
	float: left;
	margin-top: 16px!important;
	border-radius: 2px!important;
}

.row {
	margin-left: 0!important;
}

.form-horizontal .controls {
	width: 100%!important;
	height: auto!important;
	float: left!important;
	position: relative!important;
	margin-left: 0!important;
}

.bui-form-tip {
	left: 42px!important;
}

.log-user-name {
	background: url('${ctx}/static/bui/img/icon_login_account.png') no-repeat 10px center;
}

.log-pw {
	background: url('${ctx}/static/bui/img/icon_login_pw.png') no-repeat 10px center;
}

.log-pw {
	background: url('${ctx}/static/bui/img/icon_login_pw.png') no-repeat 10px center;
}

.log-valid {
	width: 178px!important;
	padding-right: 100px!important;
	background: url('${ctx}/static/bui/img/icon_login_validation.png') no-repeat 10px center;
}

.form-horizontal .controls #code {
	width: 80px!important;
	height: 30px!important;
	position: absolute;
	top: 24px;
	right: 18px;
	float: right;
	z-index: 99;
}

.form-horizontal .controls #rememberMe {
	margin-top: 16px;
	margin-bottom: 32px;
	width: 100%;
	float: left;
}

.remember-check-btn {
	margin-right: 5px;
	color: #333;
}

#loginBtn {
	width: 150px;
	height: 44px;
	color: #fff;
	background: #1BB3C9;
	border-radius: 2px;
	font-size: 14px;
	float: left;
	border-color: #1BB3C9;
}

.log-reset-btn {
	width: 150px;
	height: 44px;
	color: #333;
	background: #FFFFFF;
	border: 1px solid #D5D7DD;
	border-radius: 2px;
	font-size: 14px;
	float: right;
}

</style>
</head>
<body>
	<div class="login-container">
		<div class="loginBox ">
			<fieldset>
				<p class="log-title"><span></span>登录</p>
				<form class="form-horizontal" id="loginForm" method="post" action="${ctx}/loginForm">
					<div class="row">
						<div class="control-group ">
							<div class="controls">
								<input type="text" class="log-input log-user-name" 
									data-rules="{required : true}" value="${loginName}"
									data-messages="{required:'用户名不能为空'}" name="username" placeholder="请输入用户名">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="control-group ">
							<div class="controls">
								<input type="password" class="log-input log-pw"
									data-rules="{required : true}"
									data-messages="{required:'密码不能为空'}" name="password" placeholder="请输入密码">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="control-group">
							<div class="controls">
								<input type="text" class="log-input log-valid span2"
									data-rules="{required : true}"
									data-messages="{required:'请输入验证码'}" name="code" placeholder="请输入验证码">
								<img src="${ctx}/kaptcha" style="width:100px;height:36px;" id="code" alt="看不清楚？换一张" title="看不清楚？换一张"/>
							</div>
						</div>
					</div>
					<div class="row"> 
						<div class="control-group">
							<div class="controls" >
	                          <span id="rememberMe">
								<input name="rememberMe" class="remember-check-btn" type="checkbox" value="true"/>七天内免登录</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="">
							<button class="button button-primary" id="loginBtn" type='submit'>登录</button>
							&nbsp;
							<button class="log-reset-btn" type="reset">重置</button>
						</div>
					</div>
					<div id="errorDiv" style="display: none">
						<span class="tips tips-warning tips-small"></span>
					</div>
				</form>
			</fieldset>

		</div>
	</div>
	<script type="text/javascript">
		BUI.use([ 'bui/form', 'bui/tooltip' ], function(Form, Tooltip) {
			var loginForm = new Form.Form({
				srcNode : '#loginForm',
				submitType : 'ajax',
				action : ctx + '/loginForm',
				method : 'post',
				callback : function(data) {
					if (data.success) {
						location.href = ctx + '/home';
					} else {
						$('#errorDiv span').text(data.msg);
						$('#errorDiv').show();
						changeCode();
						hide();
					}
				}
			}).render();
			
			$('#code').click(function(){
				changeCode();
			});
			
			function changeCode(){
				var time = new Date().getTime();
				$('#code').attr('src','${ctx}/kaptcha?_t='+time);
			}
			
		      //使用模板右边显示
		      new Tooltip.Tip({
		        trigger : '#rememberMe',
		        alignType : 'right',
		        offset : 9,
		        title : '右边的提示信息',
		        elCls : 'tips tips-warning', 
		        titleTpl : '<span class="x-icon x-icon-small x-icon-error"><i class="icon icon-white icon-bell"></i></span>\
		        <div class="tips-content">为了您的信息安全，请不要在网吧或公用电脑上使用此功能！</div>'
		      }).render();
		      
		});
		function hide() {
			setTimeout(function() {
				$('#errorDiv span').text('');
				$('#errorDiv').hide();
			}, 3000);
		}
		
		if (window != top) {
			top.location.href = location.href;
		}
	</script>
</body>
</html>
