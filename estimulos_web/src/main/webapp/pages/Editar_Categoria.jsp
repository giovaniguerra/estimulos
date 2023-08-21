<%-- 
    Document   : Editar_Categoria
    Created on : 25/05/2016, 20:18:33
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">
        
        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>

        <script>
            var categorias;
            $.ajax({
                url: "/estimulos/categoria",
                type: "POST",
                data: {
                    operacao: "CONSULTAR",
                    acao: "BUSCAR"
                },
                success: function (data) {
                    categorias = JSON.parse(data);
                    for (var i = 0; i < categorias.length; i++) {
                        $("#tabela_categoria").append("<tr>" +
                                "<td class='n_int'>" + categorias[i].ID + "</td>" +
                                "<td class='txt_nome'>" + categorias[i].nome + "</td>" +
                                "<td>" +
                                "<button type='button' class='btn btn-warning' onclick='alterar(this)'>Alterar</button>" +
                                "<button type='button' class='btn btn-danger' onclick='excluir(this)'>Excluir</button>" +
                                "</td>" +
                                "</tr>");
                    }
                },
                error: function (err) {
                    console.log(err.statusText);
                    alert("ERRO AO CARREGAR AS CATEGORIAS: " + err.statusText);
                }
            });

            function alterar(linha) {
                var $row = $(linha).closest("tr");    // Find the row
                var $text = $row.find(".txt_nome").text(); // Find the text
                var $inteiro = $row.find(".n_int").text(); // Find the text

                $.ajax({
                    url: "/estimulos/categoria",
                    type: "POST",
                    data: {
                        operacao: "VISUALIZAR",
                        acao: "VISUALIZAR",
                        id: $inteiro
                    },
                    success: function (data) {
                        var c = JSON.parse(data);
                        var name = c.nome;
                        var id = c.ID;                        
                        window.location = 'Categoria.jsp?nome=' + name + '&id=' + id;
                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("ERRO AO BUSCAR CATEGORIA! " + err.statusText);
                    }
                });
            }

            function excluir(linha) {
                var $row = $(linha).closest("tr");    // Find the row
                var $text = $row.find(".txt_nome").text(); // Find the text
                var $inteiro = $row.find(".n_int").text(); // Find the text

                $.ajax({
                    url: "/estimulos/categoria",
                    type: "POST",
                    data: {
                        operacao: "EXCLUIR",
                        acao: "EXCLUIR",
                        id: $inteiro
                    },
                    success: function (data) {
                        $($row).remove();
                        alert("Categoria excluida!");                        
                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("ERRO AO EXCLUIR CATEGORIA!");
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
            <div class="box">
                <div class="box-header">
                    <i class="fa fa-search"></i>
                    <h3 class="box-title"><b>Consultar categoria</b></h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body no-padding">
                    <table class="table table-striped">
                        <tbody id="tabela_categoria">
                            <tr>
                                <th>ID</th>
                                <th>NOME</th>                                                
                                <th>OPÇÕES</th>
                            </tr>                                            
                        </tbody></table>
                </div>                                
            </div>
        </div>



    </jsp:body>
</t:genericpages>
