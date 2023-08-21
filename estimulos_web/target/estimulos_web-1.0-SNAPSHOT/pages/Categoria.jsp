<%-- 
    Document   : Categoria
    Created on : 22/04/2016, 15:55:34
    Author     : GUSTAVO-PC
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">
        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>
        <script>
            var id_categoria;
            function gravarCategoria() {
                var nome = document.getElementById("txtNomeCategoria");
                if (nome.value === "") {
                    alert("Favor inserir um nome para a categoria!");
                } else {
                    var op;
                    if (id_categoria !== "") {
                        op = "ALTERAR";
                    } else {
                        op = "SALVAR";
                    }                    
                    $.ajax({
                        url: "/estimulos/categoria",
                        type: "POST",
                        data: {
                            operacao: op,
                            acao: op,
                            nome: nome.value,
                            id: id_categoria
                        },
                        success: function (data) {
                            alert("Categoria salva!");
                        },
                        error: function (err) {
                            console.log(err.statusText);
                            alert("ERRO AO SALVAR A CATEGORIA! " + err.statusText);
                        }
                    });
                }
            }

            function loadPageVar(sVar) {
                return decodeURI(window.location.search.replace(new RegExp("^(?:.*[&\\?]" + encodeURI(sVar).replace(/[\.\+\*]/g, "\\$&") + "(?:\\=([^&]*))?)?.*$", "i"), "$1"));
            }

            $(function () {
                document.getElementById("txtNomeCategoria").value = loadPageVar('nome');
                id_categoria = loadPageVar('id');
            });
        </script>
    </jsp:attribute>
    <jsp:attribute name="footer">   
    </jsp:attribute>
    <jsp:body>
        <div class="col-md-12">
            <div class="box box-default">
                <div class="box-header with-border">
                    <i class="fa fa-plus"></i>
                    <h3 class="box-title">Nova categoria</h3>
                </div><!-- /.box-header -->
                <br/>
                <div class="box-body">
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="box-header with-border">
                                <i class="fa fa-text-width"></i>
                                <h1 class="box-title">Nome da categoria</h1>                                                            
                            </div><!-- /.box-header -->
                            <div class="box-body table-responsive no-padding">
                                <div class="row col-md-12">
                                    <div class="form-group col-md-4">                                                
                                        <label>Nome da categoria</label>
                                        <input type="text" class="form-control"  id="txtNomeCategoria" placeholder="Nome da categoria">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <br/>                                

                    <div class="row col-md-12">
                        <button type="button" class="btn btn-success pull-right btn-lg" onclick="gravarCategoria()">Salvar</button>                                    
                    </div>

                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpages>