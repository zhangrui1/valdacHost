<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 11/14/14
  Time: 2:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="htmlframe/headFrame.jsp" />
<html>
<body class="skin-black">
<!-- header logo: style can be found in header.less -->
<c:import url="htmlframe/headerFrame.jsp"/>
<div class="container">
    <!-- Left side column. contains the logo and sidebar -->

        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                工事支援
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
                <div class="col-md-12">
                    <div class="nav-tabs-custom">
                        <div class="tab-content">
                            <div class="tab-pane active" id="tab_3">
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="btn-group">
                                            <select id="currentLocation">
                                                <c:forEach items="${nameList}" var="name">
                                                    <option>${name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="box-tools">
                                            <div class="input-group">
                                                <input type="text" name="table_search" id="table_search" class="form-control input-sm pull-right" style="width: 150px;" placeholder="Filter" value="${filterKouji}">
                                                <div class="input-group-btn">
                                                    <button class="btn btn-sm btn-default" id="table_search_btn" onclick="filterPage();"><i class="fa fa-search"></i></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                    <table class="table table-hover kouji-table">
                                        <thead>
                                        <tr>
                                            <th>工事番号 <span id="kouji_num" class="valve_num">(${locationKoujiNum})</span></th>
                                            <th>工事名</th>
                                            <th>設置プラント</th>
                                            <th>開始日付</th>
                                            <th>責任者</th>
                                            <th>状態</th>
                                            <th>詳細</th>
                                        </tr>
                                        </thead>
                                        <tbody id="koujiList">
                                            <c:forEach items="${locationKoujiSelectedForKouji}" var="tempkouji">
                                                <tr id="${tempkouji.id}">
                                                    <td>${tempkouji.kjNo}</td>
                                                    <td>${tempkouji.kjMeisyo}</td>
                                                    <td>${tempkouji.location}</td>
                                                    <td>${tempkouji.bgnYmd}</td>
                                                    <td>${tempkouji.person}</td>
                                                    <td>${tempkouji.status}</td>
                                                    <td><a class="btn btn-primary btn-sm operation-button-btn" href="/valdacHost/kouji/${tempkouji.id}"><i class="glyphicon glyphicon-pencil">詳細</i></a></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                            </div>
                        </div><!-- /.tab-content -->
                    </div>
                </div>
            </div>
        </section><!-- /.content -->
</div><!-- ./wrapper -->

<!-- add new calendar event modal -->

<script type="text/javascript">
    $(document).ready(function(){
        //filter関数
        filter("${filterKouji}");

        //ユーザ権限
        var userKengen=$("#userKengen").val();
        if(userKengen=="6"){
            $(".kengen-operation").show();
        }else{
            $(".kengen-operation").hide();
        }

        $(".kouji-table tr").mouseover(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","1");
        });
        $(".kouji-table tr").mouseout(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","0");
        });

        // selected に設定する設定
        var locationFirst="${locationNameSelectedForKouji}";
        var objname=document.getElementById("currentLocation");
        checkSelect(objname,locationFirst);

        function checkSelect(obj,val){
            for(var i=0;i<obj.length;i++){
                if(obj[i].value==val){
                    obj[i].selected=true;
                    break;
                }
            }
        }

        //locationにより工事情報を取得
        $("#currentLocation").change(function(){
            var location = $("#currentLocation").val();
            if(location == "会社名"){
                location = "";
            }
            //sessionに保存
            $("#table_search").val("");
            $.get("/valdacHost/item/koujiFilter",{"filterKeyword":""},function(data){
                return true;
            });
            console.log("location="+location);
            $.post("/valdacHost/kouji/getKoujiByLocation",{"location":location},function(data){
                var items = JSON.parse(data);
                makeUpTable(items);
            });
        });

    });

    //図面上のFilter
    function filterPage(){
//            検索キーワードを取得、小文字に変更し、両端の空白を削除
        var keyword = $("#table_search").val();
        keyword=keyword.toLowerCase();
        keyword=keyword.trim();
        keyword=keyword.toOneByteAlphaNumeric();//英数字　全角英数を半角英数に変換
        keyword=tozenkaku(keyword);//カタカナ　全角英数を半角英数に変換

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
                for (var j = 0; j < (tds.length-1); j++) {
                    var tmpHtml = new String(tds[j].innerHTML);
                    tmpHtml=tmpHtml.toLowerCase();//小文字に変更
                    tmpHtml=tmpHtml.toOneByteAlphaNumeric();//全角英数を半角英数に変換
                    tmpHtml=tozenkaku(tmpHtml);//カタカナ　全角英数を半角英数に変換
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
        //sessionに保存する
        if(keyword.length<1){
            keyword=""
        }

        //sessionに保存
        $.get("/valdacHost/item/koujiFilter",{"filterKeyword":keyword},function(data){
            return true;
        });
    };
    //図面描画のFilter
    function filter(){
//            検索キーワードを取得、小文字に変更し、両端の空白を削除
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
                for (var j = 0; j < (tds.length-1); j++) {
                    var tmpHtml = new String(tds[j].innerHTML);
                    tmpHtml=tmpHtml.toLowerCase();//小文字に変更
                    tmpHtml=tmpHtml.toOneByteAlphaNumeric();//全角英数を半角英数に変換
                    tmpHtml=tozenkaku(tmpHtml);//カタカナ　全角英数を半角英数に変換

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
    };
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
        $('#kouji_num').html("("+items.length+")");
        $("#koujiList").html(htmlContent);
    }
</script>

<c:import url="htmlframe/footerFrame.jsp" />

</body>
</html>
