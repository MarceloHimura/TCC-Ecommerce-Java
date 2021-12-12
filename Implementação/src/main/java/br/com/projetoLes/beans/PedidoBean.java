package br.com.projetoLes.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.LivroPedido;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.dominio.PedidoTroca;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.STATUS_PEDIDO;
import br.com.projetoLes.fachada.FachadaRegras;
import br.com.projetoLes.strategies.GeraCupomTroca;
import br.com.projetoLes.strategies.ValidaEstoque;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
@SessionScoped
public class PedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private boolean troca = false;
	private boolean cancela = false;
	private boolean normal = true;
	private boolean devolve = true;

	private String motivo;
	
	@Inject
	private FachadaRegras fachada;

	private Pedido ped = new Pedido();
	private List<LivroPedido> prdpedido = new ArrayList<LivroPedido>();
	private List<PedidoTroca> pedTroca = new ArrayList<PedidoTroca>();
	private int totalPedidos = 0;

	public void setMaxPedido(Integer cliId) {
		Resultado res = new Resultado();
		res.setEntidade(new Pedido());
		res.setMensagem(cliId.toString());
		totalPedidos = fachada.listarPorCliente(res).getEntidades().size();
	}

	public void prdpedido() {
		prdpedido = ped.getItens().stream().collect(Collectors.toList());
//		for(LivroPedido lp :) {
//			prdpedido.add(lp);
//		}
	}

	public String trocaPedido() {
		Resultado res = new Resultado();

		System.out.println("prdpedido"+prdpedido);
		
		
		for (LivroPedido l : prdpedido) {
			Resultado transporte = new Resultado();
			PedidoTroca temp = new PedidoTroca();
			temp.setPedId(ped.getId());
			temp.setPrdId(l.getLivro().getId());
			temp.setQuantidade(l.getQuantidade());
			temp.setImagemPath(l.getLivro().getImagemPath());
			temp.setEmAberto(true);
			temp.setMotivo(motivo);
			transporte.setEntidade(temp);
			fachada.salvar(transporte);
		}

		carregar();
		ped.setStatusPedido(STATUS_PEDIDO.TROCA_SOLICITADA);
		ped.getItens().stream().forEach(item -> System.out.println(item.getQuantidade()));
		System.out.println("ped.getItens()"+ped.getItens());
		res.setEntidade(ped);
		fachada.editar(res);
		return "/cliente/perfil?faces-redirect=true";
	}

	public void status(Pedido p) {
		troca = false;
		cancela = false;
		normal = true;
		if (p.getStatusPedido() == STATUS_PEDIDO.TROCA_SOLICITADA) {
			troca = true;
			normal = false;
		}
		if (p.getStatusPedido() == STATUS_PEDIDO.CANCELAMENTO_SOLICITADO) {
			cancela = true;
			normal = false;
		}
	}

	@Transactional
	public String editarTrocaAceita() {
		Resultado res = new Resultado();
		ValidaEstoque valEstoque = new ValidaEstoque();
		Double totalTrocados = 0.;

		for (PedidoTroca troca : pedTroca) {

			for (LivroPedido livroEstoque : ped.getItens()) {

				if (troca.getPrdId() == livroEstoque.getLivro().getId()) {

					totalTrocados += livroEstoque.getLivro().getPreco() * troca.getQuantidade();
					if (devolve) {
						livroEstoque.setQuantidade(Math.abs(troca.getQuantidade() - livroEstoque.getQuantidade()));
						res.setEntidade(livroEstoque);
						fachada.editar(res);
						valEstoque.devolveEstoque(livroEstoque.getLivro(), troca.getQuantidade());
					}
				}

			}

		}
		ped.setStatusPedido(STATUS_PEDIDO.TROCA_ACEITA);
		res.setEntidade(ped);
		fachada.editar(res);
		double valorCupom = Math.floor(totalTrocados);

		res.setEntidade(GeraCupomTroca.gerarCupom(valorCupom, ped.getCliente()));
		fachada.salvar(res);
		res = new Resultado();
		return "/admin/pedido/lista?faces-redirect=true";
	}

	@Transactional
	public String editarTrocaNegada() {

		ped.setStatusPedido(STATUS_PEDIDO.TROCA_REJEITADA);
		fachada.editar(ped);
		return "/admin/pedido/lista?faces-redirect=true";
	}

	@Transactional
	public String editarCancelamentoNegado() {

		ped.setStatusPedido(STATUS_PEDIDO.TROCA_REJEITADA);
		fachada.editar(ped);
		return "/admin/pedido/lista?faces-redirect=true";
	}

	public List<Integer> estoqueMax(Integer max) {
		List<Integer> lista = new ArrayList<Integer>();
		if (max == null) {
			max = 0;
		}

		for (int i = 1; i <= max; i++) {
			lista.add(i);
		}
		return lista;
	}

	public boolean entregue() {
		if (ped.getStatusPedido().equals(STATUS_PEDIDO.ENTREGA_REALIZADA)) {
			return true;
		} else {
			return false;
		}
	}

	public Integer getIndex(LivroPedido item) {
		return new ArrayList<>(ped.getItens()).indexOf(item);
	}

	public void carregar() {
		Resultado res = new Resultado();
		res.setEntidade(new Pedido());
		res.setMensagem(id.toString());
		ped = (Pedido) fachada.buscaById(res).getEntidade();
	}

	public void carregarTroca() {
		Resultado res = new Resultado();
		res.setEntidade(new PedidoTroca());
		res.setMensagem(id.toString());
		pedTroca = converteLista(fachada.getTroca(res));

	}

	private List<PedidoTroca> converteLista(Resultado listar) {

		List<PedidoTroca> lista = new ArrayList<PedidoTroca>();

		for (EntidadeDominio e : listar.getEntidades()) {
			lista.add((PedidoTroca) e);
		}

		return lista;
	}

}
