<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 12/4/14
  Time: 11:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp" />
<style type="text/css">
    /* loading */
    #image-box-base #loading {
        display:none;
        position:absolute;
        top:0;left:0;
        width:100%;height:100%;
        background:url("/valdacHost/img/loading.gif") no-repeat center center;
        border-style: none;
        /*background-color:transparent;*/
    }
</style>
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
    <input type="hidden" id="koujiId" value="${kouji.id}" />
    <section class="content">

        <div class="row">
            <div class="col-md-11">

                <!--                  -->
                <div class="box box-solid">
                    <div class="box-body">
                        <div class="form-group">

                            <div class="row">
                                <!-- top frame -->
                                <div class="col-md-2">
                                    <!-- new image -->
                                    <div class="btn btn-block btn-warning btn-file-block kengen-operation-readonly">
                                        <span class="glyphicon glyphicon-picture"></span>
                                        <span class="glyphicon-class">図面アップロード</span>
                                        <input type="file" id="filePicker" name="attachment[]" multiple="multiple"><!-- id must be filePicker -->
                                    </div>
                                    <!--Add a button for the user to click to initiate auth sequence -->
                                    <button id="authorize-button" style="visibility: hidden">メール認証</button><br>
                                    <span class="authorize-message" id="authorize-message" style="color:#ff0000;visibility: hidden ">「メール認証」ボタンを押してから、図面をアップロードしてください</span>
                                    </br></br>
                                </div>

                                <div class="col-md-10">
                                    <div class="progress">
                                        <div id="totalProgressbar" class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="300" style="width: 0%">
                                            <span id="totalProgress"></span>
                                        </div>
                                    </div>
                                    <div class="progress xs progress-striped active">
                                        <div id="progressbar" class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
                                            <span id="currentProgress"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%--印刷ボタンなど--%>
                            <div class="row">
                                <div class="col-md-2">
                                    <button type="button" class="btn btn-default" onclick="printImage()" value=""/>印刷</button>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" id="firstButton" class="btn btn-default" onclick="turnToFirstPage()" value=""/>先頭へ</button>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" id="previousButton" class="btn btn-default" onclick="turnToPreviousPage()" value=""/>前ページ</button>
                                </div>
                                <div class="col-md-1">
                                    <span id="currentSet">${currentSet}</span>/<span id="totalSet">${totalSet}</span>
                                </div>
                                <div class="col-md-1">
                                    <button type="button" id="NextButton" class="btn btn-default" onclick="turnToNextPage()" value=""/>  次ページ</button>
                                </div>
                                <div class="col-md-2">
                                    <button type="button" id="lastButton" class="btn btn-default" onclick="turnToLastPage()" value=""/>最終へ</button>
                                </div>
                                <div class="col-md-4">
                                    <div class="row">
                                        <div class="col-md-2">
                                            <input type="text" class="form-control input-sm" name="jump_page_num" id="jump_page_num" style="width: 50px;" value=""/>
                                        </div>
                                        <div class="col-md-2">
                                            <button type="button" class="btn btn-default" onclick="turnToNumPage()" value=""/>ページに遷移</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="previewDiv">
                                    <button type="button" class="btn btn-default" onclick="routeImageForKouji(1)" value=""/>右90度</button>
                                    <button type="button" class="btn btn-default" onclick="routeImageForKouji(0)" value=""/>左90度</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                                    <input type="text"   name="index_page_num" id="index_page_num" style="width: 50px;" value=""/>
                                    <button type="button" id="index_num" class="btn btn-default" onclick="changeNumPage()" value=""/>インデックス変更</button>
                                </div>

                            </div>

                            <div class="row previewDiv">
                                <!-- image row -->
                                <!-- image column  -->
                                <div class="col-md-8" id="image-box-base">
                                    <%--<embed id="previewImage" class="previewImage" src="" />--%>
                                    <div  id="koujiImg">
                                        <%--<c:if test="${not empty firstReportImage}">--%>
                                            <%--<img  id="previewImage" class="previewImage"  style="border:solid 1px silver" width="60%"  src="http://storage.googleapis.com/valdacHost/${firstReportImage.imagename}" width="600" />--%>
                                            <img  id="previewImage" class="previewImage"  style="border:solid 1px silver" width="60%"  src="" width="600" />
                                        <%--</c:if>--%>
                                    </div>
                                    <div id="loading"> </div>
                                    <div id="previewImageName" style="display: none"></div>
                                </div>

                                <!-- select data column -->
                                <div class="col-md-4" id="operate-panel">

                                    <div class="row form-group">

                                        <input type="hidden" id="currentImageObject" value="<c:if test="${not empty firstReportImage}">${firstReportImage.imagename}</c:if>">
                                    </div>

                                    <div class="form-group">
                                        <select class="form-control input-sm imagesyu kengen-operation-readonly" onchange="saveChangeToDatabase(this)" id="currentSyu"><option><c:if test="${not empty firstReportImage}">${firstReportImage.imagesyu}</c:if></option><option>図面仕様書</option><option>点検報告書</option><option>懸案事項一覧</option><option>完成図面</option><option>作業指示書</option><option>弁棒ねじ検索記録</option></select>
                                    </div>
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h3 class="panel-title">関連弁</h3>
                                        </div>
                                        <div class="panel-body">
                                            <%--<div class="form-group">--%>
                                                <div class="row">
                                                    <div class="col-md-10">
                                                        <input type="text" name="table_search" id="table_search" class="input-sm pull-left" style="width: 150px;" placeholder="弁番号 Filter">
                                                        <%--<div class="input-group-btn pull-left col-md-5">--%>
                                                            <button class="btn btn-sm btn-default" id="table_search_btn" onclick="findBenByKeywords()"><i class="fa fa-search"></i></button>
                                                        <%--</div>--%>
                                                    </div>
                                                    <div class="col-md-2">
                                                        <button class="btn btn-default btn-sm kengen-operation  pull-right" onclick="openKanrenben()">+</button>
                                                    </div>
                                                </div>
                                            <%--</div>--%>
                                            <div class="form-group">
                                                <div class="ben-table">
                                                    <table class="table table-hover" id="selected-ben-to-image">
                                                        <thead>
                                                        <tr>
                                                            <th>弁番号</th>
                                                            <th>弁名称</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody id="current-select-ben" style="text-align: left">
                                                            <c:forEach items="${firstValveImageList}" var="image">
                                                                <tr>
                                                                    <td>${image.vNo}</td>
                                                                    <td>${image.benMeisyo}</td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group-sm">
                                        <button class="btn btn-block btn-danger kengen-operation" onclick="removeImage(this)">画像を削除</button>
                                    </div>

                                </div>
                            </div>




                            <%--<div class="row">--%>

                                <%--<div class="col-md-12" style="text-align: center">--%>
                                    <%--<!-- bottom frame -->--%>

                                    <%--<div class="row">--%>
                                        <%--<div class="col-md-12" id="previewImageDiv">--%>
                                            <%--<hr/>--%>

                                            <%--<div class="row" id="imageList">--%>
                                                <%--<div class="col-md-1">--%>
                                                    <%--<button class="btn btn-default btn-block image-page-btn" onclick="turnToPreviousPage()"><</button>--%>
                                                <%--</div>--%>
                                                <%--<div class="col-md-10">--%>
                                                    <%--<div class="row" id="thumbnail-imageList">--%>
                                                    <%--<c:forEach items="${imageList}" var="image">--%>
                                                        <%--<div id="${image.imagename}" class="col-md-2 image-div" >--%>
                                                            <%--<div class="row thumbnail-object">--%>
                                                                <%--<div class="col-md-12 thumbnail-img">--%>
                                                                    <%--<img src="http://storage.googleapis.com/valdacHost/${image.imagename}" onclick="changeImage(this)" alt="..." style="cursor:pointer;height: 100px">--%>
                                                                <%--</div>--%>
                                                            <%--</div>--%>
                                                        <%--</div>--%>
                                                    <%--</c:forEach>--%>

                                                    <%--</div>--%>
                                                <%--</div>--%>
                                                <%--<div class="col-md-1">--%>
                                                    <%--<button class="btn btn-default btn-block image-page-btn" onclick="turnToNextPage(${currentSet})">></button>--%>
                                                <%--</div>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>

                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="row">--%>
                                <%--<div class="col-md-8"></div>--%>
                                <%--<div class="col-md-4">--%>
                                    <%--<span id="currentSet">${currentSet}</span> / <span id="totalSet">${totalSet}</span><input type="text" id="setNum" class="input-sm"/><button class="btn btn-default btn-sm" onclick="turnToThisSet()">行く</button>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        </div>
                    </div>
                </div>
                <!--                -->

            </div><!-- information tab -->
            <div class="col-md-1">
                <div class="row">
                    <div class="col-md-12">
                        <ul class="nav nav-pills nav-stacked bookmarkUl">
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}"><i class="glyphicon glyphicon-cog"> 情報</i></a></li>
                            <li role="presentation" class="kengen-operation"><a href="/valdacHost/kouji/${kouji.id}/instruct"><i class="glyphicon glyphicon-indent-left"> 指示</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/kenan"><i class="glyphicon glyphicon-floppy-save"> 懸案</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/history"><i class="glyphicon glyphicon-time"> 履歴</i></a></li>
                            <li role="presentation" class="currentBookmark"><a href="/valdacHost/kouji/${kouji.id}/image"><i class="glyphicon glyphicon-picture"> 図面</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/printhtml"><i class="glyphicon glyphicon-download"> DL</i></a></li>
                            <li role="presentation"><a href="/valdacHost/kouji/${kouji.id}/gpPrinthtml"><i class="glyphicon glyphicon-download"> GP&ICS</i></a></li>
                        </ul>
                    </div>
                </div>
            </div><!-- tab button tab -->
        </div>
    </section>
