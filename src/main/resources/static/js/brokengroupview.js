"use strict";

const brokeGroup = document.querySelectorAll('.profile-icon,');
console.log(brokeGroup)
console.log(brokeGroup[0].src);

brokeGroup.forEach(replaceEmptySrc1);
function replaceEmptySrc1(brokeGroup) {
    if (brokeGroup.getAttribute('src') === '') {
        brokeGroup.src = '/imgs/group.png';
    }
}
const brokeGroup1 = document.querySelectorAll('.group-pics');
console.log(brokeGroup1)
console.log(brokeGroup1[0].src);

brokeGroup1.forEach(replaceEmptySrc);
function replaceEmptySrc(brokeGroup) {
    if (brokeGroup.getAttribute('src') === '') {
        brokeGroup.src = '/imgs/group.png';
    }
}
const brokeTrip = document.querySelectorAll('.trip-image');
console.log(brokeTrip)
console.log(brokeTrip[0].src);

brokeTrip.forEach(replaceEmptySrc2);
function replaceEmptySrc2(brokeTrip) {
    if (brokeTrip.getAttribute('src') === '') {
        brokeTrip.src = '/imgs/gray_plane.png';
    }
}