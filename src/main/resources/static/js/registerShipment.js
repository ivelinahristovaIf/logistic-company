$( document ).ready(function(){

// SUBMIT FORM
    $("#shipmentForm").submit(function(event) {
		// Prevent the form from submitting via the browser.
		console.log("submit");
		event.preventDefault();
		ajaxPost();
	});

	function ajaxPost(){
	// PREPARE FORM DATA
        	var formData = {
        		workerId : $("#workerId").val(),
        		totalPrice :  $("#totalPrice").val()
        	}
        	    	// DO POST
                	$.ajax({
            			type : "POST",
            			contentType : "application/json",
            			url : "http://localhost:8090/shipment",
            			data : JSON.stringify(formData),

            			dataType : 'json',
            			success : function (data) {
                                                      $("#bsModal3").modal('show');
                                                      function resetData(){
                                                          	$("#workerId").val("");
                                                          	$("#totalPrice").val("");
                                                          }
                                                  },
            			error : function(e) {
            				alert("Error!")
            				console.log("ERROR: ", e);
            			}
            		});

	}

})