function MM_swapImgRestore() {
if (document.MM_swapImgData != null)
for (var i=0; i<(document.MM_swapImgData.length-1); i+=2)
document.MM_swapImgData[i].src = document.MM_swapImgData[i+1];}
function MM_preloadImages() {
if (document.images) {
var imgFiles = MM_preloadImages.arguments;
if (document.preloadArray==null) document.preloadArray = new Array();
var i = document.preloadArray.length;
with (document) for (var j=0; j<imgFiles.length; j++) if (imgFiles[j].charAt(0)!="#"){
preloadArray[i] = new Image;
preloadArray[i++].src = imgFiles[j];}}}
function MM_swapImage() {
var i,j=0,objStr,obj,swapArray=new Array,oldArray=document.MM_swapImgData;
for (i=0; i < (MM_swapImage.arguments.length-2); i+=3) {
objStr = MM_swapImage.arguments[(navigator.appName == 'Netscape')?i:i+1];
if ((objStr.indexOf('document.layers[')==0 && document.layers==null) ||
(objStr.indexOf('document.all[')   ==0 && document.all   ==null))
objStr = 'document'+objStr.substring(objStr.lastIndexOf('.'),objStr.length);
obj = document.getElementById(objStr);
if (obj != null) {
swapArray[j++] = obj;
swapArray[j++] = (oldArray==null || oldArray[j-1]!=obj)?obj.src:oldArray[j];
obj.src = MM_swapImage.arguments[i+2];}}
document.MM_swapImgData = swapArray;}
/* niftycube.js */
/* Nifty Corners Cube - rounded corners with CSS and Javascript
Copyright 2006 Alessandro Fulciniti (a.fulciniti@html.it) */

var niftyOk=(document.getElementById && document.createElement && Array.prototype.push);
var niftyCss=false;

String.prototype.find=function(what){
return(this.indexOf(what)>=0 ? true : false);
}

var oldonload=window.onload;
if(typeof(NiftyLoad)!='function') NiftyLoad=function(){};
if(typeof(oldonload)=='function')
    window.onload=function(){oldonload();AddCss();NiftyLoad()};
else window.onload=function(){AddCss();NiftyLoad()};

function AddCss(){
niftyCss=true;
var l=CreateEl("link");
l.setAttribute("type","text/css");
l.setAttribute("rel","stylesheet");
l.setAttribute("href","niftyCube.css");
l.setAttribute("media","screen");
document.getElementsByTagName("head")[0].appendChild(l);
}

function Nifty(selector,options){
if(niftyOk==false) return;
if(niftyCss==false) AddCss();
var i,v=selector.split(","),h=0;
if(options==null) options="";
if(options.find("fixed-height"))
    h=getElementsBySelector(v[0])[0].offsetHeight;
for(i=0;i<v.length;i++)
    Rounded(v[i],options);
if(options.find("height")) SameHeight(selector,h);
}

function Rounded(selector,options){
var i,top="",bottom="",v=new Array();
if(options!=""){
    options=options.replace("left","tl bl");
    options=options.replace("right","tr br");
    options=options.replace("top","tr tl");
    options=options.replace("bottom","br bl");
    options=options.replace("transparent","alias");
    if(options.find("tl")){
        top="both";
        if(!options.find("tr")) top="left";
        }
    else if(options.find("tr")) top="right";
    if(options.find("bl")){
        bottom="both";
        if(!options.find("br")) bottom="left";
        }
    else if(options.find("br")) bottom="right";
    }
if(top=="" && bottom=="" && !options.find("none")){top="both";bottom="both";}
v=getElementsBySelector(selector);
for(i=0;i<v.length;i++){
    FixIE(v[i]);
    if(top!="") AddTop(v[i],top,options);
    if(bottom!="") AddBottom(v[i],bottom,options);
    }
}

function AddTop(el,side,options){
var d=CreateEl("b"),lim=4,border="",p,i,btype="r",bk,color;
d.style.marginLeft="-"+getPadding(el,"Left")+"px";
d.style.marginRight="-"+getPadding(el,"Right")+"px";
if(options.find("alias") || (color=getBk(el))=="transparent"){
    color="transparent";bk="transparent"; border=getParentBk(el);btype="t";
    }
else{
    bk=getParentBk(el); border=Mix(color,bk);
    }
d.style.background=bk;
d.className="niftycorners";
p=getPadding(el,"Top");
if(options.find("small")){
    d.style.marginBottom=(p-2)+"px";
    btype+="s"; lim=2;
    }
else if(options.find("big")){
    d.style.marginBottom=(p-10)+"px";
    btype+="b"; lim=8;
    }
else d.style.marginBottom=(p-5)+"px";
for(i=1;i<=lim;i++)
    d.appendChild(CreateStrip(i,side,color,border,btype));
el.style.paddingTop="0";
el.insertBefore(d,el.firstChild);
}

