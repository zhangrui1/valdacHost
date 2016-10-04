<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 12/4/14
  Time: 12:05 AM
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
                <div class="row">
                    <div class="col-md-8">
                        <div class="btn-group">
                            <a class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false">
                                過去懸案リスト <i class="fa fa-angle-down"></i>
                            </a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="/valdacHost/kouji/${kouji.id}/kenan">今回懸案リスト</a></li>
                                <li><a href="/valdacHost/kouji/${kouji.id}/kenanPast">過去懸案リスト</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-md-2 input-group">
                        <input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter">
                        <div class="input-group-btn">
                            <button class="btn btn-sm btn-default" id="table_search_btn" onclick="searchDataTd()"><i class="fa fa-search"></i></button>
                        </div>
                    </div>
                </div>

                <div class="panel">
                    <div class="panel-body">
                        <div class="col-md-3" style="padding-right: 0px">
                            <ul class="list-group tab-list">
                                <c:forEach items="${valvesLastYear}" var="valve">
                                    <li onclick="chooseThisValveLastKenan(this)" class="list-group-item tab-item">
                                        <div class="row">
                                            <div class="col-md-5">${valve.vNo}</div>
                                            <div class="col-md-7">${valve.benMeisyo}</div>
                                        </div>
                                        <input type="hidden" class="kikiSysId" value="${valve.kikiSysId}"/>
                                    </li>
                                </c:forEach>
                            </ul><!-- 弁 list -->
                        </div>
                        <div class="col-md-9" style="padding-left: 0px;box-shadow: 0px 2px 5px 2px #ccc;">
                            <div class="kenanList-div">
                                <table class="table table-hover" id="kenan-table">
                                    <thead>
                                    <tr>
                                        <th>状態</th>
                                        <th>発見日付</th>
                                        <%--<th>対策日付</th>--%>
                                        <th>機器の事象</th>
                                        <th>部品・箇所</th>
                                        <th>損傷の状況</th>
                                        <%--<th>損傷の要因</th>--%>
                                        <%--<th>改善対策</th>--%>
                                        <%--<th>処置内容</th>--%>
                                        <th>詳細</th>
                                    </tr>
                                    </thead>
                                    <tbody class="list-group item-list" id="kenanList-last">

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- information tab -->
            <div class="col-md-1">
                <div class="row">
                    <div class="col-md-12">
                        <ul class="nav nav-pills nav-stacked bookmarkUl">
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-cog"> 情報</i></a></li>
                            <li role="presentation" class="currentBookmark"><a href="/valdacHost/kouji/${kouji.id}/kenan"><i class="glyphicon glyphicon-floppy-save"> 懸案</i></a></li>
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
                <h4 class="modal-title" id="myModalLabel">懸案状況</h4>
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
                <%--<button type="button" onclick="updateKenan()" class="btn btn-primary">保存</button>--%>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        //ユーザ権限
        var userKengen=$("#userKengen").val();
        if(userKengen=="6" || userKengen=="5"){//管理者　責任者
            $(".kengen-operation").show();
        }else{
            $(".kengen-operation").hide();
        }
    });

    function searchDataTd(){
        var keyword = $("#table_search").val();
        keyword=keyword.toLowerCase();
        keyword=keyword.trim();
        if(keyword.length < 1){
            $('.tab-item').show();
        } else {
            var datas = $('.tab-item');
            for(var i = 0;i<datas.length;i++){
                var tmpHtml = new String(datas[i].innerText);
                if(tmpHtml.indexOf(keyword) > -1){
                    var li = $(datas[i]);
                    $(li).show();
                } else {
                    var li = $(datas[i]);
                    $(li).hide();
                }
            }
        }
    }


    //弁Dにより、今回懸案リスト
    function chooseThisValveLastKenan(obj){
        $('.tab-item').removeClass("active");
        $(obj).addClass("active");
        var koujiId=${kouji.id};
        var id = $(obj).find(".kikiSysId")[0];
        var kikiSysId=id.value;
        $.post("/valdacHost/kouji/kenanLastYear",{"koujiId":koujiId,"kikiSysId":kikiSysId},function(data){
                    var kikiDatas = JSON.parse(data);
                    $("#kenanList-last").html("");
                    var tmpHTML = "";
                    for(var i = 0; i < kikiDatas.length;i++){
                        tmpHTML =tmpHTML+'<tr class="data-tr" id="'+kikiDatas[i].id+'">'+
                                '<td class="data-td">'+kikiDatas[i].taiouFlg+'</td>'+
                                '<td class="data-td">'+kikiDatas[i].hakkenDate+'</td>'+
//                            '<td class="data-td">'+kikiDatas[i].taisakuDate+'</td>'+
                                '<td class="data-td">'+kikiDatas[i].hakkenJyokyo+'</td>'+
                                '<td class="data-td">'+kikiDatas[i].buhin+'</td>'+
                                '<td class="data-td">'+kikiDatas[i].gensyo+'</td>'+
//                            '<td class="data-td">'+kikiDatas[i].youin+'</td>'+
//                            '<td class="data-td">'+kikiDatas[i].taisaku+'</td>'+
//                            '<td class="data-td">'+kikiDatas[i].syotiNaiyou+'</td>'+
                                '<td><button onclick="editKenanTest(this)" class="btn btn-xs btn-warning"><i class="fa fa-edit">詳細</i></button>&nbsp;&nbsp;' +
                                '<button onclick="deleteKenan(this)" class="btn btn-xs btn-danger"><i class="glyphicon glyphicon-trash">削除</i></button>'+
                                '</td>'+
                                '</tr>';
                    }
                    $('#kenanList-last').html(tmpHTML);
                }
        );
    }

    function editKenanTest(obj){
        var kenanTr = $(obj).parent().parent();
        var kenanId = kenanTr[0].id;
        $.post("/valdacHost/kenan/getKenanByKenanId",{"kenanId":kenanId},function(data){
            var koujiFormData = JSON.parse(data);
            $(".kenanForm-input").val("");
            var koujiInfo ="工事："+ koujiFormData.kouji.kjNo + "/" + koujiFormData.kouji.kjMeisyo;
            $("#koujiInfo").html(koujiInfo);
            var kikisysInfo ="弁："+ koujiFormData.valve.vNo + " ( " + koujiFormData.valve.benMeisyo + " ) / " + koujiFormData.kiki.kikiMei+ "  / " + koujiFormData.kikisysId;
            $("#kikisysInfo").html(kikisysInfo);
            $("#kenanId").val(kenanId);
            $("#kenanKoujiId").val(koujiFormData.koujiId);
            $("#kenanKoujirelationId").val(koujiFormData.koujirelationId);
            $("#kenanKikiId").val(koujiFormData.kikiId);
            $("#kenanKikiSystemId").val(koujiFormData.kikisysId);
            $("#buhin").val(koujiFormData.buhin);
            $("#gensyo").val(koujiFormData.gensyo);
            $("#taisaku").val(koujiFormData.taisaku);
            $("#hakkenJyokyo").val(koujiFormData.hakkenJyokyo);
            $("#youin").val(koujiFormData.youin);
            $("#syotiNaiyou").val(koujiFormData.syotiNaiyou);
            $("#hakkenDate").val(koujiFormData.hakkenDate);
            $("#taisakuDate").val(koujiFormData.taisakuDate);
            $("#taiouFlg").val(koujiFormData.taiouFlg);
            $("#kenanModal").modal();
        })
    }

</script>
</html>