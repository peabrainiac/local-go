export default class ConnectionHandler {
	constructor(){
		const socket = new WebSocket(`ws://${document.location.host}`);
		socket.addEventListener("open",(e)=>{
			console.log("Established WebSocket connection:",e);
			socket.send("test");
			socket.send("test 2");
			socket.send("test 3");
			setTimeout(()=>{socket.send("test 4")},1000);
		});
		socket.addEventListener("error",(e)=>{
			console.log("WebSocket connection failed:",e);
		});
		socket.addEventListener("message",(e)=>{
			console.log("Received data:",e);
			socket.send(`Did someone just say "${e.data}"?`);
		});
	}
}