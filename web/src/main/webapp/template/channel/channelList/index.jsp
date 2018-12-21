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


    BUI.use(['bui/ux/crudgrid','bui/common/search','bui/common/page','bui/overlay','bui/select','bui/data'],function (CrudGrid,Search,Grid,Overlay,Select,Data) {


        //定义页面权限
        var add=false,update=false,list=false,del=false
        //"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
        <framwork:crudPermission resource="/channel/channelList"/>

        var columns = [
            {title:'注册时间',dataIndex:'user',width:'150px',renderer: function (value) {
					if(value){
					    return value.createdTime;
					}
                }},
            {title:'注册渠道ID',dataIndex:'card',width:"80px",renderer: function (value) {
                    if(value){
                        return value.registChannelId;
                    }else{
                        return '';
                    }
                }},
            {title:'客户名字',dataIndex:'idcard',width:"150px",renderer: function (value) {
                    if(value){
                        return value.name;
                    }else{
                        return '';
                    }
                }},
            {title:'手机号码',dataIndex:'user',width:"150px",renderer:function (value) {
                    if(value) {
                        return value.phone;
                    }else{
                        return '';
                    }
                }},

            {title:'所属产品',dataIndex:'product',renderer:function (value) {
                    if(value){
                        return value.remark;
                    }else{
                        return '';
                    }
                }},
            {title:'状态',dataIndex:'card',width:'100px',renderer: function (value) {
                    if(value){
                        return value.status;
                    }else{
                        return null;
                    }
                }},
            {title:'授信额度',dataIndex:'card',width:'100px',renderer: function (value) {
                    if(value){
                        return value.creditSum;
                    }else{
                        return null;
                    }
                }},
            {title:'提现金额',dataIndex:'loanAmount',width:'80px'},
            {title:'所在城市',dataIndex:'person',width:'130px',renderer: function (value) {
                    if(value){
                        return value.cityName;
                    }else{
                        return null;
                    }
                }},
            {title:'邀请码',dataIndex:'user',width:'130px',renderer:function (value) {
					if(value){
                        return value.invitationCode;
					}else{
					    return null;
					}
                }}

        ];



        var crudGrid = new CrudGrid({
            entityName : '借款订单表',
            pkColumn : 'id',//主键
            storeUrl : '${ctx}/channel/channelList/list',
            addUrl : '${ctx}/channel/channelList/add',
            updateUrl : '${ctx}/channel/channelList/update',
            removeUrl : '${ctx}/channel/channelList/del',
            columns : columns,
            showAddBtn : add,
            showUpdateBtn : update,
            showRemoveBtn : del,
            gridCfg:{
                innerBorder:true,

            }
        });



    });






</script>

</body>
</html>


