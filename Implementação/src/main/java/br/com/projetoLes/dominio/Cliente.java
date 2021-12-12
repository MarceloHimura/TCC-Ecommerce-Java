package br.com.projetoLes.dominio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cliente extends EntidadeDominio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	private String genero;

	private String telefone;

	private boolean ativo;

	@Enumerated(EnumType.STRING)
	private TIPO_CLIENTE tipoCliente;

	@Email
	private String email;

	@Temporal(TemporalType.DATE)
	private Calendar dataNascimento;

	@Embedded
	private Senha senha = new Senha();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Endereco> enderecos = new ArrayList<Endereco>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Documento> documentos = new HashSet<Documento>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<CartaoCredito> cartoes = new HashSet<CartaoCredito>();

}