function AddBottom(el,side,options){
var d=CreateEl("b"),lim=4,border="",p,i,btype="r",bk,color;
d.style.marginLeft="-"+getPadding(el,"Left")+"px";
d.style.marginRight="-"+getPadding(el,"Right")+"px";
if(options.find("alias") || (color=getBk(el))=="transparent"){
    color="transparent";bk="transparent"; border=getParentBk(el);btype="t";
    }
else{
    bk=getParentBk(el); border=Mix(color,bk);
    }
d.style.background=bk;
d.className="niftycorners";
p=getPadding(el,"Bottom");
if(options.find("small")){
    d.style.marginTop=(p-2)+"px";
    btype+="s"; lim=2;
    }
else if(options.find("big")){
    d.style.marginTop=(p-10)+"px";
    btype+="b"; lim=8;
    }
else d.style.marginTop=(p-5)+"px";
for(i=lim;i>0;i--)
    d.appendChild(CreateStrip(i,side,color,border,btype));
el.style.paddingBottom=0;
el.appendChild(d);
}

function CreateStrip(index,side,color,border,btype){
var x=CreateEl("b");
x.className=btype+index;
x.style.backgroundColor=color;
x.style.borderColor=border;
if(side=="left"){
    x.style.borderRightWidth="0";
    x.style.marginRight="0";
    }
else if(side=="right"){
    x.style.borderLeftWidth="0";
    x.style.marginLeft="0";
    }
return(x);
}

function CreateEl(x){
return(document.createElement(x));
}

function FixIE(el){
if(el.currentStyle!=null && el.currentStyle.hasLayout!=null && el.currentStyle.hasLayout==false)
    el.style.display="inline-block";
}

function SameHeight(selector,maxh){
var i,v=selector.split(","),t,j,els=[],gap;
for(i=0;i<v.length;i++){
    t=getElementsBySelector(v[i]);
    els=els.concat(t);
    }
for(i=0;i<els.length;i++){
    if(els[i].offsetHeight>maxh) maxh=els[i].offsetHeight;
    els[i].style.height="auto";
    }
for(i=0;i<els.length;i++){
    gap=maxh-els[i].offsetHeight;
    if(gap>0){
        t=CreateEl("b");t.className="niftyfill";t.style.height=gap+"px";
        nc=els[i].lastChild;
        if(nc.className=="niftycorners")
            els[i].insertBefore(t,nc);
        else els[i].appendChild(t);
        }
    }
}

function getElementsBySelector(selector){
var i,j,selid="",selclass="",tag=selector,tag2="",v2,k,f,a,s=[],objlist=[],c;
if(selector.find("#")){ //id selector like "tag#id"
    if(selector.find(" ")){  //descendant selector like "tag#id tag"
        s=selector.split(" ");
        var fs=s[0].split("#");
        if(fs.length==1) return(objlist);
        f=document.getElementById(fs[1]);
        if(f){
            v=f.getElementsByTagName(s[1]);
            for(i=0;i<v.length;i++) objlist.push(v[i]);
            }
        return(objlist);
        }
    else{
        s=selector.split("#");
        tag=s[0];
        selid=s[1];
        if(selid!=""){
            f=document.getElementById(selid);
            if(f) objlist.push(f);
            return(objlist);
            }
        }
    }
if(selector.find(".")){      //class selector like "tag.class"
    s=selector.split(".");
    tag=s[0];
    selclass=s[1];
    if(selclass.find(" ")){   //descendant selector like tag1.classname tag2
        s=selclass.split(" ");
        selclass=s[0];
        tag2=s[1];
        }
    }
var v=document.getElementsByTagName(tag);  // tag selector like "tag"
if(selclass==""){
    for(i=0;i<v.length;i++) objlist.push(v[i]);
    return(objlist);
    }
for(i=0;i<v.length;i++){
    c=v[i].className.split(" ");
    for(j=0;j<c.length;j++){
        if(c[j]==selclass){
            if(tag2=="") objlist.push(v[i]);
            else{
                v2=v[i].getElementsByTagName(tag2);
                for(k=0;k<v2.length;k++) objlist.push(v2[k]);
                }
            }
        }
    }
return(objlist);
}

