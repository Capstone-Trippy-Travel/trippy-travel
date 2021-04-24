
mapboxgl.accessToken = mapBoxToken


let searchValue=document.getElementById("tripLocationInput");
// searchValue.value="San Antonio"

let currentInterval="";

// let markers=[]

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


// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBIwzALxUPNbatRBj3Xi1Uhp0fFzwWNBkE&libraries=places">
let map;
let service;
let pageLoads=0;



function initMap() {
    let locationSearch="";
    let zoom=4;
    if (searchValue.value===""){
         locationSearch="United States";
    } else{
        locationSearch=searchValue.value;
        zoom=11;
    }
    geocode(locationSearch, mapBoxToken).then(function (result) {
        let lat=result[0];
        let long=result[1];

        const location = new google.maps.LatLng(long,lat);
            map = new google.maps.Map(document.getElementById("map"), {
                center: location,
                zoom: zoom,
            });

    })
}

searchValue.addEventListener("keyup", ()=>{
    clearTimeout(currentInterval);
    currentInterval=setTimeout(function(){
        initMap();
    },500);

});

