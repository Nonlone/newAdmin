<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../../common/import-tags.jsp"%>
<html>
<head>
	<title><spring:eval expression="@webConf['admin.title']" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../../common/import-static.jsp"%>
</head>
<body>
<div class="container">
	<!-- 查询 -->
	<form id="searchForm" class="form-horizontal search-form">
		<div class="row">
			<div class="control-group span7">
				<label class="control-label">客户姓名:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_idcard.name">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">手机号:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_user.phone">
				</div>
			</div>

			<div class="control-group span_width">
				<label class="control-label">收到补件通知日期时间:</label>
				<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
					<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
					<input type="text" class="calendar" name="search_GTE_createdTime" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_createdTime" type="text" class="calendar" data-tip="{text : '结束日期'}">
				</div>
			</div>


			<%--<div class="control-group span_width">--%>
			<%--<label class="control-label">申请日期:</label>--%>
			<%--<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">--%>
			<%--<!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->--%>
			<%--<input type="text" class="calendar" name="search_GTE_applyTime" data-tip="{text : '开始日期'}"> <span>--%>
			<%--- </span><input name="search_LTE_loanOrder.applyTime" type="text" class="calendar" data-tip="{text : '结束日期'}">--%>
			<%--</div>--%>
			<%--</div>--%>
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

	</div>
	<div class="search-grid-container">
		<div id="grid"></div>
	</div>
</div>

<script type="text/javascript">

    function openout(id) {
        window.open('${IP}'+'${ctx}/backend/supply/requirement/detail/'+id);
    }


    function flushall(){
        var elementsByTagName = document.getElementsByTagName("input");
        for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
        }
    }

    BUI.use(['bui/ux/crudgrid','bui/select','bui/data'],function (CrudGrid,Select,Data) {

        //定义页面权限
        var add=false,update=false,del=false,list=false;
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/backend/supply/requirement"/>

        var columns = [
            {title:'提现订单编号',dataIndex:'loanOrder',width:"170px",renderer:function (value) {
                    if(value){
                        return value.id;
                    }
                    return '';
                }},
            {title:'客户姓名',dataIndex:'idCardData',width:"135px",renderer: function (value) {
                    if(value){
                        return value.name;
                    }else{
                        return '';
                    }
                }},
            {title:'注册手机号',dataIndex:'user',width:"150px",renderer:function (value) {
                    if(value){
                        return value.phone;
                    }else{
                        return '';
                    }
                }},

            {title:'申请提现金额',dataIndex:'loanOrder',width:'135px',renderer: function (value) {
                    if(value){
                        return value.applyAmount;
                    }else{
                        return '';
                    }
                }},
            {title:'新网补件日期（收到总部补件请求时间）',dataIndex:'createdTime',width:'270px',renderer:BUI.Grid.Format.dateRenderer},
            {title:'补件次数',dataIndex:'supplyCount',width:'135px'},
            {title:'资金方',dataIndex:'fundName',width:'135px'},
            {title:'产品名称',dataIndex:'loanOrder',width:'135px',renderer:function (value) {
                    if(value){
                        if(value.product){
                            return value.product.name;
                        }
                    }
					return '';

                }},
            {title:'客户ID',dataIndex:'user',width:'135px',renderer:function (value) {
                    if(value){
                        return value.id;
                    }else{
                        return '';
                    }
                }}
        ];

        var crudGrid = new CrudGrid({
            entityName : '补件记录列表',
            pkColumn : 'id',//主键
            storeUrl : '${ctx}/backend/supply/requirement/list',
            columns : columns,
            showAddBtn : add,
            showUpdateBtn : update,
            showRemoveBtn : del,
            columns : columns,
            operationColumnRenderer : function(value, obj){//操作列最追加按钮
                return CrudGrid.createLinkCustomSpan({
                    class:"page-action grid-command x-icon x-icon-info",
                    id: obj.id,
                    title: obj.idCardData.name+"补件需求",
                    text: '<i class="icon icon-white icon-list-alt"></i>',
                    href: $ctx+"/backend/supply/requirement/detail/"+obj.id
                });

            },
            storeCfg:{//定义store的排序，如果是复合主键一定要修改
                sortInfo : {
                    field : 'createdTime',//排序字段（冲突以此未标准）
                    direction : 'DESC' //升序ASC，降序DESC
                }
            }
        });
        var grid = crudGrid.get('grid');
    });

</script>

</body>
</html>


