//grabbing all approve friend request buttons and putting them in an array.
let approveButtons=document.getElementsByClassName("approveRequest");

//adding event listener to all approve buttons, will send response to database, and delete friend request card.
for (let approveButton of approveButtons){
    approveButton.addEventListener("click", ()=>{
        let friendId=approveButton.id;
        sendFriendRequestResponseToDatabase(friendId, "approve")
    })
}

//grabbing all approve friend request buttons and putting them in an array.
let rejectButtons=document.getElementsByClassName("rejectButton");

//adding event listener to all approve buttons, will send response to database, and delete friend request card.
for (let rejectButton of rejectButtons){
    rejectButton.addEventListener("click", ()=>{
        let friendId=rejectButton.id;
        sendFriendRequestResponseToDatabase(friendId, "reject")
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

