package br.com.projetoLes.strategies;

import java.util.ArrayList;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.TIPO_DOCUMENTO;

public class ValidaCPF implements IStrategy {

	private static final int TAMANHO_CPF = 11;
	private static final int[] PESOS_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	@Override
	public String processar(EntidadeDominio ent) {
		System.out.println("valida cpf");
		Resultado res = new Resultado();
		Documento doc = new Documento();
		res = (Resultado) ent;
		if (res.getEntidade().getClass().getName() == Cliente.class.getName()) {
			Cliente cliente = (Cliente) res.getEntidade();
			doc = new ArrayList<Documento>(cliente.getDocumentos()).get(0);
			System.out.println("classe cliente");
		} else {
			doc = (Documento) res.getEntidade();
		}

		if (doc.getTipoDocumento().equals(TIPO_DOCUMENTO.CPF)) {
			if (cpfValido(doc.getCodigo().toString())) {
				return null;
			} else {
				return "CPF INVÁLIDO";
			}
		} else {
			return null;
		}
	}

	public boolean cpfValido(String cpf) {
		if (cpf == null) {
			return false;
		}
		if (cpf.length() != TAMANHO_CPF) {
			return false;
		}
		if (cpf.matches(cpf.charAt(0) + "{" + TAMANHO_CPF + "}")) {
			return false;
		}
		String digitos = cpf.substring(0, TAMANHO_CPF - 2);
		String verificador1 = verificador(digitos, PESOS_CPF);
		String verificador2 = verificador(digitos + verificador1, PESOS_CPF);
		return (digitos + verificador1 + verificador2).equals(cpf);
	}

	private String verificador(String digitos, int[] pesos) {
		int soma = 0;
		int qtdPesos = pesos.length;
		int qtdDigitos = digitos.length();
		for (int posicao = qtdDigitos - 1; posicao >= 0; posicao--) {
			int digito = Character.getNumericValue(digitos.charAt(posicao));
			soma += digito * pesos[qtdPesos - qtdDigitos + posicao];
		}
		soma = 11 - soma % 11;
		return String.valueOf(soma > 9 ? 0 : soma);
	}
}
