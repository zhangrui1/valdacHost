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
<style type="text/css">
    input[type=checkbox] {
        /* checkbox 大さを調整する */
        width: 15px;
        height: 15px;
        vertical-align: middle;
    }
</style>
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
            </h1>
            <ol class="breadcrumb" style="font-size:20pt;">
                <li><i class="fa fa-dashboard"></i> <a href="/valdacHost/">工事一覧へ</a></li>
            </ol>
        </section>
        <hr/>

        <section class="content">

            <%--<c:if test="${printMessage != null}">--%>
                <div class="row">
                    <div class="col-md-3">
                        <div class="alert alert-danger alert-dismissable" id="printMessageId" class="printMessageId" >${printMessage}</div>
                    </div>
                </div>
            <%--</c:if>--%>

            <div class="row">
                <div class="col-md-11">
                    <div class="panel panel-success">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-md-10">
                                    <h4><i class="fa fa-download"></i> 報告書ダウンロード </h4>
                                </div>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-4">
                                    <form name="printReportDL" action="/valdacHost/print/printReport/${kouji.id}" method="post" onsubmit="checkIdListNullForPrintReport();return false;">
                                        <input type="hidden" name="idListForPrintReport" id="idListForPrintReport" value="" />
                                        <input type="submit" class="btn btn-default" onclick="submitForm()" value="工事内容一覧DL"/>
                                    </form>
                                </div>
                                <div class="col-md-4">
                                    <form name="PrintKenanReport" action="/valdacHost/print/printKenanReport/${kouji.id}" method="post" onsubmit="checkIdListNullForPrintKenanReport();return false;">
                                        <input type="hidden" name="idListForPrintKenanReport" id="idListForPrintKenanReport" value="" />
                                        <input type="submit" class="btn btn-default" onclick="submitForm()" value="懸案報告書DL"/><br>
                                        <input type="hidden" name="kenanFlgDL" id="kenanFlgDL" value="" />
                                        <input type="radio" name="valve-kenan-radio" class="valve-kenan-radio" value="true" checked> 全部
                                        <input type="radio" name="valve-kenan-radio" class="valve-kenan-radio" value="false"> 未対応のみ
                                    </form>
                                </div>
                            </div>
                        </div>
                        </div>

                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-md-10">
                                        <h4><i class="fa fa-print"></i> 工事支援ダウンロード</h4>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="form-group">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <form name="printKenan" action="/valdacHost/print/printKenan/${kouji.id}" method="post" onsubmit="checkIdListNullForPrintKenan();return false;">
                                                <input type="hidden" name="idListForPrintKenan" id="idListForPrintKenan" value="" />
                                                <input type="submit" class="btn btn-default" onclick="submitForm()" value="過去未対応懸案リスト"/>
                                            </form>
                                        </div>
                                        <div class="col-md-4">
                                            <form name="printSagyo" action="/valdacHost/print/printSagyo/${kouji.id}" method="post" onsubmit="checkIdListNullForPrintSagyo();return false;">
                                                <input type="hidden" name="idListForPrintSagyo" id="idListForPrintSagyo" value="" />
                                                <input type="submit" class="btn btn-default" onclick="submitForm()" value="作業ラベル"/>
                                            </form>
                                        </div>
                                        <div class="col-md-4">
                                            <form name="ValveRabelDL" action="/valdacHost/print/printValve/${kouji.id}" method="post" onsubmit="setvalveRadio();return false;">
                                                <input type="submit" class="btn btn-default"  value="弁番号ラベル"/>
                                                <input type="hidden" name="naikeiFlg" id="naikeiFlg" value="" />
                                                <input type="radio" name="valve-radio" class="valve-radio" value="1" checked> 内径あり
                                                <input type="radio" name="valve-radio" class="valve-radio" value="0"> 内径なし
                                                <input type="hidden" name="idListForValveRabel" id="idListForValveRabel" value="" />
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <form  name="printTenken" action="/valdacHost/print/printTenken/${kouji.id}" method="post" onsubmit="checkIdListNullForPrintTenken();return false;">
                                                <input type="hidden" name="idListForPrintTenken" id="idListForPrintTenken" value="" />
                                                <input type="submit" class="btn btn-default" onclick="submitForm()"  value="点検リストDL"/>
                                            </form>
                                        </div>
                                        <div class="col-md-4">
                                            <form name="printIcs"  action="/valdacHost/print/printIcs/${kouji.id}" method="post" onsubmit="checkIdListNullForPrintIcs();return false;">
                                                <input type="hidden" name="idListForPrintIcs" id="idListForPrintIcs" value="" />
                                                <input type="submit" class="btn btn-default" onclick="submitForm()" value="ICS点検リスト"/>
                                            </form>
                                        </div>
                                        <div class="col-md-4">
                                            <form name="printTool" action="/valdacHost/print/printTool/${kouji.id}" method="post" onsubmit="checkIdListNullForPrintTool();return false;">
                                                <input type="hidden" name="idListForPrintTool" id="idListForPrintTool" value="" />
                                                <input type="submit" class="btn btn-default" onclick="submitForm()" value="工具点検リスト"/>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-md-10">
                                    <h4>弁リスト(弁を選択してから、ダウンロードのボタンを押してください)</h4>
                                    <p><font color="#ff0000">弁選択方法：弁番号または弁名称の上にクリックすると、選択/非選択できる</font></p><br>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-2">
                                    <div class="btn-group">
                                        <div class="input-group">
                                            <input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter">
                                            <div class="input-group-btn">
                                                <button class="btn btn-sm btn-default" id="table_search_btn" onclick="filterPage()"><i class="fa fa-search"></i></button>
                                            </div>
                                        </div>
                                    </div>
                                    </div>
                                <div class="col-md-1"></div>
                                <div class="col-md-1">
                                    <div class="btn-group">
                                        <div class="input-group">
                                            <button class="btn btn-sm btn-default selected_search_btn" id="selected_search_btn" onclick="selectedValveList()">選択された弁のみ表示</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-1"></div>
                                <div class="col-md-1">
                                    <div class="btn-group">
                                        <div class="input-group">
                                            <button class="btn btn-sm btn-default" id="all_search_btn" onclick="allValveList()">全部弁表示</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <table class="table table-hover kiki-table select-table" id="kiki-table">
                            <thead>
                            <tr>
                                <th><input type="checkbox" class="headCheckbox" onclick="selectAllItem()">(全選)</th>
                                <th>懸案</th>
                                <th>弁番号</th>
                                <th>弁名称</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${tenkenValveList}" var="tenkenRirekiUtil">

                                <tr class="valve-item" >
                                    <td><input type="checkbox" class="checkbox" style="opacity: 10" value="${tenkenRirekiUtil.valve.kikiSysId}"></td>
                                    <td><c:if test="${(tenkenRirekiUtil.valve.kenanFlg eq '1')}">〇</c:if></td>
                                    <td>${tenkenRirekiUtil.valve.vNo}</td>
                                    <td>${tenkenRirekiUtil.valve.benMeisyo}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    </div>


                <div class="col-md-1">
                    <div class="row">
                        <div class="col-md-12">
                            <ul class="nav nav-pills nav-stacked bookmarkUl">
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-cog"> 情報</i></a></li>
                                <li role="presentation" class="kengen-operation"><a href="/valdacHost/kouji/${kouji.id}/instruct"><i class="glyphicon glyphicon-indent-left"> 指示</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/kenan"><i class="glyphicon glyphicon-floppy-save"> 懸案</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/history"><i class="glyphicon glyphicon-time"> 履歴</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/image"><i class="glyphicon glyphicon-picture"> 図面</i></a></li>
                                <li role="presentation" class="currentBookmark"><a href="/valdacHost/kouji/${kouji.id}/printhtml"><i class="glyphicon glyphicon-download"> DL</i></a></li>
                                <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/gpPrinthtml"><i class="glyphicon glyphicon-download"> GP&ICS</i></a></li>
                            </ul>
                        </div>
                    </div>
                </div><!-- tab button tab -->
            </div>
        </section>
    </div>
