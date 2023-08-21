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
        <script src="../plugins/jQueryUI/jquery-ui.js" type="text/javascript"></script>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

        <script>
            var helper;
            var resposta;
            var retorno;
            var categorias;
            var estimulos;
            var fases;
            var niveis = ["1", "2", "3", "4", "5"];
            var motivoDoErro;

            function tags() {
                var availableTags = [
                    "ActionScript",
                    "AppleScript",
                    "Asp",
                    "BASIC",
                    "C",
                    "C++",
                    "Clojure",
                    "COBOL",
                    "ColdFusion",
                    "Erlang",
                    "Fortran",
                    "Groovy",
                    "Haskell",
                    "Java",
                    "JavaScript",
                    "Lisp",
                    "Perl",
                    "PHP",
                    "Python",
                    "Ruby",
                    "Scala",
                    "Scheme"
                ];
                $("#nomePaciente").autocomplete({
                    source: availableTags
                });
            }

            function gerar() {

                $.ajax({
                    url: "/estimulos/carregarestatisticas",
                    type: "POST",
                    async: false,
                    data: {
                        operacao: "CONSULTAR",
                        acao: "GERAR",
                        categoria: document.getElementById("categoria").value,
                        estimulo: document.getElementById("estimulo").value,
                        fase: document.getElementById("fase").value,
                        motivo: document.getElementById("motivo").value,
                        inicio: document.getElementById("inicio").value,
                        fim: document.getElementById("fim").value
                    },
                    success: function (data) {
                        retorno = JSON.parse(data);
                        if (retorno.mensagem === undefined) {
                            resposta = retorno.entidades;
                        } else {
                            alert(retorno.mensagem);
                            return;
                        }

                    },
                    error: function (err) {
                        console.log(err.statusText);
                        alert("ERRO!");
                    }
                });


                // Load the Visualization API and the corechart package.
                google.charts.load('current', {'packages': ['corechart']});

                // Set a callback to run when the Google Visualization API is loaded.
                google.charts.setOnLoadCallback(drawChart);

                // Callback that creates and populates a data table,
                // instantiates the pie chart, passes in the data and
                // draws it.
                function drawChart() {
                    var categoria = "";
                    var qtdeCategorias = 0;
                    // Create the data table.
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Topping');
                    var categorias = [];
                    var flgCategoria = false;
                    for (var i = 0; i < resposta.length; i++) {
                        for (var k = 0; k < categorias.length; k++) {
                            if (categorias[k] === resposta[i].categoria.nome) {
                                flgCategoria = true;
                            }
                        }
                        if (!flgCategoria) {
                            data.addColumn('number', resposta[i].categoria.nome);
                            categorias.push(resposta[i].categoria.nome);
                        } else {
                            flgCategoria = false;
                        }
                    }

                    // TODO : Arrumar bug de só adicionar null no fim
                    var primeiraData = new Date(resposta[0].data);
                    var novodia = true;
                    // variavel para manter os indices das categorias
                    var indice = 0;
                    for (var i = 0; i < resposta.length; i++) {
                        var dataTentativa = new Date(resposta[i].data);
                        if (primeiraData.getTime() === dataTentativa.getTime()) {
                            var month = dataTentativa.getUTCMonth() + 1; //months from 1-12
                            var day = dataTentativa.getUTCDate();
                            if (novodia) {
                                var aux = [day + "/" + month];
                                indice = 0;
                                if (i !== 0) {
                                    if (resposta[i - 1].categoria.nome === categorias[indice]) {
                                        aux.push(resposta[i - 1].acertos);
                                        indice++;
                                    } else {
                                        aux.push(null);
                                        indice++;
                                        for (indice; indice < categorias.length; indice++) {
                                            if (resposta[i - 1].categoria.nome === categorias[indice]) {
                                                aux.push(resposta[i - 1].acertos);
                                                indice++;
                                                break;
                                            } else {
                                                aux.push(null);
                                            }
                                        }
                                    }

                                }
                                novodia = false;
                            }
                            if (resposta[i].categoria.nome === categorias[indice]) {
                                aux.push(resposta[i].acertos);
                                indice++;
                            } else {
                                aux.push(null);
                                indice++;
                                for (indice; indice < categorias.length; indice++) {
                                    if (resposta[i - 1].categoria.nome === categorias[indice]) {
                                        aux.push(resposta[i - 1].acertos);
                                        indice++;
                                        break;
                                    } else {
                                        aux.push(null);
                                    }
                                }
                            }

                        } else {
                            if ((aux.length - 1) !== categorias.length) {
                                for (var t = (categorias.length - (aux.length - 1)); t < categorias.length + 1; t++) {
                                    aux.push(null);
                                }
                            }
                            data.addRow(aux);
                            aux = [];
                            novodia = true;
                            var primeiraData = new Date(resposta[i].data);
                            // só possui um registro neste dia?
                            if ((i + 1) === resposta.length) {
                                indice = 0;
                                var dataTentativa = new Date(resposta[i].data);
                                var month = dataTentativa.getUTCMonth() + 1; //months from 1-12
                                var day = dataTentativa.getUTCDate();
                                var aux = [day + "/" + month];
                                if (resposta[i].categoria.nome === categorias[indice]) {
                                    aux.push(resposta[i].acertos);
                                    indice++;
                                } else {
                                    aux.push(null);
                                    indice++;
                                    for (indice; indice < categorias.length; indice++) {
                                        if (resposta[i].categoria.nome === categorias[indice]) {
                                            aux.push(resposta[i].acertos);
                                            indice++;
                                            break;
                                        } else {
                                            aux.push(null);
                                        }
                                    }
                                }
                                for (indice; indice < categorias.length; indice++) {
                                    aux.push(null);
                                }
                            }
                        }
                    }
                    data.addRow(aux);
                    // Set chart options
                    var options = {'title': 'Quantidade de acertos por categoria | Fase Tocar',
                        'width': 800,
                        'height': 500,
                        curveType: "function",
                        hAxis: {
                            title: 'Dias'
                        },
                        vAxis: {
                            title: 'Quantidade de acertos'
                        }
                    };

                    // Instantiate and draw our chart, passing in some options.                    
                    document.getElementById('chart_div').innerHTML = "";
                    var grafico = document.getElementById('chart_div');
                    var chart;

                    chart = new google.visualization.LineChart(grafico);
                    chart.draw(data, options);
                }
            }
            iniciarPagina();
            function iniciarPagina() {
                carregarSelects();
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

            function carregarSelects() {
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
            }


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

        </script>

        <title>Estimulos - Estatisticas</title>
    </jsp:attribute>
    <jsp:attribute name="footer">   
    </jsp:attribute>
    <jsp:body>

        <!-- Main content -->                
        <!-- form start -->                
        <div class="box-body" onload="carregarSelects()">

            <!-- SOU CADASTRADO -->
            <div class="col-md-12">
                <div class="box box-default">
                    <div class="box-body">
                        <div class="row">                                    
                            <div class="form-group col-md-12">
                                <div class="box-header with-border">
                                    <h3>
                                        <i class="fa fa-line-chart"></i>
                                        Estatisticas
                                    </h3>                   
                                </div><!-- /.box-header -->                                        
                                <div class="row col-md-12">
                                    <div class="form-group col-md-8">                                                
                                        <div class="input-group col-md-12">
                                            <label>Nome do paciente</label>
                                            <input name="NomePaciente" id="NomePaciente" onchange="tags()" class="form-control" placeholder="Nome do paciente" type="text">                                            
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
                                            <select class="form-control" id="fase" name="fase">
                                                <option value="todos">Todas as fases</option>                                                        
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
                                                    <input class="form-control pull-right active" id="inicio" name="inicio" type="date">
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
                                                    <input class="form-control pull-right active" id="fim" name="fim" type="date">
                                                </div>
                                                <!-- /.input group -->
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>   
                        <!--                        <div class="row col-md-12">
                                                    <div class="form-group">
                                                        <div class="radio">
                                                            <label>
                                                                <input type="radio" name="optionsRadios" id="barra" value="barra" checked="">
                                                                Gráfico de Barras
                                                            </label>
                                                        </div>
                                                        <div class="radio">
                                                            <label>
                                                                <input type="radio" name="optionsRadios" id="pizza" value="pizza">
                                                                Gráfico de Pizza
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>-->
                        <div class="row col-md-12">
                            <button id="gerar" onclick="gerar()" class="btn-lg bg-green pull-right">Ver estatísticas</button>                            
                        </div>
                        <br/><br/>
                        <div class="row col-md-10">
                            <div id="chart_div" class="pull-right"></div>
                        </div>

                    </div>
                </div>
            </div>
        </div><!-- /.box-body -->

    </jsp:body>
</t:genericpages>


