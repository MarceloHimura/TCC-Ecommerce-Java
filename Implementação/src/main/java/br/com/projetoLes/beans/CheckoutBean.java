package br.com.projetoLes.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Carrinho;
import br.com.projetoLes.dominio.CarrinhoItem;
import br.com.projetoLes.dominio.CartaoCredito;
import br.com.projetoLes.dominio.CartaoPedido;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Cupom;
import br.com.projetoLes.dominio.Endereco;
import br.com.projetoLes.dominio.Livro;
import br.com.projetoLes.dominio.LivroPedido;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.STATUS_PEDIDO;
import br.com.projetoLes.fachada.FachadaRegras;
import br.com.projetoLes.strategies.GeraCupomTroca;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
@RequestScoped
public class CheckoutBean {

	@Inject
	private FachadaRegras fachada;

	private String erros = "";
	private String valoresCartoes;
	private Integer idCli;
	private CartaoCredito cartaoCredito = new CartaoCredito();
	private Pedido pedido = new Pedido();
	private double somaCupom = 0.0;
	private double frete;
	private Cupom cupom = new Cupom();
	private Endereco end = new Endereco();
	private List<Cupom> cupons = new ArrayList<Cupom>();
	private List<Endereco> enderecos = new ArrayList<Endereco>();// set
	private List<CartaoCredito> cartoes = new ArrayList<CartaoCredito>();

	@Transactional
	public String salvar(Cliente cliente, double total, Carrinho carrinho) {
		Resultado res = new Resultado();
		Calendar cale = Calendar.getInstance();
		double valor = Math.abs(total) + frete;

		cale.setTime(cale.getTime());
		valor = (valor - somaCupom) < 0 ? 0 : (valor - somaCupom);

		pedido.setDataAtualizacao(cale);
		pedido.setCliente(cliente);

		if (cupom != null) {
			res = new Resultado();
			res.setEntidade(cupom);
			res.setMensagem(cupom.getId().toString());
			pedido.setCupomDesconto((Cupom) fachada.buscaById(res).getEntidade());
		} else {
			pedido.setCupomDesconto(null);
		}

		cupons.stream().forEach(cp -> System.out.println("id: " + cp.getId()));

		if (!cupons.isEmpty()) {
			List<Cupom> cupons_id = new ArrayList<Cupom>();
			for (Cupom c : cupons) {
				res.setEntidade(c);
				res.setMensagem(c.getId().toString());
				cupons_id.add((Cupom) fachada.buscaById(res).getEntidade());
			}
			pedido.setCupomTroca(cupons_id);
		} else {
			pedido.setCupomTroca(null);
		}

//		pedido.getCupomTroca().stream().forEach(cp -> System.out.println("preço: " + cp.getPreco()));

		res = new Resultado();
		res.setEntidade(end);
		res.setMensagem(end.getId().toString());
		pedido.setEndereco((Endereco) fachada.buscaById(res).getEntidade());
		try {
			for (int i = 0; i < cartoes.size(); i++) {
				CartaoPedido car = new CartaoPedido();
				if (Double.parseDouble(valoresCartoes.split(",")[i]) >= 0) {
					car.setCartao(cartoes.get(i));
					car.setValor(Double.parseDouble(valoresCartoes.split(",")[i]));
				} else {
					erros = "Um dos valores dos cartões é invalido";
					return "/checkout/checkout";
				}
				pedido.getCartoes().add(car);
			}
		} catch (Exception e) {
			erros = "Um dos valores dos cartões é invalido";
			return "/checkout/checkout";
		}

		pedido.setTotal(valor);
		pedido.setFrete(frete);
		for (CarrinhoItem c : carrinho.getItens()) {
			LivroPedido lrp = new LivroPedido();
			lrp.setLivro(c.getLivro());
			lrp.setQuantidade(c.getQuantidade());
			pedido.getItens().add(lrp);

		}

		pedido.setStatusPedido(STATUS_PEDIDO.EM_PROCESSAMENTO);

		res = new Resultado();
		res.setEntidade(pedido);
		erros = fachada.salvar(res).getMensagem();
		if (erros != null) {
			return "/checkout/checkout";
		}

		if (somaCupom > (Math.abs(total) + frete)) {
			res = new Resultado();
			System.out.println("if cupom checkout");
			System.out.println(somaCupom - (Math.abs(total) + frete));
			double cupomValor = Math.floor((somaCupom - (Math.abs(total) + frete)));
			res.setEntidade(GeraCupomTroca.gerarCupom(cupomValor, cliente));
			fachada.salvar(res);
		}

		carrinho.resete();

		if (!cupons.isEmpty()) {
			for (Cupom cupom : pedido.getCupomTroca()) {
				res = new Resultado();
				cupom.setAtivo(false);
				res.setEntidade(cupom);
				fachada.editar(res);
			}
		}

		return "/checkout/confirmaPedido?faces-redirect=true";
	}

	@Transactional
	@Deprecated
	public String umClique(Cliente cliente, Integer id, Carrinho carrinho, boolean logado) {
		if (logado == false) {
			return "/cliente/login?faces-redirect=true";
		}
		Resultado res = new Resultado();
		Calendar cale = Calendar.getInstance();
		cale.setTime(cale.getTime());

		Livro livro = new Livro();
		res.setMensagem(id.toString());
		res.setEntidade(livro);
		livro = (Livro) fachada.buscaById(res).getEntidade();

		pedido.setDataAtualizacao(cale);
		pedido.setCliente(cliente);
		pedido.setTotal(livro.getPreco() + 5);
		pedido.setStatusPedido(STATUS_PEDIDO.EM_PROCESSAMENTO);
		pedido.setCupomDesconto(null);
		pedido.setCupomTroca(null);

		for (Endereco end : cliente.getEnderecos()) {
			if (end.isPrincipal() == true) {
				pedido.setEndereco(end);
				break;
			}
		}

		for (CartaoCredito cc : cliente.getCartoes()) {
			if (cc.isPrincipal() == true) {
				CartaoPedido carp = new CartaoPedido();
				carp.setCartao(cc);
				carp.setValor(livro.getPreco());
				pedido.getCartoes().add(carp);
				break;
			}
		}

		LivroPedido lrp = new LivroPedido();
		res = new Resultado();
		lrp.setLivro(livro);
		lrp.setQuantidade(1);
		pedido.getItens().add(lrp);

		res = new Resultado();
		res.setEntidade(pedido);
		erros = fachada.salvar(res).getMensagem();
		if (erros != null) {
			return "/checkout/checkout";
		}

		carrinho.resete();

		if (!cupons.isEmpty()) {
			for (Cupom cupom : pedido.getCupomTroca()) {
				res = new Resultado();
				cupom.setAtivo(false);
				res.setEntidade(cupom);
				fachada.editar(res);
			}
		}
		return "/checkout/confirmaPedido?faces-redirect=true";
	}

}
