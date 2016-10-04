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
<link type="text/css" rel="stylesheet" href="/valdacHost/css/image.css"/>
<html>
<style type="text/css">
    div.container { width: 1600px; }
    div.centerdiv { text-align: center; }
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
</section>

<section class="content">
<div class="row">
    <!-- collection -->
    <div class="col-md-6">
        <div class="btn-group" role="group">
            <a href="/valdacHost/item/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">弁基本情報</a>
            <a href="/valdacHost/kenan/kenan/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">懸案</a>
            <a href="/valdacHost/tenken/tenken/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">点検履歴</a>
            <a href="/valdacHost/image/image/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default  bg-yellow active">図面</a>
            <a href="/valdacHost/list/kikiSearch/" class="btn btn-default">戻る</a>
        </div>
    </div>
</div>
<%--ICS画像を表示する--%>
<div class="box box-solid box-image" id="div-1" style="display: block">
    <div class="box-header box-panel">
        <h3 class="box-title"><i class="fa fa-fw fa-files-o"></i> ${valve.vNo}の ICS図面 </h3>
    </div>
    <div class="box-body" style="display: none">
        <div class="form-group">
            <%--印刷ボタンなど--%>
            <div class="row col-md-12 centerdiv">
                <button type="button" id="firstButtonICS" class="btn btn-default" onclick="turnToFirstPage(1)" value=""/>先頭へ</button>&nbsp; &nbsp;

                <button type="button" id="previousButtonICS" class="btn btn-default" onclick="turnToPreviousPage(1)" value=""/>前ページ</button>

                &nbsp; &nbsp;<span id="valueCurrentIcs">${IcsFirstNum}</span>/<span id="valueTotalIcs">${IcsImageListSize}</span>

                <button type="button" id="NextButtonICS" class="btn btn-default" onclick="turnToNextPage(1)" value=""/>次ページ</button>

                <button type="button" id="lastButtonICS" class="btn btn-default" onclick="turnToLastPage(1)" value=""/>最終へ</button>&nbsp;
            </div></br></br>
            <div class="row col-md-12 centerdiv">

                &nbsp;&nbsp;&nbsp;図面種類：<label id="imagesyuIcs"><c:if test="${not empty IcsFirstImage}">${IcsFirstImage.imagesyu}</c:if></label>

                &nbsp;&nbsp;&nbsp;備考：<label id="imagebikouIcs"><c:if test="${not empty IcsFirstImage}">${IcsFirstImage.bikou}</c:if></label>

                <button type="button" class="btn btn-default" onclick="printImage(1)" value=""/>印刷</button>

            </div>
            <div class="row">
                <div class="row previewDiv">
                    <button type="button" class="btn btn-default" onclick="routeImageForICS(1)" value=""/>右90度</button>
                    <button type="button" class="btn btn-default" onclick="routeImageForICS(0)" value=""/>左90度</button>
                </div>
            </div>
            <div class="row">
                <div class="row previewDiv" id="ICSImg">
                    <!-- image -->
                    <img id="previewImageIcs" class="previewImageIcs" style="border:solid 1px silver"   width="600" src="" />
                </div>
            </div>
        </div>
    </div>
</div>

<%--GP画像表示する--%>
<div class="box box-solid box-image" id="div-2" style="display: block">
    <div class="box-header box-panel">
        <h3 class="box-title"><i class="fa fa-fw fa-files-o"></i> ${valve.vNo}の GP図面 </h3>
    </div>
    <div class="box-body" style="display: none">
        <div class="form-group">
            <%--印刷ボタンなど--%>
            <div class="row col-md-12 centerdiv">
                <button type="button" id="firstButtonGP" class="btn btn-default" onclick="turnToFirstPage(2)" value=""/>先頭へ</button>&nbsp; &nbsp;

                <button type="button" id="previousButtonGP" class="btn btn-default" onclick="turnToPreviousPage(2)" value=""/>前ページ</button>

                &nbsp; &nbsp;<span id="valueCurrentGP">${GPFirstNum}</span>/<span id="valueTotalGP">${GPImageListSize}</span>

                <button type="button" id="NextButtonGP" class="btn btn-default" onclick="turnToNextPage(2)" value=""/>次ページ</button>

                <button type="button" id="lastButtonGP" class="btn btn-default" onclick="turnToLastPage(2)" value=""/>最終へ</button>&nbsp;
            </div></br></br>
            <div class="row col-md-12 centerdiv">

                &nbsp;&nbsp;&nbsp;図面種類：<label id="imagesyuGP"><c:if test="${not empty GPFirstImage}">${GPFirstImage.imagesyu}</c:if></label>

                &nbsp;&nbsp;&nbsp;備考：<label id="imagebikouGP"><c:if test="${not empty GPFirstImage}">${GPFirstImage.bikou}</c:if></label>

                <button type="button" class="btn btn-default" onclick="printImage(2)" value=""/>印刷</button>
            </div>
            <div class="row">
                <div class="row previewDiv">
                    <button type="button" class="btn btn-default" onclick="routeImageForGP(1)" value=""/>右90度</button>
                    <button type="button" class="btn btn-default" onclick="routeImageForGP(0)" value=""/>左90度</button>
                </div>
            </div>
            <div class="row">
                <div class="row previewDiv" id="GPImg">
                    <!-- image -->
                    <img id="previewImageGP" class="previewImageGP" style="border:solid 1px silver"  width="600" src="" />
                </div>
            </div>
        </div>
    </div>
