<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 11/14/14
  Time: 4:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp" />
<html>
<head>
    <title>工事支援</title>
</head>
<body class="skin-black">
    <!-- header logo: style can be found in header.less -->
    <c:import url="../htmlframe/headerFrame.jsp"/>
    <div class="container">
        <!-- Left side column. contains the logo and sidebar -->

        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                <i class="glyphicon glyphicon-wrench"> ${kouji.kjMeisyo}</i>
                <c:choose>
                    <c:when test="${kouji.status == '企画中'}">
                        <span class="label label-warning">${kouji.status}</span>
                    </c:when>
                    <c:when test="${kouji.status == '施工中'}">
                        <span class="label label-warning">${kouji.status}</span>
                    </c:when>
                    <c:when test="${kouji.status == '評価入力中'}">
                        <span class="label label-warning">${kouji.status}</span>
                    </c:when>
                    <c:when test="${kouji.status == '工事完了'}">
                        <span class="label label-success">${kouji.status}</span>
                    </c:when>

                </c:choose>

            </h1>
            <ol class="breadcrumb" style="font-size:20pt;">
                <li><i class="fa fa-dashboard"></i> <a href="/valdacHost/">工事一覧へ</a></li>
            </ol>
        </section>
        <hr/>

        <section class="content">

            <div class="row">
                <div class="col-md-11">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">工事情報</h3>
                        </div>

                        <form action="/valdacHost/kouji/updateKouji">
                            <input type="hidden" name="id" value="${kouji.id}"/>
                            <div class="panel-body">
                                <div class="row form-group">
                                    <div class="col-md-3">
                                        <input type="text" name="kjNo" class="form-control" value="${kouji.kjNo}" placeholder="工事番号" readonly="readonly"/>
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" name="kjMeisyo" class="form-control" value="${kouji.kjMeisyo}" placeholder="工事名称" readonly="readonly"/>
                                    </div>
                                </div>

                                <div class="row form-group">
                                    <div class="col-md-2">
                                        <input type="text" name="person" class="form-control" value="${kouji.person}" placeholder="責任者" readonly="readonly"/>
                                    </div>

                                    <div class="col-md-8">
                                        <div class="input-daterange input-group"  style="width:100%">
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <input type="text" class="form-control" name="bgnYmd" id="bgnYmd" placeholder="開始日付" value="${kouji.bgnYmd}" readonly="readonly">
                                            <span class="input-group-addon">〜</span>
                                            <input type="text" class="form-control" name="endYmd" placeholder="終了日付" value="${kouji.endYmd}" readonly="readonly">
                                        </div>
                                    </div>

                                    <div class="col-md-2">
                                        <input type="text" name="nendo" class="form-control" value="${kouji.nendo}" placeholder="年度" readonly="readonly"/>
                                    </div>
                                </div>

                                <div class="row form-group">
                                    <div class="col-md-4">
                                        <input type="text" name="location" class="form-control" readonly="readonly" value="${kouji.location}" readonly="readonly"/>
                                    </div>
                                    <div class="col-md-4">
                                        <input type="text" name="kjKbn" class="form-control" readonly="readonly" value="${kouji.kjKbn}" readonly="readonly"/>
                                    </div>
                                    <div class="col-md-2">
                                        <input type="text" name="nextYmd" class="form-control" value="${kouji.nextYmd}" placeholder="今後日付" readonly="readonly"/>
                                    </div>
                                 </div>
                                <div class="row form-group">
                                    <div class="col-md-4">
                                        <input type="text" name="syukan" class="form-control" value="${kouji.syukan}" placeholder="主管係" readonly="readonly"/>
                                    </div>
                                    <div class="col-md-4">
                                        <input type="text" name="gyosyaRyakuA" class="form-control" value="${kouji.gyosyaRyakuA}" placeholder="点検業者" readonly="readonly"/>
                                    </div>
                                    <div class="col-md-2">
                                        <input type="text" name="status" class="form-control" value="${kouji.status}" placeholder="状態" readonly="readonly"/>
                                    </div>
                                </div>
                                <div class="row form-group">
                                    <div class="col-md-10">
                                        <input type="hidden" name="trkDate" value="${kouji.trkDate}" />
                                        <%--<input type="submit" class="btn btn-success" value="変更"/>--%>
                                    </div>
                                </div>
                            </div><!-- information penal -->
                        </form>
                    </div>

                        <div class="panel panel-default progress-panel" id="tenkenRireki-content">
                            <div class="row">
                                <div class="col-md-12">
                                    <ul class="nav nav-tabs nav-justified bg-gray">
                                        <li role="presentation" class="active progress-tab"><a href="#" onclick="return openProgressPanel(this)"><i class="fa fa-cog"></i> 点検ランク</a></li>
                                        <li role="presentation"  class="progress-tab"><a href="#" onclick="return openProgressPanel(this)"><i class="fa fa-gavel"></i> 点検結果</a></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="panel-body progressPanel" id="rank-progress">
                                <div class="row progress-data-div">
                                    <div class="col-md-4">
                                        <i class="glyphicon glyphicon-inbox"> 未完成: <span id="incompleteNum"></span></i>
                                    </div>
                                    <div class="col-md-4">
                                        <i class="glyphicon glyphicon-gift"> 完成: <span id="completeNum"></span></i>
                                    </div>
                                    <div class="col-md-4">
                                        <i class="glyphicon glyphicon-saved"> 点検機器全部: <span id="totalNum"></span></i>
                                    </div>
                                </div>
                                <div class="progress">
                                    <div id="percentProgress" class="progress-bar bg-middle-green progress-bar-striped active" role="progressbar" aria-valuenow="2" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
                                        20%
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body progressPanel" id="kekka-progress">
                                <div class="row progress-data-div">
                                    <div class="col-md-4">
                                        <i class="glyphicon glyphicon-inbox"> 未完成: <span id="kekkaIncompleteNum"></span></i>
                                    </div>
                                    <div class="col-md-4">
                                        <i class="glyphicon glyphicon-gift"> 完成: <span id="kekkaCompleteNum"></span></i>
                                    </div>
                                    <div class="col-md-4">
                                        <i class="glyphicon glyphicon-saved"> 点検機器全部: <span id="kekkaTotalNum"></span></i>
                                    </div>
                                </div>
                                <div class="progress">
                                    <div id="kekkaPercentProgress" class="progress-bar bg-warning progress-bar-striped active" role="progressbar" aria-valuenow="2" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
                                        20%
                                    </div>
                                </div>
                            </div>

                            <div class="row tool-row">
                                <div class="col-md-7"></div>
                                <div class="col-md-5">
                                    <div class="box-tools">
                                        <div class="input-group">
                                            <input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter">
                                            <div class="input-group-btn">
                                                <button class="btn btn-sm btn-default" id="table_search_btn" onclick="filterPage()"><i class="fa fa-search"></i></button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div><br>
                            <div class="tab-content box-body">
                                <table class="table table-hover kiki-table table-bordered" id="kiki-table">
                                    <thead>
                                    <tr>
                                        <th>懸案</th>
                                        <th>弁番号(弁件数：${valveSize})</th>
                                        <th>弁名称</th>
                                        <th>分類</th>
                                        <th>名称</th>
                                        <th>型式番号</th>
                                        <th>点検ランク</th>
                                        <th>点検内容</th>
                                        <th>点検結果</th>
                                        <th>特記事項</th>
                                        <%--<th>操作</th>--%>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${tenkenRirekiUtilList}" var="tenkenRirekiUtil">

                                        <tr id="${tenkenRirekiUtil.id}" class="<c:if test="${(tenkenRirekiUtil.kanryoFlg eq '完成')}">success</c:if>" >
                                        <td><c:if test="${(tenkenRirekiUtil.valve.kenanFlg eq '1')}">〇</c:if></td>
                                        <td>${tenkenRirekiUtil.valve.vNo}</td>
                                        <td>${tenkenRirekiUtil.valve.benMeisyo}</td>
                                        <td>${tenkenRirekiUtil.kiki.kikiBunrui}</td>
                                        <td>${tenkenRirekiUtil.kiki.kikiMei}</td>
                                        <td>${tenkenRirekiUtil.kiki.katasikiNo}</td>
                                        <td>${tenkenRirekiUtil.tenkenRank}</td>
                                        <td>${tenkenRirekiUtil.tenkennaiyo}</td>
                                        <td>${tenkenRirekiUtil.tenkenkekka}</td>
                                        <td>${tenkenRirekiUtil.tenkenBikou}</td>
                                        <%--<td>--%>
                                            <%--<select  id="tenkenRank" class="form-control tenken-select">--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq '')}">selected</c:if>></option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'A')}">selected</c:if>>A</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'B')}">selected</c:if>>B</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'C')}">selected</c:if>>C</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'D')}">selected</c:if>>D</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'E')}">selected</c:if>>E</option>--%>
                                            <%--</select>--%>
                                        <%--</td>--%>
                                        <%--<td>--%>
                                            <%--<input type="text" id="${tenkenRirekiUtil.id}-naiyo" class="form-control input-xs" style="height:25px" placeholder="点検内容"  value="${tenkenRirekiUtil.tenkennaiyo}" />--%>
                                        <%--</td>--%>
                                        <%--<td>--%>
                                            <%--&lt;%&ndash;<input type="text" class="form-control input-xs" style="height:25px" placeholder="点検結果" onblur="saveTenkenkekka(this)" value="${tenkenRirekiUtil.tenkenkekka}" />&ndash;%&gt;--%>
                                            <%--<select id="tenkenkekka"  class="form-control kekka-select">--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq '')}">selected</c:if>></option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'A')}">selected</c:if>>A</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'B')}">selected</c:if>>B</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'C')}">selected</c:if>>C</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'D')}">selected</c:if>>D</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'E')}">selected</c:if>>E</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'F')}">selected</c:if>>F</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'G')}">selected</c:if>>G</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'Y')}">selected</c:if>>Y</option>--%>
                                                <%--<option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'Z')}">selected</c:if>>Z</option>--%>
                                            <%--</select>--%>
                                        <%--</td>--%>
                                        <%--<td>--%>
                                            <%--<input type="text" class="form-control input-xs" style="height:25px" placeholder="特記事項" value="${tenkenRirekiUtil.tenkenBikou}" />--%>
                                        <%--</td>--%>
                                        <%--<td>--%>
                                        <%--<button class="btn btn-xs btn-warning kengen-operation" onclick="openKenan(this)">新規懸案</button></td>--%>
                                        <%--</tr>--%>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                                <div class="row">
                                    <input type="hidden" id="currentPage" value="0" />
                                    <div class="col-md-6">
                                        <nav>
                                            <ul id="pager" class="pagination">
                                            </ul>
                                        </nav>
                                    </div>

                                    <div class="col-md-6"></div>
                                </div>
                        </div>
                </div><!-- information tab -->
                <div class="col-md-1">
                    <div class="row">
                        <div class="col-md-12">
                            <ul class="nav nav-pills nav-stacked bookmarkUl">
                                <li role="presentation" class="currentBookmark"><a href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-cog"> 情報</i></a></li>
                                <li role="presentation" class="kengen-operation"><a href="/valdacHost/kouji/${kouji.id}/instruct"><i class="glyphicon glyphicon-indent-left"> 指示</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/kenan"><i class="glyphicon glyphicon-floppy-save"> 懸案</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/history"><i class="glyphicon glyphicon-time"> 履歴</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/image"><i class="glyphicon glyphicon-picture"> 図面</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/printhtml"><i class="glyphicon glyphicon-download"> DL</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/gpPrinthtml"><i class="glyphicon glyphicon-download"> GP&ICS</i></a></li>
                            </ul>
                        </div>
                    </div>
                </div><!-- tab button tab -->
            </div>
        </section>
    </div>
