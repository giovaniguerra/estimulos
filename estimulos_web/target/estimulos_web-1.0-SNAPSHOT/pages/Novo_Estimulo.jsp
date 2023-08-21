<%-- 
    Document   : Novo_Estimulo
    Created on : 22/04/2016, 15:19:52
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">


        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>

        <script src="../plugins/jQueryUI/jquery-ui.js" type="text/javascript"></script>

        <script>
            var categorias;
            var id_estimulo = null;
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

                    $(function () {
                        document.getElementById("nome").value = loadPageVar('nome');
                        var id_categoria = loadPageVar('categoria');
                        var select = document.getElementById("categoria");
                        for (var i = 0; i < select.options.length; i++) {
                            if (select.options[i].value === id_categoria) {
                                select.selectedIndex = i;
                            }
                        }
                        id_estimulo = loadPageVar('id');
                    });
                },
                error: function (err) {
                    console.log(err.statusText);
                    alert("ERRO! " + err.statusText);
                }
            });

            function loadPageVar(sVar) {
                return decodeURI(window.location.search.replace(new RegExp("^(?:.*[&\\?]" + encodeURI(sVar).replace(/[\.\+\*]/g, "\\$&") + "(?:\\=([^&]*))?)?.*$", "i"), "$1"));
            }


            function gravarEstimulo() {                
                var op;
                if(id_estimulo !== ""){
                    op = "ALTERAR";
                }
                else{
                    op="SALVAR";
                }
                alert("operacao: "+op)
                $.ajax({
                    url: "/estimulos/estimulo",
                    type: 'POST',
                    data: {
                        operacao: op,
                        acao: op,
                        nome: document.getElementById("nome").value,
                        categoria: document.getElementById("categoria").value,                        
                        id: id_estimulo
                    },
                    success: function (data) {                        
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
                        <i class="fa fa-plus"></i>
                        <h3 class="box-title">Novo Estímulo</h3>
                    </div><!-- /.box-header -->
                    <br/>
                    <div class="box-body">
                        <form id="formulario" method="post" enctype="multipart/form-data">
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="box-header with-border">
                                        <i class="fa fa-soccer-ball-o"></i>
                                        <h1 class="box-title">Nome do Estímulo</h1>                                                            
                                    </div><!-- /.box-header -->
                                    <div class="box-body table-responsive no-padding">
                                        <div class="row col-md-12">
                                            <div class="form-group col-md-4">                                                
                                                <label>Nome do Estímulo</label>
                                                <input type="text" class="form-control" id="nome"  name="nome" placeholder="Nome do estímulo">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <br/>

                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="box-header with-border">
                                        <i class="fa fa-level-down"></i>
                                        <h1 class="box-title">Categoria</h1>                                                            
                                    </div><!-- /.box-header -->
                                    <div class="box-body table-responsive no-padding">
                                        <div class="row col-md-12">
                                            <div class="form-group col-md-4">
                                                <label>Categoria</label>
                                                <select id="categoria" name="categoria" class="form-control">
                                                    <option>Selecione</option>
                                                </select>
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
                                                <label>Importar estímulo</label>
                                                <input type="file" name="foto" id="foto">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br/>

                            <div class="row col-md-12">
                                <button type="button" onclick="gravarEstimulo()" id="gravar" class="btn btn-success pull-right btn-lg">Salvar</button>                                    
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div><!-- /.box-body -->

    </jsp:body>
</t:genericpages>
