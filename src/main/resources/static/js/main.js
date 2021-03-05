$(document).ready(function () {

    $("#GeneRate-Short-URL-Form").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();
       $("#response").hide();

        generateShortUrl();

    });
    
    $("#Get-Original-URL-Form").submit(function (event) {
    $("#response").hide();

        //stop submit the form, we will post it manually.
        event.preventDefault();

        getOriginalUrl();

    });
    
    
    });
function generateShortUrl() {

    var search = {}
    search["url"] = $("#url").val();
 
    $("#bth-genShort").prop("disabled", true);
   $("#shorturl").val('');
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/generate",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
           console.log("SUCCESS : ", data);
            $("#bth-genShort").prop("disabled", false);    
            alert("Success")
             $("#shorturl").val(data.shortLink);
        },
        error: function (e) {

            var json = "<h4>Error Response</h4><pre>"
                + e.responseText + "</pre>";
                $("#response").show();
            $('#response').html(json);

            console.log("ERROR : ", e);
            $("#bth-genShort").prop("disabled", false);

        }
    });
	
	}
	
	
	
	function getOriginalUrl() {

    var search = {}
    search["shorturl"] = $("#shorturl").val();
 
    $("#bth-getOriginalURL").prop("disabled", true);
     $("#url").val('');

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/getOriginalURL",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
           console.log("SUCCESS : ", data);
              alert("Success")
             $("#url").val(data.originalUrl);
            $("#bth-getOriginalURL").prop("disabled", false);
            window.open(data.originalUrl);
        },
        error: function (e) {
  $("#response").show();
           var json = "<h4>Error Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#response').html(json);

            console.log("ERROR : ", e);
            $("#bth-getOriginalURL").prop("disabled", false);

        }
    });
	
	}