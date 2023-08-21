<%-- 
    Document   : Editar_Jogo
    Created on : 25/05/2016, 19:17:53
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
                        <i class="fa fa-search"></i>
                        <h3 class="box-title"><b>Editar jogo</b></h3>
                    </div><!-- /.box-header -->
                    <br/>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-xs-12">                                        
                                <!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <table class="table table-hover">
                                        <tbody>
                                            <tr>
                                                <th>NOME DO JOGO</th>
                                                <th>NOME DO PACIENTE</th>
                                                <th>IMAGEM</th>
                                                <th>OPÇÕES</th>
                                            </tr>
                                            <tr>
                                                <td>Bola</td>
                                                <td>João</td>
                                                <td>
                                                    <img src="../images/bola.png" style="width: 50px;height: 50px"/>
                                                </td>
                                                <td>
                                                    <button type="button" class="btn btn-warning">Vincular estímulos</button>
                                                    <button type="button" class="btn btn-success">Vincular reforçadores</button>
                                                    <button type="button" class="btn btn-primary">Configurar níveis</button>
                                                    <button type="button" class="btn btn-danger">Excluir</button>                                                            
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Frutas</td>
                                                <td>Maria</td>
                                                <td>
                                                    <img src="../images/frutas.jpg" style="width: 50px;height: 50px"/>
                                                </td>
                                                <td>
                                                    <button type="button" class="btn btn-warning">Vincular estímulos</button>
                                                    <button type="button" class="btn btn-success">Vincular reforçadores</button>
                                                    <button type="button" class="btn btn-primary">Configurar níveis</button>
                                                    <button type="button" class="btn btn-danger">Excluir</button>                                                            
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Carro</td>
                                                <td>Carlos</td>
                                                <td>
                                                    <img src="../images/carro.jpg" style="width: 50px;height: 50px"/>
                                                </td>
                                                <td>
                                                    <button type="button" class="btn btn-warning">Vincular estímulos</button>
                                                    <button type="button" class="btn btn-success">Vincular reforçadores</button>
                                                    <button type="button" class="btn btn-primary">Configurar níveis</button>
                                                    <button type="button" class="btn btn-danger">Excluir</button>                                                            
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Moveis</td>
                                                <td>Leticia</td>
                                                <td>
                                                    <img src="../images/mesa.JPG" style="width: 50px;height: 50px"/>
                                                </td>
                                                <td>
                                                    <button type="button" class="btn btn-warning">Vincular estímulos</button>
                                                    <button type="button" class="btn btn-success">Vincular reforçadores</button>
                                                    <button type="button" class="btn btn-primary">Configurar níveis</button>
                                                    <button type="button" class="btn btn-danger">Excluir</button>                                                            
                                                </td>
                                            </tr>
                                        </tbody></table>
                                </div>
                                <!-- /.box-body -->

                                <!-- /.box -->
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div><!-- /.box-body -->

    </jsp:body>
</t:genericpages>
