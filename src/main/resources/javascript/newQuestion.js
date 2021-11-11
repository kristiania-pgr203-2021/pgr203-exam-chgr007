
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

    innerPopUpContainer.innerHTML += `<div class="under-popup popup main" id="under-popup"></div>`
    underPopUp = document.querySelector("#under-popup");

    switch(type){
        case "radio": radioSelected();
            break;
        case "text": selectedOptionDiv.innerHTML = `<input type="text">`
            break;
        case "range": selectedOptionDiv.innerHTML = `<input type="range" min="0" max=5">`
            break;
    }

    questionType.value = type;

}



function radioSelected(){

    underPopUp.innerHTML = `<input type="number" id="radio-numbers" placeholder="How many radio buttons do you want?">
    <button id="radio-add-button" onclick="showRadioButtons()">Add radio buttons</button><br><br>
    `;
}

function showRadioButtons(){

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

        underPopUp.innerHTML += `
        <div class="radio-answes">
            <input type="radio" id="${radioButtonValues[i].value}" name="answer" value="${radioButtonValues[i].value}">
            <label  for="${radioButtonValues[i].value}">${radioButtonValues[i].value}</label>
        </div>
        <br>`

    }
}



