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
				<label class="control-label">渠道标识:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="search_LIKE_channelId">
				</div>
			</div>
			<label class="control-label">渠道大类:</label>
			<div class="controls bui-form-field-select height_auto"  data-items="{' ':'全部','A-线上场景':'A-线上场景','A-应用商店':'A-应用商店','A-微信推广':'A-微信推广','A-品牌推广':'A-品牌推广','A-贷款超市':'A-贷款超市','A-数据营销':'A-数据营销','B-线下渠道':'B-线下渠道','B-O2O':'B-O2O','C-其他':'C-其他'}" class="control-text input-small">
				<input name="search_EQ_channelSort" type="hidden" >
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
					<div class="controls" id ="primaryList" name='primaryList'>
						<input name="mainPackgage" type="hidden" id="mainPackgage" onchange="findPrimary(this.value);">
					</div>
				</div>
				<div class="row">
					<span style='color:#ffbc00'>请先选择一级渠道!自动抉择渠道标识前缀与渠道大类</span>
				</div>
			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>应用名称:</label>
					<div class="controls bui-form-field-select" data-items="{'借呀':'借呀'}"
						 class="control-text input-small">
						<input name="appName" type="hidden" value="">
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
                        <input id="subPackage" name="subPackage" type="text" data-rules="{required:true}" class="input-normal control-text">
					</div>
				</div>
				<div class="control-group span8">
					<label class="control-label"><s>*</s>渠道标识:</label>
					<div class="controls">
						<input id="primaryCode" name="primaryCode" class="input-minimum" readonly="true" data-rules="{required:false,}" type="text">
						<input id="channelId" name="channelId" type="text"
							data-rules="{required:true,}"
							class="input-minimum control-text">
					</div>
				</div>

			</div>
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>开发主体:</label>
					<div class="controls">
						<input name="devBody" type="text"
							   data-rules="{required:false,}"
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

    function findPrimary(primaryName) {

        document.getElementById("mainPackgage").value = primaryName;
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
	
	//定义页面权限
	var add=false,update=false,del=false,list=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/backend/channel"/>

    //一级渠道选择框
    var selectStore = new Data.Store({
        url : '${ctx}/backend/channel/primaryList',
        autoLoad : true
    });

    select = new Select.Select({
        render:'#primaryList',
        valueField:'#mainPackgage',
        store:selectStore
    });
    select.render();

    var columns = [
		 {title:'应用名称',dataIndex:'appName',width:'10%'},
		 {title:'渠道标识',dataIndex:'channelId',width:'15%'},
		 {title:'渠道大类',dataIndex:'channelSort',width:'10%'},
		 {title:'一级渠道名称',dataIndex:'mainPackgage',width:'15%'},
		 {title:'二级渠道名称',dataIndex:'subPackage',width:'15%'},
		 {title:'开发主体',dataIndex:'devBody',width:'10%'},
		 {title:'渠道终端',dataIndex:'channelTerminal',width:'10%'},
        ];
    
	var crudGrid = new CrudGrid({
		entityName : 'Channel',
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
				field : 'id',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
				}
			}
		});


    var addOrUpdateForm = crudGrid.get('addOrUpdateForm');


    var subPackage = addOrUpdateForm.getField("subPackage");
    var mainPackgage = addOrUpdateForm.getField("mainPackgage");
    var mainPackageValue = document.getElementById("mainPackgage").value;
    if(mainPackageValue==""||mainPackageValue==null){
        mainPackageValue = "null";
	}
    var subPackageValue = document.getElementById("subPackage").value;
    var url = "${ctx}/backend/channel/checkChannelName/"+mainPackageValue;

    mainPackgage.on('change',function(){
        mainPackageValue = document.getElementById("mainPackgage").value;
        url = "${ctx}/backend/channel/checkChannelName/"+mainPackageValue;
        subPackage.set('remote',{
            url:url,
            type:"POST",
            data:{"subPackage":subPackageValue},
            dataType:'json',//默认为字符串
            callback : function(data){
                return data;
            }
        });
    });

	subPackage.on('change',function(){
        subPackageValue = document.getElementById("subPackage").value;
        subPackage.set('remote',{
            url:url,
            type:"POST",
            data:{"subPackage":subPackageValue},
            dataType:'json',//默认为字符串
            callback : function(data){
                return data;
            }
        });
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
        update = false;
    };
    crudGrid.on('beforeAddShow', beforeAddShow);


    var beforeUpdateShow = function(dialog,form,record){
        update = true;
        select.setSelectedValue('');
        select.setSelectedValue(record.mainPackgage);
    };

    crudGrid.on('beforeUpdateShow', beforeUpdateShow);


});

</script>
 
</body>
</html>


