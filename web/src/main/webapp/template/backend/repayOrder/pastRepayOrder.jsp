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
					<input id="searchProduct" type="hidden" name="search_LIKE_loanOrder.product.name">
				</div>
			</div>	
            
			<div class="span1 offset2">
			  <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
			</div>
			<div class="span1 offset2">
			  <button class="button button-danger" onclick="flushall();">清空</button>
			</div>
		</div>
		     <input  type="hidden"  name="search_EQ_repayPlan.paidOff" value="1"> 
		     <input type="hidden" name="search_GTE_repayPlan.overdueDays" value="1">
		     <input type="hidden" name="search_LTE_repayPlan.overdueDays" value="3">
<!-- 			<input id="GTE_dueDate" type="hidden"  name="search_GTE_repayPlan.dueDate" > 
            <input id="LTE_dueDate" type="hidden"  name="search_LTE_repayPlan.dueDate"  > -->
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
/*     var oneday = 1000 * 60 * 60 * 24;
    var today=Date.now();
    var beginThreeDay=new Date(today-oneday*3);
    var beginOneDay=new Date(today-oneday);
    $("#GTE_dueDate").attr("value",beginThreeDay.getTime());
    $("#LTE_dueDate").attr("value",beginOneDay.getTime()); */
    
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
        {title:'用户ID',dataIndex:'id',width:'10%'},
        {title:'客户姓名',dataIndex:'idCard',width:"10%",renderer: function (value) {
                if(value){
                    return value.name;
                }else{
                    return '';
                }
            }},
        {title:'注册手机号',dataIndex:'phone',width:"10%"},
        {title:'贷款金额',dataIndex:'loanOrder',width:'10%',renderer:function (value) {
			 if(value){
				return value.loanAmount;
			 }else{
				 return '';
			 }
        }},
        {title:'还款日',dataIndex:'dueDate',width:'10%',renderer:BUI.Grid.Format.dateRenderer},
        {title:'逾期天数',dataIndex:'repayPlan.overdueDays',width:'10%'},
        {title:'当期/总期',dataIndex:'termPre',width:'10%'},
        {title:'应还金额',dataIndex:'repayPlan',width:'10%',renderer:function (value) {
			 if(value){
			     return value.amount;
			 }else{
				 return '';
			 }
        }},
        {title:"资金方",dataIndex:"fundName",width:"10%"},
        {title:'产品名称',dataIndex:'product',width:'10%',renderer: function (value) {
                if(value){
                    return value.name;
                }else{
                    return '';
                }
            }}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '逾期还款列表',
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
});
 
</script>
 
</body>
</html>


