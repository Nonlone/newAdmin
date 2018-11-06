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
		<form id="searchForm" class="form-horizontal search-form" hidden="true">
		<div class="row">
			<div class="control-group span7" hidden="true">
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_EQ_userId" value="${userId}">
				</div>
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

function flushall(){
	var elementsByTagName = document.getElementsByTagName("input");
	for(var i = 0;i<elementsByTagName.length;i++){
		elementsByTagName[i].innerText = "";
	}
}

function openout(id) {
    window.open('${IP}'+'${ctx}/backend/loanOrder/auth/'+id);
}

function stop(id) {
    BUI.use('bui/overlay',function (Overlay){
        BUI.Message.Confirm('确认要终止放款么？',function(){
            $.ajax({
                url:'${rejectCash}',
                dataType:'JSON',
                headers: {'Content-type':'application/json'},
                type:'POST',
                async:true,
                data:"{\"loanOrderId\":\""+id+"\"}",
                //contentType: 'application/json;charset=utf-8',
                success:function(result){dev_admin
                    if(result.code=="SUC000"){
                        BUI.Message.Alert('操作成功！',function(){
                        },'success');
                    }else{
                        BUI.Message.Alert('提交终止放款失败！',function(){
                        },'false');
                    }
                }});

        },'question');
    });
}


BUI.use(['bui/ux/crudgrid','bui/common/search','bui/common/page','bui/overlay'],function (CrudGrid,Search,Grid,Overlay) {


	//定义页面权限
	var add=false,update=false,list=false,del=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/loanOrder"/>

    var authUrl,authBtn=false;//授权按钮
		authBtn = true;
		authUrl = '${ctx}/backend/loan/loanOrder/auth/';



    var enumObj = {"10":"提现审批处理中","11":"提现被拒","20":"待确认提现","30":"待放款","40":"放款处理中","50":"已放款/待还款","99":"已取消","100":"已结清","21":"取消提现"};


    var columns = [
        {title:'订单编号',dataIndex:'id',width:'8%'},

        {title:'客户姓名',dataIndex:'idcard',width:"5%",renderer: function (value) {
                if(value){
                    return value.name;
                }else{
                    return '';
                }
            }},
        {title:'身份证',dataIndex:'idcard',width:"9%",renderer: function (value) {
                if(value){
                    return value.idCard;
                }else{
                    return '';
                }
            }},
        {title:'客户ID',dataIndex:'user',renderer:function (value) {
                if(value) {
                    return value.id;
                }else{
                    return '';
                }
            }},

        {title:'注册手机号',dataIndex:'user',renderer:function (value) {
                if(value){
                    return value.phone;
                }else{
                    return "";
                }
            }},
        {title:'授信金额',dataIndex:'card.creditSum',width:'5%',renderer: function (value) {
                if(value){
                    return value;
                }else{
                    return null;
                }
            }},
        {title:'提现金额',dataIndex:'loanAmount',width:'5%'},
        {title:'期限(月)',dataIndex:'loanTerm',width:'5%'},
        {title:'资金方',dataIndex:'fundName',width:'5%',renderer: function (value) {
                if(value){
                    return value;
                }else{
                    return null;
                }
            }},
        {title:'申请时间',dataIndex:'applyTime',width:'5%',renderer:BUI.Grid.Format.datetimeRenderer},
        {title:'放款时间',dataIndex:'payLoanTime',width:'5%',renderer:BUI.Grid.Format.datetimeRenderer},
        {title:'放款状态',dataIndex:'status',width:'5%',renderer:BUI.Grid.Format.enumRenderer(enumObj)},

        {title:'产品名称',dataIndex:'product',width:'5%',renderer: function (value) {
                if(value){
                    return value.name;
                }else{
                    return '';
                }
            }},

        {title:'是否新客户',dataIndex:'',width:'5%',renderer: function (value) {
                if(value){
                    return "是";
                }else{
                    return '是';
                }
            }}

    ];


    var crudGrid = new CrudGrid({
		entityName : '借款订单表',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/backend/loan/loanOrder/listOut',
        addUrl : '${ctx}/backend/loan/loanOrder/add',
        updateUrl : '${ctx}/backend/loan/loanOrder/update',
        removeUrl : '${ctx}/backend/loan/loanOrder/del',
        columns : columns,
        showAddBtn : add,
        showUpdateBtn : update,
        showRemoveBtn : del,
        operationColumnRenderer : function(value, obj){//操作列最追加按钮

            var editStr = '';
            var id = String(obj.id);
            var detail="";
            if(authBtn){
                detail = '<span class="x-icon x-icon-info" title="'+'详细信息'+'"><i class="icon icon-list-alt icon-white"  onclick="openout(\''+id+'\');"></i></span>';

            }

            if(obj.status=="40"||obj.status=="20"){
                if(obj.cancelLoan==null){
                    editStr= detail+'&nbsp'+'<span class="x-icon x-icon-error" title="终止放款" onclick="stop(\'\'+id+\'\');"><i class="icon icon-white icon-ban-circle"></i></span>';
                }else if(obj.cancelLoan==0){
                    editStr= detail+'&nbsp'+'<span class="x-icon x-icon-error" title="终止放款" onclick="stop(\'\'+id+\'\');"><i class="icon icon-white icon-ban-circle"></i></span>';
                }else{
                    editStr = detail;
                }
            }else{
                editStr = detail;
            }


            return editStr;
        }
		});



});






</script>
 
</body>
</html>


