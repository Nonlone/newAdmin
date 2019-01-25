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
				<label class="control-label">类型代码:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_typeCode">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">配置名称:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_name">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">备注:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_remark">
				</div>
			</div>
			<!--
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
			-->
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
		<form id="addOrUpdateForm" class="form-inline">
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>类型代码</label>
					<div class="controls">
						<input name = "typeCode" type="text" data-rules="{required:true,}" class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>配置名称:</label>
					<div class="controls">
						<input name="name" type="text" 
							data-rules="{required:true,}"
							class="input-normal control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span15">
					<label class="control-label">备注:</label>
						<div class="controls control-row4">
							<textarea  name="remark" class="input-large"></textarea>
						</div>
				</div>
			</div>
		</form>
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

BUI.use(['bui/ux/crudgrid'],function (CrudGrid) {
	
	//定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/appConfigType"/>

    var columns = [
		 {title:'类型代码',dataIndex:'typeCode',width:'15%'},
		 {title:'配置名称',dataIndex:'name',width:'35%'},
		 {title:'备注',dataIndex:'remark',width:'50%'}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : 'APP配置分类',
    	pkColumn : 'typeCode',//主键
      	storeUrl : '${ctx}/backend/appConfigType/list',
        addUrl : '${ctx}/backend/appConfigType/add',
        updateUrl : '${ctx}/backend/appConfigType/update',
        removeUrl : '${ctx}/backend/appConfigType/delModel',
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
				field : 'typeCode',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
				}
			}
		});
});
 
</script>
 
</body>
</html>


