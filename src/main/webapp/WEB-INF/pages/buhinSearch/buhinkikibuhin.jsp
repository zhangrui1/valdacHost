<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp" />

<body class="skin-black">
<!-- header logo: style can be found in header.less -->

<c:import url="../htmlframe/headerFrame.jsp" />
<style type="text/css">
    <!--
    .src {
        /*overflow: scroll;   *//* スクロール表示 */
        overflow-x: auto;
        overflow-y: auto;
        width: 1168px;
        height: 500px;
    }

</style>

<div class="container">

<!-- Content Header (Page header) -->
<section class="content-header">
    <h1>
        <ol class="breadcrumb">
            <li class="active"><a href="/valdacHost/item/${valve.kikiSysId}/${kikiOrBenFlg}/valve"><i class="fa fa-dashboard"></i>弁情報</a></li>
            <li>機器情報</li>
        </ol>
    </h1>
</section>

<!-- Main content -->
<section class="content">

<div class="row">
<div class="col-md-12">
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">機器情報</h3>
    </div>
    <div class="panel-body">
        <div class="form-group">
            <div class="row">
                <div class="col-md-1">
                    機器分類:
                </div>
                <div class="col-md-2">
                    <input type="text" name="kikiBunrui" class="form-control" readonly="readonly" value="${kiki.kikiBunrui}" />
                </div>
                <div class="col-md-1">
                    機器番号
                </div>
                <div class="col-md-2">
                    <input type="text" name="kikiNo" class="form-control" readonly="readonly"  value="${kiki.kikiNo}" />
                </div>
                <div class="col-md-1">
                    機器名称:
                </div>
                <div class="col-md-4">
                    <input type="text" name="kikiMei" class="form-control kikiMei" readonly="readonly" value="${kiki.kikiMei}" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-md-1">
                    主管係:
                </div>
                <div class="col-md-3">
                    <input type="text" name="syukan" class="form-control syukan" readonly="readonly" value="${kiki.syukan}" />
                </div>
                <div class="col-md-1">
                    メーカー:
                </div>
                <div class="col-md-1">
                    <input type="text" name="makerRyaku" class="form-control maker" readonly="readonly" placeholder="略称" value="${kiki.makerRyaku}" />
                </div>
                <div class="col-md-5">
                    <input type="text" name="maker" class="form-control maker" readonly="readonly" value="${kiki.maker}" />
                </div>

            </div>
        </div>
        <div class="form-group">
            <div class="row">

                <div class="col-md-1">
                    型式番号
                </div>
                <div class="col-md-2">
                    <input type="text" name="katasikiNo" class="form-control" readonly="readonly" value="${kiki.katasikiNo}" />
                </div>
                <div class="col-md-1">
                    シリアル番号:
                </div>
                <div class="col-md-2">
                    <input type="text" name="serialNo" class="form-control" readonly="readonly"  value="${kiki.serialNo}" />
                </div>
                <div class="col-md-1">
                    オーダー番号:
                </div>
                <div class="col-md-4">
                    <input type="text" name="orderNo" class="form-control" readonly="readonly" value="${kiki.orderNo}" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="row">
                <div class="col-md-1">
                    備考
                </div>
                <div class="col-md-10">
                    <input type="text" name="bikou"class="form-control" readonly="readonly" value="${kiki.bikou}" />
                </div>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <!-- collection -->
    <div class="col-md-12">
        <div class="box  box-kiki">
            <div class="box-header box-panel">
                <h3 class="box-title">部品一覧</h3>
            </div>
            <div class="box-body">
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover kiki-table table-bordered">
                        <tbody><tr>
                            <th nowrap>部品名</th>
                            <th nowrap>部品区分</th>
                            <th nowrap>アスベスト区分</th>
                            <th nowrap>使用箇所</th>
                            <th nowrap>標準仕様</th>
                            <th nowrap>資材名</th>
                            <th nowrap>メーカー(略)</th>
                            <th nowrap>品番</th>
                            <th nowrap>概略寸法</th>
                            <th nowrap>数量</th>
                            <th nowrap>備考</th>
                            <th nowrap>確定</th>
                            <th nowrap>更新日付</th>
                        </tr>
                        <c:forEach items="${buhinList}" var="tmpbuhin">
                            <tr id="${tmpbuhin.buhinId}">
                                <td nowrap>${tmpbuhin.buhinMei}</td>
                                <td nowrap>${tmpbuhin.buhinKbn}</td>
                                <td nowrap>${tmpbuhin.asbKbn}</td>
                                <td nowrap>${tmpbuhin.siyouKasyo}</td>
                                <td nowrap>${tmpbuhin.hyojunSiyou}</td>
                                <td nowrap>${tmpbuhin.sizaiName}</td>
                                <td nowrap>${tmpbuhin.maker}(${tmpbuhin.makerRyaku})</td>
                                <td nowrap>${tmpbuhin.hinban}</td>
                                <td nowrap>${tmpbuhin.sunpou}</td>
                                <td nowrap>${tmpbuhin.suryo}</td>
                                <td nowrap>${tmpbuhin.bikou}</td>
                                <td nowrap>${tmpbuhin.buhinStatus}</td>
                                <td nowrap>${tmpbuhin.updDate}</td>
                                <%--<td>--%>
                                    <%--<a class="btn btn-warning btn-sm operation-button-btn" onclick="buhinDetail(this)"><i class="fa fa-arrow-right"></i></a>--%>
                                <%--</td>--%>
                            </tr>

                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="buhin-modal" class="modal fade content-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
    <form id="BuhinForm" name="BuhinForm" method="post">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">部品詳細</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-2">
                                部品区分
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="buhinKbn" id="buhinKbn" class="form-control buhinForm-input" readonly="readonly" value="${buhin.buhinKbn}" />
                            </div>
                            <div class="col-md-2">
                                アスベスト区分
                            </div>
                            <div class="col-md-4">
                                <input type="text" name="asbKbn" id="asbKbn" class="form-control buhinForm-input" readonly="readonly" value="${buhin.asbKbn}" />
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-2">
                                使用箇所
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="siyouKasyo" id="siyouKasyo" class="form-control buhinForm-input" readonly="readonly" value="${buhin.siyouKasyo}"/>
                            </div>
                            <div class="col-md-2">
                                部品名
                            </div>
                            <div class="col-md-4">
                                <input type="text" name="buhinMei"  id="buhinMei" class="form-control buhinMei buhinForm-input" readonly="readonly" value="${buhin.buhinMei}"/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-2">
                                標準仕様
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="hyojunSiyou" id="hyojunSiyou" class="form-control hyojunsiyou buhinForm-input" readonly="readonly" value="${buhin.hyojunSiyou}"/>
                            </div>
                            <div class="col-md-2">
                                概略寸法
                            </div>
                            <div class="col-md-4">
                                <input type="text" name="sunpou" id="sunpou" class="form-control buhinForm-input" readonly="readonly" value="${buhin.sunpou}"/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-2">
                                数量
                            </div>
                            <div class="col-md-1">
                                <input type="text" name="suryo" id="suryo" class="form-control buhinForm-input" readonly="readonly" value="${buhin.suryo}"/>
                            </div>
                            <div class="col-md-1">
                                確定
                            </div>
                            <div class="col-md-2">
                                <input type="text" name="buhinStatus" id="buhinStatus"  class="form-control buhinForm-input" readonly="readonly" value="${buhin.buhinStatus}"/>
                            </div>
                            <div class="col-md-1">
                                資材名
                            </div>
                            <div class="col-md-4">
                                <input type="text" name="sizaiName" id="sizaiName" class="form-control sizainame buhinForm-input" readonly="readonly" value="${buhin.sizaiName}"/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-2">
                                メーカー
                            </div>
                            <div class="col-md-2">
                                <input type="text" name="makerRyaku"  id="makerRyaku" class="form-control makerbuhin buhinForm-input" readonly="readonly"  placeholder="略称" value="${buhin.makerRyaku}"/>
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="maker" id="maker" class="form-control makerbuhin buhinForm-input" readonly="readonly" value="${buhin.maker}"/>
                            </div>
                            <div class="col-md-1">
                                品番
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="hinban" id="hinban" class="form-control buhinForm-input" readonly="readonly"  value="${buhin.hinban}"/>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="row">

                            <div class="col-md-2">
                                備考
                            </div>
                            <div class="col-md-9">
                                <input type="text" name="bikou"  id="bikou" class="form-control buhinForm-input" readonly="readonly" value="${buhin.bikous}"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>


