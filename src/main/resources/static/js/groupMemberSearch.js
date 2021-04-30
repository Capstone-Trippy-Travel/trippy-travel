let searchUsers = document.getElementById("searchUsers");
let groupMembers = document.getElementById("groupMembers");
let groupMembersSelectList = document.getElementById("groupMembersSelectList")
let currentInterval = "";

//this function will search the database for the name being typed in the find a group member input
// It will return a list of names that can be picked in html.
function searchDatabaseForUsers(name) {
    console.log("searching for " + name)
    jQuery.ajax({
        'url': `/users.json?name=${name}`,
        success: function (users) {
            console.log(users);

            //this will generate the html for the list of names that were found.
            let html = '';
            for (let i = 0; i < users.length; i++) {
                //first will check to see if name is already in group. If it is, the user will not be added to database matches list
                let currentGroupMembers=document.getElementsByClassName("groupMemberOption");
                let instancesOfUserInGroupMembersList=0;
                if (currentGroupMembers.length>0) {
                    for (let groupMember of currentGroupMembers){
                        if (groupMember.value == users[i].id) {
                            instancesOfUserInGroupMembersList++
                        }
                    }
                }
                if (instancesOfUserInGroupMembersList===0) {

                    console.log(users[i].lastName);
                    html += '<li class="list-group-item d-flex justify-content-between align-items-center">' + users[i].firstName + " " + users[i].lastName;

                    //will check and see if user is a friend already.
                    if (users[i].friendStatus === "ACCEPTED") {
                        html += '<button id="addGroupMember' + i + '" class="float-right btn btn-primary btn-sm" type="button">' + 'Add to Group' + '</button>'
                    } else if (users[i].friendStatus === "PENDING") {
                        html += '<button class="float-right btn btn-warning btn-sm" type="button" disabled>Friend Request Pending</button>'
                        html += '<button id="addGroupMember' + i + '" class="float-right btn btn-primary btn-sm" type="button" disabled>' + 'Add to Group' + '</button>'
                    } else {
                        html += '<button class="float-right btn btn-warning btn-sm addFriendButton" id="user=' + users[i].id + '" type="button">Add Friend</button>'
                        html += '<button id="addGroupMember' + i + '" class="float-right btn btn-primary btn-sm" type="button" disabled>' + 'Add to Group' + '</button>'
                    }
                    html += '</li>'
                }
            }
            $('#databaseMatches').html(html);

            //This will add an event listener to each button, waiting for the name to be picked and added to group
            for (let i = 0; i < users.length; i++) {
                console.log(users[i].firstName + " " + users[i].lastName + " " + users[i].username + users[i].id)
                let buttonId = "addGroupMember" + i;
                console.log(buttonId)
                let addGroupMemberButton = document.getElementById(buttonId);
                console.log(addGroupMemberButton)
                setTimeout(function() {
                    addGroupMemberButton.addEventListener("click", () => {
                        let html = "";
                        html += '<li class="list-group-item d-flex justify-content-between align-items-center">' + users[i].firstName + " " + users[i].lastName + '</li>'
                        groupMembers.innerHTML = groupMembers.innerHTML + html;

                        let newGroupMember = document.createElement("option");
                        newGroupMember.setAttribute("class", "groupMemberOption")
                        newGroupMember.setAttribute("value", users[i].id);
                        newGroupMember.innerText = users[i].firstName + " " + users[i].lastName
                        console.log("about to set field")
                        // newGroupMember.setAttribute("field", [[${newGroupMemberIds}]] )
                        console.log("just set field")
                        newGroupMember.setAttribute("selected", true);
                        groupMembersSelectList.appendChild(newGroupMember);

                        //modify the button to say added to group, and make it unclickable
                        addGroupMemberButton.innerText = "Added to group"
                        addGroupMemberButton.setAttribute("class", "float-right btn btn-success btn-sm")
                        addGroupMemberButton.setAttribute("disabled", true)
                    })
                }, 500)
            }

            //add eventListener to add friend button
            let addFriendButtons = document.getElementsByClassName("addFriendButton");
            for (let addFriendButton of addFriendButtons) {
                addFriendButton.addEventListener("click", (event) => {
                    event.preventDefault();
                    let buttonId = addFriendButton.id.split("=")[1]
                    console.log(buttonId)
                    jQuery.ajax({
                        'url': `/users/${buttonId}/friend-request`,
                        success: function (users) {
                            console.log("success!")
                            console.log(users)
                            addFriendButton.innerText = "Friend Request Pending"
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
searchUsers.addEventListener("keyup", () => {
    clearTimeout(currentInterval);
    if (searchUsers.value!="") {
        currentInterval = setTimeout(function () {
            searchDatabaseForUsers(searchUsers.value)
        }, 500);
    } else{
        let databaseMatchesList=document.getElementById("databaseMatches")
        databaseMatchesList.innerHTML="";
    }

});

let groupName = document.getElementById("name");
groupName.addEventListener("change", () => {
    console.log(groupName.value)
})

