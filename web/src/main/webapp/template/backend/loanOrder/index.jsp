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
					<input type="text" class="input-normal control-text" name="search_LIKE_id">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">客户姓名:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_idcard.name">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">客户Id:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_userId">
				</div>
			</div>

			<div class="control-group span7">
				<label class="control-label">身份证号:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_idcard.idCard">
				</div>
			</div>

			<div class="control-group span7">
				<label class="control-label">注册手机号:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_user.phone">
				</div>
			</div>

			<div class="control-group span_width">
				<label class="control-label">放款状态：</label>
				<div id="statusSelect"  class="controls">
					<input id="search_EQ_status" name="search_OREQ_status" type="hidden" >
				</div>
			</div>

			<div class="control-group span6">
				<label class="control-label">产品名称:</label>
				<div class="controls" id="selectProduct">
					<input id="searchProduct" type="hidden" name="search_OREQ_product.id">
				</div>
			</div>
			<div class="control-group span6">
				<label class="control-label">资金方：</label>
				<div id="selectPayFund" class="controls">
					<input id="search_EQ_payFundId" name="search_OREQ_payFundId" type="hidden" >
				</div>
			</div>

			<div class="control-group span7">
				<label class="control-label">注册渠道:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_user.channelId">
				</div>
			</div>

			<div class="control-group span_width">
				<label class="control-label">申请时间:</label>
				<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
					<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
					<input type="text" readonly="true" class="calendarStart calendar-time" name="search_GTE_applyTime" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_applyTime" readonly="true" type="text" class="calendar-time calendarEnd" data-tip="{text : '结束日期'}">
				</div>
			</div>
			<div class="control-group span_width">
				<label class="control-label">放款时间:</label>
				<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
					<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
					<input type="text" class="calendar-time calendarStart" name="search_GTE_payLoanTime" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_payLoanTime" type="text" class="calendar-time calendarEnd" data-tip="{text : '结束日期'}">
				</div>
			</div>

			<div class="control-group span7" hidden="true">
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_EQ_userId" value="${userId}">
				</div>
			</div>
			<div class="span1 offset2">
				<button  type="button" id="btnSearch" class="button button-primary">搜索</button>
			</div>
			<div class="span1 offset2">
				<button class="button button-danger" onclick="flushall();">清空</button>
			</div>
		</div>
	</form>
	<!-- 修改新增 -->
	<div id="addOrUpdate" class="hide">

	</div>
	<div class="search-grid-container">
		<div id="grid"></div>
	</div>
</div>

