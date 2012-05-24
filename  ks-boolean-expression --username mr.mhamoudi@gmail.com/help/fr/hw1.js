addDOMLoadEvent(hwTopicOnLoad);
function hwTopicOnLoad(){Nifty("div.info-note,div.info-beta,div.info-astuce,div.info-alerte,div.info-valide,div.info-info,div.info-avertissement","smooth  normal all");
Nifty("div#header-top","smooth  normal transparent top");
var vtip_formsubmit=document.getElementById('tip_form'); if (vtip_formsubmit) vtip_formsubmit.onsubmit=f2;
var vdfocus=document.getElementById('d'); if (vdfocus) vdfocus.onfocus=f3;
var oA = document.createElement('a');oA.setAttribute('href', '#');var oTxtA = document.createTextNode("Imprimer cette page");oA.appendChild(oTxtA);oA.onclick = function() { window.print(); return false; };var oCont = document.getElementById('impression');if(!oCont) return;oCont.appendChild(oA);
var vListListPageNumchange=document.getElementById('ListListPageNum'); if (vListListPageNumchange) vListListPageNumchange.onchange=f5;
}
function f2(){search_form(this);return false;}
function f3(){if (d_first){this.value='';d_first = false;};}
function f5(){window.location.href=document.getElementById('FormListPageNum').ListListPageNum.options[document.getElementById('FormListPageNum').ListListPageNum.selectedIndex].value;}
