import GoBoard from "./GoBoard.js";

document.addEventListener("DOMContentLoaded",()=>{
	const container = document.getElementById("container");
	const board = new GoBoard();
	container.innerHTML = "";
	container.appendChild(board);
});