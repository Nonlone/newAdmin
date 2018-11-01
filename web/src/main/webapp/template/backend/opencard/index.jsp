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
                <label class="control-label">注册手机号:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_userIn.phone">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">身份证号:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_idcard.idCard">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">状态:</label>
                <div class="controls">
                    <div id="statusSelect" class="controls">
                        <input id = "search_EQ_status" name="search_EQ_status" type="hidden" >
                    </div>
                </div>
            </div>

            <div class="control-group span7">
                <label class="control-label">产品名称:</label>
                <div class="controls" id="selectProduct">
                    <input id="searchProduct" type="hidden" name="search_LIKE_product.name">
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
                    <input type="text" class="calendar" name="search_GTE_submitTime_D" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_submitTime_D" type="text" class="calendar" data-tip="{text : '结束日期'}">
                </div>
            </div>
            <div class="control-group span10">
                <label class="control-label">订单创建时间:</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar" name="search_GTE_createdTime_D" data-tip="{text : '开始日期'}"> <span>
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

<script type="text/javascript">

    //清空按钮
    function flushall(){
        var elementsByTagName = document.getElementsByTagName("input");
        for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
        }
    }

    function openout(id) {
        window.open('${IP}'+'${ctx}/backend/opencard/detail/'+id);
    }

    BUI.use(['bui/ux/crudgrid','bui/common/search','bui/common/page','bui/select','bui/data'],function (CrudGrid,Search,Grid,Select,Data) {

        var items = [
                {text:'全部',value:' '},
                {text:'阀门拒绝',value:'90'},
                {text:'授信拒绝',value:'91'},
                {text:'主体拒绝',value:'98'},
                {text:'提现拒绝',value:'97'},
                {text:'提额拒绝',value:'96'},
                {text:'续卡拒绝',value:'95'},
                {text:'续卡审核中',value:'-40'},
                {text:'提额审核中',value:'-30'},
                {text:'提现审核中',value:'-20'},
                {text:'授信审核中',value:'-10'},
                {text:'资料填写中',value:'-1'},
                {text:'放款中',value:'-2'},
                {text:'未激活',value:'0'},
                {text:'授信（未提现）',value:'1'},
                {text:'授信（已提现）',value:'2'},
                {text:'逾期',value:'12'},
                {text:'授信过期',value:'11'},
                {text:'卡废弃',value:'99'}
            ],
            select = new Select.Select({
                render:'#statusSelect',
                valueField:'#search_EQ_status',
                items:items
            });
        select.render();


        var objEmun = {'true':'是','false':'否'};
        var	ViewBtn = true;
        var	detailUrl = "${ctx}/backend/opencard/detail/";
        var spantrue = "<span style='color:#32CD32'>";
        var spanfalse = "<span style='color:#FF4500'>";
        var statusEmun = {"VALVE_REJECT":spanfalse+"阀门拒绝</span>","AUTHORIZED_REJECT":spanfalse+"授信拒绝</span>",
            "CUSTOMER_REJECT":spanfalse+"主体拒绝</span>","WITHDRAW_REJECT":spantrue+"提现拒绝</span>",
            "QUOTACHANGE_REJECT":spantrue+"提额拒绝</span>","RENEWAL_REJECT":spanfalse+"续卡拒绝</span>","RENEWAL_APPROVING":spanfalse+"续卡审核中"
            ,"QUOTACHAGNE_APPROVING":spantrue+"提额审核中</span>","WITHDRAW_APPROVING":spantrue+"提现审核中</span>",
            "OPENCARD_APPROVING":"授信审核中","FILLING":"资料填写中","LOANING":spantrue+"放款中</span>",
            "UNACTIVE":"未激活","NORMAL":spantrue+"授信（未提现）</span>","DEBT":spantrue+"授信（已提现）"
            ,"OVERDUE":spantrue+"逾期</span>","ABANDONED":spanfalse+"卡废弃</span>"};

        //定义页面权限
        var add=false,update=false,del=false,list=false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/opencard"/>

        var selectStore = new Data.Store({
            url : '${ctx}/admin/product/product/productNameList',
            autoLoad : true
        });

        select = new Select.Select({
            render:'#selectProduct',
            valueField:'#searchProduct',
            store:selectStore
        });
        select.render();

        var columns = [
            {title:'订单号',dataIndex:'id',width:'10%',renderer: function (value) {
                    if(value){
                        return String(value);
                    }else{
                        return '';
                    }
                }},
            {title:'客户姓名',dataIndex:'idcard',width:'10%',renderer: function (value) {
                    if(value){
                        return value.name;
                    }else{
                        return '<span style="color:#ff9955">未填写</span>';
                    }
                }},
            {title:'身份证',dataIndex:'idcard',width:'10%',renderer: function (value) {
                    if(value){
                        return value.idCard;
                    }else{
                        return '<span style="color:#ff9955">未填写</span>';
                    }
                }},
            {title:'客户ID',dataIndex:'userId',width:'10%'},
            {title:'注册手机号',dataIndex:'user',width:'10%',renderer:function (value) {
                    if(value){
                        return value.phone;
                    }else{
                        return "";
                    }
                }},
            {title:'授信状态',dataIndex:'status',width:'10%',renderer:BUI.Grid.Format.enumRenderer(statusEmun)},
            {title:'授信额度',dataIndex:'creditSum',width:'10%'},
            {title:"授权项",dataIndex:'auths',width:'10%'},
            {title:'进件渠道',dataIndex:'applyChannelId',width:'10%'},
            {title:'注册渠道',dataIndex:'registChannelId',width:'10%'},
            {title:'产品名称',dataIndex:'product',width:'10%',renderer:function(value){
                    if(value){
                        return value.name;
                    }else{
                        return "";
                    }
                }},
            {title:'订单创建时间',dataIndex:'createdTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
            //{title:'过期时间',dataIndex:'expiredTime',width:'10%',renderer:BUI.Grid.Format.dateRenderer},
            {title:'提交审批时间',dataIndex:'submitTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
            {title:'注册客户端',dataIndex:'user',width:'10%',renderer:function(value){
                    if(value){
                        return value.osType;
                    }else{
                        return "";
                    }
                }},
            {title:'是否新客户',dataIndex:'id',width:'10%',renderer: function (value) {
                    return '是';
                }}
        ];

        var crudGrid = new CrudGrid({
            entityName : '用户持有对应信用产品记录',
            pkColumn : 'id',//主键
            storeUrl : '${ctx}/backend/opencard/list',
            addUrl : '${ctx}/backend/opencard/add',
            updateUrl : '${ctx}/backend/opencard/update',
            removeUrl : '${ctx}/backend/opencard/del',
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
                var viewStr = "";
                var id = obj.id;
                if(ViewBtn&&obj.idcard!=null){
                    viewStr = '<span class="x-icon x-icon-info" title="'+'详细信息'+'"><i class="icon icon-list-alt icon-white"  onclick="openout(\''+id+'\');"></i></span>';

                }
                return viewStr;
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


