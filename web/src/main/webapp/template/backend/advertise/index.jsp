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
                <label class="control-label">发布时间：</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar-time calendar" name="search_GTE_maintable.publishTime" data-tip="{text : '开始日期'}">
                    <span>
             - </span><input name="search_LTE_maintable.publishTime" type="text" class="calendar-time calendar" data-tip="{text : '结束日期'}">
                </div>
            </div>
            <div class="control-group span_width">
                <label class="control-label">过期时间：</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar-time calendar" name="search_GTE_maintable.expiredTime" data-tip="{text : '开始日期'}">
                    <span>
             - </span><input name="search_LTE_maintable.expiredTime" type="text" class="calendar-time calendar" data-tip="{text : '结束日期'}">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">图片URL:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_maintable.imgUrl">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">超链接:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_maintable.imgHyperlink">
                </div>
            </div>
            <!--
            <div class="control-group span7">
                <label class="control-label">createdTime:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_createdTime">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">updateTime:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_updateTime">
                </div>
            </div>
            -->
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
                    <label class="control-label"><s>*</s>发布时间:</label>
                    <div class="controls">
                        <input name="publishTime" type="text"
                               data-rules="{required:true,}"
                               class="calendar calendar-time">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>过期时间:</label>
                    <div class="controls">
                        <input name="expiredTime" type="text"
                               data-rules="{required:true,}"
                               class="calendar calendar-time">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>图片URL:</label>
                    <div class="controls">
                        <input name="imgUrl" type="text"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">超链接:</label>
                    <div class="controls">
                        <input name="imgHyperlink" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>起效时间:</label>
                    <div class="controls">
                        <input name="effectiveTime" type="text"
                               data-rules="{required:true,}"
                               class="calendar calendar-time">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>模块编号:</label>
                    <div class="controls">
                        <input name="moduleCode" type="text"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>类型:</label>
                    <div class="controls bui-form-field-select" data-items="{'1':'默认','2':'闪屏'}"
                         class="control-text input-small">
                        <input name="type" type="hidden" data-rules="{required:true,}" value="">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>停留时间（秒）:</label>
                    <div class="controls">
                        <input name="delaySeconds" type="text"
                               data-rules="{required:true,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>版本:</label>
                    <div class="controls">
                        <input name="version" type="text"
                               data-rules="{required:true,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">更新版本:</label>
                    <div class="controls">
                        <input name="updateVersion" type="text"
                               data-rules="{required:false,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>是否有效:</label>
                    <div class="controls bui-form-field-select" data-items="{'1':'是','0':'否'}"
                         class="control-text input-small">
                        <input name="enabled" type="hidden" data-rules="{required:true,}" value="">
                    </div>
                </div>
            </div>
            <!--
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">createdTime:</label>
                    <div class="controls">
                        <input name="createdTime" type="text"
                            data-rules="{required:false,}"
                            class="calendar calendar-time">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">updateTime:</label>
                    <div class="controls">
                        <input name="updateTime" type="text"
                            data-rules="{required:false,}"
                            class="calendar calendar-time">
                    </div>
                </div>
            </div>
            -->
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
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/advertise"/>

        var enumObj = {"1":"是","0":"否"};

        var enumObjType = {"1":"默认","2":"闪屏"};

        var columns = [
            {title: 'id', dataIndex: 'id', width: '10%'},
            {title: '发布时间', dataIndex: 'publishTime', width: '10%', renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '过期时间', dataIndex: 'expiredTime', width: '10%', renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '起效时间', dataIndex: 'effectiveTime', width: '10%', renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '图片URL', dataIndex: 'imgUrl', width: '15%'},
            {title: '超链接', dataIndex: 'imgHyperlink', width: '15%'},
            {title: '模块编号', dataIndex: 'moduleCode', width: '5%'},
            {title: '类型', dataIndex: 'type', width: '5%',renderer:BUI.Grid.Format.enumRenderer(enumObjType)},
            {title: '停留时间（秒）', dataIndex: 'delaySeconds', width: '8%'},
            {title: '版本', dataIndex: 'version', width: '5%'},
            {title: '更新版本', dataIndex: 'updateVersion', width: '5%'},
            {title: '是否有效', dataIndex: 'enabled', width: '5%',renderer:BUI.Grid.Format.enumRenderer(enumObj)}
            //{title:'createdTime',dataIndex:'createdTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
            //{title:'updateTime',dataIndex:'updateTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer}
        ];

        var crudGrid = new CrudGrid({
            entityName: '广告',
            pkColumn: 'id',//主键
            storeUrl: '${ctx}/backend/advertise/list',
            addUrl: '${ctx}/backend/advertise/add',
            updateUrl: '${ctx}/backend/advertise/update',
            removeUrl: '${ctx}/backend/advertise/del',
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
    });

</script>

</body>
</html>


