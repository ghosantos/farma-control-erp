package br.com.gustavo.pharma.ejb.services;

import br.com.gustavo.pharma.ejb.dao.LoteDAO;
import br.com.gustavo.pharma.ejb.dao.ProdutoDAO;
import br.com.gustavo.pharma.ejb.entities.Lote;
import br.com.gustavo.pharma.ejb.entities.Produto;
import br.com.gustavo.pharma.shared.dto.LoteCreateDTO;
import br.com.gustavo.pharma.shared.dto.LoteDTO;
import br.com.gustavo.pharma.shared.dto.LoteUpdateDTO;
import br.com.gustavo.pharma.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Testes de Unidade - LoteFacadeBean")
class LoteFacadeBeanTest {

    @Mock
    private LoteDAO loteDAO;

    @Mock
    private ProdutoDAO produtoDAO;

    @InjectMocks
    private LoteFacadeBean loteFacade;

    @Nested
    @DisplayName("Inclusão de Lotes")
    class InclusaoLoteTests {

        @Test
        @DisplayName("Deve incluir lote com sucesso para um produto existente")
        void deveIncluirLoteComSucesso() {
            Long produtoId = 1L;
            LoteCreateDTO dto = new LoteCreateDTO(
                    "LOTE-2026-A",
                    100,
                    LocalDate.now().minusDays(10),
                    LocalDate.now().plusYears(1),
                    produtoId
            );

            Produto produtoSimulado = new Produto();
            produtoSimulado.setId(produtoId);

            when(produtoDAO.buscarPorId(produtoId)).thenReturn(Optional.of(produtoSimulado));
            when(loteDAO.verificaNumeroLoteEmUso(dto.getNumeroLote(), produtoId, null)).thenReturn(false);
            when(loteDAO.incluir(any(Lote.class))).thenAnswer(invocation -> {
                Lote l = invocation.getArgument(0);
                l.setId(10L);
                return l;
            });

            LoteDTO resultado = loteFacade.incluirLote(dto);

            assertNotNull(resultado);
            assertEquals(10L, resultado.getId());
            assertEquals("LOTE-2026-A", resultado.getNumeroLote());
            verify(loteDAO, times(1)).incluir(any(Lote.class));
        }

        @Test
        @DisplayName("Deve lançar DomainException se o produto não for encontrado")
        void deveLancarExcecaoQuandoProdutoNaoExistir() {
            LoteCreateDTO dto = new LoteCreateDTO(
                    "LOTE-001",
                    50,
                    LocalDate.now(),
                    LocalDate.now().plusMonths(6),
                    99L
            );

            when(produtoDAO.buscarPorId(99L)).thenReturn(Optional.empty());

            DomainException ex = assertThrows(DomainException.class, () -> loteFacade.incluirLote(dto));
            assertEquals("O produto que foi vinculado ao lote não existe!", ex.getMessage());
            verify(loteDAO, never()).incluir(any());
        }

        @Test
        @DisplayName("Deve lançar DomainException se o número do lote já existir no mesmo produto")
        void deveLancarExcecaoQuandoNumeroLoteDuplicadoNoMesmoProduto() {
            Long produtoId = 1L;
            LoteCreateDTO dto = new LoteCreateDTO(
                    "LOTE-REPETIDO",
                    50,
                    LocalDate.now(),
                    LocalDate.now().plusMonths(6),
                    produtoId
            );

            Produto produto = new Produto();
            produto.setId(produtoId);

            when(produtoDAO.buscarPorId(produtoId)).thenReturn(Optional.of(produto));
            when(loteDAO.verificaNumeroLoteEmUso(any(), any(), any())).thenReturn(true);

            DomainException ex = assertThrows(DomainException.class, () -> loteFacade.incluirLote(dto));
            assertEquals("Já existe um lote com esse número!", ex.getMessage());
            verify(loteDAO, never()).incluir(any());
        }
    }

    @Nested
    @DisplayName("Atualização e Exclusão de Lotes")
    class AtualizacaoEExclusaoLoteTests {

