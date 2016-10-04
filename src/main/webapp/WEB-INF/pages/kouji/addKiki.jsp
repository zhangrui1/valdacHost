<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 11/19/14
  Time: 2:53 PM
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

    <input type="hidden" id="syukan" value="${kouji.syukan}"/>
    <!-- Content Header (Page header) -->
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
            <div class="col-md-4 step-div last-step-complete">
                <span class="glyphicon glyphicon-indent-left"> 3. 点検機器選択</span>
            </div>
        </div>

        <input type="hidden" id="koujiId" value="${kouji.id}"/>
        <input type="hidden" id="currentKikiSysId" value=""/>
        <div class="row" id="content-row">
            <div class="col-md-12">
                <div class="panel">
                    <div class="panel-body">
                        <%--<div class="row filter-panel">--%>
                            <%--<div class="col-md-2"><input type="checkbox" value="全部" > 全部</div>--%>
                            <%--<div class="col-md-2"><input type="checkbox" value="弁本体" > 弁本体</div>--%>
                            <%--<div class="col-md-2"><input type="checkbox" value="付属品" > 付属品</div>--%>
                            <%--<div class="col-md-6"><a href="/valdacHost/kouji/${kouji.id}/saveValveKikiRelation" class="btn btn-success btn-sm pull-right">確定</a></div>--%>
                        <%--</div>--%>
                        <div class="kiki-content-panel">
                            <div class="col-md-8" style="padding-right: 0px;padding-left: 0px;background-color: rgb(239,239,239)">
                                <div class="row filter-div">
                                    <div class="col-md-6 text-gray">
                                        <button class="btn btn-sm btn-default" id="all_search_btn" onclick="allValveList()">全部弁表示</button>
                                        <button class="btn btn-sm btn-default selected_search_btn" id="selected_search_btn" onclick="selectedValveList()">選択されない弁のみ表示</button>
                                    </div>
                                    <div class="col-md-6"><input type="text" onkeyup="searchKikiSysList(this)" class="form-control input-sm pull-right" style="width: 60%" placeholder="弁検索"></div>
                                </div>
                                <ul class="list-group tab-list">
                                    <li>
                                        <div class="row">
                                            <div class="col-md-5">弁番号</div>
                                            <div class="col-md-7">弁名称</div>
                                        </div>
                                    </li>
                                    <c:forEach items="${valveList}" var="valve">
                                        <li onclick="chooseThisValve(this)" class="list-group-item tab-item" id="${valve.kikiSysId}">
                                            <div class="row">
                                                <div class="col-md-5">${valve.vNo}</div>
                                                <div class="col-md-7">${valve.benMeisyo}</div>
                                            </div>
                                            <input type="hidden" class="kikiSysId" value="${valve.kikiSysId}"/>
                                        </li>
                                    </c:forEach>
                                </ul><!-- 弁 list -->
                            </div>
                            <div class="col-md-4" style="padding-left: 0px">
                                <div class="kikiList-div">
                                    <div class="row filter-div">
                                        <div class="col-md-5">点検機器選択 <i id="spinner" class="fa fa-spinner fa-pulse"> </i></div>
                                        <%--<div class="col-md-7"><input type="text" onkeyup="searchKikiList(this)" class="form-control input-sm pull-right" style="width: 60%" placeholder="Filter"></div>--%>
                                    </div>
                                    <ul class="list-group item-list" id="kikiList">
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- information tab -->
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="panel">
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-4"></div>
                            <div class="col-md-2"><a href="/valdacHost/kouji/${kouji.id}/valve" class="btn btn-default btn-block"><i class="fa fa-backward"> 戻す</i></a></div>
                            <div class="col-md-2"><a href="/valdacHost/kouji/${kouji.id}/saveValveKikiRelation" class="btn btn-success btn-block" onclick="return checkValveAndTenenKiki();"><i class="fa fa-save"> 確定</i></a></div>
                            <div class="col-md-4"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </section>
</div>
<script type="text/javascript">
    $(document).ready(function(){
        $.get("/valdacHost/kouji/valveStatus",function(data){
            $(".tab-item").removeClass("haveSelect");
            var valveList = JSON.parse(data);
            for(var i = 0;i < valveList.length;i++){
                $("#"+valveList[i].kikiSysId).addClass("haveSelect");
            }
        });


    });

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
        $('#idList').val(idList);
