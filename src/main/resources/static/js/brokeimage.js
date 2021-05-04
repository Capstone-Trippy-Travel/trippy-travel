"use strict";
const brokeFriend = document.querySelectorAll('.friend-image,#profileImage');
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
const brokeTrip = document.querySelectorAll('.trip-image');
console.log(brokeTrip)
console.log(brokeTrip[0].src);

brokeTrip.forEach(replaceEmptySrc2);
function replaceEmptySrc2(brokeTrip) {
    if (brokeTrip.getAttribute('src') === '') {
        brokeTrip.src = '/imgs/gray_plane.png';
    }
}
const brokeImages = document.querySelectorAll('#my-pictures');
console.log(brokeImages)
console.log(brokeImages[0].src);

brokeImages.forEach(replaceEmptySrc3);
function replaceEmptySrc3(brokeTrip) {
    if (brokeTrip.getAttribute('src') === '') {
        brokeTrip.src = '/imgs/no_user_pics.jpeg';
    }
}