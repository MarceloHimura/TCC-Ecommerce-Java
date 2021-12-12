function endchk() {
	if (document.getElementById('j_idt9:mesmoEndchk').checked)
		document.getElementById("cobranca").style.display = "none"
	else
		document.getElementById("cobranca").style.display = "block"
	return !document.getElementById('j_idt9:mesmoEndchk').checked
}


window.addEventListener('load',
	function() {
		console.log("KYUMAI FLOWER")
		var ativo = document.getElementsByName("ativo")

		for (var i = 0; i < ativo.length; i++) {
			if (ativo[i].innerHTML == "false")
				ativo[1].style.color = "#ff2400";

			else
				ativo[i].style.color = "black";
		}

	}, false);