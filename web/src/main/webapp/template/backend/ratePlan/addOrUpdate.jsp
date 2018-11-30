<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="../../common/import-tags.jsp" %>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="../../common/import-static.jsp" %>
</head>
<body>
<div class="panel">
    <div class="panel-header">
        <h2>
            基本参数
        </h2>
    </div>
    <div class="panel-body">
        <div class="container">
            <!-- 修改新增 -->
            <form id="addOrUpdateForm" class="form-horizontal">
                <div class="row">
                    <div class="control-group span10">
                        <label class="control-label"><s>*</s>方案名称:</label>
                        <div class="controls">
                            <input name="name" type="text"
                                   data-rules="{required:true,}"
                                   class="input-normal control-text">
                        </div>
                    </div>
                    <div class="control-group span10">
                        <label class="control-label"><s>*</s>产品:</label>
                        <div class="controls"  id ="products" >
                            <input name="productId"  id="productId" type="hidden"
                                   data-rules="{required:true}"
                                   class="input-normal control-text">
                        </div>
                    </div>
                    <div class="control-group span10">
                        <label class="control-label"><s>*</s>资金方:</label>
                        <div class="controls" id ="funds" >
                            <input name="fundId" id="fundId" type="hidden"
                                   data-rules="{required:true}"
                                   class="input-normal">
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="control-group span10">
                        <label class="control-label">支持期数</label>
                        <div class="controls bui-form-field-select" data-items="{'3':'3期','6':'6期','12':'12期','24':'24期','36':'36期'}"
                             data-select="{multipleSelect:true}" class="control-text input-normal">
                            <input name="terms" type="hidden" value="">
                        </div>
                    </div>
                    <div class="control-group span10">
                        <label class="control-label">是否有效</label>
                        <div class="controls bui-form-field-select" data-items="{'1':'启用','0':'停用'}"
                             class="control-text input-small">
                            <input name="enable" type="hidden" value="">
                        </div>
                    </div>
                </div>
                <div id="weightGrid">

                </div>
                <!--
                <div class="row" style="height: 120px;">
                    <label class="control-label">支持期数</label>
                    <div class="controls" id="list"></div>
                </div>-->
                 <input type="hidden" name="id" value="${id}">
            </form>
        </div>
    </div>
    <div class="panel-header">
        <h2>
            科目设置
            <button class="button button-info" id="addRatePlanDetail">添加科目</button>
        </h2>

    </div>
    <div class="panel-body" id="ratePlanDetailGrids">
        <div id="2Grid">

        </div>
    </div>
    <div id="subjectDialog" class="hide">
        <form id="subjectForm" class="form-horizontal">
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">请选择科目：</label>
                    <div class="controls bui-form-field-select" data-items="{'10':'审批费','11':'担保费','12':'居间人服务费'}"
                         class="control-text input-normal">
                        <input name="subjectId" type="hidden" value="">
                    </div>
                </div>
            </div>
        </form>
    </div>

    <div class="form-actions offset3" style="margin-bottom: 20px">
        <button type="button" id="submit" class="button button-primary">确认</button>
    </div>
