package br.com.gustavo.pharma.swing.locator;

import br.com.gustavo.pharma.shared.interfaces.LoteFacadeRemote;
import br.com.gustavo.pharma.shared.interfaces.ProdutoFacadeRemote;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJBServiceLocator {

    private static ProdutoFacadeRemote produtoFacade;
    private static LoteFacadeRemote loteFacade;

    public static ProdutoFacadeRemote getProdutoFacade() {
        if (produtoFacade == null) {
            try {
                InitialContext context = new InitialContext();
                String jndi = "ejb:/farma-control-ejb/ProdutoFacadeBean!br.com.gustavo.pharma.shared.interfaces.ProdutoFacadeRemote";

                produtoFacade = (ProdutoFacadeRemote) context.lookup(jndi);
            } catch (NamingException e) {
                throw new RuntimeException("Erro ao buscar ProdutoFacadeRemote no WildFly", e);
            }
        }
        return produtoFacade;
    }

    public static LoteFacadeRemote getLoteFacade() {
        if (loteFacade == null) {
            try {
                InitialContext context = new InitialContext();
                String jndi = "ejb:/farma-control-ejb/LoteFacadeBean!br.com.gustavo.pharma.shared.interfaces.LoteFacadeRemote";

                loteFacade = (LoteFacadeRemote) context.lookup(jndi);
            } catch (NamingException e) {
                throw new RuntimeException("Erro ao buscar LoteFacadeRemote no WildFly", e);
            }
        }
        return loteFacade;
    }

}
