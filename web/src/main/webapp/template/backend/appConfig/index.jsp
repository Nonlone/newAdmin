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

    <div class="row">
        <div class="span6">
            <div class="panel">
                <form id="tree">
                    <div class="panel-header clearfix">
                        <h3 class="pull-left">APP配置分类</h3>
                        <div id="tbar" class="pull-right"></div>
                    </div>
                    <div id="list">
                    </div>
                </form>
            </div>
        </div>
        <div>
            <div class="panel panel-head-borded " style="overflow: hidden;padding-left: 10px;">
                <div id="gridTree">
                </div>
            </div>
        </div>

        <!-- 修改新增 -->
        <div id="addOrUpdate" class="hide">
            <form id="addOrUpdateForm" class="form-inline">
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label"><s>*</s>配置名称:</label>
                        <div class="controls">
                            <input name="name" type="text"
                                   data-rules="{required:true,}"
                                   class="input-normal control-text">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label"><s>*</s>配置code:</label>
                        <div class="controls">
                            <input name="code" type="text"
                                   data-rules="{required:true,}"
                                   class="input-normal control-text">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label"><s>*</s>配置值:</label>
                        <div class="controls">
                            <input name="value" type="text"
                                   data-rules="{required:true,}"
                                   class="input-normal control-text">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">扩展值1:</label>
                        <div class="controls">
                            <input name="extValue1" type="text"
                                   class="input-normal control-text">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label">扩展值2:</label>
                        <div class="controls">
                            <input name="extValue2" type="text"
                                   class="input-normal control-text">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">扩展值3:</label>
                        <div class="controls">
                            <input name="extValue3" type="text"
                                   class="input-normal control-text">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label"><s>*</s>是否启用:</label>
                        <div class="controls bui-form-field-select" data-items="{'true':'启用','false':'停用'}"
                             class="control-text input-small">
                            <input name="enable" type="hidden" value="">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control-group span15">
                        <label class="control-label">备注:</label>
                        <div class="controls control-row4">
                            <textarea name="remark" class="input-large"></textarea>
                        </div>
                    </div>

                </div>

                <input type="hidden" name="id" value="">
                <input type="hidden" name="typeCode" value="">
            </form>
        </div>

    </div>
</div>
<script type="text/javascript">

    BUI.use(['bui/tree', 'bui/data', 'bui/toolbar', 'bui/select', 'bui/ux/crudgrid', 'bui/mask', 'bui/list'],
        function (Tree, Data, Toolbar, Select, CrudGrid, Mask, List) {

            //定义页面权限
            var add = false, update = false, del = false, list = false;
            //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
            <framwork:crudPermission resource="/backend/appConfig"/>

            var typeCode = 0;

            var gridStore;//

            var listStore = new Data.Store({
                url: '${ctx}/backend/appConfigType/listAll',
                autoLoad: true
            });

            var list = new List.SimpleList({
                elCls: 'bui-select-list',//默认是'bui-simple-list'
                //itemTpl : '<li>{text}[{value}]</li>',
                width: 225,
                height: BUI.viewportHeight() - 90,
                render: '#list',
                store: listStore
            });
            list.render();
            list.on('itemclick', function (ev) {
                gridStore.set('lastParams', {});//清空上次的参数
                gridStore.load({'search_EQ_typeCode': ev.item.value});
                typeCode = ev.item.value;
            });

            var bar = new Toolbar.Bar({
                render: '#tbar',
                elCls: 'button-group',
                children: [
                    {
                        elCls: 'button button-small button-primary',
                        content: '<i class="icon-refresh icon-white"></i>刷新',
                        elAttrs: {title: '刷新'},
                        handler: function () {
                            listStore.load();
                        }
                    }
                ]
            });
            bar.render();

            var enumObj = {"true": "启用", "false": "停用"};

            var columns = [
                {title: 'id', dataIndex: 'id', width: '5%'},
                {title: '配置分类', dataIndex: 'typeCode', width: '8%'},
                {title: '配置名称', dataIndex: 'name', width: '15%'},
                {title: '配置code', dataIndex: 'code', width: '10%'},
                {title: '配置值', dataIndex: 'value', width: '15%'},
                {title: '扩展值1', dataIndex: 'extValue1', width: '8%'},
                {title: '扩展值2', dataIndex: 'extValue2', width: '8%'},
                {title: '扩展值3', dataIndex: 'extValue3', width: '8%'},
                {title: '是否启用', dataIndex: 'enable', width: '5%', renderer: BUI.Grid.Format.enumRenderer(enumObj)},
                {title: '备注', dataIndex: 'remark', width: '19%'}
                //{title:'创建时间',dataIndex:'createdTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
                //{title:'更新时间',dataIndex:'updateTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer}
            ];

            var crudGrid = new CrudGrid({
                gridId: 'gridTree',
                entityName: 'APP配置',
                pkColumn: 'id',//主键
                storeUrl: '${ctx}/backend/appConfig/list',
                addUrl: '${ctx}/backend/appConfig/add',
                updateUrl: '${ctx}/backend/appConfig/update',
                removeUrl: '${ctx}/backend/appConfig/del',
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

            gridStore = crudGrid.get('store');


            crudGrid.on('beforeAddShow', function (dialog, form) {
                if (!typeCode) {//dictionaryTypeId==0
                    BUI.Message.Alert('请先选择分类', 'info');
                    return false;
                } else {
                    form.getField('typeCode').set('value', typeCode);
                }
            });

            crudGrid.on('beforeUpdateShow', function (dialog, form, record) {
                form.getField('typeCode').set('value', record.typeCode);
            });
        });

</script>

</body>
</html>


