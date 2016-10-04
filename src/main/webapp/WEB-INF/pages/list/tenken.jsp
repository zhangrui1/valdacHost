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
            点検履歴検索
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
                    <a href="/valdacHost/list/kenan" class="btn btn-default">懸案</a>
                    <a href="/valdacHost/list/tenken" class="btn btn-default bg-yellow active">点検履歴</a>
                </div>
            </div>
            <form class="navbar-form navbar-left" role="search" id="searchForm" action="/valdacHost/tenken/search/">
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
                            <th>弁名称</th>
                            <th>設置プラント</th>
                            <th>工事名</th>
                            <th>機器分類</th>
                            <th>機器名称</th>
                            <th>型式番号</th>
                            <th>点検ランク</th>
                            <th>点検内容</th>
                            <th>点検結果</th>
                            <th>特記事項</th>
                            <%--<th></th>--%>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${tenkenRirekiListSearch}" var="tenkenRirekiUtil">
                            <tr id="${tenkenRirekiUtil.id}">
                                <td>${tenkenRirekiUtil.valve.vNo}</td>
                                <td>${tenkenRirekiUtil.valve.benMeisyo}</td>
                                <td>${tenkenRirekiUtil.valve.locationName}</td>
                                <td>${tenkenRirekiUtil.kouji.kjMeisyo}</td>
                                <td>${tenkenRirekiUtil.kiki.kikiBunrui}</td>
                                <td>${tenkenRirekiUtil.kiki.kikiMei}</td>
                                <td>${tenkenRirekiUtil.kiki.katasikiNo}</td>
                                <td>${tenkenRirekiUtil.tenkenRank}</td>
                                <td>${tenkenRirekiUtil.tenkennaiyo}</td>
                                <td>${tenkenRirekiUtil.tenkenkekka}</td>
                                <td>${tenkenRirekiUtil.tenkenBikou}</td>
                                <%--<td>--%>
                                     <%--<select class="form-control tenken-select" onchange="saveTenkenrank(this)">--%>
                                        <%--<option>${tenkenRirekiUtil.tenkenRank}</option>--%>
                                        <%--<option>A</option>--%>
                                        <%--<option>B</option>--%>
                                        <%--<option>C</option>--%>
                                        <%--<option>D</option>--%>
                                        <%--<option>E</option>--%>
                                    <%--</select>--%>
                                <%--</td>--%>
                                <%--<td>--%>
                                <%--<input type="text" id="${tenkenRirekiUtil.id}-naiyo" class="form-control input-xs" style="height:25px" placeholder="点検内容" onblur="saveTenkennaiyo(this)" value="${tenkenRirekiUtil.tenkennaiyo}" />--%>
                                <%--</td>--%>
                                <%--<td>--%>
                                    <%--&lt;%&ndash;<input type="text" class="form-control input-xs" style="height:25px" placeholder="点検結果" onblur="saveTenkenkekka(this)" value="${tenkenRirekiUtil.tenkenkekka}" />&ndash;%&gt;--%>
                                    <%--<select  class="form-control kekka-select" style="height:25px" onchange="saveTenkenkekka(this)">--%>
                                        <%--<option>${tenkenRirekiUtil.tenkenkekka}</option>--%>
                                        <%--<option>A</option>--%>
                                        <%--<option>B</option>--%>
                                        <%--<option>C</option>--%>
                                        <%--<option>D</option>--%>
                                        <%--<option>E</option>--%>
                                        <%--<option>F</option>--%>
                                        <%--<option>G</option>--%>
                                        <%--<option>Y</option>--%>
                                        <%--<option>Z</option>--%>
                                    <%--</select>--%>
                                <%--</td>--%>
                                <%--<td>--%>
                                <%--<input type="text" class="form-control input-xs" style="height:25px" placeholder="特記事項" onblur="saveTenkenBikou(this)" value="${tenkenRirekiUtil.tenkenBikou}" />--%>
                                <%--</td>--%>
                                <%--<td>--%>
                                    <%--<button class="btn btn-xs bg-red" onclick="deleteTenkenKiki(this)"><i class="glyphicon glyphicon-remove"></i></button>--%>
                                <%--</td>--%>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </section><!-- /.content -->
