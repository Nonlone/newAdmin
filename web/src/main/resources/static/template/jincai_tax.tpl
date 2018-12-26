<style type="text/css">
    ul{margin:0;padding:0;list-style:none;}
    .table{}
    .table-row-group{display:table-row-group;}
    .table-row{display:table-row;}
    .table-row-group .table-row:hover,.table-footer-group .table-row:hover{background:#f6f6f6}
    .table-cell{display:table-cell;padding:0 5px;border:1px solid #ccc; height: 50px;width:200px;text-align:center;vertical-align:middle;}
    .table-span{text-align:center;}
</style>
<div style="margin:10px auto;padding-left:20px;padding-top:10px;">

    <div>

        <div>
            <h1 style="background-color:#FFEFE1">纳税人基本信息</h1>
        </div>

        <div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">企业性质</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.qyxz}}</li>
                    <li class="table-cell">注册地邮政编码</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.zcdyzbm}}</li>
                    <li class="table-cell">注册登记机关</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.djjg}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">开业日期</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.clrq}}</li>
                    <li class="table-cell">营业期限</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.yyqx}}</li>
                    <li class="table-cell">经营范围</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.jyfw}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">主管税务机关</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.zgswjg}}</li>
                    <li class="table-cell">纳税信用等级</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.pjjg}}</li>
                    <li class="table-cell">纳税人识别号</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.nsrsbh}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">纳税人状态</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.nsrzt}}</li>
                    <li class="table-cell">纳税类型</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.nsrzglx}}</li>
                    <li class="table-cell">法人姓名</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.frxm}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">担任法人区间(月)</li>
                    <li class="table-cell">{{jbxx.baseinfoVo.drfrqj}}</li>
                    <li class="table-cell">股东信息</li>
                    <li class="table-cell">
                        {{each jbxx.shareholderVoList}}
                            {{$value.gdmc}},{{$value.gdxz}},{{$value.cgbl}}%<br>
                        {{/each}}
                    </li>
                    <li class="table-cell"></li>
                    <li class="table-cell"></li>
                </ul>
            </div>
        </div>
    </div>

    <br>

    <div>
        <div>
            <h1 style="background-color:#FFEFE1">主要财务数据</h1>
        </div>
        <div>

            {{each qycwbbList}}
            <div>
                <h2>年份：{{$value.nd}}</h2>
                <div class="table-row-group">
                    <ul class="table-row">
                        <li class="table-cell">营业收入(万元)</li>
                        <li class="table-cell">{{$value.record.yysr}}</li>
                        <li class="table-cell">毛利润(万元)</li>
                        <li class="table-cell">{{$value.record.mlr}}</li>
                        <li class="table-cell">净利润(万元)</li>
                        <li class="table-cell">{{$value.record.jlr}}</li>
                    </ul>
                </div>
                <div class="table-row-group">
                    <ul class="table-row">
                        <li class="table-cell">资产总额(万元)</li>
                        <li class="table-cell">{{$value.record.zczj}}</li>
                        <li class="table-cell">负债总额(万元)</li>
                        <li class="table-cell">{{$value.record.fzhj}}</li>
                        <li class="table-cell">股东权益(万元)</li>
                        <li class="table-cell">{{$value.record.gdqy}}</li>
                    </ul>
                </div>
                <div class="table-row-group">
                    <ul class="table-row">
                        <li class="table-cell">资产负债率</li>
                        <li class="table-cell">{{$value.record.zcfzl}}</li>
                        <li class="table-cell">短期借款(万元) </li>
                        <li class="table-cell">{{$value.record.dqjk}}</li>
                        <li class="table-cell">长期借款(万元)</li>
                        <li class="table-cell">{{$value.record.cqjk}}</li>
                    </ul>
                </div>
                <div class="table-row-group">
                    <ul class="table-row">
                        <li class="table-cell">流动比率</li>
                        <li class="table-cell">{{$value.record.ldbl}}</li>
                        <li class="table-cell">速动比率</li>
                        <li class="table-cell">{{$value.record.sdbl}}</li>
                        <li class="table-cell">毛利率</li>
                        <li class="table-cell">{{$value.record.mll}}</li>
                    </ul>
                </div>
                <div class="table-row-group">
                    <ul class="table-row">
                        <li class="table-cell">净利率</li>
                        <li class="table-cell">{{$value.record.jll}}</li>
                        <li class="table-cell">净资产收益率</li>
                        <li class="table-cell">{{$value.record.jzcsyl}}</li>
                        <li class="table-cell">存货周转率</li>
                        <li class="table-cell">{{$value.record.chzzl}}</li>
                    </ul>
                </div>
                <div class="table-row-group">
                    <ul class="table-row">
                        <li class="table-cell">应收账款周转率</li>
                        <li class="table-cell">{{$value.record.yszkzzl}}</li>
                        <li class="table-cell">流动资产周转率</li>
                        <li class="table-cell">{{$value.record.ldzczzl}}</li>
                        <li class="table-cell">总资产周转率</li>
                        <li class="table-cell">{{$value.record.zczjzzl}}</li>
                    </ul>
                </div>
                <div class="table-row-group">
                    <ul class="table-row">
                        <li class="table-cell">营业周期(天)</li>
                        <li class="table-cell">{{$value.record.yyzq}}</li>
                        <li class="table-cell">利润表(缺失次数统计)</li>
                        <li class="table-cell">{{$value.record.lrbSbcis}}</li>
                        <li class="table-cell">资产负债表(缺失次数统计)</li>
                        <li class="table-cell">{{$value.record.zcfzbSbcis}}</li>
                    </ul>
                </div>
            </div>
            {{/each}}

        </div>
    </div>
