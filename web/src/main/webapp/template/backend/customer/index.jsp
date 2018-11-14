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
            <div class="control-group span7">
                <label class="control-label">客户Id:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_EQ_userId" value="${userId}">
                </div>
            </div>

            <div class="control-group span7">
                <label class="control-label">姓名:</label>
                <div class="controls">
                    <input type="text" class="input-small control-text" name="search_LIKE_name">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">身份证号:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_idCard">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">性别:</label>
                <div class="controls bui-form-field-select" data-items="{' ':'全部','男':'男','女':'女'}"
                     class="control-text input-small">
                    <input name="search_EQ_sex" type="hidden" value="" onchange="searchBtn();">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">民族:</label>
                <div class="controls">
                    <input type="text" class="input-small control-text" name="search_LIKE_nation">
                </div>
            </div>


            <div class="control-group span10">
                <label class="control-label">注册时间:</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar" name="search_GTE_createdTime_D" data-tip="{text : '开始日期'}">
                    <span>
             - </span><input name="search_LTE_createdTime_D" type="text" class="calendar" data-tip="{text : '结束日期'}">
                </div>
            </div>
            <div class="span1 offset2">
                <button type="button" id="btnSearch" class="button button-primary">搜索</button>
            </div>
            <div class="span1 offset2">
                <button class="button button-danger" onclick="flushall();">清空</button>
            </div>
            <div>

            </div>
        </div>
    </form>
    <!-- 修改新增 -->
    <div id="addOrUpdate" class="hide">
        <form id="addOrUpdateForm" class="form-inline">

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

    BUI.use(['bui/ux/crudgrid'], function (CrudGrid) {

        //定义页面权限
        var add = false, update = false, del = false, list = false;
        <framwork:crudPermission resource="/backend/customer"/>

        var columns = [
            {title: '客户Id', dataIndex: 'userId', width: '13%'},
            {title: '姓名', dataIndex: 'name', width: '10%'},
            {title: '身份证号', dataIndex: 'idCard', width: '15%'},
            {title: '性别', dataIndex: 'sex', width: '10%'},
            {title: '生日', dataIndex: 'birthday', width: '10%'},
            {title: '年龄', dataIndex: 'age', width: '10%'},
            {title: '民族', dataIndex: 'nation', width: '10%'},
            {title: '注册时间', dataIndex: 'createdTime', width: '15%', renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '签发机关', dataIndex: 'orgination', width: '15%'},
            {title:'是否实名',dataIndex:'certified',width:'10%',renderer:BUI.Grid.Format.enumRenderer(booleanEnumRender)}
        ];

        var detailUrl =  "/backend/customer/detail/";

        var crudGrid = new CrudGrid({
            entityName: '用户信息',
            pkColumn: 'id',//主键
            storeUrl: '/backend/customer/list',
            addUrl: '/backend/customer/add',
            updateUrl: '/backend/customer/update',
            removeUrl: '/backend/customer/del',
            columns: columns,
            showAddBtn: add,
            showUpdateBtn: update,
            showRemoveBtn: del,
            addOrUpdateFormId: 'addOrUpdateForm',
            dialogContentId: 'addOrUpdate',
            operationColumnRenderer: function (value, obj) {//操作列最追加按钮
                return CrudGrid.createLink({
                        id: obj.id,
                        title: obj.name + '—客户信息',
                        text: '<li class="icon icon-list-alt"></li>',
                        href: $ctx+"/backend/customer/detail/" + obj.userId
                    });
            },
            storeCfg: {//定义store的排序，如果是复合主键一定要修改
                sortInfo: {
                    field: 'userIn.createdTime',//排序字段
                    direction: 'DESC' //升序ASC，降序DESC
                }
            }
        });
    });

    function openDetail() {

    }

</script>

</body>
</html>


