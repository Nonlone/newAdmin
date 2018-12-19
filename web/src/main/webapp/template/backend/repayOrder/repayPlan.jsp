<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/import-tags.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=7" />
    <title>Jtable</title>
    <%@include file="../../common/import-static.jsp"%>
    <script type="text/javascript" src="${ctx}/static/common/js/jtable.js"></script>
    <script type="text/javascript">
        Date.prototype.format = function(format) //author: meizz
        {
            var o = {
                "M+" : this.getMonth()+1, //month
                "d+" : this.getDate(),    //day
                "h+" : this.getHours(),   //hour
                "m+" : this.getMinutes(), //minute
                "s+" : this.getSeconds(), //second
                "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
                "S" : this.getMilliseconds() //millisecond
            }
            if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
                (this.getFullYear()+"").substr(4 - RegExp.$1.length));
            for(var k in o)if(new RegExp("("+ k +")").test(format))
                format = format.replace(RegExp.$1,
                    RegExp.$1.length==1 ? o[k] :
                        ("00"+ o[k]).substr((""+ o[k]).length));
            return format;
        }
    </script>
    <script type="text/javascript">
        var staFields = [ {
            field : "tbodytd",
            format : "<td id='@field' style='text-align:center;'>"
        }, {
            field : "term",
            name : "期数"
        }, {
            field : "dueDate",
            name : "还款日期"
        }  ];
        var dynFields = [ {
            field : "tbodytd",
            format : "<td id='@field' style='text-align:center;'>"
        }, {
            field : "theadth",
            format : "<th id='@field' style='text-align:center;'>"
        }, {
            field : "pincipalAmount",
            name : "应还本金"
        }, {
            field : "pincipalBalanceAmount",
            name : "剩余应还本金"
        }, {
            field : "interestAmount",
            name : "应还利息"
        }, {
            field : "interestBalanceAmount",
            name : "剩余应还利息"
        }, {
            field : "guaranteeFeeAmount",
            name : "应还担保费"
        }, {
            field : "guaranteeFeeBalanceAmount",
            name : "剩余应还担保费"
        }, {
            field : "approveFeeAmount",
            name : "应还评审费"
        }, {
            field : "approveFeeBalanceAmount",
            name : "剩余应还评审费"
        }, {
            field : "overdueFineAmount",
            name : "应还罚息"
        }, {
            field : "overdueFineBalanceAmount",
            name : "剩余应还罚息"
        }, {
            field : "overdueFineCompoundAmount",
            name : "应还罚息复利"
        }, {
            field : "overdueFineCompoundBalanceAmount",
            name : "剩余应还罚息复利"
        }, {
            field : "innerInteAmount",
            name : "应还复利"
        }, {
            field : "innerInteAalanceAmount",
            name : "剩余应还复利"
        }, {
            field : "occupyFeeAmount",
            name : "应还资金占用费"
        }, {
            field : "occupyFeeBalanceAmount",
            name : "剩余应还资金占用费"
        }, {
            field : "poundageAmtAmount",
            name : "应还违约金"
        }, {
            field : "poundageAmtBalanceAmount",
            name : "剩余应还违约金"
        } , {
            field : "brokerCommissionAmount",
            name : "应还中间人服务费"
        }, {
            field : "brokerCommissionBalanceAmount",
            name : "剩余应还中间人服务费"
        } ];

        function generateData() {
            var data = [];
            data.length = 0;

            var repayPlan = ${repayPlan};

            /************总数***************/
            var termCount = "总计：";
            var pincipalAmountCount = 0;
            var pincipalBalanceAmountCount = 0;
            var interestAmountCount = 0;
            var interestBalanceAmountCount = 0;
            var overdueFineAmountCount = 0;
            var overdueFineBalanceAmountCount = 0;
            var overdueFineCompoundAmountCount = 0;
            var overdueFineCompoundBalanceAmountCount = 0;
            var innerInteAmountCount = 0;
            var innerInteAalanceAmountCount = 0;
            var occupyFeeAmountCount = 0;
            var occupyFeeBalanceAmountCount = 0;
            var poundageAmtAmountCount = 0;
            var poundageAmtBalanceAmountCount = 0;
            var approveFeeAmountCount = 0;
            var approveFeeBalanceAmountCount = 0;
            var guaranteeFeeAmountCount = 0;
            var guaranteeFeeBalanceAmountCount = 0;
            var brokerCommissionAmountCount = 0;
            var brokerCommissionBalanceAmountCount = 0;

            for ( var i = 0; i < repayPlan.length; i++) {
                pincipalAmountCount +=  Number(repayPlan[i].pincipalAmount);
                pincipalBalanceAmountCount += Number(repayPlan[i].pincipalBalanceAmount);
                interestAmountCount += Number(repayPlan[i].interestAmount);
                interestBalanceAmountCount += Number(repayPlan[i].interestBalanceAmount);
                overdueFineAmountCount += Number(repayPlan[i].overdueFineAmount);
                overdueFineBalanceAmountCount += Number(repayPlan[i].overdueFineBalanceAmount);
                overdueFineCompoundAmountCount += Number(repayPlan[i].overdueFineCompoundAmount);
                overdueFineCompoundBalanceAmountCount += Number(repayPlan[i].overdueFineCompoundBalanceAmount);
                innerInteAmountCount += Number(repayPlan[i].innerInteAmount);
                innerInteAalanceAmountCount += Number(repayPlan[i].innerInteAalanceAmount);
                occupyFeeAmountCount += Number(repayPlan[i].occupyFeeAmount);
                occupyFeeBalanceAmountCount += Number(repayPlan[i].occupyFeeBalanceAmount);
                poundageAmtAmountCount += Number(repayPlan[i].poundageAmtAmount);
                poundageAmtBalanceAmountCount += Number(repayPlan[i].poundageAmtBalanceAmount);
                approveFeeAmountCount += Number(repayPlan[i].approveFeeAmount);
                approveFeeBalanceAmountCount += Number(repayPlan[i].approveFeeBalanceAmount);
                guaranteeFeeAmountCount += Number(repayPlan[i].guaranteeFeeAmount);
                guaranteeFeeBalanceAmountCount += Number(repayPlan[i].guaranteeFeeBalanceAmount);
                brokerCommissionAmountCount += Number(repayPlan[i].brokerCommissionAmount);
                brokerCommissionBalanceAmountCount += Number(repayPlan[i].brokerCommissionBalanceAmount);
            }

            data.push({
                term : termCount,
                dueDate : " ",
                pincipalAmount : Number(pincipalAmountCount).toFixed(2),
                pincipalBalanceAmount : Number(pincipalBalanceAmountCount).toFixed(2),
                interestAmount : Number(interestAmountCount).toFixed(2),
                interestBalanceAmount : Number(interestBalanceAmountCount).toFixed(2),
                overdueFineAmount : Number(overdueFineAmountCount).toFixed(2),
                overdueFineBalanceAmount : Number(overdueFineBalanceAmountCount).toFixed(2),
                overdueFineCompoundAmount : Number(overdueFineCompoundAmountCount).toFixed(2),
                overdueFineCompoundBalanceAmount : Number(overdueFineCompoundBalanceAmountCount).toFixed(2),
                innerInteAmount : Number(innerInteAmountCount).toFixed(2),
                innerInteAalanceAmount : Number(innerInteAalanceAmountCount).toFixed(2),
                occupyFeeAmount : Number(occupyFeeAmountCount).toFixed(2),
                occupyFeeBalanceAmount : Number(occupyFeeBalanceAmountCount).toFixed(2),
                poundageAmtAmount : Number(poundageAmtAmountCount).toFixed(2),
                poundageAmtBalanceAmount : Number(poundageAmtBalanceAmountCount).toFixed(2),
                approveFeeAmount : Number(approveFeeAmountCount).toFixed(2),
                approveFeeBalanceAmount : Number(approveFeeBalanceAmountCount).toFixed(2),
                guaranteeFeeAmount : Number(guaranteeFeeAmountCount).toFixed(2),
                guaranteeFeeBalanceAmount : Number(guaranteeFeeBalanceAmountCount).toFixed(2),
                brokerCommissionAmount : Number(brokerCommissionAmountCount).toFixed(2),
                brokerCommissionBalanceAmount : Number(brokerCommissionBalanceAmountCount).toFixed(2)
            });
            /******************************/


            for ( var i = 0; i < repayPlan.length; i++) {
                var term = repayPlan[i].term;
                var dueDate = new Date(repayPlan[i].dueDate);
                var pincipalAmount = Number(repayPlan[i].pincipalAmount).toFixed(2);
                var pincipalBalanceAmount = Number(repayPlan[i].pincipalBalanceAmount).toFixed(2);
                var interestAmount = Number(repayPlan[i].interestAmount).toFixed(2);
                var interestBalanceAmount = Number(repayPlan[i].interestBalanceAmount).toFixed(2);
                var overdueFineAmount = Number(repayPlan[i].overdueFineAmount).toFixed(2);
                var overdueFineBalanceAmount = Number(repayPlan[i].overdueFineBalanceAmount).toFixed(2);
                var overdueFineCompoundAmount = Number(repayPlan[i].overdueFineCompoundAmount).toFixed(2);
                var overdueFineCompoundBalanceAmount = Number(repayPlan[i].overdueFineCompoundBalanceAmount).toFixed(2);
                var innerInteAmount = Number(repayPlan[i].innerInteAmount).toFixed(2);
                var innerInteAalanceAmount = Number(repayPlan[i].innerInteAalanceAmount).toFixed(2);
                var occupyFeeAmount = Number(repayPlan[i].occupyFeeAmount).toFixed(2);
                var occupyFeeBalanceAmount = Number(repayPlan[i].occupyFeeBalanceAmount).toFixed(2);
                var poundageAmtAmount = Number(repayPlan[i].poundageAmtAmount).toFixed(2);
                var poundageAmtBalanceAmount = Number(repayPlan[i].poundageAmtBalanceAmount).toFixed(2);
                var approveFeeAmount = Number(repayPlan[i].approveFeeAmount).toFixed(2);
                var approveFeeBalanceAmount = Number(repayPlan[i].approveFeeBalanceAmount).toFixed(2);
                var guaranteeFeeAmount = Number(repayPlan[i].guaranteeFeeAmount).toFixed(2);
                var guaranteeFeeBalanceAmount = Number(repayPlan[i].guaranteeFeeBalanceAmount).toFixed(2);
                var brokerCommissionAmount = Number(repayPlan[i].brokerCommissionAmount).toFixed(2);
                var brokerCommissionBalanceAmount = Number(repayPlan[i].brokerCommissionBalanceAmount).toFixed(2);

                data.push({
                    term : term,
                    dueDate : dueDate.format("yyyy-MM-dd"),
                    pincipalAmount : pincipalAmount,
                    pincipalBalanceAmount : pincipalBalanceAmount,
                    interestAmount : interestAmount,
                    interestBalanceAmount : interestBalanceAmount,
                    overdueFineAmount : overdueFineAmount,
                    overdueFineBalanceAmount : overdueFineBalanceAmount,
                    overdueFineCompoundAmount : overdueFineCompoundAmount,
                    overdueFineCompoundBalanceAmount : overdueFineCompoundBalanceAmount,
                    innerInteAmount : innerInteAmount,
                    innerInteAalanceAmount : innerInteAalanceAmount,
                    occupyFeeAmount : occupyFeeAmount,
                    occupyFeeBalanceAmount : occupyFeeBalanceAmount,
                    poundageAmtAmount : poundageAmtAmount,
                    poundageAmtBalanceAmount : poundageAmtBalanceAmount,
                    approveFeeAmount : approveFeeAmount,
                    approveFeeBalanceAmount : approveFeeBalanceAmount,
                    guaranteeFeeAmount : guaranteeFeeAmount,
                    guaranteeFeeBalanceAmount : guaranteeFeeBalanceAmount,
                    brokerCommissionAmount : brokerCommissionAmount,
                    brokerCommissionBalanceAmount : brokerCommissionBalanceAmount
                });
            }
            return data;
        }
        var data = generateData();
        jQuery(document).ready(function($) {
            var dynOption = {
                data:data,//render data
                staFields:staFields,//static table fields
                dynFields:dynFields,//dynamic table fields
                scrollStart:8,//scroll start fields
                scrollBarHeight:18,//滚动条高
                staWidth:200,//static table width
                dynWidth:1300,//dynamic table width
                dynCellWidth:90//extend cell width in dynamic part
            };
            $("#dynTable").jexTable(dynOption);
        });
    </script>
</head>
<body style="height: 795px">
<table id="listTable"></table>
<div style="margin-top:20px;" id="dynTable"></div>
</body>
</html>