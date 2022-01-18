let arrPhoto = document.getElementsByClassName('idPhoto');
let lastIdPhoto = arrPhoto[arrPhoto.length-1].innerHTML - 1;
window.addEventListener('scroll', ()=>{
    const documentRectPhoto = document.documentElement.getBoundingClientRect();
    if (documentRectPhoto.bottom < document.documentElement.clientHeight + 150 && lastIdPhoto !== minFromDb - 1){
        loadPhoto();
    }
})
function loadPhoto(){
    let xhrPhoto = new XMLHttpRequest();
    let urlPhoto = "http://localhost:8080/auth/success/channel/" + userId + "/photos/" + lastIdPhoto;
    xhrPhoto.open("GET", urlPhoto);
    xhrPhoto.setRequestHeader("Content-type", "application/json");

    xhrPhoto.onload = function (){
        let jsonResponsePhoto = JSON.parse(xhrPhoto.responseText);
        if (jsonResponsePhoto != null && jsonResponsePhoto.length > 0){
            for (let i = 0; i < jsonResponsePhoto.length; i++){
                let newDivPhoto = document.createElement('div');
                newDivPhoto.className = 'catalogCard';
                newDivPhoto.innerHTML = "<p class='idPhoto'>" + jsonResponsePhoto[i].id + "</p>" + "<img class='catalogImg' src='/img/" + jsonResponsePhoto[i].filename + "' alt=''/>";
                document.getElementById('mainPhoto').appendChild(newDivPhoto);
            }
            let id = document.getElementById('mainPhoto').lastElementChild.getElementsByClassName('catalogCard').length;
            lastIdPhoto = arrPhoto[arrPhoto.length-1].innerHTML - 1;
            console.log("id: " + id);
            console.log("last: " + lastIdPhoto);
            if (id === lastIdPhoto){
                lastIdPhoto -= 3;
                console.log(lastIdPhoto);
            }
        }
    };
    xhrPhoto.send();
}