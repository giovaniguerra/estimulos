package br.com.estimulos.app.interfaces;

import java.util.List;
import java.util.Map;

import br.com.estimulos.dominio.EntidadeDominio;

/**
 * Created by Caio Gustavo on 05/04/2016.
 */
public interface InterfaceDAO <K, T extends EntidadeDominio> {

    /**
     * Salva os objetos no banco de dados
     *
     * @param object Uma EntidadeDominio
     * @return A chave gerada
     * @throws Exception Se ocorrer algum erro
     */
    public long salvar(T object) throws Exception;

    /**
     * Altera os objetos no banco
     *
     * @param object Uma EntidadeDominio
     * @throws Exception Se Ocorrer algum erro
     */
    public void alterar(T object) throws Exception;

    /**
     * Remove os objetos do banco de dados
     *
     * @param object Um EntidadeDominio
     * @return
     * @throws Exception Se Ocorrer algum erro
     */
    public int remover(T object) throws Exception;

    /**
     * Busca uma entidade no banco de dados
     *
     * @param key Chave de identificacao do objeto
     * @return Uma EntidadeDominio
     * @throws Exception Se Ocorrer Algum erro
     */
    public T visualizar(K key) throws Exception;

    /**
     * Consulta todos os objetos no banco de dados
     *
     * @return Uma lista de EntidadeDominio
     * @throws Exception Se Ocorrer algum erro
     */
    public List<T> consultarTodos() throws Exception;

    /**
     * Filtra os objetos no banco de dados
     *
     * @param filters Um mapa de parametros
     * @return Uma lista de EntidadeDominio
     * @throws Exception Se Ocorrer algum erro
     */
    public List<T> pesquisa(Map<String, String> filters) throws Exception;

    /**
     * Retorna o numero de objetos cadastrados no banco de dados
     *
     * @return Um numero inteiro
     * @throws Exception Se Ocorrer algum erro
     */
    public int count() throws Exception;

    /**
     * Retorna o sql de criacao da tabela para a entidade especifica
     *
     * @param versao Numero da versao
     * @return String de criacao da tabela
     */
    public String getCreateBancoDadosSQL(int versao);

    /**
     * Faz a atualizacao da versao de uma tabela
     *
     * @param vsAntiga da versao antiga, vsNova da nova versao
     * @return SQL para atualizao da tabela
     */
    public String getUpdateBancoDadosSQL(int vsAntiga, int vsNova);

}