<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 11/18/14
  Time: 10:09 AM
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
            <input type="hidden" id="currentPage" value="0" />
            <div class="col-md-11">
                <ul class="timeline" id="tenkenRireki">
                </ul>
                <input type="button" class="btn btn-block btn-sm btn-default bg-blue-gradient" value="もっと" onclick="changePage()" />
            </div>
            <!-- information tab -->
            <div class="col-md-1">
                <div class="row">
                    <div class="col-md-12">
                        <ul class="nav nav-pills nav-stacked bookmarkUl">
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-cog"> 情報</i></a></li>
                            <li role="presentation" class="kengen-operation"><a href="/valdacHost/kouji/${kouji.id}/instruct"><i class="glyphicon glyphicon-indent-left"> 指示</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/kenan"><i class="glyphicon glyphicon-floppy-save"> 懸案</i></a></li>
                            <li role="presentation" class="currentBookmark"><a href="/valdacHost/kouji/${kouji.id}/history"><i class="glyphicon glyphicon-time"> 履歴</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/image"><i class="glyphicon glyphicon-picture"> 図面</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/printhtml"><i class="glyphicon glyphicon-download"> DL</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/gpPrinthtml"><i class="glyphicon glyphicon-download"> GP&ICS</i></a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <!-- tab button tab -->
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

        changePage();
    });

    function changePage(){
        var currentPage = $("#currentPage").val();
        $.get("/valdacHost/tenken/getTenkenrirekiHitoryByPage",{"currentPage":currentPage},function(data){
            var datas = JSON.parse(data);
            console.log(datas);
            var originalHTML = $("#tenkenRireki").html();
            var tmpHtml = "";
            for(var i = 0;i<datas.tenkenRirekiList.length;i++){


                tmpHtml = tmpHtml +
                        '<li>'+
                        '<i class="glyphicon glyphicon-check bg-green"></i>'+
                        '<div class="timeline-item">'+
                        '<span class="time"><i class="fa fa-clock-o"></i> '+datas.tenkenRirekiList[i].updDate+'</span>'+
                        '<h3 class="timeline-header"><a href="#">'+datas.tenkenRirekiList[i].valve.vNo+' : 点検内容　　'+datas.tenkenRirekiList[i].tenkennaiyo+'</a> </h3>'+
                        '<div class="timeline-body">'+
                        datas.tenkenRirekiList[i].valve.vNo+' ( '+datas.tenkenRirekiList[i].valve.benMeisyo+' ) の '+
                        datas.tenkenRirekiList[i].kiki.kikiBunrui+' ( '+datas.tenkenRirekiList[i].kiki.kikiMei+' ) -> '+ datas.tenkenRirekiList[i].tenkenRank+' ( '+datas.tenkenRirekiList[i].tenkenkekka+' ) '+
                        '</div>'+
                        '</div>'+
                        '</li>';
            }
            $("#tenkenRireki").html(originalHTML + tmpHtml);
            $("#currentPage").val(datas.currentPage+1);


        });
    }
</script>
</html>
