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

    public void salvar(Lote lote){
        if (lote.getId() == null){
            em.persist(lote);
        }else {
            em.merge(lote);
        }
    }

    public Optional<Lote> buscarPorId(Long id){
        return Optional.ofNullable(em.find(Lote.class, id));
    }

    public Optional<Lote> buscarPorNumeroLote(String numeroLote){
        String jpql = "SELECT l FROM Lote l WHERE l.numeroLote = :numeroLote";
        TypedQuery<Lote> query = em.createQuery(jpql, Lote.class);
        query.setParameter("numeroLote", numeroLote);

        return query.getResultList().stream().findFirst();
    }

    public List<Lote> buscarTodos(){
        String jpql = "SELECT l FROM Lote l ORDER BY l.nome ASC";
        return em.createQuery(jpql, Lote.class).getResultList();
    }

}