</div><!-- ./wrapper -->

<script type="text/javascript">
    function searchDataTd(obj){
        if(obj.value.length < 1){
            $(".data-tr").show();
        }
        var dataTr = $(".data-tr");
        var keyword = new String(obj.value);
        for(var i = 0;i<dataTr.length;i++){
            $(dataTr[i]).hide();
            var dataTd = $(dataTr[i]).find(".data-td");
            for(var j = 0;j<dataTd.length;j++){
                var htmlData = new String(dataTd[j].innerHTML);
                if(htmlData.indexOf(keyword) > -1){
                    $(dataTr[i]).show();
                    break;
                }
            }
        }
    }
</script>

<script type="text/javascript">



    function deleteTenkenKiki(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $("#rirekiId").val(rirekiId);

        if (!confirm("この行を削除しますか？"))
            return;

        $.get("/valdacHost/tenken/deleteTenkenRirekiByTenkenrirekiSearch",{"id":rirekiId},function(data){

            var objTR=obj.parentNode.parentNode;
            var objTBL=objTR.parentNode;
            if(objTBL){
                objTBL.deleteRow(objTR.sectionRowIndex);
            }

        });
    }

    function saveTenkenrank(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;

        //自動的に点検内容を設定する
        if(obj.value=="A"){
            document.getElementById(rirekiId+'-naiyo').value="試験のみ";
        }else if(obj.value=="B"){
            document.getElementById(rirekiId+'-naiyo').value="GP入替";
        }else if(obj.value=="C"){
            document.getElementById(rirekiId+'-naiyo').value="分解点検";
        }else if(obj.value=="D"){
            document.getElementById(rirekiId+'-naiyo').value="取替";
        }else if(obj.value=="E"){
            document.getElementById(rirekiId+'-naiyo').value="その他";
        }else{
            document.getElementById(rirekiId+'-naiyo').value="";
        }
        var tenkennaiyo=document.getElementById(rirekiId+'-naiyo').value;
        $.post("/valdacHost/tenken/saveTenkenrankSearch",{"id":rirekiId,"tenkenrank":obj.value,"tenkennaiyo":tenkennaiyo},function(data){
            if(data == '完成'){
                if(!$(rireki).hasClass("success")) {
                    $(rireki).addClass("success");
                }
            } else {
                if($(rireki).hasClass("success")) {
                    $(rireki).removeClass("success");
                }
            }
            updateListNumbers();
        })
    }


    function saveTenkenkekka(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $.post("/valdacHost/tenken/saveTenkenkekka",{"id":rirekiId,"tenkenkekka":obj.value},function(data){
            if(data == '0'){
                if($(obj).hasClass("input-success")) {
                    $(obj).removeClass("input-success");
                }
            } else {
                if(!$(obj).hasClass("input-success")) {
                    $(obj).addClass("input-success");
                }
            }
        })
    }

    function saveTenkenBikou(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $.post("/valdacHost/tenken/saveTenkenBikou",{"id":rirekiId,"tenkenBikou":obj.value},function(data){
            if(data == '0'){
                if($(obj).hasClass("input-success")) {
                    $(obj).removeClass("input-success");
                }
            } else {
                if(!$(obj).hasClass("input-success")) {
                    $(obj).addClass("input-success");
                }
            }
        })
    }

    function saveTenkennaiyo(obj){
        var rireki = $(obj).parent().parent();
        var rirekiId = rireki[0].id;
        $.post("/valdacHost/tenken/saveTenkennaiyoSearch",{"id":rirekiId,"tenkennaiyo":obj.value},function(data){
            if(data == '0'){
                if($(obj).hasClass("input-success")) {
                    $(obj).removeClass("input-success");
                }
            } else {
                if(!$(obj).hasClass("input-success")) {
                    $(obj).addClass("input-success");
                }
            }
        })
    }

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
    });
    //検索キーワードにEnterキーを押す場合チェック
    function check(code){
        if(code==13){
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
        }
    }
</script>

<c:import url="../htmlframe/footerFrame.jsp" />

</body>
</html>

