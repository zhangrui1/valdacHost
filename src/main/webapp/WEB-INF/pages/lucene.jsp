<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 11/14/14
  Time: 2:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="htmlframe/headFrame.jsp" />
<html>
<body class="skin-black">
<!-- header logo: style can be found in header.less -->
<c:import url="htmlframe/headerFrame.jsp"/>
<div class="container">
    <!-- Left side column. contains the logo and sidebar -->

        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Lucene作成
            </h1>
        </section>
        <hr/>
        <!-- Main content -->
        <section class="content">

            <div class="row">
                <!-- collection -->
                <div class="col-md-12">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-4">
                                <form action="/valdacHost/lucene/luceneValve">
                                    <input type="submit" class="btn btn-default" value="弁作成"/>
                                </form>
                            </div>
                            <div class="col-md-4">
                                <form action="/valdacHost/lucene/luceneKiki">
                                    <input type="submit" class="btn btn-default" value="機器作成"/>
                                </form>
                            </div>
                            <div class="col-md-4">
                                <form action="/valdacHost/lucene/luceneBuhin">
                                    <input type="submit" class="btn btn-default" value="部品作成"/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <!-- collection -->
                <div class="col-md-12">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-4">
                                <form action="/valdacHost/lucene/luceneKouji">
                                    <input type="submit" class="btn btn-default" value="工事作成"/>
                                </form>
                            </div>
                            <div class="col-md-4">
                                <form action="/valdacHost/lucene/luceneKoujirelation">
                                    <input type="submit" class="btn btn-default" value="工事関係表作成"/>
                                </form>
                            </div>
                            <div class="col-md-4">
                                <form action="/valdacHost/lucene/luceneTenkenRireki">
                                    <input type="submit" class="btn btn-default" value="点検履歴作成"/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <!-- collection -->
                <div class="col-md-12">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-4">
                                <form action="/valdacHost/lucene/luceneKoujiIDTenkenRireki">
                                    <input type="submit" class="btn btn-default" value="工事ID点検履歴作成"/>
                                </form>
                            </div>
                            <div class="col-md-4">
                                <form action="/valdacHost/lucene/luceneKenan">
                                    <input type="submit" class="btn btn-default" value="懸案作成"/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section><!-- /.content -->
</div><!-- ./wrapper -->

<!-- add new calendar event modal -->

<script type="text/javascript">


</script>

<c:import url="htmlframe/footerFrame.jsp" />

</body>
</html>
