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
<div class="container">
    <!-- 查询 -->
    <form id="searchForm" class="form-horizontal search-form">
        <div class="row">
            <div class="control-group span7">
                <label class="control-label" style="width: 94px">大数资金方编号:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_fundCode">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">名称:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_fundName">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">收款银行:</label>
                <div class="controls">
                    <input type="text" class="input-normal control-text" name="search_LIKE_bank">
                </div>
            </div>
            <div class="control-group span7">
                <label class="control-label">是否启用:</label>
                <div class="controls">
                    <select name="search_EQ_enable">
                        <option value="" selected="selected">全部</option>
                        <option value="1">已启用</option>
                        <option value="0">已停用</option>
                    </select>
                </div>
            </div>
            <div class="control-group span12">
                <label class="control-label">创建时间:</label>
                <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                    <!-- search_GTE_createdTime_D 后面的D表示数据类型是Date -->
                    <input type="text" class="calendar-time calendar" name="search_GTE_createdTime" data-tip="{text : '开始日期'}">
                    <span>- </span>
                    <input name="search_LTE_createdTime" type="text" class="calendar-time calendar" data-tip="{text : '结束日期'}">
                </div>
            </div>
            <div class="span3 offset1">
                <button type="button" id="btnSearch" class="button button-primary">搜索</button>
            </div>
        </div>
    </form>
    <!-- 修改新增 -->
    <div id="addOrUpdate" class="hide">
        <form id="addOrUpdateForm" class="form-inline">
            <div class="row">
                <div class="control-group span13">
                    <label class="control-label"><s>*</s>大数资金方编号:</label>
                    <div class="controls">
                        <input name="code" type="text"
                               data-rules="{required:true}"
                               class="input-large control-text" placeholder="请输入总部提供的资金方编号">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span13">
                    <label class="control-label"><s>*</s>大数资金方代码:</label>
                    <div class="controls">
                        <input id="number_text" name="fundCode" type="text"
                               data-rules="{required:true,nid:true,sameId:true}"
                               class="input-large control-text" placeholder="输入资金方大写首拼,如：JD">
                    </div>
                </div>
            </div>

            <div class="row">

                <div class="control-group span9">
                    <label class="control-label"><s>*</s>资金方状态:</label>
                    <div id="s1" class="controls bui-form-field-select">
                        <select name="enable">
                            <option value="1" selected="selected">已启用</option>
                            <option value="0">已停用</option>
                        </select>
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>名称:</label>
                    <div class="controls">
                        <input id="fundName_text" name="fundName" type="text"
                               data-rules="{required:true,nameId:true}"
                               class="input-normal control-text" placeholder="输入资金方名称">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">收款银行:</label>
                    <div class="controls">
                        <input name="bank" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text" placeholder="输入收款银行">
                    </div>
                </div>
                <div class="control-group span9">
                    <label class="control-label">收款账户:</label>
                    <div class="controls">
                        <input name="bankAccount" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text" placeholder="输入收款账号">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">收款银行编码:</label>
                    <div class="controls">
                        <input name="bankCode" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text" placeholder="输入收款银行编码">
                    </div>
                </div>
                <div class="control-group span9">
                    <label class="control-label">描述:</label>
                    <textarea name="description" class="control-row1  span4" placeholder="输入资金方描述"></textarea>
                </div>
            </div>
            <input type="hidden" name="id" value="">
        </form>
    </div>


    <%-- 这里获取选中的id赋值 --%>
    <input type="hidden" name="selectId" id="selectId">
    <input type="hidden" name="amountRecord" id="amountRecord">

    <!-- 查看框-->
    <div id="searchInfoFormDiv" class="hide">
        <form id="searchInfoForm" class="form-inline">
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>系统编号:</label>
                    <div class="controls">
                        <input name="code" type="text"
                               data-rules="{required:true}"
                               class="input-normal control-text" placeholder="请输入总部提供的资金方编号">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>大数资金编号:</label>
                    <div class="controls">
                        <input name="fundCode" type="text"
                               data-rules="{required:true}"
                               class="input-normal control-text" placeholder="请输入总部提供的资金方编号">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label"><s>*</s>名称:</label>
                    <div class="controls">
                        <input name="fundName" type="text"
                               data-rules="{required:true}"
                               class="input-normal control-text" placeholder="请输入资金方名称">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">收款银行:</label>
                    <div class="controls">
                        <input name="bank" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text" placeholder="输入收款银行，选填">
                    </div>
                </div>
                <div class="control-group span9">
                    <label class="control-label">收款账户:</label>
                    <div class="controls">
                        <input name="bankAccount" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text" placeholder="输入收款账号，选填">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="control-group span8">
                    <label class="control-label">描述:</label>
                    <div class="controls">
                        <input name="description" type="text"
                               data-rules="{required:false,}"
                               class="input-normal control-text" placeholder="输入资金方描述">
                    </div>
                </div>
                <div class="control-group span9">
                    <label class="control-label"><s>*</s>资金方状态:</label>
                    <div class="controls">
                        <select name="enable">
                            <option value="1" selected="selected">已启用</option>
                            <option value="0">已停用</option>
                        </select>
                        <%--<input name="bankAccount" type="text"--%>
                        <%--data-rules="{required:false,}"--%>
                        <%--class="input-normal control-text" placeholder="输入收款账号，选填">--%>
                    </div>
                </div>
            </div>
            <div class="row" id="amount_count">
                <div class="control-group span12">
                    <label class="control-label">当前余额:</label>
                    <div class="controls">
                        <input name="amount" type="text" class="input-normal control-text" readonly="readonly"
                               placeholder="余额不用填写"> 元
                    </div>
                </div>
            </div>
            <input type="hidden" name="id" value="">
        </form>
    </div>
    <%-- 这里是查看与充值的div --%>
    <div id="addChargeFormDiv" class="hide">
        <form id="addChargeFormId" class="form-vertical" method="post">
            <div class="span6" id="charge_title"></div>
            <div class="row">
                <div class="control-group span6">
                    <div class="controls">
                        <input name="amount" type="text" data-rules="{required: true, number: true, range:true}" class="input-normal control-text">
                    </div>
                    <div class="controls">
                        <input name="remark" type="text" data-rules="{required:false}" class="input-normal control-text span8" placeholder="备注（选填）">
                    </div>
                </div>
            </div>
            <input type="hidden" id="fundId" name="fundId" value="">
            <input type="hidden" id="beforeAmount" name="beforeAmount" value="">
        </form>
    </div>

    <div class="search-grid-container">
        <div id="grid"></div>
    </div>
