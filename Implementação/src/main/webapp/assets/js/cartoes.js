//function trocarCartao(){
//	document.getElementById("a").classList.toggle("d-none");
//	document.getElementById("b").classList.toggle("d-none");
//}


//transfere pro campo q o jsf vai usar
function juntar() {
	var valores = "";
	var card = document.getElementById("cartoes").childNodes[0].getElementsByTagName("td");
	for (var i = 0; i < card.length; i++) {
		if (card[i].getElementsByTagName("input")[1].value != "") {
			valores += card[i].getElementsByTagName("input")[1].value.toString() + ",";
		}
	}
	document.getElementById("j_idt7:valoresCartoes").value = valores;
	console.log(document.getElementById("j_idt7:valoresCartoes").value)
}

//cria os campos
function criacao() {
	console.log("Erza Scarlet")
	var card = document.getElementById("cartoes").childNodes[0].getElementsByTagName("td")
	document.getElementById("frete").innerHTML = "R$ 0.0"

	for (var i = 0; i < card.length; i++) {
		var imp = document.createElement("input");
		imp.setAttribute("disabled", false)
		imp.setAttribute("type", "text")
		imp.setAttribute("class", "form-control")
		imp.setAttribute("onChange", "total()")
		card[i].appendChild(imp)
	}

	if (card.length == 1) {
		card[0].getElementsByTagName("input")[0].checked = true;
		card[0].getElementsByTagName("input")[1].value = document.getElementById("totalFrete").innerHTML
		card[0].firstElementChild.setAttribute("type", "radio")
	}
	somaCupom()
	getFrete()
}

//abri input no cartão
function ligaDesliga() {
	var card = document.getElementById("cartoes").childNodes[0].getElementsByTagName("td")
	if (card.length == 1) {

		return null;
	}

	for (var i = 0; i < card.length; i++) {
		if (card[i].getElementsByTagName("input")[0].checked) {
			card[i].getElementsByTagName("input")[1].disabled = false;
		} else {
			card[i].getElementsByTagName("input")[1].disabled = true;
		}
	}
}


//pega o frete
function getFrete() {
	console.log("Kang Seulgi")
	var ip = document.getElementById("valorFrete").getElementsByTagName("td")
	for (var i = 0; i < ip.length; i++) {
		if (ip[i].getElementsByTagName("input")[0].checked) {
			document.getElementById("frete").innerHTML = "R$ "+ip[i].innerHTML.split("R$")[1].split("<")[0]
			console.log(ip[i].innerHTML.split("R$")[1].split("<")[0]);
		}
	}
	somaCupom()
}


function troca() {

	var card = document.getElementById("cartoes").childNodes[0].getElementsByTagName("td")
	if (card.length == 1) {
		card[0].getElementsByTagName("input")[1].value = document.getElementById("totalFrete").innerHTML
	}
	total()
}


function total() {
	console.log("nice nature")
	var card = document.getElementById("cartoes").childNodes[0].getElementsByTagName("td")
	var soma = 0;
	for (var i = 0; i < card.length; i++) {
		var val = parseFloat(card[i].getElementsByTagName("input")[1].value)
		isNaN(val) ? soma += 0 : soma += val
	}
	if (soma == document.getElementById("totalFrete").innerHTML) {
		document.getElementById("thumbsUp").style.display = "block"
		document.getElementById("thumbsDown").style.display = "none";
		console.log("onnanoko")
	}
	else {
		document.getElementById("thumbsDown").style.display = "block"
		document.getElementById("thumbsUp").style.display = "none";
		console.log("jyousei")
	}
}

//function listaCartao(){
//	console.log("entrou");
//	document.getElementById("listaCartao").classList.toggle("d-none");
//}


//document.getElementById("botaoCartao").addEventListener("click", listaCartao);

//function doisCartoes() {
//	var cartao = document.getElementById("j_idt6:cartao").getElementsByTagName("input");
//	var limit = 2;
//	for (var i = 0; i < cartao.length; i++) {
//		cartao[i].onclick = function() {
//			var checkedcount = 0;
//				for (var i = 0; i < cartao.length; i++) {
//				checkedcount += (cartao[i].checked) ? 1 : 0;
//			}
//			if (checkedcount > limit) {
//				this.checked = false;
//			}
//		}
//	}
//}

//function doisCartoesSelcionados(){
//	var cartao = document.getElementById("j_idt6:cartao").getElementsByTagName("input");
//	var min = 2;
//	var soma =0;
//	for (var i = 0; i < cartao.length; i++) {
//		if(cartao[i].checked){
//			soma += 1;
//		}
//	}
//	if(soma != min){
//		alert("Selecione 2 cartões de crédito");
//	}
//}

function somaCupom() {
	console.log("Yeji, Yuna")
	var frete = document.getElementById("frete").innerHTML.split("R$")[1];
	var produto = document.getElementById("totalProduto").innerHTML.split(" ")[1];
	var total = 0;
	total = parseFloat(frete) + parseFloat(produto);
	var cupomTroca = document.getElementById("j_idt7:cupomTroca").getElementsByTagName("input")
	var cupomDesconto = document.getElementById("j_idt7:cupomDesconto").getElementsByTagName("input");
	var soma_troca = 0;
	var soma_desc = 0;
	// loop over them all
	for (var i = 0; i < cupomTroca.length; i++) {
		// And stick the checked ones onto an array...
		if (cupomTroca[i].checked) {
			soma_troca = + document.getElementById(cupomTroca[i].id).nextSibling.innerHTML.split("R$")[1];

		}
		if ((soma_desc + soma_troca) > (total * 1.2)) {
			alert("Os Valores dos cupons ultrapassam  o valor máximo de " + (total * 1.2).toFixed(2) + " remova algum cupom");
			document.getElementById(cupomTroca[i].id).checked = false;
			document.getElementById("totalFrete").innerHTML = parseFloat(frete) + parseFloat(produto) - soma_troca
			return total;
		}

	}

	for (var i = 0; i < cupomDesconto.length; i++) {
		// And stick the checked ones onto an array...
		if (cupomDesconto[i].checked) {
			soma_desc = + document.getElementById(cupomDesconto[i].id).nextSibling.innerHTML.split("R$")[1];

		}
		if ((soma_desc + soma_troca) > (total * 1.2)) {
			alert("Os Valores dos cupons ultrapassam  o valor máximo de " + (total * 1.2).toFixed(2) + " remova algum cupom");
			document.getElementById(cupomDesconto[i].id).checked = false;
			document.getElementById("totalFrete").innerHTML = parseFloat(frete) + parseFloat(produto) - soma_troca
			document.getElementById("cupomTotal").innerHTML = "R$ " +soma_troca
			return total;
		}
	}
	soma_cupom = parseFloat(soma_desc + soma_troca).toFixed(2);
	document.getElementById("j_idt7:somaCupom").value = soma_cupom;
	document.getElementById("cupomTotal").innerHTML = "R$ " + soma_cupom;

	document.getElementById("totalFrete").innerHTML = ((total - soma_cupom).toFixed(2) < 0) ? 0.00 : (total - soma_cupom).toFixed(2);
	troca();

	return soma_desc + soma_troca
}


