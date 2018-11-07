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
				<label class="control-label">方案期数id:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_ratePlanTermId">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">费用名称:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_name">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">还款方式(计息方式)1 等额本息 2 等本等息 3 等额本金:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_paymentType">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">收费类型 1 固定金额 2 年利率 3 月利率 4 日利率:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_feeType">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">收费:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_fee">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">计费基数 1 剩余本金 2 放款本金:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_feeBaseType">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">收费时间点 1 首个还款日收 2 按月收 3放款时收:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_paymentTimeType">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">创建时间:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_createdTime">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">更新时间:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_updateTime">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">版本:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_version">
				</div>
			</div>
			<div class="span3 offset2">
			  <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
			</div>
		</div>
		</form>
		<!-- 修改新增 -->
		<div id="addOrUpdate" class="hide">
		<form id="addOrUpdateForm" class="form-inline">
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>方案期数id:</label>
					<div class="controls">
						<input name="ratePlanTermId" type="text" 
							data-rules="{required:true,number:true}"
							class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>费用名称:</label>
					<div class="controls">
						<input name="name" type="text" 
							data-rules="{required:true,}"
							class="input-normal control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label">还款方式(计息方式)1 等额本息 2 等本等息 3 等额本金:</label>
					<div class="controls">
						<input name="paymentType" type="text" 
							data-rules="{required:false,number:true}"
							class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label">收费类型 1 固定金额 2 年利率 3 月利率 4 日利率:</label>
					<div class="controls">
						<input name="feeType" type="text" 
							data-rules="{required:false,number:true}"
							class="input-normal control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label">收费:</label>
					<div class="controls">
						<input name="fee" type="text" 
							data-rules="{required:false,number:true}"
							class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label">计费基数 1 剩余本金 2 放款本金:</label>
					<div class="controls">
						<input name="feeBaseType" type="text" 
							data-rules="{required:false,number:true}"
							class="input-normal control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label">收费时间点 1 首个还款日收 2 按月收 3放款时收:</label>
					<div class="controls">
						<input name="paymentTimeType" type="text" 
							data-rules="{required:false,number:true}"
							class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>创建时间:</label>
					<div class="controls">
						<input name="createdTime" type="text" 
							data-rules="{required:true,}"
							class="calendar calendar-time">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>更新时间:</label>
					<div class="controls">
						<input name="updateTime" type="text" 
							data-rules="{required:true,}"
							class="calendar calendar-time">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label">版本:</label>
					<div class="controls">
						<input name="version" type="text" 
							data-rules="{required:false,number:true}"
							class="input-normal control-text">
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

BUI.use(['bui/ux/crudgrid'],function (CrudGrid) {
	
	//定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/ratePlanDetail"/>

    var columns = [
		 {title:'主键',dataIndex:'id',width:'10%'},
		 {title:'方案期数id',dataIndex:'ratePlanTermId',width:'10%'},
		 {title:'费用名称',dataIndex:'name',width:'10%'},
		 {title:'还款方式(计息方式)1 等额本息 2 等本等息 3 等额本金',dataIndex:'paymentType',width:'10%'},
		 {title:'收费类型 1 固定金额 2 年利率 3 月利率 4 日利率',dataIndex:'feeType',width:'10%'},
		 {title:'收费',dataIndex:'fee',width:'10%'},
		 {title:'计费基数 1 剩余本金 2 放款本金',dataIndex:'feeBaseType',width:'10%'},
		 {title:'收费时间点 1 首个还款日收 2 按月收 3放款时收',dataIndex:'paymentTimeType',width:'10%'},
		 {title:'创建时间',dataIndex:'createdTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
		 {title:'更新时间',dataIndex:'updateTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
		 {title:'版本',dataIndex:'version',width:'10%'}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '科目明细',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/backend/ratePlanDetail/list',
        addUrl : '${ctx}/backend/ratePlanDetail/add',
        updateUrl : '${ctx}/backend/ratePlanDetail/update',
        removeUrl : '${ctx}/backend/ratePlanDetail/del',
        columns : columns,
		showAddBtn : add,
		showUpdateBtn : update,
		showRemoveBtn : del,
		addOrUpdateFormId : 'addOrUpdateForm',
		dialogContentId : 'addOrUpdate',
		gridCfg:{
    		innerBorder:true
    	},
		storeCfg:{//定义store的排序，如果是复合主键一定要修改
			sortInfo : {
				field : 'id',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
				}
			}
		});
});
 
</script>
 
</body>
</html>


