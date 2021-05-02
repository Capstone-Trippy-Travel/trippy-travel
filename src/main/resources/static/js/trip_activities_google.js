
mapboxgl.accessToken = mapBoxToken
console.log(window.location.pathname)

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


// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBIwzALxUPNbatRBj3Xi1Uhp0fFzwWNBkE&libraries=places">
let map;
let service;
let infowindow;
let pageLoads=0;

function initMap() {
    geocode(searchValue.value, mapBoxToken).then(function (result) {
        let lat=result[0];
        let long=result[1];

    const location = new google.maps.LatLng(long,lat);
    infowindow = new google.maps.InfoWindow();
    if (pageLoads===0) {
        map = new google.maps.Map(document.getElementById("map"), {
            center: location,
            zoom: 12,
        });
    }
    pageLoads++;

    service = new google.maps.places.PlacesService(map);
        service.nearbySearch({
                bounds: map.getBounds(),
                keyword: venue.value
        }, (results, status) => {
            console.log(results)
        if (status === google.maps.places.PlacesServiceStatus.OK && results) {
            venueList.innerHTML="";
            for (let i = 0; i < results.length; i++) {
                createVenueCard(results[i], i)
            }
            map.setCenter(results[0].geometry.location);
        }
    });

        //when map changes, re-run this function
        google.maps.event.addListener(map,'bounds_changed', function() {
            console.log("map bounds changed")
            service.nearbySearch({
                bounds: map.getBounds(),
                keyword: venue.value
            }, (results, status) => {
                console.log(results)
                if (status === google.maps.places.PlacesServiceStatus.OK && results) {
                    venueList.innerHTML = "";
                    clearMarkers();
                    for (let i = 0; i < results.length; i++) {
                        createMarker(results[i]);
                        createVenueCard(results[i], i)
                    }
                    // map.setCenter(results[0].geometry.location);
                }
            })
        })
})
}

function createMarker(place) {
    const marker = new google.maps.Marker({
        map,
        position: place.geometry.location,
    });
    markers.push(marker);

    return marker
}