        @Test
        @DisplayName("Deve atualizar lote com sucesso quando os dados forem válidos")
        void deveAtualizarLoteComSucesso() {
            Long loteId = 10L;
            Long produtoId = 1L;

            Produto produto = new Produto();
            produto.setId(produtoId);

            Lote loteExistente = new Lote("LOTE-ANTIGO", 50, LocalDate.now().minusDays(5), LocalDate.now().plusMonths(6));
            loteExistente.setId(loteId);
            loteExistente.setProduto(produto);

            LoteUpdateDTO dto = new LoteUpdateDTO(
                    "LOTE-NOVO",
                    80,
                    LocalDate.now().minusDays(5),
                    LocalDate.now().plusYears(1)
            );

            when(loteDAO.buscarPorId(loteId)).thenReturn(Optional.of(loteExistente));
            when(loteDAO.verificaNumeroLoteEmUso(dto.getNumeroLote(), produtoId, loteId)).thenReturn(false);

            LoteDTO resultado = loteFacade.atualizarLote(loteId, dto);

            assertNotNull(resultado);
            assertEquals("LOTE-NOVO", resultado.getNumeroLote());
            assertEquals(80, resultado.getQuantidadeAtual());
            verify(loteDAO, times(1)).atualizar(loteExistente);
        }

        @Test
        @DisplayName("Deve deletar lote com sucesso quando o ID for válido")
        void deveDeletarLoteComSucesso() {
            Long loteId = 5L;
            Lote lote = new Lote();
            lote.setId(loteId);

            when(loteDAO.buscarPorId(loteId)).thenReturn(Optional.of(lote));

            loteFacade.deletarLote(loteId);

            verify(loteDAO, times(1)).deletar(lote);
        }

        @Test
        @DisplayName("Deve lançar DomainException ao tentar deletar lote inexistente")
        void deveLancarExcecaoAoDeletarLoteInexistente() {
            Long loteIdInexistente = 99L;
            when(loteDAO.buscarPorId(loteIdInexistente)).thenReturn(Optional.empty());

            DomainException ex = assertThrows(DomainException.class, () -> loteFacade.deletarLote(loteIdInexistente));
            assertEquals("Lote não encontrado!", ex.getMessage());
            verify(loteDAO, never()).deletar(any());
        }
    }

    @Nested
    @DisplayName("Baixa de Estoque e Validade")
    class BaixaEstoqueTests {

        @Test
        @DisplayName("Deve realizar baixa de estoque com sucesso em lote válido")
        void deveRealizarBaixaComSucesso() {
            Long loteId = 5L;
            Lote loteValido = new Lote();
            loteValido.setId(loteId);
            loteValido.setQuantidadeAtual(100);
            loteValido.setDataValidade(LocalDate.now().plusMonths(6));

            when(loteDAO.buscarPorId(loteId)).thenReturn(Optional.of(loteValido));

            loteFacade.darBaixaLote(loteId, 30);

            assertEquals(70, loteValido.getQuantidadeAtual());
            verify(loteDAO, times(1)).atualizar(loteValido);
        }

        @Test
        @DisplayName("Deve impedir baixa de estoque em lote vencido")
        void deveImpedirBaixaEmLoteVencido() {
            Long loteId = 5L;
            Lote loteVencido = new Lote();
            loteVencido.setId(loteId);
            loteVencido.setQuantidadeAtual(100);
            loteVencido.setDataValidade(LocalDate.now().minusDays(1));

            when(loteDAO.buscarPorId(loteId)).thenReturn(Optional.of(loteVencido));

            DomainException ex = assertThrows(DomainException.class, () -> loteFacade.darBaixaLote(loteId, 10));
            assertEquals("Não é possível dar baixa em um lote vencido!", ex.getMessage());
            verify(loteDAO, never()).atualizar(any());
        }

        @Test
        @DisplayName("Deve lançar exceção se a quantidade de baixa for maior que o estoque disponível")
        void deveImpedirBaixaMaiorQueEstoqueDisponivel() {
            Long loteId = 5L;
            Lote loteValido = new Lote();
            loteValido.setId(loteId);
            loteValido.setQuantidadeAtual(20);
            loteValido.setDataValidade(LocalDate.now().plusMonths(3));

            when(loteDAO.buscarPorId(loteId)).thenReturn(Optional.of(loteValido));

            DomainException ex = assertThrows(DomainException.class, () -> loteFacade.darBaixaLote(loteId, 50));
            assertEquals("Estoque insuficiente no lote! Disponível: 20", ex.getMessage());
            verify(loteDAO, never()).atualizar(any());
        }
    }
}