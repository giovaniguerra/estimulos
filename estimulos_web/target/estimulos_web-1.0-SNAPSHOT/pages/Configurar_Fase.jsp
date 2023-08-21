<%-- 
    Document   : Configurar_Fase
    Created on : 22/04/2016, 16:40:55
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">

        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>                
        <script src="../js/jquery-ui-1.11.4/jquery-ui.js" type="text/javascript"></script>
        <link href="../js/jquery-ui-1.11.4/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <script>

            var numeroInsignificante = 0;
            resetar();

            function aleatorio() {
                var posicao = document.getElementById("posicao");
                var container = document.getElementById("posicoes");

                if (posicao.checked) {
                    container.style.display = 'none';
                } else {
                    container.style.display = 'table';
                }
            }

            function resetar() {
                var estimulos = document.getElementById("estimulos");
                var tarefas = document.getElementById("tarefas");
                var erros = dmulos = document.getElementById("estimulos");
                var tarefas = docuocument.getElementById("erros");
                var silenciar = document.getElementById("silenciar");

                estimulos.selectedIndex = "0";
                tarefas.selectedIndex = "0";
                erros.selectedIndex = "0";
                silenciar.selectedIndex = "0";
                excluirTudo();
                var fase = document.getElementById("fase");
                if (fase.value === "Fase Arrastar") {
                    addContainers();
                } else {
                    addPrincipal();
                }
                numeroInsignificante = 0;
            }



            function getPosicaoElemento(elemID) {
                var offsetTrail = document.getElementById(elemID);
                var offsetLeft = 0;
                var offsetTop = 0;
                while (offsetTrail) {
                    offsetLeft += offsetTrail.offsetLeft;
                    offsetTop += offsetTrail.offsetTop;
                    offsetTrail = offsetTrail.offsetParent;
                }
                if (navigator.userAgent.indexOf("Mac") != -1 &&
                        typeof document.body.leftMargin != "undefined") {
                    offsetLeft += document.body.leftMargin;
                    offsetTop += document.body.topMargin;
                }
                return {left: offsetLeft, top: offsetTop};
            }

            function teste() {
                alert("esquerda:" + getPosicaoElemento("principal").left + "\n" +
                        "topo:" + getPosicaoElemento("principal").top);
            }

            function mudouFase() {
                var fase = document.getElementById("fase");
                excluirTudo();
                if (fase.value === "Fase Arrastar") {
                    addContainers();
                } else {
                    addPrincipal();
                }
                numeroInsignificante = 0;
            }

            function addInsignificante() {
                var fase = document.getElementById("fase");
                if (numeroInsignificante > 1) {
                    alert("Número maximo de estímulo insignificante atingido!");
                } else {
                    numeroInsignificante++;
                    if (fase.value === "Fase Tocar") {
                        $("#container").append("<div id='insignificante_" + numeroInsignificante + "'" +
                                "style='border-color:#000000;border-style: solid;border-width: 3px;height: 100px; width: 100px'>" +
                                "Insignificante</div>");
                        $(function () {
                            $("#insignificante_" + numeroInsignificante).resizable({
                                containment: "#container"
                            });
                            $("#insignificante_" + numeroInsignificante).draggable({
                                containment: "#container"
                            });
                        });
                    } else {
                        $("#alvo_container").append("<div id='insignificante_" + numeroInsignificante + "'" +
                                "style='border-color:#000000;border-style: solid;border-width: 3px;height: 100px; width: 100px'>" +
                                "Insignificante</div>");
                        $(function () {
                            $("#insignificante_" + numeroInsignificante).resizable({
                                containment: "#alvo_container"
                            });
                            $("#insignificante_" + numeroInsignificante).draggable({
                                containment: "#alvo_container"
                            });
                        });
                    }
                }
            }

            function addPrincipal() {
                var fase = document.getElementById("fase");
                if (fase.value === "Fase Tocar") {
                    $("#container").append("<div id='principal'" +
                            "style='border-color:#ff9933;border-style: solid;border-width: 3px;height: 100px; width: 100px'>" +
                            "Principal</div>");
                    $(function () {
                        $("#principal").resizable({
                            alsoResize: "#alvo",
                            containment: "#container"
                        });
                        $("#principal").draggable({
                            containment: "#container"
                        });
                    });
                } else {
                    $("#principal_container").append("<div id='principal'" +
                            "style='border-color:#ff9933;border-style: solid;border-width: 3px;height: 100px; width: 100px'>" +
                            "Principal</div>");
                    $(function () {
                        $("#principal").resizable({
                            alsoResize: "#alvo",
                            containment: "#principal_container"
                        });
                        $("#principal").draggable({
                            containment: "#principal_container"
                        });
                    });
                }
            }

            function addAlvoPrincipal() {
                $("#alvo_container").append("<div id='alvo'" +
                        "style='position: absolute;border-color:#668cff;border-style: solid;border-width: 3px;height: 100px; width: 100px'>" +
                        "Alvo</div>");
                $(function () {
                    $("#alvo").resizable({
                        alsoResize: "#principal",
                        containment: "#alvo_container"
                    });
                    $("#alvo").draggable({
                        containment: "#alvo_container"
                    });
                });
            }

            function addContainers() {
                $("#container").append("<div class='col-md-6' id='principal_container' style='height: 100%;border-right-style: solid;border-right-width: 3px;'>" +
                        "</div>" +
                        "<div class='col-md-6' id='alvo_container' style='height: 100%;'>" +
                        "</div>");
                addPrincipal();
                addAlvoPrincipal();
            }

            function excluirTudo() {
                $("#container").empty();
            }

            function excluirInsignificante() {
                if (numeroInsignificante === 0) {
                    alert("Não existem estimulos insignificantes!");
                } else {
                    var fase = document.getElementById("fase");
                    if (fase.value === "Fase Arrastar") {
                        var container = document.getElementById("alvo_container");
                    } else {
                        var container = document.getElementById("container");
                    }
                    var insignificante = document.getElementById("insignificante_" + numeroInsignificante);
                    container.removeChild(insignificante);
                    numeroInsignificante--;
                }
            }

            excluirTudo();
            addContainers();
        </script>
    </jsp:attribute>
    <jsp:attribute name="footer">   
    </jsp:attribute>
    <jsp:body>        
        <div class="box box-default" onload="resetar()">
            <div class="box-header with-border">
                <i class="fa fa-cog"></i>
                <h3 class="box-title">Configurar fase</h3>
            </div><!-- /.box-header -->

            <div class="box-body">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box-header with-border">
                            <div class="pull-left">
                                <h1 class="box-title">Fase:</h1>
                                <select id="fase" class="form-control-static" onchange="mudouFase()">
                                    <option>Fase Arrastar</option>
                                    <option>Fase Tocar</option>
                                </select>
                            </div>
                            <div class="pull-right col-md-8">
                                <h1 class="box-title">Nível:</h1>
                                <select id="nivel" class="form-control-static" onchange="resetar()">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                </select>
                            </div>
                        </div><!-- /.box-header -->
                    </div>
                </div>
                <div class="box-body table-responsive no-padding">
                    <div class="row col-md-12 pull-left">
                        <br/>
                        <div class="box-header with-border">
                            <i class="fa fa-info-circle"></i>
                            <h1 class="box-title">Configurações</h1>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <div class="row col-md-12">
                                <div class="form-group col-md-4">                                                
                                    <label>Número de estímulos</label>
                                    <select class="form-control" id="estimulos" name="estimulos">                                            
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                    </select>
                                </div>
                                <div class="form-group col-md-4">                                                
                                    <label>Número de tarefas</label>
                                    <select class="form-control" id="tarefas" name="tarefas">                                            
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                        <option value="7">7</option>
                                        <option value="8">8</option>
                                        <option value="9">9</option>
                                        <option value="10">10</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <br/><br/>
                    <div class="row col-md-12">
                        <div class="box-header with-border">
                            <i class="fa fa-close"></i>
                            <h1 class="box-title">Erros</h1>
                        </div><!-- /.box-header -->
                        <div class="box-body table-responsive no-padding">
                            <div class="row col-md-12">
                                <div class="form-group col-md-4">                                                
                                    <label>Limite de erros por tarefa</label>
                                    <select class="form-control" id="erros" name="erros">                                            
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                        <option value="7">7</option>
                                        <option value="8">8</option>
                                        <option value="9">9</option>
                                        <option value="10">10</option>
                                    </select>
                                </div>
                                <div class="form-group col-md-4">                                                
                                    <label>Silenciar sons de erro após</label>
                                    <select class="form-control" id="silenciar" name="silenciar">                                            
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                        <option value="7">7</option>
                                        <option value="8">8</option>
                                        <option value="9">9</option>
                                        <option value="10">10</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row col-md-7">
                        <br/>
                        <label>
                            <input type="checkbox" id="posicao" name="posicao" onchange="aleatorio()"> Posição aleatória para os estímulos
                        </label>
                    </div>
                </div>
                <br/><br/>                 
                <div class="row col-md-12" id="posicoes">

                    <div class="box-header with-border">
                        <i class="fa fa-cog"></i>
                        <h3 class="box-title">Configurar posições</h3>                                
                    </div><!-- /.box-header -->
                    <br/>                        
                    <button type="button" class="btn btn-primary" onclick="addInsignificante()">Adicionar insignificante</button>
                    <button type="button" class="btn btn-danger" onclick="excluirInsignificante()">Apagar insignificante</button>
                    <button type="button" class="btn btn-success" onclick="mudouFase()">Refazer</button>
                    <br/><br/>
                    <div class="box-body col-md-12" id="container" style="border-style: solid;border-width: 3px;height: 300px; width: 800px;">

                    </div>
                </div>
                <div class="row col-md-12"><br/>
                    <button type="button" class="btn btn-success pull-right btn-lg">Salvar jogo</button>
                </div>
            </div>
        </div>
    </jsp:body>
</t:genericpages>
