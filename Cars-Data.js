const proxyUrl = 'http://localhost:3000/api';
const url = `${proxyUrl}/specs?key=rqx8xyzf5_uto6mwpym_php2r1u28&deepdata=1&vin=ZPBUA1ZL4NLA19618`;

// Make a GET request
fetch(url, {
    method: 'GET',
    headers: {
        "Content-Type":"application/json"
        // Add any other headers if required
    },
})
    .then(response => {
        // Check if the request was successful (status code 200)
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // Parse the JSON response
        return response.json();
    })
    .then(data => {
        // Log the data to the console
        console.log(data);
    })
    .catch(error => {
        // Log any errors to the console
        console.error('Error:', error);
    });