</div>

    <!-- 弁 リスト modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">関連弁追加</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="ben-table">
                                <div class="well well-sm">
                                <%--<input type="text" name="table_search_kanren" id="table_search_kanren" class="form-control input-sm" placeholder="弁番号 Filter" onkeyup="findBenByKeywordsInKanrenBen(this)" />--%>
                                    <div class="col-md-2 input-group">
                                        <input type="text" name="table_search_kanren" id="table_search_kanren" class="form-control input-sm pull-right" style="width: 150px;" placeholder="弁番号 Filter">
                                        <div class="input-group-btn">
                                            <button class="btn btn-sm btn-default" id="table_search_kanren_btn" onclick="findBenByKeywordsInKanrenBen()"><i class="fa fa-search"></i></button>
                                        </div>
                                    </div>
                            <table id="Allben" class="table table-hover">
                                <thead>
                                <tr>
                                    <th><input id="allCheckbox" type="checkbox" onmouseup="checkAll(this)" /></th>
                                    <th>弁番号</th>
                                    <th>弁名称</th>
                                </tr>
                                </thead>
                                <tbody id="ben-tbody">
                                <c:forEach items="${valveList}" var="valve">
                                    <tr class="valve-item">
                                        <td id="${valve.kikiSysId}"><input type="checkbox" class="select-ben" style="opacity: 10" value="${valve.kikiSysId}" id="select-${valve.kikiSysId}" /></td>
                                        <td>${valve.vNo}</td>
                                        <td>${valve.benMeisyo}</td>
                                    </tr>
                                </c:forEach>

                                </tbody>
                            </table>
                            </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="ben-table">
                                <table id="selected-table" class="table table-hover selected-table">
                                    <c:forEach items="${valveList}" var="valve">
                                        <tr id="selected-${valve.kikiSysId}" class="selected-ben" onclick="unselect(this)">
                                            <th scope="row"><button class="btn btn-xs">x</button></th>
                                            <td>${valve.vNo}</td>
                                            <td>${valve.benMeisyo}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">閉じる</button>
                    <button type="button" class="btn btn-success" onclick="submitKanrenben()">確定</button>
                </div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
    //図面回転用
    valueKouji=0;//kouji 用
    function routeImageForKouji(type){
        var type=type;
        var tmpAngle;

        if(type==1){//右回り
            valueKouji=(valueKouji+90)%360;
            tmpAngle=valueKouji-90;
        }else{//左回り
            valueKouji=(valueKouji-90)%360;
            tmpAngle=valueKouji+90;
        }
        //クリックして回す
        $("#koujiImg img").rotate({
            angle:tmpAngle,
            animateTo: valueKouji,
            easing: $.easing.easeInOutExpo
        });
    };

    function routeImage(value) {
        $("#koujiImg img").rotate({
            angle: 0,
            animateTo: value,
            easing: $.easing.easeInOutExpo
        });
    };

    $(document).ready(function(){
        //ユーザ権限
        var userKengen=$("#userKengen").val();
        if(userKengen=="6"){//管理者
            $(".kengen-operation").show();
        }else{
            $(".kengen-operation").hide();
            $(".kengen-operation-readonly").attr("disabled",true);
            //Index 修正ボタンを非表示
            document.getElementById('index_num').style.display = "none";
            document.getElementById('index_page_num').style.display = "none";
        }

        $("#left-menu-new").addClass("active");

        $(".box-panel").click(function(){
            $(this).next().toggle();
        });

        $(window).load(function () {
            //最初の図面を表示する
            loadImg("${firstReportImage.imagename}");
            document.getElementById('previewImageName').value="${firstReportImage.imagename}";
            //first ボタン設定
            firstAndLastButton(1,"${totalSet}","firstButton","previousButton","NextButton","lastButton");
        });

        var koujiId = document.getElementById("koujiId").value;
        //画像名を取得
//        var imagename=document.getElementById("previewImage").src;
//        imagename=imagename.replace("http://storage.googleapis.com/valdacHost/","");

//        $.post("/valdacHost/image/getReportimageKikisystemByKoujiAndImagename",
//                {"koujiId":koujiId,
//                    "imagename":imagename},
//                function(data){
//                    var items = JSON.parse(data);
//                    //current-select-ben
//                    var htmlContent = "";
//                    console.log("first imagename="+imagename);
//                    for(var i = 0;i<items.length;i++){
//                        htmlContent = htmlContent +
//                                "<tr>" +
//                                "<td>" + items[i].vNo + "</td>" +
//                                "<td>" + items[i].benMeisyo + "</td>" +
//                                "</tr>";
//                    }
//                    $("#current-select-ben").html(htmlContent);
//
//                });

        //弁選択
        $('.valve-item').mouseup(function(obj){
            var tr = obj.currentTarget;
            var tdList=$(tr).find('td');
            var id=tdList[0].id;

            console.log("id="+id);
            console.log("checked="+$('#select-'+id).prop('checked'));
            if($('#select-'+id).prop('checked') == true){
                $(tr).removeClass("bg-light-blue-gradient");
                $('#selected-' + id).hide();
                $('#select-'+id).prop('checked', false);
            } else {
                $(tr).addClass("bg-light-blue-gradient");
                $('#selected-' + id).show();
                $('#select-'+id).prop('checked', true);
            }

            //全選択かどうか チェックする
            var booleanAllCheck=true;//全選択かどうか
            var allCheckboxTmp = document.getElementById( "allCheckbox" );
            var selectedList = $('.selected-ben');
            for(var i = 0;i<selectedList.length;i++){
                var tmp=selectedList[i].id;
                var element =document.getElementById(tmp);
                var style =element.style.display;
//                console.log("tmp="+tmp+"   ;style ="+style );
                if(style=="none"){
                    //選択がない場合
                    booleanAllCheck = false;
                }
            }
            if(booleanAllCheck){
                allCheckboxTmp.checked = true;
            }else{
                allCheckboxTmp.checked = false;
            }

        });
    });

    //画像名を取得
    function getImageName(){
        var imageUrl=document.getElementById("previewImageName").value;
        imageUrl=imageUrl.replace("http://storage.googleapis.com/"+BUCKET+"/","");
        return imageUrl;
    }

    function checkAll(obj){
        if(!obj.checked){
            //be selected
            $('.select-ben').prop('checked', true);
            $('.selected-ben').show();
            $('.valve-item').addClass("bg-light-blue-gradient");
        } else {
            $('.select-ben').prop('checked', false);
            $('.selected-ben').hide();
            $('.valve-item').removeClass("bg-light-blue-gradient");
        }
    }

    function printImage(obj){
        var imageUrl=document.getElementById("previewImage").src;
        //画像名を取得
        imageUrl=imageUrl.replace("http://storage.googleapis.com/","");
        if(imageUrl.length>20){
            window.open("https://valdac.power-science.com/print.html?"+imageUrl);
        }
    }

    function selectValve(obj){
        if(!obj.checked) {
            $('#selected-' + obj.value).show();
        } else {
            $('#selected-' + obj.value).hide();
        }
        //全選択かどうか チェックする
        var booleanAllCheck=true;//全選択かどうか
        var allCheckboxTmp = document.getElementById( "allCheckbox" );
        var selectedList = $('.selected-ben');
        for(var i = 0;i<selectedList.length;i++){
            var tmp=selectedList[i].id;
            var element =document.getElementById(tmp);
            var style =element.style.display;
            console.log("tmp="+tmp+"   ;style ="+style );
            if(style=="none"){
                //選択がない場合
                booleanAllCheck = false;
            }
        }
        if(booleanAllCheck){
            allCheckboxTmp.checked = true;
        }else{
            allCheckboxTmp.checked = false;
        }
    }

    function unselect(obj){
        var selectedId = new String(obj.id);
        var id = selectedId.split('-')[1];
        $('#select-'+id).prop('checked', false);
        var tr=$('#'+id).parent();
        $(tr).removeClass("bg-light-blue-gradient");
        $(obj).hide();
        //全選択を外す
        var allCheckboxTmp = document.getElementById( "allCheckbox" );
        allCheckboxTmp.checked = false;
    }

    function submitKanrenben(){
        var selectedList = $('.select-ben:checked');
        var selectedIdList = "";
        for(var i = 0;i<selectedList.length;i++){
            if(selectedIdList.length-1 == i) {
                selectedIdList = selectedIdList + selectedList[i].value;
            } else {
                selectedIdList = selectedIdList + selectedList[i].value + ",";
            }
        }
        var koujiId = document.getElementById("koujiId").value;
        var imagename=document.getElementById('previewImageName').value;
        imagename=imagename.replace("http://storage.googleapis.com/"+BUCKET+"/","");
        $.post("/valdacHost/image/updateImageKikisystem",{"koujiId":koujiId,"imagename":imagename,"selectedIdList":selectedIdList},function(data){

            $.post("/valdacHost/image/getReportimageKikisystemByKoujiAndImagename",
                    {"koujiId":koujiId,
                        "imagename":imagename},
                    function(data){
                        var items = JSON.parse(data);
                        //current-select-ben
                        var htmlContent = "";
                        for(var i = 0;i<items.length;i++){
                            htmlContent = htmlContent +
                                    "<tr>" +
                                    "<td>" + items[i].vNo + "</td>" +
                                    "<td>" + items[i].benMeisyo + "</td>" +
                                    "</tr>";
                        }
                        $("#current-select-ben").html(htmlContent);

                    });

            $("#myModal").modal("hide");
//            $.post("/valdacHost/image/saveImageKikisystem",{"koujiId":koujiId,"imagename":imagename,"selectedIdList":selectedIdList},function(data){});
        });
    }

    function openKanrenben(){

        $('.select-ben').prop('checked',false);
        $('.selected-ben').hide();

        //get valve list
        var koujiId = document.getElementById("koujiId").value;
        var imagename=document.getElementById('previewImageName').value;
        imagename=imagename.replace("http://storage.googleapis.com/"+BUCKET+"/","");
        console.log("openKanrenben imagename="+imagename);
        if(koujiId.length < 1 || imagename.length < 1){
            return false;
        } else {
            $.post("/valdacHost/image/getReportimageKikisystemByKoujiAndImagename",
                    {"koujiId":koujiId,
                        "imagename": imagename},
                    function (data) {
                        var items = JSON.parse(data);
                        //current-select-ben
                        for (var i = 0; i < items.length; i++) {
                            $('#selected-' + items[i].kikiSysId).show();
                            $('#select-' + items[i].kikiSysId).prop('checked', true);
                        }
                    })
            $("#myModal").modal("show");
        }
    }

    function removeImage(){
        var imagename=document.getElementById('previewImageName').value;
        imagename=imagename.replace("http://storage.googleapis.com/"+BUCKET+"/","");
        //delete session
        $.post("/valdacHost/image/deleteByImagename",
                {"object":imagename},
                function(data){
                    object = imagename;
                    var request = gapi.client.storage.objects.delete({
                        'bucket': BUCKET,
                        'object': object
                    });
                    request.execute(function(resp) {
                        object = "";
                        //index　図面数を更新
                        var setNum = parseInt(document.getElementById("currentSet").innerHTML);
                        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
                        setNum=setNum-1;
                        totalSet=totalSet-1;
                        if(totalSet>0&&setNum==0){
                            setNum=1;
                            var tmpNum=setNum-1;
                            turnToSet(tmpNum);
                        }else if(totalSet>0&&setNum>0){
                            var tmpNum=setNum-1;
                            turnToSet(tmpNum);
                        }else if(totalSet==0){
                            document.getElementById('previewImage').src="";
                        }
                        document.getElementById("currentSet").innerHTML=setNum;
                        document.getElementById("totalSet").innerHTML=totalSet;
                        turnToSet(setNum);
                        updatePage("");
                    });
                    //delete database
                    console.log("imagename="+imagename);
                    var koujiId = document.getElementById("koujiId").value;
                    $.post("/valdacHost/image/deleteDatabaseByImagename",
                            {"object":imagename,"koujiId":koujiId},function(data){})
                });
    }

    function updatePage(imagename){
        $.post("/valdacHost/image/updatePageNumber",{"imagename":imagename},function(data){
            var items = JSON.parse(data);
            $("#totalPage").html(items.totalPage);
            $("#currentPage").html(items.currentPage);
        });
    }

    function turnToPreviousPage(){
        var setNum = parseInt(document.getElementById("currentSet").innerHTML);
        setNum = setNum - 1;
        if(setNum > 0) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum+"";
        }
    }

    function turnToFirstPage(){
        var setNum = parseInt(document.getElementById("totalSet").innerHTML);
        if(setNum > 0) {
            turnToSet(1);
            document.getElementById("currentSet").innerHTML = 1+"";
        }
    }

    function turnToLastPage(){
        var setNum = parseInt(document.getElementById("totalSet").innerHTML);
        if(setNum > 0) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum+"";
        }
    }

    function turnToNumPage(){
        var setNum = parseInt(document.getElementById("jump_page_num").value);
        console.log("setNum="+setNum);
        var tolNum = parseInt(document.getElementById("totalSet").innerHTML);
        if(setNum > 0 && setNum<=tolNum) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum+"";
        }else{
            alert("ページ数以内に半角数字を入力ください");
        }
    }

    function turnToNextPage(){
        var setNum = parseInt(document.getElementById("currentSet").innerHTML);
        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
        setNum = setNum + 1;
        if(setNum <= totalSet) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum + "";
        }
    }

    function turnToThisSet(){
        var setNum = parseInt(document.getElementById('setNum').value);
        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
        if(0 < setNum && setNum <= totalSet) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum + "";
        }
    }

    function updateCurrentSet(){
        var setNum = parseInt(document.getElementById("currentSet").innerHTML);
        var totalPage = parseInt(document.getElementById("totalPage").innerHTML);
        //update totalset
        if(totalPage>0){
            var totalSet = parseInt(totalPage/6);
            if(totalPage%6 != 0){
                totalSet = totalSet + 1;
            }
        }

        if(0 < setNum && setNum <= totalSet) {
            turnToSet(setNum);
            document.getElementById("currentSet").innerHTML = setNum + "";
            document.getElementById("totalSet").innerHTML = totalSet + "";
        }
    }

    function turnToSet(setNumStr){
        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
        //先頭、最終へボタン設定
        firstAndLastButton(0,0,"firstButton","previousButton","NextButton","lastButton");
        var setNum = parseInt(setNumStr);
        if(setNum >= 1 && setNum <= totalSet){
            setNum = setNum - 1;
            $.post("/valdacHost/image/getReportimageBySet", {"setNum": setNum}, function (data) {
                var items = JSON.parse(data);
                //図面描画
                var tmpImgUrl=items.imagename;
                document.getElementById('previewImageName').value=tmpImgUrl;
                loadImg(tmpImgUrl);
                console.log("over");
                //図面Index番号表示
                document.getElementById('index_page_num').value="";
                //図面種類表示
                $("#currentSyu").html("");
                var htmlitem="<option>"+items.imagesyu+"</option><option>図面仕様書</option><option>点検報告書</option><option>懸案事項一覧</option><option>完成図面</option><option>作業指示書</option><option>弁棒ねじ検索記録</option>";
                $("#currentSyu").html(htmlitem);
                var imagename=items.imagename;
                var koujiId = document.getElementById("koujiId").value;

                $.post("/valdacHost/image/getReportimageKikisystemByKoujiAndImagename",
                        {"koujiId":koujiId,
                            "imagename":imagename},
                        function(data){
                            var items = JSON.parse(data);
                            console.log("items size="+items.length);
                            //current-select-ben
                            var htmlContent = "";
                            for(var i = 0;i<items.length;i++){
                                htmlContent = htmlContent +
                                        "<tr>" +
                                        "<td>" + items[i].vNo + "</td>" +
                                        "<td>" + items[i].benMeisyo + "</td>" +
                                        "</tr>";
                            }
                            console.log("htmlContent="+htmlContent);
                            $("#current-select-ben").html(htmlContent);

                        });
            });
            //図面回転用
            valueKouji=0;//kouji 用
            routeImage(0);
        }
        //先頭、最終へボタン設定
        firstAndLastButton(setNumStr,totalSet,"firstButton","previousButton","NextButton","lastButton");
    }
    //先頭、最終へボタン設定
    function firstAndLastButton(firstNum,lastNum,firstButton,previousButton,NextButton,lastButton){
        var firstButtonName=document.getElementById(firstButton);
        var previousButtonName=document.getElementById(previousButton);
        var NextButtonName=document.getElementById(NextButton);
        var lastButtonName=document.getElementById(lastButton);
        console.log("firstNum="+firstNum+"      lastNum="+lastNum);
        if(firstNum<=1 && lastNum<=1){
            firstButtonName.disabled =true;
            previousButtonName.disabled =true;
            NextButtonName.disabled =true;
            lastButtonName.disabled =true;
        }else if((firstNum==1 && lastNum>1)){
            firstButtonName.disabled =true;
            previousButtonName.disabled =true;
            NextButtonName.disabled =false;
            lastButtonName.disabled =false;
        }else if(firstNum>1 && firstNum==lastNum){
            firstButtonName.disabled =false;
            previousButtonName.disabled =false;
            NextButtonName.disabled =true;
            lastButtonName.disabled =true;
        }else{
            firstButtonName.disabled =false;
            previousButtonName.disabled =false;
            NextButtonName.disabled =false;
            lastButtonName.disabled =false;
        }
    }

    function progressBarController(num){
        $("#progressbar").attr({"aria-valuenow":num});
        $("#progressbar").css({"width":num+"%"});
    }

    function totalProgressBarController(numStr,totalStr){
        var num = parseInt(numStr);
        var totalNum = parseInt(totalStr);
        var p = parseFloat(num/totalNum);
        p = p * 100;
        $("#totalProgressbar").attr({"aria-valuenow":p});
        $("#totalProgressbar").css({"width":p+"%"});

    }

    function saveChangeToDatabase(obj){
        var imagename = getImageName();
        console.log("saveChangeToDatabase imagename="+imagename+"    syurui="+$(obj).val());
        if(imagename.length < 1){
            return false;
        } else {
            //update session
            $.post("/valdacHost/image/updateSyuByImagename",
                    {"imagesyu": $(obj).val(), "object": imagename},
                    function (data) {
//                        $(obj).removeAttr("disabled");
                    });
            //update database
            $.post("/valdacHost/image/saveSyuByImagename",
                    {"imagesyu": $(obj).val(), "object": imagename},
                    function (data) {
//                        $(obj).removeAttr("disabled");
                    });
        }
    }
