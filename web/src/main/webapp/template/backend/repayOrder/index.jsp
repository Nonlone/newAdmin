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
				<label class="control-label">客户姓名:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_idcard.name">
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
					<input type="text" class="input-normal control-text" name="search_LIKE_userIn.phone">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">订单号:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_id">
				</div>
			</div>
			<div class="control-group span_width">
				<label class="control-label">还款状态：</label>
				<div class="controls bui-form-field-select height_auto"  data-items="{' ':'全部'}" class="control-text input-small">
					<input name="search_EQ_status" type="hidden" >
				</div>
			</div>
			<div class="control-group span_width">
				<label class="control-label">还款日期:</label>
				<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
					<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
					<input type="text" class="calendar" name="search_GTE_dueDate_D" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_dueDate_D" type="text" class="calendar" data-tip="{text : '结束日期'}">
				</div>
			</div>

			<div class="control-group span7">
				<label class="control-label">产品名称:</label>
				<div id="selectProduct" class="controls">
					<input id="searchProduct" type="hidden" name="search_LIKE_loanOrder.product.name">
				</div>
			</div>
			<div class="control-group span7" hidden="true">
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_EQ_userId" value="${userId}">
				</div>
			</div>
			<%--<div class="control-group span_width">--%>
				<%--<label class="control-label">申请日期:</label>--%>
				<%--<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">--%>
					<%--<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->--%>
					<%--<input type="text" class="calendar" name="search_GTE_applyTime_D" data-tip="{text : '开始日期'}"> <span>--%>
             <%--- </span><input name="search_LTE_loanOrder.applyTime_D" type="text" class="calendar" data-tip="{text : '结束日期'}">--%>
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
            url: '${ctx}/admin/product/product/productNameList',
            autoLoad: true
        });

        selectProduct = new Select.Select({
            render: '#selectProduct',
            valueField: '#searchProduct',
            store: selectProductStore
        });
        selectProduct.render();


        //定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/loan/repayOrder"/>

    var enumObj = {"":" ","10":"启用","20":"启用","30":"启用","40":"停用","50":"已放款"};

    var authUrl,authBtn=false;//授权按钮
		if(${isOut}){
            authBtn = true;
            authUrl = '${ctx}/backend/loan/repayOrder/auth/';
		}

    <shiro:hasPermission name='/admin/sys/role:auth'>
    authBtn = true;
    authUrl = '${ctx}/backend/loan/repayOrder/auth/';
    </shiro:hasPermission>

    var columns = [
        {title:'订单号',dataIndex:'id',width:'8%'},
        {title:'姓名',dataIndex:'idcard',width:"8%",renderer: function (value) {
                if(value){
                    return value.name;
                }else{
                    return '';
                }
            }},
        {title:'注册手机号',dataIndex:'phone',width:"9%"},
        {title:'身份证号',dataIndex:'idcard',width:"8%",renderer: function (value) {
                if(value){
                    return value.idCard;
                }else{
                    return '';
                }
            }},
        {title:'产品名称',dataIndex:'loanOrder',width:'8%',renderer: function (value) {
                if(value){
                    return value.product.name;
                }else{
                    return '';
                }
            }},
        {title:'是否新客户',dataIndex:'',width:'8%',renderer: function (value) {
                if(value){
                    return "是";
                }else{
                    return '是';
                }
            }},
		 {title:'还款账号',dataIndex:'payAccount',width:'8%'},
		 {title:'借款金额',dataIndex:'loanOrder',width:'8%',renderer:function (value) {
				 if(value){
					return value.loanAmount;
				 }else{
					 return '';
				 }
             }},
		 {title:'当期应还',dataIndex:'repayPlan',width:'8%',renderer:function (value) {
				 if(value){
				     return value.amount;
				 }else{
					 return '';
				 }
             }},
		 {title:'当期实还',dataIndex:'amount',width:'8%'},
		 {title:'当期/总期',dataIndex:'termPre',width:'8%'},
		 {title:'还款日',dataIndex:'dueDate',width:'8%',renderer:BUI.Grid.Format.dateRenderer},
		 {title:'实际还款日',dataIndex:'repayDate',width:'8%'},
		 {title:'还款状态',dataIndex:'',width:'8%'}
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
        operationColumnRenderer : function(value, obj){//操作列最追加按钮
            var detail="";
            var id = obj.id;
            if(authBtn){
                if(${isOut}){
                    detail = '<span title="'+obj.idcard.name+'详细信息'+'"><i class="icon-list-alt"  onclick="openout('+id+');"></i></span>';
                }else{
                    detail = CrudGrid.createLink({
                        id : 'auth' + obj.id,
                        title : obj.idcard.name +'详细信息',
                        text : '<li class="icon-list-alt auth"></li>',
                        href : authUrl +obj.id
                    });
                }
            }
            return detail;
        },
        storeCfg:{//定义store的排序，如果是复合主键一定要修改
            sortInfo : {
                field : 'repayPlan.dueDate',//排序字段（冲突以此未标准）
                direction : 'DESC' //升序ASC，降序DESC
            }
        }
		});
        var grid = crudGrid.get('grid');
});
 
</script>
 
</body>
</html>


