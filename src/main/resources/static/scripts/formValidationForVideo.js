const formVideo = document.getElementById('formVideo');
const fileVideo = document.getElementById('fileVideo');
const videoTitle = document.getElementById('videoTitle');
const videoDescription = document.getElementById('videoDescription');

formVideo.addEventListener('submit', (e) => {
    e.preventDefault();

    if (checkInputFile() === true && checkInputTitle() === true && checkInputDescription() === true){
        e.currentTarget.submit();
    }
});

function checkInputFile(){
    if (fileVideo.files[0].size > 104857600)
        setErrorFor(fileVideo, 'Размер видео не должен превышать 100 Мб');
    else {
        setSuccessFor(fileVideo);
        return true;
    }
}

function checkInputTitle(){
    const videoTitleValue = videoTitle.value.trim();

    if (videoTitleValue === ''){
        setErrorFor(videoTitle, 'Название видео не может быть пустым');
    } else if (videoTitleValue.length < 10 || videoTitleValue.length > 100){
        setErrorFor(videoTitle, 'Название видео должно содержать от 10 до 100 символов')
    } else {
        setSuccessFor(videoTitle);
        return true;
    }
}

function checkInputDescription(){
    const videoDescriptionValue = videoDescription.value.trim();

    if (videoDescriptionValue === ''){
        setErrorFor(videoDescription, 'Описание видео не может быть пустым');
    } else if (videoDescriptionValue.length < 10 || videoDescriptionValue.length > 255){
        setErrorFor(videoDescription, 'Описание видео должно содержать от 10 до 255 символов')
    } else {
        setSuccessFor(videoDescription);
        return true;
    }
}

function setErrorFor(textarea, message){
    const formControl = textarea.parentElement;
    const small = formControl.querySelector('small');

    small.innerText = message;
    formControl.className = 'form-control-video error';
}

function setSuccessFor(textarea){
    const formControl = textarea.parentElement;
    formControl.className = 'form-control-video success';
}