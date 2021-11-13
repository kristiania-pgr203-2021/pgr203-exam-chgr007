let userId = 0;

//Kilde: https://stackoverflow.com/questions/38552003/how-to-decode-jwt-token-in-javascript-without-using-a-library
function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
}

// datoene må ganges med 1000 for å få millisekunder som js bruker i date.
const cookie = document.cookie;
if (cookie.length > 0) {
    console.info("Found token");

    const tokenEncoded = cookie.substr(cookie.indexOf("=") + 1, cookie.length)
    const token = parseJwt(tokenEncoded);
    console.info(token);
    userId = token.jti;
}

function createAnswerPopUp(id){

    const form = `
            <form action="/api/newAnswer" method="POST">
                <input type="hidden" name="questionId" value="${id}">
                <input type="hidden" name="questionType" value="${questionType}">
                <input type="hidden" name="userId" value="${userId}">
                <div id="answer-form"></div>
                <button type="submit" onclick="refresh()">SUBMIT</button> 
            </form>
        `;

    fetch(`/api/newAnswer?questionId=${questionId}&questionType=${questionType}`).then(function (res) {
        return res.text()
    }).then(function (text) {
        console.log(text)
        document.querySelector("#answer-form").innerHTML += text;
    }).catch(function(error) {
        console.log(error)
    });

    return form;
}