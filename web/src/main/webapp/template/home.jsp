<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="./common/import-tags.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']"/></title>
    <%@include file="./common/import-static.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <link href="${ctx}/static/bui/css/${skin}/main-min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/static/bui/css/${skin}/page-min.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<div class="header">
    <div class="logo">
        <img src="${ctx}/static/bui/img/${skin}/bg_top_banner.png">
    </div>
    <div class="dl-title">

    </div>

    <div class="dl-log">
        <span id="clock"></span>&nbsp;&nbsp;
        欢迎您，<i class="hd-account-bg"></i><span class="dl-log-user"><shiro:principal/></span>&nbsp;&nbsp;
        <i class="hd-pw-bg"></i><a href="#" title="修改密码" class="dl-log-quit" id="changePassword">修改密码</a>&nbsp;&nbsp;
        <i class="hd-logout-bg"></i><a href="#" title="退出系统" class="dl-log-quit" id="logout">退出</a>

    </div>
    <div class="dl-main-nav">
        <ul id="J_Nav" class="nav-list ks-clear">

        </ul>
    </div>
</div>


<div class="content">
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
</div>
<div id="chPwdFormDiv" class="hide">
    <form id="chPwdFormId" class="form-horizontal " method="post">
        <div id="oldPasswordDiv" class="control-group">
            <label class="control-label"><s>*</s>原密码：</label>
            <div class="controls">
                <input id="oldPassword" name="oldPassword" type="password" class="input-normal"
                       data-messages="{required:'原密码不能为空'}" data-rules="{required : true}">
            </div>
        </div>
        <div id="passwordDiv" class="control-group">
            <label class="control-label"><s>*</s>新密码：</label>
            <div class="controls">
                <input id="plainPassword" name="plainPassword" type="password" class="input-normal"
                       data-messages="{required:'密码不能为空'" data-rules="{required:true,minlength:6,password:true}">
            </div>
        </div>
        <div id="repasswordDiv" class="control-group">
            <label class="control-label"><s>*</s>新密码确认：</label>
            <div class="controls">
                <input id="plainPassword1" name="plainPassword1" type="password" class="input-normal"
                       data-messages="{required:'确认密码不能为空'}" data-rules="{required : true,equalTo:'#plainPassword'}">
            </div>
        </div>
    </form>
</div>
<script>
    BUI.use(['bui/ux/savedialog', 'bui/form', 'bui/common/main'], function (SaveDialog, Form) {
        $(window).resize(function () {
            var height = BUI.viewportHeight() - 70;
            $('.dl-second-nav,.bui-nav-tab,.bui-nav-tab-hover').height(height);
            $('.tab-content-container').height(height - 21);
        });
        //添加 密码校验规则
        Form.Rules.add({
            name: 'password',  //规则名称
            msg: '密码必须同时包括数字和字母',//默认显示的错误信息
            validator: function (value, baseValue, formatMsg) { //验证函数，验证值、基准值、格式化后的错误信息
                var d = new RegExp('\\d+');
                var w = new RegExp('[a-zA-Z]+');
                var a = d.test(value);
                var b = w.test(value);
                if (!(d.test(value) && w.test(value))) {
                    return formatMsg;
                }
            }
        });

        $.ajax({
            url: '/home/loadMenu',
            cache: false,
            success: function (data) {
                var navs = data;
                //var active="";
                for (var i = 0; i < navs.length; i++) {
                    var nav = navs[i];
                    if (i == 0 || i == 1 || i == 2) {
                        active = 'dl-selected';
                    }
                    $('<li class="nav-item ' + active + '">' +
                        '<div class="nav-item-inner">' +
                            '<i class="iconfont ' + nav.icon + '-1 icon_nav"></i>' + nav.text + '' +
                        '</div>' +
                    '</li>').appendTo($('#J_Nav'));
                }
                new PageUtil.MainPage({
                    modulesConfig: navs
                });
            },
            failure: function (data) {
                BUI.Message.Alert('加载菜单列表失败，请稍后再试！');
            }
        });


        var saveDialog = new SaveDialog({
            entityName: '密码',
            url: {
                update: '${ctx}/home/changePasswd'
            },
            dialogContentId: 'chPwdFormDiv',
            formId: 'chPwdFormId'
        });


        $('#changePassword').click(function () {
            saveDialog.update();
        });
        $('#logout').click(function () {
            BUI.Message.Confirm('您确定要退出系统吗？？', function () {
                location.href = ctx + "/logout";
            }, 'question');
        });
    });

    window.setInterval('realSysTime()', 1000);    //实时获取并显示系统时间
    function realSysTime() {
        var now = new Date();            //创建Date对象
        var year = now.getFullYear();    //获取年份
        var month = now.getMonth();    //获取月份
        var date = now.getDate();        //获取日期
        var day = now.getDay();        //获取星期
        var hour = now.getHours();    //获取小时
        if (hour < 10) {
            hour = "0" + hour;
        }
        var minu = now.getMinutes();    //获取分钟
        if (minu < 10) {
            minu = "0" + minu;
        }
        var sec = now.getSeconds();    //获取秒钟
        if (sec < 10) {
            sec = "0" + sec;
        }
        month = month + 1;
        var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
        var week = arr_week[day];        //获取中文的星期
        var time = year + "年" + month + "月" + date + "日 " + week + " " + hour + ":" + minu + ":" + sec;    //组合系统时间
        $('#clock').text(time);    //显示系统时间
    }

</script>
</body>
</html>
