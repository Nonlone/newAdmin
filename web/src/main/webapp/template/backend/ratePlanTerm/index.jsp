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
				<label class="control-label">方案id:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_ratePlanId">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">期数:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_term">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">权重:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_weight">
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
					<label class="control-label"><s>*</s>方案id:</label>
					<div class="controls">
						<input name="ratePlanId" type="text" 
							data-rules="{required:true,number:true}"
							class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label">期数:</label>
					<div class="controls">
						<input name="term" type="text" 
							data-rules="{required:false,number:true}"
							class="input-normal control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label">权重:</label>
					<div class="controls">
						<input name="weight" type="text" 
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
	<framwork:crudPermission resource="/backend/ratePlanTerm"/>

    var columns = [
		 {title:'主键',dataIndex:'id',width:'10%'},
		 {title:'方案id',dataIndex:'ratePlanId',width:'10%'},
		 {title:'期数',dataIndex:'term',width:'10%'},
		 {title:'权重',dataIndex:'weight',width:'10%'},
		 {title:'创建时间',dataIndex:'createdTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
		 {title:'更新时间',dataIndex:'updateTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
		 {title:'版本',dataIndex:'version',width:'10%'}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '方案相关期数配置',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/backend/ratePlanTerm/list',
        addUrl : '${ctx}/backend/ratePlanTerm/add',
        updateUrl : '${ctx}/backend/ratePlanTerm/update',
        removeUrl : '${ctx}/backend/ratePlanTerm/del',
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


