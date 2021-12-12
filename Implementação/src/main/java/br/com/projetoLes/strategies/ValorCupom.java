package br.com.projetoLes.strategies;

import br.com.projetoLes.dominio.Cupom;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.LivroPedido;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.dominio.Resultado;

public class ValorCupom implements IStrategy {

	@Override
	public String processar(EntidadeDominio ent) {
		System.out.println("entro valor cupom");
		Resultado res = (Resultado) ent;
		Pedido pedido = (Pedido) res.getEntidade();
		if (pedido.getCupomDesconto() == null && pedido.getCupomTroca() == null)
			return null;
		double total = 0.;
		for (LivroPedido lp : pedido.getItens()) {
			total += (lp.getQuantidade() * lp.getLivro().getPreco());
		}
		total += pedido.getFrete();
//		if (pedido.getTotal() == 0)
//			return null;
//		else
//			total = pedido.getTotal();

		double desc = 0;
		double troca = 0;

		try {
			desc = pedido.getCupomDesconto().getPreco();
		} catch (Exception e) {
			desc = 0;
		}

		try {
			for (Cupom c : pedido.getCupomTroca()) {
				troca += c.getPreco();
			}
		} catch (Exception e) {
			troca = 0;
		}
		System.out.println("troca " + troca);
		System.out.println("desc " + desc);
		System.out.println("total " + total);
		if ((troca + desc) > (total * 1.2))
			return "Valores dos cupons acima do valor aceitável de " + (total * 1.2) + "\n";
		else
			return null;
	}

}