function getParentBk(x){
var el=x.parentNode,c;
while(el.tagName.toUpperCase()!="HTML" && (c=getBk(el))=="transparent")
    el=el.parentNode;
if(c=="transparent") c="#FFFFFF";
return(c);
}

function getBk(x){
var c=getStyleProp(x,"backgroundColor");
if(c==null || c=="transparent" || c.find("rgba(0, 0, 0, 0)"))
    return("transparent");
if(c.find("rgb")) c=rgb2hex(c);
return(c);
}

function getPadding(x,side){
var p=getStyleProp(x,"padding"+side);
if(p==null || !p.find("px")) return(0);
return(parseInt(p));
}

function getStyleProp(x,prop){
if(x.currentStyle)
    return(x.currentStyle[prop]);
if(document.defaultView.getComputedStyle)
    return(document.defaultView.getComputedStyle(x,'')[prop]);
return(null);
}

function rgb2hex(value){
var hex="",v,h,i;
var regexp=/([0-9]+)[, ]+([0-9]+)[, ]+([0-9]+)/;
var h=regexp.exec(value);
for(i=1;i<4;i++){
    v=parseInt(h[i]).toString(16);
    if(v.length==1) hex+="0"+v;
    else hex+=v;
    }
return("#"+hex);
}

function Mix(c1,c2){
var i,step1,step2,x,y,r=new Array(3);
if(c1.length==4)step1=1;
else step1=2;
if(c2.length==4) step2=1;
else step2=2;
for(i=0;i<3;i++){
    x=parseInt(c1.substr(1+step1*i,step1),16);
    if(step1==1) x=16*x+x;
    y=parseInt(c2.substr(1+step2*i,step2),16);
    if(step2==1) y=16*y+y;
    r[i]=Math.floor((x*50+y*50)/100);
    r[i]=r[i].toString(16);
    if(r[i].length==1) r[i]="0"+r[i];
    }
return("#"+r[0]+r[1]+r[2]);
}
/* tip_search */
var s = new Array();
s[0]="Centre d'aide KS Boolean Expression^index.htm^0<div></br></div>^venial ^0";
s[1]="Informations sur le produit^informations_sur_le_produit.htm^0<div></br></div>^Informations sur le produit ^0";
s[2]="Raccourcis clavier^raccourcis_clavier.htm^0<div></br></div>^Raccourcis clavier ^0";
s[3]="Syntaxe utilisée^syntaxe_utilisee.htm^0<div></br></div>^Syntaxe utilisée ^0";
s[4]="Mode d'utilisation^mode_d_utilisation.htm^0<div></br></div>^Mode d'utilisation ^0";
s[5]="Commencer à simplifier^commencer_a_simplifier.htm^0<div></br></div>^Ajouter ^0";
s[6]="Ajouter une fonction^ajouter_une_fonction.htm^0<div></br></div>^Ajouter une fonction ^0";
s[7]="Fonction algébrique^fonction_algebrique.htm^0<div></br></div>^Fonction numérique ^0";
s[8]="Fonction numérique^fonction_numerique_0.htm^0<div></br></div>^Fonction numérique ^0";
s[9]="Ajouter une table^ajouter_une_table.htm^0<div></br></div>^Ajouter une table ^0";
s[10]="Table de vérité^table_de_verite.htm^0<div></br></div>^Table de vérité ^0";
s[11]="Table de karnaugh^table_de_karnaugh.htm^0<div></br></div>^Table de karnaugh ^0";
s[12]="Importer des fonctions^importer_des_fonctions.htm^0<div></br></div>^Importer des fonctions ^0";
s[13]="Exporter vos solutions^exporter_vos_solutions.htm^0<div></br></div>^Exporter vos solutions ^0";
s[14]="Personnaliser votre application^personnaliser_votre_application.htm^0<div></br></div>^Préferences ^0";
s[15]="Foire aux questions (FAQ)^foire_aux_questions__faq_.htm^0<div></br></div>^Foire aux questions (FAQ) ^0";
s[16]="Je n'arrive pas à lancer la simplification de mon expression ?^je_n_arrive_pas_a_lancer_la_simplification_de_mon_expression__.htm^0<div></br></div>^Je n'arrive pas à lancer la simplification de mon expression ? ^0";
s[17]="Comment puis-je changer de langue ?^comment_puis_je_changer_de_langue__.htm^0<div></br></div>^Comment puis-je changer de langue ^0";
s[18]="Pourquoi je n'arrive pas à importer des fonctions à partir d'un fichier externe?^pourquoi_je_n_arrive_pas_a_importer_des_fonctions_a_partir_d_un_fichier_externe_.htm^0<div></br></div>^Pourquoi je n'arrive pas à importer des fonctions à partir d'un fichier externe? ^0";
s[19]="Je n'arrive pas à interpréter les messages d'erreurs ! ^je_n_arrive_pas_a_interpreter_les_messages_d_erreurs___.htm^0<div></br></div>^Problème lors de la simplification de ma fonction logique ^0";
s[20]="Lois et théorèmes^lois_et_theoremes.htm^0<div></br></div>^Lois et théorèmes ^0";
s[21]="Support technique^support_technique.htm^0<div></br></div>^Support technique ^0";
s[22]="License^license.htm^0<div></br></div>^License ^0";
s[23]="Remerciements^remerciements.htm^0<div></br></div>^Remerciement ^0";
var results_location = "results.html";
function search_form(tip_Form) {  
	if (tip_Form.elements['d'].value.length > 0) {
		document.cookie = '_hw_d_hw_=' + escape(tip_Form.elements['d'].value) + '; path=/';
		document.cookie = '_hw_n_hw_=0; path=/';
		
		window.location = results_location;
	}
}
var return_results = 10;
var include_num = 0;
var bold_query = 0;
var include_url = 0;
var cookie_data = '_hw_d_hw_=';
var cookie_ndata = '_hw_n_hw_=';
var cookies = document.cookie;
var p = cookies.indexOf(cookie_data);
var pn = cookies.indexOf(cookie_ndata);


