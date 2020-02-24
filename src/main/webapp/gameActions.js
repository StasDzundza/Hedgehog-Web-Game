//createUIElements();
addEventListener("keydown", keyEvent);

var socket = new WebSocket("ws://localhost:8080/HedgehogWebApp_war/gameActions");
socket.onmessage = onMessage;
var fieldSize = 0;
var appleFilename = 'apple.png';
var hedgehogFilename = 'hedgehog.png';
var currentHedgehogXPos = 0;
var currentHedgehogYPos = 0;

function onMessage(event) {
    var gameData = JSON.parse(event.data);
    if (gameData.messageType === "build") {
        fieldSize = parseInt(gameData.fieldSize);
        currentHedgehogXPos = parseInt(gameData.hedgehogXPos);
        currentHedgehogYPos = parseInt(gameData.hedgehogYPos);
        createGameField(fieldSize,fieldSize);
        putHedgehogInGameField(currentHedgehogXPos,currentHedgehogYPos);
    }else if(gameData.messageType === "applePosition"){
        putAppleInGameField(gameData.appleXPos,gameData.appleYPos);
    }else if(gameData.messageType === "gameInfo"){
        if(gameData.gameOver === "true"){
            alert("Game Over!!!");
            restart();
        }else{
            removeElementFromGameField(currentHedgehogXPos,currentHedgehogYPos);
            currentHedgehogXPos = parseInt(gameData.hedgehogXPos);
            currentHedgehogYPos = parseInt(gameData.hedgehogYPos);
            if(gameData.appleEat === "true"){
                removeElementFromGameField(currentHedgehogXPos,currentHedgehogYPos);
            }
            putHedgehogInGameField(currentHedgehogXPos,currentHedgehogYPos);
        }
    }
}

function keyEvent(event){
    switch(event.keyCode){
        case 37:  // left key
            socket.send("left");
            break;
        case 38:   // up key
            socket.send("up");
            break;
        case 39:   // right key
            socket.send("right");
            break;
        case 40:   // down key
            socket.send("down");
            break;
    }
}

function createGameField(rows,columns) {
    var body = document.getElementsByClassName('wrapper')[0];
    var tbl = document.createElement('table');
    tbl.style["margin"] = "0 auto";
    var tbdy = document.createElement('tbody');
    var id = 0;
    for (var i = 0; i < rows; i++) {
        var tr = document.createElement('tr');
        for (var j = 0; j < columns; j++) {
            var td = document.createElement('td');
            td.setAttribute("id",id++);
            td.style["border"] = "2px solid green";
            td.style["border-radius"] = "5px";
            td.style["width"] = "40px"
            td.style["height"] = "40px"
            tr.appendChild(td)
        }
        tbdy.appendChild(tr);
    }
    tbl.appendChild(tbdy);
    body.appendChild(tbl)
}

function putHedgehogInGameField(x,y) {
    var elementId = (y*fieldSize) + parseInt(x);
    var cell = document.getElementById(elementId);
    var img = document.createElement('img');
    img.setAttribute('src',hedgehogFilename);
    img.setAttribute('alt','H');
    img.style["width"] = "20px";
    img.style["height"] = "20px";
    img.style["margin"] = "0 auto";
    cell.append(img);
}

function putAppleInGameField(x,y) {
    var elementId = (y*fieldSize) + parseInt(x);
    var cell = document.getElementById(elementId);
    var img = document.createElement('img');
    img.setAttribute('src',appleFilename);
    img.setAttribute('alt','apple');
    img.style["width"] = "20px";
    img.style["height"] = "20px";
    img.style["margin"] = "0 auto";
    cell.append(img);
}

function removeElementFromGameField(x,y) {
    var elementId = (y*fieldSize) + parseInt(x);
    var cell = document.getElementById(elementId);
    cell.innerHTML = '';
}

function restart(){
    var body = document.getElementsByTagName('body')[0];
    var table = document.getElementsByTagName('table')[0];
    body.removeChild(table);
    socket.send('restart');
}

function createUIElements(){
    var wrapper = document.getElementsByTagName('body')[0];
    var input = document.createElement('input');
    input.setAttribute('type','button');
    input.setAttribute('value','RESTART GAME');
    input.setAttribute('id','restart');
    input.setAttribute('onclick','restart()');
    input.style['width'] = '100px';
    wrapper.appendChild(input);
}