//
//    function changeImage(obj){
//        $("#operate-panel").css({"opacity": "0"});
//        $(".imagesyu").val("");
////        document.getElementById("previewImage").src=obj.src;
//
//        document.getElementById("previewImage").innerHTML='<img src="'+obj.src+'" width="600px" />';
//        document.getElementById("original-link").href=obj.src;
//        var link = new String(obj.src);
//        var currentObject = link.split("http://storage.googleapis.com/valdacHost/")[1];
//        object = currentObject;
//        document.getElementById("currentImageObject").value = currentObject;
//        $.post("/valdacHost/image/getImageByImagename",{"imagename":currentObject},function(data){
//            var item = JSON.parse(data);
//            $("#currentSyu").html(item.imagesyu);
//        });
//        $.post("/valdacHost/image/getReportimageKikisystemByKoujiAndImagename",
//                {"koujiId":document.getElementById("koujiId").value,
//                "imagename":currentObject},
//                function(data){
//                    var items = JSON.parse(data);
//                    //current-select-ben
//                    var htmlContent = "";
//                    for(var i = 0;i<items.length;i++){
//                        htmlContent = htmlContent +
//                                "<tr>" +
//                                "<td>" + items[i].vNo + "</td>" +
//                                "<td>" + items[i].benMeisyo + "</td>" +
//                                "</tr>";
//                    }
//                    $("#current-select-ben").html(htmlContent);
//                    $("#table_search").val("");
//                    $("#operate-panel").css({"opacity": "1"});
//                    updatePage(currentObject);
//                })
//
//    }

    function showDelete(obj){
        var content = $(obj).find(".thumbnail-content")[0];
        var btn = $(content).find(".thumbnail-delete-btn").css("opacity","1");
    }
    function hideDelete(obj){
        var content = $(obj).find(".thumbnail-content")[0];
        var btn = $(content).find(".thumbnail-delete-btn").css("opacity","0");
    }

    function submitBikou(){
        if(object.length > 0){
            $("#buhinzubikou").css("border-color","#ccc");
            $("#bikouSubmitButton").css("disabled","true");
            $.post("/valdacHost/image/submitBikouById",{"bikou":$("#buhinzubikou").val(),"object":object},function(data){
                $("#bikouSubmitButton").removeClass("disabled");
                $("#buhinzubikou").css("border-color","#00a65a");
            });
        }
    }

    $(window).unload(function(){
        $.post("/valdacHost/image/saveSessionToDatabase");
    });


    //弁検索
    function findBenByKeywords(){
            var keyword = $("#table_search").val();
            var trs=document.getElementById("selected-ben-to-image");
            var myTr=trs.getElementsByTagName("tr");
            for(var i = 1;i<myTr.length;i++){
                var myTd=myTr[i].getElementsByTagName("td");
                $(myTr[i]).hide();
                for(var j = 0;j<myTd.length;j++){
                    var temp=myTd[j].innerHTML;
                    if(temp.match(keyword)){
                        $(myTr[i]).show();
                        break;
                    }
                }
            }
    }

    //関連弁追加画面の検索
    function findBenByKeywordsInKanrenBen(){
        var keyword = $("#table_search_kanren").val();
        var trs=document.getElementById("Allben");
        var myTr=trs.getElementsByTagName("tr");
        for(var i = 1;i<myTr.length;i++){
            var myTd=myTr[i].getElementsByTagName("td");
            $(myTr[i]).hide();
            for(var j = 0;j<myTd.length;j++){
                var temp=myTd[j].innerHTML;
                if(temp.match(keyword)){
                    $(myTr[i]).show();
                    break;
                }
            }
        }
    }

    //関連弁追加画面の検索
    function changeNumPage(){
        //arrayの中の番号　pageではない
        var setImageNumNew = parseInt(document.getElementById("index_page_num").value);
        var setImageNumOld=parseInt(document.getElementById("currentSet").innerHTML);
        //page番号を修正されたかどうか
        if(setImageNumNew==setImageNumOld){
            console.log("setImageNumNew="+setImageNumNew+"; setImageNumOld"+setImageNumOld);
            return false;
        }
        //page番号を有効値かどうか
        if(setImageNumNew>"${totalSet}" || setImageNumNew<1){
            console.log("範囲外　setImageNumNew="+setImageNumNew);
            return false;
        }


            var koujiId=document.getElementById("koujiId").value;
            var imagename= document.getElementById('previewImageName').value;

            //imagename と　koujiIdにより、page番号を修正する
            $.post("/valdacHost/image/changeImagePage",{"koujiId":koujiId,"imagename":imagename,"pageNew":setImageNumNew,"pageOld":setImageNumOld},function(data){
                console.log("data="+data);
                document.getElementById("currentSet").innerHTML=setImageNumNew;
                //first ボタン設定
                firstAndLastButton(setImageNumNew,"${totalSet}","firstButton","previousButton","NextButton","lastButton");
                alert("更新しました");
            });


    }

    //imgPreloaderを作成
    var imgPreloader=new Image();
    function loadImg(imgUrl) {
        document.getElementById('previewImage').src="/valdacHost/img/black.png";
        var OBJECT_NAME=imgUrl;
        var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
        if(totalSet>0){
            //javaにて　signUrlを取得する
            $.post("/valdacHost/SingUrlTest/SignTest",{"OBJECT_NAME":OBJECT_NAME,"BUCKET_NAME":BUCKET},function(data){
                console.log("data="+data);
                //画像読み込みの直前にLoading画像を表示する
                $("#loading").css({'display':'block'});
                //onloadイベントハンドラ
                imgPreloader.onload=function() {
                    $("#loading").css({'display':'none'});
                    //img 設定
                    document.getElementById('previewImage').src=data;
                }
                imgPreloader.onerror=function(){
                    $("#loading").css({'display':'none'});
                    //エラー画像設定
                    document.getElementById('previewImage').src="/valdacHost/img/error.jpg";
                }
                //url-Set
                imgPreloader.src=data;
            });
        }else{
            document.getElementById('previewImage').src="";
        }

    };
