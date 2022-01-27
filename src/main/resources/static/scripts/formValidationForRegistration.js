    const form = document.getElementById('form');
    const email = document.getElementById('email');
    const firstName = document.getElementById('firstName');
    const lastName = document.getElementById('lastName');
    const password = document.getElementById('password');

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
    setErrorFor(email, 'Email cannot be blank');
} else if (!isEmail(emailValue)){
    setErrorFor(email, 'Email is not valid');
} else {
    setSuccessFor(email);
    count++;
}

    function isEmail(email) {
    return /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(email);
}

    if (firstNameValue === ''){
    setErrorFor(firstName, 'FirstName cannot be blank');
}else {
    setSuccessFor(firstName);
    count++;
}

    if (lastNameValue === ''){
    setErrorFor(lastName, 'LastName cannot be blank');
}else {
    setSuccessFor(lastName);
    count++;
}

    if (passwordValue === ''){
    setErrorFor(password, 'Password cannot be blank');
}else {
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