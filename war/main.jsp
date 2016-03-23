<!DOCTYPE html>
<%@ page import="pan.ai.sudoku.Sudoku"%>
<%!Sudoku brd=new Sudoku();%>
<%!boolean difficultySet=true;%>
<%
	String s=request.getParameter("difficulty");
	System.out.println(s);
	if("easy".equals(s))
		brd.generate(50,7500,1);
	else if("medium".equals(s))
		brd.generate(60,7500,1);
	else if("hard".equals(s))
		brd.generate(81,7500,1);
	else{
		brd.clear();
		difficultySet=false;
	}
%>
<html lang="en">
	<meta charset="UTF-8">
	<meta http-equiv="Pragma" content="no-cache">
 	<meta http-equiv="Cache-Control" content="no-cache">
 	<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
	<title>Sudoku</title>
	<style>
		html{
			height:100%;
		}
		body{
			font-family:Times New Roman;
			margin-top:0;
			height:100%;
			background-color:chocolate;
			overflow:hidden;
		}
		#pg{
			margin:0 auto 0 auto;
			background-color:white;
			width:700px;
			height:100%;
			overflow:auto;
		}
		nav{
			background-color:red;
			color:white;
			display:block;
			margin:0px 0px 0px 0px;
			width:100%;
			font-size:25px;
			align=right;
		}
		nav ul{
			margin:0;
			padding:0;
		}
		nav ul li{
			display:inline-block;
			margin:0;
			padding:30px 30px;
			list-style-type:none;
		}
		#svgx{
			display:block;
			margin:40px auto 0 auto;
			height:500px;
			width:500px;
		}
		#timer{
			cursor:wait;
		}
		#givup{
			cursor:help;
		}
		nav ul li a{
			color:white;
			display:block;
			text-decoration:none;
			font-weight:300;
			font-size:24px;
		}
		nav ul li:hover{
			color:yellow;
		}
		nav ul li a:hover{
			color:yellow;
		}
	</style>
	<script type="text/javascript">
		var current=0;
		var fixed=[];
		var stop=false;
		function updateRect(){
			var curr=document.getElementById("curr");
			curr.setAttribute("transform","translate("+(Math.floor(current/9)*50)+","+((current%9)*50)+")");
		}
		function doKeyDown(e){
			var k=e.which;
			if(k==87||k==119||k==38)
				current=(81+current-1)%81;
			else if(k==83||k==115||k==40)
				current=(81+current+1)%81;
			else if(k==100||k==68||k==39)
				current=(81+current+9)%81;
			else if(k==97||k==65||k==37)
				current=(81+current-9)%81;
			else if(!fixed[current]&&!stop){
				if(k=="48")
					change(current,0);
				else if(k=="49")
					change(current,1);
				else if(k=="50")
					change(current,2);
				else if(k=="51")
					change(current,3);
				else if(k=="52")
					change(current,4);
				else if(k=="53")
					change(current,5);
				else if(k=="54")
					change(current,6);
				else if(k=="55")
					change(current,7);
				else if(k=="56")
					change(current,8);
				else if(k=="57")
					change(current,9);
				}
			//alert(xs[current].style.fill);
			updateRect();
			return true;
		}
		window.addEventListener("keydown",doKeyDown,false);
		var startTime=new Date();
		var xs=[];
		var ans=[];
		<%for(int i=0;i<9;i++)for(int j=0;j<9;j++){%>ans[<%=brd.index(i,j)%>]=<%=brd.brd[i][j]%>;fixed[<%=brd.index(i,j)%>]=<%=brd.get(i,j)==brd.brd[i][j]%>;<%}%>
		window.onload=function(){<%for(int i=0;i<81;i++){%>xs[<%=i%>]=document.getElementById("txt"+<%=i%>);<%}%>
			<%if(request.getParameter("difficulty")!=null){%>
				document.getElementById("<%=request.getParameter("difficulty")%>").style.background="orange";
				displayTime();
			<%}else{%>
				document.getElementById("givup").style.background="orange";
			<%}%>
			
		}
		function change(i,x){
			xs[i].textContent=x;
			if(xs[i].textContent=="0")
				xs[i].style.fill="white";
			else xs[i].style.fill="black";
			if(check()){
				for(var n=0;n<81;n++){
					xs[n].style.fill="orange";
					xs[n].onclick="";
				}
				stop=true;
			}
		}
		function incr(i){
			change(i,(parseInt(xs[i].textContent)+1)%10);
		}
		function show(){
			for(var v=0;v<81;v++){
				if(xs[v].textContent!=ans[v])
					xs[v].style.fill="green";
				xs[v].textContent=ans[v];
				xs[v].onclick="";
			}
			stop=true;
		}
		function index(i,j){return i*9+j;}
		function check(){
			for(var i=0;i<9;i++){
				for(var j=0;j<9;j++){
					if(xs[index(i,j)].textContent=="0")
						return false;
					for(var k=0;k<9;k++){
						if(k!=j&&xs[index(i,j)].textContent==xs[index(i,k)].textContent)
							return false;
						if(k!=i&&xs[index(k,j)].textContent==xs[index(i,j)].textContent)
							return false;
					}
					var x=i-i%3;
					var y=j-j%3;
					for(var k=0;k<3;k++)
						for(var l=0;l<3;l++)
							if(((x+k!=i)||(y+l!=j))&&xs[index(i,j)].textContent==xs[index(k+x,l+y)].textContent)
								return false;
				}
			}
			return true;
		}
		function displayTime(){
			var diff=Math.floor(((new Date())-startTime)/1000);
			var sec=diff%60;
			document.getElementById("timer").innerHTML=Math.floor(diff/60)+":"+(sec<10?"0":"")+sec;
			if(!stop)
				var t = setTimeout(function(){displayTime()},500);
		}
	</script>
<body>
	<div id="pg">
		<nav>
			<ul>
				<li id="easy" ><a href="<%=request.getRequestURL()%>?difficulty=easy">Easy</a></li>
				<li id="medium"><a href="<%=request.getRequestURL()%>?difficulty=medium">Medium</a></li>
				<li id="hard"><a href="<%=request.getRequestURL()%>?difficulty=hard">Hard</a></li>
				<li id="timer">Time</li>
				<li id="givup" onclick="show()">Give up</li>
			</ul>
		</nav>
		<svg id="svgx" viewBox="0 0 450 450">
			<%for(int i=0;i<10;i++){%><line x1="0" y1="<%=i*50%>" x2="450" y2="<%=i*50%>" stroke="black" stroke-width="<%=i%3==0?3:1%>"/><line x1="<%=i*50%>" y1="0" x2="<%=i*50%>" y2="450" stroke="black" stroke-width="<%=i%3==0?3:1%>"/><%}%>
			<rect id="curr" x="0" y="0" width="50" height="50" fill="transparent" stroke="red" stroke-width="3"/>
			<%for(int i=0;i<9;i++)for(int j=0;j<9;j++){%><text id="txt<%=brd.index(i,j)%>" <%=brd.fixed(i,j)?"":"onclick=\"incr("+brd.index(i,j)+")\""%> x="<%=i*50+15%>" y="<%=j*50+40%>" style="fill:<%=brd.get(i,j)==0?"white":"red"%>" font-size="40px"><%=brd.get(i,j)%></text><%}%>
		</svg>
	</div>
</body>
</html>
