<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<link href="${ctx}/static/bui/css/${skin}/bs3/dpl-min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/static/bui/css/${skin}/bs3/bui-min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/static/bui/css/${skin}/page-min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/static/iconfont/css/iconfont.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${ctx}/static/common/js/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="${ctx}/static/common/js/template.js"></script>
<script type="text/javascript" src="${ctx}/static/bui/bui-min.js"></script>
<script>
    var booleanEnumRender = {"true": "是", "false": "否"};
    var $ctx = '${pageContext.request.contextPath}';

    BUI.actions = {};
    BUI.setDebug(<spring:eval expression="@webConf['bui.debug']" />);

    //ajax
    // 设置 application/json
    // 设置添加contextPath前缀
    $(function () {
        $.ajaxSetup({
            dataType: "json",
            beforeSend:function(request,object){
                var url = object.url;
                if(!url.startsWith("http")&&!url.startsWith($ctx)){
                    object.url = $ctx + url;
                }
            }
        });

        //放大图片
        $('img.dialog').on('click', function () {
            var large_image = '<img class=\'closeImg\' style=\'max-height: 800px;max-width: 800px\' src= ' + $(this).attr("src") +'></img>';
            BUI.use('bui/overlay', function (Overlay) {
                var width = this.width;
                var height = this.height;
                var dialog = new Overlay.Dialog({
                    title: '图片放大',
                    width: width,
                    height: height,
                    mask: false,
                    buttons: [],
                    bodyContent: large_image
                });
                dialog.show();
            });
        });

        template.defaults.imports.log = console.log;

        template.defaults.imports.getCtx = function(){
            return $ctx;
        };

        template.defaults.imports.notEmpty = function(object){
            return !jQuery.isEmptyObject(object);
        };

        template.defaults.imports.dateFormat = function (data) {
            var pattern = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
            var formatedDate = data.replace(pattern, '$1-$2-$3 $4:$5:$6');
            console.log(data,formatedDate);
            return formatedDate;
        }

    })
</script>
<script type="text/javascript" >

    function autoSetIframeHeight(id) {
        var ifm= document.getElementById(id);
        var subWeb = document.frames ? document.frames[id].document :ifm.contentDocument;
        if(ifm != null && subWeb != null) {
            ifm.height = subWeb.body.scrollHeight;
        }
    }

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
