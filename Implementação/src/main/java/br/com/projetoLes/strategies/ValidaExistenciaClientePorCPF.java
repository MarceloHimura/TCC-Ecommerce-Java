package br.com.projetoLes.strategies;

import br.com.projetoLes.daos.Dao;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.TIPO_DOCUMENTO;

public class ValidaExistenciaClientePorCPF implements IStrategy {

	@Override
	public String processar(EntidadeDominio ent) {

		Dao dao = new Dao();
		Resultado res = (Resultado) ent;
		Cliente cliente = (Cliente) res.getEntidade();

		System.out.println("valida existencia de cpf cliente");
		
		for (Documento d : cliente.getDocumentos()) {
			if (d.getTipoDocumento().equals(TIPO_DOCUMENTO.CPF)) {
				if (dao.validaExistencia(d.getCodigo()) > 0) 
					return "Cpf já Cadastrado  ";
				else
					return null;
			}
		}
		return null;
	}
}
