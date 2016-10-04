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
    </section>

    <!-- Main content -->
    <section class="content">

        <div class="row">
            <!-- collection -->
            <div class="col-md-6">
                <div class="btn-group" role="group">
                    <a href="/valdacHost/item/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">弁基本情報</a>
                    <a href="/valdacHost/kenan/kenan/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">懸案</a>
                    <a href="/valdacHost/tenken/tenken/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default bg-yellow active">点検履歴</a>
                    <a href="/valdacHost/image/image/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">図面</a>
                    <a href="/valdacHost/list/buhinSearch/" class="btn btn-default">戻る</a>
                </div>
            </div>
        </div>
        <hr/>


        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">弁基本情報</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-2">
                                    弁番号:
                                </div>
                                <div class="col-md-2">
                                    <input type="text" id="VNo" name="VNo" class="form-control" readonly="readonly"  value="${valve.vNo}">
                                </div>
                                <div class="col-md-2">
                                    弁名称:
                                </div>
                                <div class="col-md-5">
                                    <input type="text" id="BenMeisyo" name="BenMeisyo" class="form-control" readonly="readonly"  value="${valve.benMeisyo}">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-2">
                                    設置プラント
                                </div>
                                <div class="col-md-9">
                                    <input type="text" id="locationName" name="locationName" class="form-control" readonly="readonly" value="${valve.locationName}">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- collection -->
            <div class="tab-content">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">点検履歴</h3>
                        </div>
                        <table class="table table-hover" id="kenan-table">
                            <thead>
                            <tr>
                                <th>工事名</th>
                                <th>機器分類</th>
                                <th>メーカー</th>
                                <th>型式番号</th>
                                <th>点検ランク</th>
                                <th>点検内容</th>
                                <th>点検結果</th>
                                <th>特記事項</th>
                                <th>点検日付</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${tenkenRirekiListSearch}" var="tenkenRirekiUtil">
                                <tr id="${tenkenRirekiUtil.id}">
                                    <td>${tenkenRirekiUtil.kouji.kjMeisyo}</td>
                                    <td>${tenkenRirekiUtil.kiki.kikiBunrui}</td>
                                    <td>${tenkenRirekiUtil.kiki.makerRyaku}/${tenkenRirekiUtil.kiki.maker}</td>
                                    <td>${tenkenRirekiUtil.kiki.katasikiNo}</td>
                                    <td>${tenkenRirekiUtil.tenkenRank}</td>
                                    <td>${tenkenRirekiUtil.tenkennaiyo}</td>
                                    <td>${tenkenRirekiUtil.tenkenkekka}</td>
                                    <td>${tenkenRirekiUtil.tenkenBikou}</td>
                                    <td>${tenkenRirekiUtil.tenkenDate}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                   </div>
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

