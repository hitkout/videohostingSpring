let arr = document.getElementsByClassName('id');
let lastId = arr[arr.length-1].innerHTML - 1;
    window.addEventListener('scroll', ()=>{
        const documentRect = document.documentElement.getBoundingClientRect();
        if (documentRect.bottom < document.documentElement.clientHeight + 150 && lastId !== 0){
            load();
        }
    })
    function load(){
        let xhr = new XMLHttpRequest();
        let url = "http://localhost:8080/auth/success/" + lastId;
        xhr.open("GET", url);
        xhr.setRequestHeader("Content-type", "application/json");

        xhr.onload = function (){
            let jsonResponse = JSON.parse(xhr.responseText);
            if (jsonResponse != null && jsonResponse.length > 0){
                for (let i = 0; i < jsonResponse.length; i++){
                    let newDiv = document.createElement('div');
                    newDiv.innerHTML = "<p class='id'>" + jsonResponse[i].id + "</p>"
                        + "<p>" + jsonResponse[i].tag + "</p>"
                        + "<p>" + jsonResponse[i].text + "</p>";
                    document.getElementById('main').appendChild(newDiv);
                }
                lastId = arr[arr.length-1].innerHTML - 1;
            }
        };
        xhr.send();
    }