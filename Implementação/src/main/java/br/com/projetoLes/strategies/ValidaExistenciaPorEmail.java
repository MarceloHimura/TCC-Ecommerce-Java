package br.com.projetoLes.strategies;

import br.com.projetoLes.daos.Dao;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;

public class ValidaExistenciaPorEmail implements IStrategy {

	@Override
	public String processar(EntidadeDominio ent) {

		Dao dao = new Dao();
		Resultado res = (Resultado) ent;
		Cliente cliente = (Cliente) res.getEntidade();
		
		System.out.println("valida email");

		if (dao.validaExistencia(cliente.getEmail()) > 0)
			return "Email já Cadastrado  ";
		else
			return null;

	}

}
