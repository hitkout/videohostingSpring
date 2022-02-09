const formPhoto = document.getElementById('formPhoto');
const filePhoto = document.getElementById('filePhoto');
const photoTitle = document.getElementById('photoTitle');

formPhoto.addEventListener('submit', (e) => {
    e.preventDefault();

    if (checkInputFile() === true && checkInputTitle() === true){
        e.currentTarget.submit();
    }
});

function checkInputFile(){
    if (filePhoto.files[0].size > 104857600)
        setErrorFor(filePhoto, 'Размер изображения не должен превышать 100 Мб');
    else {
        setSuccessFor(filePhoto);
        return true;
    }
}

function checkInputTitle(){
    const photoTitleValue = photoTitle.value.trim();

    if (photoTitleValue === ''){
        setErrorFor(photoTitle, 'Название фотографии не может быть пустым');
    } else if (photoTitleValue.length < 10 || photoTitleValue.length > 100){
        setErrorFor(photoTitle, 'Название фотографии должно содержать от 10 до 100 символов')
    } else {
        setSuccessFor(photoTitle);
        return true;
    }
}

function setErrorFor(textarea, message){
    const formControl = textarea.parentElement;
    const small = formControl.querySelector('small');

    small.innerText = message;
    formControl.className = 'form-control-photo error';
}

function setSuccessFor(textarea){
    const formControl = textarea.parentElement;
    formControl.className = 'form-control-photo success';
}