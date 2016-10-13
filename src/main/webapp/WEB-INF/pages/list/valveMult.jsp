<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 12/4/14
  Time: 10:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp" />
<html>
<style type="text/css">
<!--
.src {
    /*overflow: scroll;   *//* スクロール表示 */
    overflow-x: auto;
    overflow-y: auto;
    width: 1168px;
    height: 500px;
}
/*スクロール用*/
thead.scrollHead,tbody.scrollBody{
    /*display:block;*/
}
tbody.scrollBody{
    overflow-y:scroll;
    /*overflow-x: auto;*/
    /*width: 1168px;*/
    height:500px;
}

</style>
<body class="skin-black">
<!-- header logo: style can be found in header.less -->
<c:import url="../htmlframe/headerFrame.jsp"/>
<div class="container">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <%--<h1>--%>
            <%--弁検索--%>
        <%--</h1>--%>
        <%--<ol class="breadcrumb">--%>
            <%--<li><i class="fa fa-dashboard"></i> Index</li>--%>
        <%--</ol>--%>
    </section>
    <hr/>
    <!-- Main content -->
    <section class="content">
        <%--<div class="row">--%>
            <%--<!-- collection -->--%>
            <%--<div class="col-md-4">--%>
                <%--<div class="btn-group" role="group">--%>
                    <%--<a href="/valdacHost/list/valveMult" class="btn btn-default bg-yellow active" onclick="return saveSelectedForValve()">複合検索</a>--%>
                    <%--<a href="/valdacHost/list/valve" class="btn btn-default" onclick="return saveSelectedForValve()">弁検索</a>--%>
                    <%--<a href="/valdacHost/list/kikiSearch" class="btn btn-default" onclick="return saveSelectedForValve()">機器検索</a>--%>
                    <%--<a href="/valdacHost/list/buhinSearch" class="btn btn-default" onclick="return saveSelectedForValve()">部品検索</a>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div></br></br>--%>


        <div class="row">
        <div class="panel panel-success">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-md-10">
                        <h4><i class="fa fa-download"></i> ファイルダウンロード </h4>
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-2">
                        <form name="kikisysDL" action="/valdacHost/print/printKikisys"  method="post" onsubmit="checkKikisysIDListNull();return false;">
                            <input type="hidden" name="idList" id="idList" value="" />
                            <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="部品リスト印刷"/>
                        </form>
                    </div>
                    <div class="col-md-2">
                        <form name="kenan" action="/valdacHost/print/printKenanForKikisysList"  method="post" onsubmit="checkKenan();return false;">
                            <input type="hidden" name="idList" id="idListKenan" value="" />
                            <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="懸案リスト印刷"/><br>
                            <input type="hidden" name="kenanFlgDL" id="kenanFlgDL" value="" />
                            <input type="radio" name="valve-kenan-radio" class="valve-kenan-radio" value="true" checked> 全部
                            <input type="radio" name="valve-kenan-radio" class="valve-kenan-radio" value="false"> 未対応のみ
                        </form>
                    </div>
                    <div class="col-md-2">
                        <form  name="GPListDL" action="/valdacHost/print/printGPForKikisysList"  method="post" onsubmit="checkGPListNull();return false;">
                            <input type="hidden" name="idList" id="idListGP" value="" />
                            <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="GP仕様印刷"/>
                        </form>
                    </div>
                    <div class="col-md-2">
                        <form  name="DaityoListDL" action="/valdacHost/print/printDaityoForKikisysList"  method="post" onsubmit="checkDaityoListNull();return false;">
                            <input type="hidden" name="idList" id="idListDaityo" value="" />
                            <input type="hidden" name="companyLocation" id="companyLocation" value="" />
                            <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="台帳履歴印刷"/>
                        </form>
                    </div>
                    <div class="col-md-2">
                        <form  name="TokuBetuKakoKirokuListDL" action="/valdacHost/print/printTokuBetuKakoKirokuForKikisysList"  method="post" onsubmit="checkTokuBetuKakoKirokuListNull();return false;">
                            <input type="hidden" name="idListTokuBetuKako" id="idListTokuBetuKako" value="" />
                            <input type="hidden" name="koujiId" id="koujiId" value="" />
                            <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="特別加工記録印刷"/>
                        </form>
                    </div>
                    <div class="col-md-2">
                        <form  name="SijisyoListDL" action="/valdacHost/print/printSijisyoForKikisysList"  method="post" onsubmit="checkSijisyoListNull();return false;">
                            <input type="hidden" name="idListSijisyo" id="idListSijisyo" value="" />
                            <input type="hidden" name="koujiIdSijisyo" id="koujiIdSijisyo" value="" />
                            <input type="submit" class="btn btn-default" onclick="submitPrintForm()" value="指示書印刷"/>
                        </form>
                    </div>
                </div><br><br>
                <div class="row">
                    <div class="col-md-2">
                        <div class="btn-group">
                            <div class="input-group">
                                <input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter" value="${filterValve}">
                                <div class="input-group-btn">
                                    <button class="btn btn-sm btn-default" id="table_search_btn" onclick="filterPage();"><i class="fa fa-search"></i></button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="btn-group">
                            <div class="input-group">
                                <button class="btn btn-sm btn-default selected_search_btn" id="selected_search_btn" onclick="selectedValveList()">選択された弁のみ表示</button>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="btn-group">
                            <div class="input-group">
                                <button class="btn btn-sm btn-default" id="all_search_btn" onclick="allValveList()">全部弁表示</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-10">
                        <div id="loading" align="center"><img src="/valdacHost/img/loading.gif"></div>
                    </div>
                </div></br>
            </div>
        </div>
        </div></br>
        <div class="row">
            <div class="panel panel-success">
                <div class="panel-heading" id="searchForValveHeader">
                    <div class="row">
                        <div class="col-md-10">
                            <h4><i class="glyphicon glyphicon-chevron-up" id="iClass"></i> 検索項目 </h4>
                        </div>
                    </div>
                </div>
                <div class="panel-body" id="searchForValve" style="display:block;">
                    <div class="row">
                        <div class="col-md-14">
                            <form action="/valdacHost/item/valveMultSearchForSeikak" method="post" class="sidebar-form"  name="searchForValve" onsubmit="return checkValveSearchForSeikak();">
                                <div class="box-body">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <h4 style="background-color:#EDF7FF;"> <span style="color: red;">弁項目</span></h4>
                                            </div>
                                        </div><br>
                                        <div class="row">
                                            <div class="col-md-1">
                                                <span STYLE="color: red;">設置プラント(必須)</span>
                                            </div>
                                            <div class="col-md-5" >
                                                <div class="input-group">
                                                    <select id="locationName" name="locationName" class="form-control" value="">
                                                        <option>全部会社名</option>
                                                        <c:forEach items="${nameList}" var="name">
                                                            <option>${name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                        </div><br>
                                        <div class="row">
                                            <div class="col-md-1">
                                                弁番号
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="VNo" name="VNo" class="form-control" placeholder="弁番号" value="${valveMultSearchForSeikak.vNo}">
                                            </div>
                                            <div class="col-md-1">
                                                弁名称
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="BenMeisyo" name="BenMeisyo" class="form-control" placeholder="弁名称" value="${valveMultSearchForSeikak.benMeisyo}">
                                            </div>
                                            <div class="col-md-1">
                                                設置設備
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="SetSetubi" name="SetSetubi" class="form-control" placeholder="設置設備" value="${valveMultSearchForSeikak.setSetubi}">
                                            </div>

                                        </div><br>

                                        <div class="row">
                                            <div class="col-md-1">
                                                設置機器
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="SetKiki" name="SetKiki" class="form-control" placeholder="設置機器" value="${valveMultSearchForSeikak.setKiki}">
                                            </div>
                                            <div class="col-md-1">
                                            設置場所
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="SetBasyo" name="SetBasyo" class="form-control" placeholder="設置場所" value="${valveMultSearchForSeikak.setBasyo}">
                                            </div>
                                            <div class="col-md-1">
                                                ICS番号
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="ics" name="ics" class="form-control ics" placeholder="ICS番号" value="${valveMultSearchForSeikak.ics}">
                                            </div>
                                        </div><br>

                                        <div class="row">
                                            <div class="col-md-1">
                                                流体
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="ryutaiRyaku" name="ryutaiRyaku" class="form-control ryutai" placeholder="略称" value="${valveMultSearchForSeikak.ryutaiRyaku}">
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="ryutai" name="ryutai" class="form-control ryutai" placeholder="規格記号" value="${valveMultSearchForSeikak.ryutai}">
                                            </div>
                                            <div class="col-md-1">
                                                形式
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="keisikiRyaku" name="keisikiRyaku" class="form-control keisiki" placeholder="略称" value="${valveMultSearchForSeikak.keisikiRyaku}">
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="keisiki" name="keisiki" class="form-control keisiki" placeholder="規格記号" value="${valveMultSearchForSeikak.keisiki}">
                                            </div>
                                        </div><br>

                                        <div class="row">
                                            <div class="col-md-1">
                                                駆動方式
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="sousaRyaku" name="sousaRyaku" class="form-control sousa" placeholder="略称" value="${valveMultSearchForSeikak.sousaRyaku}">
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="sousa" name="sousa" class="form-control sousa" placeholder="規格記号" value="${valveMultSearchForSeikak.sousa}">
                                            </div>
                                            <div class="col-md-1">
                                                クラス
                                            </div>
                                            <div class="col-md-2" style="display:none">
                                                <input type="text" id="classRyaku" name="classRyaku" class="form-control classType" placeholder="略称" value="${valveMultSearchForSeikak.classRyaku}">
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="classType" name="classType" class="form-control classType" placeholder="規格記号" value="${valveMultSearchForSeikak.classType}">
                                            </div>
                                        </div><br>

                                        <div class="row">
                                            <div class="col-md-1">
                                                呼び径
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="yobikeiRyaku" name="yobikeiRyaku" class="form-control yobikei" placeholder="略称" value="${valveMultSearchForSeikak.yobikeiRyaku}">
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="yobikei" name="yobikei" class="form-control yobikei" placeholder="規格記号" value="${valveMultSearchForSeikak.yobikei}">
                                            </div>
                                            <div class="col-md-1">
                                                接続入口
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="szHouRyaku" name="szHouRyaku" class="form-control szHou" placeholder="略称" value="${valveMultSearchForSeikak.szHouRyaku}">
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="szHou" name="szHou" class="form-control szHou" placeholder="規格記号" value="${valveMultSearchForSeikak.szHou}">
                                            </div>
                                        </div><br>

                                        <div class="row">
                                            <div class="col-md-1">
                                                接続規格
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="szKikaku" name="szKikaku" class="form-control szKikaku" placeholder="接続規格" value="${valveMultSearchForSeikak.szKikaku}">
                                            </div>
                                            <div class="col-md-2">
                                            </div>
                                            <div class="col-md-1">
                                                本体材質
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="zaisituRyaku" name="zaisituRyaku" class="form-control zaisitu" placeholder="略称" value="${valveMultSearchForSeikak.zaisituRyaku}">
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" id="zaisitu" name="zaisitu" class="form-control zaisitu" placeholder="規格記号" value="${valveMultSearchForSeikak.zaisitu}">
                                            </div>
                                        </div><br>

                                        <div class="row">
                                            <div class="col-md-12">
                                                <h4  style="background-color:#EDF7FF;"><span style="color: red;">機器項目</span></h4>
                                            </div>
                                        </div><br>

                                    <div class="row">
                                        <div class="col-md-1">
                                            型式番号
                                        </div>
                                        <div class="col-md-2">
                                            <input type="text" id="katasikiNo" name="katasikiNo" class="form-control" value="${valveMultSearchForSeikak.katasikiNo}" />
                                        </div>
                                        <div class="col-md-1">
                                            メーカー
                                        </div>
                                        <div class="col-md-1">
                                            <input type="text" id="makerRyaku" name="makerRyaku" class="form-control maker" placeholder="略称" value="${valveMultSearchForSeikak.makerRyaku}" />
                                        </div>
                                        <div class="col-md-2">
                                            <input type="text" id="maker" name="maker" class="form-control maker" placeholder="メーカー" value="${valveMultSearchForSeikak.maker}" />
                                        </div>
                                        <div class="col-md-1">
                                            主管係
                                        </div>
                                        <div class="col-md-3">
                                            <%--<input type="text" id="syukan" name="syukan" class="form-control syukan" value="${valveMultSearchForSeikak.syukan}" />--%>
                                            <select id="syukan" name="syukan" class="form-control syukan" value="">
                                                <option></option>
                                                <c:forEach items="${syukanList}" var="syukan">
                                                    <option>${syukan.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div><br>


                                    <div class="row">
                                        <div class="col-md-12">
                                            <h4 style="background-color:#EDF7FF;"><span style="color: red;">部品項目</span></h4>
                                        </div>
                                    </div><br>

                                    <div class="row">
                                        <div class="col-md-1">
                                            部品名
                                        </div>
                                        <div class="col-md-2">
                                            <input type="text" id="buhinMei" name="buhinMei" class="form-control" value="${valveMultSearchForSeikak.buhinMei}" />
                                        </div>
                                        <div class="col-md-1">
                                            標準仕様
                                        </div>
                                        <div class="col-md-2">
                                            <input type="text" id="hyojunSiyou" name="hyojunSiyou" class="form-control" value="${valveMultSearchForSeikak.hyojunSiyou}" />
                                        </div>
                                        <div class="col-md-1">
                                            概略寸法
                                        </div>
                                        <div class="col-md-2">
                                            <input type="text" id="sunpou" name="sunpou" class="form-control" value="${valveMultSearchForSeikak.sunpou}" />
                                        </div>
                                    </div><br>

                                    <div class="row">
                                        <div class="col-md-1">
                                            メーカー
                                        </div>
                                        <div class="col-md-2">
                                            <input type="text" id="buhinMakerRyaku" name="buhinMakerRyaku" class="form-control maker" placeholder="略称" value="${valveMultSearchForSeikak.buhinMakerRyaku}" />
                                        </div>
                                        <div class="col-md-2">
                                            <input type="text" id="buhinMaker" name="buhinMaker" class="form-control maker" placeholder="メーカー" value="${valveMultSearchForSeikak.buhinMaker}" />
                                        </div>
                                    </div></br>
                                    <div class="box-body clearfix">
                                        <div class="form-group">
                                            <div class="col-md-6">
                                            </div>
                                            <div class="col-md-1">
                                            </div>
                                            <div class="col-md-2">
                                                <span id="valve_num" class="valve_num pull-right" STYLE="font-size: 16px;">ヒット弁 ${valveMultSearchitemNum} 台</span>
                                            </div>
                                            <div class="col-md-2">
                                                <button class="btn btn-danger">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-search"></i> この条件で検索&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                </button>
                                            </div>
                                        </div>

                                    </div>
                                </div></br></br>
                            </form>
                        </div>
                    </div></br>
                </div>
            </div>
        </div></br>

        <div class="row">
        <div class="src">
        <div>
            <!-- collection -->
            <div class="tab-content box-body">
                    <table class="table table-hover select-table table-bordered" id="valve-table" >
                        <thead>
                        <tr>
                            <th nowrap><input type="checkbox" class="headCheckbox" onclick="selectAllItem()">(全選)</th>
                            <th nowrap>詳細</th>
                            <th nowrap>懸案</th>
                            <th nowrap>弁番号</th>
                            <th nowrap>設置プラント</th>
                            <th nowrap>弁名称</th>
                            <th nowrap>設置設備</th>
                            <th nowrap>設置機器</th>
                            <th nowrap>設置場所</th>
                            <th nowrap>系統</th>
                            <th nowrap>圧力</th>
                            <th nowrap>圧力単位</th>
                            <th nowrap>温度</th>
                            <th nowrap>流体略称</th>
                            <th nowrap>流体</th>
                            <th nowrap>形式略称</th>
                            <th nowrap>形式</th>
                            <th nowrap>駆動方式略称</th>
                            <th nowrap>駆動方式</th>
                            <%--<th nowrap>クラス略称</th>--%>
                            <th nowrap>クラス</th>
                            <th nowrap>呼び径略称</th>
                            <th nowrap>呼び径</th>
                            <th nowrap>接続入口略称</th>
                            <th nowrap>接続入口</th>
                            <th nowrap>接続規格</th>
                            <th nowrap>本体材質略称</th>
                            <th nowrap>本体材質</th>
                            <th nowrap>更新日付</th>
                            <th nowrap>詳細</th>
                        </tr>
                        </thead>
                        <tbody id="valveList">
                            <c:forEach items="${valveMultResultsForKikisys}" var="tempvalve">
                                <tr class="valve-item" id="select-${tempvalve.kikiSysId}">
                                    <td onclick="setSelectedColor(this)"><input type="checkbox" class="checkbox"  value="${tempvalve.kikiSysId}"></td>
                                    <td nowrap><a class="btn btn-primary btn-sm operation-button-btn" href="/valdacHost/item/${tempvalve.kikiSysId}/${kikiOrBenFlg}/valve" onclick="return saveSelectedForValve()"><i class="fa fa-pencil">詳細</i></a></td>
                                    <td nowrap><c:if test="${(tempvalve.kenanFlg eq '1')}">〇</c:if></td>
                                    <td nowrap>${tempvalve.vNo}</td>
                                    <td nowrap>${tempvalve.locationName}</td>
                                    <td nowrap>${tempvalve.benMeisyo}</td>
                                    <td nowrap>${tempvalve.setSetubi}</td>
                                    <td nowrap>${tempvalve.setKiki}</td>
                                    <td nowrap>${tempvalve.setBasyo}</td>
                                    <td nowrap>${tempvalve.keitou}</td>
                                    <td nowrap>${tempvalve.aturyokuMax}</td>
                                    <td nowrap>${tempvalve.tani}</td>
                                    <td nowrap>${tempvalve.ondoMax}</td>
                                    <td nowrap>${tempvalve.ryutaiRyaku}</td>
                                    <td nowrap>${tempvalve.ryutai}</td>
                                    <td nowrap>${tempvalve.keisikiRyaku}</td>
                                    <td nowrap>${tempvalve.keisiki}</td>
                                    <td nowrap>${tempvalve.sousaRyaku}</td>
                                    <td nowrap>${tempvalve.sousa}</td>
                                    <%--<td nowrap>${tempvalve.classRyaku}</td>--%>
                                    <td nowrap>${tempvalve.classType}</td>
                                    <td nowrap>${tempvalve.yobikeiRyaku}</td>
                                    <td nowrap>${tempvalve.yobikei}</td>
                                    <td nowrap>${tempvalve.szHouRyaku}</td>
                                    <td nowrap>${tempvalve.szHou}</td>
                                    <td nowrap>${tempvalve.szKikaku}</td>
                                    <td nowrap>${tempvalve.zaisituRyaku}</td>
                                    <td nowrap>${tempvalve.zaisitu}</td>
                                    <td nowrap>${tempvalve.updDate}</td>
                                    <td nowrap><a class="btn btn-primary btn-sm operation-button-btn" href="/valdacHost/item/${tempvalve.kikiSysId}/${kikiOrBenFlg}/valve" onclick="return saveSelectedForValve()"><i class="fa fa-pencil">詳細</i></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
            </div>
        </div>
        </div><!-- ./src -->
        </div><!-- ./row -->
    </section><!-- /.content -->
</div><!-- ./wrapper -->

<script type="text/javascript">
    function searchDataTd(obj){
        if(obj.value.length < 1){
            $(".data-tr").show();
        }
        var dataTr = $(".data-tr");
        var keyword = new String(obj.value);
        for(var i = 0;i<dataTr.length;i++){
            $(dataTr[i]).hide();
            var dataTd = $(dataTr[i]).find(".data-td");
            for(var j = 0;j<dataTd.length;j++){
                var htmlData = new String(dataTd[j].innerHTML);
                if(htmlData.indexOf(keyword) > -1){
                    $(dataTr[i]).show();
                    break;
                }
            }
        }
    }

    //弁精確検索
    $("div#searchForValveHeader").click(function(){
        var tmp= document.getElementById("searchForValve").style.display;
        if(tmp=="block"){
            //非表示場合
            document.getElementById("searchForValve").style.display="none";
            document.getElementById("iClass").className="glyphicon glyphicon-chevron-down";
        }else{
            //表示場合
            document.getElementById("searchForValve").style.display="block";
            document.getElementById("iClass").className="glyphicon glyphicon-chevron-up";
        }
    } );

    function checkKenan(){
        //弁ラベル　懸案全部か？未対応のみか？
        $('.valve-kenan-radio:checked').each(function(){
            $('#kenanFlgDL').val($(this)[0].value);
        });

        var idList = $("#idListKenan").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            $.post("/valdacHost/print/checkKenanForKikisysList",{"idList":idList}, function(data){
                var isKenan = JSON.parse(data);
                if(isKenan) {
                    document.kenan.submit();
                }else{
                    alert("懸案はありません。");
                }
            });
        }
    }
    //IdListは空場合はメッセージを表示する
    function  checkKikisysIDListNull() {
        var idList = $("#idList").val();
        console.log("idList="+idList);
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.kikisysDL.submit();
        }
    }
    //IdListは空場合はメッセージを表示する
    function  checkGPListNull() {
        var idList = $("#idList").val();
        console.log("idList="+idList);
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.GPListDL.submit();
        }
    }

    //IdListは空場合はメッセージを表示する
    function  checkDaityoListNull() {
        var idList = $("#idListDaityo").val();
        console.log("idList="+idList);
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.DaityoListDL.submit();
        }
    }
    //IdListは空場合はメッセージを表示する
    function checkTokuBetuKakoKirokuListNull(){
        var idList = $("#idListTokuBetuKako").val();
        var idListArray=idList.split(",");
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else if(idListArray.length>11){
            alert("弁件数を最大１０個にしてください。");
        }else{
            document.TokuBetuKakoKirokuListDL.submit();
        }
    }
    //IdListは空場合はメッセージを表示する　この機能を破棄した
    function checkSijisyoListNull(){
        var idList = $("#idListSijisyo").val();
        var idListArray=idList.split(",");
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else if(idListArray.length>11){
            alert("弁件数を最大１０個にしてください。");
        }else{
            document.SijisyoListDL.submit();
        }
    }

    function selectAllItem(){
        if(!$('.headCheckbox').prop('checked')){
            $('.select-table').find('.checkbox').each(function(){
                var checkbox = $(this)[0];
                if($(checkbox).prop('checked')== true) {
                    $(checkbox).prop('checked', false);
                }
            });
            $('.select-table').find('.valve-item').each(function(){
                var tr = $(this)[0];
                if($(tr).hasClass('bg-light-blue-gradient')){
                    $(tr).removeClass("bg-light-blue-gradient");
                }
            });
        } else {
            $('.select-table').find('.checkbox').each(function(){
                var checkbox = $(this)[0];
                if($(checkbox).prop('checked')== true) {
                }
                else {
                    $(checkbox).prop('checked', true);
                }
            });
            $('.select-table').find('.valve-item').each(function(){
                var tr = $(this)[0];
                if($(tr).hasClass('bg-light-blue-gradient')){
                }
                else {
                    $(tr).addClass("bg-light-blue-gradient");
                }
            });
        }
    }
