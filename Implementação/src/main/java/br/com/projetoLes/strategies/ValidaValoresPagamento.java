package br.com.projetoLes.strategies;

import br.com.projetoLes.dominio.CartaoPedido;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.dominio.Resultado;

public class ValidaValoresPagamento implements IStrategy {

	@Override
	public String processar(EntidadeDominio ent) {
		Resultado res = (Resultado) ent;
		Pedido ped = (Pedido) res.getEntidade();
		Double total = ped.getTotal();
		if (total <= 10) {
			return null;
		} else {
			for (CartaoPedido c : ped.getCartoes()) {
				if (c.getValor() < 10)
					return "Pagamento abaixo de R$ 10 em um cartão é invalido para esse valor de compra " + "\n";
			}
			return null;
		}
	}

}