</div>

<script type="text/javascript">
    //1.判断number的编号、fundName是否相同
    var isFundNameOk = false;

    var enableUrl = "/backend/fund/enable";
    var disableUrl = "/backend/fund/disable";


    BUI.use(['bui/ux/crudgrid', 'bui/form', 'bui/ux/savedialog', 'bui/overlay'], function (CrudGrid, Form, Dialog) {

        //定义页面权限
        <framwork:crudPermission resource="/backend/fund"/>

        //这里稍后添加充值和查看充值记录的信息
        var chargeDialog = new Dialog({
            entityName: '资金方充值',
            url: {
                update: '${ctx}/backend/fund/addCharge'
            },
            dialogContentId: 'addChargeFormDiv',
            formId: 'addChargeFormId'
        });

        var columns = [
            {title: '序号', dataIndex: 'id', width: '5%'},
            {title: '资金方代码', dataIndex: 'code', width: '8%'},
            {title: '资金方编码（总部提供）', dataIndex: 'fundCode', width: '10%'},
            {title: '资金方名称', dataIndex: 'fundName', width: '10%'},
            {
                title: '余额', dataIndex: 'balance', width: '10%', renderer: function (value) {
                    return value.toFixed(2);
                }
            },
            {
                title: '是否启用', dataIndex: 'enable', width: '120px', renderer: function (value) {
                    console.info(value);
                    if (value) {
                        return '已启用';
                    } else {
                        return '已停用';
                    }
                }
            },
            {title: '修改时间', dataIndex: 'updateTime', width: '150px', renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '创建时间', dataIndex: 'createdTime', width: '150px', renderer: BUI.Grid.Format.datetimeRenderer}
        ];

        var crudGrid = new CrudGrid({
            entityName: '资金方信息',
            pkColumn: 'id',//主键
            storeUrl: '${ctx}/backend/fund/list',
            addUrl: '${ctx}/backend/fund/add',
            updateUrl: '${ctx}/backend/fund/update',
            removeUrl: '${ctx}/backend/fund/disable',
            columns: columns,
            showAddBtn: true,
            showUpdateBtn: true,
            showRemoveBtn: false,
            addOrUpdateFormId: 'addOrUpdateForm',
            operationwidth:'180px',
            dialogContentId: 'addOrUpdate',
            operationColumnRenderer: function (value, obj) {//操作追加
                var operatorHtml = '';

                <shiro:hasPermission name='/backend/fund:recharge'>
                operatorHtml +=  '<span class="grid-command js-fundRecharge cart" data-id="'+obj.id+'" title="资金方充值">充值</span>';
                </shiro:hasPermission>

                <shiro:hasPermission name='/backend/fund:detail'>
                operatorHtml += CrudGrid.createLink({
                    id: obj.id,
                    title: obj.fundName + '—充值记录',
                    text: '充值记录',
                    href: '${ctx}/backend/fund/detail?fundId=' + obj.id
                });
                </shiro:hasPermission>

                <shiro:hasPermission name='/backend/fund:enable'>
                if (obj.enable == '1') {
                    operatorHtml += '<span class="grid-command js-fundEnable down" data-id="'+obj.id+'" title="停用资金方" >停用</span>';
                } else {
                    operatorHtml += '<span class="grid-command js-fundDisable up" data-id="'+obj.id+'" title="启用资金方" >启用</span>';
                }
                </shiro:hasPermission>
                return operatorHtml;
            },
            gridCfg: {
                innerBorder: true
            },
            storeCfg: {//定义store的排序，如果是复合主键一定要修改
                sortInfo: {
                    field: 'id',//排序字段
                    direction: 'DESC' //升序ASC，降序DESC
                }
            }
        });


        // 充值列表
        $(".js-fundRecharge").on("click",function (ele) {
            var $this = $(ele);
            var id = $(ele).data("id");
            var grid = crudGrid.get('grid');
            var item =  grid.getItemAt(id)
            //记录选中的id
            $("#selectId").val(target.id);
            //记录充值金额--并对ChargeDialog添加信息
            $("#amountRecord").val(target.amount);
            $("#charge_title").text("您正在为资金方【" + title + "】充值");
            $("#fundId").val(target.id);//赋值到fund的id

            BUI.use(['bui/ux/savedialog'], function (Dialog) {
                new Dialog({
                    entityName: '资金方充值',
                    url: {
                        update: '${ctx}/backend/fund/addCharge'
                    },
                    dialogContentId: 'addChargeFormDiv',
                    formId: 'addChargeFormId'
                }).show();
            });
        })




        //点击获取列，获取主键id
        var grid = crudGrid.get('grid');
        grid.on('cellclick', function (ev) {
            console.info(ev);
            var sender = $(ev.domTarget); //点击的Dom
            var target = ev.record;
            console.info(target.id)
            var title = target.fundName;
            //记录选中的id
            $("#selectId").val(target.id);
            //记录充值金额--并对ChargeDialog添加信息
            $("#amountRecord").val(target.amount);

            if (sender.hasClass('cart')) {
                $("#charge_title").text("您正在为资金方【" + title + "】充值");
                $("#fundId").val(target.id);//赋值到fund的id
                $('#rechargeMoney').attr("placeholder", '当前余额为: ' + target.amount + ' 元');
                chargeDialog.update();
            } else if (sender.hasClass('icon-trash')) {
                fundInfoDailog.show();
            } else if (sender.hasClass('down')) {
                var data = {};
                data.id = target.id;
                data.enable = '0';
                if (confirm('你确定要资金方【' + target.fundName + '】无效？')) {
                    $.ajax({
                        url: disableUrl,
                        type: "POST",
                        data: data,
                        async: false,
                        success: function (data) {
                            console.info(data);
                            /* 这里添加一个提示框 */
                            if (data.success) {
                                showSuccess(data.msg);
                                crudGrid.load(true);
                            } else {
                                showWarning(data.msg);
                            }
                        },
                        failure: function () {
                            showWarning('系统异常，请稍后操作');
                        }
                    });
                }
            } else if (sender.hasClass('up')) {
                var data = {};
                data.id = target.id;
                data.enable = '1';
                if (confirm('你确定要资金方【' + target.fundName + '】生效？')) {
                    $.ajax({
                        url: enableUrl,
                        type: "POST",
                        data: data,
                        async: false,
                        success: function (data) {
                            console.info(data);
                            /* 这里添加一个提示框 */
                            if (data.success) {
                                showSuccess(data.msg);
                                crudGrid.load(true);
                            } else {
                                showWarning(data.msg);
                            }
                        },
                        failure: function () {
                            showWarning('系统异常，请稍后操作');
                        }
                    });
                }
            } else {
                return true;
            }
        });



        chargeDialog.on('afterSubmit', function (data, dialog, form, saveType) {
            crudGrid.load(true);
        });


        var beforeUpdateShow = function (dialog, form) {
            //显示余额
            $("#amount_text").show();
        }
        crudGrid.on('beforeUpdateShow', beforeUpdateShow);

        /* 判断充值金额是否合规 */
        Form.Rules.add({
            name: 'range',  //规则名称
            msg: '请输入合适资金',//默认显示的错误信息
            validator: function (value, baseValue, formatMsg) { //验证函数，验证值、基准值、格式化后的错误信息
                var chargeAmount = $('#rechargeMoney').val();
                var before = parseFloat(beforeAmount);
                var now = parseFloat(chargeAmount);
                if ((before + now) < 0) {
                    return formatMsg;
                }
            }
        });

        /*校验saveAndUpdate表单中编号要大写*/
        Form.Rules.add({
            name: 'nid',
            msg: '请输入大写字母开头',
            validator: function (value, baseValue, formatMsg) {
                var regexp = new RegExp(/^[A-Z]+\w*$/g)
                if (!regexp.test(value)) {
                    return formatMsg
                }
            }
        });

        /* 显示提示框 */
        function showWarning(str) {
            BUI.Message.Alert(str, 'warning');
        }

        function showSuccess(str) {
            BUI.Message.Alert(str, 'success');
        }

        /* 这里ajax校验数据编号或名称的唯一方法 */
        function checkIsTheOne(url, str, target) {
            var isNotSame = false;
            console.info(ctx + url + str);
            $.ajax({
                url: ctx + url + str,
                type: "GET",
                cache: false,
                success: function (data) {
                    console.info(data);
                    if (!data.success) {
                        // BUI.Message.Alert(data.msg,'warning');
                        console.info(data.msg);
                    }
                    if (target == "1") {
                        isNotSame = data.success;
                    } else if (target == "2") {
                        isFundNameOk = data.success;
                    }
                },
                failure: function (data) {
                    BUI.Message.Alert('加载失败，请稍后再试！');
                    return isNotSame;
                }
            })
        }
    });

</script>

</body>
</html>