</script>

<!-- Google Sotrage -->
<script type="text/javascript">
var PROJECT = 'power-science-20140719001';

//サーバー用 clientId
var clientId = '13771198627-plrtfkpr8r96ccev7n6ip1f1ublte6n1.apps.googleusercontent.com';
//開発用 clientId
//var clientId = '13771198627-bpo57i2unf6dkbna2jo7ehljin6aseoa.apps.googleusercontent.com';
//サーバー用 api key
var apiKey = 'AIzaSyAG-h3cIM_SsO0fE_gA8lAIl2x71zdC6NA';
//開発用 api key
//var apiKey = 'AIzaSyCnoDTW4BQB-DKKf9YWd9AaoEFYKeN6Esw';

var scopes = 'https://www.googleapis.com/auth/devstorage.full_control';
var API_VERSION = 'v1';
//var BUCKET = 'valdac-construction-aisa';//テストサーバ用
var BUCKET = 'valdac-construction-asia'; //本番
var object = "";

var GROUP =
        'group-00b4903a9744bffac3b0196718449ddbaf5cbc5a1ebfff7783546ad6f13e63f6';
var ENTITY = 'allUsers';
var ROLE = 'OWNER';
var ROLE_OBJECT = 'OWNER';


//multiple file upload control flag
var uploadLoopStatus = 0;
var fileCount = 0;
var timer;
function insertObject(event) {
    progressBarController(0);
    //********************************************************
//    $("#totalProgress").html(event.target.files.length+"枚画像をアップロード中です。。。");
    var errorCount=0;//アップできなかった図面数
    var errorFileName="";

    timer = setInterval(function(){
        if(fileCount < event.target.files.length){
            var Imagenum=fileCount+1;
            $("#totalProgress").html(event.target.files.length+"枚画像をアップロード中です。。。"+"   第"+Imagenum+"枚目です");
            if(uploadLoopStatus == 0){
                uploadLoopStatus = 1;

                try {
                    var fileData = event.target.files[fileCount];
                }
                catch (e) {
                    console.log(e);
                }
                //image type
                var imagetype=["jpg","jpeg","pdf","gif","png","bmp"];
                var fileTypeOld = new String(fileData.name);
                var fileType = fileTypeOld.split('.');
                var  fileTypeName=fileType[fileType.length - 1]
                console.log("fileTypeName="+fileTypeName);
                if(imagetype.indexOf(fileTypeName.toLowerCase())>=0){
                    console.log("存在する");
                    // progressbar
                    progressBarController(10);
                    const boundary = '-------314159265358979323846';
                    const delimiter = "\r\n--" + boundary + "\r\n";
                    const close_delim = "\r\n--" + boundary + "--";
                    var reader = new FileReader();
                    reader.readAsBinaryString(fileData);
                    reader.onload = function (e) {
                        var fileTypeOld = new String(fileData.name);
                        var fileType = fileTypeOld.split('.');
                        console.log("fileType="+fileType);

                        //make file name
                        var d = new Date();
                        var vYear = d.getFullYear();
                        var vMon = d.getMonth() + 1;
                        var vDay = d.getDate();
                        if(vMon < 10){
                            vMon = "0" + vMon;
                        }
                        if (vDay < 10) {
                            vDay = "0" + vDay;
                        }
                        var todayDate = vYear + "" + vMon + "" + vDay;
                        var oldfilename= fileType[0].replace(/\s|　/g,"");
                        var objectAndName = todayDate + "/" +oldfilename+"_"+ d.getTime() + "." + fileType[fileType.length - 1];
//                    var objectAndName = todayDate + "/"+fileTypeOld;
                        //make parameters
                        progressBarController(30);
                        var contentType = fileData.type || 'application/octet-stream';
                        var metadata = {
                            'name': objectAndName,
                            'mimeType': contentType
                        };
                        var base64Data = btoa(reader.result);
                        var multipartRequestBody =
                                delimiter +
                                'Content-Type: application/json\r\n\r\n' +
                                JSON.stringify(metadata) +
                                delimiter +
                                'Content-Type: ' + contentType + '\r\n' +
                                'Content-Transfer-Encoding: base64\r\n' +
                                '\r\n' +
                                base64Data +
                                close_delim;
                        //Note: gapi.client.storage.objects.insert() can only insert
                        //small objects (under 64k) so to support larger file sizes
                        //we're using the generic HTTP request method gapi.client.request()
                        var request = gapi.client.request({
                            'path': '/upload/storage/v1/b/' + BUCKET + '/o',
                            'method': 'POST',
                            'params': {'uploadType': 'multipart'},
                            'headers': {
                                'Content-Type': 'multipart/mixed; boundary="' + boundary + '"'
                            },
                            'body': multipartRequestBody});
                        progressBarController(60);
                        try {
                            //Execute the insert object request
                            executeRequest(request, 'insertObject');
                            //Store the name of the inserted object
                            object = objectAndName;
                        }
                        catch (e) {
                            alert('An error has occurred: ' + e.message);
                        }
                    }
                }else{
                    errorFileName=errorFileName+fileTypeOld+";\n";
                    errorCount=errorCount+1;
                    progressBarController(0);
                    uploadLoopStatus = 2;
                }
            } else if(uploadLoopStatus == 1){
            } else if(uploadLoopStatus == 2){
                fileCount++;
                totalProgressBarController(fileCount,event.target.files.length);
                uploadLoopStatus = 0;
            }
        } else {
            console.log('over');
            if(errorCount>0){
                console.log('下記の拡張子の図面がアップできませんでした\n'+errorFileName);
                alert("下記の拡張子の図面がアップできませんでした\n"+errorFileName);
            }else{
                alert("図面全部アップしました");
            }

            //全て画像アップ後、データ初期化
            uploadLoopStatus = 0;
            fileCount = 0;
            clearInterval(timer);
            location.reload();
        }
    },1000)
}

