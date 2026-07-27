package br.com.gustavo.pharma.ejb.services;

import br.com.gustavo.pharma.ejb.dao.ProdutoDAO;
import br.com.gustavo.pharma.ejb.entities.Produto;
import br.com.gustavo.pharma.ejb.mappers.ProdutoMapper;
import br.com.gustavo.pharma.shared.dto.ProdutoCreateDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoUpdateDTO;
import br.com.gustavo.pharma.shared.exceptions.DomainException;
import br.com.gustavo.pharma.shared.interfaces.ProdutoFacadeRemote;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class ProdutoFacadeBean implements ProdutoFacadeRemote {

    @Inject
    ProdutoDAO dao;

    @Override
    public ProdutoDTO incluirProduto(ProdutoCreateDTO dto) {
        validarInclusaoProduto(dto);

        Produto produto = ProdutoMapper.toEntity(dto);
        produto = dao.incluir(produto);
        return ProdutoMapper.toDTO(produto);
    }

    @Override
    public ProdutoDTO buscarPorId(Long id){
        Produto produto = dao.buscarPorId(id).orElseThrow(() -> new DomainException("Produto não encontrado"));

        return ProdutoMapper.toDTO(produto);
    }

    @Override
    public List<ProdutoDTO> buscarTodos(){
        List<Produto> produtos = dao.buscarTodos();

        return produtos.stream()
                .map(p -> ProdutoMapper.toDTO(p))
                .toList();
    }

    @Override
    public ProdutoDTO atualizarProduto(Long id, ProdutoUpdateDTO dto){
        if (id == null) {
            throw new DomainException("ID null");
        }

        Produto produto = dao.buscarPorId(id).orElseThrow(() -> new DomainException("Produto não encontrado"));

        validarAtualizacaoProduto(dto, id);
        aplicarAlteracoes(produto, dto);

        dao.atualizar(produto);

        return ProdutoMapper.toDTO(produto);
    }

    @Override
    public void deletarProduto(Long id){
        if (id == null){
            throw new DomainException("ID null");
        }

        Produto produto = dao.buscarPorId(id).orElseThrow(() -> new DomainException("Produto não encontrado"));
        dao.deletar(produto);
    }


    // Métodos auxiliares

    private void validarCamposObrigatorios(String nome, String codigoBarras, String fabricante, String principioAtivo){

        if (nome == null || nome.isBlank()) {
            throw new DomainException("O nome do produto deve ser informado!");
        }

        if (codigoBarras == null || codigoBarras.isBlank()) {
            throw new DomainException("O código de barras deve ser informado!");
        }

        if (fabricante == null || fabricante.isBlank()) {
            throw new DomainException("O fabricante deve ser informado!");
        }

        if (principioAtivo == null || principioAtivo.isBlank()) {
            throw new DomainException("O princípio ativo deve ser informado!");
        }
    }

    /**
     * Valida se já existe outro produto cadastrado com o mesmo nome ou
     * código de barras.
     *
     * <p>Na inclusão, o parâmetro {@code idIgnorar} deve ser {@code null},
     * fazendo com que a validação considere todos os produtos cadastrados.
     * Na atualização, deve ser informado o ID do produto que esta sendo
     * alterado, para que ele seja desconsiderado na verificação de
     * duplicidade.</p>
     *
     * @param nome nome do produto.
     * @param codigoBarras código de barras do produto.
     * @param idIgnorar ID do produto que deve ser desconsiderado na validação,
     *                  ou {@code null} quando se tratar de uma inclusão.
     * @throws DomainException caso já exista outro produto com o mesmo nome
     *                         ou código de barras.
     */
    private void validarDuplicidade(String nome, String codigoBarras, Long idIgnorar){
        if (dao.verificaNomeEmUso(nome, idIgnorar)) {
            throw new DomainException("Já existe um produto com esse nome");
        }

        if (dao.verificaCodigoBarrasEmUso(codigoBarras, idIgnorar)){
            throw new DomainException("Já existe um produto com esse código de barras");
        }
    }

    private void validarInclusaoProduto(ProdutoCreateDTO dto){
        if (dto == null) {
            throw new DomainException("Os dados do produto devem ser informados!");
        }

        validarCamposObrigatorios(dto.getNome(), dto.getCodigoBarras(), dto.getFabricante(), dto.getPrincipioAtivo());
        validarDuplicidade(dto.getNome(), dto.getCodigoBarras(), null);
    }

    private void validarAtualizacaoProduto(ProdutoUpdateDTO dto, Long id){
        if (dto == null) {
            throw new DomainException("Os dados do produto devem ser informados!");
        }

        validarCamposObrigatorios(dto.getNome(), dto.getCodigoBarras(), dto.getFabricante(), dto.getPrincipioAtivo());
        validarDuplicidade(dto.getNome(), dto.getCodigoBarras(), id);
    }

    private void aplicarAlteracoes(Produto produto, ProdutoUpdateDTO dto){
        produto.setNome(dto.getNome());
        produto.setFabricante(dto.getFabricante());
        produto.setCodigoBarras(dto.getCodigoBarras());
        produto.setPrincipioAtivo(dto.getPrincipioAtivo());
    }
}
