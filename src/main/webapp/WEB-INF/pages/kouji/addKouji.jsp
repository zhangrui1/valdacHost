<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 11/18/14
  Time: 10:23 AM
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
            <i class="glyphicon glyphicon-wrench"> 新規工事</i>
        </h1>
        <%--<ol class="breadcrumb" style="font-size:20pt;">--%>
            <%--<li><i class="fa fa-dashboard"></i> Index/新規工事</li>--%>
        <%--</ol>--%>
    </section>
    <hr/>

    <section class="content">
        <div class="row" id="step-row">
            <div class="col-md-4 step-div first-step-complete">
                <span class="glyphicon glyphicon-calendar"> 1. 工事情報追加</span>
            </div>
            <div class="col-md-4 step-div">
                <span class="glyphicon glyphicon-list"> 2. 点検バルブ選択</span>
            </div>
            <div class="col-md-4 step-div">
                <span class="glyphicon glyphicon-indent-left"> 3. 点検機器選択</span>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">工事情報</h3>
                    </div>

                    <div class="panel-body">
                        <form id="KoujiForm" name="KoujiForm" action="/valdacHost/kouji/add" method="post" onsubmit="return check()">
                        <%--<form id="KoujiForm" name="KoujiForm" action="/valdacHost/kouji/add" method="post">--%>
                            <div class="row form-group">
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="kjNo" value="" placeholder="工事番号(必須)"/>
                                </div>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="kjMeisyo" value="" placeholder="工事名称(必須)"/>
                                </div>
                            </div>

                            <div class="row form-group">
                                <div class="col-md-3">
                                    <select name="person" id="person" class="form-control">
                                        <option>--責任者--</option>
                                    </select>
                                </div>
                                <div class="col-md-8" id="sandbox-container">
                                    <div class="input-daterange input-group" id="datepicker" style="width:100%">
                                        <div class="input-group-addon">
                                            <i class="fa fa-calendar"></i>
                                        </div>
                                        <input type="text" class="form-control" name="bgnYmd" id="bgnYmd" placeholder="開始日付(必須)">
                                        <span class="input-group-addon">〜</span>
                                        <input type="text" class="form-control" name="endYmd" placeholder="終了日付(必須)">
                                    </div>
                                </div>
                                <%--<div class="col-md-4">--%>
                                    <%--<input type="text" class="form-control" name="endYmd" value="" placeholder="終了日付"/>--%>
                                <%--</div>--%>
                                <div class="col-md-1">
                                    <input type="text" class="form-control" name="nendo" value="" id="nendo" placeholder="年度"/>
                                </div>
                            </div>

                            <div class="row form-group">
                                <div class="col-md-10">
                                    <input type="text" class="form-control location-master-toggle" name="location" id="location" value="" placeholder="設置プラント(必須)"/>
                                    <div class="panel panel-default dropdown-panel" id="location-master">
                                        <div class="panel-body">
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-md-4">
                                                        <input type="text" id="kCodeL-input" class="form-control" readonly="true" placeholder="会社名" />
                                                    </div>
                                                    <div class="col-md-4">
                                                        <input type="text" id="kCodeM-input" class="form-control" readonly="true" placeholder="発電所" />
                                                    </div>
                                                    <div class="col-md-4">
                                                        <input type="text" id="kCodeS-input" class="form-control" readonly="true" placeholder="機器名" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-md-4">
                                                        <select class="form-control" id="kCodeL-select">
                                                            <%--<option></option>--%>
                                                        </select>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <select class="form-control" id="kCodeM-select">
                                                            <%--<option></option>--%>
                                                        </select>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <select class="form-control" id="kCodeS-select">
                                                            <%--<option></option>--%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-2"><input type="button" id="master-location-confirm" class="btn btn-block btn-success" value="確定"></div>
                                                <div class="col-md-2"><input type="button" id="master-location-cancel" class="btn btn-block btn-default" value="取消"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <select name="kjKbn" id="kjKbn" class="form-control">
                                        <option>--工事区分--</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row form-group">
                                <div class="col-md-4">
                                    <select name="syukan" id="syukan" class="form-control">
                                        <option>--主管係--</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <select name="gyosyaRyakuA" id="gyosyaRyakuA" class="form-control">
                                        <option>--点検業者--</option>
                                    </select>
                                </div>
                                <div class="col-md-4" id="nextYmdDiv">
                                    <input type="text" class="form-control" name="nextYmd" value="" placeholder="今後日付"/>
                                </div>

                            </div>

                            <div class="row form-group">
                                <div class="col-md-10">
                                    <input type="submit" class="btn btn-success" value="保存"/>
                                    <%--<input type="button" class="btn btn-default" onclick="KoujiForm.clear();return false" value="リセット"/>--%>
                                </div>
                            </div>
                        </form>
                    </div>
                    <!-- information penal -->
                </div>

            </div>
            <!-- information tab -->
        </div>
    </section>
