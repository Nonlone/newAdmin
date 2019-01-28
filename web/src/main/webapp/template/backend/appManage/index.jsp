<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="../../common/import-tags.jsp" %>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="../../common/import-static.jsp" %>
</head>
<body>
<div class="container">
    <!-- 查询 -->
    <form id="searchForm" class="form-horizontal search-form">
        <div class="row">

            <div class="control-group span_width">
                <label class="control-label">应用编码:</label>
                <div class="controls">
                    <input type="text" class="input-small control-text" name="search_LIKE_code">
                </div>
            </div>

            <div class="control-group span_width">
                <label class="control-label">应用名称:</label>
                <div class="controls">
                    <input type="text" class="input-small control-text" name="search_LIKE_name">
                </div>
            </div>

            <div class="control-group span_width">
                <label class="control-label">Android(APPID):</label>
                <div class="controls">
                    <input type="text" class="input-small control-text" name="search_LIKE_appId">
                </div>
            </div>

            <div class="control-group span_width">
                <label class="control-label">IOS(BundleID):</label>
                <div class="controls">
                    <input type="text" class="input-small control-text" name="search_LIKE_bundleId">
                </div>
            </div>

            <div class="control-group span_width">
                <label class="control-label">创建时间:</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendarStart calendar-time" name="search_GTE_createdTime" data-tip="{text : '开始日期'}">
                    <span>
             - </span><input name="search_LTE_createdTime" type="text" class="calendar-time calendarEnd" data-tip="{text : '结束日期'}">
                </div>
            </div>
            <div class="span1 offset2">
                <button type="button" id="btnSearch" class="button button-primary">搜索</button>
            </div>
            <div class="span1 offset2">
                <button class="button button-danger" onclick="flushall();">清空</button>
            </div>
        </div>
    </form>
    <!-- 修改新增 -->
    <div id="addOrUpdate" class="hide">
        <form id="addOrUpdateForm" class="form-inline">
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>应用编码:</label>
                    <div class="controls">
                        <input id="code" name="code" type="text"
                               data-rules="{required:true,channel:true}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>应用名称:</label>
                    <div class="controls">
                        <input name="name" type="text"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">Android(APPID):</label>
                    <div class="controls">
                        <input name="appId" type="text"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">IOS(BundleID):</label>
                    <div class="controls">
                        <input name="bundleId" type="text"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>

            <input type="hidden" name="id" value="">
        </form>
    </div>
    <div class="search-grid-container">
        <div id="grid"></div>
    </div>
</div>

<script type="text/javascript">

    function flushall() {
        var elementsByTagName = document.getElementsByTagName("input");
        for (var i = 0; i < elementsByTagName.length; i++) {
            elementsByTagName[i].innerText = "";
        }
    }


    BUI.use(['bui/ux/crudgrid','bui/calendar','bui/form'], function (CrudGrid,Calendar,Form) {

        Form.Rules.add({
            name: 'channel',
            msg: '不允许包含非英文或数字的标识',
            validator: function (value, baseValue, formatMsg) {
                var regexp = new RegExp(/^[0-9a-zA-Z_]+$/g)
                if (!regexp.test(value)) {
                    return formatMsg
                }
            }
        });

        var datepickerStart = new Calendar.DatePicker({
            trigger:'.calendarStart',
            showTime : true,
            lockTime : { //可以锁定时间，hour,minute,second
                hour : 00,
                minute:00,
                second : 00,
                editable : true
            },
            editable : true,
            autoRender : true

        });

        var datepickerEnd = new Calendar.DatePicker({
            trigger:'.calendarEnd',
            showTime : true,
            lockTime : { //可以锁定时间，hour,minute,second
                hour : 23,
                minute:59,
                second : 59,
                editable : true
            },

            autoRender : true

        });

        //定义页面权限
        var add = false, update = false, del = false, list = false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/appVersion"/>

        var enumObj = {"1": "是", "0": "否"};

        var columns = [
            {title: 'id', dataIndex: 'id', width: '5%'},
            {title: '应用编码', dataIndex: 'code', width: '10%'},
            {title: '应用名称', dataIndex: 'name', width: '10%'},
            {title: 'Android(APPID)', dataIndex: 'appId', width: '10%'},
            {title: 'IOS(BundleID)', dataIndex: 'bundleId', width: '10%'},
            {title: '创建时间', dataIndex: 'createdTime', width: '10%', renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '更新时间', dataIndex: 'updateTime', width: '10%', renderer: BUI.Grid.Format.datetimeRenderer}

        ];

        var crudGrid = new CrudGrid({
            entityName: 'APP管理',
            pkColumn: 'id',//主键
            storeUrl: '${ctx}/backend/appManage/list',
            addUrl: '${ctx}/backend/appManage/add',
            updateUrl: '${ctx}/backend/appManage/update',
            removeUrl: '${ctx}/backend/appManage/del',
            columns: columns,
            showAddBtn: add,
            showUpdateBtn: update,
            showRemoveBtn: del,
            addOrUpdateFormId: 'addOrUpdateForm',
            dialogContentId: 'addOrUpdate',
            gridCfg: {
                innerBorder: true
            },
            storeCfg: {//定义store的排序，如果是复合主键一定要修改
                sortInfo: {
                    field: 'id',//排序字段
                    direction: 'DESC' //升序ASC，降序DESC
                }
            }
        });

        var beforeUpdateShow = function(dialog,form,record){
            form.getField("code").disable();
        };

        crudGrid.on('beforeUpdateShow', beforeUpdateShow);
    });


</script>

</body>
</html>


