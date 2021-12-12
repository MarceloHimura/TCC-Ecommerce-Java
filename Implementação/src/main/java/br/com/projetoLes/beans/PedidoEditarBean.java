package br.com.projetoLes.beans;

import java.util.ArrayList;
import java.util.List;

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
public class PedidoEditarBean {

	@Inject
	private FachadaRegras fachada;

	@Inject
	private ValidaEstoque valEstoque;

	private Integer id;

	private boolean devolve = true;

	private static Pedido ped = new Pedido();

	private List<LivroPedido> prdpedido = new ArrayList<LivroPedido>();
	private List<PedidoTroca> pedTroca = new ArrayList<PedidoTroca>();

	@Transactional
	public String editar() {
		Resultado res = new Resultado();
		res.setEntidade(ped);
		fachada.editar(res);
		ped = null;
		return "/admin/pedido/lista?faces-redirect=true";
	}

	public String trocaPedido() {
		for (LivroPedido c : prdpedido) {
			if (c.getQuantidade() == null) {
				c.setQuantidade(0);

			}
		}
		ped.setStatusPedido(STATUS_PEDIDO.TROCA_ACEITA);
		System.out.println(prdpedido.size());
		for (LivroPedido l : prdpedido) {
			System.out.println(l.getLivro());
			System.out.println(l.getQuantidade());
		}
		fachada.editar(ped);
		return "/cliente/perfil?faces-redirect=true";
	}

	@Transactional
	public String editarTrocaAceita() {
		Resultado res = new Resultado();
		double totalTrocados = 0.;

		carregarTroca();
		for (PedidoTroca troca : pedTroca) {
			for (LivroPedido livroEstoque : ped.getItens()) {
				if (troca.getPrdId() == livroEstoque.getLivro().getId()) {
					if(troca.getQuantidade() == null) troca.setQuantidade(0);
					System.out.println("livroEstoque.getLivro().getPreco()"+livroEstoque.getLivro().getPreco());
					System.out.println("troca.getQuantidade()"+troca.getQuantidade());
					totalTrocados += livroEstoque.getLivro().getPreco() * troca.getQuantidade();
					if (devolve) {
						livroEstoque.setQuantidade(Math.abs(troca.getQuantidade() - livroEstoque.getQuantidade()));
						res.setEntidade(livroEstoque);
						fachada.editar(res);
						valEstoque.devolveEstoque(livroEstoque.getLivro(), troca.getQuantidade());
					}
				}

			}
			troca.setEmAberto(false);
			res.setEntidade(troca);
			fachada.editar(res);
		}

		ped.setStatusPedido(STATUS_PEDIDO.TROCA_EFETUADA);
		res.setEntidade(ped);
		fachada.editar(res);
		double valorCupom = Math.floor(totalTrocados);

		System.out.println("valor cupom: " + valorCupom);

		res = new Resultado();
		res.setEntidade(GeraCupomTroca.gerarCupom(valorCupom, ped.getCliente()));
		fachada.salvar(res);
		return "/admin/pedido/lista?faces-redirect=true";
	}

	@Transactional
	public String editarCancelamentoAceito() {
		Resultado res = new Resultado();
		double totalTrocados = 0.;

		for (LivroPedido livroEstoque : ped.getItens()) {
			totalTrocados += livroEstoque.getLivro().getPreco() * livroEstoque.getQuantidade();
			valEstoque.devolveEstoque(livroEstoque.getLivro(), livroEstoque.getQuantidade());
			livroEstoque.setQuantidade(0);
			res.setEntidade(livroEstoque);
			fachada.editar(res);
		}

		ped.setStatusPedido(STATUS_PEDIDO.CANCELAMENTO_ACEITO);
		res.setEntidade(ped);
		fachada.editar(res);
		double valorCupom = Math.floor(totalTrocados);

		System.out.println("valor cupom:" + valorCupom);

		res = new Resultado();
		res.setEntidade(GeraCupomTroca.gerarCupom(valorCupom, ped.getCliente()));
		fachada.salvar(res);

		return "/admin/pedido/lista?faces-redirect=true";
	}

	@Transactional
	public String editarTrocaNegada() {
		ped.setStatusPedido(STATUS_PEDIDO.TROCA_REJEITADA);
		Resultado res = new Resultado();
		res.setEntidade(ped);
		fachada.editar(res);
		return "/admin/pedido/lista?faces-redirect=true";
	}

	@Transactional
	public String editarCancelamentoNegado() {
		ped.setStatusPedido(STATUS_PEDIDO.CANCELAMENTO_REJEITADO);
		Resultado res = new Resultado();
		res.setEntidade(ped);
		fachada.editar(res);
		return "/admin/pedido/lista?faces-redirect=true";
	}

	@Transactional
	public String cancelaPedido() {
		Resultado res = new Resultado();
		ped.setStatusPedido(STATUS_PEDIDO.CANCELAMENTO_SOLICITADO);
		res.setEntidade(ped);
		fachada.editar(res);
		ped = null;
		return "/cliente/perfil?faces-redirect=true";
	}

	public STATUS_PEDIDO[] getStatusPedido() {
		return STATUS_PEDIDO.values();
	}

	public void prdpedido() {
		prdpedido = new ArrayList<LivroPedido>(ped.getItens());
	}

	public void carregar() {
		Resultado res = new Resultado();
		res.setEntidade(new Pedido());
		res.setMensagem(id.toString());
		PedidoEditarBean.ped = (Pedido) fachada.buscaById(res).getEntidade();
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

	public boolean cancelavel() {
		if (ped.getStatusPedido() == STATUS_PEDIDO.CANCELAMENTO_SOLICITADO
				|| ped.getStatusPedido() == STATUS_PEDIDO.CANCELAMENTO_REJEITADO
				|| ped.getStatusPedido() == STATUS_PEDIDO.CANCELAMENTO_ACEITO
				|| ped.getStatusPedido() == STATUS_PEDIDO.CANCELAMENTO_EFETUADO
				|| ped.getStatusPedido() == STATUS_PEDIDO.TROCA_SOLICITADA
				|| ped.getStatusPedido() == STATUS_PEDIDO.TROCA_EFETUADA
				|| ped.getStatusPedido() == STATUS_PEDIDO.TROCA_REJEITADA
				|| ped.getStatusPedido() == STATUS_PEDIDO.TROCA_ACEITA
				|| ped.getStatusPedido() == STATUS_PEDIDO.ENTREGA_REALIZADA) {

			return false;
		} else {
			return true;
		}
	}

	public Pedido getPed() {
		return ped;
	}

	public void setPed(Pedido ped) {
		PedidoEditarBean.ped = ped;
	}
}
