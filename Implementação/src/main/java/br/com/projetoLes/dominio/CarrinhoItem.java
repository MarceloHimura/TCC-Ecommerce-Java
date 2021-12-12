package br.com.projetoLes.dominio;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarrinhoItem {

	private Livro livro;
	private Integer quantidade;
	int max;
	private int quantidadeAnterior = 1;

	public CarrinhoItem(Livro livro) {
		this.livro = livro;
		this.quantidade = 1;
		max = livro.getEstoque().getQuantidade();
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public List<Integer> estoqueMax() {
		List<Integer> lista = new ArrayList<Integer>();
		for (int i = 1; i <= max; i++) {
			lista.add(i);
		}
		return lista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((livro == null) ? 0 : livro.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarrinhoItem other = (CarrinhoItem) obj;
		if (livro == null) {
			if (other.livro != null)
				return false;
		} else if (!livro.equals(other.livro))
			return false;
		return true;
	}

}
