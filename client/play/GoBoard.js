import GoBoardField from "./GoBoardField.js";

export default class GoBoard extends HTMLElement {
	constructor(width=9,height=9){
		super();
		this.attachShadow({mode:"open"});
		this.shadowRoot.innerHTML = `
			<style>
				:host {
					display: flex;
					width: 100%;
					height: 100%;
					box-shadow: 5px 5px 10px #00000080;
					background-color: #e0b07080;
					flex-direction: column;
					--color-player-1: #404040;
					--color-player-2: #dfdfdf;
				}
				#top, #bottom {
					height: 0;
					flex-grow: 1.5;
				}
				go-board-row {
					height: 0;
					flex-grow: 1;
					display: flex;
					width: 100%;
					flex-direction: row;
				}
				#left, #right {
					width: 0;
					flex-grow: 1.5;
				}
				go-board-field {
					width: 0;
					flex-grow: 1;
				}
			</style>
			<div id="top"></div>
			<div id="bottom"></div>
		`;
		this._topDiv = this.shadowRoot.querySelector("#top");
		this._bottomDiv = this.shadowRoot.querySelector("#bottom");
		for (let y=0;y<height;y++){
			let row = new GoBoardRow(width,y+1==height);
			this.shadowRoot.insertBefore(row,this._bottomDiv);
		}
	}
}
class GoBoardRow extends HTMLElement {
	constructor(width=9,hideLinesDown){
		super();
		this.innerHTML = `
			<div id="left"></div>
			<div id="right"></div>
		`;
		this._leftDiv = this.querySelector("#left");
		this._rightDiv = this.querySelector("#right");
		for (let x=0;x<width;x++){
			let field = new GoBoardField();
			field.state = Math.floor(Math.random()*3);
			if (hideLinesDown){
				field.hideLineDown = true;
			}
			if (x+1==width){
				field.hideLineRight = true;
			}
			this.insertBefore(field,this._rightDiv);
		}
	}
}
window.customElements.define("go-board",GoBoard);
window.customElements.define("go-board-row",GoBoardRow);