function uploadFilesAction(event){

}

/**
 * Google Cloud Storage API request to insert an Access Control List into
 * your Google Cloud Storage object.
 */
function insertObjectAccessControls() {
    resource = {
        'entity': ENTITY,
        'role': ROLE_OBJECT
    };
    var request = gapi.client.storage.objectAccessControls.insert({
        'bucket': BUCKET,
        'object': object,
        'resource': resource
    });
//    executeRequest(request, 'insertObjectAccessControls');
    executeRequestInsert(object);
}
/**
 * Google Cloud Storage API request to delete a Google Cloud Storage object.
 */
function deleteObject() {
    var request = gapi.client.storage.objects.delete({
        'bucket': BUCKET,
        'object': object
    });
    executeRequest(request, 'deleteObject');
}
function updateApiResultEntry(apiRequestName) {
    listChildren = document.getElementById('main-content')
            .childNodes;
    if (listChildren.length > 1) {
        listChildren[1].parentNode.removeChild(listChildren[1]);
    }
    if (apiRequestName != 'null') {
        window[apiRequestName].apply(this);
    }
}
function executeRequestInsert(objectName) {
        var totalPage = Number($("#totalPage").html());
        totalPage = totalPage + 1;
        $("#totalPage").html(totalPage);

        progressBarController(100);
        //update session
        var koujiId = document.getElementById("koujiId").value;
        $.post("/valdacHost/image/updateImageByImagePath",{"object":objectName,"koujiId":koujiId},function(data){
            progressBarController(0);
            uploadLoopStatus = 2;
        });
        //update database
        $.post("/valdacHost/image/saveImageByImagePath",{"object":objectName,"koujiId":koujiId},function(data){
            //図面件数を更新
            var totalSet = parseInt(document.getElementById("totalSet").innerHTML);
            totalSet=totalSet+1;
            document.getElementById("totalSet").innerHTML=totalSet;
            document.getElementById("currentSet").innerHTML=totalSet;
            //最新の図面に移動
            document.getElementById('previewImage').src="http://storage.googleapis.com/"+BUCKET+objectName;
            //アップした図面を表示する
            loadImg(objectName);
            document.getElementById('previewImageName').value=objectName;
        });
}
function executeRequest(request, apiRequestName) {
    request.execute(function(resp) {
        if (apiRequestName != 'insertObject') {
        } else {
            progressBarController(80);
            insertObjectAccessControls();
        }
    });
}
/**
 * Set required API keys and check authentication status.
 */
