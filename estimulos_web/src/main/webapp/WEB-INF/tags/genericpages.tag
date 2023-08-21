<%-- 
    Document   : genericpages
    Created on : 29/04/2016, 01:29:42
    Author     : Caio Gustavo
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<%-- any content can be specified here e.g.: --%>
<html>
    <body>
        <div id="pageheader">

            <title>Estímulos</title>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <!-- Tell the browser to be responsive to screen width -->
            <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

            <base href="${pageContext.request.contextPath}"/> 

            <!-- Bootstrap 3.3.5 -->
            <link rel="stylesheet" href="../../bootstrap/css/bootstrap.min.css">
            <!-- Font Awesome -->
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
            <!-- Ionicons -->
            <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
            <!-- Theme style -->
            <link rel="stylesheet" href="../../dist/css/AdminLTE.min.css">
            <!-- AdminLTE Skins. Choose a skin from the css/skins
                 folder instead of downloading all of them to reduce the load. -->
            <link rel="stylesheet" href="../../dist/css/skins/_all-skins.min.css">
            <jsp:invoke fragment="header"/>
        </div>
        <div id="body" class="hold-transition skin-blue sidebar-mini">        
            <div class="wrapper">
                <header class="main-header">
                    <!-- Logo -->
                    <a href="Home.jsp" class="logo">
                        <!-- logo for regular state and mobile devices -->
                        <span class="logo-lg">Estimulos</span>
                    </a>
                    <!-- Header Navbar: style can be found in header.less -->
                    <nav class="navbar navbar-static-top" role="navigation">
                        <!-- Sidebar toggle button-->
                        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>            
                        </a>           
                        <!-- LEFT -->
                        <div class="navbar-custom-menu">                                                               
                            <ul class="nav navbar-nav">                
                                <!-- Notifications: style can be found in dropdown.less -->                                          
                                <li class="dropdown user user-menu">
                                    <a href="/Login.jsp">
                                        <span class="hidden-xs">Sair</span>
                                    </a>                                
                                </li>
                            </ul>
                        </div>
                    </nav>
                </header>

                <aside class="main-sidebar">
                    <!-- sidebar: style can be found in sidebar.less -->
                    <section class="sidebar">                    
                        <!-- sidebar menu: : style can be found in sidebar.less -->
                        <ul class="sidebar-menu">
                            <li class="header">MENUS</li>                        
                            <li class="treeview">
                                <a href="#">
                                    <i class="fa fa-gamepad"></i><span class="h4">Jogo</span>
                                    <i class="fa fa-angle-left pull-right"></i>
                                </a>
                                <ul class="treeview-menu">
                                    <li><a href="/pages/Novo_Jogo.jsp"><i class="fa fa-plus"></i> Novo</a></li>
                                    <li><a href="/pages/Editar_Jogo.jsp"><i class="fa fa-edit"></i> Editar jogo</a></li>
<!--                                    <li><a href="/pages/Vincular_Estimulo.jsp"><i class="fa fa-chain"></i> Vincular estímulos</a></li>                                
                                    <li><a href="/pages/Configurar_Fase.jsp"><i class="fa fa-cog"></i> Configurar níveis</a></li>-->
                                </ul>
                            </li>
                            <li class="treeview">
                                <a href="#">
                                    <i class="fa fa-soccer-ball-o"></i><span class="h4">Estimulos</span>
                                    <i class="fa fa-angle-left pull-right"></i>
                                </a>
                                <ul class="treeview-menu">
                                    <li><a href="/pages/Novo_Estimulo.jsp"><i class="fa fa-plus"></i> Novo</a></li>
                                    <li><a href="/pages/Consultar_Estimulo.jsp"><i class="fa fa-search"></i> Consultar</a></li>                                
                                </ul>
                            </li>
                            <li class="treeview">
                                <a href="#">
                                    <i class="fa fa-volume-up"></i><span class="h4">Reforçadores</span>
                                    <i class="fa fa-angle-left pull-right"></i>
                                </a>
                                <ul class="treeview-menu">
                                    <li><a href="/pages/Novo_Reforcador.jsp"><i class="fa fa-plus"></i> Novo</a></li>
                                    <li><a href="/pages/Consultar_Reforcador.jsp"><i class="fa fa-search"></i> Consultar</a></li>                                
                                </ul>
                            </li>
                            <li class="treeview">
                                <a href="#">
                                    <i class="fa fa-navicon"></i> <span class="h4">Categorias</span>
                                    <i class="fa fa-angle-left pull-right"></i>
                                </a>
                                <ul class="treeview-menu">
                                    <li><a href="/pages/Categoria.jsp"><i class="fa fa-plus"></i> Nova categoria</a></li>
                                    <li><a href="/pages/Editar_Categoria.jsp"><i class="fa fa-search"></i> Consultar categoria</a></li>                                
                                </ul>                                                                                   
                            </li>
<!--                            <li>
                                <a href="/pages/Replay.jsp">
                                    <i class="fa fa-video-camera"></i> <span class="h4">Replays</span>
                                </a>                                                    
                            </li>-->
                            <li>
                                <a href="#">
                                    <i class="fa fa-line-chart"></i> <span class="h4">Estatísticas</span>
                                    <i class="fa fa-angle-left pull-right"></i>
                                </a>  
                                <ul class="treeview-menu">
                                    <li><a href="/pages/Grafico_Resultado.jsp"><i class="fa fa-line-chart"></i> Gráfico de resultados</a></li>
                                    <li><a href="/pages/Estatistica_Replay.jsp"><i class="fa fa-video-camera"></i> Replay</a></li>
                                </ul>
                            </li>
                            <li>
                                <a href="/pages/Configuracao.jsp">
                                    <i class="fa fa-cog"></i> <span class="h4">Editar dados</span>
                                </a>                                                    
                            </li>
                        </ul>
                    </section>
                    <!-- /.sidebar -->
                </aside>

                <!-- Content Wrapper. Contains page content -->
                <div class="content-wrapper">
                    <!-- Main content -->                
                    <!-- form start -->                
                    <div class="box-body">
                        <jsp:doBody/>
                    </div><!-- /.box-body -->
                </div><!-- /.content-wrapper -->    

                <!-- jQuery 2.1.4 -->
                
                <!-- Bootstrap 3.3.5 -->
                <script src="../../bootstrap/js/bootstrap.min.js"></script>        
                <!-- FastClick -->
                <script src="../../plugins/fastclick/fastclick.min.js"></script>
                <!-- AdminLTE App -->
                <script src="../../dist/js/app.min.js"></script>
                <!-- AdminLTE for demo purposes -->
                <script src="../../dist/js/demo.js"></script>      
            </div>
        </div>
        <div id="pagefooter">
            <jsp:invoke fragment="footer"/>
        </div>
    </body>
</html>
