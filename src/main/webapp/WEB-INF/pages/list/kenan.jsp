<%@ page import="com.toyo.vh.dto.KenanForm" %>
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
<body class="skin-black">
<!-- header logo: style can be found in header.less -->
<c:import url="../htmlframe/headerFrame.jsp"/>
<div class="container">
    <!-- Left side column. contains the logo and sidebar -->

    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            懸案検索
        </h1>
        <ol class="breadcrumb">
            <li><i class="fa fa-dashboard"></i> Index</li>
        </ol>
    </section>
    <hr/>
    <!-- Main content -->
    <section class="content">

        <div class="row">
            <!-- collection -->
            <div class="col-md-4">
                <div class="btn-group" role="group">
                    <a href="/valdacHost/list" class="btn btn-default">工事</a>
                    <a href="/valdacHost/list/kenan" class="btn btn-default bg-yellow active">懸案</a>
                    <a href="/valdacHost/list/tenken" class="btn btn-default">点検履歴</a>
                </div>
            </div>
            <form class="navbar-form navbar-left" role="search" id="searchForm" action="/valdacHost/kenan/search/">
                <%--<div class="form-group">--%>
                    <%--<input type="text" name="searchtext" id="searchtext" class="form-control" placeholder="懸案検索" value="">--%>
                <%--</div>--%>
                <%--<input type="submit" class="btn btn-default" value="検索"/>--%>

                <div class="input-group search-div">
                    <input type="text" class="form-control" id="keyword-input" name="keyword" placeholder="keywords" onkeypress="return check(event.keyCode);">
                        <span class="input-group-btn">
                            <input class="btn btn-default btn-flat" id="keyword-btn" type="button" value="検索"/>
                        </span>
                </div>
            </form>
        </div>


        <hr/>

        <div class="row">
            <!-- collection -->
            <div class="tab-content">

                <div class="panel-body">
                    <table class="table table-hover" id="kenan-table">
                        <thead>
                        <tr>
                            <th>弁番号</th>
                            <th>機器名</th>
                            <th>工事名</th>
                            <th>事象</th>
                            <th>部品</th>
                            <th>発見状況</th>
                            <th>発見日付</th>
                            <th>状態</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${kenanFormListSearch}" var="kenanForm">
                            <tr class="data-tr" id="${kenanForm.id}">
                                <td class="data-td">${kenanForm.valve.vNo}</td>
                                <td class="data-td">${kenanForm.kiki.kikiMei}</td>
                                <td class="data-td">${kenanForm.kouji.kjMeisyo}</td>
                                <td class="data-td">${kenanForm.gensyo}</td>
                                <td class="data-td">${kenanForm.buhin}</td>
                                <td class="data-td">${kenanForm.hakkenJyokyo}</td>
                                <td class="data-td">${kenanForm.hakkenDate}</td>
                                <td class="data-td">${kenanForm.taiouFlg}</td>
                                <td>
                                    <button onclick="editKenan(this)" class="btn btn-xs btn-default bg-red-gradient"><i class="fa fa-edit">詳細</i></button>
                                    <%--<button onclick="deleteKenan(this)" class="btn btn-xs btn-default bg-blue-gradient"><i class="glyphicon glyphicon-trash">Delete</i></button>--%>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>



        <div class="modal fade" id="kenanModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                        <h4 class="modal-title" id="myModalLabel">懸案詳細</h4>
                    </div>
                    <div class="modal-body">
                        <form id="kenanForm" action="/valdacHost/kenan/updateKenanSearch" name="kenanForm" method="post">

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

                            <div class="panel panel-danger">
                                <div class="panel-heading">
                                    <h3 class="panel-title">懸案状況</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="row form-group">
                                        <div class="col-md-3">
                                            <input type="text" name="jisyo" id="jisyo" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="事象" />
                                        </div>
                                        <div class="col-md-3">
                                            <input type="text" name="buhin" id="buhin" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="部品" />
                                        </div>
                                        <div class="col-md-3">
                                            <input type="text" name="gensyo" id="gensyo" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="現象" />
                                        </div>
                                        <div class="col-md-3">
                                            <input type="text" name="taisaku" id="taisaku" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="対策" />
                                        </div>
                                    </div>

                                    <div class="row form-group">
                                        <div class="col-md-4">
                                            <input type="text" name="hakkenJyokyo" id="hakkenJyokyo" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="発見状況" />
                                        </div>
                                        <div class="col-md-4">
                                            <input type="text" name="youin" id="youin" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="要因" />
                                        </div>
                                        <div class="col-md-4">
                                            <input type="text" name="syotiNaiyou" id="syotiNaiyou" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="処置内容" />
                                        </div>
                                    </div>
                                    <div class="row form-group">
                                        <div class="col-md-4">
                                            <input type="text" name="hakkenDate" id="hakkenDate" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="発見日付" />
                                        </div>
                                        <div class="col-md-4">
                                            <input type="text" name="taisakuDate" id="taisakuDate" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="対策日付" />
                                        </div>
                                        <div class="col-md-4">
                                            <input type="text" name="taiouFlg" id="taiouFlg" class="form-control kenanForm-input" readonly="readonly" value="" placeholder="対応" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <%--<div class="modal-footer">--%>
                        <%--<button type="button" onclick="updateKenan()" class="btn btn-primary">保存</button>--%>
                    <%--</div>--%>
                </div>
            </div>


        </div>

    </section><!-- /.content -->
</div><!-- ./wrapper -->

