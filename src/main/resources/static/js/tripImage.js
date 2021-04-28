"use strict";


const brokeTrip = document.querySelectorAll('.viewAll');
console.log(brokeTrip)
console.log(brokeTrip[0].src);

brokeTrip.forEach(replaceEmptySrc2);
function replaceEmptySrc2(brokeTrip) {
    if (brokeTrip.getAttribute('src') === '') {
        brokeTrip.src = '/imgs/gray_plane.png';
    }
}
const brokeimg = document.querySelectorAll('.profile-icon-small');
console.log(brokeimg)
console.log(brokeimg[0].src);

brokeimg.forEach(replaceEmptySrc3);
function replaceEmptySrc3(image) {
    if (image.getAttribute('src') === '') {
        image.src = '/imgs/blank-profile-picture-973460_1280.png';
    }
}