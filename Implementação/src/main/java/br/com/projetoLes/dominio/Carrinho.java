package br.com.projetoLes.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.com.projetoLes.fachada.FachadaRegras;

@Named
@SessionScoped
public class Carrinho implements Serializable {

	private static final long serialVersionUID = 1L;

	private Set<CarrinhoItem> itens = new LinkedHashSet<>();

	private Pedido pedido = new Pedido();

	private double frete;

	public void finalizar(Pedido pedido) {
		FachadaRegras fachada = new FachadaRegras();
		pedido.setTotal(getTotal());
		fachada.salvar(pedido);
	}

	public void add(CarrinhoItem item) {
		item.setQuantidadeAnterior(item.getQuantidade());
		itens.add(item);
	}

	public List<CarrinhoItem> getItens() {
		return new ArrayList<CarrinhoItem>(itens);
	}

	public Double getTotal(CarrinhoItem item) {
		return item.getLivro().getPreco() * item.getQuantidade();
	}
	
	public Double getTotal() {
		Double total = 0.00;

		for (CarrinhoItem carrinhoItem : itens) {
			total += carrinhoItem.getLivro().getPreco() * carrinhoItem.getQuantidade();
		}

		return total;
	}

	public void resete() {
		this.itens = new HashSet<>();
	}

	public void remover(CarrinhoItem item) {
		this.itens.remove(item);
	}

	public Integer getQuantidadeTotal() {
		return itens.stream().mapToInt(item -> item.getQuantidade()).sum();
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public double getFrete() {
		return frete;
	}

	public void setFrete(double frete) {
		this.frete = frete;
	}
}