</div>

<%--弁の画像を表示--%>
<div class="box box-solid box-image" id="div-3" style="display: block">
    <div class="box-header box-panel">
        <h3 class="box-title"><i class="fa fa-fw fa-files-o"></i> ${valve.vNo}の 弁関連図面 ${imageListKikisysSize}枚 </h3>
    </div>
    <div class="box-body" style="display: none">
        <div class="form-group">
            <div class="row col-md-12">
                <div class="btn-group" role="group">
                    <button type="button" class="btn btn-default" onclick="turnToImagesyu(0)"/>全部</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyu(1)"/>図面仕様書</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyu(2)"/>点検報告書</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyu(3)"/>懸案事項一覧</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyu(4)"/>完成図面</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyu(5)"/>作業指示書</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyu(6)"/>弁棒ねじ検索記録</button>
                </div>
            </div>
        </div><br><br>
        <div class="form-group">
            <%--印刷ボタンなど--%>
            <div class="row col-md-12 centerdiv">
                <button type="button" id="firstButtonValve" class="btn btn-default" onclick="turnToFirstPage(3)" value=""/>先頭へ</button>&nbsp; &nbsp;

                <button type="button" id="previousButtonValve" class="btn btn-default" onclick="turnToPreviousPage(3)" value=""/>前ページ</button>

                &nbsp; &nbsp;<span id="valueCurrent">${ValveFirstNum}</span>/<span id="valueTotal">${imageListKikisysSize}</span>

                <button type="button"  id="NextButtonValve" class="btn btn-default" onclick="turnToNextPage(3)" value=""/>次ページ</button>

                <button type="button" id="lastButtonValve" class="btn btn-default" onclick="turnToLastPage(3)" value=""/>最終へ</button>&nbsp; &nbsp;&nbsp; &nbsp;

                <input type="text"  name="jump_page_num" id="jump_page_num" style="width: 50px;" value=""/>&nbsp; &nbsp;&nbsp; &nbsp;

                <button type="button" class="btn btn-default" onclick="turnToNumPage(3)" value=""/>ページに遷移</button>
            </div></br></br>
            <div class="row col-md-12 centerdiv">

                &nbsp;&nbsp;&nbsp;図面種類：<label id="imagesyu" ><c:if test="${not empty ValveFirstImage}">${ValveFirstImage.imagesyu}</c:if></label>

                &nbsp;&nbsp;&nbsp;備考：<label  id="imagebikou"><c:if test="${not empty ValveFirstImage}">${ValveFirstImage.bikou}</c:if></label>

                &nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-default" onclick="printImage(3)" value=""/>印刷</button>

            </div>
            <div class="row">
                <div class="row previewDiv">
                    <button type="button" class="btn btn-default" onclick="routeImageForValve(1)" value=""/>右90度</button>
                    <button type="button" class="btn btn-default" onclick="routeImageForValve(0)" value=""/>左90度</button>
                </div>
            </div>
            <div class="row">
                <div class="row previewDiv" id="valveImg">
                    <!-- image -->
                    <img id="previewImage" class="previewImage" style="border:solid 1px silver"  width="600" src="" />
                </div>
            </div>
        </div>
    </div>
</div>

