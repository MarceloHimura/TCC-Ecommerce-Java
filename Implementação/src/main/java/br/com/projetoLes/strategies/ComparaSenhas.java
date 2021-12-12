package br.com.projetoLes.strategies;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;

public class ComparaSenhas implements IStrategy {

	@Override
	public String processar(EntidadeDominio ent) {
		Resultado res = (Resultado) ent;
		Cliente cliente = (Cliente) res.getEntidade();

		System.out.println("valida senhas");

		if (cliente.getSenha().getSenha().equals(cliente.getSenha().getConfirmaSenha()))
			return null;
		else
			return "Senhas são diferentes";

	}

}
