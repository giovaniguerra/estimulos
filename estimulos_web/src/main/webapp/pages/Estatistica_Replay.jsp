<%-- 
    Document   : Estatistica
    Created on : 21/04/2016, 22:34:21
    Author     : GUSTAVO-PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:genericpages>
    <jsp:attribute name="header">        
        <script src="../js/jquery-3.0.0.min.js" type="text/javascript"></script>
        <style type="text/css">
            #toque{
                width:80px;
                height:80px;
                background-color:#ff0000;
                border-radius: 130px;
                margin-top: 150px;
                margin-left: 300px;
            }
        </style>
        <script>
            var helper;
            var resposta;
            var retorno;
            var categorias;
            var estimulos;
            var fases = [];
            var nivel = [];
            var niveis = ["1", "2", "3", "4", "5"];
            var motivoDoErro;
            var pLeft;
            var pTop;
            // startar o replay

            var obj;
            function startar() {
                document.getElementById("start").disabled = true;
                document.getElementById("stop").disabled = false;

                var nTentativas = 1;

                for (var i = 0; i < resposta.length; i++) {
                    document.getElementById("resultadoTentativa").innerHTML = resposta[i].resultado.nome + " | Tentativa: " + (nTentativas++) + " | " + "Tarefa: " + resposta[i].tarefa.numTarefa;
                    // eh a fase de arrastar?
                    if (resposta[i].tarefa.faseId === 5) {
                        pLeft = document.getElementById("obj_" + resposta[i].movimentos[0].codigo).style.marginLeft;
                        pTop = document.getElementById("obj_" + resposta[i].movimentos[0].codigo).style.marginTop;
                        for (var j = 0; j < resposta[i].movimentos.length; j++) {
                            obj = document.getElementById("obj_" + resposta[i].movimentos[j].codigo);
                            $(obj).animate({marginTop: resposta[i].movimentos[j].y, marginLeft: resposta[i].movimentos[j].x}, 1000, function () {
                                $(obj).animate({marginTop: pTop, marginLeft: pLeft}, 1000, function () {
                                    document.getElementById("start").disabled = false;
                                    document.getElementById("stop").disabled = true;
                                });
                            });
                        }

                        for (var k = 0; k < resposta[i].tarefa.listLocais.length; k++) {
                            var div = document.createElement("obj_" + resposta[i].tarefa.listLocais[k].posicao.tipo);

                            div.style.marginLeft = resposta[i].tarefa.listLocais[k].posicao.margemX + "px";
                            div.style.marginTop = resposta[i].tarefa.listLocais[k].posicao.margemY + "px";
                        }
                    } else {
                        
                        for (var j = 0; j < resposta[i].movimentos.length; j++) {
                            $("#toque").fadeIn(1000);
                            $("#toque").fadeOut(1000)
                            $("#toque").animate({marginTop: resposta[i].movimentos[j].y, marginLeft: resposta[i].movimentos[j].x}, 1000, function(){
                                $("#toque").fadeIn(1000);                                
                            });
                            console.log("MarginTop: "+resposta[i].movimentos[j].y+"\n"+
                                    "MarginLeft: "+resposta[i].movimentos[j].x);
                        }
                        
                    }
                }
                nTentativas = 1;
            }

            function parar() {
                document.getElementById("start").disabled = false;
                document.getElementById("stop").disabled = true;
                $(obj).clearQueue();
            }


            $.ajax({
                url: "/estimulos/carregarestatisticas",
                type: "POST",
                data: {
                    operacao: "CONSULTAR",
                    acao: "CONSULTAR"
                },
                success: function (data) {
                    helper = JSON.parse(data);
                    categorias = helper.categorias;
                    fases = helper.fases;
                    motivoDoErro = helper.motivos;
                    criarSelects();
                },
                error: function (err) {
                    console.log(err.statusText);
                }
            });

            function criarSelects() {
                var selectCategoria = document.getElementById("categoria");
                var selectFase = document.getElementById("fase");
                var selectMotivo = document.getElementById("motivo");

                for (var i = 0; i < categorias.length; i++) {
                    var elem = document.createElement('option');
                    elem.id = categorias[i].ID;
                    elem.value = categorias[i].ID;
                    elem.text = categorias[i].nome;
                    selectCategoria.add(elem, selectCategoria[i + 1]);
                }

                for (var i = 0; i < fases.length; i++) {
                    var elem = document.createElement('option');
                    elem.value = fases[i].ID;
                    elem.text = fases[i].nome;
                    elem.id = fases[i].ID;
                    selectFase.add(elem, selectFase[i + 1]);
                }


                for (var i = 0; i < motivoDoErro.length; i++) {
                    var elem = document.createElement('option');
                    elem.value = motivoDoErro[i].ID;
                    elem.text = motivoDoErro[i].nome;
                    elem.id = motivoDoErro[i].ID;
                    selectMotivo.add(elem, selectMotivo[i + 1]);
                }
            }

            function recriar() {
                document.getElementById("objetos").innerHTML = "<div class='box-header with-border'>" +
                        "<button id='start' onclick='startar()' class='btn btn-success'> Iniciar replay</button>" +
                        "<button id='stop' onclick='parar()' class='btn btn-danger' disabled>Parar</button>" +
                        "<div class='row'>" +
                        "<br/>" +
                        "<h4>Resultado: <b><span id='resultadoTentativa'></span></b></h4>" +
                        "</div>" +
                        "</div>" +
                        "<br><br>" +
                        "<div id='toque' style='position: absolute;display:none;'></div>";
            }

            function gerar() {
                var objetos = document.getElementById("objetos");
                $.ajax({
                    url: "/estimulos/carregarestatisticas",
                    type: "POST",
                    async: false,
                    data: {
                        operacao: "CONSULTAR",
                        acao: "REPLAY",
                        categoria: document.getElementById("categoria").value,
                        estimulo: document.getElementById("estimulo").value,
                        fase: document.getElementById("fase").value,
                        nivel: document.getElementById("nivel").value,
                        motivo: document.getElementById("motivo").value,
                        inicio: document.getElementById("dataInicio").value,
                        fim: document.getElementById("dataFim").value
                    },
                    success: function (data) {
                        retorno = JSON.parse(data);
                        resposta = retorno.entidades;                        
                        if (resposta.length === 0) {
                            alert("Não existem replays para estes filtros!");
                        } else {
                            $(objetos).children().remove();
                            recriar();
                            // TODO : descobrir quantas tarefas existem, e montar as tarefas.
                            for (var i = 0; i < resposta[0].tarefa.listLocais.length; i++) {
                                var div = document.createElement("div");
                                var img = document.createElement("img");

                                img.id = "img_" + resposta[0].tarefa.listLocais[i].posicao.tipo;
                                img.src = resposta[0].tarefa.listLocais[i].estimulo.imagem.uriMascara;
                                img.height = resposta[0].tarefa.listLocais[i].estimulo.imagem.altura;
                                img.width = resposta[0].tarefa.listLocais[i].estimulo.imagem.largura;

                                div.id = "obj_" + resposta[0].tarefa.listLocais[i].posicao.tipo;
                                div.height = resposta[0].tarefa.listLocais[i].estimulo.imagem.altura;
                                div.width = resposta[0].tarefa.listLocais[i].estimulo.imagem.largura;

                                div.style.marginLeft = resposta[0].tarefa.listLocais[i].posicao.margemX + "px";
                                div.style.marginTop = resposta[0].tarefa.listLocais[i].posicao.margemY + "px";
                                div.style.position = "absolute";
                                div.appendChild(img);
                                objetos.appendChild(div);
                            }
//                        var tabela = document.getElementById("tabela");
                            var replay = document.getElementById("replay");

//                        tabela.style.display = 'table';
                            replay.style.display = 'table';
                        }
                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("Dados indisponíveis!");
                    }
                });
            }

            function reposicionar() {
                var obj_estimulo = document.getElementById("obj_estimulo");
                var obj_final = document.getElementById("obj_final");
                for (var i = 0; i < resposta.length; i++) {
                    for (var k = 0; k < resposta[i].tarefa.listLocais.length; k++) {
                        if (resposta[i].tarefa.listLocais[k].posicao.tipo === 0) {
                            // estimulo principal                        
                            obj_estimulo.style.marginLeft = resposta[0].tarefa.listLocais[k].posicao.margemX + "px";
                            obj_estimulo.style.marginTop = resposta[0].tarefa.listLocais[k].posicao.margemY + "px";
                            obj_estimulo.setAttribute("height", resposta[0].tarefa.listLocais[k].posicao.altura);
                            obj_estimulo.setAttribute("width", resposta[0].tarefa.listLocais[k].posicao.largura);
                        } else if (resposta[i].tarefa.listLocais[k].posicao.tipo === 2) {
                            // estimulo alvo                        
                            obj_final.style.marginLeft = resposta[0].tarefa.listLocais[k].posicao.margemX + "px";
                            obj_final.style.marginTop = resposta[0].tarefa.listLocais[k].posicao.margemY + "px";
                            obj_final.setAttribute("height", resposta[0].tarefa.listLocais[k].posicao.altura);
                            obj_final.setAttribute("width", resposta[0].tarefa.listLocais[k].posicao.largura);
                        } else if (resposta[i].tarefa.listLocais[k].posicao.tipo === 1) {

                        }
                    }
                }
            }

            function mudouCategoria() {
                var categoria = document.getElementById("categoria");
                var estimulo = document.getElementById("estimulo");
                var elem = document.createElement('option');
                if (categoria.value === "todos") {
                    estimulo.disabled = true;
                    estimulo.selectedIndex = 0;
                } else {
                    $.ajax({
                        url: "/estimulos/carregarestatisticas",
                        type: "POST",
                        async: false,
                        data: {
                            operacao: "BUSCAR",
                            objeto: "ESTIMULO",
                            categoria: categoria[categoria.selectedIndex].id
                        },
                        success: function (data) {
                            helper = JSON.parse(data);
                            estimulos = helper.estimulos;
                        },
                        error: function (err) {
                            console.log(err.statusText);
                        }
                    });

                    estimulo.disabled = false;
                    var opcoes = estimulo.getElementsByTagName('OPTION');

                    for (var i = 1; i < opcoes.length; i++) {
                        estimulo.removeChild(opcoes[i]);
                        i--;
                    }

                    for (var i = 0; i < estimulos.length; i++) {
                        var elem = document.createElement('option');
                        elem.value = estimulos[i].nome;
                        elem.text = estimulos[i].nome;
                        elem.id = estimulos[i].ID;
                        estimulo.add(elem, (i + 1));
                    }
                }
            }

            function carregarSelects() {
                $.ajax({
                    url: "/estimulos/carregarestatisticas",
                    type: "POST",
                    data: {
                        operacao: "CONSULTAR"
                    },
                    success: function (data) {
                        helper = JSON.parse(data);
                        categorias = helper.categorias;
                        fases = helper.fases;
                        motivoDoErro = helper.motivos;
                        criarSelects();
                    },
                    error: function (err) {
                        console.log(err.statusText);
                    }
                });
            }

            function mudouFase() {
                var fase = document.getElementById("fase");
                var nivel = document.getElementById("nivel");
                if (fase.value === "todos") {
                    nivel.disabled = true;
                    nivel.selectedIndex = 0;
                } else {
                    nivel.disabled = false;
                }
            }

            function criarSelects() {
                var selectCategoria = document.getElementById("categoria");
                var selectFase = document.getElementById("fase");
                var selectNivel = document.getElementById("nivel");
                var selectMotivo = document.getElementById("motivo");

                for (var i = 0; i < categorias.length; i++) {
                    var elem = document.createElement('option');
                    elem.id = categorias[i].ID;
                    elem.value = categorias[i].ID;
                    elem.text = categorias[i].nome;
                    selectCategoria.add(elem, selectCategoria[i + 1]);
                }

                for (var i = 0; i < fases.length; i++) {
                    var elem = document.createElement('option');
                    elem.id = fases[i].ID;
                    elem.value = fases[i].ID;
                    elem.text = fases[i].nome;
                    selectFase.add(elem, selectFase[i + 1]);
                }

                for (var i = 0; i < niveis.length; i++) {
                    var elem = document.createElement('option');
                    elem.value = niveis[i];
                    elem.text = niveis[i];
                    selectNivel.add(elem, selectNivel[i + 1]);
                }

                for (var i = 0; i < motivoDoErro.length; i++) {
                    var elem = document.createElement('option');
                    elem.value = motivoDoErro[i].ID;
                    elem.text = motivoDoErro[i].nome;
                    elem.id = motivoDoErro[i].ID;
                    selectMotivo.add(elem, selectMotivo[i + 1]);
                }
            }

            function mudouCategoria() {
                var categoria = document.getElementById("categoria");
                var estimulo = document.getElementById("estimulo");
                var elem = document.createElement('option');
                if (categoria.value === "todos") {
                    estimulo.disabled = true;
                    estimulo.selectedIndex = 0;
                } else {
                    $.ajax({
                        url: "/estimulos/carregarestatisticas",
                        type: "POST",
                        async: false,
                        data: {
                            operacao: "CONSULTAR",
                            acao: "BUSCAR",
                            objeto: "ESTIMULO",
                            categoria: categoria[categoria.selectedIndex].id
                        },
                        success: function (data) {
                            helper = JSON.parse(data);
                            estimulos = helper.estimulos;
                        },
                        error: function (err) {
                            console.log(err.statusText);
                        }
                    });

                    estimulo.disabled = false;
                    var opcoes = estimulo.getElementsByTagName('OPTION');

                    for (var i = 1; i < opcoes.length; i++) {
                        estimulo.removeChild(opcoes[i]);
                        i--;
                    }

                    for (var i = 0; i < estimulos.length; i++) {
                        var elem = document.createElement('option');
                        elem.value = estimulos[i].ID;
                        elem.text = estimulos[i].nome;
                        elem.id = estimulos[i].ID;
                        estimulo.add(elem, (i + 1));
                    }
                }
            }

        </script>

        <title>Estimulos - Replay</title>
        <!-- JavaScript das animações --> 
        <!--        <script src="Scripts/Animacoes.js" type="text/javascript"></script>-->

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
                    <div class="box-body">
                        <div class="row">                                    
                            <div class="form-group col-md-12">
                                <div class="box-header with-border">
                                    <h3>
                                        <i class="fa fa-video-camera"></i>
                                        Replay
                                    </h3>                   
                                </div><!-- /.box-header -->                                        
                                <div class="row col-md-12">
                                    <div class="form-group col-md-8">                                                
                                        <div class="input-group col-md-12">
                                            <label>Nome do paciente</label>
                                            <input id="nomePaciente" class="form-control" onchange="tags()" placeholder="Nome do paciente" type="text">
                                        </div>
                                    </div>                                           
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box-header with-border">
                                    <i class="fa fa-soccer-ball-o"></i>
                                    <h1 class="box-title">Estimulo</h1>                                                            
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">                                                
                                            <label>Categoria</label>
                                            <select class="form-control" id="categoria" name="categoria" onchange="mudouCategoria()">
                                                <option value="todos">Todas categorias</option>                                                        
                                            </select>
                                        </div>
                                        <div class="form-group col-md-4">                                                
                                            <label>Estimulo</label>
                                            <select class="form-control" id="estimulo" name="estimulo" disabled>
                                                <option value="todos">Todos os estímulos</option>                                                        
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
                                    <i class="fa fa-gamepad"></i>
                                    <h1 class="box-title">Jogo</h1>                                                            
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">                                                
                                            <label>Fase</label>
                                            <select class="form-control" id="fase" name="fase" onchange="mudouFase()">
                                                <option value="todos">Todas as fases</option>                                                        
                                            </select>
                                        </div>
                                        <div class="form-group col-md-4">                                                
                                            <label>Nível</label>
                                            <select class="form-control" id="nivel" name="nivel" disabled>
                                                <option value="todos">Todos os níveis</option>                                                        
                                            </select>
                                        </div>
                                        <div class="form-group col-md-4">                                                
                                            <label>Tipo resultado</label>
                                            <select class="form-control" id="motivo" name="motivo">
                                                <option value="todos">Todos os tipos</option>                                                        
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
                                    <i class="fa fa-calendar"></i>
                                    <h1 class="box-title">Periodo</h1>
                                </div><!-- /.box-header -->
                                <div class="box-body table-responsive no-padding">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-4">
                                            <div class="form-group">
                                                <label>Data inicio:</label>
                                                <div class="input-group">
                                                    <div class="input-group-addon">
                                                        <i class="fa fa-calendar"></i>
                                                    </div>
                                                    <input class="form-control pull-right active" id="dataInicio" name="dataInicio" type="date">
                                                </div>
                                                <!-- /.input group -->
                                            </div>
                                        </div>
                                        <div class="form-group col-md-4">
                                            <div class="form-group">
                                                <label>Data fim:</label>
                                                <div class="input-group">
                                                    <div class="input-group-addon">
                                                        <i class="fa fa-calendar"></i>
                                                    </div>                                                    
                                                    <input class="form-control pull-right active" id="dataFim" name="dataFim" type="date">
                                                </div>
                                                <!-- /.input group -->
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>   
                        <div class="row col-md-12">                            
                            <button class="btn-lg bg-green pull-right" onclick="gerar()">Ver estatísticas</button>                            
                        </div>


                        <div class="col-md-12">
                            <div class="box box-default" id="replay" style="display: none">
                                <div class="box-body">
                                    <div class="row">
                                        <div id="objetos" class="form-group col-md-12" style="height: 600px">  
                                            <div class="box-header with-border">
                                                <button id="start" class="btn btn-success"> Iniciar replay</button>
                                                <button id="stop" class="btn btn-danger" disabled>Parar</button>                                                
                                                <div class="row">
                                                    <br/>
                                                    <h4>Resultado: <b><span id="resultadoTentativa"></span></b></h4>
                                                </div>
                                            </div><!-- /.box-header -->
                                            <br><br>                                            
                                            <div id="toque" style="position: absolute;display:none;"></div>
                                        </div>
                                    </div>                                    
                                </div>
                            </div>
                        </div>
                    </div>                    
                </div>                
            </div>            
        </div><!-- /.box-body -->        

    </jsp:body>
</t:genericpages>


