$(document).ready(
    function() {
        $("#shortener").submit(
            function(event) {
                event.preventDefault();
                $.ajax({
                    type : "POST",
                    url : "/link",
                    data : $(this).serialize(),
                    success : function(msg) {
                        $("#result").html(
                        	 "<div class='alert alert-success lead'><p>This is your URL shortened</p><a target='_blank' href='"
                            + msg.uri
                            + "'>"
                            + msg.uri
                            + "</a> <p>This is your MEME image</p><img src='"
                            + msg.image
                            + "'/> </div>");
                    },
                    error : function() {
                        $("#result").html(
                                "<div class='alert alert-danger lead'><p>Oops! Something went wrong while submitting the form. :(</p></div>");
                        
                    }
                });
            });
    });
