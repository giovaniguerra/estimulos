/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.estimulos.core.teste;

import br.com.estimulos.core.fachada.impl.Fachada;
import br.com.estimulos.dominio.Audio;
import br.com.estimulos.dominio.CategoriaEstimulo;
import br.com.estimulos.dominio.EntidadeDominio;
import br.com.estimulos.dominio.EnumObjetoTela;
import br.com.estimulos.dominio.Estimulo;
import br.com.estimulos.dominio.Fase;
import br.com.estimulos.dominio.FaseTocar;
import br.com.estimulos.dominio.Imagem;
import br.com.estimulos.dominio.Jogo;
import br.com.estimulos.dominio.LocalEstimulo;
import br.com.estimulos.dominio.NivelArrastar;
import br.com.estimulos.dominio.Paciente;
import br.com.estimulos.dominio.Pessoa;
import br.com.estimulos.dominio.Posicao;
import br.com.estimulos.dominio.Reforcador;
import br.com.estimulos.dominio.Resultado;
import br.com.estimulos.dominio.Tarefa;
import br.com.estimulos.dominio.Terapeuta;
import br.com.estimulos.dominio.TipoResultado;
import br.com.estimulos.dominio.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Caio Gustavo
 */
public class TesteMain {

    static Fachada f;

    public static void main(String[] args) {

        f = new Fachada();
//        f.salvar(mockEstimulo());

        CategoriaEstimulo categoriaEstimulo = new CategoriaEstimulo();
        categoriaEstimulo.setNome("Bola");
        categoriaEstimulo.setDataCriacao(new Date());
        categoriaEstimulo.setUltimaAtualizacao(new Date());
        categoriaEstimulo.setSincronizado(false);
                
        f.salvar(categoriaEstimulo);

        CategoriaEstimulo categoriaEstimulo2 = new CategoriaEstimulo();
        categoriaEstimulo2.setNome("Fruta");
        categoriaEstimulo2.setDataCriacao(new Date());
        categoriaEstimulo2.setUltimaAtualizacao(new Date());
        categoriaEstimulo2.setSincronizado(false);
        
        f.salvar(categoriaEstimulo2);

        criarEstimulos("a", categoriaEstimulo.getID(), "/teste",
                "/teste", "Bola de Futebol");

        criarEstimulos("a", categoriaEstimulo.getID(), "/teste",
                "/teste", "Bola de Basquete");

        criarEstimulos("a", categoriaEstimulo.getID(), "/teste",
                "/teste", "Bola de Tênis");

        criarEstimulos("a", categoriaEstimulo.getID(), "/teste",
                "/teste", "Bola de Vôley");

        criarEstimulos("a", categoriaEstimulo2.getID(), "/teste",
                "/teste", "Banana");

        criarEstimulos("a", categoriaEstimulo2.getID(), "/teste",
                "/teste", "Pera");

        criarEstimulos("a", categoriaEstimulo2.getID(), "/teste",
                "/teste", "Maçã");

        criarEstimulos("o", categoriaEstimulo2.getID(), "/teste",
                "/teste", "Mamão");

    }

    public static void criarEstimulos(String genero, int idCate, String uriImagem, String uriMascara, String nomeEstimulo) {
        Estimulo estimulo;
        CategoriaEstimulo primeiraCategoria = new CategoriaEstimulo();
        primeiraCategoria.setID(idCate);
        Imagem imagem = new Imagem();
        imagem.setLargura(10);
        imagem.setAltura(10);
        imagem.setNome("ic_casa.png");
        imagem.setUri(uriImagem);
        imagem.setUriMascara(uriMascara);
        imagem.setDataCriacao(new Date());
        imagem.setUltimaAtualizacao(new Date());
        imagem.setSincronizado(false);

        estimulo = new Estimulo();
        estimulo.setCategoria(primeiraCategoria);

        estimulo.setNome(nomeEstimulo);
        estimulo.setDataCriacao(new Date());
        estimulo.setUltimaAtualizacao(new Date());
        
        
        Audio a = new Audio();
        a.setDuracao(4564645);
        a.setNome("testse");
        a.setUri("hudgau");
        a.setDataCriacao(new Date());
        a.setUltimaAtualizacao(new Date());
        a.setSincronizado(false);
        
        estimulo.setAudio(a);
        estimulo.setImagem(imagem);
        estimulo.setGenero(genero);
        estimulo.setDataCriacao(new Date());
        estimulo.setUltimaAtualizacao(new Date());
        estimulo.setSincronizado(false);
        estimulo.setCodigo("teste");
        
        
        
//        return estimulo;

            f.salvar(estimulo);
        
    }

    public static Audio mockAudio() {
        Audio a = new Audio();
        a.setDuracao(4564645);
        a.setNome("testse");
        a.setUri("hudgau");
        a.setDataCriacao(new Date());
        a.setUltimaAtualizacao(new Date());
        a.setSincronizado(false);

        return a;
    }

    public static Terapeuta mockTerapeuta() {
        Terapeuta t = new Terapeuta();
        t.setUsuario(new Usuario());
        t.getUsuario().setLogin("teste@teste.com.br");
        t.getUsuario().setSenha("teste");

        t.getUsuario().setDataCriacao(new Date());
        t.getUsuario().setUltimaAtualizacao(new Date());
        t.getUsuario().setSincronizado(false);
        t.setNome("teste");
        t.setDataCriacao(new Date());
        t.setUltimaAtualizacao(new Date());
        t.setSincronizado(false);

        return t;
    }

