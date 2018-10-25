<!DOCTYPE HTML>
 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
 <head>
  <title>${sessionScope.logon_user.systemName}</title>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <%@include file="../../common/import-tags.jsp"%>
   <%@include file="../../common/import-static.jsp"%>
 </head>
 <body >

  
  <div class="container">
 <!-- 查询 -->
    <form id="searchForm" class="form-horizontal search-form">
      <div class="row">
        <div class="control-group span8">
          <label class="control-label">分类名称：</label>
          <div class="controls">
            <input type="text" class="control-text input-small" name="search_LIKE_name">
          </div>
        </div>
        <div class="control-group span8">
          <label class="control-label">分类代码：</label>
          <div class="controls">
            <input type="text" class="control-text input-small" name="search_LIKE_code">
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
      <form id="addOrUpdateForm" class="form-inline">
        <div class="row">
          <div class="control-group span8">
            <label class="control-label"><s>*</s>分类名称:</label>
            <div class="controls">
              <input name="name" type="text" data-rules="{required:true}" class="input-normal control-text">
            </div>
          </div>
          </div>
          <div class="row">
          <div class="control-group span8">
            <label class="control-label"><s>*</s>分类代码:</label>
            <div class="controls">
              <input name="code" type="text" data-rules="{required:true}" class="input-normal control-text">
            </div>
          </div>
        </div>
         <div class="row">
           <div class="control-group span8">
            <label class="control-label"><s>*</s>备注</label>
            <div class="controls control-row-auto">
              <textarea  name="memo" ></textarea>
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
	
	//定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/system/dictionaryType"/>
	
	
    var columns = [
          {title:'ID',dataIndex:'id',width:'5%'},
          {title:'分类名称',dataIndex:'name',width:'20%'},
          {title:'分类代码',dataIndex:'code',width:'20%'},
          {title:'备注',dataIndex:'memo',width:'30%'}
        ];
    
    var crudGrid = new CrudGrid({
    	entityName : '数据字典分类',
    	storeUrl : '${ctx}/system/dictionaryType/list',
        addUrl : '${ctx}/system/dictionaryType/add',
        updateUrl : '${ctx}/system/dictionaryType/update',
        removeUrl : '${ctx}/system/dictionaryType/del',
        columns : columns,
        showAddBtn : add,
        showUpdateBtn : update,
        showRemoveBtn : del,
        addOrUpdateFormId : 'addOrUpdateForm',
        dialogContentId : 'addOrUpdate'
    });
});
</script>
 
</body>
</html>  
