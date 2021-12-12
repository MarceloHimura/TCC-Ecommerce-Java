package br.com.projetoLes.dominio;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Livro extends EntidadeDominio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	private String isbn;

	private String editora;

	private Integer numPag;

	private String descricao;

	private String autor;

	@Enumerated(EnumType.STRING)
	private CATEGORIA categoria;

	@Min(value = 1)
	private double preco;

	private boolean ativo;

	@OneToOne(cascade = CascadeType.ALL)
	private Estoque estoque;

	private String imagemPath;

	@Min(value = 1)
	private double valorCusto;

	private String fornecedor;

	private String dataEntrada;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Livro other = (Livro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getNomeTela() {
		if (nome.length() > 50)
			return nome.substring(0, 40) + "...";
		else
			return this.nome;
	}

}
