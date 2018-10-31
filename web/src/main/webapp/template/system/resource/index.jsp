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
     <div class="row">
      <div class="span6">
        <div class="panel">
        <form id = "tree">
          <div class="panel-header clearfix">
            <!--  <h3 class="pull-left">资源</h3> -->
            <div id="tbar" class="pull-right"></div>
          </div>
          <div id="t1">
          </div>
          </form> 
        </div>
      </div>
      <div>
        <div style="overflow: hidden;padding-left: 5px;">
          <div id="grid">
          </div>
        </div>
      </div>
          <!-- 修改新增 -->
   <div id="addOrUpdate" class="hide">
      <form id="addOrUpdateForm" class="form-horizontal">
        <div class="row">
          <div class="control-group span8">
            <label class="control-label"><s>*</s>资源名称</label>
            <div class="controls">
              <input name="name" type="text" data-rules="{required:true}" class="input-normal control-text">
            </div>
          </div>
          <div class="control-group span8">
            <label class="control-label"><s>*</s>排序</label>
            <div class="controls">
              <input name="orderId" type="text" data-rules="{required:true,number:true}" class="input-normal control-text">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="control-group span8">
            <label class="control-label">菜单图标</label>
            <div class="controls">
              <input name="ico" type="text" class="input-normal control-text">
            </div>
            <span style="color:rgb(189, 76, 93)" class="page-action grid-command" data-id="iconfont" data-href="${ctx}/static/iconfont/iconfont.html" title="查看图标">
              <i class="iconfont iconfont-survey" ></i>图标</span>
          </div>
          <div class="control-group span8">
            <label class="control-label">URL</label>
            <div class="controls">
              <input name="url" type="text" class="input-normal control-text">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="control-group span8">
            <label class="control-label">代码</label>
            <div class="controls">
              <input name="code" type="text" data-rules="{required:true}" class="input-normal control-text">
            </div>
          </div>
          <div class="control-group span10">
            <label class="control-label">权限</label>
              <div class="controls" id ="permissionList" name='permissionList'>
              <input name="permissionIds" type="hidden" id="permissionIds" value='' >
            </div>
          </div>
        </div>
        <input type="hidden" name="parent_id" value="">
        <input type="hidden" name="level" value="">
        <input type="hidden" name="id" value="">
      </form>
    </div>
  </div>
  </div>
