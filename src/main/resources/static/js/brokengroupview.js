"use strict";

const brokeGroup = document.querySelectorAll('.profile-image');
console.log(brokeGroup)
console.log(brokeGroup[0].src);

brokeGroup.forEach(replaceEmptySrc1);
function replaceEmptySrc1(brokeGroup) {
    if (brokeGroup.getAttribute('src') === '') {
        brokeGroup.src = '/imgs/group.png';
    }
}
const brokeGroup23 = document.querySelectorAll('#groupMember');
console.log(brokeGroup23)
console.log(brokeGroup23[0].src);

brokeGroup23.forEach(replaceEmptySrc12);
function replaceEmptySrc12(brokeGroup) {
    if (brokeGroup.getAttribute('src') === '') {
        brokeGroup.src = '/imgs/blank-profile-picture-973460_1280.png';
    }
}
const brokeGroup1 = document.querySelectorAll('#groupPics');
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