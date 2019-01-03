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
			width: 600px;
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
					<input type="text" maxlength="10" class="input-normal control-text" name="code">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">模板名称:</label>
				<div class="controls">
					<input type="text" maxlength="100" class="input-normal control-text" name="name">
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
				<input type="hidden" name="id" value="">
				<div class="row">
					<div class="control-group span10">
						<label for="code" class="control-label">模板code</label>
						<div class="controls">
							<input type="text" class="form-control" maxlength="10" data-rules="{required:true,}"
								   id="code" name="code" placeholder="模板code">
						</div>
					</div>
					<div class="control-group span10">
						<label for="name" class="control-label">模板名称</label>
						<div class="controls">
							<input type="text" class="form-control" data-rules="{required:true,}" id="name"
								   maxlength="100"  name="name" placeholder="模板名称">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="control-group span10">
						<label for="title" class="control-label">内容标题</label>
						<div class="controls">
							<input type="text" class="form-control" data-rules="{required:true,}" id="title"
								   maxlength="100" name="title" placeholder="内容标题">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="control-group span24">
						<label for="mailContent" class="control-label">站内信内容</label>
						<div class="controls">
						<textarea class="form-control text-size" maxlength="1000" name="mailContent" id="mailContent"
								  rows="3"></textarea>
						</div>
					</div>
				</div><br/><br/>
				<div class="row">
					<div class="control-group span24">
						<label for="smsContent" class="control-label">短信内容</label>
						<div class="controls">
						<textarea class="form-control text-size" maxlength="1000" name="smsContent" id="smsContent"
								  rows="3"></textarea>
						</div>
					</div>
				</div><br/><br/>
				<div class="row">
					<div class="control-group span24">
						<label for="pushContent"class="control-label">推送内容</label>
						<div class="controls">
						<textarea class="form-control text-size" maxlength="1000" name="pushContent" id="pushContent"
								  rows="3"></textarea>
						</div>
					</div>
				</div><br/><br/>
				<div class="row">
					<div class="control-group span8">
						<label for="name" class="control-label">推送极光通知</label>
						<div class="controls bui-form-field-select" data-items="{'0':'启用','1':'禁用'}"
							 class="control-text input-small">
							<input name="disablePushNotice" data-rules="{required:true,}" type="hidden"  value="">
						</div>
					</div>
					<div class="control-group span10">
						<label for="name" class="control-label">推送极光消息</label>
						<div class="controls bui-form-field-select" data-items="{'0':'启用','1':'禁用'}"
							 class="control-text input-small">
							<input name="disablePushMsg" data-rules="{required:true,}" type="hidden" value="">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="control-group span10">
						<label for="name" class="control-label">是否禁用模板</label>
						<div class="controls bui-form-field-select" data-items="{'1':'启用','0':'禁用'}"
							 class="control-text input-small">
							<input name="status" type="hidden" data-rules="{required:true,}" value="">
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
    var enumObj = {"true": "启用", "false": "禁用"};
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
        addUrl : '/message/noticeTemplate/add',
        updateUrl : '/message/noticeTemplate/update',
        removeUrl : '/message/noticeTemplate/delete',
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