<script type="text/javascript">
BUI.use(['bui/tree','bui/data','bui/toolbar','bui/select','bui/ux/crudgrid','bui/mask','bui/common/page'],
		function (Tree,Data,Toolbar,Select,CrudGrid,Mask) {
	
	
	//定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/system/resource"/>
	

			var level = 1;//添加资源时需要使用到
			var parentId = 0;
			var enumPermission = {};

			
			//权限选择框
	       	var selectStore = new Data.Store({
	        	url : '${ctx}/system/permission/permissionCheckList',
	        	autoLoad : true
	      	});
 	       	selectStore.on('load',function(e){
	       		var result = selectStore.getResult();
	            BUI.each(result,function(item){
	            	enumPermission[item.value] = item.text;
	              });
	       	}); 
	      	var select = new Select.Select({  
	        	render:'#permissionList',
	        	valueField:'#permissionIds',
	        	multipleSelect : true,
	        	store : selectStore
	      	});
	      	select.render();
			
		     //资源树开始
	        //一次性异步加载所有数据
	        var treeStore = new Data.TreeStore({
	            url : '${ctx}/system/resource/resourceTree',
	            autoLoad : true,
	    	    root : {                  //由于要显示根节点，所以需要配置根节点
	    	       id : '0',
	    	       text : '资源列表',
	    	       expanded : true,
	    	       cls : 'icon-home'
	    	     }
	        });
	        
	    	 //由于这个树，不显示根节点，所以可以不指定根节点
	    	 var tree = new Tree.TreeList({
	    		 //itemTpl : '<li id="{value}">{text}</li>',
	    	     render : '#t1',
	    	     store : treeStore,
	    	     showLine : true,
	    	     height: BUI.viewportHeight()-90,
	    	     dirCls : 'icon-tasks',
	    	     leafCls : 'icon-leaf',
	    	     showLine : true, //显示连接线
	    	     showRoot : true,
	    	     loadMask : new Mask.LoadMask({el : '#t1'})
	    	   });
	    	 tree.render();
	         
	         
	        tree.on('selectedchange',function(ev){
	        	var node = ev.item;
	        	level = node.level+1;
	           	parentId = node.id;
		        if(node.level==0){
		       		store.set('lastParams',{});//清空上次的参数
		        	store.load({search_EQ_level : 1,start : 0});
		            //加载对应的数据，同时将分页回复到第一页
		        }else if(node.level==1 || node.level==2){
		        	store.set('lastParams',{});
		        	store.load({'search_EQ_parent_id' : node.id,start : 0});
		         }else if(node.level==3){
		        	store.set('lastParams',{});
		        	store.load({'search_EQ_id' : node.id,start : 0});
		         }
	        });
	        
	        //树上方按键
	        var bar = new Toolbar.Bar({
	            render : '#tbar',
	            elCls : 'button-group',
	            children : [{
	                elCls : 'button button-small button-primary',
	                content : '<i class="icon-refresh icon-white"></i>刷新',
	                elAttrs:{title : '刷新'},
	                handler : function(){
	                	treeStore.load();
	                }
	              },
	              {
	            	  elCls : 'button button-small button-primary',
	            	  content : '<i class="icon-plus icon-white"></i>展开',
	            	  elAttrs:{title : '展开'},
	            	  handler : function(){
	            		  tree.expandAll();
	            	}
	              },
	              {
	            	  elCls : 'button button-small button-primary',
	            	  content : '<i class="icon-minus icon-white"></i>收缩',
	            	  elAttrs:{title : '收缩'},
	            	  handler : function(){
	            		  tree.collapseAll();
	            	}
	              }
	            ]
	         });
	        bar.render();
	          
	         var columns = [
	            {title : 'ID', dataIndex:'id',width:'5%'},
	            {title : '资源名称',dataIndex :'name',width:'15%'},
	            {title : '代码',dataIndex: 'code', width :'15%'},
	            {title : '连接/显示页面',dataIndex :'url',width:'20%'}, 
	            {title : '导航图标',dataIndex : 'ico',width:'15%'},
	            {title : '级别', dataIndex : 'level', width:'5%'},
	            {title : '排序', dataIndex : 'orderId', width:'5%'},
	            {title : '权限', dataIndex : 'permissionIds', width:'25%',renderer :BUI.Grid.Format.multipleItemsRenderer(enumPermission)},
	            {title : '上级资源', dataIndex : 'parent', withd:100,renderer: function (value) {
	            	if(value){
	            		return value.name;
	            	}else{
	            		return '';
	            	}
	           }}
	          ];
	         
	         
	         
	         var crudGrid = new CrudGrid({
	        	 gridId :'grid',
	         	 entityName : '资源',
	         	storeUrl : '${ctx}/system/resource/list',
	            addUrl : '${ctx}/system/resource/add',
	            updateUrl : '${ctx}/system/resource/update',
	            removeUrl : '${ctx}/system/resource/del',
	            columns : columns,
	            showAddBtn : add,
	            showUpdateBtn : update,
	            showRemoveBtn : del,
	             addOrUpdateFormId : 'addOrUpdateForm',
	             dialogContentId : 'addOrUpdate',
	             autoSearch : false,//不自动查询
	             storeCfg : {
	            	 sortInfo : {
	            		 field : 'orderId',
	            		 direction : 'ASC' //升序ASC，降序DESC
	            	 }
	             }
	         });
	         var store = crudGrid.get('store');
	         
	         
	         //save包括 add\update\remove
	         store.on('saved',function(){
	        	 treeStore.load();}
	         );
	         
	         //根据级别显示或隐藏部分表单
	         var changeFormFieldByLevel = function(form,level){
	        	 if(level == 1){
	        		 form.getField('url').enable();
	        		 form.getField('ico').enable();
	        		 form.getField('code').enable();
	        		 select.disable();
	        	 }else if(level == 2){
	        		 form.getField('url').disable();
	        		 //form.getField('ico').disable();
	        		 form.getField('code').disable();
	        		 select.disable();
	        	 }else if(level == 3){
	        		 form.getField('url').enable();
	        		 form.getField('ico').disable();
	        		 form.getField('code').enable();
	        		 select.enable();
	        	 }
	         }
	         
	         var addAdnUpdateForm = crudGrid.get('addOrUpdateForm');
	         var beforeAddShow = function(dialog,form){
	        	 	form.getField('level').set('value',level);
	      		 if(parentId!=0){
	      			form.getField('parent._d').set('value',parentId);
	      		 }else{
	      			form.getField('parent_id').set('value','');
	      		 }
	      		select.setSelectedValue('permissionIds');
	      		changeFormFieldByLevel(form,level);
	         };
	         crudGrid.on('beforeAddShow', beforeAddShow);
	   
	         var beforeUpdateShow = function(dialog,form,record){
 	         	if(record.parent && record.parent.id){
	         		form.getField('parent_id').set('value',record.parent.id);
	         	} 
 	         	form.getField('level').set('value',record.level);
	         	//貌似不清空数据重新加载就会有问题
	         	select.setSelectedValue('');
	         	select.setSelectedValue(record.permissionIds);
	         	changeFormFieldByLevel(form,record.level);
	         	
	         };

	         crudGrid.on('beforeUpdateShow', beforeUpdateShow);
});
 
</script>
 
</body>
</html>
