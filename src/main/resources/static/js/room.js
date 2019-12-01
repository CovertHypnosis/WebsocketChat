const room_Id = document.querySelector('#room_id').value;
let stompClient = null;
let userId = null;

function init() {
    userId = document.querySelector('#user_id').value.trim();
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

function onError(error) {
    console.log(error);
}

function onConnected() {
    stompClient.subscribe('/topic/room/in/'+room_Id, onMessageReceived);
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    // append
    let art = document.createElement("article");
    art.classList = "row message";
    let div = document.createElement("div");
    div.classList = "col-12";
    let spanTimeStamp = document.createElement("span");
    spanTimeStamp.innerHTML = new Date(message.date).toLocaleTimeString() + " ";

    let spanAuthor = document.createElement("span");
    spanAuthor.innerHTML = message.senderName;

    let paragraph = document.createElement("p");
    paragraph.innerHTML = message.text;
    div.append(spanTimeStamp);
    div.append(spanAuthor);
    div.append(paragraph);
    art.append(div);
    document.querySelector("#content").append(art);
}


function sendMessage(event) {
    event.preventDefault();
    let message = document.querySelector('#message');
    console.log(message);

    if(message && stompClient) {
        let eventDTO = {
            senderId: userId,
            text: message.value.trim(),
            type: 'CHAT',
            roomId: parseInt(room_Id)
        };

        stompClient.send("/app/room/out/"+room_Id, {}, JSON.stringify(eventDTO));
        message.value = '';
    }
}

document.querySelector("#send").addEventListener("click", sendMessage);


init();