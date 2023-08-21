<%-- 
    Document   : Vincular_Estimulo
    Created on : 22/04/2016, 16:28:31
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">
        
        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>

        <script>
            var categorias;
            var estimulos;

            var id_jogo;

            function loadPageVar(sVar) {
                return decodeURI(window.location.search.replace(new RegExp("^(?:.*[&\\?]" + encodeURI(sVar).replace(/[\.\+\*]/g, "\\$&") + "(?:\\=([^&]*))?)?.*$", "i"), "$1"));
            }

            $(function () {
                id_jogo = loadPageVar('id');
            });

            function getSelecionados() {
                var itens = document.getElementsByName("check_estimulo");
                var selecionados = new Array();
                for (var i = 0; i < itens.length; i++) {
                    if (itens[i].checked) {
                        selecionados.push(itens[i].id);
                    }
                }
                var json = JSON.parse(JSON.stringify(selecionados));
                $.ajax({
                    url: "/estimulos/jogo",
                    type: "POST",
                    data: {
                        operacao: "SALVAR",
                        acao: "VINCULAR",
                        id: id_jogo,
                        estimulos: json
                    },
                    success: function (data) {                        
                        window.location = 'Configurar_Fase.jsp?id=' + id_jogo;
                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("ERRO AO GRAVAR O JOGO! " + err.statusText);
                    }
                });
            }


            // Ajax para popular o filtro de categorias
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
                                $("#estimulos").append("<div class='col-md-3' id='estimulo_" + estimulos[i].ID + "' name='" + estimulos[i].categoria.ID + "'>" +
                                        "<img src='" + estimulos[i].imagem.uri + "' style='width: 100px;height: 100px'/><br/>" +
                                        "<label>" +
                                        "<input type='checkbox' id='" + estimulos[i].ID + "' name='check_estimulo'> " + estimulos[i].nome +
                                        "</label>" +
                                        "</div>");
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

            function mudouCategoria() {
                var objetos = document.getElementById("estimulos").childNodes;
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

            <!-- SOU CADASTRADO -->
            <div class="col-md-12">
                <div class="box box-default">
                    <div class="box-header with-border">
                        <i class="fa  fa-chain"></i>
                        <h3 class="box-title"><b>Vincular estímulo</b></h3>
                        <div class="pull-right">
                            <h1 class="box-title">Categoria:</h1>
                            <select id="categoria" name="categoria" class="form-control-static" onchange="mudouCategoria()">
                                <option value="todos">Todas categorias</option>                                
                            </select>
                        </div>
                    </div><!-- /.box-header -->
                    <br/>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-xs-12">                                        
                                <!-- /.box-header -->
                                <div class="box-body table-responsive no-padding" >
                                    <div class="row col-md-12" id="estimulos">

                                    </div>
                                </div>
                            </div>
                        </div>
                        <br/><br/>
                        <div class="row col-md-12">
                            <button onclick="getSelecionados()" type="button" class="btn btn-primary pull-right btn-lg">Próximo</button>
                        </div>
                    </div>
                </div>
            </div>
        </div><!-- /.box-body -->

    </jsp:body>
</t:genericpages>
