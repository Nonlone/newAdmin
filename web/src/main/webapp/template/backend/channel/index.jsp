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
			<label class="control-label">应用名称:</label>
			<div class="controls bui-form-field-select height_auto"  data-items="{' ':'全部','借呀':'借呀'}" class="control-text input-small">
				<input name="search_EQ_appName" type="hidden" >
			</div>
			<div class="control-group span7">
				<label class="control-label">一级渠道标识:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_mainPackageCode">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">二级渠道标识:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_channelId">
				</div>
			</div>
			<label class="control-label">渠道大类:</label>
			<div class="controls  height_auto"   class="control-text input-small">
				<div id="channelSortSelect" class="controls">
				<input id="channelSortSearch"  name="search_EQ_channelSort" type="hidden" >
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">一级渠道名称:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_mainPackgage">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">二级渠道名称:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_subPackage">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">开发主体:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_devBody">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">渠道终端:</label>
				<div class="controls bui-form-field-select height_auto"  data-items="{' ':'全部','APP':'APP','H5':'H5','PC':'PC'}" class="control-text input-small">
					<input name="search_EQ_channelTerminal" type="hidden" >
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
				<div class="control-group span10">
					<label class="control-label"><s>*</s>一级渠道</label>
					 <div class="controls" id ="primaryList" name='primaryList' onchange="findPrimary();" data-rules="{required:true}">
						
					</div>
				</div>
				
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>应用名称:</label>
					<div class="controls bui-form-field-select" data-items="{'借呀':'借呀'}"
						 class="control-text input-small">
						<input name="appName" type="hidden" data-rules="{required:true}">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>渠道大类:</label>
					<div class="controls">
						<input id="channelSort" name="channelSort" type="text"
							   data-rules="{required:true,}"
							   readonly="true"
							   class="input-normal control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>二级渠道名称:</label>
					<div class="controls">
                        <input id="subPackage" name="subPackage" type="text" data-rules="{required:true}"  data-remote="${ctx}/backend/channel/checkChannelName">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>二级渠道标识:</label>
					<div class="controls">
						<input id="primaryCode" name="primaryCode" class="input-minimum" readonly="true" data-rules="{required:false,}" type="text">
						<input id="channelId" name="channelId" type="text"
							data-remote="${ctx}/backend/channel/checkChannelId" data-rules="{required:true,channel:true}"
							class="input-minimum control-text">
					</div>
				</div>

			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>开发主体:</label>
					<div class="controls">
						<input name="devBody" type="text"
							   data-rules="{required:true,}"
							   class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>渠道终端:</label>
					<div class="controls bui-form-field-select" data-items="{'APP':'APP','H5':'H5','PC':'PC'}"
						 class="control-text input-small">
						<input name="channelTerminal" type="hidden" value="">
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

    function findPrimary() {

        var primaryName=$("#primaryList div input").val();
        if(primaryName==''){
        	return;
        }
        console.log(primaryName);
        var channelIdValue,channelSortValue,code;
        if(primaryName!=null||primaryName!=""){
            $.ajax({
                url: "${ctx}/backend/channel/getPrimaryChannel/"+primaryName,
                type:"GET",
                async:false,
                success:function (data) {
                    if(data!=null){
                        code = data.code;
                        if(code){
                            code = code+"_";
						}
                        channelSortValue = data.sort;
                    }
                }
            });
            if(code){
                document.getElementById("primaryCode").value = code;
                channelIdValue = document.getElementById("channelId").value;
                if(channelIdValue){
                    document.getElementById("channelId").value = channelIdValue.replace(code,"");
                }
			}else {
                document.getElementById("primaryCode").value = "";
			}

			if(channelSortValue){
                document.getElementById("channelSort").value = channelSortValue;
            }


        }

    }

    function flushall(){
        var elementsByTagName = document.getElementsByTagName("input");
        for(var i = 0;i<elementsByTagName.length;i++){
            elementsByTagName[i].innerText = "";
        }
    }
 

