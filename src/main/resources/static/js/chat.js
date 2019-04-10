$("document").ready(function() {
	
});

function getMessagesOfGroup(){
	
}

function getAllGroupNamesOfUser(){
	$.get("/api/getAllGroupNames").done(function(data) {
		alert( "Got the group names" );
		console.log(data)
	}).fail(function() {
		alert( "error" );
	})
}

function getAllGroupMemberLocations(){
	$.get("/api/getLocationsOfGroupMembers").done(function(data) {
		alert( "Got the locations of group members" );
		console.log(data)
	}).fail(function() {
		alert( "error" );
	})
}