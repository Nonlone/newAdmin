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
					<input type="text" data-tip="{text : '请输入订单号'}" class="input-normal control-text" name="search_LIKE_loanOrderId">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">客户ID:</label>
				<div class="controls">
					<input type="text" data-tip="{text : '请输入客户ID'}" class="input-normal control-text" name="search_LIKE_userId">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">客户姓名:</label>
				<div class="controls">
					<input type="text" data-tip="{text : '请输入客户姓名'}" class="input-normal control-text" name="search_LIKE_idcard.name">
				</div>
			</div>
           <div class="control-group span6">
				<label class="control-label">产品:</label>
				<div id="selectProduct" class="controls">
					<input id="searchProduct" type="hidden" name="search_EQ_loanOrder.productId">
				</div>
			</div>
			<div class="control-group span6">
				<label class="control-label">资金方:</label>
				<div id="selectPayFund" class="controls">
					<input id="searchPayFund" type="hidden" name="search_EQ_loanOrder.payFundId">
				</div>
			</div>
			<input id="repayPlan_dueDate" type="hidden" name="search_EQ_dueDate" >
			<div class="span1 offset2">
			  <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
			</div>
			<div class="span1 offset2">
			  <button class="button button-danger" onclick="flushall();">清空</button>
			</div>
			
			<div class="span1 offset2">
			  <button type="button" class="button button-primary" onclick="downLoad();">导出</button>
			</div>
		</div>
		<input type="hidden" name="search_EQ_term" value="1" />
		<input type="hidden" name="search_EQ_paidOff" value="0" />
		</form>
		<!-- 修改新增 -->
		<div id="addOrUpdate" class="hide">

		</div>
		<div class="search-grid-container">
			<div id="grid"></div>
		</div>
	</div>

<script type="text/javascript">

    Date.prototype.format = function (format) {
           var args = {
               "M+": this.getMonth() + 1,
               "d+": this.getDate(),
               "h+": this.getHours(),
               "m+": this.getMinutes(),
               "s+": this.getSeconds(),
               "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
               "S": this.getMilliseconds()
           };
           if (/(y+)/.test(format))
               format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
           for (var i in args) {
               var n = args[i];
               if (new RegExp("(" + i + ")").test(format))
                   format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
           }
           return format;
       };
       function downLoad(){
    		 var downLoadUrl='${ctx}/backend/loan/debt/downLoadFirstRepayOrder';
    		 var $form=$("#searchForm");
    		 var oldAction=$form.attr("action");
    		 $form.attr("action",downLoadUrl);
    		 $form.submit();
    		 $form.attr("action",oldAction);
    	 }
    function openout(id) {
        window.open('${IP}'+'${ctx}/backend/loan/repayOrder/auth/'+id);
    }

    var oneday = 1000 * 60 * 60 * 24;
    var today=Date.now();
    var date=new Date(today+oneday*5);
    $("#repayPlan_dueDate").val(date.format("yyyy-MM-dd"));
    function changeDueDate(obj){
    	var tDate=new Date($(obj).val());
    	var dueDate=new Date(tDate.getTime()+oneday*5);
    	$("#repayPlan_dueDate").val(dueDate.format("yyyy-MM-dd"));
    }
    
    
    function flushall(){
	    var elementsByTagName = document.getElementsByTagName("input");
	    for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
		}
	    $("#repayPlan_dueDate").val(new Date(today+oneday*5));
	}
    
    

    BUI.use(['bui/ux/crudgrid','bui/select','bui/data'],function (CrudGrid,Select,Data) {

        selectProduct = new Select.Select({
            render: '#selectProduct',
            valueField: '#searchProduct',
            items:JSON.parse('${productList}')
        });
        selectProduct.render();
       
        var selectFundStore = new Data.Store({
            url : '${ctx}/backend/fund/getFundList',
            autoLoad : true
        });

        selectFundStatus = new Select.Select({
            render:'#selectPayFund',
            valueField:'#searchPayFund',
            store:selectFundStore
        });
        selectFundStatus.render();


        //定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/loan/debt"/>


    var columns = [
        {title:'客户ID',dataIndex:'userId',width:'150px'},
        {title:'客户姓名',dataIndex:'idcard',width:"150px",renderer: function (value) {
                if(value){
                    return value.name;
                }else{
                    return '';
                }
            }},
        {title:'注册手机号',dataIndex:'user',width:"150px",renderer:function (value) {
			 if(value){
					return value.phone;
				 }else{
					 return '';
				 }
	        }},
        {title:'贷款金额',dataIndex:'loanOrder',width:'150px',renderer:function (value) {
			 if(value){
				return value.loanAmount;
			 }else{
				 return '';
			 }
        }},
        {title:'首个还款日',dataIndex:'dueDate',width:'150px',renderer:function (value) {
			 if(value){
					return BUI.Grid.Format.dateRenderer(value);
				 }else{
					 return '';
				 }
	        }},
	    {title:'总期数',dataIndex:'orderTerm',width:'100px'},
        {title:'首期总费用',dataIndex:'amount',width:'150px'},
        {title:"评审费",dataIndex:"orderPlande",width:"150px",renderer:function (value) {
			 if(value){
					return value.approveFeeAmount;
				 }else{
					 return '';
				 }
	        }},
        {title:"担保费",dataIndex:"orderPlande",width:"150px",renderer:function (value) {
			 if(value){
			     return value.guaranteeFeeAmount;
			 }else{
				 return '';
			 }
        }},
        {title:"本息",dataIndex:"orderPlande",width:"150px",renderer:function (value) {
			 if(value){
				   if(value.interestAmount){
					   return value.pincipalAmount+value.interestAmount;
				   }				   
					return value.interestAmount;
				 }else{
					 return '';
				 }
	        }},
        {title:"资金方",dataIndex:"fundName",width:"100px"},
        {title:'产品名称',dataIndex:'product',width:'100px',renderer: function (value) {
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
      	storeUrl : '${ctx}/backend/loan/debt/list',
        columns : columns,
		showAddBtn : false,
		showUpdateBtn : false,
		showRemoveBtn : false,
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


