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
				<label class="control-label">模板code:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="noticeCode">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">模板标题:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="noticeTitle">
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
			<form id="addOrUpdateForm" class="form-inline">
				<div class="row">
					<div class="control-group span4">
						<label class="control-label">模板code:</label>
						<div class="controls">
							<input type="text" class="input-normal control-text" name="code">
						</div>
					</div>
					<div class="control-group span10">
						<label class="control-label">模板名称:</label>
						<div class="controls">
							<input type="text" class="input-normal control-text" name="name">
						</div>
					</div>
					<div class="control-group span10">
						<label class="control-label">内容标题:</label>
						<div class="controls">
							<input type="text" class="input-normal control-text" name="title">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="control-group">
						<label class="control-label">站内信内容:</label>
						<div class="controls">
							<input type="text" class="input-normal control-text" name="mailContent">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="control-group">
						<label class="control-label">短信内容:</label>
						<div class="controls">
							<input type="text" class="input-normal control-text" name="smsContent">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="control-group">
						<label class="control-label">推送内容:</label>
						<div class="controls">
							<input type="text" class="input-normal control-text" name="pushContent">
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
	var add=true,update=true,del=true;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/message/noticeTemplate"/>

    var columns = [
		{title:'模板code',dataIndex:'code'},
		{title:'模板标题',dataIndex:'title'},
		{title:'模板名称',dataIndex:'name'},
        {title:'站内信内容',dataIndex:'mailContent',width: '25%'},
        {title:'短信内容',dataIndex:'smsContent',width: '25%'},
        {title:'推送内容',dataIndex:'pushContent',width: '25%'},
        {title:'创建时间',dataIndex:'createdTime'},
        {title:'更新时间',dataIndex:'updateTime'}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '消息模板',
    	pkColumn : 'id',//主键
      	storeUrl : '/message/noticeTemplate/list',
        addUrl : '/backend/appConfigType/add',
        updateUrl : '/backend/appConfigType/update',
        removeUrl : '/backend/appConfigType/del',
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
				field : 'code',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
				}
			}
		});
});
 
</script>
 
</body>
</html>


