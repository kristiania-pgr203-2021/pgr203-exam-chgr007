
//Random color generator

    function randomColor(){
        let randomColor = document.getElementsByClassName("random-color");
        for(let i = 0; i<randomColor.length; i++){
            console.log("RUNNING!")
            console.log(randomColor[i])
        
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

function changeName(){
    popUpContainer.innerHTML = `<div id="new-question-popup" class="popup">
        <div class="random-color popup-header"><h1>CHANGE QUESTIONNAIRE NAME</h1></div>
            <form action="/api/changeQuestionnaireName" method="POST">
                <input type="text" maxlength="250" name="questionnaireName" placeholder="Write your new name here"></p>
                <button>ADD</button>
            </form>
        </div>
    </div>`

    popUpContainer.innerHTML += `<div class="background-panel main"></div>`
    randomColor();
}

function addButton(container, type, id){
    container.innerHTML = `
        <div id="add-new-${type}-button" onclick="popUpForm(popUpContainer, ${type}, ${id})" class="random-color flexbox-box flex-content">
            <h2 class="centred">Add new ${type}</h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="3em" height="3em" fill="currentColor" class="margin-auto bi bi-plus-square" display="block" viewBox="0 0 16 16">
                <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>
                <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>
            </svg>
        </div>
    `
    
}


function popUpForm(container, type, id){

    container.innerHTML = `<div class="background-panel main"></div>`
    container.innerHTML += `<div id="inner-pop-up-container" class="main"></div>`

    let innerContainer = document.querySelector("#inner-pop-up-container");

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
                <button type="submit">CREATE</button> 
            </form>
        `
        break;

        case "question": popUpDiv.innerHTML += `
            <form action="/api/newQuestion" method="POST">
                <input type="hidden" name="questionnaireId" value="${id}">
                <input type="hidden" name="questionType" id="question-type" value="">
                <input type="text" maxlength="250" name="question" placeholder="Write your question here"></p>
                <select onchange="changeQuestionType()" id="input-types" name="input-types">
                <option value="options">Options</option>
                <option value="text">Text</option>
                <option value="radio">Radio</option>
                <option value="range">range</option>
                </select>

                <div id="selected-option-div"></div>

                <button type="submit">ADD</button> 
            </form>
        `
        break;

        case "answer": popUpDiv.innerHTML += `
            <form action="/api/newAnswer" method="POST">
                <input type="hidden" name="questionId" value="${id}">
                <input type="text" maxlength="250" name="answer" placeholder="What is your reponse?"></p>
                <button type="submit">SUBMIT</button> 
            </form>
        `
    }
}

function changeQuestionType(){

    const type = document.getElementById("input-types").value
    const selectedOptionDiv = document.getElementById("selected-option-div");
    const questionType = document.getElementById("question-type")
    const popUpContainer = document.getElementById("inner-pop-up-container");
    


    switch(type){
        case "radio": selectedOptionDiv.innerHTML = radioSelected(popUpContainer);
        break;
        case "text": selectedOptionDiv.innerHTML = `<input type="text">`
        break;
        case "range": selectedOptionDiv.innerHTML = `<input type="range" min="0" max=5">`
        break;
    }

    questionType.value = type;
    
}

function closePopUp(){
    popUpContainer.innerHTML = "";

}

function radioSelected(container){

    container.innerHTML += `<div class="under-popup popup" id="radio-popup"></div>`

    let radioPopup = document.querySelector("#radio-popup");

    radioPopup.innerHTML = `<input type="number" id="radio-numbers" placeholder="How many radio buttons do you want?">
    <button id="radio-add-button">Add radio buttons</button><br><br>
    `;

    let radioAddButton = document.querySelector("#radio-add-button");
    let radioNumbers = document.querySelector("#radio-numbers")
    radioAddButton.onclick =()=> showRadioButtons(radioPopup, radioNumbers.value);

}

function showRadioButtons(container, number){

    const selectedOptionDiv = document.getElementById("selected-option-div");

    let radioArray = []
    container.innerHTML = "";
    for(i=0; i<number; i++){
        container.innerHTML += `<input type="text" placeholder="Enter text for button here" class="radio-button-value">`
    }

    container.innerHTML += `<button id="submit-radio-names"> Sumbit </button>`

    let submitRadioButton = document.querySelector("#submit-radio-names");

    submitRadioButton.onclick =()=> printRadioButtons(container, number);

        

}
    

function printRadioButtons(container, number){
    console.log(container);
    console.log(number);
    let radioPopup = document.querySelector("#radio-popup");


    let radioButtonValues = document.querySelectorAll(".radio-button-value")

    radioPopup.innerHTML = ""; 

        for(i=0; i<number; i++){
            
            radioPopup.innerHTML += `<div class="radio-answes">

            <input type="radio" id="${radioButtonValues[i].value}" name="answer" value="${radioButtonValues[i].value}">
            <label  for="${radioButtonValues[i].value}">${radioButtonValues[i].value}</label><br>`

        }

        radioPopup.innerHTML += `</div>`

        console.log(container.innerHTML);
    console.log(number);
}