//        return false;
    }

    function checkValveAndTenenKiki(){
        var allSelectFlg=true;
        var tmp=$('.tab-item');
        console.log("length="+tmp.length);
        for(var i=0;i<tmp.length;i++){
            var tmpName=$(tmp[i])[0].className;
            console.log(i+"  =   "+tmpName);
            //文字列が見つからない場合、indexOfが-1を返す。
            if(tmpName.indexOf("haveSelect")==-1){
                console.log("見つからない");
                alert("点検機器を選択されない弁があります、ご確認ください。");
                return false;
            }
        }
        return true;
    }
    //選択されない弁のみ表示
    function selectedValveList(){
        var allSelectFlg=true;
        var tmp=$('.tab-item');
        console.log("length="+tmp.length);
        for(var i=0;i<tmp.length;i++){
            var tmpName=$(tmp[i])[0].className;
            //文字列が見つからない場合、表示;見つかる場合、非表示
            if(tmpName.indexOf("haveSelect")==-1){
                console.log("見つからない");
            }else{
                $(tmp[i]).hide()
            }
        }

        //右のkikiリストをリセット
        $('#kikiList').html("");
        $('.tab-item').removeClass("active");
    }

    //すべて弁表示
    function allValveList(){
        var allSelectFlg=true;
        var tmp=$('.tab-item');
        console.log("length="+tmp.length);
        for(var i=0;i<tmp.length;i++){
           $(tmp[i]).show()
        }
        //右のkikiリストをリセット
        $('#kikiList').html("");
        $('.tab-item').removeClass("active");
    }

    function chooseThisValve(obj){
        $('#spinner').show();
        $('.tab-item').removeClass("active");
        $(obj).addClass("active");
        var koujiId = $("#koujiId").val();
        var id = $(obj).find(".kikiSysId")[0];
        var syukanValve = document.getElementById("syukan");
        $.post("/valdacHost/item/getKikiByKikiSysId",{"koujiId":koujiId,"kikiSysId":id.value,"syukan":syukanValve.value},
                function(data){
                    var kikiDatas = JSON.parse(data);
                    $("#kikiList").html("");
                    var tmpHTML = "";
                    for(var i = 0; i < kikiDatas.length;i++){
                        tmpHTML =tmpHTML+ '<li class="list-group-item kiki-item '+kikiDatas[i].status+'" onmouseup="selectKiki(this)" id="'+kikiDatas[i].kiki.kikiId+'">'+
                                '<div class="row">'+
                        '<div class="col-md-12">'+
                        '<span class="data-span">'+kikiDatas[i].kiki.kikiNo+'</span><span class="data-span">'+kikiDatas[i].kiki.kikiBunrui+'</span><span class="data-span">'+kikiDatas[i].kiki.kikiMei+'</span><span class="data-span">'+kikiDatas[i].kiki.serialNo+'</span><span class="data-span">'+kikiDatas[i].kiki.katasikiNo+'</span>'+
                        '</div>'+
                        '</div>'+
                        '</li>';
                    }
                    $('#kikiList').html(tmpHTML);
                    $('#currentKikiSysId').val(id.value);
                    $('#spinner').hide();
                }
        );
    }

    function selectKiki(obj){
        $(obj).toggleClass("active");
        var koujiId = $("#koujiId").val();
        if($(obj).hasClass("active")) {
            $.post("/valdacHost/item/saveStatusToSession", {"koujiId":koujiId,"kikiSysId": $('#currentKikiSysId').val(), "kikiId": obj.id, "status": "active"},function(data){
                console.log("active yes="+status);
                $("#"+$('#currentKikiSysId').val()).addClass("haveSelect");
            });
        } else {
            $.post("/valdacHost/item/saveStatusToSession", {"koujiId":koujiId,"kikiSysId": $('#currentKikiSysId').val(), "kikiId": obj.id, "status": ""},function(data){
                //選択を消す時、他の機器が選択される場合は、haveSelectを追加する、他の機器が選択されない場合、なにもしない
                $("#"+$('#currentKikiSysId').val()).removeClass("haveSelect");
                var datas = $('.kiki-item');
                for(var i=0;i<datas.length;i++){
                    console.log("datas[i]"+i+"= "+datas[i].className);
                    if(datas[i].className.indexOf("active") > -1){
                        $("#"+$('#currentKikiSysId').val()).addClass("haveSelect");
                        break;
                    }
                }
                console.log("active  no="+status);
            });
        }
    }

    function searchKikiSysList(obj){
        if(obj.value.length < 1){
            $('.tab-item').show();
        } else {
            var keyword = obj.value;
            keyword=keyword.toLowerCase();
            keyword=keyword.trim();

            var datas = $('.tab-item');
            for(var i = 0;i<datas.length;i++){
                var tmpHtml = new String(datas[i].innerText);
                tmpHtml=tmpHtml.toLowerCase();
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

    function searchKikiList(obj){
        if(obj.value.length < 1){
            $('.kiki-item').show();
        } else {
            var keyword = obj.value;
            keyword=keyword.toLowerCase();
            keyword=keyword.trim();

            var datas = $('.data-span');
            for(var i = 0;i<datas.length;i++){
                var tmpHtml = new String(datas[i].innerHTML);
                tmpHtml=tmpHtml.toLowerCase()
                if(tmpHtml.indexOf(keyword) > -1){
                    var li = $(datas[i]).parent().parent().parent();
//                    console.log(li);
                    $(li).show();
                } else {
                    var li = $(datas[i]).parent().parent().parent();
//                    console.log(li);
                    $(li).hide();
                }
            }
        }
    }

</script>
</body>
</html>
