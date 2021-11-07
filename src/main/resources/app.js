//https://stackoverflow.com/questions/38552003/how-to-decode-jwt-token-in-javascript-without-using-a-library
function parseJwt (token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
};

const cookie = document.cookie;
const tokenEncoded = cookie.substr(cookie.indexOf("=")+1,cookie.length)
const token = parseJwt(tokenEncoded);
console.info("User has token")
console.info(token);