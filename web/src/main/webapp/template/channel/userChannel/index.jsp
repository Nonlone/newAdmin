<!DOCTYPE HTML>
 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
<title>${sessionScope.logon_user.systemName}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../../common/import-tags.jsp"%>
<%@include file="../../common/import-static.jsp"%>
<style type="text/css">
.bui-select .bui-select-input{
    width:60px;
 }
</style>

</head>
<body>

<div class="container">
 <!-- 查询 -->
    <form id="searchForm" class="form-horizontal search-form">
      <div class="row">
        <div class="control-group span_width">
          <label class="control-label">用户名：</label>
          <div class="controls height_auto">
            <input type="text" class="control-text input-small" name="search_LIKE_name">
          </div>
        </div>

        <div class="control-group span_width">
          <label class="control-label">状态：</label>
           <div class="controls bui-form-field-select height_auto"  data-items="{' ':'全部','1':'启用','2':'停用'}" class="control-text input-small">
            <input name="search_EQ_status" type="hidden" value="${product.isShow}" >
          </div>
        </div>
      
        <div class="control-group span_width">
          <label class="control-label">注册时间：</label>
          <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
          <!-- search_GTE_createTime_D 后面的D表示数据类型是Date -->
            <input type="text" class="calendar" name="search_GTE_createTime" data-tip="{text : '开始日期'}"> <span>
             - </span><input name="search_LTE_createTime" type="text" class="calendar" data-tip="{text : '开始日期'}">
          </div>
        </div>
        <div class="span_width">
          <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
        </div>
      </div>
    </form>
    <!-- 修改新增 -->
   <div id="addOrUpdate" class="hide">
      <form id="addOrUpdateForm" class="form-horizontal">
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>登录名</label>
                <div class="controls">
                    <input name="loginName" type="text" class="input-normal control-text">
                </div>
            </div>
            <div class="control-group span8">
              <label class="control-label">姓名</label>
                <div class="controls">
                    <input name="name" type="text" class="input-normal control-text">
                </div>
            </div>
        </div>

        <div class="row">
          <div class="control-group span8">
            <label class="control-label"><s>*</s>可看渠道</label>
              <div class="controls" id ="channels" name='channels'>
                  <input name="channels" type="hidden" id="channelIds" value='' data-rules="{required:true}">
              </div>
          </div>
        </div>
        <input type="hidden" name="extFlag" value="0">
        <input type="hidden" name="id" value="">
      </form>
    </div>
    <div class="search-grid-container">
      <div id="grid"></div>
    </div>

     <input type="hidden" name="id" value="">
      </form>
	</div> 
    
  </div>
<script type="text/javascript">
	function flushall(){
	    var elementsByTagName = document.getElementsByTagName("input");
	    for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
		}
	}

BUI.use(['bui/ux/crudgrid','bui/form','bui/ux/savedialog','bui/grid'],function (CrudGrid,Form,SaveDialog) {
	
	//定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/system/user"/>
	var changePasswdBtn = false;
  	//管理员修改密码部分
  	<shiro:hasRole name='Admin'>
		changePasswdBtn = true;
	   	var saveDialog = new SaveDialog({
	   		entityName: '密码', 
	  	     url :{
	  	    	 update : '${ctx}/system/user/changePasswd'
	  	     }, 
	      	 dialogContentId:'chPwdFormDiv',
	  		 formId:'chPwdFormId'
	     });
	</shiro:hasRole>

  	
  	
    //添加 密码校验规则
    Form.Rules.add({
      name : 'password',  //规则名称
      msg : '密码必须同时包括数字和字母',//默认显示的错误信息
      validator : function(value,baseValue,formatMsg){ //验证函数，验证值、基准值、格式化后的错误信息
        var d = new RegExp('\\d+');
        var w = new RegExp('[a-zA-Z]+');
        var a = d.test(value);
        var b = w.test(value);
        if(!(d.test(value) && w.test(value))){
          return formatMsg;
        }
      }
    });
    
    

	
	var enumObj = {"1":"启用","2":"停用"};
	
    var columns = [
          {title:'ID',dataIndex:'id',width:'5%'},
          {title:'姓名',dataIndex:'name',width:'15%'},
          {title:'可看渠道', dataIndex:'channel',width:'20%'},
          {title:'状态',dataIndex:'status',width:'20%',renderer:BUI.Grid.Format.enumRenderer(enumObj)}
        ];

    var crudGrid = new CrudGrid({
    	entityName : '用户',
    	storeUrl : '${ctx}/system/user/list',
        addUrl : '${ctx}/system/user/add',
        updateUrl : '${ctx}/system/user/update',
        removeUrl : '${ctx}/system/user/del',
        columns : columns,
        showAddBtn : add,
        showUpdateBtn : update,
        showRemoveBtn : del,
        addOrUpdateFormId : 'addOrUpdateForm',
        searchBtnId :'btnSearch',
        dialogContentId : 'addOrUpdate',
    });
    
   	var grid = crudGrid.get('grid');
    grid.on('cellclick',function(ev){//定义点击行的出发事件
        var sender = $(ev.domTarget); //点击的Dom
        var record = ev.record;
        if(sender.hasClass('icon-lock')){
        	from = saveDialog.get('form');
        	from.getField('id').set('value',record.id);
        	saveDialog.update();
        }
    });

    var addOrUpdateForm = crudGrid.get('addOrUpdateForm');

    var update = true;

    
    var beforeUpdateShow = function(dialog,form,record){
        form.getField("name").disable();
        form.getField("loginName").disable();
    	var channels = record.channels;
  	  	var channelList = '';
	  	BUI.each(channels,function(channel){
            channelList += channel.id+',';
	    });
        channelList =  channelList.substring(0,channelList.length-1);
	  	select.setSelectedValue('');
    	select.setSelectedValue(channelList);
    };

    crudGrid.on('beforeUpdateShow', beforeUpdateShow);
    
    
    
    var Select = BUI.Select,
    Data = BUI.Data;

  	var store = new Data.Store({
    	url : '${ctx}/backend/channel/primaryList',
    	autoLoad : true
  	}),
  	select = new Select.Select({  
    	render:'#channels',
    	valueField:'#channelIds',
    	multipleSelect : true,
    	store : store
  	});
  	select.render(); 
});

</script>
 
</body>
</html>  
