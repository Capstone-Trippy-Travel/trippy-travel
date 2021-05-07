//grabbing all approve friend request buttons and putting them in an array.
let approveButtons=document.getElementsByClassName("approveRequest");
let friendRequestsNotificationIcon = document.getElementById("friendRequestsNotificationIcon")

//adding event listener to all approve buttons, will send response to database, and delete friend request card.
for (let approveButton of approveButtons){
    approveButton.addEventListener("click", ()=>{
        let friendId=approveButton.id;
        sendFriendRequestResponseToDatabase(friendId, "approve")
        friendRequestsNotificationIcon.style.display="none";
    })
}

//grabbing all approve friend request buttons and putting them in an array.
let rejectButtons=document.getElementsByClassName("rejectButton");

//adding event listener to all approve buttons, will send response to database, and delete friend request card.
for (let rejectButton of rejectButtons){
    rejectButton.addEventListener("click", ()=>{
        let friendId=rejectButton.id;
        sendFriendRequestResponseToDatabase(friendId, "reject")
        friendRequestsNotificationIcon.style.display="none";
    })
}

//function to send friend request response to database
function sendFriendRequestResponseToDatabase(friendId, response){
    jQuery.ajax({
        'url': `/users/friend-requests/${friendId}?response=${response}`,
        success: function (newFriend) {
            console.log(newFriend)
            let friendRequestCard=document.getElementById("friendRequestCard"+friendId);
            friendRequestCard.style.display="none";
        },
        error: function (data) {
            console.log(data)
        }
    })
}

//grabbing the red circle icon from the navbar, and notifications modal comment section
let notificationsButton=document.getElementById("notificationsModalButton");
let commentNotificationIcon=document.getElementById("commentNotificationIcon");
let modalNotificationIcon=document.getElementById("modalNotificationIcon");

let tripIdHolderArray=document.getElementsByClassName("tripIdHolder");
// let tripIdsArray=tripIdHolderArray.map((idDiv)=>Number(idDiv.innerText))
let tripIdsArray=[]
for (let tripIdHolder of tripIdHolderArray ){
    console.log(tripIdHolderArray)
    tripIdsArray.push(Number(tripIdHolder.innerText))
}
console.log(tripIdsArray)


notificationsButton.addEventListener("click", ()=>{
    if (modalNotificationIcon!=null) {
        modalNotificationIcon.style.display = "none";
        for (let i=0; i<tripIdsArray.length; i++){
            updateUnreadCommentStatus(tripIdsArray[i])
        }

    }
    let modalFadeDivs=document.getElementsByClassName("modal-backdrop")

     setTimeout(function(){
        for (let modalBackdrop of modalFadeDivs ){
            modalBackdrop.style.display="none";
        }
    }, 1)
})

let closeModalButton=document.getElementById("closeModalButton")

closeModalButton.addEventListener("click", ()=>{
    if (commentNotificationIcon!=null) {
        commentNotificationIcon.style.display = "none";
    }


})

function updateUnreadCommentStatus(id){
    jQuery.ajax({
        'url': `/trip/${id}/commentNotifications`,
        success: function (newFriend) {
            console.log("successfully updated comment notifications")
        },
        error: function (data) {
            console.log(data)
        }
    })
}


