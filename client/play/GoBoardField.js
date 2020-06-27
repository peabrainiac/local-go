export const EMPTY = 0;
export const STONE_PLAYER_1 = 1;
export const STONE_PLAYER_2 = 2;

export default class GoBoardField extends HTMLElement {
	constructor(){
		super();
		this.attachShadow({mode:"open"});
		this.shadowRoot.innerHTML = `
			<style>
				:host {
					display: block;
					position: relative;
				}
				#line-right {
					position: absolute;
					top: 50%;
					left: 50%;
					width: calc(100% - 4px);
					height: 0;
					box-shadow: 0 0 1.5px 2px #00000080;
				}
				#line-down {
					position: absolute;
					top: calc(50% + 4px);
					left: 50%;
					width: 0;
					height: calc(100% - 8px);
					box-shadow: 0 0 1.5px 2px #00000080;
				}
				#line-down.hidden, #line-right.hidden {
					display: none;
				}
				#line-right.hidden + #line-down {
					top: 50%;
					height: calc(100% - 4px);
				}
				#line-right.hidden + #line-down.hidden {
					width: 0;
					height: 0;
					display: block;
				}
				#stone {
					position: absolute;
					top: 50%;
					left: 50%;
					transform: translate(-50%,-50%);
					width: 75%;
					height: 75%;
					margin: auto;
					border-radius: 50%;
					font-size: 100%;
					box-shadow: 3px 3px 5px #00000080, inset 8px 8px 10px -4px #ffffff60, inset -8px -8px 10px -4px #00000060;
				}
				#stone.hidden {
					display: none;
				}
				:host(:hover) #stone.hidden {
					display: block;
					opacity: 0.5;
					background: var(--color-player-1);
				}
				#stone.stone-1 {
					background: var(--color-player-1);
				}
				#stone.stone-2 {
					background: var(--color-player-2);
				}
			</style>
			<div id="line-right"></div>
			<div id="line-down"></div>
			<div id="stone"></div>
		`;
		this._lineDown = this.shadowRoot.getElementById("line-down");
		this._lineRight = this.shadowRoot.getElementById("line-right");
		this._stoneDiv = this.shadowRoot.getElementById("stone");
		this._state = EMPTY;
		this._stoneDiv.className = "hidden";
	}

	set state(state){
		this._state = state;
		if (state==EMPTY){
			this._stoneDiv.className = "hidden";
		}else if(state==STONE_PLAYER_1){
			this._stoneDiv.className = "stone-1";
		}else if(state==STONE_PLAYER_2){
			this._stoneDiv.className = "stone-2"
		}else {
			throw new Error("Invalid Enum value:",state);
		}
	}

	get state(){
		return this._state;
	}

	static get observedAttributes(){
		return ["hidelinedown","hidelineright"];
	}

	attributeChangedCallback(name,oldValue,newValue){
		if (name=="hidelinedown"){
			this._lineDown.classList.toggle("hidden",newValue);
		}else if (name=="hidelineright"){
			this._lineRight.classList.toggle("hidden",newValue);
		}
	}

	set hideLineDown(hideLineDown){
		this.setAttribute("hidelinedown",hideLineDown);
	}

	get hideLineDown(){
		return this.getAttribute("hidelinedown");
	}

	set hideLineRight(hideLineRight){
		this.setAttribute("hidelineright",hideLineRight);
	}

	get hideLineRight(){
		return this.getAttribute("hidelineright");
	}
}
window.customElements.define("go-board-field",GoBoardField);