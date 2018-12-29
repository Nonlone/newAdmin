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
				<label class="control-label">关键字类别:</label>
				<div class="controls">
					<input type="text" maxlength="20" class="input-normal control-text" name="keywordsType">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">关键字词条:</label>
				<div class="controls">
					<input type="text" maxlength="20" class="input-normal control-text" name="keywords">
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
						<label for="keywordsType" class="control-label">关键字类别</label>
						<div class="controls">
							<input type="text" maxlength="20" class="form-control"  id="keywordsType"
								   name="keywordsType" data-rules="{required:true,}" placeholder="关键字类别">
						</div>
					</div>
					<div class="control-group span10">
						<label for="keywords" class="control-label">关键字词条</label>
						<div class="controls">
							<input type="text" class="form-control" id="keywords" name="keywords" style="width:100%"
								   maxlength="20" data-rules="{required:true,}" placeholder="关键字词条">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="control-group span24">
						<label for="content" class="control-label">关键字回复内容</label>
						<div class="controls control-row10">
							<textarea class="form-control text-size" name="content"
									maxlength="500"  id="content"rows="10"></textarea>
						</div>
					</div>
				</div>
				<br/>
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
	<framwork:crudPermission resource="/message/weixinReplyTemplate"/>

    var columns = [
		{title:'关键字类别',dataIndex:'keywordsType'},
		{title:'关键字词条',dataIndex:'keywords',width: '10%'},
		{title:'关键字回复内容',dataIndex:'content',width: '80%'},
        {title:'创建时间',dataIndex:'createdTime'},
        {title:'更新时间',dataIndex:'updateTime'}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '微信自动回复语模板',
    	pkColumn : 'id',//主键
      	storeUrl : '/message/weixinReplyTemplate/list',
        addUrl : '/message/weixinReplyTemplate/add',
        updateUrl : '/message/weixinReplyTemplate/update',
        removeUrl : '/message/weixinReplyTemplate/delete',
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