<%--<%--%>
    <%--List<KenanForm> kenanFormListSearch=(List<KenanForm>)session.getAttribute("kenanFormListSearch");--%>
<%--%>--%>
<form name="hogeForm">
    <input type="hidden" name="hoge" value="<%=(List<KenanForm>) session.getAttribute("kenanFormListSearch")%>">
</form>
<script type="text/javascript">
    function editKenan(obj){
        var kenanTr = $(obj).parent().parent();
        var kenanId = kenanTr[0].id;
        $.get("/valdacHost/kenan/getKenanByIdInSessionSearch",{"id":kenanId},function(data){
            var koujiFormData = JSON.parse(data);
            $(".kenanForm-input").val("");
            var koujiInfo = koujiFormData.kouji.kjNo + "/" + koujiFormData.kouji.kjMeisyo;
            $("#koujiInfo").html(koujiInfo);
            var kikisysInfo = koujiFormData.valve.vNo + " ( " + koujiFormData.valve.benMeisyo + " ) / " + koujiFormData.kiki.kikiMei;
            $("#kikisysInfo").html(kikisysInfo);

            $("#kenanId").val(kenanId);
            $("#kenanKoujiId").val(koujiFormData.kouji.id);
            $("#kenanKoujirelationId").val(koujiFormData.koujirelationId);
            $("#kenanKikiId").val(koujiFormData.kikiId);
            $("#jisyo").val(koujiFormData.jisyo);
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

    function deleteKenan(obj) {
        var kenanTr = $(obj).parent().parent();
        var kenanId = kenanTr[0].id;
        $("#kenanId").val(kenanId);

        if (!confirm("この行を削除しますか？"))
            return;

        $.get("/valdacHost/kenan/deleteSearch",{"id":kenanId},function(data){
            var objTR=obj.parentNode.parentNode;
            var objTBL=objTR.parentNode;
            if(objTBL){
                objTBL.deleteRow(objTR.sectionRowIndex);
            }
        });
    }

    function searchDataTd(obj){
        if(obj.value.length < 1){
            $(".data-tr").show();
        }
        var dataTr = $(".data-tr");
        var keyword = new String(obj.value);
        keyword=keyword.toLowerCase();
        keyword=keyword.trim();

        for(var i = 0;i<dataTr.length;i++){
            $(dataTr[i]).hide();
            var dataTd = $(dataTr[i]).find(".data-td");
            for(var j = 0;j<dataTd.length;j++){
                var htmlData = new String(dataTd[j].innerHTML);
                htmlData=htmlData.toLowerCase();
                if(htmlData.indexOf(keyword) > -1){
                    $(dataTr[i]).show();
                    break;
                }
            }
        }
    }

    function updateKenan(obj){
        $("#kenanForm").submit();
    }

</script>

<script type="text/javascript">

    $(document).ready(function(){

        $(".kenan-table tr").mouseover(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","1");
        });
        $(".kenan-table tr").mouseout(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","0");
        });

        $("#table_search").keyup(function(){
            var keyword = $("#table_search").val();
            keyword=keyword.toLowerCase();
            keyword=keyword.trim();

            if(keyword.length > 0) {
                var trs = $(".active .kenan-table tbody tr");
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
                var trs = $(".active .kenan-table tbody tr");
                for (var i = 0; i < trs.length; i++) {
                    $(trs[i]).show();
                }
            }
        });

    });


    //検索チェック用
    $("#keyword-input").focus(function(){
        $(".search-div").animate({"margin-left":"0"},300,function(){
            $("#keyword-btn").toggleClass("bg-red");
        });
    });
    $("#keyword-input").blur(function(){
        $(".search-div").animate({"margin-left":"50%"},300,function(){
            $("#keyword-btn").toggleClass("bg-red");
        });
    });

    $(document).ready(function(){
        $("#keyword-btn").click(function(){
            var keywords = new String($("#keyword-input").val());
            keywords = keywords.toLowerCase();
            if(keywords.length<1){
                return false;
            } else if(keywords.charAt(keywords.length-1) == '-' || keywords.charAt(keywords.length-1) == '/') {
                alert("キーワードは正しくありません");
                return false;
            } else {
                var ills = new Array();
                ills = ['+', '&&', '||', '!', '(', ')' ,'{' ,'}', '[', ']', '^', '"', '~', '*', '?', ':'];
                for(var i = 0;i<ills.length;i++){
                    if(keywords.indexOf(ills[i]) > -1){
                        alert("キーワードは正しくありません");
                        return false;
                    }
                }

            }
            $("#keyword-input").val(keywords);
            $("#searchForm").submit();
        });
    });
    //検索キーワードにEnterキーを押す場合チェック
    function check(code){
        if(code==13){
            var keywords = new String($("#keyword-input").val());
            keywords = keywords.toLowerCase();
            if(keywords.length<1){
                return false;
            } else if(keywords.charAt(keywords.length-1) == '-' || keywords.charAt(keywords.length-1) == '/') {
                alert("キーワードは正しくありません");
                return false;
            } else {
                var ills = new Array();
                ills = ['+', '&&', '||', '!', '(', ')' ,'{' ,'}', '[', ']', '^', '"', '~', '*', '?', ':'];
                for(var i = 0;i<ills.length;i++){
                    if(keywords.indexOf(ills[i]) > -1){
                        alert("キーワードは正しくありません");
                        return false;
                    }
                }

            }
            $("#keyword-input").val(keywords);
            $("#searchForm").submit();
        }
    }
</script>

<c:import url="../htmlframe/footerFrame.jsp" />

</body>
</html>

