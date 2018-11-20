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
				<label class="control-label">产品名称:</label>
				<div id="selectProduct" class="controls">
					<input id="searchProduct" type="hidden" name="search_LIKE_loanOrder.productId">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">还款日:</label>
				<div id="selectRepayDay" class="controls">
					<input id="searchRepayDay" type="hidden" name="">
				</div>
			</div>
			
			<%-- <div class="control-group span7" hidden="true">
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_EQ_userId" value="${userId}">
				</div>
			</div> --%>
			<div class="span1 offset2">
			  <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
			</div>
			<div class="span1 offset2">
			  <button class="button button-danger" onclick="flushall();">清空</button>
			</div>
		</div>
		<input type="hidden" name="search_EQ_repay_plan.term" value="1" />
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

        selectProduct = new Select.Select({
            render: '#selectProduct',
            valueField: '#searchProduct',
            items:JSON.parse('${productList}')
        });
        selectProduct.render();
        
        
        selectRepayDay = new Select.Select({
            render: '#selectRepayDay',
            valueField: '#searchRepayDay',
            items:[{text:'全部',value:''},{text:'大于首个还款日',value:''},{text:'小于首个还款日',value:''}]
        });
        selectRepayDay.render();


        //定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/loan/firstRepayOrder"/>


    var columns = [
        {title:'用户ID',dataIndex:'userId',width:'9%'},
        {title:'客户姓名',dataIndex:'idCard',width:"9%",renderer: function (value) {
                if(value){
                    return value.name;
                }else{
                    return '';
                }
            }},
        {title:'注册手机号',dataIndex:'user',width:"9%",renderer:function (value) {
			 if(value){
					return value.phone;
				 }else{
					 return '';
				 }
	        }},
        {title:'贷款金额',dataIndex:'loanOrder',width:'9%',renderer:function (value) {
			 if(value){
				return value.loanAmount;
			 }else{
				 return '';
			 }
        }},
        {title:'首个还款日',dataIndex:'repayPlan',width:'9%',renderer:function (value) {
			 if(value){
					return BUI.Grid.Format.dateRenderer(value.dueDate);
				 }else{
					 return '';
				 }
	        }},//renderer:BUI.Grid.Format.dateRenderer
        {title:'首期总费用',dataIndex:'amount',width:'9%'},
        {title:"评审费",dataIndex:"orderPlande",width:"9%",renderer:function (value) {
			 if(value){
					return value.approveFeeAmount;
				 }else{
					 return '';
				 }
	        }},
        {title:"担保费",dataIndex:"orderPlande",width:"9%",renderer:function (value) {
			 if(value){
			     return value.guaranteeFeeAmount;
			 }else{
				 return '';
			 }
        }},
        {title:"本息",dataIndex:"orderPlande",width:"9%",renderer:function (value) {
			 if(value){
					return value.pincipalAmount;
				 }else{
					 return '';
				 }
	        }},
        {title:"资金方",dataIndex:"fundName",width:"9%"},
        {title:'产品名称',dataIndex:'product',width:'10%',renderer: function (value) {
                if(value){
                    return value.name;
                }else{
                    return '';
                }
            }}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '首期还款列表',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/backend/loan/repayOrder/list',
        columns : columns,
		showAddBtn : add,
		showUpdateBtn : update,
		showRemoveBtn : del,
        storeCfg:{//定义store的排序，如果是复合主键一定要修改
            sortInfo : {
                field : 'repayPlan.dueDate',//排序字段（冲突以此未标准）
                direction : 'DESC' //升序ASC，降序DESC
            }
        }
		});
        var grid = crudGrid.get('grid');
        grid.render();
});
 
</script>
 
</body>
</html>


