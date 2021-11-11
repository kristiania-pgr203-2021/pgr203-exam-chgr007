
let selectedOptionDiv;
let type;
let underPopUp;
let questionType;
let innerPopUpContainer;
let questionForm;



function createQuestionPopUp(questionnaireId) {
    const question = {};
    const form = `
            <form action="/api/v2/question" method="POST" id="question-form">
                <input type="hidden" name="questionnaireId" value="${questionnaireId}">
                <input type="hidden" name="questionType" id="question-type" value="">
                <input type="text" maxlength="250" name="question" id="question-name" placeholder="Write your question here"></p>
                <select onchange="changeQuestionType()" id="input-types" name="input-types">
                <option value="options">Options</option>
                <option value="text">Text</option>
                <option value="radio">Radio</option>
                <option value="range">range</option>
                </select>

                <div id="selected-option-div"></div>

                <button id="add-question-button">ADD</button> 
            </form>
        `;


    /*
    question.id = -1;
    question.question = document.getElementById("question-name").value;
    question.questionnaire = id;
    question.hasAnswerOptions = hasAnswerOptions;
    question.answerOptionList = [];

    if (hasAnswerOptions) {
        // add to option list
    }
    */
    return form;
}

function changeQuestionType(){

    type = document.querySelector("#input-types").value
    selectedOptionDiv = document.querySelector("#selected-option-div");
    questionType = document.querySelector("#question-type");
    innerPopUpContainer = document.getElementById("inner-pop-up-container")
    questionForm = document.querySelector("#question-form")
    questionName = document.querySelector("#question-name")

    console.log(document.getElementById("under-popup"))

    if(document.getElementById("under-popup") == null){
        innerPopUpContainer.innerHTML += `<div class="under-popup popup main" id="under-popup"></div>`
        underPopUp = document.querySelector("#under-popup");

    } else {
        underPopUp.innerHTML = "";
    }

    document.querySelector("#selected-option-div").innerHTML = "";

    

    switch(type){
        case "radio": radioSelected();
            break;
        case "text": textSelected();
            break;
        case "range": rangeSelected();
            break;
    }

    questionType.value = type;

}

function rangeSelected(){

    underPopUp.innerHTML = `
    <input type="number" id="min-range" placeholder="what is the minimum?">
    <input type="number" id="max-range" placeholder="what is the maximum?">
    <input type="text" id="min-label" placeholder="what is the min-label?">
    <input type="text" id="max-label" placeholder="what is the max-label?">

    <button id="range-add-button" onclick="printRange()">Add range input</button><br><br>
    `
}

function printRange(){

    let minRange = document.querySelector("#min-range").value;
    let maxRange = document.querySelector("#max-range").value;
    let minLabel = document.querySelector("#min-label").value;
    let maxLabel = document.querySelector("#max-label").value;

    if(maxRange < minRange){
        var tempRange = minRange;
        minRange = maxRange;
        maxRange = tempRange;

        var tempLabel = minLabel;
        minLabel = maxLabel;
        maxLabel = tempLabel;
    }


    document.querySelector("#selected-option-div").innerHTML = `
    <label>${minLabel}</label>
    <input type="range" min="${minRange}" max="${maxRange}">
    <label>${maxLabel}</label>`
    innerPopUpContainer.removeChild(underPopUp);

}


function textSelected(){

    underPopUp.innerHTML = `
    <input type="number" id="text-numbers" placeholder="What is the maximum amount of characters?">
    <input type="text" id="placeholder-text" placeholder="What would you like the placeholder text to be?">

    <button id="text-add-button" onclick="printTextInput()">Add text input</button><br><br>
    `;
}

function printTextInput(){

    let maxChars = document.querySelector("#text-numbers").value;
    let placeholderText = document.querySelector("#placeholder-text").value;

    document.querySelector("#selected-option-div").innerHTML = `
            <input type="text" id="text-input" name="answer" value="" maxlength="${maxChars}" placeholder="${placeholderText}">
        <br>`;

    innerPopUpContainer.removeChild(underPopUp);
}


function radioSelected(){

    underPopUp.innerHTML = `<input type="number" id="radio-numbers" placeholder="How many radio buttons do you want?">
    <button id="radio-add-button" onclick="addRadioButtonNames()">Add radio buttons</button><br><br>
    `;
}

function addRadioButtonNames(){

    let radioNumbers = document.querySelector("#radio-numbers").value
    underPopUp.innerHTML = "";

    for(i=0; i<radioNumbers; i++){
        underPopUp.innerHTML += `<input type="text" placeholder="Enter text for button here" class="radio-button-value">`
    }

    underPopUp.innerHTML += `<button id="submit-radio-names" onclick="printRadioButtons()"> Sumbit </button>`

}

function printRadioButtons(){

    let radioButtonValues = document.querySelectorAll(".radio-button-value")
    underPopUp.innerHTML = "";

    console.log(selectedOptionDiv)

    for(i=0; i<radioButtonValues.length; i++){

        document.querySelector("#selected-option-div").innerHTML += `
        <div class="radio-answes">
            <input type="radio" id="${radioButtonValues[i].value}" name="answer" value="${radioButtonValues[i].value}">
            <label  for="${radioButtonValues[i].value}">${radioButtonValues[i].value}</label>
        </div>
        <br>`
    }

    innerPopUpContainer.removeChild(underPopUp);

}



