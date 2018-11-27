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
				<label class="control-label">一级渠道标识:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_channelCode">
				</div>
			</div>

			<div class="control-group span7">
				<label class="control-label">一级渠道名称:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_primaryChannelName">
				</div>
			</div>
		<!-- 	<div class="control-group span_width">
				<label class="control-label">渠道大类:</label>
				<div class="controls bui-form-field-select height_auto"  data-items="{' ':'全部','A-线上场景':'A-线上场景','A-应用商店':'A-应用商店','A-微信推广':'A-微信推广','A-品牌推广':'A-品牌推广','A-贷款超市':'A-贷款超市','A-数据营销':'A-数据营销','B-线下渠道':'B-线下渠道','B-O2O':'B-O2O','C-其他':'C-其他'}" class="control-text input-small">
					<input name="search_EQ_channelSort" type="hidden" >
				</div>
			</div> -->
			<div class="control-group span_width">
				<label class="control-label">渠道大类:</label>
				<div id="channelSortSearchSelect" class="controls height_auto"   class="control-text input-small">
					<input id="channelSortSearch" name="search_EQ_channelSort" type="hidden" >
				</div>
			</div> 
			<div class="span3 offset1">
			  <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
			</div>
            <div class="span3 offset1">
                <button class="button button-danger" onclick="flushall();">清空</button>
            </div>
		</div>
		</form>
		<!-- 修改新增 -->
		<div id="addOrUpdate" class="hide">
		<form id="addOrUpdateForm" class="form-inline">
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>渠道标识:</label>
					<div class="controls">
						<input id="channelCode"
							   name="channelCode"
							   type="text"
							   data-rules="{required:true,}"
							   data-remote="${ctx}/backend/channelPrimary/checkChannelCode"
							   class="input-normal control-text"
							   >
					</div>
				</div>
			</div>

			<div class="row">

				<div class="control-group span8">
					<label class="control-label"><s>*</s>渠道名称:</label>
					<div class="controls">
						<input name="primaryChannelName" type="text" data-rules="{required:true}" data-remote="${ctx}/backend/channelPrimary/checkChannelName" class="input-normal control-text">
					</div>
				</div>
			</div>

			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>渠道大类:</label>
					<!-- <div class="controls bui-form-field-select" data-items="{'A-线上场景':'A-线上场景','A-应用商店':'A-应用商店','A-微信推广':'A-微信推广','A-品牌推广':'A-品牌推广','A-贷款超市':'A-贷款超市','A-数据营销':'A-数据营销','B-线下渠道':'B-线下渠道','B-O2O':'B-O2O','C-其他':'C-其他'}"
						 class="control-text input-small">
						<input name="channelSort" type="hidden" value="">
					</div> -->
					 <div id="channelSortUpdateSelect" class="controls"
						 class="control-text input-small">
						<input id="channelSortUpdate" name="channelSort" type="hidden" value="">
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


    function flushall(){
        var elementsByTagName = document.getElementsByTagName("input");
        for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
        }
    }

BUI.use(['bui/ux/crudgrid','bui/select'],function (CrudGrid,Select) {

	  var channelSortSearchSelect = new Select.Select({
	        render:'#channelSortSearchSelect',
	        valueField:'#channelSortSearch',
	        items:JSON.parse('${channelSortList}')
	    });
	  channelSortSearchSelect.render();
	    
	    var channelSortUpdateSelect = new Select.Select({
	        render:'#channelSortUpdateSelect',
	        valueField:'#channelSortUpdate',
	        items:JSON.parse('${channelSortList}')
	    });
	    channelSortUpdateSelect.render();
	
	//定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/channelPrimary"/>

    var columns = [
		 {title:'id',dataIndex:'id',width:'10%'},
		 {title:'渠道标识',dataIndex:'channelCode',width:'20%'},
		 {title:'一级渠道名称',dataIndex:'primaryChannelName',width:'20%'},
		 {title:'渠道大类',dataIndex:'channelSort',width:'20%'},
		 {title:'创建时间',dataIndex:'createdTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer},
		 {title:'更新时间',dataIndex:'updateTime',width:'10%',renderer:BUI.Grid.Format.datetimeRenderer}
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '一级渠道',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/backend/channelPrimary/list',
        addUrl : '${ctx}/backend/channelPrimary/add',
        updateUrl : '${ctx}/backend/channelPrimary/update',
        removeUrl : '${ctx}/backend/channelPrimary/del',
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
				field : 'updateTime',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
				}
			}
		});


    var addOrUpdateForm = crudGrid.get('addOrUpdateForm');

    var update = true;

    addOrUpdateForm.getField('primaryChannelName').on('remotecomplete',function(){
        if(update){
            addOrUpdateForm.getField('primaryChannelName').clearErrors();
        }
    });

    addOrUpdateForm.getField('channelCode').on('remotecomplete',function(){
        if(update){
            addOrUpdateForm.getField('channelCode').clearErrors();
        }
    });


    var beforeAddShow = function(dialog,form){
        update = false;
    };
    crudGrid.on('beforeAddShow', beforeAddShow);


    var beforeUpdateShow = function(dialog,form,record){
        update = true;
        form.getField('primaryChannelName').disable();
        form.getField('channelCode').disable();
    };

    crudGrid.on('beforeUpdateShow', beforeUpdateShow);
});
 
</script>
 
</body>
</html>


