package br.com.projetoLes.strategies;

import br.com.projetoLes.dominio.CartaoPedido;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.dominio.Resultado;

public class ValorCartoes implements IStrategy {

	@Override
	public String processar(EntidadeDominio ent) {
		double somaCartoes = 0;
		Resultado res = (Resultado) ent;
		Pedido pedido = (Pedido) res.getEntidade();
		for (CartaoPedido cc : pedido.getCartoes()) {
			somaCartoes += cc.getValor();
		}
		if (somaCartoes != pedido.getTotal())
			return "A soma dos Cartões e diferente do valor Total "+ "\n";
		else
			return null;
	}

}
