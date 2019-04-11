$("document").ready(function() {
	groupLocationsTest();
});

function getMessagesOfGroup(){
	
}

function groupLocationsTest(){
	$.get("/api/getAllGroupNames").done(function(data) {
		var groupNames = JSON.parse(data)
		console.log(groupNames[0])
		console.log(getLocationsOfGroupMembers(groupNames[0]))
	}).fail(function() {
		alert( "error" );
	})
}

function getLocationsOfGroupMembers(groupId){
	$.get("/api/getLocationsOfGroupMembers",
			{
				"groupId" : groupId 
			}).done(function(data) {
		alert( "Got the locations of group members" );
		return JSON.parse(data);
	}).fail(function() {
		alert( "error" );
	})
}