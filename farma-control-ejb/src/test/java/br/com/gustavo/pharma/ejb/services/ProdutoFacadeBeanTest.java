package br.com.gustavo.pharma.ejb.services;

import br.com.gustavo.pharma.ejb.dao.ProdutoDAO;
import br.com.gustavo.pharma.ejb.entities.Produto;
import br.com.gustavo.pharma.shared.dto.ProdutoCreateDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoUpdateDTO;
import br.com.gustavo.pharma.shared.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Unidade - ProdutoFacadeBean")
class ProdutoFacadeBeanTest {

    @Mock
    private ProdutoDAO dao;

    @InjectMocks
    private ProdutoFacadeBean produtoFacade;

    @Nested
    @DisplayName("Inclusão de Produtos")
    class InclusaoProdutoTests {

        @Test
        @DisplayName("Deve incluir produto com sucesso quando os dados forem válidos")
        void deveIncluirProdutoComSucesso() {
            ProdutoCreateDTO dto = new ProdutoCreateDTO("Dipirona 500mg", "7891234567890", "Medley", "Dipirona Monoidratada");

            when(dao.verificaNomeEmUso(dto.getNome(), null)).thenReturn(false);
            when(dao.verificaCodigoBarrasEmUso(dto.getCodigoBarras(), null)).thenReturn(false);

            when(dao.incluir(any(Produto.class))).thenAnswer(invocation -> {
                Produto p = invocation.getArgument(0);
                p.setId(1L);
                return p;
            });

            ProdutoDTO resultado = produtoFacade.incluirProduto(dto);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("Dipirona 500mg", resultado.getNome());
            verify(dao, times(1)).incluir(any(Produto.class));
        }

        @Test
        @DisplayName("Deve lançar DomainException se o nome do produto já existir")
        void deveLancarExcecaoQuandoNomeDuplicado() {
            ProdutoCreateDTO dto = new ProdutoCreateDTO("Dipirona 500mg", "7891234567890", "Medley", "Dipirona");

            when(dao.verificaNomeEmUso(dto.getNome(), null)).thenReturn(true);

            DomainException ex = assertThrows(DomainException.class, () -> produtoFacade.incluirProduto(dto));
            assertEquals("Já existe um produto com esse nome", ex.getMessage());
            verify(dao, never()).incluir(any());
        }

        @Test
        @DisplayName("Deve lançar DomainException se o nome estiver em branco")
        void deveLancarExcecaoQuandoNomeEmBranco() {
            ProdutoCreateDTO dto = new ProdutoCreateDTO("", "7891234567890", "Medley", "Dipirona");

            DomainException ex = assertThrows(DomainException.class, () -> produtoFacade.incluirProduto(dto));
            assertEquals("O nome do produto deve ser informado!", ex.getMessage());
            verify(dao, never()).incluir(any());
        }

        @Test
        @DisplayName("Deve lançar DomainException se o código de barras já existir")
        void deveLancarExcecaoQuandoCodigoBarrasDuplicado() {
            ProdutoCreateDTO dto = new ProdutoCreateDTO("Dipirona", "7891234567890", "Medley", "Dipirona");
            when(dao.verificaNomeEmUso(dto.getNome(), null)).thenReturn(false);
            when(dao.verificaCodigoBarrasEmUso(dto.getCodigoBarras(), null)).thenReturn(true);

            DomainException ex = assertThrows(DomainException.class, () -> produtoFacade.incluirProduto(dto));
            assertEquals("Já existe um produto com esse código de barras", ex.getMessage());
            verify(dao, never()).incluir(any());
        }
    }

    @Nested
    @DisplayName("Atualização e Exclusão")
    class AtualizacaoEExclusaoTests {

        @Test
        @DisplayName("Deve atualizar produto com sucesso quando os dados forem válidos")
        void deveAtualizarProdutoComSucesso() {
            Long produtoId = 1L;
            ProdutoUpdateDTO dto = new ProdutoUpdateDTO("Paracetamol 750mg", "7891234567891", "Neo Química", "Paracetamol");

            Produto produtoExistente = new Produto();
            produtoExistente.setId(produtoId);
            produtoExistente.setNome("Paracetamol 500mg");

            when(dao.buscarPorId(produtoId)).thenReturn(Optional.of(produtoExistente));
            when(dao.verificaNomeEmUso(dto.getNome(), produtoId)).thenReturn(false);
            when(dao.verificaCodigoBarrasEmUso(dto.getCodigoBarras(), produtoId)).thenReturn(false);

            ProdutoDTO resultado = produtoFacade.atualizarProduto(produtoId, dto);

            assertNotNull(resultado);
            assertEquals("Paracetamol 750mg", resultado.getNome());
            verify(dao, times(1)).atualizar(produtoExistente);
        }

        @Test
        @DisplayName("Deve lançar DomainException ao tentar atualizar produto com ID nulo")
        void deveLancarExcecaoAoAtualizarComIdNulo() {
            ProdutoUpdateDTO dto = new ProdutoUpdateDTO("Paracetamol", "7891234567891", "Neo Química", "Paracetamol");

            DomainException ex = assertThrows(DomainException.class, () -> produtoFacade.atualizarProduto(null, dto));
            assertEquals("ID null", ex.getMessage());
            verify(dao, never()).atualizar(any());
        }

        @Test
        @DisplayName("Deve deletar produto com sucesso quando ID for válido")
        void deveDeletarProdutoComSucesso() {
            Long produtoId = 1L;
            Produto produto = new Produto();
            produto.setId(produtoId);

            when(dao.buscarPorId(produtoId)).thenReturn(Optional.of(produto));

            produtoFacade.deletarProduto(produtoId);

            verify(dao, times(1)).deletar(produto);
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar por ID inexistente")
        void deveLancarExcecaoQuandoIdNaoEncontrado() {
            when(dao.buscarPorId(99L)).thenReturn(Optional.empty());

            DomainException ex = assertThrows(DomainException.class, () -> produtoFacade.buscarPorId(99L));
            assertEquals("Produto não encontrado", ex.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar com ID nulo")
        void deveLancarExcecaoAoDeletarComIdNulo() {
            DomainException ex = assertThrows(DomainException.class, () -> produtoFacade.deletarProduto(null));
            assertEquals("ID null", ex.getMessage());
        }
    }
}