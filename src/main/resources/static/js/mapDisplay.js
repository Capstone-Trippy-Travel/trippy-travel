mapboxgl.accessToken = mapBoxToken

//array to hold all markers, store for deletion and animation
let markers=[];

let map;
let service;
let infowindow;

//grabs all of the locations from each trip displayed on the page
let tripsLocations=document.getElementsByClassName("tripLocation");

function geocode(search, token) {
    var baseUrl = 'https://api.mapbox.com';
    var endPoint = '/geocoding/v5/mapbox.places/';
    return fetch(baseUrl + endPoint + encodeURIComponent(search) + '.json' + "?" + 'access_token=' + token)
        .then(function(res) {
            return res.json();
            // to get all the data from the request, comment out the following three lines...
        }).then(function(data) {
            return data.features[0].center;
        });
}

//google maps api called whne page is loaded
function initMap() {

    //setting map center to center of america
    let tripLocation=new google.maps.LatLng(0,0);

    //creates new map
    map = new google.maps.Map(document.getElementById("map"), {
        center: tripLocation,
        zoom: 2,
    });

    //loops through trip locations, and generates a marker for each
    for (let i=0; i<tripsLocations.length; i++) {
        let tripLocationString=tripsLocations[i].innerText
        console.log(tripLocationString)
        geocode(tripLocationString, mapBoxToken).then(function (result) {
            let lat = result[0];
            let long = result[1];

            let tripCoordinates= new google.maps.LatLng(long, lat);
            createMarker(tripCoordinates)

        })
    }


}

function createMarker(tripLocation) {
    console.log('creating marker')
    console.log(tripLocation)
    const marker = new google.maps.Marker({
        map,
        position: tripLocation,
    });
    markers.push(marker);
    return marker
}