</script>

<script type="text/javascript">

     //filter関数
     filter("${filterValve}");

     //選択された弁を設定する
     setSelectedForValve();

     //弁選択
     function setSelectedColor(obj){
         var td = obj.currentTarget;
         var tr=$(obj).parent();
         var selected = $(tr).find('.checkbox');
         var checkbox = selected[0];

         //checkboxのクリックイベント
//         var checkbox=obj.currentTarget;
//         var tr = document.getElementById("select-"+obj.value);
         if($(checkbox).prop("checked") == true){
             $(checkbox).prop('checked', false);
             $(tr).removeClass("bg-light-blue-gradient");
         } else {
             $(checkbox).prop('checked', true);
             $(tr).addClass("bg-light-blue-gradient");

         }
     }


     //Filter
     function filterPage(){
         var keyword = $("#table_search").val();
         keyword = keyword.toLowerCase();
         keyword=keyword.trim();
         keyword=keyword.toOneByteAlphaNumeric();//英数字　全角英数を半角英数に変換
         keyword=tozenkaku(keyword);//カタカナ　全角英数を半角英数に変換

         var trs = $(".select-table tbody tr");
         for(var i = 0;i<trs.length;i++){
             $(trs[i]).hide();
             var tds = $(trs[i]).find("td");

             for(var j = 1;j<(tds.length-1);j++){
                 var tmpHtml = new String(tds[j].innerHTML);
                 tmpHtml=tmpHtml.toLowerCase();
                 tmpHtml=tmpHtml.toOneByteAlphaNumeric();//全角英数を半角英数に変換
                 tmpHtml=tozenkaku(tmpHtml);//カタカナ　全角英数を半角英数に変換

                 if(tmpHtml.match(keyword)){
                     $(trs[i]).show();
                     break;
                 }
             }
             //selected状態は表示されるように
             var selected = $(trs[i]).find('.checkbox');
             var checkbox = selected[0];
             if($(checkbox).prop("checked") == true){
                 $(trs[i]).show();
             }
         }
         //sessionに保存する
         if(keyword.length<1){
             keyword=""
         }

         //sessionに保存
         $.get("/valdacHost/item/valveFilter",{"filterKeyword":keyword},function(data){
             return true;
         });
     };
     //Filter
     function filter(val){
         var keyword=val;
         if(val=="keyword"){
             keyword="";
         }
         document.getElementById("table_search").value=keyword;

         var trs = $(".select-table tbody tr");
         for(var i = 0;i<trs.length;i++){
             $(trs[i]).hide();
             var tds = $(trs[i]).find("td");

             for(var j = 1;j<(tds.length-1);j++){
                 var tmpHtml = new String(tds[j].innerHTML);
                 tmpHtml=tmpHtml.toLowerCase();
                 tmpHtml=tmpHtml.toOneByteAlphaNumeric();//全角英数を半角英数に変換
                 tmpHtml=tozenkaku(tmpHtml);//カタカナ　全角英数を半角英数に変換

                 if(tmpHtml.match(keyword)){
                     $(trs[i]).show();
                     break;
                 }
             }
             //selected状態は表示されるように
             var selected = $(trs[i]).find('.checkbox');
             var checkbox = selected[0];
             if($(checkbox).prop("checked") == true){
                 $(trs[i]).show();
             }
         }
     };

    //選択された弁リストを表示される　Filter
    function selectedValveList(){
        var keyword = $("#table_search").val();
        keyword = keyword.toLowerCase();
        keyword=keyword.trim();

        var trs = $(".select-table tbody tr");
        for(var i = 0;i<trs.length;i++){
            $(trs[i]).hide();
            var tds = $(trs[i]).find("td");
            //selected状態は表示されるように
            var selected = $(trs[i]).find('.checkbox');
            var checkbox = selected[0];
            if($(checkbox).prop("checked") == true){
                $(trs[i]).show();
            }
        }
    }

    //すべての弁を表示される　Filter
    function allValveList(){
        var trs = $(".select-table tbody tr");
        for(var i = 0;i<trs.length;i++){
            $(trs[i]).show();
        }
    }

    function submitPrintForm(){
        //対象弁を記録
        var idList = "";
        $('.checkbox:checked').each(function(){
            idList = idList + $(this)[0].value + ',';
        });
        $('#idList').val(idList);
        $('#idListKenan').val(idList);
        $('#idListGP').val(idList);
        $('#idListDaityo').val(idList);
        $('#idListTokuBetuKako').val(idList);
        $('#idListSijisyo').val(idList);
        //会社名を記録
        var location = $("#currentLocation").val();
        if(location == "全部会社名"){
            location = "";
        }
        $('#companyLocation').val(location);
    }
    $(document).ready(function(){
        window.onload = function(){
            $(function() {
                //ページの読み込みが完了したのでアニメーションはフェードアウトさせる
                $("#loading").fadeOut();
            });
        }

        // location selected  条件付け検索 に設定する設定
        var locationFirst2="${valveMultSearchForSeikak.locationName}";
        var objname2=document.getElementById("locationName");
        checkSelect(objname2,locationFirst2);

        //syukan selected 条件付け検索 に設定する設定
        var syukan2="${valveMultSearchForSeikak.syukan}";
        var objsyukan=document.getElementById("syukan");
        checkSelect(objsyukan,syukan2);

    })
    function checkSelect(obj,val){
        for(var i=0;i<obj.length;i++){
            if(obj[i].value==val){
                obj[i].selected=true;
                break;
            }
        }
    }
     //検索キーワードに空チェック
    function checkValveSearchForSeikak(){
        if(($("#locationName").val())=="全部会社名"){
            alert("設置プラントにキーワードをご入力ください");
            return false;
        }else{
            return true;
        }
    }
     //選択された弁を記録する
        function saveSelectedForValve(){
            //対象弁を記録
            var idList = "";
            $('.checkbox:checked').each(function(){
                idList = idList + $(this)[0].value + ',';
            });
            //Filter sessionに保存
            $.get("/valdacHost/item/saveSelectedForValve",{"idList":idList,"type":"4"},function(data){
                return true;
        });
}
     //選択された弁を再秒化する
     function setSelectedForValve(){
         var tmp = "${saveMultSelectedForValve}";
         if(tmp.length>0){
             var tmp2=tmp.split(",");
             for(var i=0;i<tmp2.length-1;i++){
                 var id="select-"+tmp2[i];
                 document.getElementById(id).className="valve-item bg-light-blue-gradient";

                 //checkout
                 var tr = document.getElementById(id);
                 var selected = $(tr).find('.checkbox');
                 var checkbox = selected[0];
                 $(checkbox).prop('checked', true);
             }
         }
     }

</script>

<c:import url="../htmlframe/footerFrame.jsp" />

</body>
</html>

