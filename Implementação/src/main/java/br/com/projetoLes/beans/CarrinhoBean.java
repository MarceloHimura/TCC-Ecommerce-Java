package br.com.projetoLes.beans;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Carrinho;
import br.com.projetoLes.dominio.CarrinhoItem;
import br.com.projetoLes.dominio.Livro;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import br.com.projetoLes.strategies.ValidaEstoque;

@Model
public class CarrinhoBean {

	@Inject
	private Carrinho carrinho;
	
	@Inject
	private FachadaRegras fachada;
	
	@Inject
	private ValidaEstoque validaestoque;

	public String add(Integer id) {
		Resultado res = new Resultado();
		Livro livro = new Livro();
		res.setMensagem(id.toString());
		res.setEntidade(livro);
		livro = (Livro) fachada.buscaById(res).getEntidade();
		CarrinhoItem item = new CarrinhoItem(livro);
		validaestoque.dropEstoque(livro, 1);
		carrinho.add(item);
		return "index?faces-redirect=true";

	}

	public String comprar(Integer id, boolean logado){
		Resultado res = new Resultado();
		Livro livro = new Livro();
		res.setMensagem(id.toString());
		res.setEntidade(livro);
		livro = (Livro) fachada.buscaById(res).getEntidade();
		CarrinhoItem item = new CarrinhoItem(livro);
		validaestoque.dropEstoque(livro, 1);
		carrinho.add(item);
		if (logado == false) {
			return "/cliente/loginCheckout?faces-redirect=true";
		}
		return "/checkout/checkout?faces-redirect=true";
	}
	
	public List<CarrinhoItem> getItens() {
		return carrinho.getItens();
	}

	@Transactional
	public String redirCheckout(List<CarrinhoItem> itens, boolean logado) {
		if (logado == false) {
			return "/cliente/loginCheckout?faces-redirect=true";
		}

		validaestoque.validaEstoque(itens);
		return "/checkout/checkout?faces-redirect=true";

	}

	public void carrinhoCheck(List<CarrinhoItem> itens) {
		validaestoque.validaEstoque(itens);
	}

	@Transactional
	public void remover(CarrinhoItem item) {
		validaestoque.devolveEstoque(item.getLivro(), item.getQuantidadeAnterior());
		carrinho.remover(item);
	}

}