function setMapOnAll(map) {
    for (let i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
    setMapOnAll(null);
}

function toggleBounce(marker) {
    if (markers!=undefined) {
        for (let i = 0; i < markers.length; i++) {
            markers[i].setAnimation(null)
        }
    }
    if (marker.getAnimation() !== null) {
        marker.setAnimation(null);
    } else {
        marker.setAnimation(google.maps.Animation.BOUNCE);
    }
}

function createVenueCard(place, index){
    // console.log(place.opening_hours.isOpen)
    // console.log(place.photos[0].getUrl);

    let marker = createMarker(place);

        let venueCard = document.createElement("div");
        venueCard.setAttribute("class", "card venueSearchCard");

        //adding event listener to card, will toggle marker animation bounce.
        venueCard.addEventListener("mouseenter", ()=>{
            toggleBounce(marker)
        })

    venueCard.addEventListener("mouseleave", ()=>{
        toggleBounce(marker)
    })



        let html = "";
        // if(place.photos!=undefined) {
        //     // imageUrl = place.photos[0].getUrl({height: 200},{width:200});
        //     // html += `<img class="card-img-top" src="${imageUrl}" alt="Card image cap">`
        // }
        html += `<div class="card-body">`
        html += `<h5 class="card-title">${place.name}</h5>`
        html += `<p class="card-text">${place.vicinity}</p>`
        html += `<p class="card-text">${place.rating} stars - ${place.user_ratings_total} reviews</p>`


    html += `</div>`
        venueCard.innerHTML = html;

        let buttonsDiv=document.createElement("div");
        buttonsDiv.setAttribute("class", "buttonsDiv")

        let venueDetailsButton=document.createElement("button");
        venueDetailsButton.setAttribute("class", "btn btn-primary float-left venueDetailsButton btn-sm" )
        venueDetailsButton.innerText="See Details"

    let saveButton=document.createElement("button");
    saveButton.setAttribute("class", "saveButton btn btn-success btn-sm float-right ")
    saveButton.innerHTML="save"



    buttonsDiv.appendChild(venueDetailsButton);
    buttonsDiv.appendChild(saveButton)
    venueCard.appendChild(buttonsDiv)

        venueList.appendChild(venueCard)

        // venueCard.addEventListener("click", () => {
        //      getVenueDetails(place.place_id)
        // })

    saveButton.addEventListener("click",()=>{
        console.log("clicked saved button")

        clickedPlace.innerHTML="";

        service = new google.maps.places.PlacesService(map);
        service.getDetails({
            placeId:place.place_id
        }, (place, status) => {
            console.log(place);
            addActivityToDatabase(place, venueCard);
        })
    })

    venueDetailsButton.addEventListener("click", ()=>{
        getVenueDetails(place.place_id)
    })
    //
    marker.addListener("click", ()=>{
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
        html += `<div class="card-body" style="text-align: center">`
        html += `<h5 class="card-title">${place.name}</h5>`
        html += `<p class="card-text">${place.formatted_address}</p>`
        html += `<p class="card-text">${place.rating} stars - ${place.user_ratings_total} reviews</p>`
        html += `<p class="card-text" style="color: blue"><a href="${place.website}" style="color:blue">Visit Website</a></p>`
        html += `<p class="card-text">${place.formatted_phone_number}</p>`

        //will add formatted hours
        //will format hours to not show up in a block
        console.log(place.opening_hours.weekday_text)
        let hoursArray=place.opening_hours.weekday_text
        html+='<p>'
        for (let i=0; i<hoursArray.length; i++){
            html+=hoursArray[i]
            if (i!==hoursArray.length-1){
                html+='<br>'
            }
        }
        html+='</p>'

        // html += `<p class="card-text">${place.opening_hours.weekday_text}</p>`

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



        venueDetailsCard.appendChild(exitButton);
        venueDetailsCard.appendChild(saveButton)

        let clickedPlace=document.getElementById("clickedPlace");
        clickedPlace.appendChild(venueDetailsCard)

        saveButton.addEventListener("click",()=>{
            addActivityToDatabase(place, clickedPlace);

        })
})
}

searchButton.addEventListener("click", ()=>{
    initMap();
})

venueButton.addEventListener("click", ()=>{
    initMap();
})

function addActivityToDatabase(place, venueCard){
    let placeName=document.getElementById("place");
    placeName.value=place.name;

    let address=document.getElementById("address");
    address.value=place.formatted_address;

    let rating= document.getElementById("rating");
    rating.value=place.rating;

    let reviews=document.getElementById("reviews");
    reviews.value=place.user_ratings_total;

    let website=document.getElementById("website");
    website.value=place.website;

    let phone=document.getElementById("phone");
    phone.value=place.formatted_phone_number;
    if(place.opening_hours!=undefined) {
        let hours = document.getElementById("hours");
        hours.value = place.opening_hours.weekday_text;
    }
    console.log(place.place_id)
    let placeId=document.getElementById("placeId")
    placeId.value=place.place_id;

    let lat=document.getElementById("lat");
    lat.value= place.geometry.viewport.La.g

    let lng=document.getElementById("lng")
    lng.value=place.geometry.viewport.Ua.g

    let photoURL=document.getElementById("photoURL");
    photoURL.value=place.photos[0].getUrl({maxHeight: 300});

    clickedPlace.innerHTML="";

    $.ajax({
        type: "POST",
        url: window.location.pathname,
        data: $("#activityForm").serialize(), // serializes the form's elements.
        success: function(data)
        {
            console.log(data);
            console.log("success!!")

            let successfulSaveDiv=document.createElement("h3");
            successfulSaveDiv.setAttribute("class", "successfulSaveDiv")
            successfulSaveDiv.innerText="Activity Saved!"

            venueCard.appendChild(successfulSaveDiv)

            setInterval(function(){
                successfulSaveDiv.style.display="none";
            }, 1500)


        }
    });
}