</body>

<script type="text/javascript">
    $(document).ready(function(){
        //ユーザ権限
        var userKengen=$("#userKengen").val();
        if(userKengen=="6" || userKengen=="5"){//管理者　責任者
            $(".kengen-operation").show();
        }else{
            $(".kengen-operation").hide();
        }

        //message表示するかどうか
        var tmpKenanMessage=document.getElementById('printMessageId').innerHTML;
        if(tmpKenanMessage != ""){
            document.getElementById('printMessageId').style.display='block'
        } else {
            document.getElementById('printMessageId').style.display='none'
        }

        //弁選択
        $('.valve-item').mouseup(function(obj){
            var tr = obj.currentTarget;
            var selected = $(tr).find('.checkbox');
            var checkbox = selected[0];
            if($(checkbox).prop("checked") == true){
                $(checkbox).prop('checked', false);
                $(tr).removeClass("bg-light-blue-gradient");
            } else {
                $(checkbox).prop('checked', true);
                $(tr).addClass("bg-light-blue-gradient");

            }
            //kenan message初期化
            document.getElementById('printMessageId').innerHTML="";
            document.getElementById('printMessageId').style.display='none'
        });
    });
    function  setvalveRadio() {
        //弁ラベル　内径あり、なし情報取得
        $('.valve-radio:checked').each(function(){
            $('#naikeiFlg').val($(this)[0].value);
        });
        //弁リストを取得
        var idList = "";
        $('.checkbox:checked').each(function(){
            idList = idList + $(this)[0].value + ',';
        });
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            $('#idListForValveRabel').val(idList);
            document.ValveRabelDL.submit();
        }


    }

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
                    if (tmpHtml.match(keyword)) {
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
        } else {
            var trs = $(".kiki-table tr");
            for (var i = 0; i < trs.length; i++) {
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

    function submitForm(){
        var idList = "";
        $('.checkbox:checked').each(function(){
            idList = idList + $(this)[0].value + ',';
        });
        $('#idListForPrintReport').val(idList);
        $('#idListForPrintKenanReport').val(idList);
        $('#idListForPrintKenan').val(idList);
        $('#idListForPrintSagyo').val(idList);
        $('#idListForPrintTenken').val(idList);
        $('#idListForPrintIcs').val(idList);
        $('#idListForPrintTool').val(idList);

        document.getElementById('printMessageId').style.display='none'
        document.getElementById('printMessageId').innerHTML="";
    }

    //IdListは空場合はメッセージを表示する
    function checkIdListNullForPrintReport(){
        var idList = $("#idListForPrintReport").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.printReportDL.submit();
        }
    }
    //IdListは空場合はメッセージを表示する
    function checkIdListNullForPrintKenanReport(){
        //弁ラベル　懸案全部か？未対応のみか？
        $('.valve-kenan-radio:checked').each(function(){
            $('#kenanFlgDL').val($(this)[0].value);
        });

        var idList = $("#idListForPrintKenanReport").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.PrintKenanReport.submit();
        }
    }
    //IdListは空場合はメッセージを表示する
    function checkIdListNullForPrintKenan(){
        var idList = $("#idListForPrintKenan").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.printKenan.submit();
        }
    }
    //IdListは空場合はメッセージを表示する
    function checkIdListNullForPrintSagyo(){
        var idList = $("#idListForPrintSagyo").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.printSagyo.submit();
        }
    }

    //IdListは空場合はメッセージを表示する
    function checkIdListNullForPrintTenken(){
        var idList = $("#idListForPrintTenken").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.printTenken.submit();
        }
    }
    //IdListは空場合はメッセージを表示する
    function checkIdListNullForPrintIcs(){
        var idList = $("#idListForPrintIcs").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.printIcs.submit();
        }
    }

    //IdListは空場合はメッセージを表示する
    function checkIdListNullForPrintTool(){
        var idList = $("#idListForPrintTool").val();
        if(idList.length<1){
            alert("弁を選択してから、ボタンを押してください。");
        }else{
            document.printTool.submit();
        }
    }

</script>
</html>
