package br.com.projetoLes.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.projetoLes.dominio.GraficoItem;
import br.com.projetoLes.dominio.LivroPedido;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class GraficoBean implements Serializable {

	@Inject
	private FachadaRegras fachada;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static final long serialVersionUID = 1L;
	private static String min = null;
	private static String max = null;
	private boolean vazio = false;
	private static String filtro = "livro";
	private LinkedHashSet<String> datas = new LinkedHashSet<String>();
	private Set<String> nomes = new HashSet<String>();
	private Map<String, List<Integer>> label = new HashMap<String, List<Integer>>();
	private List<Integer> data = new ArrayList<Integer>();
	GraficoItem gi = new GraficoItem();
	private int limitador;

	@PostConstruct
	public void init() {
		createAreaModel();
	}

	private void createAreaModel() {
		List<Pedido> itens = fachada.grafico();
		
		for (Pedido i : itens) {
			datas.add(dateFormat.format(i.getDataAtualizacao().getTime()).toString());
			for (LivroPedido c : i.getItens()) {
				if (filtro.equals("livro"))
					nomes.add(c.getLivro().getNome());
				else
					nomes.add(c.getLivro().getCategoria().toString());
			}
		}

		for (int i = 0; i < datas.size(); i++) {
			data.add(0);
		}
		for (String n : nomes) {
			List<Integer> copy = new ArrayList<>(data);
			label.put(n, copy);
		}

		for (int i = 0; i < datas.size(); i++) {
			for (Pedido p : itens) {
				String datas_texto = dateFormat.format(p.getDataAtualizacao().getTime()).toString();
				if (datas.toArray()[i].equals(datas_texto)) {
					for (LivroPedido c : p.getItens()) {
						if (filtro.equals("livro")) {
							int prev = label.get(c.getLivro().getNome()).get(i);
							int soma = prev + c.getQuantidade();
							label.get(c.getLivro().getNome()).set(i, soma);
							soma = 0;
							prev = 0;
						} else {
							int prev = label.get(c.getLivro().getCategoria().toString()).get(i);
							int soma = prev + c.getQuantidade();
							label.get(c.getLivro().getCategoria().toString()).set(i, soma);
							soma = 0;
							prev = 0;
						}

					}
				}
			}
		}
//		for (String s : nomes)
//			System.out.println(label.get(s).size());
		gi.setLabel(label);
		gi.setDatas(datas);
		gi.setLimitador(limitador);

	}

	public void createAreaModel_2() throws ParseException {
		nomes = new HashSet<String>();
		datas = new LinkedHashSet<String>();
		label = new HashMap<String, List<Integer>>();
		data = new ArrayList<Integer>();

		List<Pedido> itens = fachada.grafico();
		if (max.isEmpty() || max == null) {
			max = LocalDate.now().toString();// "2100-12-31"
		}
		if (min.isEmpty() || min == null) {
			int mes = LocalDate.now().minusMonths(1).lengthOfMonth();
			int dia = LocalDate.now().getDayOfMonth()-1;
			min = LocalDate.now().minusDays(mes + dia).toString();// "1980-12-31";
		}
		
		Date data_min = new SimpleDateFormat("yyyy-MM-dd").parse(min);
		Date data_max = new SimpleDateFormat("yyyy-MM-dd").parse(max);

		if(data_max.before(data_min)) {
			max = "";
			min = "";
			return;
		}
		
		System.out.println(data_max);
		for (Pedido i : itens) {
			Date cal = i.getDataAtualizacao().getTime();
			if (data_min.before(cal) && data_max.after(cal)) {
				datas.add(dateFormat.format(i.getDataAtualizacao().getTime()).toString());
				for (LivroPedido c : i.getItens()) {
					if (filtro.equals("livro"))
						nomes.add(c.getLivro().getNome());
					else
						nomes.add(c.getLivro().getCategoria().toString());
				}
			}
		}

		if(datas.size()  == 0) {
			vazio = true;
			return;
		}
		
		for (int i = 0; i < datas.size(); i++) {
			data.add(0);
		}
		for (String n : nomes) {
			List<Integer> copy = new ArrayList<>(data);
			label.put(n, copy);
		}

		for (int i = 0; i < datas.size(); i++) {
			for (Pedido p : itens) {
				String datas_texto = dateFormat.format(p.getDataAtualizacao().getTime()).toString();
				if (datas.toArray()[i].equals(datas_texto)) {
					for (LivroPedido c : p.getItens()) {
						if (filtro.equals("livro")) {
							int prev = label.get(c.getLivro().getNome()).get(i);
							int soma = prev + c.getQuantidade();
							label.get(c.getLivro().getNome()).set(i, soma);
							soma = 0;
							prev = 0;
						} else {
							int prev = label.get(c.getLivro().getCategoria().toString()).get(i);
							int soma = prev + c.getQuantidade();
							label.get(c.getLivro().getCategoria().toString()).set(i, soma);
							soma = 0;
							prev = 0;
						}

					}
				}
			}
		}
//		for (String s : nomes)
//			System.out.println(s + " " + label.get(s).size());
		gi.setLabel(label);
		gi.setDatas(datas);
		gi.setLimitador(limitador);
		if (max == LocalDate.now().toString())// "2100-12-31")
			max = "";
		if (min == LocalDate.now().minusDays(LocalDate.now().minusMonths(1).lengthOfMonth()).toString())// "1980-12-31")
			min = "";

	}

	public void setData() {
		try {
			createAreaModel_2();
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("erro");
		}
	}

	public String getJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(gi);
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		GraficoBean.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		GraficoBean.max = max;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		GraficoBean.filtro = filtro;
	}

	public int getLimitador() {
		return limitador;
	}

	public void setLimitador(int limitador) {
		this.limitador = limitador;
	}

	public boolean isVazio() {
		return vazio;
	}

	public void setVazio(boolean vazio) {
		this.vazio = vazio;
	}

}
