<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 11/19/14
  Time: 10:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp"/>
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
    <input type="hidden" name="koujiId" id="koujiId" value="${kouji.id}" />
    <section class="content-header">
        <h1>
            <i class="glyphicon glyphicon-wrench"> ${kouji.kjMeisyo}</i>
            <span class="label label-warning">未完成</span>
        </h1>
        <%--<ol class="breadcrumb" style="font-size:20pt;">--%>
            <%--<li><i class="fa fa-dashboard"></i> Index/${kouji.kjMeisyo}</li>--%>
        <%--</ol>--%>
    </section>
    <hr/>

    <section class="content">
        <div class="row" id="step-row">
            <div class="col-md-4 step-div first-step-complete">
                <span class="glyphicon glyphicon-calendar"> 1. 工事情報追加</span>
            </div>
            <div class="col-md-4 step-div middle-step-complete">
                <span class="glyphicon glyphicon-list"> 2. 点検バルブ選択</span>
            </div>
            <div class="col-md-4 step-div">
                <span class="glyphicon glyphicon-indent-left"> 3. 点検機器選択</span>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-md-2">
                                <div class="btn-group">
                                    <div class="input-group">
                                        <input type="text" id="vNo" name="vNo"  class="form-control" style="width: 150px;" placeholder="Filter">
                                          <span class="input-group-btn">
                                            <button type="button" class="btn btn-default pull-left" onclick="filterByKeywords()"><i class="fa fa-search"></i> </button>
                                          </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-1"><i id="spinner" class="fa fa-spinner fa-2x fa-pulse"></i></div>
                        </div>
                    </div>
                    <div class="panel-body table-panel">
                        <table class="table table-hover table-striped select-table">
                            <tr>
                                <th><input type="checkbox" class="headCheckbox" onclick="selectAllItem()">全選</th>
                                <th>懸案</th>
                                <th>弁番号</th>
                                <th>弁名称</th>
                                <%--<th>形式</th>--%>
                                <th>クラス</th>
                                <th>呼び径</th>
                                <th>圧力</th>
                                <th>流体</th>
                            </tr>

                        <c:forEach items="${valveListByL}" var="valve">
                            <tr class="valve-item" id="select-${valve.kikiSysId}">
                                <td><input type="checkbox" class="checkbox" style="opacity: 0" value="${valve.kikiSysId}"></td>
                                <td><c:if test="${(valve.kenanFlg eq '1')}">〇</c:if></td>
                                <td class="selected-detail">${valve.vNo}</td>
                                <td class="selected-detail">${valve.benMeisyo}</td>
                                <%--<td>${valve.keisiki}</td>--%>
                                <td>${valve.classType}</td>
                                <td>${valve.yobikei}</td>
                                <td>${valve.aturyokuMax}${valve.tani}</td>
                                <td>${valve.ryutai}</td>
                            </tr>
                        </c:forEach>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <%--<form action="/valdacHost/kouji/${kouji.id}/valve" id="valveListForm" method="post">--%>
                            <%--<input type="hidden" name="idList" id="idList" value="" />--%>
                            <%--<button class="btn btn-success btn-sm" onclick="submitForm()">追加</button>--%>
                        <%--</form>--%>
                        <%--<form action="/valdacHost/kouji/${kouji.id}/kiki" id="valveList" method="post">--%>
                            <%--<button class="btn btn-success btn-sm">Next</button>--%>
                        <%--</form>--%>
                    </div>
                </div>
            </div>
            <!-- information tab -->

            <div class="col-md-4">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-md-10"><i class="fa fa-list-ul"></i> 選択された</div>
                            <div class="col-md-2"><i class="fa fa-check"></i> <span id="selectedCount">0</span></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-1">
                            <div class="btn-group">
                                <div class="input-group">
                                    <input type="text" id="selected-keyword" name="selected-keyword"  class="form-control" style="width: 150px;" placeholder="Filter">
                                      <span class="input-group-btn">
                                        <button type="button" class="btn btn-default pull-left" onclick="filterSelectedByKeywords()"><i class="fa fa-search"></i> </button>
                                      </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body table-panel">
                        <table id="kikiSys-selected-table" class="table table-hover kikiSys-selected-table">

                            <tr>
                                <%--<th><input type="checkbox" class="headCheckbox" onclick="selectAllItem()"></th>--%>
                                <th width="30%">弁番号</th>
                                <th width="60%">弁名称</th>
                                <th width="10%"></th>
                            </tr>

                            <c:forEach items="${valveList}" var="valve">
                                <tr class='selected-item' id="selected-${valve.kikiSysId}">
                                    <%--<td><input type="checkbox" class="checkbox" style="opacity: 0" value="${valve.kikiSysId}"></td>--%>
                                    <td>${valve.vNo}</td>
                                    <td>${valve.benMeisyo}</td>
                                    <td><button class="btn btn-default btn-xs" onclick="removeSelected(${valve.kikiSysId})">X</button></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-md-12">
                                <form action="/valdacHost/kouji/${kouji.id}/valve" id="valveListForm" method="post">
                                    <input type="hidden" name="idList" id="idList" value="" />
                                    <button class="btn btn-success btn-block" onclick="submitForm()">確定 <i class="fa fa-angle-double-right"></i></button>
                                </form>
                            </div>
                            <%--<div class="col-md-2"></div>--%>
                            <%--<div class="col-md-5">--%>
                                <%--<form action="/valdacHost/kouji/${kouji.id}/kiki" id="valveList" method="post">--%>
                                    <%--<button class="btn btn-success btn-sm">Next</button>--%>
                                <%--</form>--%>
                            <%--</div>--%>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>
