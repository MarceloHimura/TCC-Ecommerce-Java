package br.com.projetoLes.dominio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Pedido extends EntidadeDominio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Cliente cliente;

	@ManyToOne
	private Endereco endereco = new Endereco();

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<CartaoPedido> cartoes = new ArrayList<CartaoPedido>();
	
	@OrderBy()
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<LivroPedido> itens = new LinkedHashSet<LivroPedido>();//HashSet

	@Temporal(TemporalType.DATE)
	protected Calendar dataAtualizacao;

	@OneToOne(fetch = FetchType.EAGER)
	private Cupom cupomDesconto = new Cupom();

	@OneToMany()
	private List<Cupom> cupomTroca = new ArrayList<Cupom>();

	private double total;

	private double frete;

	@Enumerated(EnumType.STRING)
	private STATUS_PEDIDO statusPedido;

	public String status() {
		return statusPedido.toString().replace("_", " ");
	}

}
