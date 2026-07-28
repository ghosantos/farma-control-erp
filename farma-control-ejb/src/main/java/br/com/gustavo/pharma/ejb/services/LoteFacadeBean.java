package br.com.gustavo.pharma.ejb.services;

import br.com.gustavo.pharma.ejb.dao.LoteDAO;
import br.com.gustavo.pharma.ejb.dao.ProdutoDAO;
import br.com.gustavo.pharma.ejb.entities.Lote;
import br.com.gustavo.pharma.ejb.entities.Produto;
import br.com.gustavo.pharma.ejb.mappers.LoteMapper;
import br.com.gustavo.pharma.shared.dto.LoteCreateDTO;
import br.com.gustavo.pharma.shared.dto.LoteDTO;
import br.com.gustavo.pharma.shared.dto.LoteUpdateDTO;
import br.com.gustavo.pharma.shared.exceptions.DomainException;
import br.com.gustavo.pharma.shared.exceptions.EstoqueInsuficienteException;
import br.com.gustavo.pharma.shared.exceptions.LoteVencidoException;
import br.com.gustavo.pharma.shared.interfaces.LoteFacadeRemote;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class LoteFacadeBean implements LoteFacadeRemote {

    @Inject
    LoteDAO dao;

    @Inject
    private ProdutoDAO produtoDAO;

    @Override
    public LoteDTO incluirLote(LoteCreateDTO dto){
        validarInclusaoLote(dto);

        Produto produto = produtoDAO.buscarPorId(dto.getProdutoId())
                .orElseThrow(() -> new DomainException("O produto que foi vinculado ao lote não existe!"));

        Lote lote = LoteMapper.toEntity(dto);
        lote.setProduto(produto);

        lote = dao.incluir(lote);
        return LoteMapper.toDTO(lote);
    }

    @Override
    public LoteDTO buscarPorId(Long id) {
        Lote lote = dao.buscarPorId(id)
                .orElseThrow(() -> new DomainException("Lote não encontrado!"));

        return LoteMapper.toDTO(lote);
    }

    @Override
    public List<LoteDTO> buscarTodos() {
        List<Lote> lotes = dao.buscarTodos();

        return lotes.stream()
                .map(l -> LoteMapper.toDTO(l))
                .toList();
    }

    @Override
    public List<LoteDTO> buscarPorProdutoId(Long produtoId) {
        if (produtoId == null){
            throw new DomainException("ID null");
        }

        List<Lote> lotes = dao.buscarLotesPorProduto(produtoId);

        return lotes.stream()
                .map(l -> LoteMapper.toDTO(l))
                .toList();
    }

    @Override
    public LoteDTO atualizarLote(Long id, LoteUpdateDTO dto) {
        if (id == null){
            throw new DomainException("ID null");
        }

        Lote lote = dao.buscarPorId(id)
                .orElseThrow(() -> new DomainException("Lote não encontrado!"));

        validarAtualizacaoLote(dto, lote.getProduto().getId(), id);
        aplicarAtualizacoes(lote, dto);

        dao.atualizar(lote);

        return LoteMapper.toDTO(lote);
    }

    @Override
    public void deletarLote(Long id) {
        if (id == null){
            throw new DomainException("ID null");
        }

        Lote lote = dao.buscarPorId(id)
                .orElseThrow(() -> new DomainException("Lote não encontrado!"));

        dao.deletar(lote);
    }

    @Override
    public LoteDTO darBaixaLote(Long loteId, Integer quantidade) {
        if (loteId == null){
            throw new DomainException("ID null");
        }

        if (quantidade == null || quantidade <= 0) {
            throw new DomainException("A quantidade para baixa deve ser maior que zero!");
        }

        Lote lote = dao.buscarPorId(loteId)
                .orElseThrow(() -> new DomainException("Lote não encontrado!"));

        if (lote.getDataValidade().isBefore(LocalDate.now()) || lote.getDataValidade().isEqual(LocalDate.now())){
            throw new LoteVencidoException("Não é possível dar baixa em um lote vencido!");
        }

        if (lote.getQuantidadeAtual() < quantidade) {
            throw new EstoqueInsuficienteException("Estoque insuficiente no lote! Disponível: " + lote.getQuantidadeAtual());
        }

        lote.setQuantidadeAtual(lote.getQuantidadeAtual() - quantidade);
        dao.atualizar(lote);

        return LoteMapper.toDTO(lote);
    }

    // Métodos auxiliares

    private void validarCamposObrigatorios(String numeroLote, Integer quantidadeAtual,
                                           LocalDate dataFabricacao, LocalDate dataValidade){

        if (numeroLote == null || numeroLote.isBlank()){
            throw new DomainException("O número do lote deve ser informado!");
        }

        if (quantidadeAtual == null || quantidadeAtual < 0){
            throw new DomainException("A quantidade deve ser informada e não pode ser negativa!");
        }

        if (dataFabricacao == null){
            throw new DomainException("A data de fabricação deve ser informada!");
        }

        if (dataValidade == null){
            throw new DomainException("A data de validade deve ser informada!");
        }

        if (dataFabricacao.isAfter(LocalDate.now())){
            throw new DomainException("A data de fabricação não pode ser uma data futura!");
        }

        if (dataValidade.isBefore(dataFabricacao) || dataValidade.isEqual(dataFabricacao)){
            throw new DomainException("A data de validade deve ser posterior à data de fabricação!");
        }
    }

    /**
     * Valida se já existe outro lote cadastrado com o mesmo número para o mesmo produto.
     *
     * @param numeroLote número do lote a ser verificado.
     * @param produtoId  ID do produto vinculado ao lote.
     * @param idIgnorar  ID do lote a ser desconsiderado na verificação (usado na edição),
     *                   ou {@code null} quando for inclusão.
     * @throws DomainException caso já exista outro lote com o mesmo número para este produto.
     */
    private void validarDuplicidade(String numeroLote, Long produtoId, Long idIgnorar){
        if (dao.verificaNumeroLoteEmUso(numeroLote, produtoId, idIgnorar)){
            throw new DomainException("Já existe um lote com esse número!");
        }
    }

    private void validarInclusaoLote(LoteCreateDTO dto){
        if (dto == null) {
            throw new DomainException("Os dados do lote devem ser informados!");
        }

        validarCamposObrigatorios(dto.getNumeroLote(), dto.getQuantidadeAtual(), dto.getDataFabricacao(), dto.getDataValidade());
        validarDuplicidade(dto.getNumeroLote(), dto.getProdutoId(), null);
    }

    private void validarAtualizacaoLote(LoteUpdateDTO dto, Long produtoId, Long id){
        if (dto == null) {
            throw new DomainException("Os dados do lote devem ser informados!");
        }

        validarCamposObrigatorios(dto.getNumeroLote(), dto.getQuantidadeAtual(), dto.getDataFabricacao(), dto.getDataValidade());
        validarDuplicidade(dto.getNumeroLote(), produtoId, id);
    }

    private void aplicarAtualizacoes(Lote lote, LoteUpdateDTO dto){
        lote.setNumeroLote(dto.getNumeroLote());
        lote.setQuantidadeAtual(dto.getQuantidadeAtual());
        lote.setDataFabricacao(dto.getDataFabricacao());
        lote.setDataValidade(dto.getDataValidade());
    }

}
