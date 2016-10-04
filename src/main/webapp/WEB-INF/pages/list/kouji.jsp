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
            工事検索
        </h1>
        <%--<ol class="breadcrumb">--%>
            <%--<li><i class="fa fa-dashboard"></i> Index</li>--%>
        <%--</ol>--%>
    </section>
    <hr/>
    <!-- Main content -->
    <section class="content">

        <div class="row">
            <!-- collection -->
            <div class="col-md-2">
                <div class="btn-group" role="group">
                    <a href="/valdacHost/list" class="btn btn-default bg-yellow active">工事検索</a>
                    <%--<a href="/valdacHost/list/kenan" class="btn btn-default">懸案</a>--%>
                    <%--<a href="/valdacHost/list/tenken" class="btn btn-default">点検履歴</a>--%>
                    <a href="/valdacHost/list/valve" class="btn btn-default">弁検索</a>
                </div>
            </div>
            <div class="col-md-3">
                <form class="btn-group" role="search" id="searchForm" action="/valdacHost/kouji/search/" method="post">
                    <div class="input-group search-div">
                        <input type="text" class="form-control" id="keyword-input" name="keyword" placeholder="keywords" onkeypress="return check(event.keyCode);">
                            <span class="input-group-btn">
                                <input class="btn btn-default btn-flat" id="keyword-btn" type="button" value="検索"/>
                            </span>
                    </div>
                </form>
            </div>
            <div class="col-md-3">
                <div class="btn-group">
                    <select id="currentLocation">
                        <option>会社名</option>
                        <c:forEach items="${nameList}" var="name">
                            <option>${name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-md-3">
                <div class="btn-group">
                    <div class="input-group">
                        <input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter">
                        <div class="input-group-btn">
                            <button class="btn btn-sm btn-default" id="table_search_btn"><i class="fa fa-search"></i></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <hr/>

        <div class="row">
            <!-- collection -->
            <div class="tab-content">

                <div class="tab-pane active" id="tab_1">
                    <table class="table table-hover kouji-table">
                        <thead>
                        <tr>
                            <th>工事番号</th>
                            <th>工事名</th>
                            <th>設置プラント</th>
                            <th>開始日付</th>
                            <th>責任者</th>
                            <th>状態</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="koujiList">
                        <c:forEach items="${searchKoujiList}" var="kouji">
                            <tr id="${kouji.id}">
                                <td>${kouji.kjNo}</td>
                                <td>${kouji.kjMeisyo}</td>
                                <td>${kouji.location}</td>
                                <td>${kouji.bgnYmd}</td>
                                <td>${kouji.person}</td>
                                <td>
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
                                </td>
                                <td>
                                    <%--<div class="operation-button">--%>
                                        <a class="btn btn-primary btn-sm operation-button-btn" href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-pencil">詳細</i></a>
                                    <%--</div>--%>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div><!-- /.tab-pane -->
            </div>
        </div>

    </section><!-- /.content -->
</div><!-- ./wrapper -->


<script type="text/javascript">
    $(document).ready(function(){

        $(".kouji-table tr").mouseover(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","1");
        });
        $(".kouji-table tr").mouseout(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","0");
        });

        $("#table_search").keyup(function(){
            var keyword = $("#table_search").val();
            keyword=keyword.toLowerCase();
            keyword=keyword.trim();

            if(keyword.length > 0) {
                var trs = $(".active .kouji-table tbody tr");
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
                var trs = $(".active .kouji-table tbody tr");
                for (var i = 0; i < trs.length; i++) {
                    $(trs[i]).show();
                }
            }
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
        $("#keyword-btn").click(function(){
            var keywords = new String($("#keyword-input").val());
            keywords = keywords.toLowerCase();
            keywords=keywords.trim();
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

        //locationにより工事情報を取得
        $("#currentLocation").change(function(){
            var location = $("#currentLocation").val();
            if(location == "会社名"){
                location = "";
            }

            $.post("/valdacHost/kouji/getKoujiByLocation",{"location":location},function(data){
                var items = JSON.parse(data);
                makeUpTable(items);
            });
        });
    });
    function makeUpTable(items){
        $("#koujiList").html("");
        var htmlContent = "";
        for(var i = 0;i<items.length;i++){
            htmlContent =
                    htmlContent + '' +
                    '<tr id="'+items[i].id+'">' +
                    '<td>'+items[i].kjNo+'</td>' +
                    '<td>'+items[i].kjMeisyo+'</td>' +
                    '<td>'+items[i].location+'</td>' +
                    '<td>'+items[i].bgnYmd+'</td>' +
                    '<td>'+items[i].person+'</td>' +
                    '<td>'+items[i].status+'</td>' +
                    '<td>' +
                    '<a class="btn btn-primary btn-sm operation-button-btn" href="/valdacHost/kouji/'+items[i].id+'"><i class="glyphicon glyphicon-pencil">編集</i></a>' +
                    '</td>' +
                    '</tr>';
        }

        $("#koujiList").html(htmlContent);
    }
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

