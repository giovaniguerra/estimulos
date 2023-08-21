<%-- 
    Document   : TesteApplet
    Created on : 17/04/2016, 15:46:12
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">
        
        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>
    </jsp:attribute>
    <jsp:attribute name="footer">   
    </jsp:attribute>
    <jsp:body>
        <script>
            $(document).ready(function () {
                $("#start").click(function () {
                    document.getElementById("start").disabled = true;
                    document.getElementById("stop").disabled = false;

                    var top = document.getElementById("estimulo").style.marginTop;
                    var left = document.getElementById("estimulo").style.marginLeft;

                    for (var i = 0; i < 10; i++) {
                        $("#estimulo").animate({marginTop: 50, marginLeft: 300}, 1000);
                        $("#estimulo").animate({marginTop: 300, marginLeft: 50}, 1000);
                        $("#estimulo").animate({marginTop: 50, marginLeft: 150}, 1000);
                        $("#estimulo").animate({marginTop: 200, marginLeft: 600}, 1000);
                        $("#estimulo").animate({marginTop: top, marginLeft: left}, 500);
                    }

                });
                $("#stop").click(function () {
                    document.getElementById("start").disabled = false;
                    document.getElementById("stop").disabled = true;
                    $("#estimulo").clearQueue();
                });
            });
        </script>

        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                <i class="fa fa-video-camera"></i>
                Replay
            </h1>                   
        </section>

        <!-- Main content -->                
        <!-- form start -->                
        <div class="box-body">
            <form role="form" method="post" action="/estimulos/login">                        
                <!-- SOU CADASTRADO -->
                <div class="col-md-12">
                    <div class="box box-default">
                        <div class="box-body">
                            <div class="row">
                                <div class="form-group col-md-12" style="height: 600px">  
                                    <div class="box-header with-border">
                                        <button id="start" class="btn btn-success"> Iniciar replay</button>
                                        <button id="stop" class="btn btn-danger" disabled>Parar</button>
                                        <button id="voltar" class="btn btn-primary pull-right"> Voltar</button>
                                    </div><!-- /.box-header -->
                                    <br><br>
                                    <button class="fa fa-soccer-ball-o" id="estimulo" style="background:red;height:100px;width:100px;margin-left:0px;margin-top:0px;position: absolute"></button>
                                    <button id="final" style="background:blue;height:100px;width:100px;margin-left:600px;margin-top: 200px; position: absolute"></button>
                                </div>
                            </div>                                    
                        </div>
                    </div>
                </div>
            </form>                    
        </div>
    </jsp:body>
</t:genericpages>