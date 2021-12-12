package br.com.projetoLes.beans;

import java.util.regex.Pattern;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.Senha;
import br.com.projetoLes.dominio.TIPO_CLIENTE;
import br.com.projetoLes.fachada.FachadaRegras;
import br.com.projetoLes.seguranca.CriptografaSenha;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class ClienteEditarBean {

	@Inject
	private FachadaRegras fachada;

	private Senha senha = new Senha();
	private boolean senhaErro = false;
	private static Cliente cliente = new Cliente();
	private CriptografaSenha crp = new CriptografaSenha();
	private String senhaAtual;
	private static String erro = "";

	public String redirAdmin(Cliente cli) {
		cliente = cli;
		return "/admin/cliente/editar";
	}
	
	@Transactional
	public void ativaCliente(Cliente cli) {
		Resultado res = new Resultado();
		cli.setAtivo(true);
		res.setEntidade(cli);
		fachada.editar(res);
	}
	
	@Transactional
	public void desativaCliente(Cliente cli) {
		Resultado res = new Resultado();
		cli.setAtivo(false);
		res.setEntidade(cli);
		fachada.editar(res);
	}

	@Transactional
	public String editarCli(Cliente cli) {
		Resultado res = new Resultado();
		res.setEntidade(cli);
		fachada.editar(res);
		return "/cliente/perfil?faces-redirect=true";
	}

	@Transactional
	public String atualizaSenha(Cliente cli) {
		Resultado res = new Resultado();
		senhaAtual = crp.criptoSenha(senhaAtual);
		erro = null;
		

		if (cli.getSenha().getSenha().equals(senhaAtual)) {

			
			if (!senha.getSenha().equals(senha.getConfirmaSenha())) {
				erro = "As novas senhas não estão iguais";
				System.out.println(erro);
				return "/cliente/senha/form?faces-redirect=true";
			}
			
			cli.getSenha().setSenha(crp.criptoSenha(senha.getSenha()));
			res.setEntidade(cli);
			fachada.editar(res);
			
			return "/cliente/perfil?faces-redirect=true";
		} else {
			erro = "Senha atual incorreta";
			return "/cliente/senha/form?faces-redirect=true";
		}

	}

	@Transactional
	public String editar() {
		Resultado res = new Resultado();
		if (!senha.getSenha().equals("")) {
			if (Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{8,13}",
					senha.getSenha())) {

				senha.setSenha(crp.criptoSenha(senha.getSenha()));
				cliente.setSenha(senha);

			} else {
				senhaErro = true;
				return "/admin/cliente/editar?faces-redirect=true";
			}
		}
		res.setEntidade(cliente);
		fachada.editar(res);
		cliente = null;
		return "/admin/cliente/lista?faces-redirect=true";

	}

	public TIPO_CLIENTE[] getTipoCliente() {

		return TIPO_CLIENTE.values();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		ClienteEditarBean.cliente = cliente;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		ClienteEditarBean.erro = erro;
	}

}
