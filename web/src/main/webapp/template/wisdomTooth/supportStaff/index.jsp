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
				<label class="control-label">客服名字:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_name">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">客服邮箱:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_email">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">是否启用:</label>
				<div class="controls bui-form-field-select"  data-items="{'1':'是','0':'否',' ':'全部'}" class="control-text input-small">
					<input name="search_EQ_enable" type="hidden" value="">
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
					<label class="control-label"><s>*</s>客服名字:</label>
					<div class="controls">
						<input name="name" type="text" 
							data-rules="{required:false,}"
							class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>客服邮箱:</label>
					<div class="controls">
						<input name="email" type="text" 
							data-rules="{required:false,}"
							class="input-normal control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>是否启用:</label>
					<div class="controls bui-form-field-select"  data-items="{'1':'启用','0':'停用'}" class="control-text input-small">
						<input name="enable" type="hidden" value="" >
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

BUI.use(['bui/ux/crudgrid'],function (CrudGrid) {

    var enumObj = {"1":"启用","0":"停用"};
    //定义页面权限
	var add=false,update=false,del=false,list=false;
    <framwork:crudPermission resource="/wisdomTooth/supportStaff"/>

	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
    var columns = [
		 {title:'客服名字',dataIndex:'name',width:'10%'},
		 {title:'客服邮箱',dataIndex:'email',width:'10%'},
		 {title:'是否启用',dataIndex:'enable',width:'10%',renderer:BUI.Grid.Format.enumRenderer(enumObj)}
        ];

	var crudGrid = new CrudGrid({
		entityName : 'SupportStaff',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/wisdomTooth/supportStaff/list',
        addUrl : '${ctx}/wisdomTooth/supportStaff/add',
        updateUrl : '${ctx}/wisdomTooth/supportStaff/update',
        removeUrl : '${ctx}/wisdomTooth/supportStaff/del',
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