<div class="box box-solid box-image" id="div-4" style="display: block">
    <div class="box-header box-panel">
        <h3 class="box-title"><i class="fa fa-fw fa-files-o"></i> ${valve.vNo}の 工事の図面 ${imageListSize}枚 </h3>
    </div>
    <div class="box-body" style="display:none">
        <div class="form-group">
            <div class="row col-md-12">
                <div class="btn-group" role="group">
                    <button type="button" class="btn btn-default" onclick="turnToImagesyuKouji(0)"/>全部</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyuKouji(1)"/>図面仕様書</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyuKouji(2)"/>点検報告書</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyuKouji(3)"/>懸案事項一覧</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyuKouji(4)"/>完成図面</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyuKouji(5)"/>作業指示書</button>
                    <button type="button" class="btn btn-default" onclick="turnToImagesyuKouji(6)"/>弁棒ねじ検索記録</button>
                </div>
            </div>
        </div></br></br>
        <div class="form-group">
            <%--印刷ボタンなど--%>
            <div class="row col-md-12 centerdiv">
                <button type="button"  id="firstButtonKouji" class="btn btn-default" onclick="turnToFirstPage(4)" value=""/>先頭へ</button>&nbsp; &nbsp;

                <button type="button" id="previousButtonKouji" class="btn btn-default" onclick="turnToPreviousPage(4)" value=""/>前ページ</button>

                <span id="valueCurrentKouji">${KoujiFirstNum}</span>/<span id="valueTotalKouji">${imageListSize}</span>

                <button type="button" id="NextButtonKouji" class="btn btn-default" onclick="turnToNextPage(4)" value=""/>次ページ</button>

                <button type="button" id="lastButtonKouji" class="btn btn-default" onclick="turnToLastPage(4)" value=""/>最終へ</button>&nbsp; &nbsp;&nbsp; &nbsp;

                <input type="text"  name="jump_page_num_kouji" id="jump_page_num_kouji" style="width: 50px;" value=""/>&nbsp; &nbsp;&nbsp; &nbsp;

                <button type="button" class="btn btn-default" onclick="turnToNumPage(4)" value=""/>ページに遷移</button>

            </div></br></br>
            <div class="row col-md-12 centerdiv">

                &nbsp;&nbsp;&nbsp;図面種類：<label id="imagesyuKouji"><c:if test="${not empty KoujiFirstImage}">${KoujiFirstImage.imagesyu}</c:if></label>

                &nbsp;&nbsp;&nbsp;備考：<label id="imagebikouKouji"><c:if test="${not empty KoujiFirstImage}">${KoujiFirstImage.bikou}</c:if></label>

                <button type="button" class="btn btn-default" onclick="printImage(4)" value=""/>印刷</button>
            </div>
            <div class="row">
                <div class="row previewDiv">
                    <button type="button" class="btn btn-default" onclick="routeImageForKouji(1)" value=""/>右90度</button>
                    <button type="button" class="btn btn-default" onclick="routeImageForKouji(0)" value=""/>左90度</button>
                </div>
            </div>
            <div class="row">
                <div class="row previewDiv"  id="koujiImg">
                    <!-- image -->
                    <img id="previewImageKouji" class="previewImageKouji" style="border:solid 1px silver"   width="600" src="" />
                </div>
            </div>
        </div>
    </div>
</div>
</section>
</div>
</body>

<script type="text/javascript">
//図面回転用
valueICS=0;//ICS用
valueGP=0; //GP用
valueBen=0;//valve 用
valueKouji=0;//kouji 用


//図面用
//var BUCKETValdacConstruction = 'valdac-construction-aisa'; //テストサーバ用
var BUCKETValdacConstruction = 'valdac-construction-asia'; //本番
//var BUCKETValdac = 'valdac-aisa'; //テストサーバ用
var BUCKETValdac = 'valdac-asia';   //本番
var BUCKETICS="icsmaster";
var BUCKETGP="valdac";

$(window).load(function () {
    //最初の図面を表示する
    loadImg("${IcsFirstImage.imagename}","previewImageIcs");
    loadImg("${GPFirstImage.imagename}","previewImageGP");
    loadImg("${ValveFirstImage.imagename}","previewImage");
    loadImg("${KoujiFirstImage.imagename}","previewImageKouji");

    //先頭、最終へボタン設定
    firstAndLastButton(1,"${IcsImageListSize}","firstButtonICS","previousButtonICS","NextButtonICS","lastButtonICS");
    firstAndLastButton(1,"${GPImageListSize}","firstButtonGP","previousButtonGP","NextButtonGP","lastButtonGP");
    firstAndLastButton(1,"${imageListKikisysSize}","firstButtonValve","previousButtonValve","NextButtonValve","lastButtonValve");
    firstAndLastButton(1,"${imageListSize}","firstButtonKouji","previousButtonKouji","NextButtonKouji","lastButtonKouji");
});

