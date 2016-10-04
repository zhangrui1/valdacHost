<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 12/4/14
  Time: 9:54 PM
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
            工事支援
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
                <div class="btn-group" role="group" aria-label="...">
                    <button type="button" class="btn btn-default">工事</button>
                    <button type="button" class="btn btn-default">懸案</button>
                    <button type="button" class="btn btn-default">点検履歴</button>
                </div>
            </div>
            <div class="col-md-2">
            </div>
            <div class="col-md-6">
                <input type="text" class="form-control" placeholder="keyword"/>
            </div>
        </div>

        <div class="row">
            <!-- collection -->
            <div class="col-md-12">
            </div>
        </div>

    </section><!-- /.content -->
</div><!-- ./wrapper -->

<!-- add new calendar event modal -->

<script type="text/javascript">
    $(document).ready(function(){

        $(".kouji-table tr").mouseover(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","1");
        });
        $(".kouji-table tr").mouseout(function(obj){
            var tr = $(obj.currentTarget)[0];
            $(tr).find(".operation-button").css("opacity","0");
        });

        $("#table_search").keyup(function(){
            var keyword = $("#table_search").val();
            keyword=keyword.toLowerCase();
            keyword=keyword.trim();

            if(keyword.length > 0) {
                var trs = $(".active .kouji-table tbody tr");
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
                        tmpHtml=tmpHtml.toLowerCase();
                        if (tmpHtml.match(keyword)) {
                            $(trs[i]).show();
                            break;
                        }
                    }
                }
            } else {
                var trs = $(".active .kouji-table tbody tr");
                for (var i = 0; i < trs.length; i++) {
                    $(trs[i]).show();
                }
            }
        });

    });
</script>

<c:import url="../htmlframe/footerFrame.jsp" />

</body>
</html>

