<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/import-tags.jsp"%>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/import-static.jsp"%>
</head>
<body>
<div class="container">
    <!-- 查询 -->
    <form id="searchForm" class="form-horizontal search-form">
        <div class="row">
            <div class="control-group span7">
                <label class="control-label">订单号:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_EQ_id">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">客户姓名:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_idcard.name">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">客户ID:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_EQ_user.id">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">注册手机号:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_user.phone">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">身份证号:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_idcard.idCard">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">授信状态:</label>
                <div class="controls">
                    <div id="statusSelect" class="controls">
                        <input id = "search_EQ_status" name="search_EQ_status" type="hidden" >
                    </div>
                </div>
            </div>

            <div class="control-group span7">
                <label class="control-label">产品:</label>
                <div class="controls" id="selectProduct">
                    <input id="searchProduct" type="hidden" name="search_EQ_product.id">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">注册渠道:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_registChannelId">
                </div>
            </div>
            <div class="control-group span10">
                <label class="control-label">提交审批时间:</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar" name="search_GTE_submitTime" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_submitTime_D" type="text" class="calendar" data-tip="{text : '结束日期'}">
                </div>
            </div>
            <div class="control-group span10">
                <label class="control-label">订单创建时间:</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar" name="search_GTE_createdTime" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_createdTime_D" type="text" class="calendar" data-tip="{text : '结束日期'}">
                </div>
            </div>

            <div class="span3 offset1">
                <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
            </div>
            <div class="span3 offset1">
                <button class="button button-danger" onclick="flushall();">清空</button>
            </div>
        </div>
    </form>
    <!-- 修改新增 -->
    <div id="addOrUpdate" class="hide">

        <input type="hidden" name="id" value="">
        </form>
    </div>
    <div class="search-grid-container">
        <div id="grid"></div>
    </div>
</div>
<%-- <c:forEach var="item" items="${cardStatusList}">
</c:forEach> --%>
<script type="text/javascript">

    //清空按钮
    function flushall(){
        var elementsByTagName = document.getElementsByTagName("input");
        for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
        }
    }

    function openout(id) {
        window.open('${IP}'+'/backend/opencard/detail/'+id);
    }

    BUI.use(['bui/ux/crudgrid','bui/select','bui/data'],function (CrudGrid,Select,Data) {

         
        selectStatus = new Select.Select({
            render:'#statusSelect',
            valueField:'#search_EQ_status',
            items:JSON.parse('${itemList}')
        });
        selectStatus.render();

        var ViewBtn = true;
        var spantrue = "<span style='color:#32CD32'>";
        var spanfalse = "<span style='color:#FF4500'>";


        //定义页面权限
        var add = false, update = false, del = false, list = false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/opencard"/>

        var selectProductStore = new Data.Store({
            url: '/admin/backend/product/productNameList',
            autoLoad: true
        });

        selectProduct = new Select.Select({
            render: '#selectProduct',
            valueField: '#searchProduct',
            store: selectProductStore
        });
        selectProduct.render();

        var columns = [
            {title:'订单号',dataIndex:'id',width:'150px'},
            {title:'客户ID',dataIndex:'userId',width:'150px'},
            {title:'客户姓名',dataIndex:'idCard',width:'80px',renderer: function (value) {
                    if(value){
                        return value.name;
                    }else{
                        return '<span style="color:#ff9955">未填写</span>';
                    }
                }},
            {title:'身份证',dataIndex:'idCard',width:'120px',renderer: function (value) {
                    if(value){
                        return value.idCard;
                    }else{
                        return '<span style="color:#ff9955">未填写</span>';
                    }
                }},
            {title:'注册手机号',dataIndex:'user',width:'100px',renderer:function (value) {
                    if(value){
                        return value.phone;
                    }else{
                        return "";
                    }
                }},
            {title:'产品名称',dataIndex:'product',width:'100px',renderer:function(value){
                    if(value){
                        return value.remark;
                    }
                    return "";
                }},
            {title:'授信状态',dataIndex:'cardStatusName',width:'100px'},
            {title:"授权项",dataIndex:'auths',width:'140px'},
            {title:'授信额度',dataIndex:'creditSum',width:'80px'},
            {title:'注册渠道',dataIndex:'registChannelId',width:'100px'},
            {title:'创建时间',dataIndex:'createdTime',width:'140px',renderer:BUI.Grid.Format.datetimeRenderer},
            {title:'进件渠道',dataIndex:'applyChannelId',width:'80px'},
            {title:'提交审批时间',dataIndex:'submitTime',width:'140px',renderer:BUI.Grid.Format.datetimeRenderer},
            {title:'注册客户端',dataIndex:'user',width:'80px',renderer:function(value) {
                    if (value) {
                        return value.osType;
                    }
                    return "";
                }}
        ];

        var crudGrid = new CrudGrid({
            entityName : '用户持有对应信用产品记录',
            pkColumn : 'id',//主键
            storeUrl : '/backend/opencard/list',
            addUrl : '/backend/opencard/add',
            updateUrl : '/backend/opencard/update',
            removeUrl : '/backend/opencard/del',
            columns : columns,
            showAddBtn : add,
            showUpdateBtn : update,
            showRemoveBtn : del,
            addOrUpdateFormId : 'addOrUpdateForm',
            dialogContentId : 'addOrUpdate',
            gridCfg:{
                innerBorder:true
            },
            operationColumnRenderer : function(value, obj){//操作列最追加按钮
                var title = obj.id+"—授信信息";
                if(!jQuery.isEmptyObject(obj.idCard)){
                    title = obj.idCard.name + "—授信信息"
                }
                return CrudGrid.createLinkCustomSpan({
                    class:"page-action grid-command x-icon x-icon-info",
                    id: obj.id,
                    title: title,
                    text: '<i class="icon icon-white icon-list-alt"></i>',
                    href: $ctx+"/backend/opencard/detail/" +obj.id
                })
            },
            storeCfg:{//定义store的排序，如果是复合主键一定要修改
                sortInfo : {
                    field : 'createdTime',//排序字段（冲突以此未标准）
                    direction : 'DESC' //升序ASC，降序DESC
                }
            }
        });
    });

</script>

</body>
</html>


