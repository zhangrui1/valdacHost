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
                <li><i class="fa fa-dashboard"></i><a href="/valdacHost/">工事一覧へ</a></li>
            </ol>
        </section>
        <hr/>

        <section class="content">

            <div class="row">
                <div class="col-md-11">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-md-11">
                                    <h4>工事情報</h4>
                                </div>
                                <div class="col-md-1 kengen-person">
                                    <form action="/valdacHost/kouji/delete/${kouji.id}" method="post" onsubmit="return deleteKouji()">
                                        <input type="submit" class="btn btn-danger pull-right" value="X 工事削除"/>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <form id="KoujiForm" name="KoujiForm"  method="post">
                            <input type="hidden" name="id" class="kouji-form" value="${kouji.id}"/>
                            <div class="panel-body">
                                <div class="row form-group">
                                    <div class="col-md-3">
                                        <input type="text" name="kjNo" class="form-control kengen-readonly kouji-form" value="${kouji.kjNo}" placeholder="工事番号(必須)" />
                                    </div>
                                    <div class="col-md-9">
                                        <input type="text" name="kjMeisyo" class="form-control kengen-readonly kouji-form" value="${kouji.kjMeisyo}" placeholder="工事名称(必須)" />
                                    </div>
                                </div>

                                <div class="row form-group">
                                    <div class="col-md-2">
                                        <select name="person" id="person" class="form-control kengen-readonly kouji-form" value="${kouji.person}" placeholder="責任者">
                                            <option>--責任者--</option>
                                            <c:forEach items="${koujiPersons}" var="tmp">
                                                <option>${tmp.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-md-8" id="sandbox-container">
                                        <div class="input-daterange input-group" id="datepicker" style="width:100%">
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <input type="text" class="form-control kengen-readonly kouji-form" name="bgnYmd" id="bgnYmd" placeholder="開始日付(必須)" value="${kouji.bgnYmd}">
                                            <span class="input-group-addon">〜</span>
                                            <input type="text" class="form-control kengen-readonly kouji-form" name="endYmd" placeholder="終了日付(必須)" value="${kouji.endYmd}">
                                        </div>
                                    </div>

                                    <div class="col-md-2">
                                        <input type="text" name="nendo" class="form-control kengen-readonly kouji-form" value="${kouji.nendo}" placeholder="年度" />
                                    </div>
                                </div>

                                <div class="row form-group">
                                    <div class="col-md-4">
                                        <input type="text" name="location" class="form-control kouji-form" readonly="readonly" value="${kouji.location}" />
                                    </div>
                                    <div class="col-md-4">
                                        <select name="kjKbn" id="kjKbn" class="form-control kengen-readonly kouji-form">
                                            <%--<option>--工事区分--</option>--%>
                                            <c:forEach items="${koujiKjkbns}" var="tmp">
                                                <option>${tmp.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <input type="text" name="nextYmd" class="form-control kengen-readonly kouji-form" value="${kouji.nextYmd}" placeholder="今後日付" />
                                    </div>
                                 </div>
                                <div class="row form-group">
                                    <div class="col-md-4">
                                        <%--<input type="text" name="syukan" class="form-control" value="${kouji.syukan}" placeholder="主管係" />--%>
                                        <select name="syukan" id="syukan" class="form-control kengen-readonly kouji-form">
                                            <%--<option>--主管係--</option>--%>
                                            <c:forEach items="${koujiSyukans}" var="tmp">
                                                <option>${tmp.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <select name="gyosyaRyakuA" id="gyosyaRyakuA" class="form-control kengen-readonly kouji-form">
                                            <option>--点検業者--</option>
                                            <c:forEach items="${koujiGyosyas}" var="tmp">
                                                <option>${tmp.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <select name="status" id="koujiStatus" class="form-control kengen-readonly kouji-form">
                                            <%--<option>--工事進捗--</option>--%>
                                            <c:forEach items="${koujiStatus}" var="tmp">
                                                <option>${tmp.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row form-group">
                                    <div class="col-md-10 kengen-person">
                                        <input type="hidden" name="trkDate kouji-form" value="${kouji.trkDate}" />
                                        <button type="button" onclick="checkKouji()" class="btn btn-success">変更</button>
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
                                <div class="col-md-2 kengen-person"><a href="/valdacHost/kouji/${kouji.id}/valve" class="btn btn-success bg-middle-green btn-sm" ><i class="fa fa-plus">点検弁追加</i></a></div>
                                <div class="col-md-2 kengen-person"><button onclick="showDeleteBtn(this)" class="btn btn-default btn-sm"><i class="fa fa-trash-o">削除ボタン表示</i></button></div>
                                <div class="col-md-2">点検ランク(一括設定)：</div>
                                <div class="col-md-1">
                                    <select  class="form-control tenken-select" onchange="saveAllTenkenRank(this)">
                                        <option></option>
                                        <option>A</option>
                                        <option>B</option>
                                        <option>C</option>
                                        <option>D</option>
                                        <option>E</option>
                                    </select>
                                </div>
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
                                <table class="table table-hover kiki-table  table-bordered" id="kiki-table">
                                    <thead>
                                    <tr>
                                        <th>懸案</th>
                                        <th>弁番号(弁件数：${valveSize})</th>
                                        <th>弁名称</th>
                                        <th>分類</th>
                                        <th>名称</th>
                                        <th>点検ランク</th>
                                        <th>点検内容</th>
                                        <th>点検結果</th>
                                        <th>特記事項</th>
                                        <th>操作</th>
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
                                            <td>
                                                <select  class="form-control tenken-select" onchange="saveTenkenrank(this)">
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq '')}">selected</c:if>></option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'A')}">selected</c:if>>A</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'B')}">selected</c:if>>B</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'C')}">selected</c:if>>C</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'D')}">selected</c:if>>D</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenRank eq 'E')}">selected</c:if>>E</option>
                                                　</select>
                                            </td>
                                            <td>
                                                <input type="text" id="${tenkenRirekiUtil.id}-naiyo" class="form-control input-xs tenken-naiyo kengen-readonly" style="height:25px" placeholder="点検内容" onchange="saveTenkennaiyo(this)" value="${tenkenRirekiUtil.tenkennaiyo}" />
                                            </td>
                                            <td>
                                                <%--<input type="text" class="form-control input-xs" style="height:25px" placeholder="点検結果" onblur="saveTenkenkekka(this)" value="${tenkenRirekiUtil.tenkenkekka}" />--%>
                                                <select  class="form-control kekka-select"  onchange="saveTenkenkekka(this)">
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq '')}">selected</c:if>></option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'A')}">selected</c:if>>A</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'B')}">selected</c:if>>B</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'C')}">selected</c:if>>C</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'D')}">selected</c:if>>D</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'E')}">selected</c:if>>E</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'F')}">selected</c:if>>F</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'G')}">selected</c:if>>G</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'Y')}">selected</c:if>>Y</option>
                                                    <option  <c:if test="${(tenkenRirekiUtil.tenkenkekka eq 'Z')}">selected</c:if>>Z</option>
                                                </select>
                                            </td>
                                            <td>
                                                <input type="text" class="form-control input-xs" style="height:25px" placeholder="特記事項" onblur="saveTenkenBikou(this)" value="${tenkenRirekiUtil.tenkenBikou}" />
                                            </td>
                                            <td>
                                                <button class="btn btn-xs btn-warning" onclick="openKenan(this)">新規懸案</button>
                                                <button class="btn btn-xs bg-red tenkenkiki-delete-btn" onclick="deleteTenkenKiki(this)"><i class="glyphicon glyphicon-remove"></i></button>
                                            </td>
                                        </tr>
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
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-cog"> 情報</i></a></li>
                                <li role="presentation" class="currentBookmark kengen-operation"><a href="/valdacHost/kouji/${kouji.id}/instruct"><i class="glyphicon glyphicon-indent-left"> 指示</i></a></li>
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
        if(userKengen=="6"){
            $(".kengen-operation").show();
        }else if(userKengen=="5"){
            $(".kengen-operation").show();
            $(".kengen-person").hide();
            $(".kengen-readonly").attr('readonly',true);
        }else{
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

        // selected に設定する設定
        //責任者
        var koujiper="${kouji.person}";
        var objname1=document.getElementById("person");
        checkSelect(objname1,koujiper);
        //工事区分
        var koujikjKbn="${kouji.kjKbn}";
        var objname2=document.getElementById("kjKbn");
        checkSelect(objname2,koujikjKbn);
        //工事主関係
        var koujiSyukan="${kouji.syukan}";
        var objname3=document.getElementById("syukan");
        checkSelect(objname3,koujiSyukan);
        //工事業者
        var koujigyosya="${kouji.gyosyaRyakuA}";
        var objname4=document.getElementById("gyosyaRyakuA");
        checkSelect(objname4,koujigyosya);
        //工事進捗
        var koujiStatu="${kouji.status}";
        var objname5=document.getElementById("koujiStatus");
        checkSelect(objname5,koujiStatu);

        function checkSelect(obj,val){
            for(var i=0;i<obj.length;i++){
                if(obj[i].value==val){
                    obj[i].selected=true;
                    break;
                }
            }
        }

    });
