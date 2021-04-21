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


let searchUsers= document.getElementById("searchUsers");
let usersList=document.getElementById("usersList");
// let groupMembersSelectList=document.getElementById("groupMembersSelectList")
let currentInterval="";

//this function will search the database for the name being typed in the find a group member input
// It will return a list of names that can be picked in html.
function searchDatabaseForUsers(name) {
    console.log("searching for "+name)
    jQuery.ajax({
        'url': `/users.json?name=${name}`,
        success: function (users) {
            console.log(users);
            //this will generate the html for the list of names that were found.
            let html = '';
            for (let i = 0; i < users.length; i++) {
                console.log(users[i].lastName);
                html += '<li class="list-group-item d-flex justify-content-between align-items-center">'+users[i].firstName+" "+users[i].lastName;

                //will check and see if user is a friend already.
                if (users[i].friendStatus==="ACCEPTED") {
                    html += '<button class="float-right btn btn-primary btn-sm" type="button" disabled>Friends</button>'
                } else if (users[i].friendStatus==="PENDING"){
                    html += '<button class="float-right btn btn-warning btn-sm" type="button" disabled>Friend Request Pending</button>'
                } else{
                    html += '<button class="float-right btn btn-warning btn-sm addFriendButton" id="user='+users[i].id+'" type="button">Add Friend</button>'
                }
                html+= '</li>'
            }
            usersList.innerHTML=html;

            //add eventListener to add friend button
            let addFriendButtons=document.getElementsByClassName("addFriendButton");
            for (let addFriendButton of addFriendButtons){
                addFriendButton.addEventListener("click", (event)=>{
                    event.preventDefault();
                    let buttonId=addFriendButton.id.split("=")[1]
                    console.log(buttonId)
                    jQuery.ajax({
                        'url': `/users/${buttonId}/friend-request`,
                        success: function (users) {
                            console.log("success!")
                            console.log(users)
                            addFriendButton.innerText="Friend Request Pending"
                        },
                        error: function (data) {
                            console.log("failure")
                            console.log(data)
                        }
                    })
                })

            }



        },
        error: function (data) {
            console.log(data)
        }
    })
}

//this event listener will wait for the user to search for users to add for group, and then will run
// the searchDatabaseForUsers function.
searchUsers.addEventListener("keyup", ()=>{
    clearTimeout(currentInterval);
    currentInterval=setTimeout(function(){
        searchDatabaseForUsers(searchUsers.value)
    },500);

});
