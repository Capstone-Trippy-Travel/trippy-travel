let markers=[];

let map;
let service;
let infowindow;


function initMap() {
    //setting map center to center of america
    let tripLocation=new google.maps.LatLng(39.8283,-98.5795);

    map = new google.maps.Map(document.getElementById("map"), {
        center: tripLocation,
        zoom: 5,
    });
}
