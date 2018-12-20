<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/import-tags.jsp"%>
<html>
<head>
	<title><spring:eval expression="@webConf['admin.title']" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/import-static.jsp"%>
	<style>
		.text-size{
			height: 60px;
			width: 500px;
		}
	</style>
</head>
<body>
	<div class="container">
		<!-- 查询 -->
		<form id="searchForm" class="form-horizontal search-form">
		<div class="row">
			<div class="control-group span7">
				<label class="control-label">模板code:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="code">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">模板名称:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="name">
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
			<form id="addOrUpdateForm" role="form" class="form-horizontal">
				<div class="form-group">
					<label for="code">模板code</label>
					<input type="text" class="form-control" id="code" name="code" placeholder="模板code">
				</div>
				<div class="form-group">
					<label for="name">模板名称</label>
					<input type="text" class="form-control" id="name" name="name" placeholder="模板名称">
				</div>
				<div class="form-group">
					<label for="title">模板名称</label>
					<input type="text" class="form-control" name="title"  id="title" placeholder="内容标题">
				</div>
				<div class="form-group">
					<label for="mailContent">站内信内容</label>
					<textarea class="form-control text-size" name="mailContent" id="mailContent"  rows="3"></textarea>
				</div>
				<div class="form-group">
					<label for="smsContent">短信内容</label>
					<textarea class="form-control text-size" name="smsContent" id="smsContent"  rows="3"></textarea>
				</div>
				<div class="form-group">
					<label for="pushContent">推送内容</label>
					<textarea class="form-control text-size" name="pushContent" id="pushContent" rows="3"></textarea>
				</div>
				<label for="name">是否禁用推送极光通知</label>
				<div>
					<label class="radio-inline">
						<input type="radio" name="disablePushNotice" value="0" checked>启用
					</label>
					<label class="radio-inline">
						<input type="radio" name="disablePushNotice" value="1">禁用
					</label>
				</div>
				<label for="name">是否禁用推送极光消息</label>
				<div>
					<label class="radio-inline">
						<input type="radio" name="disablePushMsg" value="0" checked>启用
					</label>
					<label class="radio-inline">
						<input type="radio" name="disablePushMsg" value="1">禁用
					</label>
				</div>
				<label for="name">是否禁用模板</label>
				<div>
					<label class="radio-inline">
						<input type="radio" name="status" value="0" checked>启用
					</label>
					<label class="radio-inline">
						<input type="radio" name="status" value="1">禁用
					</label>
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


