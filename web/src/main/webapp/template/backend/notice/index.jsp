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
			<div class="control-group span_width">
				<label class="control-label">发布时间：</label>
				<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
					<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
					<input type="text" class="calendar-time calendar" name="search_GTE_maintable.publishTime" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_maintable.publishTime" type="text" class="calendar-time calendar" data-tip="{text : '结束日期'}">
				</div>
			</div>
			<div class="control-group span_width">
				<label class="control-label">过期时间：</label>
				<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
					<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
					<input type="text" class="calendar" name="search_GTE_maintable.expiredTime" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_maintable.expiredTime" type="text" class="calendar" data-tip="{text : '结束日期'}">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">公告信息:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_maintable.noticeText">
				</div>
			</div>
			<!--
			<div class="control-group span7">
				<label class="control-label">createdTime:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_createdTime">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">updateTime:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_updateTime">
				</div>
			</div>
			-->
			<div class="span3 offset2">
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
					<label class="control-label"><s>*</s>发布时间:</label>
					<div class="controls">
						<input name="publishTime" type="text"
							data-rules="{required:true,}"
						class="calendar calendar-time">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>过期时间:</label>
					<div class="controls">
						<input name="expiredTime" type="text"
							   data-rules="{required:true,}"
							   class="calendar calendar-time">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span15">
					<label class="control-label"><s>*</s>公告信息:</label>
					<div class="controls control-row4">
						<textarea name="noticeText" data-rules="{required:true,}" class="input-large"></textarea>
					</div>
				</div>
				<!--
				<div class="control-group span8">
					<label class="control-label">createdTime:</label>
					<div class="controls">
						<input name="createdTime" type="text" 
							data-rules="{required:false,}"
							class="calendar calendar-time">
					</div>
				</div>
				-->
			</div>
			<!--
			<div class="row">
				<div class="control-group span8">
					<label class="control-label">updateTime:</label>
					<div class="controls">
						<input name="updateTime" type="text" 
							data-rules="{required:false,}"
							class="calendar calendar-time">
					</div>
				</div>
			</div>
			-->
			<input type="hidden" name="id" value="">
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
	<framwork:crudPermission resource="/backend/notice"/>

    var columns = [
		 {title:'id',dataIndex:'id',width:'20%'},
		 {title:'发布时间',dataIndex:'publishTime',width:'20%',renderer:BUI.Grid.Format.datetimeRenderer},
        {title:'过期时间',dataIndex:'expiredTime',width:'20%',renderer:BUI.Grid.Format.datetimeRenderer},
        {title:'公告信息',dataIndex:'noticeText',width:'45%'}
		 //{title:'createdTime',dataIndex:'createdTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
		 //{title:'updateTime',dataIndex:'updateTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '公告',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/backend/notice/list',
        addUrl : '${ctx}/backend/notice/add',
        updateUrl : '${ctx}/backend/notice/update',
        removeUrl : '${ctx}/backend/notice/del',
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


