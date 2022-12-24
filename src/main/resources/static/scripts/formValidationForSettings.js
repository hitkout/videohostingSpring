const formFirstName = document.getElementById('formFirstName');
const formLastName = document.getElementById('formLastName');
const formPassword = document.getElementById('formPassword');
const formFile = document.getElementById('formFile');
const firstName = document.getElementById('name');
const file = document.getElementById('file');
const lastName = document.getElementById('surname');
const oldPassword = document.getElementById('oldPassword');
const newPassword = document.getElementById('newPassword');
const newPasswordConfirm = document.getElementById('newPasswordConfirm');

formFirstName.addEventListener('submit', (e) => {
    e.preventDefault();

    if (checkInputFirstName() === true){
        e.currentTarget.submit();
    }
});

formFile.addEventListener('submit', (e) => {
    e.preventDefault();

    if (checkInputFile() === true){
        e.currentTarget.submit();
    }
});

formLastName.addEventListener('submit', (e) => {
    e.preventDefault();

    if (checkInputLastName() === true){
        e.currentTarget.submit();
    }
});

formPassword.addEventListener('submit', (e) => {
    e.preventDefault();

    if (checkInputPassword() === true){
        e.currentTarget.submit();
    }
});

function checkInputFile(){
    if (file.files[0].size > 104857600)
        setErrorForPhoto(file, 'Размер изображения не должен превышать 100 Мб');
    else {
        setSuccessFor(file);
        return true;
    }
}

function checkInputFirstName(){
    const firstNameValue = firstName.value.trim();
    let checker = false;

    if (firstNameValue === ''){
        setErrorFor(firstName, 'Поле имени не может быть пустым');
    } else if (firstNameValue.length < 2 || firstNameValue.length > 50){
        setErrorFor(firstName, 'Поле имени должно содержать от 2 до 50 символов')
    } else {
        setSuccessFor(firstName);
        checker = true;
    }

    return checker;
}

function checkInputLastName(){
    const lastNameValue = lastName.value.trim();
    let checker = false;

    if (lastNameValue === ''){
        setErrorFor(lastName, 'Поле фамилии не может быть пустым');
    } else if (lastNameValue.length < 2 || lastNameValue.length > 50){
        setErrorFor(lastName, 'Поле фамилии должно содержать от 2 до 50 символов')
    } else {
        setSuccessFor(lastName);
        checker = true;
    }

    return checker;
}

function checkInputPassword(){
    const oldPasswordValue = oldPassword.value.trim();
    const newPasswordValue = newPassword.value.trim();
    const newPasswordConfirmValue = newPasswordConfirm.value.trim();
    const regularNewPass = /(?=.*[!@#$%^&*])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}/g;
    let checkerOldPass = false;
    let checkerNewPass = false;
    let checkerNewPassConfirm = false;

    if (oldPasswordValue === ''){
        setErrorFor(oldPassword, 'Поле пароля не может быть пустым');
    } else {
        setSuccessFor(oldPassword);
        checkerOldPass = true;
    }

    if (newPasswordValue === ''){
        setErrorFor(newPassword, 'Поле пароля не может быть пустым');
    } else if (!regularNewPass.test(newPasswordValue)){
        setErrorFor(newPassword, 'Пароль должен содержать от 6 символов, а также хотя бы один спецсимвол и латинскую букву в верхнем регистре');
    }
    else {
        setSuccessFor(newPassword);
        checkerNewPass = true;
    }

    if ((newPasswordValue === newPasswordConfirmValue) && checkerNewPass){
        setSuccessFor(newPasswordConfirm);
        checkerNewPassConfirm = true;
    } else if (checkerNewPass) {
        setErrorFor(newPassword, 'Пароли не совпадают');
        setErrorFor(newPasswordConfirm, 'Пароли не совпадают');
    }

    return (checkerOldPass && checkerNewPass && checkerNewPassConfirm);
}

function setErrorFor(input, message){
    const formControl = input.parentElement;
    const small = formControl.querySelector('small');

    small.innerText = message;
    formControl.className = 'form-control-settings error';
}

function setErrorForPhoto(input, message){
    const formControl = input.parentElement;
    const small = formControl.querySelector('small');

    small.innerText = message;
    formControl.className = 'label form-control-settings error';
}

function setSuccessFor(input){
    const formControl = input.parentElement;
    formControl.className = 'form-control-settings success';
}