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
            name : "本金"
        }, {
            field : "pincipalBalanceAmount",
            name : "本金"
        }, {
            field : "interestAmount",
            name : "利息"
        }, {
            field : "interestBalanceAmount",
            name : "利息"
        }, {
            field : "overdueFineAmount",
            name : "罚息"
        }, {
            field : "overdueFineBalanceAmount",
            name : "罚息"
        }, {
            field : "overdueFineCompoundAmount",
            name : "罚息复利"
        }, {
            field : "overdueFineCompoundBalanceAmount",
            name : "罚息复利"
        }, {
            field : "innerInteAmount",
            name : "复利"
        }, {
            field : "innerInteAalanceAmount",
            name : "复利"
        }, {
            field : "occupyFeeAmount",
            name : "资金占用费"
        }, {
            field : "occupyFeeBalanceAmount",
            name : "资金占用费"
        }, {
            field : "poundageAmtAmount",
            name : "违约金"
        }, {
            field : "poundageAmtBalanceAmount",
            name : "违约金"
        } , {
            field : "approveFeeAmount",
            name : "审批费"
        }, {
            field : "approveFeeBalanceAmount",
            name : "审批费"
        }, {
            field : "guaranteeFeeAmount",
            name : "担保费"
        }, {
            field : "guaranteeFeeBalanceAmount",
            name : "担保费"
        }, {
            field : "brokerCommissionAmount",
            name : "中间人服务费"
        }, {
            field : "brokerCommissionBalanceAmount",
            name : "中间人服务费"
        } ];

        function generateData() {
            var data = [];
            data.length = 0;

            var repayPlan = ${repayPlan};

            for ( var i = 0; i < repayPlan.length; i++) {
                var term = repayPlan[i].term;
                var dueDate = new Date(repayPlan[i].dueDate);
                var pincipalAmount = repayPlan[i].pincipalAmount;
                var pincipalBalanceAmount = repayPlan[i].pincipalBalanceAmount;
                var interestAmount = repayPlan[i].interestAmount;
                var interestBalanceAmount = repayPlan[i].interestBalanceAmount;
                var overdueFineAmount = repayPlan[i].overdueFineAmount;
                var overdueFineBalanceAmount = repayPlan[i].overdueFineBalanceAmount;
                var overdueFineCompoundAmount = repayPlan[i].overdueFineCompoundAmount;
                var overdueFineCompoundBalanceAmount = repayPlan[i].overdueFineCompoundBalanceAmount;
                var innerInteAmount = repayPlan[i].innerInteAmount;
                var innerInteAalanceAmount = repayPlan[i].innerInteAalanceAmount;
                var occupyFeeAmount = repayPlan[i].occupyFeeAmount;
                var occupyFeeBalanceAmount = repayPlan[i].occupyFeeBalanceAmount;
                var poundageAmtAmount = repayPlan[i].poundageAmtAmount;
                var poundageAmtBalanceAmount = repayPlan[i].poundageAmtBalanceAmount;
                var approveFeeAmount = repayPlan[i].approveFeeAmount;
                var approveFeeBalanceAmount = repayPlan[i].approveFeeBalanceAmount;
                var guaranteeFeeAmount = repayPlan[i].guaranteeFeeAmount;
                var guaranteeFeeBalanceAmount = repayPlan[i].guaranteeFeeBalanceAmount;
                var brokerCommissionAmount = repayPlan[i].brokerCommissionAmount;
                var brokerCommissionBalanceAmount = repayPlan[i].brokerCommissionBalanceAmount;

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