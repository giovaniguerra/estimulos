<%-- 
    Document   : Novo_Jogo
    Created on : 22/04/2016, 16:12:14
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">
        
        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>
        <script>
                        
            function gravar() {
                $.ajax({
                    url: "/estimulos/jogo",
                    type: "POST",
                    data: {
                        operacao: "SALVAR",
                        acao: "JOGO",
                        nome: document.getElementById("txtNomeJogo").value,
                        nome_paciente: document.getElementById("txtNomePaciente").value,
                        dt_nascimento: document.getElementById("dtNasc").value
                    },
                    success: function (data) {
                        var jogo = JSON.parse(data);
                        window.location = 'Vincular_Estimulo.jsp?id=' + jogo.ID;
                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("ERRO AO GRAVAR O JOGO! " + err.statusText);
                    }
                });
            }
        </script>
    </jsp:attribute>
    <jsp:attribute name="footer">   
    </jsp:attribute>
    <jsp:body>

        <!-- Main content -->                
        <!-- form start -->                
        <div class="box-body">

            <!-- SOU CADASTRADO -->
            <div class="col-md-12">
                <div class="box box-default">
                    <div class="box-header with-border">
                        <i class="fa fa-gamepad"></i>
                        <h3 class="box-title">Novo Jogo</h3>
                    </div><!-- /.box-header -->
                    <br/>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box-header with-border">
                                    <i class="fa fa-soccer-ball-o"></i>
                                    <h1 class="box-title">Nome do Jogo</h1>                                                            
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">                                                
                                            <label>Nome do Jogo</label>
                                            <input type="text" class="form-control"  id="txtNomeJogo" placeholder="Nome do jogo">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <br/>

                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box-header with-border">
                                    <i class="fa fa-user"></i>
                                    <h1 class="box-title">Nome do paciente</h1>
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">                                                
                                            <label>Nome do Paciente</label>
                                            <input type="text" class="form-control"  id="txtNomePaciente" placeholder="Nome do paciente">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box-header with-border">
                                    <i class="fa fa-calendar"></i>
                                    <h1 class="box-title">Data de Nascimento</h1>                                                            
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">
                                            <label>Data de Nascimento</label>
                                            <div class="input-group">
                                                <div class="input-group-addon">
                                                    <i class="fa fa-calendar"></i>
                                                </div>
                                                <input class="form-control pull-right active" id="dtNasc" type="date">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br/>

                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box-header with-border">
                                    <i class="fa fa-paperclip"></i>
                                    <h1 class="box-title">Importar</h1>                                                            
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">
                                            <label>Importar imagem do jogo</label>
                                            <input type="file" id="arquivo">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <br/>

                        <div class="row col-md-12">
                            <button type="button" class="btn btn-primary pull-right btn-lg" onclick="gravar()">Próximo</button>
                        </div>

                    </div>
                </div>
            </div>
        </div><!-- /.box-body -->

    </jsp:body>
</t:genericpages>
