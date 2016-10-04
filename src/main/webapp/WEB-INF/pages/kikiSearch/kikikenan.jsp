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
<section class="content-header">
</section>
<!-- Main content -->
<section class="content">

<div class="row">
    <!-- collection -->
    <div class="col-md-6">
        <div class="btn-group" role="group">
            <a href="/valdacHost/item/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">弁基本情報</a>
            <a href="/valdacHost/kenan/kenan/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default bg-yellow active">懸案</a>
            <a href="/valdacHost/tenken/tenken/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">点検履歴</a>
            <a href="/valdacHost/image/image/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">図面</a>
            <a href="/valdacHost/list/kikiSearch/" class="btn btn-default">戻る</a>
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
                            <input type="text" id="VNo" name="VNo" class="form-control" readonly="readonly" value="${valve.vNo}">
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
                    <h3 class="panel-title">懸案リスト</h3>
                </div>
                <table class="table table-hover" id="kenan-table">
                    <thead>
                    <tr>
                        <%--<th>機器名</th>--%>
                        <%--<th>工事名</th>--%>
                        <th>状態</th>
                        <th>発見日付</th>
                        <th>対策日付</th>
                        <th>機器の事象</th>
                        <th>部品・箇所</th>
                        <th>損傷の状況</th>
                        <th>損傷の要因</th>
                        <th>改善対策</th>
                        <th>処置内容</th>
                        <th>詳細</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${kenanFormListSearch}" var="kenanForm">
                        <tr class="data-tr" id="${kenanForm.id}">
                                <%--<td class="data-td">${kenanForm.kiki.kikiMei}</td>--%>
                                <%--<td class="data-td">${kenanForm.kouji.kjMeisyo}</td>--%>
                            <td class="data-td">${kenanForm.taiouFlg}</td>
                            <td class="data-td">${kenanForm.hakkenDate}</td>
                            <td class="data-td">${kenanForm.taisakuDate}</td>
                            <td class="data-td">${kenanForm.hakkenJyokyo}</td>
                            <td class="data-td">${kenanForm.buhin}</td>
                            <td class="data-td">${kenanForm.gensyo}</td>
                            <td class="data-td">${kenanForm.youin}</td>
                            <td class="data-td">${kenanForm.taisaku}</td>
                            <td class="data-td">${kenanForm.syotiNaiyou}</td>
                            <td>
                                <button onclick="editKenan(this)" class="btn btn-xs btn-warning"><i class="fa fa-edit">詳細</i></button>
                                <button onclick="deleteKenan(this)" class="btn btn-xs btn-danger"><i class="glyphicon glyphicon-trash">削除</i></button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
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
                <form id="kenanForm"  name="kenanForm" method="post">

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
                    <input type="hidden" id="kenanKikiSystemId" class="kenanForm-input" name="kikisysId" value="" />

                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            <h3 class="panel-title">懸案状況</h3>
                        </div>
                        <div class="panel-body">
                            <div class="row form-group">
                                <div class="col-md-2">対応フラグ：</div>
                                <div class="col-md-4">
                                    <select  name="taiouFlg" id="taiouFlg" class="form-control kenanForm-input">
                                        <option>未対応</option>
                                        <option>対応</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-2">発見日付：</div>
                                <div class="col-md-4">
                                    <input type="text" name="hakkenDate" id="hakkenDate" class="form-control kenanForm-input"  placeholder="例：yyyy/mm/dd" value="" />
                                </div>
                                <div class="col-md-2">対策日付：</div>
                                <div class="col-md-4">
                                    <input type="text" name="taisakuDate" id="taisakuDate" class="form-control kenanForm-input" placeholder="例：yyyy/mm/dd"  value="" />
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-2">機器の事象：</div>
                                <div class="col-md-4">
                                    <input type="text" name="hakkenJyokyo" id="hakkenJyokyo" class="form-control kenanForm-input"  value="" />
                                </div>
                                <div class="col-md-2">部品・箇所： </div>
                                <div class="col-md-4">
                                    <input type="text" name="buhin" id="buhin" class="form-control kenanForm-input"  value="" />
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-2">損傷の状況： </div>
                                <div class="col-md-4">
                                    <input type="text" name="gensyo" id="gensyo" class="form-control kenanForm-input"  value=""/>
                                </div>
                                <div class="col-md-2">損傷の要因： </div>
                                <div class="col-md-4">
                                    <input type="text" name="youin" id="youin" class="form-control kenanForm-input"  value="" />
                                </div>
                            </div>

                            <div class="row form-group">
                                <div class="col-md-2">改善対策： </div>
                                <div class="col-md-4">
                                    <input type="text" name="taisaku" id="taisaku" class="form-control kenanForm-input" />
                                </div>
                                <div class="col-md-2">処置内容： </div>
                                <div class="col-md-4">
                                    <input type="text" name="syotiNaiyou" id="syotiNaiyou" class="form-control kenanForm-input"  value=""/>
                                </div>
                            </div>

                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="updateKenan()" class="btn btn-primary">保存</button>
            </div>
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
            var koujiInfo ="工事："+ koujiFormData.kouji.kjNo + "/" + koujiFormData.kouji.kjMeisyo;
            $("#koujiInfo").html(koujiInfo);
            var kikisysInfo = "弁："+koujiFormData.valve.vNo + " ( " + koujiFormData.valve.benMeisyo + " ) " ;
            $("#kikisysInfo").html(kikisysInfo);

            $("#kenanId").val(kenanId);
            $("#kenanKoujiId").val(koujiFormData.kouji.id);
            $("#kenanKoujirelationId").val(koujiFormData.koujirelationId);
            $("#kenanKikiId").val(koujiFormData.kikiId);
            $("#kenanKikiSystemId").val(koujiFormData.kikisysId);
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
        $("#kenanModal").modal("hide");
    }

    function deleteKenan(obj) {
        var kenanTr = $(obj).parent().parent();
        var kenanId = kenanTr[0].id;
        $("#kenanId").val(kenanId);

        if (!confirm("この行を削除しますか？"))
            return;
        $.post("/valdacHost/kenan/deleteKenanByKenanId",{"kenanId":kenanId},function(data) {
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

    function updateKenan(){
        var datas = $(".kenanForm-input");
        var kenanFormArray = new Array();
        for(var i = 0;i<datas.length;i++){
            kenanFormArray[i] = datas[i].value;
        }
        var kenanFormJson = JSON.stringify(kenanFormArray);
        $.post("/valdacHost/kenan/updateKenan",{"kenanForm":kenanFormJson},function(data) {
            $("#kenanModal").modal("hide");
            location.reload();
        });
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