function routeImageForICS(type){
    var type=type;
    var tmpAngle;

    if(type==1){//右回り
        valueICS=(valueICS+90)%360;
        tmpAngle=valueICS-90;
    }else{//左回り
        valueICS=(valueICS-90)%360;
        tmpAngle=valueICS+90;
    }
    //クリックして回す
    $("#ICSImg img").rotate({
        angle:tmpAngle,
        animateTo: valueICS,
        easing: $.easing.easeInOutExpo
    });
};
function routeImageForGP(type){
    var type=type;
    var tmpAngle;

    if(type==1){//右回り
        valueGP=(valueGP+90)%360;
        tmpAngle=valueGP-90;
    }else{//左回り
        valueGP=(valueGP-90)%360;
        tmpAngle=valueGP+90;
    }
    //クリックして回す
    $("#GPImg img").rotate({
        angle:tmpAngle,
        animateTo: valueGP,
        easing: $.easing.easeInOutExpo
    });
};
function routeImageForValve(type){
    var type=type;
    var tmpAngle;

    if(type==1){//右回り
        valueBen=(valueBen+90)%360;
        tmpAngle=valueBen-90;
    }else{//左回り
        valueBen=(valueBen-90)%360;
        tmpAngle=valueBen+90;
    }
    //クリックして回す
    $("#valveImg img").rotate({
        angle:tmpAngle,
        animateTo: valueBen,
        easing: $.easing.easeInOutExpo
    });
};
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
function routeImage(type,value) {
    if(type==1){
        $("#ICSImg img").rotate({
            angle: 0,
            animateTo: value,
            easing: $.easing.easeInOutExpo
        });
    }else if(type==2){
        $("#GPImg img").rotate({
            angle: 0,
            animateTo: value,
            easing: $.easing.easeInOutExpo
        });
    }else if(type==3){
        $("#valveImg img").rotate({
            angle: 0,
            animateTo: value,
            easing: $.easing.easeInOutExpo
        });
    }else if(type==4){
        $("#koujiImg img").rotate({
            angle: 0,
            animateTo: value,
            easing: $.easing.easeInOutExpo
        });
    }
};

$(document).ready(function(){
    //ユーザ権限
    var userKengen=$("#userKengen").val();
    if(userKengen=="6"){
        $(".kengen-operation").show();
    }else{
        $(".kengen-operation").hide();
        $(".kengen-operation-readonly").attr("disabled",true);
    }

    $("#left-menu-new").addClass("active");

    $(".box-panel").click(function(){
        $(this).next().toggle();
    });


});
function setTagFlg(type){
    //図面typeにより　表示
    if(type=="1"){
        document.getElementById("div-1").style.display='block';
        document.getElementById("div-2").style.display="none";
        document.getElementById("div-3").style.display="none";
        document.getElementById("div-4").style.display="none";
    }else if(type=="2"){
        document.getElementById("div-1").style.display='none';
        document.getElementById("div-2").style.display="block";
        document.getElementById("div-3").style.display="none";
        document.getElementById("div-4").style.display="none";
    }else if(type=="3"){
        document.getElementById("div-1").style.display='none';
        document.getElementById("div-2").style.display="none";
        document.getElementById("div-3").style.display="block";
        document.getElementById("div-4").style.display="none";
    }else {
        document.getElementById("div-1").style.display='none';
        document.getElementById("div-2").style.display="none";
        document.getElementById("div-3").style.display="none";
        document.getElementById("div-4").style.display="block";
    }

}
//画像名を取得
function getImageName(imageUrl){
    var imageUrlname=document.getElementById(imageUrl).src;
    imageUrlname=imageUrlname.replace("http://storage.googleapis.com/","");
    return imageUrlname;
}

