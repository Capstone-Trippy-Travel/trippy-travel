

mapboxgl.accessToken = mapBoxToken

let tripLocation=document.getElementById("tripLocation").innerText;
console.log(tripLocation)

let mapContainer=document.getElementById("map");

let markers=[];
let venueCards=[];

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
    console.log("about to create activity cards")
    geocode(tripLocation, mapBoxToken).then(function (result) {
        let lat=result[0];
        let long=result[1];

        const location = new google.maps.LatLng(long,lat);
        // infowindow = new google.maps.InfoWindow();
        map = new google.maps.Map(document.getElementById("map"), {
            center: location,
            zoom: 11,
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
                    let newMarker=createMarker(new google.maps.LatLng(lng,lat));
                    // let newMarker='';
                    createVenueCard(activities[i], newMarker)
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
    markers.push(marker);
    return marker
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

function createVenueCard(place, marker){
    // console.log(place.opening_hours.isOpen)
    // console.log(place.photos[0].getUrl);

    //creating div for each activity card.
    let venueCard = document.createElement("div");
    venueCard.setAttribute("class", "venueCard col-md-auto");

    //creating div to show image (when not hidden)
    let venueImageDiv=document.createElement("div")
    venueImageDiv.setAttribute("class", "venueImageDiv")



    //creating form to hold activity info : comments, pictures, etc.
    let activityImageForm=document.createElement("form");
    activityImageForm.setAttribute("action", '/trip/'+place.trip.id);
    activityImageForm.setAttribute("method", "Post")
    venueCard.appendChild(activityImageForm)

    let cardBody=document.createElement("div");
    cardBody.setAttribute("class", "venueCardBody")
    activityImageForm.appendChild(cardBody)

    //creating div to hold each venue image, and adding to card body
    let venueImage=document.createElement("img")
    venueImage.setAttribute("class", "venueImage")
    venueImageDiv.appendChild(venueImage);
    cardBody.appendChild(venueImageDiv)

    //creating div for main body of each activity
    let venueInfo=document.createElement("div");
    venueInfo.setAttribute("class", "venueInfo")

    cardBody.appendChild(venueInfo)


    //creating activity name header
    let venueNameHeader=document.createElement("h5");
    venueNameHeader.innerText=place.place;
    venueInfo.appendChild(venueNameHeader)

    //creating activity ratings paragraph
    let venueRatings=document.createElement("p");
    venueRatings.innerText=place.rating + " stars - "+place.reviews + " reviews"
    venueInfo.appendChild(venueRatings);


    let activityInput=document.createElement("input");
    activityInput.setAttribute("value", place.id);
    activityInput.setAttribute("name", "activity_id")
    activityInput.setAttribute("type", "hidden")
    venueInfo.appendChild(activityInput);

    let imageUrlInput=document.createElement("input");
    imageUrlInput.setAttribute("name", "image_url")
    imageUrlInput.setAttribute("type", "hidden")
    venueInfo.appendChild(imageUrlInput)





//
//     html += `</div>`
// html+=` <div class="modal fade" id="exampleModal1" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
//                 <div class="modal-dialog">
//                     <div class="modal-content">
//                         <div class="modal-header">
//                             <h5 class="modal-title" id="exampleModalLabel">Albums</h5>
//                             <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
//                         </div>
//                         <div class="modal-body" th:each="image : ${place.image}">
//                             <table class="table">
//
//                                     <a th:href="">
//                                     <img th:src="" id="" class="img-thumbnail"  alt="...">
//
//                                     </a>
//
//                                 <!--                &lt;!&ndash;    this is our hidden input field and this is what we want to actually pass our image url in. &ndash;&gt;-->
// <!--                                <input type="hidden" id="image"  name="image_url">-->
//
//                                  <!--                &lt;!&ndash;    This is the button a user will press if they want to open the filepicker &ndash;&gt;-->
//                                 <!--                &lt;!&ndash;    note: you might want to change the text in the button after &ndash;&gt;-->
//                                 <!--                &lt;!&ndash;    they have uploaded a image with the filepicker to "change picture" or file. &ndash;&gt;-->
//                                 <!--                <input type="text"  name="image_url">-->
//
//                                 <br>
//
//                             </table>
//                         </div>
//                         <div class="modal-footer">
//
//                         </div>
//
//                 </div>
//
//
// <!--                            <button id="addPicture">addPicture</button>-->
// <!--                            <input class="btn" type="submit" />-->
//                         </div>`
//     cardBody.innerHTML = html;

    let yesButton=document.createElement("button");
    yesButton.setAttribute("id", "yes-button");
    if (place.usersPreviousVote==="like"){
        yesButton.setAttribute("class", "btn btn-success")
    } else {
        yesButton.setAttribute("class", "btn btn-primary")
    }
    yesButton.innerText="Like";
    let counter=document.createElement("div")
    counter.setAttribute("id", "counter");
    counter.innerText=place.voteCount;

    let noButton=document.createElement("button");
    if (place.usersPreviousVote==="dislike") {
        noButton.setAttribute("class", "btn btn-danger")
    } else{
        noButton.setAttribute("class", "btn btn-primary")
    }
    noButton.setAttribute("id", "no-button");
    noButton.innerText="Dislike";

    yesButton.addEventListener('click', function () {

        //sending yes vote to database, and will return list of all vote to update vote Count
        addVoteToDatabase(true, place.id, counter);


        let currentVote=Number(counter.innerText);
        currentVote++;
        counter.innerText=currentVote;
        yesButton.setAttribute("class", "btn btn-success")
        noButton.setAttribute("class", "btn btn-primary")


    });

    noButton.addEventListener('click', function () {
        let currentVote=Number(counter.innerText);
        currentVote--;
        counter.innerText=currentVote;
        noButton.setAttribute("class", "btn btn-danger");
        yesButton.setAttribute("class", "btn btn-primary")

        addVoteToDatabase(false, place.id, counter);


    });

    let voteDiv = document.createElement("div");
    voteDiv.style.display="flex"
    voteDiv.appendChild(yesButton);
    voteDiv.appendChild(counter);
    voteDiv.appendChild(noButton)
    venueInfo.appendChild(voteDiv)

    //creating button to display additional venue details.
    let venueDetailsDiv=document.createElement("div");
    let venueDetailsButton=document.createElement("button");
    venueDetailsButton.setAttribute("class", "btn btn-primary float-left btn-sm text-center" )
    venueDetailsDiv.appendChild(venueDetailsButton)
    venueDetailsButton.innerText="See Details"

    //adding filestack addPicture button
    let fileStackButton=document.createElement("button");
    fileStackButton.setAttribute("class", "addPicture");
    fileStackButton.innerText="Add picture"
    // let pictureIcon=document.createElement("i");
    // pictureIcon.setAttribute("class", "bi bi-images");
    // fileStackButton.appendChild(pictureIcon)

    cardBody.appendChild(fileStackButton)


    venueCard.appendChild(venueDetailsDiv)


    let activityList=document.getElementById("activityList")
    activityList.appendChild(venueCard)




    var showActivityImages = function (event) {
        let imageDiv = document.getElementById(place.place)
        let activityPic = document.getElementById("activity-pics")
        let activityDiv= document.getElementsByClassName("activityImageDiv")
        for (let i =0;i<activityDiv.length;i++){
            let singleActivityDiv = activityDiv[i]
            if (singleActivityDiv != imageDiv){
                singleActivityDiv.style.display = "none"
            }else{
                singleActivityDiv.style.display = "inline-block"
            }
        }
        activityPic.innerHTML;
    }


    var showAllImages = function (event) {
        let imageDiv = document.getElementById(place.place)
        let activityPic = document.getElementById("activity-pics")
        let activityDiv= document.getElementsByClassName("activityImageDiv")
        for (let i =0;i<activityDiv.length;i++){
            let singleActivityDiv = activityDiv[i]
            singleActivityDiv.style.display = "inline-block"
        }
        activityPic.innerHTML;
    }

    venueDetailsButton.addEventListener("click", ()=>{
        if (venueDetailsButton.innerText==="See Details"){
            toggleBounce(marker)
            // marker.addListener("click", toggleBounce(marker))
            venueCard.style.width="100%";
            venueCard.style.maxWidth="100%";
            venueImage.setAttribute("src", place.photoURL);
            venueDetailsButton.innerText="Hide Details"
             additionalVenueInfo.style.display="block"
            showActivityImages();
        } else{
            toggleBounce(marker);
            venueCard.style.width="250px";
            venueCard.style.maxWidth="250px";
            venueImage.setAttribute("src", "");
            venueDetailsButton.innerText="Hide Details"
            additionalVenueInfo.style.display="none"
            venueDetailsButton.innerText="See Details"
            showAllImages();
        }


    })




    let additionalVenueInfo=document.createElement("div");
    additionalVenueInfo.setAttribute("class", "additionalVenueInfo");

    let websiteLink=document.createElement("a");
    websiteLink.setAttribute("class", "websiteDiv");
    websiteLink.innerText=place.website
    additionalVenueInfo.appendChild(websiteLink);

    let addressDiv=document.createElement("div");
    addressDiv.setAttribute("class", "addressDiv")
    addressDiv.innerText=place.address
    additionalVenueInfo.appendChild(addressDiv);

    let hoursDiv=document.createElement("div");
    hoursDiv.setAttribute("class", "hoursDiv")
    hoursDiv.innerText=place.hours
    additionalVenueInfo.appendChild(hoursDiv);

    cardBody.appendChild(additionalVenueInfo)


    // let imagePreview = document.createElement("image")
    // let imagePreviewDiv = document.createElement("div")
    // imagePreviewDiv.appendChild(imagePreview)
    // imagePreviewDiv.style.display = "none"
    // let submitButton=document.createElement("button")
    // submitButton.setAttribute("type", "submit")
    // submitButton.innerText="submit"
    // imagePreviewDiv.appendChild(imagePreview)
    // imagePreviewDiv.appendChild(submitButton)
    //
    // cardBody.appendChild(imagePreviewDiv)




    const options = {

        //onFileUploadFinished is called when the the user uploads a image in the
        // picker and they have successfully uploaded the image to filestack servers.
        //
        onFileUploadFinished: callback =>{
            console.log(callback);
            // I save the filestack image url to a const because I plan to use it in multiple places.
            const imgURL = callback.url;
            console.log(imgURL);

            // this sets my hidden input to the value of my new image url.
            imageUrlInput.value=imgURL;

            //will submit form now
            activityImageForm.submit();
        }
    }

    // This is an event listen for listening to a click on a button
    fileStackButton.addEventListener("click",()=>{
        console.log('adding picture')
        fileStackButton.style.display="none";
        additionalVenueInfo.style.display="none"

        // this is what prevents the button from submiting the form
        event.preventDefault();

        //we use this to tell filestack to open their file picker interface.
        // the picker method can take an argument of a options object
        // where you can specify what you want the picker to do
        client.picker(options).open();
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
html+=`<h3 id="results">
  total: 0
  yes: 0
  no: 0
</h3>
<button type="button" id="yes-button">Click to vote yes</button>
<button type="button" id="no-button">Click to vote no</button>`

        venueDetailsCard.innerHTML=html;



});

    let yes = 0;
    let no = 0;

    function refreshResults () {
            var results = document.getElementById('results');
            results.innerHTML = 'total: ' + (yes + no);
            results.innerHTML += '<br />yes: ' + yes;
            results.innerHTML += '<br />no: ' + no;
        }



        document.getElementById('no-button').addEventListener('click', function () {
            no++;
            refreshResults();
        });


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
        venueDetailsCard.appendChild(saveButton);

        let clickedPlace=document.getElementById("clickedPlace");
        clickedPlace.appendChild(venueDetailsCard)
    }

function addVoteToDatabase(vote, id, counter){
    jQuery.ajax({
        'url': `/activity/${id}/activityVote?vote=${vote}`,
        success: function (activityVotes) {
            let currentCount=0;
            for (let voteObject of activityVotes){
               if (voteObject.vote){
                   currentCount++
               } else{
                   currentCount--
               }
           }
            counter.innerText=currentCount;

        },
        error: function (data) {
            console.log(data)
        }
    })
}


