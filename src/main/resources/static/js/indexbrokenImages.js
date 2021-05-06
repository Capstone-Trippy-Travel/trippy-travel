"use strict";
const brokeTrip = document.querySelectorAll('.indexImage    ');
console.log(brokeTrip)
console.log(brokeTrip[0].src);

brokeTrip.forEach(replaceEmptySrc2);
function replaceEmptySrc2(brokeTrip) {
    if (brokeTrip.getAttribute('src') === '') {
        brokeTrip.src = '/imgs/gray_plane.png';
    }
}