$( document ).ready(function() {

	// SUBMIT FORM
    $("#loginForm").submit(function(event) {
		// Prevent the form from submitting via the browser.
		event.preventDefault();
		ajaxPost();
	});


    function ajaxPost(){

    	// PREPARE FORM DATA
    	var formData = {
    		email : $("#email").val(),
    		password :  $("#password").val()
    	}

    	// DO POST
    	$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "http://localhost:8090/user/signin",
			data : JSON.stringify(formData),
			dataType : 'json',
			success : function (data) {
                                          //save token
                                          localStorage.setItem("Authentication","Bearer " + data.token);
                                          document.cookie="Bearer " + data.token;
                                          var myHeaders = new Headers(); // Currently empty
                                          myHeaders.set('Authentication', "Bearer " + data.token);
                                          console.log(myHeaders)
                                          console.log(document)
                                          renderHomePage(data);
                                      },
			error : function(e) {
				alert("Error!")
				console.log("ERROR: ", e);
			}
		});

    	// Reset FormData after Posting
    	resetData();

    }

    function renderHomePage(data){
    console.log(data);
        switch(data.userLoginResponse.userRole){
        case 'EMPLOYEE':
        window.location.href="http://localhost:8090/home";
        break;
        case 'CLIENT':
        window.location.href="http://localhost:8090/home?role=CLIENT";
        break;
        }
    }

    function resetData(){
    	$("#email").val("");
    	$("#password").val("");
    }
})