function handleClientLoad() {
    gapi.client.setApiKey(apiKey);
    window.setTimeout(checkAuth, 1);
}
/**
 * Authorize Google Cloud Storage API.
 */
function checkAuth() {
    gapi.auth.authorize({
        client_id: clientId,
        scope: scopes,
        immediate: true
    }, handleAuthResult);
}
/**
 * Handle authorization.
 */
function handleAuthResult(authResult) {
    var authorizeButton = document.getElementById('authorize-button');
    var authorizeMessage=document.getElementById('authorize-message');
    if (authResult && !authResult.error) {
        authorizeButton.style.visibility = 'hidden';
        authorizeMessage.style.visibility="hidden";
        initializeApi();
        filePicker.onchange = insertObject;
    } else {
        authorizeButton.style.visibility = '';
        authorizeMessage.style.visibility='';
        authorizeButton.onclick = handleAuthClick;
    }
}
/**
 * Handle authorization click event.
 */
function handleAuthClick(event) {
    gapi.auth.authorize({
        client_id: clientId,
        scope: scopes,
        immediate: false
    }, handleAuthResult);
    return false;
}
/**
 * Load Google Cloud Storage API v1beta12.
 */
function initializeApi() {
    gapi.client.load('storage', API_VERSION);
}
/**
 * Driver for sample application.
 */
$(window)
        .bind('load', function() {
//            addSelectionSwitchingListeners();
            handleClientLoad();
        });

</script>

</html>