if (p != -1) {
	var st = p + cookie_data.length;
	var en = cookies.indexOf(';', st);
	if (en == -1) {
		en = cookies.length;
	}
	var d = cookies.substring(st, en);
	d = unescape(d);
}
if (pn != -1) {
	var st = pn + cookie_ndata.length;
	var en = cookies.indexOf(';', st);
	if (en == -1) {
		en = cookies.length;
	}
	var n = cookies.substring(st, en);
}

if (d==null) {d=" ";}

var od = d;
var nr = return_results;
n = parseInt(n);
var nb = n + nr;
var nc = 0;
var nd = 0;
var r = new Array();
var co = 0;
var m = 0;

if (d.charAt(0) == '"' && d.charAt(d.length - 1) == '"') {
	m = 1;
}
var rn = d.search(/ or /i);
if (rn >= 0) {
	m = 2;
}

if (m == 0) {
	var woin = new Array();
	d = d.replace(/ and /gi, ' ');
	var w = d.split(' ');
	for (var a = 0; a < w.length; a++) {
		woin[a] = 0;
		if (w[a].charAt(0) == '-') {
			woin[a] = 1;
		}
	}
	for (var a = 0; a < w.length; a++) {
		w[a] = w[a].replace(/^\-|^\+/gi, '');
	}
	a = 0;
	for (var c = 0; c < s.length; c++) {
		var pa = 0;
		var nh = 0;
		for (var i = 0; i < woin.length; i++) {
			if (woin[i] == 0) {
				nh++;
				var pat = new RegExp(w[i], 'i');
				rn = s[c].search(pat);
				if (rn >= 0) {
					pa++;
				} else {
					pa = 0;
				}
			}
			if (woin[i] == 1) {
				var pat = new RegExp(w[i], 'i');
				var rn = s[c].search(pat);
				if (rn >= 0) {
					pa = 0;
				}
			}
		}
		if (pa == nh) {
			r[a] = s[c];
			a++;
		}
	}
	co = a;
}

