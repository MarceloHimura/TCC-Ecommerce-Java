package br.com.projetoLes.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.http.Part;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.CATEGORIA;
import br.com.projetoLes.dominio.EntidadeDominio;
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
public class LivroSalvarBean {

	@Inject
	private FachadaRegras fachada;
	
	private Livro livro = new Livro();
	private Estoque estoque = new Estoque();
	private Part imagemLivro;
	private String fornecedor;

	private List<Livro> livros = new ArrayList<>();

	@Transactional
	public String salvar() throws ParseException {
		Resultado res = new Resultado();
		livro.setAtivo(true);
		livro.setEstoque(estoque);

		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = Calendar.getInstance().getTime();

		livro.setDataEntrada(dtf.format(now));

		FileSaver fileSaver = new FileSaver();
		livro.setImagemPath(fileSaver.write(imagemLivro, "capas"));
		res = new Resultado();
		res.setEntidade(livro);
		fachada.salvar(res);

		return "/admin/livros/lista?faces-redirect=true";
	}

	@Transactional
	public List<Livro> listar() {

		this.livros = converteLista(fachada.listar(this.livro));

		return livros;
	}

	private List<Livro> converteLista(Resultado listar) {

		List<Livro> lista = new ArrayList<Livro>();

		for (EntidadeDominio e : listar.getEntidades()) {
			Livro c = (Livro) e;
			lista.add(c);
		}

		return lista;
	}

	public CATEGORIA[] getCategoria() {
		return CATEGORIA.values();
	}

}
