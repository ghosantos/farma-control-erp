package br.com.gustavo.pharma.ejb.dao;

import br.com.gustavo.pharma.ejb.entities.Produto;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Dependent
public class ProdutoDAO {

    @PersistenceContext(unitName = "farmaPU")
    EntityManager em;

    public Produto incluir(Produto produto){
        em.persist(produto);
        return produto;
    }

    public Optional<Produto> buscarPorId(Long id){
        return Optional.ofNullable(em.find(Produto.class, id));
//        String jpql = "SELECT p FROM Produto p WHERE p.id = :id";
//        TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
//        query.setParameter("id", id);
//
//        return query.getResultList().stream().findFirst();
    }

    public List<Produto> buscarTodos(){
        String jpql = "SELECT p FROM Produto p ORDER BY p.nome ASC";
        return em.createQuery(jpql, Produto.class).getResultList();
    }

    public void atualizar(Produto produto){
        em.merge(produto);
    }

    public void deletar(Produto produto){
        em.remove(produto);
    }

    public boolean verificaCodigoBarrasEmUso(String codigo, Long id){
        String jpql = "SELECT COUNT(p) FROM Produto p WHERE p.codigoBarras = :codigo AND (:id IS NULL OR p.id <> :id)";

        Long quantidade = em.createQuery(jpql, Long.class)
                .setParameter("codigo", codigo)
                .setParameter("id", id)
                .getSingleResult();

        return quantidade > 0;
    }

    public boolean verificaNomeEmUso(String nome, Long id){
        String jpql = "SELECT COUNT(p) FROM Produto p WHERE p.nome = :nome AND (:id IS NULL OR p.id <> :id)";

        Long quantidade = em.createQuery(jpql, Long.class)
                .setParameter("nome", nome)
                .setParameter("id", id)
                .getSingleResult();

        return quantidade > 0;
    }
}




