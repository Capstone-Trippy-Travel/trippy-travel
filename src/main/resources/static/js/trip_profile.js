

mapboxgl.accessToken = mapBoxToken

let tripLocation=document.getElementById("tripLocation").innerText;
console.log(tripLocation)

let mapContainer=document.getElementById("map");

let markers=[];
let venueCards=[];
let modalImages=[]

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

function createVenueCard(place, marker) {
    // console.log(place.opening_hours.isOpen)
    // console.log(place.photos[0].getUrl);

    //creating div for each activity card.
    let venueCard = document.createElement("div");

    venueCard.setAttribute("class", "venueCard card");

    //creating div to show image (when not hidden)
    let venueImageDiv = document.createElement("div")
    venueImageDiv.setAttribute("class", "venueImageDiv")


    //creating form to hold activity info : comments, pictures, etc.
    let activityImageForm = document.createElement("form");
    activityImageForm.setAttribute("action", '/trip/' + place.trip.id);
    activityImageForm.setAttribute("method", "Post")
    venueCard.appendChild(activityImageForm)

    let cardBody = document.createElement("div");
    cardBody.setAttribute("class", "venueCardBody")
    activityImageForm.appendChild(cardBody)

    //creating div to hold each venue image, and adding to card body
    let venueImage = document.createElement("img")
    venueImage.setAttribute("class", "venueImage")
    venueImageDiv.appendChild(venueImage);
    cardBody.appendChild(venueImageDiv)

    //creating div for main body of each activity
    let venueInfo = document.createElement("div");
    venueInfo.setAttribute("class", "venueInfo")

    cardBody.appendChild(venueInfo)


    //creating activity name header
    let venueNameHeader = document.createElement("h5");
    venueNameHeader.style.paddingTop="20px";
    venueNameHeader.innerText = place.place;
    venueInfo.appendChild(venueNameHeader)

    //creating activity ratings paragraph
    let venueRatings = document.createElement("p");
    venueRatings.innerText = place.rating + " stars - " + place.reviews + " reviews"
    venueInfo.appendChild(venueRatings);


    let activityInput = document.createElement("input");
    activityInput.setAttribute("value", place.id);
    activityInput.setAttribute("name", "activity_id")
    activityInput.setAttribute("type", "hidden")
    venueInfo.appendChild(activityInput);

    let imageUrlInput = document.createElement("input");
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

    //creating the comments counter and button
    let commentLikeCounterDiv = document.createElement("div")
    commentLikeCounterDiv.setAttribute("class", "commentLikeCounterDiv")

    //this div will hold the counter for votes (made below);
    let counterDiv = document.createElement("div")
    counterDiv.setAttribute("id", "counterDiv");
    counterDiv.setAttribute("class", "counterDiv float-left col-6");
    commentLikeCounterDiv.appendChild(counterDiv)

    let counter = document.createElement("span")
    counter.setAttribute("id", "counter");
    counter.innerText = place.voteCount;
    counterDiv.innerHTML = 'Vote Count: ';
    counterDiv.appendChild(counter);

    //creating div to hold the comment counter
    let commentCounter = document.createElement("div")
    if (place.commentCount==1){
        commentCounter.innerText = place.commentCount + " Comment"
    } else{
        commentCounter.innerText = place.commentCount + " Comments"
    }
    commentCounter.setAttribute("class", "col-6 float-left")
    commentLikeCounterDiv.appendChild(commentCounter)

    //this div will hold the comment counter


//creating the vote buttons, counter and div container to hold them,
    let yesButton = document.createElement("button");
    yesButton.setAttribute("id", "yes-button");
    yesButton.setAttribute("class", "btn btn-primary btn-sm")
    if (place.usersPreviousVote === "like") {
        yesButton.style.color="blue";
    }
    yesButton.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-hand-thumbs-up" viewBox="0 0 16 16">\n' +
        '  <path d="M8.864.046C7.908-.193 7.02.53 6.956 1.466c-.072 1.051-.23 2.016-.428 2.59-.125.36-.479 1.013-1.04 1.639-.557.623-1.282 1.178-2.131 1.41C2.685 7.288 2 7.87 2 8.72v4.001c0 .845.682 1.464 1.448 1.545 1.07.114 1.564.415 2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217 1.466.217h3.5c.937 0 1.599-.477 1.934-1.064a1.86 1.86 0 0 0 .254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857 0-.288-.036-.585-.113-.856a2.144 2.144 0 0 0-.138-.362 1.9 1.9 0 0 0 .234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a9.84 9.84 0 0 0-.443.05 9.365 9.365 0 0 0-.062-4.509A1.38 1.38 0 0 0 9.125.111L8.864.046zM11.5 14.721H8c-.51 0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65 1.095-.3 1.977-.996 2.614-1.708.635-.71 1.064-1.475 1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34 8.34 0 0 1-.145 4.725.5.5 0 0 0 .595.644l.003-.001.014-.003.058-.014a8.908 8.908 0 0 1 1.036-.157c.663-.06 1.457-.054 2.11.164.175.058.45.3.57.65.107.308.087.67-.266 1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 0 .212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.224 2.224 0 0 1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.866.866 0 0 1-.121.416c-.165.288-.503.56-1.066.56z"/>\n' +
        '</svg>';

    counter.innerText = place.voteCount;

    let noButton = document.createElement("button");
    noButton.setAttribute("class", "btn btn-danger btn-sm")
    if (place.usersPreviousVote === "dislike") {
        noButton.style.color="red";
    }
        // }noButton.setAttribute("style","border:none")
        noButton.setAttribute("id", "no-button");
        noButton.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-hand-thumbs-down" viewBox="0 0 16 16">\n' +
            '  <path d="M8.864 15.674c-.956.24-1.843-.484-1.908-1.42-.072-1.05-.23-2.015-.428-2.59-.125-.36-.479-1.012-1.04-1.638-.557-.624-1.282-1.179-2.131-1.41C2.685 8.432 2 7.85 2 7V3c0-.845.682-1.464 1.448-1.546 1.07-.113 1.564-.415 2.068-.723l.048-.029c.272-.166.578-.349.97-.484C6.931.08 7.395 0 8 0h3.5c.937 0 1.599.478 1.934 1.064.164.287.254.607.254.913 0 .152-.023.312-.077.464.201.262.38.577.488.9.11.33.172.762.004 1.15.069.13.12.268.159.403.077.27.113.567.113.856 0 .289-.036.586-.113.856-.035.12-.08.244-.138.363.394.571.418 1.2.234 1.733-.206.592-.682 1.1-1.2 1.272-.847.283-1.803.276-2.516.211a9.877 9.877 0 0 1-.443-.05 9.364 9.364 0 0 1-.062 4.51c-.138.508-.55.848-1.012.964l-.261.065zM11.5 1H8c-.51 0-.863.068-1.14.163-.281.097-.506.229-.776.393l-.04.025c-.555.338-1.198.73-2.49.868-.333.035-.554.29-.554.55V7c0 .255.226.543.62.65 1.095.3 1.977.997 2.614 1.709.635.71 1.064 1.475 1.238 1.977.243.7.407 1.768.482 2.85.025.362.36.595.667.518l.262-.065c.16-.04.258-.144.288-.255a8.34 8.34 0 0 0-.145-4.726.5.5 0 0 1 .595-.643h.003l.014.004.058.013a8.912 8.912 0 0 0 1.036.157c.663.06 1.457.054 2.11-.163.175-.059.45-.301.57-.651.107-.308.087-.67-.266-1.021L12.793 7l.353-.354c.043-.042.105-.14.154-.315.048-.167.075-.37.075-.581 0-.211-.027-.414-.075-.581-.05-.174-.111-.273-.154-.315l-.353-.354.353-.354c.047-.047.109-.176.005-.488a2.224 2.224 0 0 0-.505-.804l-.353-.354.353-.354c.006-.005.041-.05.041-.17a.866.866 0 0 0-.121-.415C12.4 1.272 12.063 1 11.5 1z"/>\n' +
            '</svg>';

        yesButton.addEventListener('click', function () {

            //sending yes vote to database, and will return list of all vote to update vote Count
            addVoteToDatabase(true, place.id, counter);


            let currentVote = Number(counter.innerText);
            currentVote++;
            counter.innerText = currentVote;
            yesButton.style.color="blue"
            noButton.style.color="black"


        });

        noButton.addEventListener('click', function () {
            let currentVote = Number(counter.innerText);
            currentVote--;
            counter.innerText = currentVote;
            yesButton.style.color="black"
            noButton.style.color="red"

            addVoteToDatabase(false, place.id, counter);


        });

        //creating comment button to add to card
        let commentButton = document.createElement("button");
        commentButton.setAttribute("id", "activityCommentButton")
        commentButton.setAttribute("style", "")
        commentButton.setAttribute("data-toggle", "modal")
        commentButton.setAttribute("data-target", "#modalPoll-1")
        commentButton.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg"  width="20" height="20" fill="currentColor" class="bi bi-chat" viewBox="0 0 16 16">\n' +
            '  <path d="M2.678 11.894a1 1 0 0 1 .287.801 10.97 10.97 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8.06 8.06 0 0 0 8 14c3.996 0 7-2.807 7-6 0-3.192-3.004-6-7-6S1 4.808 1 8c0 1.468.617 2.83 1.678 3.894zm-.493 3.905a21.682 21.682 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a9.68 9.68 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9.06 9.06 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105z"/>\n' +
            '</svg>'

        let voteDiv = document.createElement("div");
        voteDiv.setAttribute("class", "voteDiv")
        voteDiv.style.display = "flex"
        voteDiv.appendChild(yesButton);
        voteDiv.appendChild(commentButton);
        voteDiv.appendChild(noButton)

        //creating button to display additional venue details.
        let venueDetailsDiv = document.createElement("div");
        venueDetailsDiv.setAttribute("class", "text-center")
        let venueDetailsButton = document.createElement("button");
        venueDetailsButton.setAttribute("class", "btn btn-primary text-center")
        venueDetailsButton.setAttribute("data-toggle", "modal")
        venueDetailsButton.setAttribute("data-target", "#modalPoll-1")
        venueDetailsDiv.appendChild(venueDetailsButton)
        venueDetailsButton.innerText = "See Details"


        let fileStackButton = document.createElement("button");
        fileStackButton.setAttribute("class", "addPicture btn btn-sm");
        // fileStackButton.innerText="Add picture"
        fileStackButton.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-images" viewBox="0 0 16 16">\n' +
            '  <path d="M4.502 9a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z"/>\n' +
            '  <path d="M14.002 13a2 2 0 0 1-2 2h-10a2 2 0 0 1-2-2V5A2 2 0 0 1 2 3a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v8a2 2 0 0 1-1.998 2zM14 2H4a1 1 0 0 0-1 1h9.002a2 2 0 0 1 2 2v7A1 1 0 0 0 15 11V3a1 1 0 0 0-1-1zM2.002 4a1 1 0 0 0-1 1v8l2.646-2.354a.5.5 0 0 1 .63-.062l2.66 1.773 3.71-3.71a.5.5 0 0 1 .577-.094l1.777 1.947V5a1 1 0 0 0-1-1h-10z"/>\n' +
            '</svg>'
//adding filestack addPicture button if user is a groupMember
        if (place.loggedInUserCanEditActivity) {
            cardBody.appendChild(fileStackButton)
        }


        venueCard.appendChild(venueDetailsDiv)
        venueCard.appendChild(commentLikeCounterDiv)

        //will only add voteDiv if user is a groupMember
        if (place.loggedInUserCanEditActivity) {
            venueCard.appendChild(voteDiv)
        }


        let activityList = document.getElementById("activityList")
        activityList.appendChild(venueCard)


        var showActivityImages = function (event) {
            let imageDiv = document.getElementById(place.place)
            let activityPic = document.getElementById("activity-pics")
            let activityDiv = document.getElementsByClassName("myTripImg")
            for (let i = 0; i < activityDiv.length; i++) {
                let singleActivityDiv = activityDiv[i]
                if (singleActivityDiv != imageDiv) {
                    singleActivityDiv.closest('.tripImageDiv').style.display = "none"
                } else {
                    singleActivityDiv.closest('.tripImageDiv').style.display = "inline-block"
                }
            }
            activityPic.innerHTML;
        }


        var showAllImages = function (event) {
            let imageDiv = document.getElementById(place.place)
            let activityPic = document.getElementById("activity-pics")
            let activityDiv = document.getElementsByClassName("myTripImg")
            for (let i = 0; i < activityDiv.length; i++) {
                let singleActivityDiv = activityDiv[i]
                singleActivityDiv.closest('.tripImageDiv').style.display = "inline-block"
            }
            activityPic.innerHTML;
        }

        venueDetailsButton.addEventListener("click", () => {

            populateModalWithActivityInfo()
        })

        commentButton.addEventListener("click", () => {
            populateModalWithActivityInfo();
            $('#modalContent').scrollTop($('#modalContent')[0].scrollHeight);
            $('#activityModalComments').scrollTop($('#activityModalComments')[0].scrollHeight);


        })

        //will loop through all reply buttons, and add event listener
        let replyButtons = document.getElementsByClassName("activityCommentReplyButton");
        for (let replyButton of replyButtons) {
            if (replyButton.classList.contains(place.placeId)) {
                replyButton.addEventListener("click", () => {
                    populateModalWithActivityInfo();
                    $('#modalContent').scrollTop($('#modalContent')[0].scrollHeight);
                    $('#activityModalComments').scrollTop($('#activityModalComments')[0].scrollHeight);
                })
            }
        }

        venueCard.addEventListener("mouseenter", () => {
            toggleBounce(marker)
            showActivityImages();

        })

        venueCard.addEventListener("mouseleave", () => {
            toggleBounce(marker)
            showAllImages()
        })


        function populateModalWithActivityInfo() {

            //will run api call to google places detail, to add latest activity image to modal
            let modalActivityImage = document.getElementById("modalActivityImage")
            getVenueImages(place.placeId, modalActivityImage);

            //add activity name to modal
            let modalActivityName = document.getElementById("modalActivityName");
            modalActivityName.innerText = place.place;

            //add activity website to modal
            let modalActivityWebsite = document.getElementById("modalActivityWebsite");
            modalActivityWebsite.setAttribute("href", place.website);
            modalActivityWebsite.innerText = 'Visit Website!';
            modalActivityWebsite.style.color="blue";

            //add activity address to modal
            let modalActivityAddress = document.getElementById("modalActivityAddress");
            modalActivityAddress.innerText = place.address;

            //add activity hours to modal
            let modalActivityHours = document.getElementById("modalActivityHours");

            //will format hours to not show up in a block
            let hoursArray = place.hours.split(",")
            let hoursHtml="";
            for (let i = 0; i < hoursArray.length; i++) {
                hoursHtml += hoursArray[i]
                if (i !== hoursArray.length - 1) {
                    hoursHtml += '<br>'
                }
            }
            modalActivityHours.innerHTML=hoursHtml;


            //make ajax call to grab latest activity comments
            if (place.loggedInUserCanEditActivity) {

                retrieveActivityCommentsFromDatabase(place);


                //set eventListener on comment submit button
                let activityCommentSubmit = document.getElementById("activityModalCommentButton");
                activityCommentSubmit.addEventListener("click", () => {
                    //will grab text from new comment
                    let activityCommentText = document.getElementById("modalActivityCommentInput");

                    if (modalActivityName.innerText === place.place) {
                        retrieveActivityCommentsFromDatabase(place, activityCommentText.value)

                        //will reset comment input to nothing
                        activityCommentText.value="";

                        //will add to current comment count
                        let currentCommentCount = Number(commentCounter.innerText.split(" ")[0])+1;
                        if (currentCommentCount===1){
                            commentCounter.innerText="1 Comment"
                        } else{
                            commentCounter.innerText=currentCommentCount+" Comments"
                        }


                        setTimeout(function () {
                            addCommentToTrip(place.trip.id)
                        }, 300)
                    }
                })
            }

        }


        let additionalVenueInfo = document.createElement("div");
        additionalVenueInfo.setAttribute("class", "additionalVenueInfo");

        let websiteLink = document.createElement("a");
        websiteLink.setAttribute("class", "websiteDiv");
        websiteLink.innerText = "Visit Website"
        additionalVenueInfo.appendChild(websiteLink);

        let addressDiv = document.createElement("div");
        addressDiv.setAttribute("class", "addressDiv")
        addressDiv.innerText = place.address
        additionalVenueInfo.appendChild(addressDiv);

        let hoursDiv = document.createElement("div");
        hoursDiv.setAttribute("class", "hoursDiv")
        hoursDiv.innerText = place.hours
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
            onFileUploadFinished: callback => {
                console.log(callback);
                // I save the filestack image url to a const because I plan to use it in multiple places.
                const imgURL = callback.url;
                console.log(imgURL);


                // this sets my hidden input to the value of my new image url.
                imageUrlInput.value = imgURL;

                //will submit form now
                activityImageForm.submit();
            }
        }

        // This is an event listen for listening to a click on a button
        fileStackButton.addEventListener("click", () => {
            console.log('adding picture')
            // fileStackButton.style.display="none";
            // additionalVenueInfo.style.display="none"

            // this is what prevents the button from submiting the form
            event.preventDefault();

            //we use this to tell filestack to open their file picker interface.
            // the picker method can take an argument of a options object
            // where you can specify what you want the picker to do
            client.picker(options).open();
        })


    }

    function getVenueImages(id, modalImage) {
        service = new google.maps.places.PlacesService(map);
        service.getDetails({
            placeId: id
        }, (place, status) => {
            console.log(place);
            let imageUrl = ""
            if (place.photos.length > 0) {

                //will add the first image to the activity modal
                imageUrl = place.photos[0].getUrl({maxHeight: 300});
                modalImage.setAttribute("src", imageUrl)

                //will save all modal Images, in case we want to add a carousel later.
                for (let i = 0; i < place.photos.length; i++) {
                    modalImages.push(place.photos[0].getUrl({maxHeight: 300}));
                }
                console.log(modalImages)
            }

        });
    }

    function addVoteToDatabase(vote, id, counter) {
        jQuery.ajax({
            'url': `/activity/${id}/activityVote?vote=${vote}`,
            success: function (activityVotes) {
                let currentCount = 0;
                for (let voteObject of activityVotes) {
                    if (voteObject.vote) {
                        currentCount++
                    } else {
                        currentCount--
                    }
                }
                counter.innerText = currentCount;

            },
            error: function (data) {
                console.log(data)
            }
        })
    }


