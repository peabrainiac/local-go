import GoBoard from "./GoBoard.js";
import ConnectionHandler from "./ConnectionHandler.js";

document.addEventListener("DOMContentLoaded",()=>{
	const container = document.getElementById("container");
	const board = new GoBoard();
	container.innerHTML = "";
	container.appendChild(board);

	const connection = new ConnectionHandler();
});