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
		<div class="search-grid-container">
			<div id="grid"></div>
		</div>
	</div>

<script type="text/javascript">

BUI.use(['bui/ux/crudgrid','bui/form','bui/ux/savedialog','bui/overlay','bui/common/page'],function (CrudGrid,Form,Page,Overlay) {
	
	//定义页面权限
	var add=false,update=false,list=false,del=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="admin/mop/partner"/>

    var columns = [
		 {title:'变更时间',dataIndex:'createdTime',width:'30%'},
		 {title:'操作帐号',dataIndex:'operator',width:'30%'},
         {title:'变更内容',dataIndex:'changeDesc',width:'40%'}
        ];
	var crudGrid = new CrudGrid({
		entityName : '变更记录',
      	storeUrl : '${ctx}/mop/partner/changeLog/list',
        columns : columns,
        showAddBtn : false,
        showUpdateBtn : false,
        showRemoveBtn : false,
		gridCfg:{
    		innerBorder:true
    	}});

	/* 显示提示框 */
	function showWarning(str) {
        BUI.Message.Alert(str,'warning');
    }
    function showSuccess(str) {
        BUI.Message.Alert(str,'success');
    }

});
</script>
 
</body>
</html>


