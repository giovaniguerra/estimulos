<%-- 
    Document   : Consultar_Estimulo
    Created on : 22/04/2016, 15:43:00
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">
        
        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>

        <script>

            $.ajax({
                url: "/estimulos/estimulo",
                type: "POST",
                data: {
                    operacao: "CONSULTAR",
                    acao: "CONSULTAR"
                },
                success: function (data) {
                    categorias = JSON.parse(data);
                    var selectCategoria = document.getElementById("categoria");
                    for (var i = 0; i < categorias.length; i++) {
                        var elem = document.createElement('option');
                        elem.id = categorias[i].ID;
                        elem.value = categorias[i].ID;
                        elem.text = categorias[i].nome;
                        selectCategoria.add(elem, selectCategoria[i + 1]);
                    }
                    // Ajax para criar os estimulos
                    $.ajax({
                        url: "/estimulos/estimulo",
                        type: "POST",
                        data: {
                            operacao: "CONSULTAR",
                            acao: "BUSCAR"
                        },
                        success: function (data) {
                            estimulos = JSON.parse(data);
                            for (var i = 0; i < estimulos.length; i++) {                                
                                $("#tabela_estimulo").append("<tr name='" + estimulos[i].categoria.ID + "' id='estimulo_" + estimulos[i].ID + "'>" +
                                        "<td class='n_int'>" + estimulos[i].ID + "</td>" +
                                        "<td class='txt_nome'>" + estimulos[i].nome + "</td>" +
                                        "<td>" +
                                        "<img src='" + estimulos[i].imagem.uri + "' style='width: 50px;height: 50px'/>" +
                                        "</td>" +
                                        "<td>" +
                                        "<button type='button' class='btn btn-warning' onclick='alterar(this)'>Alterar</button>" +
                                        "<button type='button' class='btn btn-danger' onclick='excluir(this)'>Excluir</button>" +
                                        "</td>" +
                                        "</tr>");
                            }
                        },
                        error: function (err) {
                            console.log(err.statusText);
                            alert("ERRO AO CARREGAR AS IMAGENS " + err.statusText);
                        }
                    });
                },
                error: function (err) {
                    console.log(err.statusText);
                    alert("ERRO AO CARREGAR O SELECT! " + err.statusText);
                }
            });


            function alterar(linha) {
                var $row = $(linha).closest("tr");    // Find the row
                var $text = $row.find(".txt_nome").text(); // Find the text
                var $inteiro = $row.find(".n_int").text(); // Find the text

                $.ajax({
                    url: "/estimulos/estimulo",
                    type: "POST",
                    data: {
                        operacao: "VISUALIZAR",
                        acao: "VISUALIZAR",
                        id: $inteiro
                    },
                    success: function (data) {
                        var e = JSON.parse(data);
                        var name = e.nome;
                        var id = e.ID;
                        var cat = e.categoria.ID;
                        window.location = 'Novo_Estimulo.jsp?nome=' + name + '&id=' + id + '&categoria=' + cat;
                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("ERRO AO BUSCAR ESTIMULO! " + err.statusText);
                    }
                });
            }

            function excluir(linha) {
                var $row = $(linha).closest("tr");    // Find the row
                var $text = $row.find(".txt_nome").text(); // Find the text
                var $inteiro = $row.find(".n_int").text(); // Find the text

                $.ajax({
                    url: "/estimulos/estimulo",
                    type: "POST",
                    data: {
                        operacao: "EXCLUIR",
                        acao: "EXCLUIR",
                        id: $inteiro
                    },
                    success: function (data) {
                        $($row).remove();
                        alert("Estímulo excluido!");
                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("ERRO AO EXCLUIR ESTIMULO!");
                    }
                });
            }

            function mudouCategoria() {
                var objetos = document.getElementById("tabela_estimulo").childNodes;
                var categoria = document.getElementById("categoria");
                $(objetos).fadeIn("fast", "linear");
                if (categoria.value !== "todos") {
                    for (var i = 1; i < objetos.length; i++) {
                        if (objetos[i].getAttribute("name") !== categoria.value) {
                            $(objetos[i]).fadeToggle("fast", "linear");
                        }
                    }
                }
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
                    <h3 class="box-title"><b>Consultar estímulo</b></h3>
                    <div class="pull-right">
                        <h1 class="box-title">Categoria:</h1>
                        <select id="categoria" name="categoria" class="form-control-static" onchange="mudouCategoria()">
                            <option value="todos">Todas categorias</option>                                
                        </select>
                    </div>
                </div>
                <!-- /.box-header -->
                <div class="box-body no-padding">
                    <table class="table table-striped">
                        <tr>
                            <th>ID</th>
                            <th>NOME</th>
                            <th>IMAGEM</th>
                            <th>OPÇÕES</th>
                        </tr>   
                        <tbody id="tabela_estimulo">

                        </tbody></table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </div>

    </jsp:body>
</t:genericpages>