</div>

<script type="text/javascript">
    $(document).ready(function(){

        //工事区分 Master を呼ぶ
        $.post("/valdacHost/master/getMasterByTypeJson",{"type":'工事区分'},function(data){
            var masters = JSON.parse(data);
            $('#kjKbn').html("");
            var tmpHTML = "<option>--工事区分--</option>";
            for(var i = 0 ; i < masters.length ; i++){
                tmpHTML = tmpHTML+"<option>" + masters[i].name + "</option>";
            }
            $('#kjKbn').html(tmpHTML);
        });

        //責任者 Master を呼ぶ
        $.post("/valdacHost/master/getMasterByTypeJson",{"type":'責任者'},function(data){
            var masters = JSON.parse(data);
            $('#person').html("");
            var tmpHTML = "<option>--責任者--</option>";
            for(var i = 0 ; i < masters.length ; i++){
                tmpHTML = tmpHTML+"<option>" + masters[i].name + "</option>";
            }
            $('#person').html(tmpHTML);
        });

        //主管係 Master を呼ぶ
        $.post("/valdacHost/master/getMasterByTypeJson",{"type":'主管係'},function(data){
            var masters = JSON.parse(data);
            $('#syukan').html("");
            var tmpHTML = "<option>--主管係--</option>";
            for(var i = 0 ; i < masters.length ; i++){
                tmpHTML = tmpHTML+"<option>" + masters[i].name + "</option>";
            }
            $('#syukan').html(tmpHTML);
        });

        //主管係 Master を呼ぶ
        $.post("/valdacHost/master/getMasterByTypeJson",{"type":'点検業者'},function(data){
            var masters = JSON.parse(data);
            $('#gyosyaRyakuA').html("");
            var tmpHTML = "<option>--点検業者--</option>";
            for(var i = 0 ; i < masters.length ; i++){
                tmpHTML = tmpHTML+"<option>" + masters[i].name + "</option>";
            }
            $('#gyosyaRyakuA').html(tmpHTML);
        });

        //The Calender
        $('#sandbox-container .input-daterange').datepicker({
            format: 'yyyy/mm/dd',
            language: 'ja'
        });
        $('#nextYmdDiv input').datepicker({
            format: 'yyyy/mm/dd',
            language: 'ja'
        });

        $('#bgnYmd').change(function(){
            var dateStr = new String($('#bgnYmd').val());
            var nendo = dateStr.split('/');
            $('#nendo').val(nendo[0]);
        });
        $(".location-master-toggle").focus(function(obj){
            var toggleInput = obj.currentTarget;

            var dropdownPanel = $(toggleInput).next(".dropdown-panel");

            $.get("/valdacHost/location/getKCodeLJson",function(data){
                $("#kCodeL-select").html("");
                var tmpHTML = "<option></option>";
                var masters = JSON.parse(data);
                for(var i = 0;i<masters.length;i++){
                    tmpHTML = tmpHTML+ "<option>" + masters[i] + "</option>";
                }
                $("#kCodeL-select").html(tmpHTML);
                $("#kCodeM-select").html("");
                $("#kCodeS-select").html("");
                //選択した行を空にする
                $("#kCodeL-input").val("");
                $("#kCodeM-input").val("");
                $("#kCodeS-input").val("");
                $(dropdownPanel).show();
            });
        });

        $("#master-location-confirm").click(function(){
            var locationValue = $("#kCodeL-input").val()+" "+$("#kCodeM-input").val();
            locationValue=locationValue.trim();
            locationValue = locationValue+" "+$("#kCodeS-input").val();
           // 両端のスペースを削除
            locationValue=locationValue.trim();
            $("#location").val(locationValue);
            $(".dropdown-panel").hide();
        });

        $("#master-location-cancel").click(function(){
            $(".dropdown-panel").hide();
        });

        $("#kCodeL-select").change(function(){
            $("#kCodeL-input").val($("#kCodeL-select").val());
            $.post("/valdacHost/location/getKcodeMJsonBykCodeL",{"kCodeL":$("#kCodeL-select").val()},function(data){
                $("#kCodeM-select").html("");
                var tmpHTML = "<option></option>";
                var masters = JSON.parse(data);
                for(var i = 0;i<masters.length;i++){
                    tmpHTML = tmpHTML+ "<option>" + masters[i] + "</option>";
                }
                $("#kCodeM-select").html(tmpHTML);
            });
        });

        $("#kCodeM-select").change(function(){
            $("#kCodeM-input").val($("#kCodeM-select").val());
            $.post("/valdacHost/location/getKcodeSJsonBykCodeLkCodeM",{"kCodeL":$("#kCodeL-select").val(),"kCodeM":$("#kCodeM-select").val()},function(data){
                $("#kCodeS-select").html("");
                var tmpHTML = "<option></option>";
                var masters = JSON.parse(data);
                for(var i = 0;i<masters.length;i++){
                    tmpHTML = tmpHTML+ "<option>" + masters[i] + "</option>";
                }
                $("#kCodeS-select").html(tmpHTML);
            });
        });
        $("#kCodeS-select").change(function(){
            $("#kCodeS-input").val($("#kCodeS-select").val());
        });

    });


