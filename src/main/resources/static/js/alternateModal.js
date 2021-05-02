"use strict";
// Get the modal
var modal = document.getElementById("myModal");

// Get the image and insert it inside the modal - use its "alt" text as a caption
var modalImg = document.getElementsByTagName("img");
var captionText = document.getElementsByTagName("p");
function image(event)  {
    modal.style.display = "block";
    modalImg.src = event.target.src;
    captionText.innerHTML = event.target.alt;
}
// Get the <span> element that closes the modal
var spanList = document.getElementsByClassName("close")

// When the user clicks on <span> (x), close the modal
for (let span of spanList) {
    span.onclick = function () {
        modal.style.display = "none";
    }
}

// $(function () {
//     $('#modal').modal('hide');
// });
//Modal for trips


// Like Button
//     (function() {
//         const heart = document.getElementById('heart');
//         heart.addEventListener('click', function() {
//             heart.classList.toggle('red');
//         });
//     })();
// const images = document.querySelectorAll('img');

// const images = document.getElementById('groupPic');
// console.log(images[0].src);
// images.forEach(replaceEmptySrc1);
// function replaceEmptySrc1(image) {
//     if (image.getAttribute('src') === '') {
//         image.src = '/imgs/group.png';
//     }
// }

//will try to make all images equal size, and stretch to container
let imageContainer = document.getElementById("activity-pics")
let imageBox=document.getElementsByClassName("tripImageDiv")[0]
let imageWidth=imageBox.offsetWidth;

let imageContainerWidth=imageContainer.offsetWidth;
console.log(imageContainerWidth)
if (imageContainerWidth>750){
    $('.tripImageDiv').css('width','33.3%')
    imageWidth=imageBox.offsetWidth;
    $('.tripImageDiv').css('height',imageWidth*0.8+'px')

} else {
    $('.tripImageDiv').css('height', imageWidth * 0.8 + 'px')
}


window.addEventListener('resize', ()=>{
    imageContainerWidth=imageContainer.offsetWidth;
    console.log(imageContainerWidth)
    if (imageContainerWidth>750){
        $('.tripImageDiv').css('width','33.3%')
        imageWidth=imageBox.offsetWidth;
        $('.tripImageDiv').css('height',imageWidth*0.8+'px')

    } else {
        $('.tripImageDiv').css('width','50%')
        imageWidth = imageBox.offsetWidth;
        $('.tripImageDiv').css('height', imageWidth * 0.8 + 'px')
    }

});

