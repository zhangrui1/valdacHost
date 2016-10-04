<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../htmlframe/headFrame.jsp" />

<body class="skin-black">
<!-- header logo: style can be found in header.less -->

<c:import url="../htmlframe/headerFrame.jsp" />
<div class="container">
<!-- Content Header (Page header) -->
<section class="content-header">
</section>

<!-- Main content -->
<section class="content">

<div class="row">
    <!-- collection -->
    <div class="col-md-6">
        <div class="btn-group" role="group">
            <a href="/valdacHost/item/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default bg-yellow active">弁基本情報</a>
            <a href="/valdacHost/kenan/kenan/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">懸案</a>
            <a href="/valdacHost/tenken/tenken/${valve.kikiSysId}/${kikiOrBenFlg}/valve" class="btn btn-default">点検履歴</a>
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
                        <div class="col-md-1">
                            弁番号:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="VNo" name="VNo" class="form-control" readonly="readonly" value="${valve.vNo}">
                        </div>
                        <div class="col-md-1">識番:</div>
                        <div class="col-md-1">
                            <input type="text" id="VNoSub" name="VNoSub" class="form-control" readonly="readonly" value="${valve.vNoSub}">
                        </div>
                        <div class="col-md-1">
                            弁名称:
                        </div>
                        <div class="col-md-5">
                            <input type="text" id="BenMeisyo" name="BenMeisyo" class="form-control" readonly="readonly" value="${valve.benMeisyo}">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            設置設備:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="SetSetubi" name="SetSetubi" class="form-control"  readonly="readonly" value="${valve.setSetubi}">
                        </div>
                        <div class="col-md-1">
                            設置機器:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="SetKiki" name="SetKiki" class="form-control" readonly="readonly"  value="${valve.setKiki}">
                        </div>
                        <div class="col-md-1">
                            設置場所:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="SetBasyo" name="SetBasyo" class="form-control"  readonly="readonly" value="${valve.setBasyo}">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            系統:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Keitou" name="Keitou" class="form-control"  readonly="readonly" value="${valve.keitou}">
                        </div>
                        <div class="col-md-1">
                            工具メガネ:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Kougu1M" name="Kougu1M" class="form-control"  readonly="readonly" value="${valve.kougu1M}">
                        </div>
                        <div class="col-md-1">
                            工具スパナ:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Kougu2S" name="Kougu2S" class="form-control" readonly="readonly"  value="${valve.kougu2S}">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            工具定盤:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Kougu3T" name="Kougu3T" class="form-control" readonly="readonly" value="${valve.kougu3T}">
                        </div>
                        <div class="col-md-1">
                            工具六角レンチ:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Kougu4L" name="Kougu4L" class="form-control"  readonly="readonly" value="${valve.kougu4L}">
                        </div>
                        <div class="col-md-1">
                            工具その他:
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Kougu5O" name="Kougu5O" class="form-control" readonly="readonly"  value="${valve.kougu5O}">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            ICS番号
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Ics" name="Ics" class="form-control ics" readonly="readonly" value="${valve.ics}">
                        </div>
                        <div class="col-md-1">
                            設置プラント
                        </div>
                        <div class="col-md-7">
                            <input type="text" id="locationName" name="locationName" class="form-control" readonly="readonly"  value="${valve.locationName}">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            圧力:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="AturyokuMax" name="AturyokuMax" class="form-control"  readonly="readonly" value="${valve.aturyokuMax}">
                        </div>
                        <div class="col-md-1">
                            単位:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="Tani" name="Tani" class="form-control"  readonly="readonly" value="${valve.tani}">
                        </div>
                        <div class="col-md-1">
                            温度:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="OndoMax" name="OndoMax" class="form-control"  readonly="readonly" value="${valve.ondoMax}" >
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            流体:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="RyutaiRyaku" name="RyutaiRyaku" class="form-control ryutai" readonly="readonly"  value="${valve.ryutaiRyaku}">
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Ryutai" name="Ryutai" class="form-control ryutai" readonly="readonly" value="${valve.ryutai}">
                        </div>
                        <div class="col-md-1">
                            形式:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="KeisikiRyaku" name="KeisikiRyaku" class="form-control keisiki" readonly="readonly" value="${valve.keisikiRyaku}">
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Keisiki" name="Keisiki" class="form-control keisiki" placeholder="" readonly="readonly" value="${valve.keisiki}">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            駆動方式:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="SousaRyaku" name="SousaRyaku" class="form-control sousa" readonly="readonly" placeholder="略称" value="${valve.sousaRyaku}">
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Sousa" name="Sousa" class="form-control sousa" readonly="readonly"  value="${valve.sousa}">
                        </div>
                        <div class="col-md-1">
                            クラス:
                        </div>
                        <div class="col-md-2" style="display:none">
                            <input type="text" id="ClassRyaku" name="ClassRyaku" class="form-control classtype" readonly="readonly" placeholder="" value="${valve.classRyaku}">
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="ClassType" name="ClassType" class="form-control classtype" readonly="readonly" placeholder="" value="${valve.classType}">
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            呼び径:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="YobikeiRyaku" name="YobikeiRyaku" class="form-control yobikei" readonly="readonly" placeholder="略称" value="${valve.yobikeiRyaku}">
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Yobikei" name="Yobikei" class="form-control yobikei" readonly="readonly" value="${valve.yobikei}">
                        </div>
                        <div class="col-md-1">
                            接続入口:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="SzHouRyaku" name="SzHouRyaku" class="form-control szhou" readonly="readonly" placeholder="" value="${valve.szHouRyaku}">
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="SzHou" name="SzHou" class="form-control szhou" readonly="readonly" placeholder=""value="${valve.szHou}">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            接続規格:
                        </div>
                        <div class="col-md-5">
                            <input type="text" id="SzKikaku" name="SzKikaku" class="form-control szkikaku" readonly="readonly" value="${valve.szKikaku}">
                        </div>
                        <div class="col-md-1">
                            本体材質:
                        </div>
                        <div class="col-md-2">
                            <input type="text" id="ZaisituRyaku" name="ZaisituRyaku" class="form-control Zaisitu" readonly="readonly" placeholder="略称" value="${valve.zaisituRyaku}">
                        </div>
                        <div class="col-md-3">
                            <input type="text" id="Zaisitu" name="Zaisitu" class="form-control Zaisitu" placeholder="" readonly="readonly" value="${valve.zaisitu}">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-md-1">
                            付帯情報:
                        </div>
                        <div class="col-md-11">
                            <input type="text" id="Futai" name="Futai" class="form-control" readonly="readonly" value="${valve.futai}">
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="row">
    <!-- collection -->
    <div class="col-lg-12">
        <div class="box  box-kiki">
            <div class="box-header box-panel">
                <h3 class="box-title">機器一覧</h3>
            </div>
            <div class="box-body">
                <div class="box-body table-responsive no-padding">
                    <table class="table table-hover kiki-table">
                        <tbody><tr>
                            <th>機器分類</th>
                            <th>機器名称</th>
                            <th>主管係</th>
                            <th>メーカ名</th>
                            <th>型式番号</th>
                            <th>シリアル</th>
                            <th>オーダー</th>
                            <th>更新日付</th>
                            <th>操作</th>
                        </tr>
                        <c:forEach items="${kikiList}" var="tmpkiki">
                            <tr>
                                <td>${tmpkiki.kikiBunrui}</td>
                                <td>${tmpkiki.kikiMei}</td>
                                <td>${tmpkiki.syukan}</td>
                                <td>${tmpkiki.maker}(${tmpkiki.makerRyaku})</td>
                                <td>${tmpkiki.katasikiNo}</td>
                                <td>${tmpkiki.serialNo}</td>
                                <td>${tmpkiki.orderNo}</td>
                                <td>${tmpkiki.updDate}</td>
                                <td>
                                    <div class="operation-button">
                                        <a class="btn btn-info btn-sm operation-button-btn" href="/valdacHost/item/${valve.kikiSysId}/${tmpkiki.kikiId}/${kikiOrBenFlg}/valve"><i class="fa fa-arrow-right"></i></a>
                                    </div>
                                </td>
                            </tr>

                        </c:forEach>
                        </tbody></table>
                </div>
            </div>
        </div>


    </div>
</div><!-- insert -->
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

</script>

<style type="text/css">

    .master-li:hover{
        cursor:pointer;
        background-color: #eee;
    }
</style>

<script type="text/javascript">
    //valveの必須項目チェック
    function checkValve(){
        var flag=0;
        //必須項目設定
        if(document.valveForm.VNo.value==""){flag=1;}
        if(document.valveForm.BenMeisyo.value==""){flag=1;}
        if(document.valveForm.locationName.value==""){flag=1;}

        if(flag){
            window.alert("「弁番号,弁名称,設置プラント」の三つを全部入力ください");
            return false;
        }else{
            return true;
        }
    }
</script>

</body>
</html>