function printImage(type){
    //画像名を取得
    if(type=="1"){
        var imageUrl=document.getElementById("previewImageIcs").src;
    }else if(type=="2"){
        var imageUrl=document.getElementById("previewImageGP").src;
    }else if(type=="3"){
        var imageUrl=document.getElementById("previewImage").src;
    }else {
        var imageUrl=document.getElementById("previewImageKouji").src;
    }

    imageUrl=imageUrl.replace("http://storage.googleapis.com/","");
    if(imageUrl.length>10){
        window.open("https://valdac.power-science.com/print.html?"+imageUrl);
    }
}
//set image for valve
function setImage(items,flg) {
    if(flg){
//        document.getElementById('previewImage').src="http://storage.googleapis.com/valdac/"+items.imagename;
        loadImg(items.imagename,'previewImage');
        document.getElementById('imagebikou').innerHTML=items.bikou;
        document.getElementById('imagesyu').innerHTML=items.imagesyu;
    }else{
        document.getElementById("valueCurrent").innerHTML ="0";
        document.getElementById("valueTotal").innerHTML="0";
        document.getElementById('previewImage').src="http://storage.googleapis.com/noimage";
        document.getElementById('imagebikou').innerHTML="";
        document.getElementById('imagesyu').innerHTML="";
    }
    //図面向けリセット
    valueBen=0;
    routeImage(3,0);
}

//set image for kouji
function setImageForKouji(items,flg) {
    if(flg){
//        document.getElementById('previewImageKouji').src="http://storage.googleapis.com/valdacHost/"+items.imagename;
        loadImg(items.imagename,'previewImageKouji');
        document.getElementById('imagebikouKouji').innerHTML=items.bikou;
        document.getElementById('imagesyuKouji').innerHTML=items.imagesyu;
    }else{
        document.getElementById("valueCurrentKouji").innerHTML ="0";
        document.getElementById("valueTotalKouji").innerHTML="0";
        document.getElementById('previewImageKouji').src="http://storage.googleapis.com/noimage";
        document.getElementById('imagebikouKouji').innerHTML="";
        document.getElementById('imagesyuKouji').innerHTML="";
    }
    //図面向けリセット
    valueKouji=0;
    routeImage(4,0);
}
//種別毎に表示
function turnToImagesyu(type){
    //indexを取得
    if(type=="1"){
        var numallKikisysImageList01=${allKikisysImageList01.size()};
        if(numallKikisysImageList01>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setImagesyuSession", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImage(items,true)
                document.getElementById("valueCurrent").innerHTML ="1";
                document.getElementById("valueTotal").innerHTML=numallKikisysImageList01+"";
            });
        }else{
            setImage(null,false);
        }
    }else if(type=="2"){
        var numallKikisysImageList02=${allKikisysImageList02.size()};
        if(numallKikisysImageList02>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setImagesyuSession", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImage(items,true)
                document.getElementById("valueCurrent").innerHTML ="1";
                document.getElementById("valueTotal").innerHTML=numallKikisysImageList02+"";
            });
        }else{
            setImage(null,false);
        }
    }else if(type=="3"){
        var numallKikisysImageList03=${allKikisysImageList03.size()};
        if(numallKikisysImageList03>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setImagesyuSession", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImage(items,true)
                document.getElementById("valueCurrent").innerHTML ="1";
                document.getElementById("valueTotal").innerHTML=numallKikisysImageList03+"";
            });
        }else{
            setImage(null,false);
        }
    }else if(type=="4"){
        var numallKikisysImageList04=${allKikisysImageList04.size()};
        if(numallKikisysImageList04>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setImagesyuSession", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImage(items,true)
                document.getElementById("valueCurrent").innerHTML ="1";
                document.getElementById("valueTotal").innerHTML=numallKikisysImageList04+"";
            });
        }else{
            setImage(null,false);
        }
    }else if(type=="5"){
        var numallKikisysImageList05=${allKikisysImageList05.size()};
        if(numallKikisysImageList05>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setImagesyuSession", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImage(items,true)
                document.getElementById("valueCurrent").innerHTML ="1";
                document.getElementById("valueTotal").innerHTML=numallKikisysImageList05+"";
            });
        }else{
            setImage(null,false);
        }
    }else if(type=="6"){
        var numallKikisysImageList06=${allKikisysImageList06.size()};
        if(numallKikisysImageList06>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setImagesyuSession", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImage(items,true)
                document.getElementById("valueCurrent").innerHTML ="1";
                document.getElementById("valueTotal").innerHTML=numallKikisysImageList05+"";
            });
        }else{
            setImage(null,false);
        }
    }else {
//            すべての図面
        var numallKikisysImageList00=${allKikisysImageList.size()};
        if(numallKikisysImageList00>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setImagesyuSession", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImage(items,true)
                document.getElementById("valueCurrent").innerHTML ="1";
                document.getElementById("valueTotal").innerHTML=numallKikisysImageList00+"";
            });
        }else{
            setImage(null,false);
        }
    }
}

