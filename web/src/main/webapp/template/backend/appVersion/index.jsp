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
                <label class="control-label">操作系统类型:</label>
                <div class="controls bui-form-field-select" data-items='${osTypes}'
                     class="control-text input-small">
                    <input name="search_EQ_osType" type="hidden" value="">
                </div>
            </div>
            <div class="control-group span_width">
                <label class="control-label">版本号:</label>
                <div class="controls bui-form-field-select" data-items='${versions}'
                     class="control-text input-small">
                    <input name="search_EQ_version" type="hidden" value="">
                </div>
            </div>
            <div class="control-group span_width">
                <label class="control-label">版本号整形:</label>
                <div class="controls bui-form-field-select" data-items='${versionNums}'
                     class="control-text input-small">
                    <input name="search_EQ_versionNum" type="hidden" value="">
                </div>
            </div>

            <div class="control-group span_width">
                <label class="control-label">渠道:</label>
                <div class="controls">
                    <input type="text" class="input-small control-text" name="search_LIKE_channelId">
                </div>
            </div>

            <div class="control-group span_width">
                <label class="control-label">是否强更:</label>
                <div class="controls bui-form-field-select" data-items="{' ':'全部','1':'是','0':'否'}"
                     class="control-text input-small">
                    <input name="search_EQ_forceUpdate" type="hidden" value="" onchange="searchBtn();">
                </div>
            </div>
            <div class="control-group span_width">
                <label class="control-label">发布时间:</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar-time calendar" name="search_GTE_releaseTime" data-tip="{text : '开始日期'}">
                    <span>
             - </span><input name="search_LTE_releaseTime" type="text" class="calendar-time calendar" data-tip="{text : '结束日期'}">
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
                    <label class="control-label"><s>*</s>操作系统类型:</label>
                    <div class="controls">
                        <input name="osType" type="text"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8" title="XXX.YYYY.ZZZ">
                    <label class="control-label"><s>*</s>版本号:</label>
                    <div class="controls">
                        <input id="version" name="version" type="text" onchange="setVersionNum(this.value);"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8"
                     title="为了方便查询，把版本号转换成一个数字，规则：如版本好为：XXX.YYY.ZZZ则本字段为XXX*1000000+YYY*1000+ZZZ">
                    <label class="control-label"><s>*</s>版本号整形:</label>
                    <div class="controls">
                        <input id="versionNum" name="versionNum" type="text"
                               data-rules="{required:true,}"
                               readonly="true"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>是否强更:</label>
                    <div class="controls bui-form-field-select" data-items="{'1':'是','0':'否'}"
                         class="control-text input-small">
                        <input name="forceUpdate" type="hidden" value="">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>url:</label>
                    <div class="controls">
                        <input name="url" type="text"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">渠道:</label>
                    <div class="controls">
                        <input name="channelId" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text">
                    </div>
                </div>

            </div>
            <div class="row">
                <div class="control-group span12">
                    <label class="control-label">发布时间:</label>
                    <div class="controls">
                        <input name="releaseTime" type="text"
                               data-rules="{required:false,}"
                               class="calendar calendar-time">
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="control-group span15">
                    <label class="control-label">更新记录:</label>
                    <div class="controls control-row4">
                        <textarea name="updateNote" class="input-large"></textarea>
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

    /***
     * 监控version改变versionNum的值
     * @param version
     */
    function setVersionNum(version) {

        var versions = version.split(".");

        var versionNum = document.getElementById("versionNum").value = (parseInt(versions[0]) * 1000000 + parseInt(versions[1]) * 1000 + parseInt(versions[2])).toString();
        if (versionNum == "NaN") {
            document.getElementById("versionNum").value = "版本号不符合规则";
        }
    }


    BUI.use(['bui/ux/crudgrid'], function (CrudGrid) {

        //定义页面权限
        var add = false, update = false, del = false, list = false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/appVersion"/>

        var enumObj = {"1": "是", "0": "否"};

        var columns = [
            {title: 'id', dataIndex: 'id', width: '5%'},
            {title: '操作系统类型', dataIndex: 'osType', width: '10%'},
            {title: '版本号', dataIndex: 'version', width: '10%'},
            {title: '版本号整型', dataIndex: 'versionNum', width: '10%'},
            {title: '渠道', dataIndex: 'channelId', width: '10%'},
            {title: 'url', dataIndex: 'url', width: '18%'},
            {title: '更新记录', dataIndex: 'updateNote', width: '7%'},
            {title: '是否强更', dataIndex: 'forceUpdate', width: '10%', renderer: BUI.Grid.Format.enumRenderer(enumObj)},
            {title: '发布时间', dataIndex: 'releaseTime', width: '10%', renderer: BUI.Grid.Format.datetimeRenderer}

        ];

        var crudGrid = new CrudGrid({
            entityName: 'APP版本 ',
            pkColumn: 'id',//主键
            storeUrl: '${ctx}/backend/appVersion/list',
            addUrl: '${ctx}/backend/appVersion/add',
            updateUrl: '${ctx}/backend/appVersion/update',
            removeUrl: '${ctx}/backend/appVersion/del',
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