</body>

<!-- Modal -->
<div class="modal fade" id="kenanModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">懸案追加</h4>
            </div>
            <div class="modal-body">
                <form id="kenanForm"  name="kenanForm" method="post">

                    <div class="row form-group">
                        <div class="col-md-12">
                            <h3>
                                <i class="glyphicon glyphicon-wrench"> <span class="kenanForm-span" id="koujiInfo"></span></i>
                            </h3>
                        </div>
                    </div>

                    <div class="row form-group">
                        <div class="col-md-2"></div>
                        <div class="col-md-10">
                            <span class="kenanForm-span" id="kikisysInfo"></span>
                        </div>
                    </div>
                    <input type="hidden" id="kenanId" class="kenanForm-input" name="id" value="" />
                    <input type="hidden" id="kenanKoujiId" class="kenanForm-input" name="koujiId" value="" />
                    <input type="hidden" id="kenanKoujirelationId" class="kenanForm-input" name="koujirelationId" value="" />
                    <input type="hidden" id="kenanKikiId" class="kenanForm-input" name="kikiId" value="" />
                    <input type="hidden" id="kenanKikiSystemId" class="kenanForm-input" name="kikisysId" value="" />

                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            <h3 class="panel-title">懸案状況</h3>
                        </div>
                        <div class="panel-body">
                            <div class="row form-group">
                                <div class="col-md-2">対応フラグ：</div>
                                <div class="col-md-4">
                                    <select  name="taiouFlg" id="taiouFlg" class="form-control kenanForm-input">
                                        <option>未対応</option>
                                        <option>対応</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-2">発見日付：</div>
                                <div class="col-md-4">
                                    <input type="text" name="hakkenDate" id="hakkenDate" class="form-control kenanForm-input"  placeholder="例：yyyy/mm/dd" value="" />
                                </div>
                                <div class="col-md-2">対策日付：</div>
                                <div class="col-md-4">
                                    <input type="text" name="taisakuDate" id="taisakuDate" class="form-control kenanForm-input" placeholder="例：yyyy/mm/dd"  value="" />
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-2">機器の事象：</div>
                                <div class="col-md-4">
                                    <input type="text" name="hakkenJyokyo" id="hakkenJyokyo" class="form-control kenanForm-input"  value="" />
                                </div>
                                <div class="col-md-2">部品・箇所： </div>
                                <div class="col-md-4">
                                    <input type="text" name="buhin" id="buhin" class="form-control kenanForm-input"  value="" />
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-2">損傷の状況： </div>
                                <div class="col-md-4">
                                    <input type="text" name="gensyo" id="gensyo" class="form-control kenanForm-input"  value=""/>
                                </div>
                                <div class="col-md-2">損傷の要因： </div>
                                <div class="col-md-4">
                                    <input type="text" name="youin" id="youin" class="form-control kenanForm-input"  value="" />
                                </div>
                            </div>

                            <div class="row form-group">
                                <div class="col-md-2">改善対策： </div>
                                <div class="col-md-4">
                                    <input type="text" name="taisaku" id="taisaku" class="form-control kenanForm-input" />
                                </div>
                                <div class="col-md-2">処置内容： </div>
                                <div class="col-md-4">
                                    <input type="text" name="syotiNaiyou" id="syotiNaiyou" class="form-control kenanForm-input"  value=""/>
                                </div>
                            </div>

                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="saveKenan()" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>




