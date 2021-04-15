mapboxgl.accessToken = MapBoxToken
// let fourSquareId=""
// let fourSquarePassword=""

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


//moves the map to the searched area
function getSearchLocation(location) {
    geocode(location, mapBoxToken).then(function (result) {
        map.setCenter(result)
    })
}



//creates initial settings for maps.
var map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    // center: getSearchCoordinates(searchValue.value),
    zoom: 9.5,
    dragRotate: true
})

getSearchLocation(searchValue.value)

//colors array that we will loop through to create different colored markers and venue containers.
let colors=["red", "yellow", "blue", "green", "gray", "purple", "pink", "orange", "brown", "white" ]

function searchMapForVenues(venue) {
    $.ajax({
        dataType: "json",
        url: `https://api.foursquare.com/v2/venues/search?client_id=${fourSquareId}&client_secret=${fourSquarePassword}&v=20180323&limit=10&near=${searchValue.value}&query=${venue}`,
        data: {},
        success: function (locations) {
            // Code for handling API response

            //remove all previous markers from map after each new search.
            for (let marker of markers) {
                marker.remove();
            }

            //clear list of venues on screen already
            venueList.innerHTML = "";

            //creates an array with the ten venues returned from the foursquare api call
            let venues = locations.response.venues;

            //resets the color array index to 0
            let colorIndex = 0;

            //looping through the venue
            for (let singleVenue of venues) {

                //this block of code creates the colored div containing the venue name and address
                let venueDiv = document.createElement("div");
                venueDiv.style.background = colors[colorIndex]
                let venueTitle = document.createElement("h4")
                venueTitle.innerText = singleVenue.name;
                let venueLocation = document.createElement("div");
                venueLocation.innerText = singleVenue.location.address;
                venueDiv.appendChild(venueTitle);
                venueDiv.appendChild(venueLocation);
                venueList.appendChild(venueDiv)

                //creates a colored marker for the venue on the map
                var marker = new mapboxgl.Marker({
                    color: colors[colorIndex]
                })
                    .setLngLat([singleVenue.location.lng, singleVenue.location.lat])
                    .addTo(map);

                //adds an event listener to the markers.
                marker.getElement().addEventListener("click", () => {
                    let clickedPlace = document.getElementById("clickedPlace");
                    clickedPlace.innerHTML = "";
                    let clickedVenue = venueDiv.cloneNode(true)
                    clickedPlace.appendChild(clickedVenue)

                })

                //adds the marker to the markers array, to be deleted later on when a new search is executed
                markers.push(marker);

                //increases index of colors array, so next div is a different color.
                colorIndex++;
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            // Code for handling errors
        }
    });
}

searchMapForVenues("restaurants")



searchButton.addEventListener("click", ()=>{
    console.log(searchValue.value)

    //will set map location to searched location
    getSearchLocation(searchValue.value)

    //will add markers and venues to screen
    searchMapForVenues(searchValue.value)

})

venueButton.addEventListener("click", ()=>{
    console.log(venue.value)

    //will add markers and venues to screen
    searchMapForVenues(venue.value)

})