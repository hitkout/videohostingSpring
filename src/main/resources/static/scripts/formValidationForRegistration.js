    const form = document.getElementById('form');
    const email = document.getElementById('email');
    const firstName = document.getElementById('firstName');
    const lastName = document.getElementById('lastName');
    const password = document.getElementById('password');
    const regularRegPass = /(?=.*[!@#$%^&*])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}/g;

    form.addEventListener('submit', (e) => {
    e.preventDefault();

    if (checkInputs() === 4){
    e.currentTarget.submit();
}
});

    function checkInputs(){
    const emailValue = email.value.trim();
    const firstNameValue = firstName.value.trim();
    const lastNameValue = lastName.value.trim();
    const passwordValue = password.value.trim();
    let count = 0;

    if (emailValue === ''){
        setErrorFor(email, 'Поле почты не может быть пустым');
    } else if (emailValue.length > 254){
        setErrorFor(email, 'Почта не должна превышать 254 символов');
    }else if (!isEmail(emailValue)){
        setErrorFor(email, 'Некорректный адрес электронной почты');
    } else {
        setSuccessFor(email);
        count++;
}

    function isEmail(email) {
    return /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(email);
}

    if (firstNameValue === ''){
    setErrorFor(firstName, 'Поле имени не может быть пустым');
}else {
    setSuccessFor(firstName);
    count++;
}

    if (lastNameValue === ''){
    setErrorFor(lastName, 'Поле фамилии не может быть пустым');
}else {
    setSuccessFor(lastName);
    count++;
}

    if (passwordValue === ''){
        setErrorFor(password, 'Поле пароля не может быть пустым');
    } else if (!regularRegPass.test(passwordValue)){
        setErrorFor(password, 'Пароль должен содержать от 6 символов, а также хотя бы один спецсимвол и латинскую букву в верхнем регистре');
    } else {
    setSuccessFor(password);
    count++;
}

    return count;
}

    function setErrorFor(input, message){
    const formControl = input.parentElement;
    const small = formControl.querySelector('small');

    small.innerText = message;
    formControl.className = 'form-control error';
}

    function setSuccessFor(input){
    const formControl = input.parentElement;
    formControl.className = 'form-control success';
}