</div>
<script type="text/javascript">
    $(document).ready(function(){
        $('#spinner').show();
        $('.selected-item').each(function(){
            var trId = new String($(this)[0].id);
            var id = trId.split('-')[1];
            var tr = $("#select-"+id);
            var selected = $(tr).find('.checkbox');
            var checkbox = selected[0];
            $(checkbox).prop('checked', true);
            $(tr).addClass("bg-light-blue-gradient");
        });
        $('#spinner').hide();
        updateSelectedListCount();

        $('.valve-item').mouseup(function(obj){
            var tr = obj.currentTarget;
            var selected = $(tr).find('.checkbox');
            var checkbox = selected[0];
            if($(checkbox).prop("checked") == true){
                $(checkbox).prop('checked', false);
                $(tr).removeClass("bg-light-blue-gradient");
                $("#selected-"+checkbox.value).remove();
            } else {
                $(checkbox).prop('checked', true);
                $(tr).addClass("bg-light-blue-gradient");
                var details = $(tr).find('.selected-detail');
                var htmlContent = $('#kikiSys-selected-table').html();
                htmlContent=htmlContent+
                        "<tr class='selected-item' id='selected-"+checkbox.value+"'>"+
                            "<td>"+$(details[0]).html()+"</td>"+
                            "<td>"+$(details[1]).html()+"</td>"+
                            "<td><button class='btn btn-default btn-xs' onclick='removeSelected("+checkbox.value+")'>X</button></td>"+
                        "</tr>";
                $('#kikiSys-selected-table').html(htmlContent);
            }
            updateSelectedListCount();
        });
//        $('#vNo').keyup(function(e){
//            if(e.keyCode == 13){
//                filterByKeywords();
//            }
//        });
    });

    function removeSelected(id){
        var tr = $('#select-'+id);
        var selected = $(tr).find('.checkbox');
        console.log("selected.length="+selected.length);
        console.log("id="+id);
        var checkbox = selected[0];
        $(checkbox).prop('checked', false);
        $(tr).removeClass("bg-light-blue-gradient");
        $("#selected-"+id).remove();

        updateSelectedListCount();
    }

    function selectAllItem(){
        if(!$('.headCheckbox').prop('checked')){
            //選択をはずす
            console.log("000000選択を外す");
            $('.select-table').find('.checkbox').each(function(){
                var checkbox = $(this)[0];
                if($(checkbox).prop('checked')== true) {
                    $(checkbox).prop('checked', false);
                    //右リストから削除
                    $("#selected-"+checkbox.value).remove();

                }
            });
            $('.select-table').find('.valve-item').each(function(){
                var tr = $(this)[0];
                if($(tr).hasClass('bg-light-blue-gradient')){
                    $(tr).removeClass("bg-light-blue-gradient");
                }
            });


        } else {
            //選択される
            console.log("000000選択に追加");
            $('.select-table').find('.checkbox').each(function(){
                var checkbox = $(this)[0];
                if($(checkbox).prop('checked')== true) {
                }
                else {
                    $(checkbox).prop('checked', true);
                    //右リストに追加
                    var details = $(this).parents("tr").find('.selected-detail');
                    var htmlContent = $('#kikiSys-selected-table').html();
                    htmlContent=htmlContent+
                            "<tr class='selected-item' id='selected-"+checkbox.value+"'>"+
                            "<td>"+$(details[0]).html()+"</td>"+
                            "<td>"+$(details[1]).html()+"</td>"+
                            "<td><button class='btn btn-default btn-xs' onclick='removeSelected("+checkbox.value+")'>X</button></td>"+
                            "</tr>";
                    $('#kikiSys-selected-table').html(htmlContent);
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
        updateSelectedListCount();
    }

    function submitForm(){
        var idList = "";
//        $('.checkbox:checked').each(function(){
//            idList = idList + $(this)[0].value + ',';
//        });
//        console.log("idList old="+idList);
        //チェックリストではなく、右の選択されたリストにする
        $('.selected-item').each(function(){
            var trId = new String($(this)[0].id);
            var id = trId.split('-')[1];
            idList = idList +id + ',';
        });
        console.log("idList new="+idList);
        $('#idList').val(idList);
//        return false;
    }

    function searchByVNO(){
        var koujiId = $('#koujiId').val();
        var vNo = $('#vNo').val();
        $.post('/valdacHost/item/getKikiSysIdByVNoJson',
                ({"vNo":vNo,
                  "koujiId":koujiId}),
        function(data){
            var items = JSON.parse(data);
        });
    }
    //弁リスト　Filter
    function filterByKeywords(){
        $('#spinner').show();
        var keyword = $('#vNo').val();
        keyword=keyword.toLowerCase();
        keyword=keyword.trim();

        $('.valve-item').hide();
        var trs = $('.valve-item');
        for(var i = 0;i<trs.length;i++){
            var tds = $(trs[i]).children();
            for(var j = 0 ;j<tds.length;j++){
                var tmpStr = new String($(tds[j]).html());
                tmpStr=tmpStr.toLowerCase();
                if(tmpStr.match(keyword)) {
                    $(trs[i]).show();
                    break;
                }
            }
        }


        $('#spinner').hide();
    }
    //選択された弁リスト　Filter
    function filterSelectedByKeywords(){
        var keyword = $('#selected-keyword').val();
        keyword=keyword.toLowerCase();
        keyword=keyword.trim();

        $('.selected-item').hide();
        var trs = $('.selected-item');
        for(var i = 0;i<trs.length;i++){
            var tds = $(trs[i]).children();
            for(var j = 0 ;j<tds.length;j++){
                var tmpStr = new String($(tds[j]).html());
                tmpStr=tmpStr.toLowerCase();
                if(tmpStr.match(keyword)) {
                    $(trs[i]).show();
                    break;
                }
            }
        }
    }

    function updateSelectedListCount(){
        var trs = $('#kikiSys-selected-table').find('tr');
        var count = trs.length - 1;
        $('#selectedCount').html(count);
    }
</script>
</body>
</html>
