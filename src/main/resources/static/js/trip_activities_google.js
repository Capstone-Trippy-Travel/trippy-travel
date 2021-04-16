
mapboxgl.accessToken = mapBoxToken


let venueButton=document.getElementById("venueButton");
let venue=document.getElementById("venue");
let venueList=document.getElementById("venueList")
venue.value="restaurants"

let searchButton=document.getElementById("searchButton");
let searchValue=document.getElementById("searchValue");
// searchValue.value="San Antonio"

let markers=[]

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
//
//
// //moves the map to the searched area
//
//
//
// let map;
// let service;
// let infowindow;
//
// function initMap() {
//     function getSearchLocation(location) {
//         geocode(location, mapBoxToken).then(function (result) {
//             console.log(result)
//             const zoomLocation = new google.maps.LatLng(result[0], result[1]);
//             infowindow = new google.maps.InfoWindow();
//             map = new google.maps.Map(document.getElementById("map"), {
//                 center: zoomLocation,
//                 zoom: 10,
//             });
//             const location = {
//                 lat: Number(result[0]),
//                 long: Number(result[1])
//             };
//             service = new google.maps.places.PlacesService(map);
//             service.nearbySearch({
//                 location: new google.maps.LatLng(result[0], result[1]),
//                 radius: 1000,
//                 keyword: venue.value
//             }, (results, status) => {
//                 console.log(results)
//                 if (status === google.maps.places.PlacesServiceStatus.OK && results) {
//                     for (let i = 0; i < results.length; i++) {
//                         createMarker(results[i]);
//                     }
//                     // map.setCenter(results[0].geometry.location);
//                 }
//             });
//
//
//         })
//
//     }
//     getSearchLocation(searchValue.value)
//
// }
// console.log("about to create map")
// initMap();
//
// function createMarker(place) {
//     if (!place.geometry || !place.geometry.location) return;
//     const marker = new google.maps.Marker({
//         map,
//         position: place.geometry.location,
//     });
//     google.maps.event.addListener(marker, "click", () => {
//         infowindow.setContent(place.name || "");
//         infowindow.open(map);
//     });
// }

// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBIwzALxUPNbatRBj3Xi1Uhp0fFzwWNBkE&libraries=places">
let map;
let service;
let infowindow;

function initMap() {
    geocode(searchValue.value, mapBoxToken).then(function (result) {
        console.log(result)
        let lat=result[0];
        let long=result[1];
        let newLat=lat-1.1;
        let newLong=long+1.1;
        console.log(newLat);
        console.log(newLong);
    const location = new google.maps.LatLng(long,lat);
    infowindow = new google.maps.InfoWindow();
    map = new google.maps.Map(document.getElementById("map"), {
        center: location,
        zoom: 15,
    });
    const request = {
        query: "Museum of Contemporary Art Australia",
        fields: ["name", "geometry"],
    };
    service = new google.maps.places.PlacesService(map);
        service.nearbySearch({
                location: location,
                radius: 1000,
                keyword: venue.value
        }, (results, status) => {
            console.log(results)
        if (status === google.maps.places.PlacesServiceStatus.OK && results) {
            for (let i = 0; i < results.length; i++) {
                createMarker(results[i]);
            }
            // map.setCenter(results[0].geometry.location);
        }
    });
})
}

function createMarker(place) {
    if (!place.geometry || !place.geometry.location) return;
    const marker = new google.maps.Marker({
        map,
        position: place.geometry.location,
    });
    google.maps.event.addListener(marker, "click", () => {
        infowindow.setContent(place.name || "");
        infowindow.open(map);
    });
}