//種別毎に表示  kouji
function turnToImagesyuKouji(type){
    //indexを取得
    if(type=="1"){
        var numallKikisysImageList01=${allKikisysImageListKouji01.size()};
        if(numallKikisysImageList01>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setReportImagesyuSessionForKouji", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImageForKouji(items,true)
                document.getElementById("valueCurrentKouji").innerHTML ="1";
                document.getElementById("valueTotalKouji").innerHTML=numallKikisysImageList01+"";
            });
        }else{
            setImageForKouji(null,false);
        }
    }else if(type=="2"){
        var numallKikisysImageList02=${allKikisysImageListKouji02.size()};
        if(numallKikisysImageList02>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setReportImagesyuSessionForKouji", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImageForKouji(items,true)
                document.getElementById("valueCurrentKouji").innerHTML ="1";
                document.getElementById("valueTotalKouji").innerHTML=numallKikisysImageList02+"";
            });
        }else{
            setImageForKouji(null,false);
        }
    }else if(type=="3"){
        var numallKikisysImageList03=${allKikisysImageListKouji03.size()};
        if(numallKikisysImageList03>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setReportImagesyuSessionForKouji", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImageForKouji(items,true)
                document.getElementById("valueCurrentKouji").innerHTML ="1";
                document.getElementById("valueTotalKouji").innerHTML=numallKikisysImageList03+"";
            });
        }else{
            setImageForKouji(null,false);
        }
    }else if(type=="4"){
        var numallKikisysImageList04=${allKikisysImageListKouji04.size()};
        if(numallKikisysImageList04>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setReportImagesyuSessionForKouji", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImageForKouji(items,true)
                document.getElementById("valueCurrentKouji").innerHTML ="1";
                document.getElementById("valueTotalKouji").innerHTML=numallKikisysImageList04+"";
            });
        }else{
            setImageForKouji(null,false);
        }
    }else if(type=="5"){
        var numallKikisysImageList05=${allKikisysImageListKouji05.size()};
        if(numallKikisysImageList05>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setReportImagesyuSessionForKouji", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImageForKouji(items,true)
                document.getElementById("valueCurrentKouji").innerHTML ="1";
                document.getElementById("valueTotalKouji").innerHTML=numallKikisysImageList05+"";
            });
        }else{
            setImageForKouji(null,false);
        }
    }else if(type=="6"){
        var numallKikisysImageList06=${allKikisysImageListKouji06.size()};
        if(numallKikisysImageList06>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setReportImagesyuSessionForKouji", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImageForKouji(items,true)
                document.getElementById("valueCurrentKouji").innerHTML ="1";
                document.getElementById("valueTotalKouji").innerHTML=numallKikisysImageList05+"";
            });
        }else{
            setImageForKouji(null,false);
        }
    }else {
//            すべての図面
        var numallKikisysImageList00=${allKikisysImageListKouji.size()};
        if(numallKikisysImageList00>0){
            //該種類のデータを設定
            $.post("/valdacHost/image/setReportImagesyuSessionForKouji", {"type": type}, function (data) {
                var items = JSON.parse(data);
                setImageForKouji(items,true)
                document.getElementById("valueCurrentKouji").innerHTML ="1";
                document.getElementById("valueTotalKouji").innerHTML=numallKikisysImageList00+"";
            });
        }else{
            setImageForKouji(null,false);
        }
    }
}

function turnToPreviousPage(type){
    //indexを取得
    if(type=="1"){
        var valueCurrent = parseInt(document.getElementById("valueCurrentIcs").innerHTML);
        var valueTotal = parseInt(document.getElementById("valueTotalIcs").innerHTML);
    }else if(type=="2"){
        var valueCurrent = parseInt(document.getElementById("valueCurrentGP").innerHTML);
        var valueTotal = parseInt(document.getElementById("valueTotalGP").innerHTML);
    }else if(type=="3"){
        var valueCurrent = parseInt(document.getElementById("valueCurrent").innerHTML);
        var valueTotal = parseInt(document.getElementById("valueTotal").innerHTML);
    }else {
        var valueCurrent = parseInt(document.getElementById("valueCurrentKouji").innerHTML);
        var valueTotal = parseInt(document.getElementById("valueTotalKouji").innerHTML);
    }

    valueCurrent = valueCurrent - 1;
    if(valueCurrent > 0) {
        turnToSet(valueCurrent,valueTotal,type);
    }
}