</script>

<script type="text/javascript">
    function check(){
        var flag=0;
        var nendo=0;
        //必須項目設定
        if(document.KoujiForm.kjNo.value==""){flag=1;}
        if(document.KoujiForm.kjMeisyo.value==""){flag=1;}
        if(document.KoujiForm.person.value==""){flag=1;}
        if(document.KoujiForm.bgnYmd.value==""){flag=1;}
        if(document.KoujiForm.endYmd.value==""){flag=1;}
        if(document.KoujiForm.nendo.value==""){flag=1;}
        if(document.KoujiForm.syukan.value==""){flag=1;}
        if(document.KoujiForm.gyosyaRyakuA.value==""){flag=1;}
        if(document.KoujiForm.location.value==""){flag=1;}

        //点検年度チェック　４桁の半角
        if(document.KoujiForm.nendo.value.length>4||document.KoujiForm.nendo.value.length<4){nendo=1;}
        if(document.KoujiForm.nendo.value.match(/[^0-9]+/)){nendo=1;}

        if(flag){
            window.alert("必須項目を入力ください");
            return false;
        }
        if(nendo){
            window.alert("点検年度を4桁半角数字で入力ください。例：2015");
            return false;
        }
        //長さ　判断
        if(document.KoujiForm.kjNo.value.length>20){
            window.alert("弁番号を20文字以内にしてください。");
            return false;
        }
        if(document.KoujiForm.kjMeisyo.value.length>90){
            window.alert("弁名称を90文字以内にしてください。");
            return false;
        }
        if(document.KoujiForm.location.value.length>255){
            window.alert("設置プラントを255文字以内にしてください。");
            return false;
        }


        //工事番号は重複かどうかチェック
        var KoujiCheckResult=1;
        var flag=true;

        var kjNo= document.KoujiForm.kjNo.value;
        var location=document.KoujiForm.location.value;
        location=location.trim();
        console.log("kjNo="+kjNo+"   ;location="+location);
        //DBにてチェックする
        $.ajax({
            url:"/valdacHost/kouji/getResultForKoujiVNoCheck",
            type: "POST",
            data: { "kjNo":kjNo,"location":location },
            async:false,
            success: function(data){
                var resultTmp=JSON.parse(data);
                console.log("resultTmp="+resultTmp);
                if(resultTmp){
                    KoujiCheckResult=0;//使える
                }else{
                    KoujiCheckResult=1;//使えない
                }
                flag=false;
            }
        });
        while(flag){
        }
        console.log("flag2="+flag);
        console.log("KoujiCheckResult="+KoujiCheckResult);
        if(KoujiCheckResult==1){
            window.alert("この工事番号はすでに使われてます。");
            return false;
        }
        return true;
    }

</script>

</body>
</html>
