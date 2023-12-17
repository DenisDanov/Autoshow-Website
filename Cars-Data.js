const urlParams = new URLSearchParams(window.location.search);
let carParam = urlParams.get('car');
carParam = carParam.split(`/`)[1];

const apiUrl = 'https://carapi.app/api/engines';
const params = {
    make: carParam.split(`-`)[0],
    year: carParam.split(`-`)[2].split(`.`)[0],
    model: carParam.split(`-`)[1]
};

// Constructing the URL with parameters
const urlWithParams = new URL(apiUrl);
Object.keys(params).forEach(key => urlWithParams.searchParams.append(key, params[key]));

// Making the Fetch GET request
fetch(urlWithParams, {
    method: `GET`,
    headers: {
        "Accept": "application/json",
        "Content-Type": "application/json",
        "Authorization": "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjYXJhcGkuYXBwIiwic3ViIjoiNzU3MTMwNDAtOWVjYy00NGI1LWIxY2MtMmI0ZWJjNjM5YTdlIiwiYXVkIjoiNzU3MTMwNDAtOWVjYy00NGI1LWIxY2MtMmI0ZWJjNjM5YTdlIiwiZXhwIjoxNzAzNDE4MDIwLCJpYXQiOjE3MDI4MTMyMjAsImp0aSI6IjI2NDVjN2JhLTJhOTgtNDlhMS1hODMzLWUxNWFmZjhmMWRhNSIsInVzZXIiOnsic3Vic2NyaWJlZCI6ZmFsc2UsInN1YnNjcmlwdGlvbiI6bnVsbCwicmF0ZV9saW1pdF90eXBlIjoiaGFyZCJ9fQ.QhbFDLVGX77KuMA4jXnYsHQ7z6gCU7hC7N3BFvhFpA8"
    }
})
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.error('Error:', error));
