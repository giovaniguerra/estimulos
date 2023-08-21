<%-- 
    Document   : LoginEstimulos
    Created on : 16/04/2016, 21:52:46
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Estimulos - Login</title>
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
    </head>
    <body class="hold-transition skin-blue sidebar-mini">      
        <div class="wrapper">

            <header class="main-header">
                <!-- Logo -->
                <a href="#" class="logo">
                    <!-- mini logo for sidebar mini 50x50 pixels -->
                    <span class="logo-mini">EST</span>
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
                                <a href="#">
                                    <span class="hidden-xs">Entrar</span>
                                </a>                                
                            </li>
                        </ul>
                    </div>
                </nav>
            </header>            

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <h1>${msg}</h1>
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Entrar            
                    </h1>            
                </section>

                <!-- Main content -->                
                <!-- form start -->                
                <div class="box-body">
                    <form id="fLogin" role="form" method="post" action="/estimulos/login">                        
                        <!-- SOU CADASTRADO -->
                        <div class="col-md-6">
                            <div class="box box-default">
                                <div class="box-header with-border">
                                    <i class="fa fa-user"></i>
                                    <h3 class="box-title">Sou cadastrado</h3>
                                </div><!-- /.box-header -->

                                <div class="box-body">
                                    <div class="row">
                                        <div class="form-group col-md-12">
                                            <label>E-mail</label>
                                            <input type="email" class="form-control" id="txtEmail"  name="txtEmail" placeholder="E-mail" autofocus>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-md-12">
                                            <label>Senha</label>
                                            <input type="password" class="form-control" name="txtSenha" placeholder="Senha">
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-md-11">
                                            <label><a href="#">Esqueci meus dados</a></label>
                                            <button type="submit" class="btn btn-info pull-right" name="operacao" value="VISUALIZAR">Entrar</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <form role="form" method="post" action="/estimulos/terapeuta">
                        <!-- NÃO SOU CADASTRADO --> 
                        <div class="col-md-6">
                            <div class="box box-default">
                                <div class="box-header with-border">
                                    <i class="fa fa-user-plus"></i>
                                    <h3 class="box-title">Não sou cadastrado</h3>
                                </div>
                                <div class="box-body">
                                    <!-- PRIMEIRA LINHA -->
                                    <div class="row">
                                        <div class="form-group col-md-12">
                                            <label>Nome</label>
                                            <input type="text" class="form-control" name="txtNome" placeholder="Nome">                                    
                                        </div>                                     
                                    </div>                                    

                                    <!-- SEGUNDA LINHA -->
                                    <div class="row">
                                        <div class="form-group col-md-6">
                                            <label>Dt. Nascimento</label>
                                            <input type="date" name="dtNasc" class="form-control">
                                        </div>

                                    </div>
                                    <!-- TERCEIRA LINHA -->
                                    <div class="row">
                                        <div class="form-group col-md-10">
                                            <label>E-mail</label>
                                            <input type="email" class="form-control" name="txtEmail" placeholder="E-mail">
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-md-10">
                                            <label>Senha</label>                                            
                                            <input type="password" class="form-control col-md-10" name="txtSenha" placeholder="Senha">
                                        </div>
                                    </div>
                                    <!-- QUARTA LINHA -->
                                    <div class="row">
                                        <div class="form-group col-md-11">
                                            <button type="submit" class="btn btn-success pull-right" name="operacao" value="SALVAR">Cadastrar</button>
                                        </div>
                                    </div>
                                </div>
                            </div><!-- /.box-body -->
                        </div>
                    </form>
                </div>
            </div><!-- /.box-body -->
        </div><!-- /.content-wrapper -->    

        <!-- jQuery 2.1.4 -->
        <script src="../../plugins/jQuery/jQuery-2.1.4.min.js"></script>
        <!-- Bootstrap 3.3.5 -->
        <script src="../../bootstrap/js/bootstrap.min.js"></script>        
        <!-- FastClick -->
        <script src="../../plugins/fastclick/fastclick.min.js"></script>
        <!-- AdminLTE App -->
        <script src="../../dist/js/app.min.js"></script>
        <!-- AdminLTE for demo purposes -->
        <script src="../../dist/js/demo.js"></script>
    </body>
</html>
