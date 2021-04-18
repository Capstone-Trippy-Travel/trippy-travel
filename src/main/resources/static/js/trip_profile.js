
mapboxgl.accessToken = mapBoxToken

let tripLocation=document.getElementById("tripLocation").innerText;
console.log(tripLocation)

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
let infowindow;

function initMap() {
    geocode(tripLocation, mapBoxToken).then(function (result) {
        let lat=result[0];
        let long=result[1];

        const location = new google.maps.LatLng(long,lat);
        infowindow = new google.maps.InfoWindow();
        map = new google.maps.Map(document.getElementById("map"), {
            center: location,
            zoom: 12,
        });

        let pathUrl=window.location.pathname;
        let pathUrlArray=pathUrl.split("/");
        let tripId=pathUrlArray[pathUrlArray.length-1];

        jQuery.ajax({
            'url': `/trip.json?tripId=${tripId}`,
            success: function (activities) {
                console.log(activities)
                for (let i=0; i<activities.length; i++){
                    let lng=activities[i].lng;
                    let lat=activities[i].lat;
                    createMarker(new google.maps.LatLng(lng,lat));
                    createVenueCard(activities[i])
                }
            },
            error: function (data) {
                console.log(data)
            }
        })

    })
}



function createMarker(location) {
    console.log("creating marker")
      const marker = new google.maps.Marker({
        map,
        position: location,
    });
}

function createVenueCard(place){
    // console.log(place.opening_hours.isOpen)
    // console.log(place.photos[0].getUrl);


    let venueCard = document.createElement("div");
    venueCard.setAttribute("class", "card venueCard");


    let html = "";
    if(place.photoURL) {
        html += `<img class="card-img-top" src="${place.photoURL}" alt="Card image cap">`
    }
    html += `<div class="card-body">`
    html += `<h5 class="card-title">${place.place}</h5>`
    html += `<p class="card-text">${place.rating} stars - ${place.reviews} reviews</p>`


    html += `</div>`
    venueCard.innerHTML = html;

    let venueDetailsButton=document.createElement("button");
    venueDetailsButton.setAttribute("class", "btn btn-primary float-left btn-sm text-center" )
    venueDetailsButton.innerText="See Details"

    venueCard.appendChild(venueDetailsButton)

    let activityList=document.getElementById("activityList")
    activityList.appendChild(venueCard)



    venueDetailsButton.addEventListener("click", ()=>{
        getVenueDetails(place.place_id)
    })
}

function getVenueDetails(id){
    service = new google.maps.places.PlacesService(map);
    service.getDetails({
        placeId:id
    }, (place, status) => {
        console.log(place);
        console.log(place.geometry.location);
        console.log(place.geometry.location.lat)
        console.log(place.geometry.viewport)
        console.log(place.geometry.viewport.La.g)
        console.log(place.geometry.viewport.Ua.g)


        let html = "";
        let venueDetailsCard = document.createElement("div");
        venueDetailsCard.setAttribute("class", "card");
        html+=`<div class="cardImages">`
        if(place.photos.length>0) {
            for (let i=0; i<place.photos.length&&i<3; i++) {
                imageUrl = place.photos[i].getUrl({maxHeight: 300});

                html += `<img src="${imageUrl}" alt="Card image cap">`
            }
        }
        html+="</div>"
        html += `<div class="card-body">`
        html += `<h5 class="card-title">${place.name}</h5>`
        html += `<p class="card-text">${place.formatted_address}</p>`
        html += `<p class="card-text">${place.rating} stars - ${place.user_ratings_total} reviews</p>`
        html += `<p class="card-text"><a href="${place.website}">Visit Website</a></p>`
        html += `<p class="card-text">${place.formatted_phone_number}</p>`
        html += `<p class="card-text">${place.opening_hours.weekday_text}</p>`

        venueDetailsCard.innerHTML=html;



        let exitButton=document.createElement("button");
        exitButton.setAttribute("class", "exitButton btn btn-danger")
        exitButton.innerHTML="close"
        exitButton.addEventListener("click",()=>{
            clickedPlace.innerHTML="";
        })

        let saveButton=document.createElement("button");
        saveButton.setAttribute("class", "saveButton btn btn-success btn-sm text-center")
        saveButton.innerHTML="save"

        saveButton.addEventListener("click",()=>{

            addActivityToDatabase(place);

        })

        venueDetailsCard.appendChild(exitButton);
        venueDetailsCard.appendChild(saveButton)

        let clickedPlace=document.getElementById("clickedPlace");
        clickedPlace.appendChild(venueDetailsCard)
    })
}