    public static Tarefa mockTarefa() {
        Tarefa t = new Tarefa();
        t.setDataCriacao(new Date());
        t.setUltimaAtualizacao(new Date());

        t.setCompletada(false);
        t.setFaseId(4);
        t.setNumTarefa(1);
        t.setDataCriacao(new Date());
        t.setUltimaAtualizacao(new Date());
        t.setSincronizado(false);

        LocalEstimulo le = new LocalEstimulo();
        le.setPosicao(new Posicao());
        le.getPosicao().setID(1);
        le.setEstimulo(new Estimulo());
        le.getEstimulo().setID(1);

        List<LocalEstimulo> locais = new ArrayList<>();
        locais.add(le);

        t.setListLocais(locais);

        return t;
    }

    public static Reforcador mockReforcador() {
        Reforcador r = new Reforcador();
        r.setMidia(new Audio());
        r.setNome("som");
        r.setDataCriacao(new Date());
        r.setUltimaAtualizacao(new Date());
        r.setSincronizado(false);
        r.setTempoDuracao(445454);
        return r;
    }

    public static Posicao mockPOsicao() {
        Posicao p = new Posicao();
        p.setAltura(4f);
        p.setLargura(5f);
        p.setMargemX(6f);
        p.setMargemY(6f);
        p.setTipo(1);

        p.setDataCriacao(new Date());
        p.setUltimaAtualizacao(new Date());
        p.setSincronizado(false);

        return p;
    }

    public static Fase mockFase() {
        Fase f = new Fase();
        f.setNome("tocar");
        f.setUriImagem("teste");
        f.setNomeClasseHerdade(FaseTocar.class.getName());
        f.setDataCriacao(new Date());
        f.setUltimaAtualizacao(new Date());
        f.setSincronizado(false);

        return f;
    }

    public static NivelArrastar mockNivel() {
        /**
         * NIVEL 1
         */
        NivelArrastar nivelArrastar = new NivelArrastar();
        nivelArrastar.setJogo(new Jogo());
        nivelArrastar.getJogo().setID(1);
        nivelArrastar.setNumero(1);
        nivelArrastar.setNumTarefas(10);
        nivelArrastar.setFase(new Fase());
        nivelArrastar.getFase().setID(2);
        nivelArrastar.setQtdeEstimulos(2);
        nivelArrastar.setRandPosicao(true);
        nivelArrastar.setLimiteErros(5);
        nivelArrastar.setLimiteSomDeErro(3);
        nivelArrastar.setDataCriacao(new Date());
        nivelArrastar.setUltimaAtualizacao(new Date());
        nivelArrastar.setSincronizado(false);

        List<Posicao> posicoes = new ArrayList<>();

        Posicao posicao = new Posicao();

        posicao.setAltura(0.5f);
        posicao.setLargura(0.5f);
        posicao.setMargemX(0.02f);
        posicao.setMargemY(0.25f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_PRINCIPAL.getObjeto());
        posicao.setDataCriacao(new Date());
        posicao.setUltimaAtualizacao(new Date());
        posicao.setSincronizado(false);
        posicoes.add(posicao);

        posicao = new Posicao();
        posicao.setAltura(0.5f);
        posicao.setLargura(0.5f);
        posicao.setMargemX(0.56f);
        posicao.setMargemY(0.25f);
        posicao.setTipo(EnumObjetoTela.ESTIMULO_ALVO.getObjeto());
        posicoes.add(posicao);

        posicao.setDataCriacao(new Date());
        posicao.setUltimaAtualizacao(new Date());
        posicao.setSincronizado(false);

        nivelArrastar.setPosicoes(posicoes);
        nivelArrastar.setUriImagem("teste");
        return nivelArrastar;

    }

    public static Jogo mockJogo() {
        Paciente p = new Paciente();
        p.setNome("teste");
        p.setDtNascimento(new Date());
        p.setDataCriacao(new Date());
        p.setUltimaAtualizacao(new Date());

        Imagem i = new Imagem();
        i.setNome("teste");
        i.setUri("husfahh");
        i.setUriMascara("hsusau");
        i.setDataCriacao(new Date());
        i.setUltimaAtualizacao(new Date());

        Jogo jogo = new Jogo();

        jogo.setImg(i);
        jogo.setTema("teste");
        jogo.setPaciente(p);

        jogo.setDataCriacao(new Date());
        Estimulo e = new Estimulo();
        e.setID(1);
        e.setDataCriacao(new Date());
        e.setUltimaAtualizacao(new Date());

        List<Estimulo> es = new ArrayList<>();
        es.add(e);

        jogo.setDataCriacao(new Date());
        jogo.setUltimaAtualizacao(new Date());
        jogo.setEstimulos(es);

        return jogo;
    }

    public static Estimulo mockEstimulo() {
        Imagem i = new Imagem();
        i.setNome("teste");
        i.setUri("husfahh");
        i.setUriMascara("hsusau");

        i.setDataCriacao(new Date());
        i.setUltimaAtualizacao(new Date());
        i.setSincronizado(false);

        CategoriaEstimulo categoriaEstimulo = new CategoriaEstimulo();
        categoriaEstimulo.setNome("teste");
        categoriaEstimulo.setDataCriacao(new Date());
        categoriaEstimulo.setUltimaAtualizacao(new Date());
        categoriaEstimulo.setSincronizado(false);

        Audio audio = new Audio();
        audio.setDuracao(4545);
        audio.setUri("hsuha");
        audio.setNome("teste");

        audio.setDataCriacao(new Date());
        audio.setUltimaAtualizacao(new Date());
        audio.setSincronizado(false);

        Estimulo e = new Estimulo();
        e.setAudio(audio);
        e.setCategoria(categoriaEstimulo);
        e.setCodigo("teste");
        e.setGenero("F");
        e.setImagem(i);
        e.setNome("teste");
        e.setDataCriacao(new Date());
        e.setUltimaAtualizacao(new Date());
        e.setSincronizado(false);

        return e;
    }

}
