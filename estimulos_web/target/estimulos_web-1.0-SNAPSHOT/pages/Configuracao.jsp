<%-- 
    Document   : Configuracao
    Created on : 25/05/2016, 21:13:35
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">

        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>
        <script>
            var terapeuta;

            Date.prototype.yyyymmdd = function () {
                var yyyy = this.getFullYear().toString();
                var mm = (this.getMonth() + 1).toString(); // getMonth() is zero-based
                var dd = this.getDate().toString();
                return yyyy + "-" +(mm[1] ? mm : "0" + mm[0]) + "-" + (dd[1] ? dd : "0" + dd[0]); // padding
            };

            $(document).ready(function () {
                $.ajax({
                    url: "/estimulos/terapeuta",
                    type: "POST",
                    async: false,
                    data: {
                        operacao: "CONSULTAR",
                        acao: "CONSULTAR"
                    },
                    success: function (data) {
                        terapeuta = JSON.parse(data);
                        document.getElementById("nome_terapeuta").value = terapeuta.nome;
                        var dataNasc = new Date(terapeuta.dtNascimento.toString());                                      
                        document.getElementById("dtNasc").value = dataNasc.yyyymmdd();
                        document.getElementById("login").value = terapeuta.usuario.login;
                        document.getElementById("senha").value = terapeuta.usuario.senha;
                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("ERRO!");
                    }
                });
            });
        </script>
    </jsp:attribute>
    <jsp:attribute name="footer">   
    </jsp:attribute>
    <jsp:body>
        <div class="col-md-12" onload="carregar()">
            <div class="box box-default">
                <div class="box-header with-border">
                    <i class="fa fa-user"></i>
                    <h3 class="box-title">Editar dados pessoais</h3>
                </div><!-- /.box-header -->
                <br/>
                <div class="box-body">
                    <div class="row">                        
                        <div class="col-md-6">                            
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <label>Nome</label>
                                    <input type="text" class="form-control"  id="nome_terapeuta" name="nome_terapeuta" placeholder="Nome">
                                </div>                                     
                            </div>                                    

                            <!-- SEGUNDA LINHA -->
                            <div class="row">
                                <div class="form-group col-md-6">
                                    <label>Dt. Nascimento</label>
                                    <input type="date" name="dtNasc" id="dtNasc" class="form-control">
                                </div>

                            </div>
                            <!-- TERCEIRA LINHA -->
                            <div class="row">
                                <div class="form-group col-md-10">
                                    <label>E-mail</label>
                                    <input type="email" class="form-control" name="login" id="login">
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-10">
                                    <label>Senha</label>                                            
                                    <input type="password" class="form-control col-md-10" name="senha" id="senha">
                                </div>
                            </div>                                   

                        </div>                        
                    </div>

                    <br/>                                

                    <div class="row col-md-12">
                        <button type="button" class="btn btn-success pull-right btn-lg">Salvar</button>                                    
                    </div>

                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpages>
