package br.com.projetoLes.strategies;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.CarrinhoItem;
import br.com.projetoLes.dominio.Livro;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

public class ValidaEstoque {

	@Inject
	private FachadaRegras fachada;
	
	@Transactional
	public void validaEstoque(List<CarrinhoItem> itens) {

		for (CarrinhoItem item : itens) {
			if (item.getQuantidadeAnterior() < item.getQuantidade()) {
				dropEstoque(item.getLivro(), item.getQuantidade() - 1);

			} else if ((item.getQuantidadeAnterior() > item.getQuantidade())) {
				devolveEstoque(item.getLivro(), item.getQuantidadeAnterior() - 1);

			}
			item.setQuantidadeAnterior(item.getQuantidade());
		}
	}

	@Transactional
	public void dropEstoque(Livro livro, int quantidade) {
		livro.getEstoque().setQuantidade(livro.getEstoque().getQuantidade() - quantidade);
		Resultado res = new Resultado();
		res.setEntidade(livro.getEstoque());
		fachada.editar(res);
	}

	@Transactional
	public void devolveEstoque(Livro livro, int quantidade) {
		livro.getEstoque().setQuantidade(livro.getEstoque().getQuantidade() + quantidade);
		Resultado res = new Resultado();
		res.setEntidade(livro.getEstoque());
		fachada.editar(res);
	}

}
