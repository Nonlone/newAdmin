

<!DOCTYPE html>
<html lang="en">
<head>
    <style type="text/css">
        ul{margin:0;padding:0;list-style:none;}
        .table{display:table;border-collapse:collapse;border:1px solid #ccc;width: 900px;}
        .table-row-group{display:table-row-group;}
        .table-row{display:table-row;}
        .table-row-group .table-row:hover,.table-footer-group .table-row:hover{background:#f6f6f6}
        .table-cell{display:table-cell;padding:0 5px;border:1px solid #ccc; height: 50px;vertical-align:middle;font-size: small}
        .table-span{width: 30px;height: 30px; line-height: 30px;text-align:center;border: 2px #0c0c0c solid;border-top-left-radius:2mm;border-top-right-radius: 2mm;border-bottom-left-radius: 2mm;border-bottom-right-radius: 2mm}
    </style>
    <meta charset="UTF-8">
    <title>贷后邦</title>
</head>
<body>

<div>

    <span style="font-size:14px">查询结果:&nbsp;<strong>{{user_name}}</strong></span>
    <br>
    <br>
    <h1><strong>查询参数</strong></h1>
    &nbsp;&nbsp;&nbsp;用户输入信息
    <div class="table">
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell"><strong>姓名：{{user_name}}</strong></li>
                <li class="table-cell"><strong>手机号：{{user_phone}}</strong></li>
                <li class="table-cell"><strong>身份证：{{user_idcard}}</strong></li>
            </ul>
        </div>

    </div>

    <br>
    <h1><strong>基本信息</strong></h1>
    &nbsp;&nbsp;&nbsp;根据身份证等信息进行基本分析

    <div class="table">
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">年龄</li>
                <li class="table-cell">{{user_basic.age}}</li>
                <li class="table-cell">性别</li>
                <li class="table-cell">{{user_basic.gender}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">生日日期</li>
                <li class="table-cell">{{user_basic.birthday}}</li>
                <li class="table-cell">身份证是否是有效身份证</li>
                <li class="table-cell">
                    {{if user_basic.idcard_validate = 1}}
                    是
                    {{else if user_basic.idcard_validate = 0}}
                    否
                    {{else}}

                    {{/if}}
                </li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">身份证户籍省份</li>
                <li class="table-cell">{{user_basic.idcard_province}}</li>
                <li class="table-cell">身份证户籍城市</li>
                <li class="table-cell">{{user_basic.idcard_city}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">身份证户籍地区</li>
                <li class="table-cell">{{user_basic.idcard_region}}</li>
                <li class="table-cell">手机运营商</li>
                <li class="table-cell">{{user_basic.phone_operator}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">手机归属地省份</li>
                <li class="table-cell">{{user_basic.phone_province}}</li>
                <li class="table-cell">手机归属地城市</li>
                <li class="table-cell">{{user_basic.phone_city}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">身份证号记录天数</li>
                <li class="table-cell">{{user_basic.record_idcard_days}}</li>
                <li class="table-cell">手机号记录天数</li>
                <li class="table-cell">{{user_basic.record_phone_days}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">身份证最近出现时间</li>
                <li class="table-cell">{{user_basic.last_appear_idcard}}</li>
                <li class="table-cell">手机号最近出现时间</li>
                <li class="table-cell">{{user_basic.last_appear_phone}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">关联身份证数量</li>
                <li class="table-cell">{{user_basic.used_idcards_cnt}}</li>
                <li class="table-cell">关联手机号数量</li>
                <li class="table-cell">{{user_basic.used_phones_cnt}}</li>
            </ul>
        </div>
    </div>

    <br>
    <h1><strong>社交风险点</strong></h1>
    &nbsp;&nbsp;&nbsp;根据社交网络进行分析

    <div class="table">
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">葫芦分</li>
                <li class="table-cell"><div class="table-span">{{risk_social_network.sn_score}}</div>(推荐值: 10分以下可认定为坏人) </li>
                <li class="table-cell">直接联系人</li>
                <li class="table-cell">{{risk_social_network.sn_order1_contacts_cnt}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">直接联系人在黑名单中数量(直接黑人)</li>
                <li class="table-cell">{{risk_social_network.sn_order1_blacklist_contacts_cnt}}</li>
                <li class="table-cell">间接联系人在黑名单中数量(间接黑人)</li>
                <li class="table-cell">{{risk_social_network.sn_order2_blacklist_contacts_cnt}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">认识间接黑人的直接联系人个数</li>
                <li class="table-cell">{{risk_social_network.sn_order2_blacklist_routers_cnt}}</li>
                <li class="table-cell">认识间接黑人的直接联系人比例</li>
                <li class="table-cell">{{risk_social_network.sn_order2_blacklist_routers_pct}}</li>
            </ul>
        </div>
    </div>


    <br>
    <h1><strong>黑名单</strong></h1>
    &nbsp;&nbsp;&nbsp;根据网贷、法院、银行黑名单匹配分析

    <div class="table">
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">身份证是否命中黑名单</li>
                <li class="table-cell"><div class="table-span">
                    {{if risk_blacklist.idcard_in_blacklist}}
                    是
                    {{else}}
                    否
                    {{/if}}
                    </div></li>
                <li class="table-cell">手机号是否命中黑名单</li>
                <li class="table-cell"><div class="table-span">
                    {{if risk_blacklist.phone_in_blacklist}}
                    是
                    {{else}}
                    否
                    {{/if}}
                    </div></li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">是否命中法院黑名单</li>
                <li class="table-cell"><div class="table-span">
                    {{if risk_blacklist.in_court_blacklist}}
                    是
                    {{else}}
                    否
                    {{/if}}
                </div></li>
                <li class="table-cell">是否命中网贷黑名单</li>
                <li class="table-cell"><div class="table-span">
                    {{if risk_blacklist.in_p2p_blacklist}}
                    是
                    {{else}}
                    否
                    {{/if}}
                    </div>
                </li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">是否命中银行黑名单</li>
                <li class="table-cell"><div class="table-span">
                    {{if risk_blacklist.in_bank_blacklist}}
                    是
                    {{else}}
                    否
                    {{/if}}
                </div></li>
                <li class="table-cell"></li>
                <li class="table-cell"></li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">最近该手机号出现在黑名单中时间</li>
                <li class="table-cell">{{risk_blacklist.last_appear_idcard_in_blacklist}}</li>
                <li class="table-cell">最近该身份证出现在黑名单中时间</li>
                <li class="table-cell">{{risk_blacklist.last_appear_phone_in_blacklist}}</li>
            </ul>
        </div>
    </div>


    <br>
    <h1><strong>历史类型</strong></h1>
    &nbsp;&nbsp;&nbsp;根据查询记录分析

    <div class="table">
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">线上消费分期出现次数</li>
                <li class="table-cell">{{history_org.online_installment_cnt}}</li>
                <li class="table-cell">线下消费分期出现次数</li>
                <li class="table-cell">{{history_org.offline_installment_cnt}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">信用卡代换出现次数</li>
                <li class="table-cell">{{history_org.credit_card_repayment_cnt}}</li>
                <li class="table-cell">小额快速贷出现次数</li>
                <li class="table-cell">{{history_org.payday_loan_cnt}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">线上现金贷出现次数</li>
                <li class="table-cell">{{history_org.online_cash_loan_cnt}}</li>
                <li class="table-cell">线下现金贷出现次数</li>
                <li class="table-cell">{{history_org.offline_cash_loan_cnt}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">其他</li>
                <li class="table-cell">{{history_org.others_cnt}}</li>
            </ul>
        </div>
    </div>



    <br>
    <h1><strong>查询历史</strong></h1>
    &nbsp;&nbsp;&nbsp;根据查询记录分析

    <div class="table">
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">历史查询总次数</li>
                <li class="table-cell">{{history_search.search_cnt}}</li>
                <li class="table-cell">历史查询总机构数</li>
                <li class="table-cell">{{history_search.org_cnt}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">最近7天查询次数</li>
                <li class="table-cell">{{history_search.search_cnt_recent_7_days}}</li>
                <li class="table-cell">最近7天查询机构数</li>
                <li class="table-cell">{{history_search.org_cnt_recent_7_days}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">最近14天查询次数</li>
                <li class="table-cell">{{history_search.search_cnt_recent_14_days}}</li>
                <li class="table-cell">最近14天查询机构数</li>
                <li class="table-cell">{{history_search.org_cnt_recent_14_days}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">最近30天查询次数</li>
                <li class="table-cell">{{history_search.search_cnt_recent_30_days}}</li>
                <li class="table-cell">最近30天查询机构数</li>
                <li class="table-cell">{{history_search.org_cnt_recent_30_days}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">最近60天查询次数</li>
                <li class="table-cell">{{history_search.search_cnt_recent_60_days}}</li>
                <li class="table-cell">最近60天查询机构数</li>
                <li class="table-cell">{{history_search.org_cnt_recent_60_days}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">最近90天查询次数</li>
                <li class="table-cell">{{history_search.search_cnt_recent_90_days}}</li>
                <li class="table-cell">最近90天查询机构数</li>
                <li class="table-cell">{{history_search.org_cnt_recent_90_days}}</li>
            </ul>
        </div>
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">最近180天查询次数</li>
                <li class="table-cell">{{history_search.search_cnt_recent_180_days}}</li>
                <li class="table-cell">最近180天查询机构数</li>
                <li class="table-cell">{{history_search.org_cnt_recent_180_days}}</li>
            </ul>
        </div>
    </div>


    <br>
    <h1><strong>手机可疑身份</strong></h1>
    &nbsp;&nbsp;&nbsp;除本机以外其他的手机绑定情况

    <div class="table">
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">其他手机号码</li>
                <li class="table-cell">运营商</li>
                <li class="table-cell">归属地</li>
                <li class="table-cell">归属地城市</li>
                <li class="table-cell">此号码绑定其他姓名个数</li>
                <li class="table-cell">查询此手机号的机构数</li>
                <li class="table-cell">最近此手机号出现时间</li>
            </ul>
        </div>

        {{each binding_phones}}
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">{{$value.other_phone}}</li>
                <li class="table-cell">{{$value.phone_operator}}</li>
                <li class="table-cell">{{$value.phone_province}}</li>
                <li class="table-cell">{{$value.phone_city}}</li>
                <li class="table-cell">{{$value.other_names_cnt}}</li>
                <li class="table-cell">{{$value.search_orgs_cnt}}</li>
                <li class="table-cell">{{$value.last_appear_phone}}</li>
            </ul>
        </div>
        {{/each}}

    </div>


    <br>
    <h1><strong>身份证可疑身份</strong></h1>
    &nbsp;&nbsp;&nbsp;除本机以外其他的身份证绑定情况

    <div class="table">
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">绑定的其他身份证</li>
                <li class="table-cell">身份证是否是有效身份</li>
                <li class="table-cell">身份证户籍省份</li>
                <li class="table-cell">身份证户籍城市</li>
                <li class="table-cell">身份证户籍地区</li>
                <li class="table-cell">年龄</li>
                <li class="table-cell">性别</li>
                <li class="table-cell">此号码绑定其他姓名个数</li>
                <li class="table-cell">查询此身份证的机构数</li>
                <li class="table-cell">最近此身份证出现的时间</li>
            </ul>
        </div>

        {{each binding_idcards}}
        <div class="table-row-group">
            <ul class="table-row">
                <li class="table-cell">{{$value.other_idcard}}</li>
                <li class="table-cell">
                    {{if $value.idcard_validate}}
                    是
                    {{else $value.idcard_validate}}
                    否
                    {{/if}}</li>
                <li class="table-cell">{{$value.idcard_province}}</li>
                <li class="table-cell">{{$value.idcard_city}}</li>
                <li class="table-cell">{{$value.idcard_region}}</li>
                <li class="table-cell">{{$value.idcard_age}}</li>
                <li class="table-cell">{{$value.idcard_gender}}</li>
                <li class="table-cell">{{$value.other_names_cnt}}</li>
                <li class="table-cell">{{$value.search_orgs_cnt}}</li>
                <li class="table-cell">{{$value.last_appear_idcard}}</li>
            </ul>
        </div>
        {{/each}}

    </div>

    <br><br><br><br>
</div>

</body>
</html>

