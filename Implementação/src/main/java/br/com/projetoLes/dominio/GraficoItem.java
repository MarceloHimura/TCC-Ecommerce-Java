package br.com.projetoLes.dominio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraficoItem {

	private Set<String> datas = new HashSet<String>();
	private Map<String, List<Integer>> label = new HashMap<String, List<Integer>>();
	private Map<String, List<Integer>> data = new HashMap<String, List<Integer>>();
	private Map<Map<String, Set<String>>, Map<String, List<Integer>>> livros = new HashMap<Map<String, Set<String>>, Map<String, List<Integer>>>();
	private int limitador = 0;

}