</div>
<script type="text/javascript">

    BUI.use(['bui/ux/crudgrid', 'bui/form', 'bui/grid', 'bui/data', 'bui/overlay', 'bui/list'],
        function (CrudGrid, Form, Grid, Data, Overlay, List) {

            var fundStore = new Data.Store({
                    url : '${ctx}/backend/fund/items',
                    autoLoad : true
                }),
                fundSelect = new BUI.Select.Select({
                    render:'#funds',
                    valueField:'#fundId',
                    multipleSelect : false,
                    store : fundStore
                });
            fundSelect.render();

            var productStore = new Data.Store({
                    url : '${ctx}/backend/product/items',
                    autoLoad : true
                }),
                productSelect = new BUI.Select.Select({
                    render:'#products',
                    valueField:'#productId',
                    multipleSelect : false,
                    store : productStore
                });
            productSelect.render();

            var subjectForm = new Form.Form({
                srcNode: '#subjectForm'
            });
            subjectForm.render();

            var addOrUpdateForm = new Form.Form({
                srcNode: '#addOrUpdateForm'
            }).render();

            var paymentTypeEnum = {'1': '等额本息', '2': '等本等息', '3': '等额本金'};

            var feeTypeEnum = {'1': '固定金额', '2': '固定利率'};

            var paymentTimeTypeEnum = {'1': '首个还款日收', '2': '按月收', '3': '放款时收', '4': '定期收取'};

            var feeBaseTypeEnum = {'1': '剩余本金', '2': '放款本金'};

            var subjectIdEnum = {'10': '审批费', '11': '担保费', '12': '居间人服务费'}

            //期数多选框
            var termsSelect = addOrUpdateForm.getField('terms');

            //科目的所有Grid都放这个数组里
            var subjectGridList = [];

            //期数多选框添加时间，选择后自动添加或者删除下面的列表的行
            termsSelect.on('change', function (e) {
                var terms = [];
                if (addOrUpdateForm.getFieldValue('terms')) {
                    terms = addOrUpdateForm.getFieldValue('terms').split(',');
                }
                BUI.each(subjectGridList, function (g) {
                    var store = g.get('store');
                    changeGridDataBySelectTerm(store, terms);
                });

            });


            //根据选择的期数修改列表中的行
            var changeGridDataBySelectTerm = function (store, terms) {
                var data = store.getResult();
                //判断选择的期数是否在列表中，如果不在则添加var
                BUI.each(terms, function (term) {
                    var flag = false;
                    BUI.each(data, function (item) {
                        if (item.term == term) {
                            flag = true;
                        }
                    });
                    if (!flag) {
                        store.add({'term': term,'weight':0});
                    }
                });

                for (var i = 0; i < data.length; i++) {
                    if (terms.indexOf(data[i].term) == -1) {
                        store.remove(data[i]);
                    }
                }
            }

            //利息表格
            var weightColumns = [
                {title: '期数', dataIndex: 'term'},
                {
                    title: '权重',
                    dataIndex: 'weight',
                    width: '80%',
                    editor: {id: 'weight', xtype: 'number', rules: {required: true}}
                }
            ];


            var weightData = [];

            var weightStore = new Data.Store({
                data: weightData,
                autoLoad: true
            })

            var weightEditing = new Grid.Plugins.RowEditing();

            var weightGrid = new Grid.Grid({
                render: '#weightGrid',
                columns: weightColumns,
                width: '200px',
                //forceFit: true,
                plugins: [weightEditing],
                store: weightStore
            });
            weightGrid.render();

            subjectGridList.push(weightGrid);


            //利息表格
            var rateColumns = [
                {title: '期数', dataIndex: 'term'},
                {
                    title: '计息方式',
                    dataIndex: 'paymentType',
                    width: '20%',
                    editor: {id: 'paymentTypeSelect', xtype: 'select', items: paymentTypeEnum, rules: {required: true}},
                    renderer: Grid.Format.enumRenderer(paymentTypeEnum)
                },
                {
                    title: '收费类型',
                    dataIndex: 'calculationMode',
                    width: '20%',
                    editor: {id: 'feeTypeSelect', xtype: 'select', items: feeTypeEnum, rules: {required: true}},
                    renderer: Grid.Format.enumRenderer(feeTypeEnum)
                },
                {
                    title: '收费内容（%/元）',
                    dataIndex: 'fee',
                    width: '20%',
                    editor: {xtype: 'number', rules: {required: true}}
                },
                {
                    title: '收取时点',
                    dataIndex: 'paymentTimeType',
                    width: '20%',
                    editor: {
                        id: 'paymentTimeTypeSelect',
                        xtype: 'select',
                        items: paymentTimeTypeEnum,
                        rules: {required: true}
                    },
                    renderer: Grid.Format.enumRenderer(paymentTimeTypeEnum)
                }
            ];


            var rateData = [];

            var rateStore = new Data.Store({
                data: rateData,
                autoLoad: true
            })

            var editing = new Grid.Plugins.RowEditing();

            var rateGrid = new Grid.Grid({
                render: '#2Grid',
                columns: rateColumns,
                width: '90%',
                //forceFit: true,
                plugins: [editing],
                store: rateStore,
                tbar: {
                    // items:工具栏的项， 可以是按钮(bar-item-button)、 文本(bar-item-text)、 默认(bar-item)、 分隔符(bar-item-separator)以及自定义项
                    items: [{
                        xclass: 'bar-item-text',
                        text: '<h3>科目名称：利息&emsp;&emsp;&emsp;&emsp;&emsp;费用收取方：资金方</h3>'
                    }
                    ]
                }
            });
            rateGrid.render();

            subjectGridList.push(rateGrid);

            /*********新增科目相关代码*******/
                //科目选择框
            var dialog = new Overlay.Dialog({
                    title: '请选择要添加的科目',
                    contentId: 'subjectDialog',
                    success: function () {
                        var subjectId = subjectForm.getFieldValue('subjectId');
                        //BUI.Message.Alert(subjectId,'warning');
                        var a = addSubjectGrid(subjectId);
                        if (!a) {
                            BUI.Message.Alert('不能重复添加科目！', 'warning');
                        } else {
                            this.close();
                        }

                    }
                });

            $('#addRatePlanDetail').on('click', function () {
                dialog.show();
            });

            var delSubjectGrid = function (gridId) {
                // 弹出提示
                BUI.Message.Confirm('确认要删除对应科目吗？',function(){
                    for (var i = 0; i < subjectGridList.length; i++) {
                        var grid = subjectGridList[i];
                        var render = grid.get('render');
                        if (render == '#' + gridId+"Grid") {
                            subjectGridList.splice(i, 1);
                            $('#' + gridId+"Grid").remove();
                            return false;
                        }
                    }

                },'question');



            }


            //增加一个科目的列表
            var addSubjectGrid = function (subjectId) {

                var isAdded = false;
                BUI.each(subjectGridList, function (grid) {
                    var render = grid.get('render');
                    if (render == '#' + subjectId + 'Grid') {
                        isAdded = true;
                        return false;
                    }
                });

                if (isAdded) {
                    return false;
                }

                var subjectName = subjectIdEnum[subjectId];

                $('#ratePlanDetailGrids').append('<div id="' + subjectId + 'Grid"></div>');

                var data = [];

                //获取已经选中的期数，添加进去
                var terms = [];


                var store = new Data.Store({
                    data: data,
                    autoLoad: true
                })

                if (addOrUpdateForm.getFieldValue('terms')) {
                    terms = addOrUpdateForm.getFieldValue('terms').split(',');
                    BUI.each(terms, function (term) {
                        store.add({'term': term});
                    })

                }

                var subjectColumns = [
                    {title: '期数', dataIndex: 'term'},
                    {
                        title: '收费类型',
                        dataIndex: 'calculationMode',
                        width: '20%',
                        editor: {id: 'feeTypeSelect', xtype: 'select', items: feeTypeEnum, rules: {required: true}},
                        renderer: Grid.Format.enumRenderer(feeTypeEnum)
                    },
                    {
                        title: '收费内容（%/元）',
                        dataIndex: 'fee',
                        width: '20%',
                        editor: {xtype: 'number', rules: {required: true}}
                    },
                    {
                        title: '计费基数',
                        dataIndex: 'feeBaseType',
                        width: '20%',
                        editor: {
                            id: 'feeBaseTypeSelect',
                            xtype: 'select',
                            items: feeBaseTypeEnum,
                            rules: {required: true}
                        },
                        renderer: Grid.Format.enumRenderer(feeBaseTypeEnum)
                    },
                    {
                        title: '收取时点',
                        dataIndex: 'paymentTimeType',
                        width: '20%',
                        editor: {
                            id: 'paymentTimeTypeSelect',
                            xtype: 'select',
                            items: paymentTimeTypeEnum,
                            rules: {required: true}
                        },
                        renderer: Grid.Format.enumRenderer(paymentTimeTypeEnum)
                    },
                    {
                        title: '收费时间点',
                        dataIndex: 'paymentTime',
                        width: '15%',
                        editor: {xtype: 'text'}
                    },

                ];


                var editing1 = new Grid.Plugins.RowEditing();
                var grid = new Grid.Grid({
                    render: '#' + subjectId + 'Grid',
                    columns: subjectColumns,
                    width: '90%',
                    plugins: [editing1],
                    store: store,
                    tbar: {
                        // items:工具栏的项， 可以是按钮(bar-item-button)、 文本(bar-item-text)、 默认(bar-item)、 分隔符(bar-item-separator)以及自定义项
                        items: [{
                            xclass: 'bar-item-text',
                            text: '<h3>科目名称：' + subjectName + '</h3>'
                        }, {
                            xclass: 'bar-item-button',
                            btnCls: 'button button-primary js-DelSubjectGrid js-subject-' + subjectId+"-gridId",
                            text: '删除'
                        }
                        ]
                    }
                });
                grid.render();
                subjectGridList.push(grid);

                //绑定删除按钮的时间
                $('button.js-DelSubjectGrid').unbind();

                $('button.js-DelSubjectGrid').click(function () {
                    var clazz = $(this).attr('class');
                    var gridId = clazz.match(/js-subject-(.*)-gridId/);
                    if (gridId && gridId.length >= 2) {
                        delSubjectGrid(gridId[1]);
                    }
                });
                return true;
            }


            var sumbit = function () {
                addOrUpdateForm.valid();
                if (addOrUpdateForm.isValid()) {
                    var param = addOrUpdateForm.serializeToObject();

                    BUI.each(subjectGridList, function (grid) {
                        var key = grid.get('render').replace("#", "").replace("Grid", "");
                        var result = grid.get('store').getResult();
                        if (key == "weight") {
                            param.weight = result;
                        } else {
                            // 费率数据
                            if (!param.hasOwnProperty("fee")) {
                                param.fee = {};
                            }
                            param.fee[key] = result;
                        }
                    });
                    // 转换json，按照期数重新分类
                    $.each(param.weight, function (key, value) {
                        var term = value.term;
                        var result = {};
                        result.subject = [];
                        // 循环科目
                        $.each(param.fee, function (key, value) {
                            //循环期数
                            $.each(value, function (index, value) {
                                if (value.hasOwnProperty("term")
                                    && value.term == term) {
                                    // 判断和期数相同
                                    var item = value;
                                    // 回写科目类型
                                    item.subjectId = key;
                                    result.subject.push(item);
                                }
                            });
                        });
                        // 回写封装
                        if (!param.hasOwnProperty("feePlan")) {
                            param.feePlan = [];
                        }
                        result.term = term;
                        param.feePlan.push(result);
                    });
                    // 删除原来数据域
                    delete param.fee;
                    // 输出检查
                    console.log(param);
                    var url;
                    if (param.id == "" || param.id == -1) {
                        url = "${ctx}/backend/ratePlan/add";
                    } else {
                        url = "${ctx}/backend/ratePlan/update"
                    }
                    //提交数据
                    $.ajax({
                        url: url,
                        type: "POST",
                        dataType: "json",
                        data: JSON.stringify(param),
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader("content-type", "application/json")
                        },
                        success: function (result) {
                            if(result.success){
                                BUI.Message.Alert('操作成功！',function(){
                                    top.topManager.closePage();
                                    top.topManager.reloadPage('/backend/ratePlan');
                                },'success');
                            }
                        }
                    })
                }
            };

            $('#submit').click(function () {
                sumbit();
            });

            // 获取
            getRatePlan = function getRatePlan(id) {
                if (id != -1) {
                    $.post("${ctx}/backend/ratePlan/getRatePlan", {id:id}, function (ratePlan) {
                        console.log(ratePlan);
                        addOrUpdateForm.setFieldValue("name",ratePlan.name);
                        addOrUpdateForm.setFieldValue("enable",ratePlan.enable);
                        // addOrUpdateForm.setFieldValue("productId",ratePlan.productId);
                        // addOrUpdateForm.setFieldValue("fundId",ratePlan.fundId);
                        productSelect.setSelectedValue(ratePlan.productId);
                        fundSelect.setSelectedValue(ratePlan.fundId);
                        var terms = "";
                        var weightData = [];
                        $.each(ratePlan.ratePlanTerms,function(key,value){
                            if(terms!=""){
                                terms = terms + ",";
                            }
                            terms = terms+value.term;
                            weightData.push({
                                id:value.id,
                                term:value.term.toString(),
                                weight:value.weight
                            });
                        });
                        //回写权重
                        addOrUpdateForm.setFieldValue("terms",terms);
                        weightStore.setResult(weightData);
                        //转换科目
                        var subjectMap = {}
                        $.each(ratePlan.ratePlanTerms,function(key,term){
                            $.each(term.ratePlanDetails,function(key,value){
                                value.term = term.term.toString();
                                var subject = subjectMap[value.subjectId];
                                if(subject==null){
                                    subjectMap[value.subjectId]=new Array(value);
                                }else{
                                    subject.push(value);
                                    subjectMap[value.subjectId]=subject;
                                }
                            });
                        });
                        $.each(subjectMap,function(subjectId,subject){
                            var exist = false;
                            $.each(subjectGridList,function(key,value){
                                if(value.get("render")=="#"+subjectId+"Grid"){
                                    // 存在对应Grid
                                    console.log(value);
                                    exist = true;
                                    value.get("store").setResult(subject);
                                }
                            });
                            if(!exist){
                                // 不存在Grid
                                addSubjectGrid(subjectId);
                                $.each(subjectGridList,function(key,value){
                                    if(value.get("render")=="#"+subjectId+"Grid"){
                                        // 存在对应Grid
                                        console.log(value);
                                        exist = true;
                                        value.get("store").setResult(subject);
                                    }
                                });
                            }
                        });
                    });
                }
            }

            getRatePlan(addOrUpdateForm.getFieldValue("id"));

        });




</script>
</body>
</html>