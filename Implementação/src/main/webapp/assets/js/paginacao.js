function trocaIndex(){
	document.getElementById("j_idt6:botaoIndex").click();
}


console.log("Kang Seulgi")
var passo = 10;
var max_item = Number(passo);
var min_item = Number(0);
let totalPedido = Number(document.getElementById("totalPedido").innerHTML);
let totalPaginas = Math.round(totalPedido/passo);
var countPagina = 1;


document.onkeydown = function (e) {
    switch (e.key) {
        case 'ArrowLeft':
        	volta();
            break;
        case 'ArrowRight':
        	avanca();
    }
};

/*<![CDATA[*/
function avanca(){
	if(totalPedido % passo != 0){
		if(countPagina <= totalPaginas){
			max_item += passo ;
			min_item += passo;
			countPagina += 1;
			pag();
		}
	}
	else{
		if(countPagina < totalPaginas){
			max_item += passo;
			min_item += passo;
			countPagina += 1;
			pag();
		}
	}
}

function volta(){
	if(countPagina > 1){
		max_item -= passo;
		min_item -= passo;
		countPagina -= 1;
		pag();
	}
}

window.onload = function() {
	avanca();
	volta();
};

function pag(){
	tabela = document.getElementById("pedidoCliente").getElementsByTagName("tbody")[0].children;
	
	for (var i = 0; i < tabela.length;i++){
		 console.log("i: " + i >= min_item && i < max_item)
		  if(i >= min_item && i < max_item){
			  tabela[i].style.display = "";
		  }else{
			  tabela[i].style.display = "none";
		  }
		}
	
}
/*]]>*/