<script type="text/javascript">
    $(document).ready(function(){
        //ユーザ権限
        var userKengen=$("#userKengen").val();
        if(userKengen=="6" || userKengen=="5"){//管理者　責任者
            $(".kengen-operation").show();
        }else{//一般者
            $(".kengen-operation").hide();
        }


        $(".kiki-table tr").mouseover(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","1");
        });
        $(".kiki-table tr").mouseout(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","0");
        });

    });
</script>

<script type="text/javascript">
    $(document).ready(function(){
        updateListNumbers();
        updateKekkaListNumbers();

    });
    //Filter 手動
    function filterPage(){
        var keyword = $("#table_search").val();
        keyword=keyword.toLowerCase();
        keyword=keyword.trim();
        if(keyword.length > 0) {
            var trs = $(".kiki-table tr");
            for (var i = 0; i < trs.length; i++) {
                $(trs[i]).hide();
                //是否是头
                var ths = $(trs[i]).find("th");
                if(ths.length > 0){
                    $(trs[i]).show();
                    continue;
                }
                //不是头的行
                var tds = $(trs[i]).find("td");

                for (var j = 0; j < tds.length; j++) {
                    var tmpHtml = new String(tds[j].innerHTML);
                    tmpHtml=tmpHtml.toLowerCase();
                    if (tmpHtml.match(keyword)) {
                        $(trs[i]).show();
                        break;
                    }
                }
            }
        } else {
            var trs = $(".kiki-table tr");
            for (var i = 0; i < trs.length; i++) {
                $(trs[i]).show();
            }
        }
    };

    function saveTenkenrank(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;

        //自動的に点検内容を設定する
        if(obj.value=="A"){
            document.getElementById(rirekiId+'-naiyo').value="試験のみ";
        }else if(obj.value=="B"){
            document.getElementById(rirekiId+'-naiyo').value="GP入替";
        }else if(obj.value=="C"){
            document.getElementById(rirekiId+'-naiyo').value="分解点検";
        }else if(obj.value=="D"){
            document.getElementById(rirekiId+'-naiyo').value="取替";
        }else if(obj.value=="E"){
            document.getElementById(rirekiId+'-naiyo').value="その他";
        }else{
            document.getElementById(rirekiId+'-naiyo').value="";
        }
        var tenkennaiyo=document.getElementById(rirekiId+'-naiyo').value;

        $.post("/valdacHost/tenken/saveTenkenrank",{"id":rirekiId,"tenkenrank":obj.value,"tenkennaiyo":tenkennaiyo},function(data){
            if(data == '完成'){
                if(!$(obj).hasClass("input-success")) {
                    $(obj).addClass("input-success");
                }
            } else {
                if($(obj).hasClass("input-success")) {
                    $(obj).removeClass("input-success");
                }
            }
            updateListNumbers();
        })

    }

    function saveTenkenkekka(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $.post("/valdacHost/tenken/saveTenkenkekka",{"id":rirekiId,"tenkenkekka":obj.value},function(data){
            if(data == '0'){
                if($(obj).hasClass("input-success")) {
                    $(obj).removeClass("input-success");
                }
                if($(rireki).hasClass("success")) {
                    $(rireki).removeClass("success");
                }
            } else {
                if(!$(obj).hasClass("input-success")) {
                    $(obj).addClass("input-success");
                }
                if(!$(rireki).hasClass("success")) {
                    $(rireki).addClass("success");
                }
            }
            updateKekkaListNumbers();
        })
    }

    function saveTenkenBikou(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $.post("/valdacHost/tenken/saveTenkenBikou",{"id":rirekiId,"tenkenBikou":obj.value},function(data){
            if(data == '0'){
                if($(obj).hasClass("input-success")) {
                    $(obj).removeClass("input-success");
                }
            } else {
                if(!$(obj).hasClass("input-success")) {
                    $(obj).addClass("input-success");
                }
            }
        })
    }

    function saveTenkennaiyo(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $.post("/valdacHost/tenken/saveTenkennaiyo",{"id":rirekiId,"tenkennaiyo":obj.value},function(data){
            console.log("data="+data);
            if(data == '0'){
                if($(obj).hasClass("input-success")) {
                    $(obj).removeClass("input-success");
                }
            } else {
                if(!$(obj).hasClass("input-success")) {
                    $(obj).addClass("input-success");
                }
            }
        })
    }

    function updateListNumbers(){
        $.get("/valdacHost/tenken/getListNumber",function(data){
            var datas = JSON.parse(data);
            $("#totalNum").html(datas.total);
            $("#incompleteNum").html(datas.incomplete);
            $("#completeNum").html(datas.complete);

            var percent = datas.complete / datas.total;
            percent = percent*100;
            $("#percentProgress").css({"width":parseInt(percent)+"%"});
            $("#percentProgress").html(parseInt(percent)+"%");
        })
    }

    function updateKekkaListNumbers(){
        $.get("/valdacHost/tenken/getKekkaListNumber",function(data){
            var datas = JSON.parse(data);
            $("#kekkaTotalNum").html(datas.total);
            $("#kekkaIncompleteNum").html(datas.incomplete);
            $("#kekkaCompleteNum").html(datas.complete);

            var percent = datas.complete / datas.total;
            percent = percent*100;
            $("#kekkaPercentProgress").css({"width":parseInt(percent)+"%"});
            $("#kekkaPercentProgress").html(parseInt(percent)+"%");
        })
    }

    function openKenan(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $("#rirekiId").val(rirekiId);

        $.get("/valdacHost/kenan/addKenanByTenkenrireki",{"id":rirekiId},function(data){
            var koujiFormData = JSON.parse(data);
            $(".kenanForm-input").val("");
            var koujiInfo = koujiFormData.kouji.kjNo + "/" + koujiFormData.kouji.kjMeisyo;
            $("#koujiInfo").html(koujiInfo);
            var kikisysInfo = koujiFormData.valve.vNo + " ( " + koujiFormData.valve.benMeisyo + " ) / " + koujiFormData.kiki.kikiMei;
            $("#kikisysInfo").html(kikisysInfo);
            $("#kenanKoujiId").val(koujiFormData.koujiId);
            $("#kenanKoujirelationId").val(koujiFormData.koujirelationId);
            $("#kenanKikiId").val(koujiFormData.kikiId);
            $("#kenanKikiSystemId").val(koujiFormData.kikisysId);
            $("#taiouFlg").val("未対応");
            $("#kenanModal").modal();
        });
    }

    function deleteTenkenKiki(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $("#rirekiId").val(rirekiId);

        if (!confirm("この行を削除しますか？"))
            return;

        $.get("/valdacHost/tenken/deleteTenkenRirekiByTenkenrireki",{"id":rirekiId},function(data){

            var objTR=obj.parentNode.parentNode;
            var objTBL=objTR.parentNode;
            if(objTBL){
                objTBL.deleteRow(objTR.sectionRowIndex);
            }

            updateListNumbers();
            updateKekkaListNumbers();
        });
    }
    function openProgressPanel(obj){
        var content = new String($(obj).html());
        $(".progress-tab").removeClass("active");
        $(".progressPanel").hide();
        if(content.indexOf("ランク") > -1){
            $($(".progressPanel")[0]).show();
            $($(".progress-tab")[0]).addClass("active");
        } else {
            $($(".progressPanel")[1]).show();
            $($(".progress-tab")[1]).addClass("active");
        }
        return false;
    }

    function saveKenan(){
        var datas = $(".kenanForm-input");
        var kenanFormArray = new Array();
        for(var i = 0;i<datas.length;i++){
            kenanFormArray[i] = datas[i].value;
        }
        var kenanFormJson = JSON.stringify(kenanFormArray);
        $.post("/valdacHost/kenan/saveKenanWithForm",{"kenanForm":kenanFormJson},function(data){
            $("#kenanModal").modal("hide");
        })
    }
</script>
</html>
