package br.com.gustavo.pharma.ejb.dao;

import br.com.gustavo.pharma.ejb.entities.Lote;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Dependent
public class LoteDAO {

    @PersistenceContext(unitName = "farmaPU")
    EntityManager em;

    public Lote incluir(Lote lote){
        em.persist(lote);
        return lote;
    }

    public Optional<Lote> buscarPorId(Long id){
        return Optional.ofNullable(em.find(Lote.class, id));
    }

    public List<Lote> buscarTodos(){
        String jpql = "SELECT l From Lote l ORDER BY l.dataValidade ASC";
        return em.createQuery(jpql, Lote.class).getResultList();
    }

    // Metodo responsável por buscar todos os lotes vinculados ao produto selecionado.
    public List<Lote> buscarLotesPorProduto(Long produtoId){
        String jpql = "SELECT l FROM Lote l WHERE l.produto.id = :produtoId ORDER BY l.dataValidade ASC";
        return em.createQuery(jpql, Lote.class)
                .setParameter("produtoId", produtoId)
                .getResultList();
    }

    public void atualizar(Lote lote){
        em.merge(lote);
    }

    public void deletar(Lote lote){
        em.remove(lote);
    }

    public Optional<Lote> buscarPorNumeroLote(String numeroLote){
        String jpql = "SELECT l FROM Lote l WHERE l.numeroLote = :numeroLote";
        TypedQuery<Lote> query = em.createQuery(jpql, Lote.class);
        query.setParameter("numeroLote", numeroLote);

        return query.getResultList().stream().findFirst();
    }

    /**
     * Verifica se já existe um lote cadastrado com o mesmo número para um produto específico.
     *
     * <p>Esta consulta utiliza uma técnica de filtro dinâmico no {@code WHERE} com a expressão
     * {@code (:idIgnorar IS NULL OR l.id <> :idIgnorar)} para atender dois cenários distintos:</p>
     *
     * <ul>
     *   <li><b>Inclusão (Novo Lote):</b> Ao passar {@code idIgnorar = null}, a primeira parte da expressão
     *       é avaliada como {@code TRUE}, fazendo com que a consulta pesquise duplicidade em todos os lotes
     *       cadastrados para o produto informado.</li>
     *
     *   <li><b>Atualização (Edição de Lote):</b> Ao passar o ID do lote que está sendo editado (ex: {@code idIgnorar = 10}),
     *       a consulta ignora o próprio registro durante a verificação. Isso evita um falso positivo onde o lote
     *       colidiria com seus próprios dados ao salvar alterações secundárias (como validade ou quantidade).</li>
     * </ul>
     *
     * @param numeroLote número do lote a ser verificado.
     * @param produtoId  ID do produto ao qual o lote pertence (escopo do produto).
     * @param idIgnorar  ID do lote a ser desconsiderado na busca (usado em atualizações),
     *                   ou {@code null} caso se trate de um novo cadastro (inclusão).
     * @return {@code true} se já existir outro lote com esse número para o mesmo produto;
     *         {@code false} caso o número esteja livre para uso.
     */
    public boolean verificaNumeroLoteEmUso(String numeroLote, Long produtoId, Long idIgnorar){
        String jpql = """
                SELECT COUNT(l)
                FROM Lote l
                WHERE l.numeroLote = :numeroLote
                AND l.produto.id = :produtoId
                AND (:idIgnorar IS NULL OR l.id <> :idIgnorar)
                """;

        Long quantidade = em.createQuery(jpql, Long.class)
                .setParameter("numeroLote", numeroLote)
                .setParameter("produtoId", produtoId)
                .setParameter("idIgnorar", idIgnorar)
                .getSingleResult();

        return quantidade > 0;
    }
}
