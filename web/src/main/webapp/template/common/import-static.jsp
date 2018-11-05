<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="${ctx}/static/bui/css/${skin}/bs3/dpl-min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/static/bui/css/${skin}/bs3/bui-min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/static/bui/css/${skin}/page-min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/static/iconfont/css/iconfont.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${ctx}/static/common/js/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="${ctx}/static/artTemplate/dist/template.js"></script>
<script type="text/javascript" src="${ctx}/static/bui/bui-min.js"></script>

<script>
    var ctx = '${pageContext.request.contextPath}';
    BUI.actions = {};
    BUI.setDebug(<spring:eval expression="@webConf['bui.debug']" />);

    //ajax 设置 application/json
    $(function () {
        $.ajaxSetup({
            dataType: "json",
        })
    })
</script>
<script type="text/javascript" src="${ctx}/static/bui/ux/crudgrid.js"></script>
<script type="text/javascript" src="${ctx}/static/bui/ux/savedialog.js"></script>

<style type="text/css">
    body {
        background: none repeat scroll 0 0 #F9F9F9;
    }

    .bui-select .bui-select-input {
        width: 60px;
    }

    .form-horizontal .control-label {
        width: 85px;
    }

    .bui-bar .bui-grid-button-bar {
        padding-bottom: 15px;
        /*padding-left: 25px;*/
    }

    .bui-grid {
        margin-bottom: 30px;
    }

    .panel-header {
        background-color: #FBFBFB;
    }

    .bui-select-list .bui-list-item {
        height: 25px;
        margin: 1px;
        border-bottom: 1px solid #dddddd;
    }

    /*search tool bar start*/
    .search-form {
        background-color: #f5f5f5;
        padding: 12px 0px 0px 0px;
        margin: 0px 0px;
        border: 1px solid #ddd !important;
        margin-bottom: 10px;
        border-radius: 3px;

    }

    .span_width {
        width: auto;
    }

    .puff-left {
        float: left;
    }

    .height_auto {
    }

    /*search tool bar end*/
</style>

<script type="text/javascript">

    document.onkeydown = function (e) {
        if (!e) {
            e = window.event;
        }
        if ((e.keyCode || e.which) == 13) {

            $("#btnSearch").click();
        }
    }

    $.ajaxSetup({
        complete: function (xhr, status) {
            if (xhr.status == 403) {
                BUI.Message.Alert('您没有权限进行此操作或登录超时！', function () {
                    window.location.href = '${ctx}/<spring:eval expression="@webConf['admin.login']" />';
                }, 'error');
            }
        }
    });

    function searchBtn() {
        document.getElementById("btnSearch").click();
    }

</script>
