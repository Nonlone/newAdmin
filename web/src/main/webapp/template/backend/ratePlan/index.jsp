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
                <label class="control-label">方案名称:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_name">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">资金方ID:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_EQ_fundId">
                </div>
            </div>
            <div  class="control-group span7">
                <label class="control-label">产品ID:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_EQ_productId">
                </div>
            </div>
            <!--
            <div class="control-group span7">
                <label class="control-label">是否启用</label>
                <div class="controls">
                    <select name="search_EQ_enable">
                        <option value="" selected="selected">全部</option>
                        <option value="1">已启用</option>
                        <option value="0">已停用</option>
                    </select>
                </div>
            </div>
            -->
            <div class="control-group span12">
                <label class="control-label">创建时间:</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createdTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar-time calendar" name="search_GTE_createdTime" data-tip="{text : '开始日期'}">
                    <span>- </span>
                    <input name="search_LTE_createdTime" type="text" class="calendar-time calendar" data-tip="{text : '结束日期'}">
                </div>
            </div>
            <div class="span3 offset2">
                <button type="button" id="btnSearch" class="button button-primary">搜索</button>
            </div>
        </div>
    </form>
    <!-- 修改新增 -->
    <div id="addOrUpdate" class="hide">
        <form id="addOrUpdateForm" class="form-inline">
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>方案代码:</label>
                    <div class="controls">
                        <input name="code" type="text"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>方案名称:</label>
                    <div class="controls">
                        <input name="name" type="text"
                               data-rules="{required:true,}" class="input-normal control-text">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>资金方id:</label>
                    <div class="controls">
                        <input name="fundId" type="text" data-rules="{required:true,number:true}" class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>产品ID:</label>
                    <div class="controls">
                        <input name="productId" type="text"
                               data-rules="{required:true,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">0=无效,1=有效:</label>
                    <div class="controls">
                        <input name="enable" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>创建时间:</label>
                    <div class="controls">
                        <input name="createdTime" type="text"
                               data-rules="{required:true,}"
                               class="calendar calendar-time">
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

    BUI.use(['bui/ux/crudgrid', 'bui/common/page'], function (CrudGrid) {

        //定义页面权限
        var add = false, update = false, del = false, list = false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/ratePlan"/>


        var addOrUpdateFunction = function (event, id) {

            var title = '';
            var href = '${ctx}/backend/ratePlan/addOrUpdate';
            if (id) {
                title = '修改费率方案';
                href += '?id=' + id;
            } else {
                title = '添加费率方案';
            }

            if (top.topManager) {
                //打开左侧菜单中配置过的页面
                top.topManager.openPage({
                    id: 'main-menu',
                    href: href,
                    title: title
                });
            }
        }

        var addBtn;
        if (add) {
            addBtn = {
                text: '<i class="icon-plus icon-white"></i>新增',
                btnCls: 'button  button-success',
                handler: addOrUpdateFunction
            };
        }

        var columns = [
            {title: '主键', dataIndex: 'id', width: '10%'},
            {title: '方案名称', dataIndex: 'name', width: '10%'},
            {title: '资金方名称', dataIndex: 'fund', width: '10%',renderer:function (value) {
                if (!jQuery.isEmptyObject(value)) {
                    return value.fundName;
                }else {
                    return '无';
                }
            }},
            {title: '产品名称', dataIndex: 'product', width: '10%',renderer:function (value) {
                if (!jQuery.isEmptyObject(value)) {
                    return value.remark;
                }else {
                    return '无';
                }
            }},
            {title: '版本', dataIndex: 'currentVersion', width: '10%'},
            {title: '是否生效', dataIndex: 'enable', width: '10%',renderer:function (value) {
                    if (value) {
                        return '已启用';
                    }else {
                        return '已停用';
                    }
            }},
            {title: '创建时间', dataIndex: 'createdTime', width: '10%', renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '更新时间', dataIndex: 'updateTime', width: '10%', renderer: BUI.Grid.Format.datetimeRenderer}
        ];

        var crudGrid = new CrudGrid({
            entityName: '产品费率方案表',
            pkColumn: 'id',//主键
            storeUrl: '${ctx}/backend/ratePlan/list',
            addUrl: '${ctx}/backend/ratePlan/add',
            updateUrl: '${ctx}/backend/ratePlan/update',
            removeUrl: '${ctx}/backend/ratePlan/del',
            columns: columns,
            showAddBtn: add,
            showUpdateBtn: false,
            showRemoveBtn: del,
            addOrUpdateFormId: 'addOrUpdateForm',
            dialogContentId: 'addOrUpdate',
            addBtn: addBtn,
            gridCfg: {
                innerBorder: true
            },
            storeCfg: {//定义store的排序，如果是复合主键一定要修改
                sortInfo: {
                    field: 'id',//排序字段
                    direction: 'DESC' //升序ASC，降序DESC
                }
            },
            operationColumnRenderer: function (value, obj) {
                var editStr = '';
                if (update) {
                    editStr = '<span class="grid-command edit" title="修改">修改</span>';
                }
                return editStr;
            }
        });

        var grid = crudGrid.get('grid');
        grid.on('cellclick', function (ev) {
            var sender = $(ev.domTarget); //点击的Dom
            var record = ev.record;
            if (sender.hasClass('edit')) {
                addOrUpdateFunction('', record.id);
            }
        });

    });

</script>

</body>
</html>