function turnToFirstPage(type){
    //indexを取得
    if(type=="1"){
        var valueTotal = parseInt(document.getElementById("valueTotalIcs").innerHTML);
    }else if(type=="2"){
        var valueTotal = parseInt(document.getElementById("valueTotalGP").innerHTML);
    }else if(type=="3"){
        var valueTotal = parseInt(document.getElementById("valueTotal").innerHTML);
    }else {
        var valueTotal = parseInt(document.getElementById("valueTotalKouji").innerHTML);
    }

    valueCurrent = 1;
    //ページ数以内場合遷移する
    if(valueCurrent > 0 && valueCurrent<=valueTotal) {
        turnToSet(valueCurrent,valueTotal,type);
    }
}

function turnToLastPage(type){
    //indexを取得
    if(type=="1"){
        var valueTotal = parseInt(document.getElementById("valueTotalIcs").innerHTML);
    }else if(type=="2"){
        var valueTotal = parseInt(document.getElementById("valueTotalGP").innerHTML);
    }else if(type=="3"){
        var valueTotal = parseInt(document.getElementById("valueTotal").innerHTML);
    }else {
        var valueTotal = parseInt(document.getElementById("valueTotalKouji").innerHTML);
    }

    valueCurrent = valueTotal;
    //ページ数以内場合遷移する
    if(valueCurrent > 0) {
        turnToSet(valueCurrent,valueTotal,type);
    }
}

function turnToNumPage(type){
    //indexを取得
    if(type=="1"){
        var valueTotal = parseInt(document.getElementById("valueTotalIcs").innerHTML);
        var valueCurrent=0;
    }else if(type=="2"){
        var valueTotal = parseInt(document.getElementById("valueTotalGP").innerHTML);
        var valueCurrent=0;
    }else if(type=="3"){
        var valueTotal = parseInt(document.getElementById("valueTotal").innerHTML);
        var valueCurrent = parseInt(document.getElementById("jump_page_num").value);
    }else {
        var valueTotal = parseInt(document.getElementById("valueTotalKouji").innerHTML);
        var valueCurrent = parseInt(document.getElementById("jump_page_num_kouji").value);
    }
    //枚数目を取得

    console.log("valueCurrent="+valueCurrent+"  type="+type+"  valueTotal ="+valueTotal);
    //ページ数以内場合遷移する
    if(valueCurrent > 0 && valueCurrent<=valueTotal) {
        turnToSet(valueCurrent,valueTotal,type);
    }else{
        alert("ページ数以内に半角数字を入力ください");
    }
}

function turnToNextPage(type){

    //indexを取得
    if(type=="1"){
        var valueCurrent = parseInt(document.getElementById("valueCurrentIcs").innerHTML);
        var valueTotal = parseInt(document.getElementById("valueTotalIcs").innerHTML);
    }else if(type=="2"){
        var valueCurrent = parseInt(document.getElementById("valueCurrentGP").innerHTML);
        var valueTotal = parseInt(document.getElementById("valueTotalGP").innerHTML);
    }else if(type=="3"){
        var valueCurrent = parseInt(document.getElementById("valueCurrent").innerHTML);
        var valueTotal = parseInt(document.getElementById("valueTotal").innerHTML);
    }else {
        var valueCurrent = parseInt(document.getElementById("valueCurrentKouji").innerHTML);
        var valueTotal = parseInt(document.getElementById("valueTotalKouji").innerHTML);
    }

    valueCurrent = valueCurrent + 1;
    if(valueCurrent <= valueTotal) {
        turnToSet(valueCurrent,valueTotal,type);
    }
}

