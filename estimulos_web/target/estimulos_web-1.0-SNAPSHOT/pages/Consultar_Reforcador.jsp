<%-- 
    Document   : Consultar_Reforcador
    Created on : 22/04/2016, 14:56:32
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">        
        
        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>

        <script>
            var reforcadores;
            var entidades;
            // TODO: BUG do vincular. BUG do redimensionar.
            // tempo do reforçador.
            $.ajax({
                url: "/estimulos/reforcador",
                type: "POST",
                data: {
                    operacao: "CONSULTAR",
                    acao: "BUSCAR"
                },
                success: function (data) {
                    reforcadores = JSON.parse(data);
                    for (var i = 0; i < reforcadores.length; i++) {                        
                        $("#tabela_estimulo").append("<tr>" +
                                "<td>" + (i + 1) + "</td>" +
                                "<td>" + reforcadores[i].nome + "</td>" +
                                "<td>" +
                                "<button type='button' class='btn btn-primary'>Exibir</button>" +
                                "<button type='button' class='btn btn-warning'>Alterar</button>" +
                                "<button type='button' class='btn btn-danger'>Excluir</button>" +
                                "</td>" +
                                "</tr>");
                    }
                },
                error: function (err) {
                    console.log(err.statusText);
                    alert("ERRO AO CARREGAR AS IMAGENS " + err.statusText);
                }
            });
        </script>
    </jsp:attribute>
    <jsp:attribute name="footer">   
    </jsp:attribute>
    <jsp:body>

        <!-- Main content -->                
        <!-- form start -->                
        <div class="box-body">
            <div class="box">
                <div class="box-header">
                    <i class="fa fa-search"></i>
                    <h3 class="box-title"><b>Consultar reforçador</b></h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body no-padding">
                    <table class="table table-striped">
                        <tbody id="tabela_estimulo">
                            <tr>
                                <th>ID</th>
                                <th>NOME</th>                                
                                <th>OPÇÕES</th>
                            </tr>   
                        </tbody></table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </div>
    </jsp:body>
</t:genericpages>
