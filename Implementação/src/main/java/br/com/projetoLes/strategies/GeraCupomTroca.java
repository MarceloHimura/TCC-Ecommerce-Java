package br.com.projetoLes.strategies;

import java.util.UUID;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Cupom;

public class GeraCupomTroca {

	private static Cupom cupom = new Cupom();

	public static Cupom gerarCupom(double valor, Cliente cli) {
		cupom = new Cupom();
		cupom.setCodigo(UUID.randomUUID().toString().split("-")[0]);
		cupom.setPreco(valor);
		cupom.setTipoCupom("Troca");
		cupom.setAtivo(true);
		cupom.setCliente(cli);
		return cupom;
	}

	public static Cupom getCupom() {
		return cupom;
	}

	public static void setCupom(Cupom cupom) {
		GeraCupomTroca.cupom = cupom;
	}

}