<br>

    <div>
        <div>
            <h1 style="background-color:#FFEFE1">历年发票数据汇总</h1>
        </div>
        {{each qyfpsj.qyfpxxList}}
        <div>
            <h2>年份：{{$value.nd}}</h2>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">上游供应商交易金额(万元)</li>
                    <li class="table-cell">{{$value.record.jyjeSy}}</li>
                    <li class="table-cell">上游供应商交易次数(次)</li>
                    <li class="table-cell">{{$value.record.jyzsSy}}</li>
                    <li class="table-cell">上游供应商数量(个)</li>
                    <li class="table-cell">{{$value.record.xsfSl}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">下游经销商交易金额(万元)</li>
                    <li class="table-cell">{{$value.record.jyjeXy}}</li>
                    <li class="table-cell">下游经销商交易次数(次)</li>
                    <li class="table-cell">{{$value.record.jyzsXy}}</li>
                    <li class="table-cell">下游经销商数量(个)</li>
                    <li class="table-cell">{{$value.record.gmfSl}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">其它重要支出交易金额(万元)</li>
                    <li class="table-cell">{{$value.record.jyjeQt}}</li>
                    <li class="table-cell"></li>
                    <li class="table-cell"></li>
                    <li class="table-cell"></li>
                    <li class="table-cell"></li>
                </ul>
            </div>
        </div>
        {{/each}}
    </div>
<br>
    <div>
        <div>
            <h1 style="background-color:#FFEFE1">历年其他重要支出数据明细</h1>
        </div>
        {{each qyfpsj.qyfpxxList}}
        <div>
            <h2>年份：{{$value.nd}}</h2>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">水费支出(万元)</li>
                    <li class="table-cell">{{$value.record.sfZc}}</li>
                    <li class="table-cell">电费支出(万元)</li>
                    <li class="table-cell">{{$value.record.dfZc}}</li>
                    <li class="table-cell">燃气支出(万元)</li>
                    <li class="table-cell">{{$value.record.rqZc}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">热力支出(万元)</li>
                    <li class="table-cell">{{$value.record.rlZc}}</li>
                    <li class="table-cell">运输及仓储支出(万元)</li>
                    <li class="table-cell">{{$value.record.ysCcZc}}</li>
                    <li class="table-cell"></li>
                    <li class="table-cell"></li>
                </ul>
            </div>
        </div>
        {{/each}}
    </div>

<br>
    <div>
        <div>
            <h1 style="background-color:#FFEFE1">纳税情况汇总</h1>
        </div>
        {{each nszsxx.ndQyZsxxVoList}}
        <div>
            <h2>年份：{{$value.nd}}</h2>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">纳税总额(万元)</li>
                    <li class="table-cell">{{$value.record.nsze}}</li>
                    <li class="table-cell">纳税总额增长率</li>
                    <li class="table-cell">{{$value.record.nszeRate}}</li>
                    <li class="table-cell">其中,增值税(万元)</li>
                    <li class="table-cell">{{$value.record.zzs}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">其中,企业所得税(万元)</li>
                    <li class="table-cell">{{$value.record.qysds}}</li>
                    <li class="table-cell">未按期缴纳税款次数</li>
                    <li class="table-cell">{{$value.record.wnqjnkcs}}</li>
                    <li class="table-cell">违法违规次数</li>
                    <li class="table-cell">{{$value.record.wfwgcs}}</li>
                </ul>
            </div>
        </div>
        {{/each}}
    </div>

</div>