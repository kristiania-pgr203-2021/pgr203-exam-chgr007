
//Random color generator

    function randomColor(){
        let randomColor = document.getElementsByClassName("random-color");
        for(let i = 0; i<randomColor.length; i++){
        
            let r = Math.floor(Math.random()*255+1);
            let g = Math.floor(Math.random()*255+1);
            let b = Math.floor(Math.random()*255+1);
        
        
            const brightness = Math.round(((r * 299) +
                (g * 587) +
                (b * 114)) / 1000);
        
            const textColour = (brightness > 125) ? 'black' : 'white';
        
            randomColor[i].style.backgroundColor = 'rgb(' + r + ',' + g + ',' + b + ')'
            randomColor[i].style.color = textColour;

    }
}
function changeName(type, id){

    popUpContainer.innerHTML = `<div class="background-panel main"></div>`
    popUpContainer.innerHTML += `<div id="inner-pop-up-container" class="main"></div>`

    let innerContainer = document.querySelector("#inner-pop-up-container");
    let currentName = document.querySelector(`#${type}-name`).innerHTML;

    console.log(currentName)
    console.log(id)

    innerContainer.innerHTML = `
    <div id="pop-up-div" class="popup">
        <div class="random-color popup-header">
            <h1>CHANGE ${type.toUpperCase()} NAME 
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" onclick="closePopUp()" class="bi bi-x-circle-fill" viewBox="0 0 16 16">
                    <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z"/>
                </svg>
            </h1>
        </div>
        
        <form action="/api/${type.toLowerCase()}Name" method="POST">
            <input type="hidden" name="${type}Id" value="${id}">
            <input type="text" maxlength="250" name="name" value="${currentName}"></p>
            <button type="submit">CHANGE</button>
        </form>
    </div>`


    popUpContainer.innerHTML += `<div class="background-panel main"></div>`
    randomColor();
}

function addButton(container, type, id, userId){

    console.log(userId)

    if(userId != null && userId != 0){
        console.log("button added, userID: " + userId)
        container.innerHTML = `<div id="add-new-${type}-button" onclick="popUpForm(popUpContainer, ${type}, ${id})" class="random-color flexbox-box flex-content">
            <h2 class="centred">Add new ${type}</h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="3em" height="3em" fill="currentColor" class="margin-auto bi bi-plus-square" display="block" viewBox="0 0 16 16">
                <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>
                <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>
            </svg>
        </div>
    `
    } else {
        container.innerHTML = `
        <a href="/login.html" class="flexbox-box flex-content" style="background-color:grey">
            <h2 class="centred">Log in to add ${type}</h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="3em" height="3em" fill="currentColor" class="margin-auto bi bi-plus-square" display="block" viewBox="0 0 16 16">
                <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>
                <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>
            </svg>
        </a>`
    }
}

function refresh(){
    location.reload();
}




function popUpForm(container, type, id){

    container.innerHTML = `<div class="background-panel main"></div>`
    container.innerHTML += `<div id="inner-pop-up-container" class="main"></div>`

    let innerContainer = document.getElementById("inner-pop-up-container");

    innerContainer.innerHTML += `
    <div id="pop-up-div" class="popup">
        <div class="random-color popup-header">
            <h1>ADD NEW ${type.toUpperCase()} 
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" onclick="closePopUp()" class="bi bi-x-circle-fill" viewBox="0 0 16 16">
  <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM5.354 4.646a.5.5 0 1 0-.708.708L7.293 8l-2.647 2.646a.5.5 0 0 0 .708.708L8 8.707l2.646 2.647a.5.5 0 0 0 .708-.708L8.707 8l2.647-2.646a.5.5 0 0 0-.708-.708L8 7.293 5.354 4.646z"/>
                </svg>
            </h1>
        </div>
    </div>`

    
    randomColor();

    const popUpDiv = document.querySelector("#pop-up-div");

    switch(type){
        case "questionnaire": popUpDiv.innerHTML += `
            <form action="/api/newQuestionnaire" method="POST">
                <input type="hidden" name="userId" value="${id}">
                <input type="text" maxlength="250" name="questionnaireName" placeholder="What would you like to name the new questionnaire?"></p>
                <button type="submit" onclick="refresh()">CREATE</button> 
            </form>
        `
        break;

        case "question": popUpDiv.innerHTML += createQuestionPopUp(id);
        break;

        case "answer": popUpDiv.innerHTML += createAnswerPopUp(id)
    }
}



function closePopUp(){
    popUpContainer.innerHTML = "";

}