if (m == 1) {
	d = d.replace(/"/gi, '');
	var a = 0;
	var pat = new RegExp(d, 'i');
	for (var c = 0; c < s.length; c++) {
		var rn = s[c].search(pat);
		if (rn >= 0) {
			r[a] = s[c];
			a++;
		}
	}
	co = a;
}

if (m == 2) {
	d = d.replace(/ or /gi, ' ');
	var w = d.split(' ');
	var a = 0;
	for (var i = 0; i < w.length; i++) {
		var pat = new RegExp(w[i], 'i');
		for (var c = 0; c < s.length; c++) {
		var rn = s[c].search(pat);
			if (rn >= 0) {
				var pa = 0;
				for (var e = 0; e < r.length; e++) {
					if (s[c] == r[e]) {
						pa = 1;
					}
				}
				if (pa == 0) {
					r[a] = s[c];
					a++;
					co++;
				}
			}
		}
	}
}

function write_cookie(nw) {
	document.cookie = '_hw_d_hw_=' + escape(od) + '; path=/';
	document.cookie = '_hw_n_hw_=' + nw + '; path=/';
}

var tip_Num = co;

function tip_query() {
	document.tip_Form.d.value = od;
}

function tip_num() {
	document.write(co);
}

function tip_out() {
	if (co == 0) {
		document.write('');
		return;
	}
	
	if (nr==0){n=0;nd=co;}
	else{
	     if (n + nr > co) {nd = co;} else {nd = n + nr;}
  }

	for (var a = n; a < nd; a++) {
		var os = r[a].split('^');
		if (bold_query == 1 && m == 0) {
			for (var i = 0; i < w.length; i++) {
				var lw = w[i].length;
				var tw = new RegExp(w[i], 'i');
				rn = os[2].search(tw);
				if (rn >= 0) {
					var o1 = os[2].slice(0, rn);
					var o2 = os[2].slice(rn, rn + lw);
					var o3 = os[2].slice(rn + lw);
					os[2] = o1 + '<b>' + o2 + '</b>' + o3; 
				}
			}
		}
		if (bold_query == 1 && m == 1) {
			var lw = d.length;
			var tw = new RegExp(d, 'i');
			rn = os[2].search(tw);
			if (rn >= 0) {
				var o1 = os[2].slice(0, rn);
				var o2 = os[2].slice(rn, rn + lw);
				var o3 = os[2].slice(rn + lw);
				os[2] = o1 + '<b>' + o2 + '</b>' + o3; 
			}
		}
		if (include_num == 1) {
			if (os[4] == '1') {
		
			document.write(a + 1, '. <a href="', os[1], '" target="_blank">', os[0], '</a>');
    
			} else {
			  document.write(a + 1, '. <a href="', os[1], '">', os[0], '</a>');
				
			}
				if (os[2].length > 1) {
			document.write('<br>', os[2]);
			}			
		} else {
			if (os[4] == '1') {
				if (a%2==0){
				document.write('<div ><a href="', os[1], '" target="_blank">', os[0], '</a></div>');
				} else {document.write('<div ><a href="', os[1], '" target="_blank">', os[0], '</a></div>');}
			} else {
				if (a%2==0){
				document.write('<div ><a href="', os[1], '">', os[0], '</a></div>');
				} else {document.write('<div ><a href="', os[1], '">', os[0], '</a></div>');}
			}
		}
		}
	if (co > nr) {

		nc = co - nb;
		if (nc > nr) {
			nc = nr;
		}
		document.write('<p>');
	}
	if (n > 1) {
		document.write('<a href="', results_location, '" onclick="write_cookie(', n - nr, ')">Précédent ', nr, '</a> &nbsp;');
	}
	if (nc > 0) {
		document.write('<a href="', results_location, '" onclick="write_cookie(', n + nr, ')">Suivant ', nc, '</a>');
	}
	
}
var d_first = true;q=document.getElementById("d");if (q!=null) {q.value = "Chercher"};/* domloadevent */
function addDOMLoadEvent(f){if(!window.__ADLE){var n=function(){if(arguments.callee.d)return;arguments.callee.d=true;if(window.__ADLET){clearInterval(window.__ADLET);window.__ADLET=null}for(var i=0;i<window.__ADLE.length;i++){window.__ADLE[i]()}window.__ADLE=null};if(document.addEventListener)document.addEventListener("DOMContentLoaded",n,false);/*@cc_on @*//*@if (@_win32)document.write("<scr"+"ipt id=__ie_onload defer src=//0><\/scr"+"ipt>");var s=document.getElementById("__ie_onload");s.onreadystatechange=function(){if(this.readyState=="complete")n()};/*@end @*/if(/WebKit/i.test(navigator.userAgent)){window.__ADLET=setInterval(function(){if(/loaded|complete/.test(document.readyState)){n()}},10)}window.onload=n;window.__ADLE=[]}window.__ADLE.push(f)}