</div>



</div>

</section><!-- /.content -->
</aside><!-- /.right-side -->
</div><!-- ./wrapper -->

<script type="text/javascript">
    $(document).ready(function(){

        //ユーザ権限
        var userKengen=${user.kengen};
        if(userKengen=="6"){
            $(".kengen-operation").show();
        }else{
            $(".kengen-operation").hide();
        }

        $(".box-panel").click(function(){
            $(this).next().toggle();
        });

        $(".kiki-table tr").mouseover(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","1");
        });
        $(".kiki-table tr").mouseout(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","0");
        });

    });

    function buhinDetail(obj){
        var buhinTr = $(obj).parent().parent();
        var buhinId = buhinTr[0].id;

        $.get("/valdacHost/item/getBuhin",{"buhinId":buhinId},function(data){
            var buhinData = JSON.parse(data);
            $(".buhinForm-input").val("");
            $("#buhinKbn").val(buhinData.buhinKbn);
            $("#asbKbn").val(buhinData.asbKbn);
            $("#siyouKasyo").val(buhinData.siyouKasyo);
            $("#buhinMei").val(buhinData.buhinMei);
            $("#hyojunSiyou").val(buhinData.hyojunSiyou);
            $("#sunpou").val(buhinData.sunpou);
            $("#suryo").val(buhinData.suryo);
            $("#buhinStatus").val(buhinData.buhinStatus);
            $("#makerRyaku").val(buhinData.makerRyaku);
            $("#maker").val(buhinData.maker);
            $("#sizaiName").val(buhinData.sizaiName);
            $("#hinban").val(buhinData.hinban);
            $("#bikou").val(buhinData.bikou);

            $("#buhin-modal").modal();
        })
    }

</script>

<style type="text/css">

    .master-li:hover{
        cursor:pointer;
        background-color: #eee;
    }
</style>

</body>
</html>