function turnToSet(setNumStr,totalNumStr,type){
    var valueTotal = parseInt(totalNumStr);
    var setNum = parseInt(setNumStr);
    if(setNum >= 1 && setNum <= valueTotal){
        setNum = setNum - 1;
        console.log("setNum="+setNum+"  ;type="+type);
        $.post("/valdacHost/image/getimageByValveID", {"setNum": setNum,"type":type}, function (data) {
            var items = JSON.parse(data);
            var tmpImgUrl=items.imagename;
            if(type=="1"){
//                document.getElementById('previewImageIcs').src="http://storage.googleapis.com/valdac/"+items.imagename;
                //図面描画
                loadImg(tmpImgUrl,"previewImageIcs");
                //先頭、最終へボタン設定
                firstAndLastButton(1,"${IcsImageListSize}","firstButtonICS","previousButtonICS","NextButtonICS","lastButtonICS");

                document.getElementById('imagebikouIcs').innerHTML=items.bikou;
                document.getElementById('imagesyuIcs').innerHTML=items.imagesyu;
                document.getElementById("valueCurrentIcs").innerHTML = setNumStr;
            }else if(type=="2"){
//                document.getElementById('previewImageGP').src="http://storage.googleapis.com/valdac/"+items.imagename;
                //図面描画
                loadImg(tmpImgUrl,"previewImageGP");
                //先頭、最終へボタン設定
                firstAndLastButton(1,"${GPImageListSize}","firstButtonGP","previousButtonGP","NextButtonGP","lastButtonGP");

                document.getElementById('imagebikouGP').innerHTML=items.bikou;
                document.getElementById('imagesyuGP').innerHTML=items.imagesyu;
                document.getElementById("valueCurrentGP").innerHTML = setNumStr;
            }else if(type=="3"){
//                document.getElementById('previewImage').src="http://storage.googleapis.com/valdac/"+items.imagename;
                //図面描画
                loadImg(tmpImgUrl,"previewImage");
                //先頭、最終へボタン設定
                firstAndLastButton(setNum+1,valueTotal,"firstButtonValve","previousButtonValve","NextButtonValve","lastButtonValve");

                document.getElementById('imagebikou').innerHTML=items.bikou;
                document.getElementById('imagesyu').innerHTML=items.imagesyu;
                document.getElementById("valueCurrent").innerHTML = setNumStr;
            }else {
//                document.getElementById('previewImageKouji').src="http://storage.googleapis.com/valdacHost/"+items.imagename;
                //図面描画
                loadImg(tmpImgUrl,"previewImageKouji");
                //先頭、最終へボタン設定
                firstAndLastButton(setNum+1,valueTotal,"firstButtonKouji","previousButtonKouji","NextButtonKouji","lastButtonKouji");

                document.getElementById('imagebikouKouji').innerHTML=items.bikou;
                document.getElementById('imagesyuKouji').innerHTML=items.imagesyu;
                document.getElementById("valueCurrentKouji").innerHTML = setNumStr;
            }
            //図面を元の向けにする
            valueICS=0;//ICS用
            valueGP=0; //GP用
            valueBen=0;//valve 用
            valueKouji=0;//kouji 用
            routeImage(1,0);
            routeImage(2,0);
            routeImage(3,0);
            routeImage(4,0);
        });
    }
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

function loadImg(imgUrl,ImageId) {
    document.getElementById(ImageId).src="/valdacHost/img/black.png";
    var OBJECT_NAME=imgUrl;
    //bucket  totalSet
    var BUCKET="";
    var totalSet =0;
    if(ImageId=="previewImageIcs"){
        BUCKET=BUCKETValdac;
        totalSet = parseInt(document.getElementById("valueTotalIcs").innerHTML);
    }else if(ImageId=="previewImageGP"){
        BUCKET=BUCKETValdac;
        totalSet = parseInt(document.getElementById("valueTotalGP").innerHTML);
    }else if(ImageId=="previewImage"){
        BUCKET=BUCKETValdac;
        totalSet = parseInt(document.getElementById("valueTotal").innerHTML);
    }else{
        BUCKET=BUCKETValdacConstruction;
        totalSet = parseInt(document.getElementById("valueTotalKouji").innerHTML);
    }

    if(totalSet>0){
        //javaにて　signUrlを取得する
        $.post("/valdacHost/SingUrlTest/SignTest",{"OBJECT_NAME":OBJECT_NAME,"BUCKET_NAME":BUCKET},function(data){
            console.log("data="+data);
            //画像読み込みの直前にLoading画像を表示する
            $("#loading").css({'display':'block'});
            //imgPreloaderを作成
            var imgPreloader=new Image();
            //onloadイベントハンドラ
            imgPreloader.onload=function() {
                $("#loading").css({'display':'none'});
                //img 設定
                document.getElementById(ImageId).src=data;
            }
            imgPreloader.onerror=function(){
                $("#loading").css({'display':'none'});
                //エラー画像設定
                document.getElementById(ImageId).src="/valdacHost/img/error.jpg";
            }
            //url-Set
            imgPreloader.src=data;
        });
    }else{
        document.getElementById(ImageId).src="";
    }
};

</script>

</html>