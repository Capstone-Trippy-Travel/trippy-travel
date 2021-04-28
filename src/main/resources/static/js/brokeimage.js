"use strict";
const brokeFriend = document.querySelectorAll('.friend-image');
console.log(brokeFriend)
console.log(brokeFriend[0].src);

brokeFriend.forEach(replaceEmptySrc);
function replaceEmptySrc(image) {
    if (image.getAttribute('src') === '') {
        image.src = '/imgs/blank-profile-picture-973460_1280.png';
    }
}
const brokeGroup = document.querySelectorAll('.group-image');
console.log(brokeGroup)
console.log(brokeGroup[0].src);

brokeGroup.forEach(replaceEmptySrc1);
function replaceEmptySrc1(brokeGroup) {
    if (brokeGroup.getAttribute('src') === '') {
        brokeGroup.src = '/imgs/group.png';
    }
}