//this filestack code below will be tied to the add picture button on trip profile column.
//will submit image immediately after filestack display is submitted.

//this is the form that will submit, containing uploaded filestack image url
    let tripImageForm = document.getElementById("tripImageForm");

    const client = filestack.init(FileStackApiKey);

// we will pass this object as an argument in the picker method.
    const options = {

        //onFileUploadFinished is called when the the user uploads a image in the
        // picker and they have successfully uploaded the image to filestack servers.
        //
        onFileUploadFinished: callback => {
            console.log(callback);
            // I save the filestack image url to a const because I plan to use it in multiple places.
            const imgURL = callback.url;
            console.log(imgURL);

            // this sets my hidden input to the value of my new image url.
            $('#image').val(imgURL);
            tripImageForm.submit();

            // this lets the user see a preview of the image that they uploaded.
            console.log(callback);
        }
    }
// This is an event listen for listening to a click on a button only if user is a groupMember
    $('#addTripPicture').click(function (event) {
        console.log('adding picture')

        // this is what prevents the button from submiting the form
        event.preventDefault();

        //we use this to tell filestack to open their file picker interface.
        // the picker method can take an argument of a options object
        // where you can specify what you want the picker to do
        client.picker(options).open();
    })


    let pathUrl = window.location.pathname;
    let pathUrlArray = pathUrl.split("/");
    let tripId = pathUrlArray[pathUrlArray.length - 1];

    let tripCommentInput = document.getElementById("tripCommentInput")
    let tripCommentButton = document.getElementById("tripCommentButton")
    let currentTripComments = document.getElementById("comments")

    tripCommentButton.addEventListener("click", () => {
        addCommentToTrip(tripId, tripCommentInput.value)
    })

    function addCommentToTrip(tripId, comment) {
        let url = "";

        //if a comment is passed, it will be saved to database first, then load updated comments
        if (comment) {
            url = `/trip/${tripId}/comments?comment=${comment}`;
        } else {
            url = `/trip/${tripId}/comments`;
        }
        jQuery.ajax({
            'url': url,
            success: function (comments) {
                console.log(comments)
                let html = "";

                for (let comment of comments) {
                    html += `<div class="tripComment">`
                    if (comment.activity!=null){
                        html+=`<div style="font-style: italic;color: gray;padding-bottom: 5px;">Discussion: ${comment.activity.place}</div>`
                    }
                    html+=`<div class="row no-gutters">
                        <div class="col-sm-2"  >
                            <img src="${comment.user.profile_image}" class="card-img-top h-100 commentProfileImage" style="max-height: 50px;max-width: 50px;border-radius: 100%;margin-bottom: 2px"  default="/imgs/default-profile-picture.png">
                        </div>
                        <div class="col-sm-10">
                            <div class="" style="border-radius: 5%;background-color: #f6f6f6;margin-bottom: 2px">
                                <div class="" ><strong>${comment.user.firstName} ${comment.user.lastName}</strong></div>
                                <div>${comment.comment_text}</div>
                            </div>`

                    if (comment.ajaxCallCommentReplies != null) {
                        html+=`<div class="text-center">Replies</div>
                            <div class="activityCommentRepliesDiv">`
                        for (let commentReply of comment.ajaxCallCommentReplies) {
                            html += `<div class="activityCommentReplies row">
                                    <div class="col-sm-3">
                                        <img src="${commentReply[3]}"
                                             class="card-img-top h-100 replyProfileImage" alt="..." style="max-height: 50px;max-width: 50px;border-radius: 100%;margin-bottom: 2px">
                                    </div>
                                    <div class="col-sm-9" style="border-radius: 5%;background-color: #f6f6f6;margin-bottom: 2px">
                                        <div class=""><strong>${commentReply[1]} ${commentReply[2]}</strong></div>
                                        <div>${commentReply[0]}</div>
                                    </div>
                                </div>`
                        }
                    } else{
                        html+='<div class="activityCommentRepliesDiv">'
                    }

                    html += `</div>`
                    if (comment.ajaxCallCommentReplies != null || comment.activity!=null) {

                        html += `<button data-toggle="modal" style="border-radius: 5%; border: none; background-color: dodgerblue; color: white" data-target="#modalPoll-1" class="activityCommentReplyButton ChIJpVZj64-cJIYR0DNvmDpeF8Y btn btn-primary btn-sm">Reply</button>`
                    }
                    html += `</div>
                    </div>
                </div>`

                }
                currentTripComments.innerHTML = html;
                tripCommentInput.value = ""
                $('#comments').scrollTop($('#comments')[0].scrollHeight);

                let activityCommentsRepliesDivList = document.getElementsByClassName('activityCommentRepliesDiv')

                for (let replyDiv of activityCommentsRepliesDivList) {
                    $(replyDiv).scrollTop($(replyDiv)[0].scrollHeight);

                }


            },
            error: function (data) {
                console.log(data)
            }
        })
    }


    function retrieveActivityCommentsFromDatabase(place, newComment) {
        let url = ""
        //if new comment is added, will append it to ajax url
        if (newComment) {
            url = `/trip/${place.trip.id}/activity/${place.id}?comment=${newComment}`
        } else {
            url = `/trip/${place.trip.id}/activity/${place.id}`
        }


    jQuery.ajax({
        'url': url,
        success: function (comments) {
            //if successful, will delete text-area comment text
            newComment="";

            console.log(comments)
            //loop through comments returned from database and add them to commentsDiv
            let html=""

            //will check to see if original comment was included in comment replies response, and will add it first if not
            if (comments.length>0 && comments[0].id!==0){
                let comment=comments[0];
                html+=`<div >

                        <div class="row no-gutters">
                            <div class="col-sm-2"  >
                                <img src="${comment.parentComment.user.profile_image}" style="max-height: 50px;max-width: 50px;border-radius: 100%; margin-bottom: 2px" class="card-img-top h-100" default="/imgs/default-profile-picture.png">
                            </div>
                            <div class="col-sm-10">
                                <div class="" style="border-radius: 5%;background-color: #f6f6f6;margin-bottom: 2px">
                                    <div class=""><strong>${comment.parentComment.user.firstName} ${comment.parentComment.user.lastName}</strong></div>
                                    <div>${comment.parentComment.comment_text}</div>
                                </div>
                            </div>
                            
                        </div>
                        
                    </div>`
                }

                //will loop through all comment replies returned from database and generate comments
                for (let comment of comments) {
                    html += `<div >
                        <div class="row no-gutters">
                            <div class="col-sm-2" >
                                <img src="${comment.user.profile_image}" class="card-img-top h-100" style="max-height: 50px;max-width: 50px;border-radius: 100%;margin-bottom: 2px" default="/imgs/default-profile-picture.png">
                            </div>
                            <div class="col-sm-10">
                                <div class="" style="border-radius: 5%;background-color: #f6f6f6;margin-bottom: 2px">
                                    <div class=""><strong>${comment.user.firstName} ${comment.user.lastName}</strong></div>
                                    <div>${comment.comment_text}</div>
                                </div>
                            </div>
                            
                        </div>
                        
                    </div>`

                }

                let commentDiv = document.getElementById("activityModalComments")
                commentDiv.innerHTML = html;

                //will make the modal comments auto scroll to bottom.
                $('#activityModalComments').scrollTop($('#activityModalComments')[0].scrollHeight);

        },
            error: function (data) {
                console.log(data)
            }
        })
    }

    $('#comments').scrollTop($('#comments')[0].scrollHeight);
    $('.activityCommentRepliesDiv').scrollTop($('.activityCommentRepliesDiv')[0].scrollHeight);

    let activityCommentsRepliesDivList = document.getElementsByClassName('activityCommentRepliesDiv')

    for (let replyDiv of activityCommentsRepliesDivList) {
        $(replyDiv).scrollTop($(replyDiv)[0].scrollHeight);

    }