<script type="text/javascript">

    function flushall(){
        var elementsByTagName = document.getElementsByTagName("input");
        for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
        }
    }

    function openout(id) {
        window.open('${IP}'+'${ctx}/backend/loanOrder/auth/'+id);
    }

    function stop(id) {
        BUI.use('bui/overlay',function (Overlay){
            BUI.Message.Confirm('确认要终止放款么？',function(){
                $.ajax({
                    url:'${ctx}/backend/loanOrder/rejectCash/'+id,
                    dataType:'JSON',
                    headers: {'Content-type':'application/json'},
                    type:'POST',
                    async:true,
                    //contentType: 'application/json;charset=utf-8',
                    success:function(result){
                        debugger;
                        if(result.code== 0){
                            BUI.Message.Alert('操作成功！',function(){
                            },'success');
                        }else {
                            BUI.Message.Alert(result.message,function(){
                            },'error');
                        }
                    }});

            },'question');
        });
    }


    BUI.use(['bui/ux/crudgrid','bui/common/search','bui/common/page','bui/overlay','bui/select','bui/data','bui/calendar'],function (CrudGrid,Search,Grid,Overlay,Select,Data,Calendar) {

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


        var  detailUrl = '${ctx}/backend/loanOrder/detail/';

        var selectFundStore = new Data.Store({
            url : '${ctx}/backend/fund/getFundList',
            autoLoad : true
        });

        selectFundStatus = new Select.Select({
            render:'#selectPayFund',
            valueField:'#search_EQ_payFundId',
            multipleSelect:true,
            store:selectFundStore
        });
        selectFundStatus.render();

        var selectStatusStore = new Data.Store({
            url : '${ctx}/backend/loanOrder/getLoanStatusList',
            autoLoad : true
        });

        selectStatus = new Select.Select({
            render:'#statusSelect',
            valueField:'#search_EQ_status',
            multipleSelect:true,
            store:selectStatusStore
        });
        selectStatus.render();


        var selectProductStore = new Data.Store({
            url : '${ctx}/backend/product/productNameList',
            autoLoad : true
        });

        selectProduct = new Select.Select({
            render:'#selectProduct',
            valueField:'#searchProduct',
            multipleSelect:true,
            store:selectProductStore
        });
        selectProduct.render();

        //定义页面权限
        var add=false,update=false,list=false,del=false,stop=false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/loanOrder"/>

        <shiro:hasPermission name="/backend/loanOrder:stop">
        stop = true;
        </shiro:hasPermission>







        var enumObj = ${loanStatusMap};


        var columns = [
            {title:'订单编号',dataIndex:'id',width:'150px'},
            {title:'客户姓名',dataIndex:'idcard',width:"70px",renderer: function (value) {
                    if(value){
                        return value.name;
                    }else{
                        return '';
                    }
                }},
            {title:'客户ID',dataIndex:'user',width:"150px",renderer:function (value) {
                    if(value) {
                        return value.id;
                    }else{
                        return '';
                    }
                }},
            {title:'注册手机号',dataIndex:'user',renderer:function (value) {
                    if(value){
                        return value.phone;
                    }else{
                        return "";
                    }
                }},
            {title:'身份证',dataIndex:'idcard',width:"130px",renderer: function (value) {
                    if(value){
                        return value.idCard;
                    }else{
                        return '';
                    }
                }},
            {title:'产品名称',dataIndex:'product',width:'90px',renderer: function (value) {
                    if(value){
                        return value.remark;
                    }else{
                        return '';
                    }
                }},
            {title:'注册渠道',dataIndex:'user',width:'90px',renderer: function (value) {
                    if(value){
                        return value.channelId;
                    }else{
                        return '';
                    }
                }},
            {title:'授信金额',dataIndex:'card.creditSum',width:'80px',renderer: function (value) {
                    if(value){
                        return value;
                    }else{
                        return null;
                    }
                }},
            {title:'提现金额',dataIndex:'loanAmount',width:'80px'},
            {title:'期限(月)',dataIndex:'loanTerm',width:'80px'},
            {title:'资金方',dataIndex:'fundName',width:'70px',renderer: function (value) {
                    if(value){
                        return value;
                    }else{
                        return null;
                    }
                }},
            {title:'放款状态',dataIndex:'status',width:'140px',renderer:BUI.Grid.Format.enumRenderer(enumObj)},
            {title:'申请时间',dataIndex:'applyTime',width:'140px',renderer:BUI.Grid.Format.datetimeRenderer},
            {title:'放款时间',dataIndex:'payLoanTime',width:'140px',renderer:BUI.Grid.Format.datetimeRenderer}

        ];



        var crudGrid = new CrudGrid({
            entityName : '借款订单表',
            pkColumn : 'id',//主键
            storeUrl : '${ctx}/backend/loanOrder/list',
            addUrl : '${ctx}/backend/loanOrder/add',
            updateUrl : '${ctx}/backend/loanOrder/update',
            removeUrl : '${ctx}/backend/loanOrder/del',
            columns : columns,
            showAddBtn : add,
            showUpdateBtn : update,
            showRemoveBtn : del,
            operationwidth:'110px',
            gridCfg:{
                innerBorder:true,

            },
            operationColumnRenderer : function(value, obj){//操作列最追加按钮

                var editStr = '';
                var title = obj.id+"—提现信息";
                if(!jQuery.isEmptyObject(obj.idcard)){
                    title = obj.idcard.name + "—提现信息"
                }
                var detail="";

                var id = String(obj.id);
                detail = CrudGrid.createLinkCustomSpan({
                    class:"page-action grid-command ",
                    id: 'auth' + id,
                    title: title,
                    text: '详情',
                    href: detailUrl + id
                })

                if(obj.status=="3"||obj.status=="-10"||obj.status=="10"){
                    if(obj.cancelLoan==null&&stop){
                        editStr= detail+'&nbsp'+'<span class="grid-command" title="终止放款" onclick="stop(\''+id+'\');">取消提现</span>';
                    }else if(obj.cancelLoan==0&&stop){
                        editStr= detail+'&nbsp'+'<span class="grid-command" title="终止放款" onclick="stop(\''+id+'\');">取消提现</span>';
                    }else{
                        editStr = detail;
                    }
                }else{
                    editStr = detail;
                }
                return "<div style='text-align:left'>&nbsp;&nbsp;"+editStr+"</div>";
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