</script>
<script type="text/javascript">
    $(document).ready(function(){

        <%--//工事区分 Master を呼ぶ--%>
        <%--$.post("/valdacHost/master/getMasterByTypeJson",{"type":'工事区分'},function(data){--%>
            <%--var masters = JSON.parse(data);--%>
            <%--$('#kjKbn').html("");--%>
            <%--var tmpHTML = "<option>${kouji.kjKbn}</option>";--%>
            <%--for(var i = 0 ; i < masters.length ; i++){--%>
                    <%--tmpHTML = tmpHTML+"<option>" + masters[i].name + "</option>";--%>
            <%--}--%>
            <%--$('#kjKbn').html(tmpHTML);--%>
        <%--});--%>

        <%--//責任者 Master を呼ぶ--%>
        <%--$.post("/valdacHost/master/getMasterByTypeJson",{"type":'責任者'},function(data){--%>
            <%--var masters = JSON.parse(data);--%>
            <%--$('#person').html("");--%>
            <%--var tmpHTML = "<option>${kouji.person}</option>";--%>

            <%--for(var i = 0 ; i < masters.length ; i++){--%>
                <%--tmpHTML = tmpHTML+"<option>" + masters[i].name + "</option>";--%>
            <%--}--%>
            <%--$('#person').html(tmpHTML);--%>
        <%--});--%>

        <%--//主管係 Master を呼ぶ--%>
        <%--$.post("/valdacHost/master/getMasterByTypeJson",{"type":'主管係'},function(data){--%>
            <%--var masters = JSON.parse(data);--%>
            <%--$('#syukan').html("");--%>
            <%--var tmpHTML = "<option>${kouji.syukan}</option>";--%>
            <%--for(var i = 0 ; i < masters.length ; i++){--%>
                <%--tmpHTML = tmpHTML+"<option>" + masters[i].name + "</option>";--%>
            <%--}--%>
            <%--$('#syukan').html(tmpHTML);--%>
        <%--});--%>

        <%--//主管係 Master を呼ぶ--%>
        <%--$.post("/valdacHost/master/getMasterByTypeJson",{"type":'点検業者'},function(data){--%>
            <%--var masters = JSON.parse(data);--%>
            <%--$('#gyosyaRyakuA').html("");--%>
            <%--var tmpHTML = "<option>${kouji.gyosyaRyakuA}</option>";--%>
            <%--for(var i = 0 ; i < masters.length ; i++){--%>
                <%--tmpHTML = tmpHTML+"<option>" + masters[i].name + "</option>";--%>
            <%--}--%>
            <%--$('#gyosyaRyakuA').html(tmpHTML);--%>
        <%--});--%>

        //The Calender
        $('#sandbox-container .input-daterange').datepicker({
            format: 'yyyy/mm/dd',
            language: 'ja'
        });
        $('#nextYmdDiv input').datepicker({
            format: 'yyyy/mm/dd',
            language: 'ja'
        });

        $('#bgnYmd').change(function(){
            var dateStr = new String($('#bgnYmd').val());
            var nendo = dateStr.split('/');
            $('#nendo').val(nendo[0]);
        });

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

    //点検ランクを一括設定
    function saveAllTenkenRank(obj){

        $('.tenken-select').val(obj.value);
        var koujiId=${kouji.id};
        var tenkennaiyo="";

        //自動的に点検内容を設定する
        if(obj.value=="A"){
            $('.tenken-naiyo').val("試験のみ");
            tenkennaiyo="試験のみ";
        }else if(obj.value=="B"){
            $('.tenken-naiyo').val("GP入替");
            tenkennaiyo="GP入替";
        }else if(obj.value=="C"){
            $('.tenken-naiyo').val("分解点検");
            tenkennaiyo="分解点検";
        }else if(obj.value=="D"){
            $('.tenken-naiyo').val("取替");
            tenkennaiyo="取替";
        }else if(obj.value=="E"){
            $('.tenken-naiyo').val("その他");
            tenkennaiyo="その他";
        }else{
            $('.tenken-naiyo').val("");
        }


        $.post("/valdacHost/tenken/saveTenkenrankAllByKoujiID",{"id":koujiId,"tenkenrank":obj.value,"tenkennaiyo":tenkennaiyo},function(data){
           console.log("DB更新した");
           updateListNumbers();
        })
    }

    //点検ランクを設定
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

    //点検結果を設定
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
        //長さ　判断
        if(obj.value.length>255){
            window.alert("点検備考を255文字以内にしてください。");
        }else{
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
    }

    function saveTenkennaiyo(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        //長さ　判断
        if(obj.value.length>199){
            window.alert("点検備考を200文字以内にしてください。");
        }else{
            $.post("/valdacHost/tenken/saveTenkennaiyo",{"id":rirekiId,"tenkennaiyo":obj.value},function(data){
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
    //日付チェック関数
    function ckDate(datestr) {
        // 正規表現による書式チェック
        if(!datestr.match(/^\d{4}\/\d{2}\/\d{2}$/)){
            return false;
        }
        var vYear = datestr.substr(0, 4) - 0;
        var vMonth = datestr.substr(5, 2) - 1; // Javascriptは、0-11で表現
        var vDay = datestr.substr(8, 2) - 0;
        // 月,日の妥当性チェック
        if(vMonth >= 0 && vMonth <= 11 && vDay >= 1 && vDay <= 31){
            var vDt = new Date(vYear, vMonth, vDay);
            if(isNaN(vDt)){
                return false;
            }else if(vDt.getFullYear() == vYear && vDt.getMonth() == vMonth && vDt.getDate() == vDay){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    //日付　比較
    function dateCompare(dateA,dateB) {
        if(dateA<dateB){
            return true;
        }else{
            return false;
        }
    }
    //懸案保存
    function saveKenan(){
        var datas = $(".kenanForm-input");
        var kenanFormArray = new Array();
        for(var i = 0;i<datas.length;i++){
            kenanFormArray[i] = datas[i].value;
        }
        //kenan　長さ判断
        var tmpHakkenDate=document.kenanForm.hakkenDate.value;
        var tmpTaisakuDate=document.kenanForm.taisakuDate.value;
        //開始日付データがあるときのみ　チェックする
        if(tmpHakkenDate.length>0){
            if(tmpHakkenDate.length>10 || !ckDate(tmpHakkenDate)){
                window.alert("発見日付を「yyyy/mm/dd」型式にしてください。");
                return false;
            }
        }
        //終了日付データがあるときのみ　チェックする
        if(tmpTaisakuDate.length>0){
            if(tmpTaisakuDate.length>10 || !ckDate(tmpTaisakuDate)){
                window.alert("対策日付を「yyyy/mm/dd」型式にしてください。");
                return false;
            }
        }
        //開始日付と終了日付　両方があるときのみ　　チェックする
        if((tmpHakkenDate.length>0) &&(tmpTaisakuDate.length>0)) {
            if(dateCompare(tmpTaisakuDate,tmpHakkenDate)){
                window.alert("対策日付は発見日付より小さいので、ご確認ください");
                return false;
            }
        }

        if(document.kenanForm.hakkenJyokyo.value.length>100 ){
            window.alert("機器の事象を100文字以内にしてください。");
            return false;
        }
        if(document.kenanForm.buhin.value.length>30 ){
            window.alert("部品・箇所事象を30文字以内にしてください。");
            return false;
        }
        if(document.kenanForm.gensyo.value.length>30 ){
            window.alert("損傷の状況を30文字以内にしてください。");
            return false;
        }
        if(document.kenanForm.youin.value.length>30 ){
            window.alert("損傷の要因を30文字以内にしてください。");
            return false;
        }
        if(document.kenanForm.taisaku.value.length>30 ){
            window.alert("改善対策を30文字以内にしてください。");
            return false;
        }
        if(document.kenanForm.syotiNaiyou.value.length>100 ){
            window.alert("処置内容を100文字以内にしてください。");
            return false;
        }
        //DBに保存する
        var kenanFormJson = JSON.stringify(kenanFormArray);
        $.post("/valdacHost/kenan/saveKenanWithForm",{"kenanForm":kenanFormJson},function(data){
            $("#kenanModal").modal("hide");
        })

    }

    function deleteKouji(obj) {
        if (!confirm("この工事を削除しますか？")){
            return false;
        } else {
            return true;
        }
    }

    function showDeleteBtn(obj){
        $(obj).toggleClass('bg-red');
        $('.tenkenkiki-delete-btn').toggle();
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

    function sbumitFunction(){
        console.log($("#KoujiForm"));
    }

    function checkKouji(){
        var flag=0;
        var nendo=0;
        var updateFlg=1;
        //必須項目設定
        if(document.KoujiForm.kjNo.value==""){flag=1;}
        if(document.KoujiForm.kjMeisyo.value==""){flag=1;}
        if(document.KoujiForm.person.value==""){flag=1;}
        if(document.KoujiForm.bgnYmd.value==""){flag=1;}
        if(document.KoujiForm.endYmd.value==""){flag=1;}
        if(document.KoujiForm.nendo.value==""){flag=1;}
        if(document.KoujiForm.syukan.value==""){flag=1;}
        if(document.KoujiForm.gyosyaRyakuA.value==""){flag=1;}
        if(document.KoujiForm.location.value==""){flag=1;}

        //点検年度チェック　４桁の半角
        if(document.KoujiForm.nendo.value.length>4||document.KoujiForm.nendo.value.length<4){nendo=1;}
        if(document.KoujiForm.nendo.value.match(/[^0-9]+/)){nendo=1;}

        if(flag){
            updateFlg=0;
            window.alert("必須項目を入力ください");
            return false;
        }
        if(nendo){
            updateFlg=0;
            window.alert("点検年度を4桁半角数字で入力ください。例：2015");
            return false;
        }
        //長さ　判断
        if(document.KoujiForm.kjNo.value.length>20){
            window.alert("弁番号を20文字以内にしてください。");
            return false;
        }
        if(document.KoujiForm.kjMeisyo.value.length>90){
            window.alert("弁名称を90文字以内にしてください。");
            return false;
        }

        //工事番号は重複かどうかチェック
        var KoujiCheckResult=1;
        var flag=true;

        var kjNo= document.KoujiForm.kjNo.value;
        var location=document.KoujiForm.location.value;
        var id=document.KoujiForm.id.value;
        location=location.trim();
        console.log("kjNo="+kjNo+"   ;location="+location);
        //DBにてチェックする
        $.ajax({
            url:"/valdacHost/kouji/getResultForKoujiVNoCheckEdit",
            type: "POST",
            data: { "id":id,"kjNo":kjNo,"location":location },
            async:false,
            success: function(data){
                var resultTmp=JSON.parse(data);
                console.log("resultTmp="+resultTmp);
                if(resultTmp){
                    KoujiCheckResult=0;//使える
                }else{
                    KoujiCheckResult=1;//使えない
                }
                flag=false;
            }
        });
        while(flag){
        }
        console.log("flag2="+flag);
        console.log("KoujiCheckResult="+KoujiCheckResult);
        if(KoujiCheckResult==1){
            updateFlg=0;
            window.alert("この工事番号はすでに使われてます。");
            return false;
        }
        //工事情報更新
        if(updateFlg==1){
            var datas = $(".kouji-form");
            var koujiFormArray = new Array();
            for(var i = 0;i<datas.length;i++){
                koujiFormArray[i] = datas[i].value;
            }
            var koujiFormJson = JSON.stringify(koujiFormArray);
            $.post("/valdacHost/kouji/instruct/updateKoujiJson",{"koujiForm":koujiFormJson},function(data){
                window.alert("工事情報が更新されました");
            })
        }

        return true;

    }
</script>
</html>
