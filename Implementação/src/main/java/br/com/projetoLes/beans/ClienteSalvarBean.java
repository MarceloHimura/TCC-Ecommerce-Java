package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.BANDEIRA;
import br.com.projetoLes.dominio.CartaoCredito;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.ESTADOS;
import br.com.projetoLes.dominio.Endereco;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.Senha;
import br.com.projetoLes.dominio.TIPO_CLIENTE;
import br.com.projetoLes.dominio.TIPO_DOCUMENTO;
import br.com.projetoLes.dominio.TIPO_ENDERECO;
import br.com.projetoLes.fachada.FachadaRegras;
import br.com.projetoLes.seguranca.CriptografaSenha;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class ClienteSalvarBean {

	@Inject
	private HttpServletRequest req;

	@Inject
	private FachadaRegras fachada;

	private Cliente cliente = new Cliente();
	private Endereco endereco = new Endereco();
	private Endereco enderecoDois = new Endereco();
	private Documento documento = new Documento();
	private CartaoCredito cartao = new CartaoCredito();
	private String dataNascimento;
	private Senha senha = new Senha();
	private boolean cpfError;
	private boolean mesmoEnd = false;
	private String erros;

	Resultado resultado = new Resultado();

	@Transactional
	public String salvar() {

		if (senha.getConfirmaSenha().equals(senha.getSenha())) {

			CriptografaSenha crp = new CriptografaSenha();

			cliente.getSenha().setSenha(crp.criptoSenha(senha.getSenha()));

			cliente.setAtivo(true);

			cliente.setTipoCliente(TIPO_CLIENTE.Basico);

			cliente.getDocumentos().add(documento);

			endereco.setTipoEndereco(TIPO_ENDERECO.ENTREGA);
			endereco.setPrincipal(true);

			if (mesmoEnd) {
				endereco.setTipoEndereco(TIPO_ENDERECO.ENTREGA_E_COBRANCA);

				cliente.getEnderecos().add(endereco);
			} else {
				enderecoDois.setTipoEndereco(TIPO_ENDERECO.COBRANCA);

				cliente.getEnderecos().add(endereco);
				cliente.getEnderecos().add(enderecoDois);
			}

			cartao.setPrincipal(true);
			cliente.getCartoes().add(cartao);

			Resultado res = new Resultado();
			res.setEntidade(cliente);
			res.add(new Cliente());
			resultado = fachada.salvar(res);
			System.out.println(resultado.getMensagem());

			if (resultado.getMensagem() == null) {
				HttpSession session = req.getSession(false);
				session.setAttribute("cliente", cliente);
				session.setAttribute("cli_nome", cliente.getNome());
				session.setAttribute("cli_id", cliente.getId());
				session.setAttribute("status", true);

				return "/cliente/perfil?faces-redirect=true";
			} else {
				setErros(resultado.getMensagem());
				System.out.println(erros);
				return "/cliente/form.xhtml";
			}

		} else {
			setErros("Senhas não estão iguais");
			System.out.println(erros);
			return "/cliente/form.xhtml";

		}

	}

	public TIPO_ENDERECO[] getTipos() {
		return TIPO_ENDERECO.values();
	}

	public TIPO_DOCUMENTO[] getDocumentos() {
		return TIPO_DOCUMENTO.values();
	}

	public BANDEIRA[] getBandeiras() {
		return BANDEIRA.values();
	}

	public ESTADOS[] getEstados() {
		return ESTADOS.values();
	}

}
