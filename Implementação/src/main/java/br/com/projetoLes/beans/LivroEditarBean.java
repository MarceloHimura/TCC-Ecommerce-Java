package br.com.projetoLes.beans;

import java.text.ParseException;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.http.Part;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.CATEGORIA;
import br.com.projetoLes.dominio.Estoque;
import br.com.projetoLes.dominio.Livro;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import br.com.projetoLes.infra.FileSaver;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class LivroEditarBean {

	@Inject
	private FachadaRegras fachada;

	private Livro livro = new Livro();
	private static Estoque estoque = new Estoque();
	private Integer id;
	private Part imagemCapa;

	@Transactional
	public String salvar() throws ParseException {
		Resultado res = new Resultado();
		System.out.println(estoque.getId());
		livro.setEstoque(estoque);

		if (imagemCapa != null) {
			FileSaver fileSaver = new FileSaver();
			livro.setImagemPath(fileSaver.write(imagemCapa, "cartas"));
		}

		livro.setAtivo(true);

		res.setEntidade(livro);

		fachada.editar(res);

		return "/admin/livros/lista?faces-redirect=true";
	}

	@Transactional
	public void ativaLivro(Livro livro) {
		Resultado res = new Resultado();
		livro.setAtivo(true);
		livro.setEstoque(estoque);
		res.setEntidade(livro);
		fachada.editar(res);
	}

	public String redir(Livro livro) {
		this.livro = livro;
		estoque = livro.getEstoque();
		return "/admin/livros/alterar.xhtml";
	}

	public void carregaDetalhe() {
		Resultado res = new Resultado();
		Resultado msg = new Resultado();
		msg.setMensagem(id.toString());
		res.add(livro);
		res.add(msg);
		this.livro = (Livro) fachada.buscaById(res).getEntidade();
		estoque = livro.getEstoque();
	}

	public CATEGORIA[] getCategoria() {
		return CATEGORIA.values();
	}

	public Estoque getEstoque() {
		return estoque;
	}

	public void setEstoque(Estoque estoque) {
		LivroEditarBean.estoque = estoque;
	}

}
