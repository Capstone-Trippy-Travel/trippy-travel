// Login Validation
(function () {
    $(function(){
        $('.form').on('submit', function(event){
            if (myForm.username.value == '' || myForm.password.value == '') {
                event.preventDefault();
                event.stopPropagation();
                alert('Please enter all required fields');
                return false;
            }
            else {
                myForm.submit();
            }
        });
    });
})();

// Create User Validation
$(function(){
    $('.myForm').on('submit', function(event){
        if (myForm.first-name.value == '' || myForm.username.value == '' || myForm.email.value == '' || myForm.password.value == '') {
            event.preventDefault();
            event.stopPropagation();
            alert('Please enter all required fields');
            return false;
        }
        else {
            myForm.submit();
        }
    });
});
//Create Trip Validation
$(function(){
    $('.tripForm').on('submit', function(event){
        if (myForm.name.value == '' || myForm.location.value == '' || myForm.startDate.value == '' || myForm.endDate.value == '') {
            event.preventDefault();
            event.stopPropagation();
            alert('Please enter all required fields');
            return false;
        }
        else {
            myForm.submit();
        }
    });
});

//Create Group Validation
$(function(){
    $('.groupForm').on('submit', function(event){
        if (myForm.name.value == '') {
            event.preventDefault();
            event.stopPropagation();
            alert('Please enter all required fields');
            return false;
        }
        else {
            myForm.submit();
        }
    });
});