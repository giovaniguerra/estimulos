<%-- 
    Document   : Novo_Reforcador
    Created on : 22/04/2016, 14:13:15
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

        <!-- Main content -->                
        <!-- form start -->                
        <div class="box-body">

            <!-- SOU CADASTRADO -->
            <div class="col-md-12">
                <div class="box box-default">
                    <div class="box-header with-border">
                        <i class="fa fa-plus"></i>
                        <h3 class="box-title">Novo reforçador</h3>
                    </div><!-- /.box-header -->
                    <br/>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box-header with-border">
                                    <i class="fa fa-text-width"></i>
                                    <h1 class="box-title">Nome do reforçador</h1>                                                            
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">                                                
                                            <label>Nome do reforçador</label>
                                            <input type="text" class="form-control"  name="txtNomeReforcador" placeholder="Nome do reforçador">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <br/>

                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box-header with-border">
                                    <i class="fa fa-clock-o"></i>
                                    <h1 class="box-title">Tempo de duração</h1>                                                            
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">
                                            <label>Tempo de duração</label>
                                            <input type="range" min="1" max="60" value="1" step="1" onchange="showValue(this.value)"/>
                                            <span id="range">1s</span>
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
                                            <label>Importar reforçador</label>
                                            <input type="file" id="exampleInputFile">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br/>

                        <div class="row col-md-12">
                            <button type="button" class="btn btn-success pull-right btn-lg">Salvar</button>
                            <button type="button" class="btn btn-primary pull-right btn-lg">Executar</button>                                    
                        </div>

                    </div>
                </div>
            </div>
        </div><!-- /.box-body -->

    </jsp:body>
</t:genericpages>
