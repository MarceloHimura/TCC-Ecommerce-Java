package br.com.projetoLes.strategies;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import br.com.projetoLes.dominio.CartaoCredito;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;

public class ValidaCartao implements IStrategy {

	@Override
	public String processar(EntidadeDominio ent) {
		luhnFormula luhnFormula = new luhnFormula();
		Resultado res = new Resultado();
		res = (Resultado) ent;

		if (res.getEntidades().isEmpty()) {
			return null;
		}

		boolean flg = true;

		Date data_ct = null;
		Date data_hj = null;

		Cliente cliente = (Cliente) res.getEntidade();
		for (CartaoCredito cc : cliente.getCartoes()) {

			try {
				data_ct = new SimpleDateFormat("MM/yy").parse(cc.getValidade());
				data_hj = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());

			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (data_hj.after(data_ct)) {
				return "Cartão Expirado";
			}
//			System.out.println(cc.getNumeroCartao());
//			System.out.println("e falso " + !luhnFormula.validate(cc.getNumeroCartao()));
			if (!luhnFormula.validate(cc.getNumeroCartao())) {
				flg = false;

			}
		}

		if (flg) {
			return null;
		} else {
			return "Cartão inválido";
		}

	}

}
