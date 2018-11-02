<DIV align=center>
    <DIV>
        <TABLE style=" BACKGROUND-REPEAT: repeat" border=0 cellSpacing=0 cellPadding=0 width=980 align=center>
            <TBODY>
            <TR></TR>
            <TR>
                <TD>
                    <TABLE border=0 cellSpacing=0 width=980 align=center>
                        <TBODY>
                        <TR>
                            <TH colSpan=6 align=center>
                                <DIV class=h1 align=center><STRONG>个人信用报告</STRONG> </DIV></TH></TR>
                        <TR>
                            <TD width=377 colSpan=2 align=left><STRONG>报告编号：</STRONG><STRONG class=p>{{report.reportinfo.reportSN}} </STRONG></TD>
                            <TD width=300 colSpan=2 align=left><STRONG>查询时间：</STRONG><STRONG class=p>{{report.reportinfo.querytime | dateFormat}} </STRONG></TD>
                            <TD width=300 colSpan=2 align=right><STRONG>报告时间：</STRONG><STRONG class=p>{{report.reportinfo.reporttime | dateFormat}} </STRONG></TD></TR></TBODY></TABLE><!-- json输入传入 -->
                    <TABLE border=0 cellSpacing=0 width=980 align=center>
                        <TBODY>
                        <TR>
                            <TD width=170 align=left><STRONG>姓名：</STRONG><STRONG class=p> {{report.personalinfo.name}} </STRONG></TD>
                            <TD width=167 align=left><STRONG>证件类型：</STRONG><STRONG class=p>{{report.personalinfo.IDtype}} </STRONG></TD>
                            <TD width=269 align=right><STRONG>证件号码：</STRONG><STRONG class=p>{{report.personalinfo.IDnumber}}</STRONG></TD>
                            <TD width=133 align=right><STRONG class=p>{{report.personalinfo.marital}}</STRONG></TD></TR></TBODY></TABLE><BR>
                    <TABLE border=1 cellSpacing=0 borderColor=#e5e1e1 cellPadding=0 width=980 align=center height=55>
                        <TBODY>
                        <TR borderColor=#a7dfed bgColor=#89cbe4>
                            <TD bgColor=#9e9bd2 height=35 borderColor=#e5e1e1 width="100%" align=center><STRONG style="COLOR: white" class=h1>&nbsp;信贷记录</STRONG> </TD></TR>
                        <TR borderColor=#a7dfed>
                            <TD bgColor=#f1edec height=18 borderColor=#e5e1e1 align=center><STRONG class=p>{{report.creditRecord.intro}}</STRONG> </TD></TR></TBODY></TABLE><BR>
                    <TABLE width="100%">
                        <TBODY>
                        <TR>
                            <TD width=782>
                                <P class=p><STRONG><SPAN class=h1>信息概要</SPAN> </STRONG></P>
                                <TABLE width="100%" align=center>
                                    <TBODY>
                                    <TR>
                                        <TD width="100%"></TD></TR></TBODY></TABLE></TD>
                            <TD vAlign=top rowSpan=4 width=186>
                                <DIV style="BACKGROUND-COLOR: #f1edec" class=p><SPAN style="BACKGROUND-COLOR: #f1edec">逾期记录可能影响对您的信用评价。 </SPAN></DIV><BR><BR>
                                <DIV style="BACKGROUND-COLOR: #f1edec" class=p><SPAN style="BACKGROUND-COLOR: #f1edec">购房贷款，包括个人住房贷款、个人商用房（包括商住两用）贷款和个人住房公积金贷款。</SPAN> </DIV><BR>
                                <DIV style="BACKGROUND-COLOR: #f1edec" class=p><SPAN>发生过逾期的信用卡账户，指曾经“未按时还最低还款额”的贷记卡账户和曾经“连续透支60天以上”的准贷记卡账户。 </SPAN></DIV></TD></TR>
                        <TR>
                            <TD width=782></TD></TR>
                        <TR>
                            <TD width=782>
                                <TABLE width="100%" align=center>
                                    <TBODY>
                                    <TR>
                                        <TD width="100%">
                                            <TABLE border=1 cellSpacing=0 borderColor=#e5e1e1 cellPadding=0 width="100%" height=155>
                                                <TBODY>
                                                <TR borderColor=#89cbe4>
                                                    <TD class=p borderColor=#e5e1e1 width=333 align=center>&nbsp; </TD>
                                                    <TD class=p borderColor=#e5e1e1 width=300 align=center>信用卡 </TD>
                                                    <TD class=p borderColor=#e5e1e1 width=200 align=center>购房贷款 </TD>
                                                    <TD class=p borderColor=#e5e1e1 width=200 align=center>其他贷款 </TD></TR>
                                                <TR>
                                                    <TD class=p align=left>&nbsp;账户数 </TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.creditCard.accountTotal}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.mortgage.accountTotal}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.otherLoan.accountTotal}}</TD></TR>
                                                <TR>
                                                    <TD class=p align=left>&nbsp;&nbsp;&nbsp;未结清/未销户账户数 </TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.creditCard.activeTotal}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.mortgage.activeTotal}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.otherLoan.activeTotal}}</TD></TR>
                                                <TR>
                                                    <TD class=p align=left>&nbsp;发生过逾期的账户数 </TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.creditCard.overdueTotal}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.mortgage.overdueTotal}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.otherLoan.overdueTotal}}</TD></TR>
                                                <TR>
                                                    <TD class=p align=left>&nbsp;&nbsp;&nbsp;发生过90天以上逾期的账户数 </TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.creditCard.overdue90Total}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.mortgage.overdue90Total}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.otherLoan.overdue90Total}}</TD></TR>
                                                <TR>
                                                    <TD class=p align=left>&nbsp;为他人担保笔数 </TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.creditCard.guarantee}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.mortgage.guarantee}}</TD>
                                                    <TD class=p align=center>{{report.creditRecord.summary.otherLoan.guarantee}}</TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD></TR>
                        <TR>
                            <TD width=782></TD></TR></TBODY></TABLE><SPAN><STRONG>信用卡 </STRONG></SPAN>
                    <OL class="p olstyle"><SPAN class=spanem><STRONG>发生过逾期的贷记卡账户明细如下：</STRONG> </SPAN><BR>
                        {{each report.creditRecord.detail.creditCard.overdueDetails}}
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: disc">{{$value}}</LI><!-- 这里是数组 -->
                        {{/each}}
                        <BR><SPAN><STRONG>从未逾期过的贷记卡及透支未超过60天的准贷记卡账户明细如下：</STRONG> </SPAN><BR>
                        {{each report.creditRecord.detail.creditCard.noOverdueDetails}}
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: disc">{{$value}}</LI><!--这里是数组-->
                        {{/each}}
                    </OL>
                    <BR>
                    <SPAN class=h1><STRONG>购房贷款</STRONG> </SPAN>
                    <OL class="p olstyle"><SPAN class=spanem><STRONG>逾期过的账户明细如下：</STRONG> </SPAN><BR>
                        {{each report.creditRecord.detail.mortgage.overdueDetails}}
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: disc">{{$value}}</LI>
                        {{/each}}
                    <BR>
                    <OL class="p olstyle"><SPAN class=spanem><STRONG>从未逾期过的账户明细如下：</STRONG> </SPAN><BR>
                        {{each report.creditRecord.detail.mortgage.overdueDetails}}
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: disc">{{$value}}</LI>
                        {{/each}}
                    <BR>
                    <SPAN class=h1><STRONG>其他贷款</STRONG> </SPAN>
                        <OL class="p olstyle"><SPAN class=spanem><STRONG>逾期过的账户明细如下：<BR></STRONG></SPAN><BR>
                            {{each report.creditRecord.detail.otherLoan.overdueDetails}}
                            <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: disc">{{$value}}</LI>
                            {{/each}}
                        </OL>
                        <BR>
                    <OL class="p olstyle"><SPAN class=spanem><STRONG>从未逾期过的账户明细如下：<BR></STRONG></SPAN><BR>
                        {{each report.creditRecord.detail.otherLoan.noOverdueDetails}}
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: disc">{{$value}}</LI>
                        {{/each}}
                    </OL>
                        <BR>
                    <TABLE border=1 cellSpacing=0 borderColor=#e5e1e1 cellPadding=0 width=980 align=center>
                        <TBODY>
                        <TR borderColor=#bfdfff bgColor=#a7dfed>
                            <TD style="COLOR: white" class=h1 bgColor=#9e9bd2 height=35 borderColor=#e5e1e1 width="100%" align=center>&nbsp;公共记录 </TD></TR>
                        <TR borderColor=#a7dfed bgColor=#bfdfff>
                            <TD bgColor=#f1edec height=18 borderColor=#e5e1e1 align=center><STRONG class=p>{{report.publicRecord.intro}}</STRONG></TD></TR></TBODY></TABLE><BR><BR><BR>
                    <TABLE border=1 cellSpacing=0 borderColor=#e5e1e1 cellPadding=0 width=980 align=center>
                        <TBODY>
                        <TR borderColor=#89cbe4>
                            <TD style="COLOR: white" class=h1 bgColor=#9e9bd2 height=35 borderColor=#e5e1e1 width="100%" align=center>查询记录 </TD></TR>
                        <TR borderColor=#a7dfed bgColor=#bfdfff>
                            <TD bgColor=#f1edec height=18 borderColor=#e5e1e1 align=center><STRONG class=p>{{report.queryRecord.intro}}</STRONG> </TD></TR></TBODY></TABLE>
                    <TABLE style="MARGIN-TOP: 12px" border=0 cellSpacing=0 width=980 align=center>
                        <TBODY>
                        <TR>
                            <TD vAlign=middle colSpan=4 align=center><STRONG class=p>机构查询记录明细</STRONG> </TD></TR>
                        <TR>
                            <TD class=p colSpan=4>------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</TD></TR>
                        <TR align=center>
                            <TD class=p width=100><STRONG>编号</STRONG> </TD>
                            <TD class=p width=200><STRONG>查询日期</STRONG> </TD>
                            <TD class=p width=380><STRONG>查询操作员</STRONG> </TD>
                            <TD class=p width=300><STRONG>查询原因</STRONG> </TD></TR>
                        <!-- queryRecord.detail.Organization 是一个数组  -->
                        {{each report.queryRecord.detail.Organization}}
                        <TR align=center>
                            <TD class=p>{{$index}}</TD>
                            <TD class=p>{{$value.date}} </TD>
                            <TD class=p>{{$value.operator}}</TD>
                            <TD class=p>{{$value.reason}}</TD>
                        </TR>
                        {{/each}}
                            <TD class=p colSpan=4>------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</TD></TR></TBODY></TABLE>
                    <TABLE border=0 cellSpacing=0 width=980 align=center>
                        <TBODY>
                        <TR>
                            <TD height=18 borderColor=#e5e1e1 colSpan=4 align=center><STRONG class=p>个人查询记录明细</STRONG> </TD></TR>
                        <TR>
                            <TD class=p colSpan=4>------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</TD></TR>
                        <TR align=center>
                            <TD class=p width=100><STRONG>编号</STRONG> </TD>
                            <TD class=p width=200><STRONG>查询日期</STRONG> </TD>
                            <TD class=p width=380><STRONG>查询操作员</STRONG> </TD>
                            <TD class=p width=300><STRONG>查询原因</STRONG> </TD></TR>
                        <!-- Individual 是一个数组 -->
                        {{each report.queryRecord.detail.Individual}}
                        <TR align=center>
                            <TD class=p>{{$index}}</TD>
                            <TD class=p>{{$value.date}}</TD>
                            <TD class=p>{{$value.operator}}</TD>
                            <TD class=p>{{$value.reason}}</TD></TR>
                        {{/each}}
                            <TD class=p colSpan=4>------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</TD></TR></TBODY></TABLE><BR>
                    <TABLE border=0 cellSpacing=0 width=980 align=center>
                        <TBODY>
                        <TR>
                            <TD width=980 align=center><STRONG class=h1>说&nbsp;&nbsp;明</STRONG> </TD></TR></TBODY></TABLE>
                    <OL>
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: decimal" class=p>除查询记录外，本报告中的信息是依据截至报告时间个人征信系统记录的信息生成，征信中心不确保其真实性和准确性，但承诺在信息汇总、加工、整合的全过程中保持客观、中立的地位。
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: decimal" class=p>本报告仅包含可能影响您信用评价的主要信息，如需获取您在个人征信系统中更详细的记录，请到当地信用报告查询网点查询。信用报告查询网点的具体地址及联系方式可访问征信中心门户网站（www.pbccrc.org.cn）查询。
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: decimal" class=p>您有权对本报告中的内容提出异议。如有异议，可联系数据提供单位，也可到当地信用报告查询网点提出异议申请。
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: decimal" class=p>本报告仅供您了解自己的信用状况，请妥善保管。因保管不当造成个人隐私泄露的，征信中心将不承担相关责任。
                        <LI style="LIST-STYLE-POSITION: outside; LIST-STYLE-TYPE: decimal" class=p>更多咨询，请致电全国客户服务热线400-810-8866。 </LI></OL>
            <TR>
                <TD>&nbsp; </TD></TR></TBODY></TABLE></DIV></DIV>
<SCRIPT language=JavaScript>
    self.moveTo(0,0)
    self.resizeTo(screen.availWidth,screen.availHeight)
</SCRIPT>
