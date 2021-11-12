function createAnswerPopUp(id){

    const form = `
            <form action="/api/newAnswer" method="POST">
                <input type="hidden" name="questionId" value="${id}">
                <input type="hidden" name="questionType" value="${questionType}">
                <input type="hidden" name="userId" value="${1}">
                <div id="answer-form"></div>
                <button type="submit">SUBMIT</button> 
            </form>
        `;

    fetch(`/api/v2/question?questionId=${questionId}&questionType=${questionType}`).then(function (res) {
        return res.text()
    }).then(function (text) {
        console.log(text)
        document.querySelector("#answer-form").innerHTML += text;
    }).catch(function(error) {
        console.log(error)
    });

    return form;
}