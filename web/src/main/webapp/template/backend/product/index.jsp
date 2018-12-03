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
                <label class="control-label">产品编号:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_code">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">产品名称:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_name">
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
 <!--                <div class="control-group span8">
                    <label class="control-label"><s>*</s>主键:</label>
                    <div class="controls">
                        <input name="id" type="text"
                               data-rules="{required:true,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>  -->
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>产品编号:</label>
                    <div class="controls">
                        <input name="code" type="text"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>产品名称:</label>
                    <div class="controls">
                        <input name="name" type="text"
                               data-rules="{required:true,}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>
            <div class="row">
                
                <div class="control-group span8">
                    <label class="control-label">产品类型</label>
                    <div class="controls ">
                        <div class="bui-form-field-select " data-items="{'1':'现金分期','2':'随借随还','3':'大额分期'}">
                            <input name="type" type="hidden" data-rules="{required:true,number:true}" class="input-normal"/>
                        </div>
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>部分还款:</label>
                    <div class="controls">
                        <div class="bui-form-field-select" data-items="{'1':'支持','0':'不支持'}">
                            <input name="partialPaymentFlag" type="hidden" value="0"  data-rules="{required:true,}" >
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>是否启用:</label>
                    <div class="controls">
                        <div class="bui-form-field-select " data-items="{'1':'启用','0':'不启用'}" >
                            <input name="enabled" type="hidden" data-rules="{required:true}"/>
                        </div>
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>期数:</label>
                    <div class="controls">
                        <div class=" bui-form-field-select" data-items="{'3':'3期','6':'6期','12':'12期','24':'24期','36':'36期'}" data-select="{multipleSelect:true}" >
                            <input name="termNums" type="hidden" value="" data-rules="{required:true}">
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>最小借款金额:</label>
                    <div class="controls">
                        <input name="minLoanAmount" type="text"
                               data-rules="{required:true,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>最大借款金额:</label>
                    <div class="controls">
                        <input name="maxLoanAmount" type="text"
                               data-rules="{required:true,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>
            <div class="row">
                
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>借款步长:</label>
                    <div class="controls">
                        <input name="loanStep" type="text"
                               data-rules="{required:true,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>循环借款上限:</label>
                    <div class="controls">
                        <input name="maxLoopLoanAmount" type="text"
                               data-rules="{required:true,number:true}"
                               class="input-normal control-text">
                    </div>
                </div>
            </div>
            <div class="row">
                
                <div class="control-group span8">
                    <label class="control-label">备注:</label>
                    <div class="controls">
                        <input name="remark" type="text" class="input-normal control-text">
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

    BUI.use(['bui/ux/crudgrid'], function (CrudGrid) {

        //定义页面权限
        var add = false, update = false, del = false, list = false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/product"/>

        var enumEabled = {"1": "启用", "0": "不启用"};

        var partialPaymentFlag = {"1": "支持", "0": "不支持"};

        var enumType = {"1": "现金分期", "2": "随借随还", "3": "大额分期"};

        var columns = [
            {title: '主键', dataIndex: 'id', width: '10%', visible: false},
            {title: '产品编号', dataIndex: 'code', width: '10%'},
            {title: '产品名称', dataIndex: 'name', width: '10%'},
            {title: '产品类型', dataIndex: 'type', width: '10%', renderer: BUI.Grid.Format.enumRenderer(enumType)},
            {title: '是否启用', dataIndex: 'enabled', width: '10%', renderer: BUI.Grid.Format.enumRenderer(enumEabled)},
            {title: '最小借款金额', dataIndex: 'minLoanAmount', width: '10%'},
            {title: '最大借款金额', dataIndex: 'maxLoanAmount', width: '10%'},
            {title: '借款步长', dataIndex: 'loanStep', width: '10%'},
            {title: '最大循环借款金额', dataIndex: 'maxLoopLoanAmount', width: '10%'},
            {title: '支持期数', dataIndex: 'termNums', width: '10%'},
            {
                title: '部分还款',
                dataIndex: 'partialPaymentFlag',
                width: '10%',
                renderer: BUI.Grid.Format.enumRenderer(partialPaymentFlag)
            },
            {title: '创建时间', dataIndex: 'createdTime', width: '150px', renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '更新时间', dataIndex: 'updateTime', width: '150px', renderer: BUI.Grid.Format.datetimeRenderer}
        ];

        var crudGrid = new CrudGrid({
            entityName: '产品表',
            pkColumn: 'id',//主键
            storeUrl: '${ctx}/backend/product/list',
            addUrl: '${ctx}/backend/product/add',
            updateUrl: '${ctx}/backend/product/update',
            removeUrl: '${ctx}/backend/product/del',
            columns: columns,
            showAddBtn: add,
            showUpdateBtn: update,
            showRemoveBtn: false,
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

        var addOrUpdateForm = crudGrid.get('addOrUpdateForm');

        var update = true;

        var beforeAddShow = function(dialog,form){
            update = false;
        };
        crudGrid.on('beforeAddShow', beforeAddShow);


        var beforeUpdateShow = function(dialog,form,record){
            debugger;
            update = true;
            //form.getField('id').disable();
            form.getField('code').disable();
        };

        crudGrid.on('beforeUpdateShow', beforeUpdateShow);

    });

</script>

</body>
</html>


