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
				<label class="control-label">客户ID:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_user.id">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">身份证号:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_idcard.idCard">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">手机号:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_user.phone">
				</div>
			</div>

			<div class="control-group span_width">
				<label class="control-label">还款状态：</label>
				<div id="selectStatus" class="controls">
					<input id="searchStatus" name="search_OREQ_status" type="hidden" >
				</div>
			</div>
			<div class="control-group span_width">
				<label class="control-label">还款到期日:</label>
				<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
					<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
					<input type="text" class="calendar" name="search_GTE_repayPlan.dueDate" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_repayPlan.dueDate" type="text" class="calendar" data-tip="{text : '结束日期'}">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">逾期天数:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_EQ_repayPlan.overdueDays">
				</div>
			</div>

			<div class="control-group span7">
				<label class="control-label">产品名称:</label>
				<div id="selectProduct" class="controls">
					<input id="searchProduct" type="hidden" name="search_EQ_loanOrder.productId">
				</div>
			</div>
			<div class="control-group span7" hidden="true">
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_OREQ_userId" value="${userId}">
				</div>
			</div>
			<%--<div class="control-group span_width">--%>
			<%--<label class="control-label">申请日期:</label>--%>
			<%--<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">--%>
			<%--<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->--%>
			<%--<input type="text" class="calendar" name="search_GTE_applyTime" data-tip="{text : '开始日期'}"> <span>--%>
			<%--- </span><input name="search_LTE_loanOrder.applyTime" type="text" class="calendar" data-tip="{text : '结束日期'}">--%>
			<%--</div>--%>
			<%--</div>--%>
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

    function openout(id) {
        window.open('${IP}'+'${ctx}/backend/loan/repayOrder/auth/'+id);
    }


    function flushall(){
        var elementsByTagName = document.getElementsByTagName("input");
        for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
        }
    }

    BUI.use(['bui/ux/crudgrid','bui/select','bui/data'],function (CrudGrid,Select,Data) {

        var selectProductStore = new Data.Store({
            url: '${ctx}/backend/product/productNameList',
            autoLoad: true
        });

        selectProduct = new Select.Select({
            render: '#selectProduct',
            valueField: '#searchProduct',
            multipleSelect:true,
            store: selectProductStore
        });
        selectProduct.render();

        var selectStatusStore = new Data.Store({
            url: '${ctx}/backend/loan/repayOrder/repayOrderStatus',
            autoLoad: true
        });

        selectStatus = new Select.Select({
            render: '#selectStatus',
            valueField: '#searchStatus',
            multipleSelect:true,
            store: selectStatusStore
        });
        selectStatus.render();



        //定义页面权限
        var add=false,update=false,del=false,list=false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/loan/repayOrder"/>

        var authUrl = '${ctx}/backend/loan/repayOrder/detail/';

        var columns = [
            {title:'订单号',dataIndex:'id',width:'170px'},
            {title:'姓名',dataIndex:'idcard',width:"110px",renderer: function (value) {
                    if(value){
                        return value.name;
                    }else{
                        return '';
                    }
                }},
            {title:'客户ID',dataIndex:'user',width:"170px",renderer: function (value) {
                    if(value){
                        return value.id;
                    }else{
                        return '';
                    }
                }},
            {title:'注册手机号',dataIndex:'user',width:"120px",renderer:function (value) {
                    if(value){
                        return value.phone;
                    }
                    return '';
                }},
            {title:'身份证号',dataIndex:'idcard',width:"150px",renderer: function (value) {
                    if(value){
                        return value.idCard;
                    }else{
                        return '';
                    }
                }},
            {title:'产品名称',dataIndex:'loanOrder',width:'100px',renderer: function (value) {
                    if(value){
                        return value.product.remark;
                    }else{
                        return '';
                    }
                }},
            {title:'借款金额',dataIndex:'loanOrder',width:'100px',renderer:function (value) {
                    if(value){
                        return value.loanAmount;
                    }else{
                        return '';
                    }
                }},
            {title:'当期应还',dataIndex:'repayPlan',width:'100px',renderer:function (value) {
                    if(value){
                        return value.amount;
                    }else{
                        return '';
                    }
                }},
            {title:'当期实还',dataIndex:'amount',width:'100px'},

            {title:'还款到期日',dataIndex:'dueDate',width:'100px'},
            {title:'逾期天数',dataIndex:'repayPlan',width:'80px',renderer:function (value) {
                    if(value){
                        return value.overdueDays;
                    }
                    return '';
                }},
            {title:'存储信贷系统的出账编号',dataIndex:'repayPlan',width:'180px',renderer:function (value) {
                    if(value){
                        return value.putoutno;
                    }
                    return '暂无';
                }},
            {title:'当期/总期',dataIndex:'termPre',width:'100px'},
            {title:'还款状态',dataIndex:'status',width:'100px'}
        ];

        var crudGrid = new CrudGrid({
            entityName : '还款列表',
            pkColumn : 'id',//主键
            storeUrl : '${ctx}/backend/loan/repayOrder/list',
            addUrl : '${ctx}/backend/loan/repayOrder/add',
            updateUrl : '${ctx}/backend/loan/repayOrder/update',
            removeUrl : '${ctx}/backend/loan/repayOrder/del',
            columns : columns,
            showAddBtn : add,
            showUpdateBtn : update,
            showRemoveBtn : del,
            operationwidth:'80px',
            operationColumnRenderer : function(value, obj){//操作列最追加按钮
                var detail="";
                var id = obj.id;
                detail = CrudGrid.createLinkCustomSpan({
                    class:"page-action grid-command",
                    id: 'detail' + obj.id,
                    title: obj.idcard.name + '还款信息',
                    text: '详情',
                    href: authUrl + obj.id
                })
                return "<div style='text-align:left'>&nbsp;&nbsp;"+detail+"</div>";
            },
            storeCfg:{//定义store的排序，如果是复合主键一定要修改
                sortInfo : {
                    field : 'repayPlan.createdTime',//排序字段（冲突以此未标准）
                    direction : 'DESC' //升序ASC，降序DESC
                }
            }
        });
        var grid = crudGrid.get('grid');
    });

</script>

</body>
</html>


