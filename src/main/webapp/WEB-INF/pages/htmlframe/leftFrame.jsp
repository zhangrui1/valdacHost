<%--
  Created by IntelliJ IDEA.
  User: Lsr
  Date: 10/20/14
  Time: 2:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <aside class="left-side sidebar-offcanvas">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
            <!-- Sidebar user panel -->
            <div class="user-panel">
                <div class="pull-left image">
                    <img id="leftProfile" src="${imageRoot}${user.profile}" class="img-circle" alt="User Image" />
                </div>
                <div class="pull-left info">
                    <p>お疲れ様, ${user.username}</p>

                    <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
                </div>
            </div>
            <!-- search form -->
            <form action="/valdacHost/search" method="post" class="sidebar-form">
                <div class="input-group">
                    <input type="text" name="keyword" class="form-control" placeholder="Search..."/>
                            <span class="input-group-btn">
                                <button type='submit' name='seach' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button>
                            </span>
                </div>
            </form>
            <!-- /.search form -->
            <!-- sidebar menu: : style can be found in sidebar.less -->
            <ul class="sidebar-menu">
                <li id="left-menu-new">
                    <a href="#">
                        <i class="fa fa-paste"></i> <span>新規工事</span>
                    </a>
                </li>
                <li id="left-menu-edit">
                    <a href="#">
                        <i class="fa fa-edit"></i> <span>工事一覧</span>
                    </a>
                </li>
            </ul>
        </section>
        <!-- /.sidebar -->
    </aside>