BUI.use(['bui/ux/crudgrid','bui/select','bui/data','bui/form'],function (CrudGrid,Select,Data,Form) {


    Form.Rules.add({
        name: 'channel',
        msg: '不允许包含非英文或数字的标识',
        validator: function (value, baseValue, formatMsg) {
            var regexp = new RegExp(/^[0-9a-zA-Z]+$/g)
            if (!regexp.test(value)) {
                return formatMsg
            }
        }
    });

	//定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/channel"/>
		
	
    //一级渠道选择框
    	var selectStore = new Data.Store({
        url : '${ctx}/backend/channel/primaryList',
        autoLoad : true
    });
        var primayChanneStr='${primaryChannelList}';
        var dataItems=primayChanneStr.split(",");
    	var select = new Select.Suggest({
    		render:'#primaryList',
            name:'mainPackgage',
           data:dataItems
    	  });
    	select.render();


    
    
    var channelSortSelect = new Select.Select({
        render:'#channelSortSelect',
        valueField:'#channelSortSearch',
        items:JSON.parse('${channelSortList}')
    });
    channelSortSelect.render();
  
    var columns = [
		 {title:'应用名称',dataIndex:'appName',width:'8%'},
		 {title:'一级渠道名称',dataIndex:'mainPackgage',width:'15%'},
		 {title:'二级渠道名称',dataIndex:'subPackage',width:'15%'},
		 {title:'一级渠道标识',dataIndex:'mainPackageCode',width:'8%'},
		 {title:'二级渠道标识',dataIndex:'channelId',width:'8%'},
		 {title:'渠道大类',dataIndex:'channelSort',width:'10%'},
		 {title:'渠道终端',dataIndex:'channelTerminal',width:'10%'},
		 {title:'开发主体',dataIndex:'devBody',width:'10%'},		 
        ];
    
	var crudGrid = new CrudGrid({
		entityName : '二级渠道',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/backend/channel/list',
        addUrl : '${ctx}/backend/channel/add',
        updateUrl : '${ctx}/backend/channel/update',
        removeUrl : '${ctx}/backend/channel/del',
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
				field : 'createdTime',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
				}
			}
		});


    var addOrUpdateForm = crudGrid.get('addOrUpdateForm');


    var subPackage = addOrUpdateForm.getField("subPackage");
    var channelId = addOrUpdateForm.getField("channelId");
    // if(mainPackageValue==""||mainPackageValue==null){
    //     mainPackageValue = "null";
    // }

	channelId.on('remotestart',function (ev) {
	   // debugger;
		var data = ev.data;
		data.primaryCode = document.getElementById("primaryCode").value;
    })

	subPackage.on('remotestart',function(ev){
        var data = ev.data;
        data.mainPackage = $("#primaryList div input").val();
    });




    var update = true;

    addOrUpdateForm.getField('subPackage').on('remotecomplete',function(){
        if(update){
            addOrUpdateForm.getField('subPackage').clearErrors();
        }
    });

    addOrUpdateForm.getField('channelId').on('remotecomplete',function(){
        if(update){
            addOrUpdateForm.getField('channelId').clearErrors();
        }
    });

    addOrUpdateForm.getField('channelSort').on('remotecomplete',function(){
        if(update){
            addOrUpdateForm.getField('channelSort').clearErrors();
        }
    });


    addOrUpdateForm.getField('mainPackgage').on('remotecomplete',function(){
        if(update){
            addOrUpdateForm.getField('mainPackgage').clearErrors();
        }
    });

    var beforeAddShow = function(dialog,form){
        form.getField("channelSort").disable();
        form.getField("primaryCode").disable();
        select.enable();
        update = false;
    };
    crudGrid.on('beforeAddShow', beforeAddShow);


    var beforeUpdateShow = function(dialog,form,record){
        update = true;
        findPrimary();
        select.disable();
        form.getField("channelId").disable();
        form.getField("channelSort").disable();
        form.getField("primaryCode").disable();
    };

    crudGrid.on('beforeUpdateShow', beforeUpdateShow);


});

</script>
 
</body>
</html>


