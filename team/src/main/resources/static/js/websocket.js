$(function(){
	
	var ws = null;
	google.charts.load('current', {'packages':['corechart']});
	//google.charts.setOnLoadCallback(drawChart);
	
	var client = null;
	function WebSocketTest(){
		
	  if (ws == null){
		  
	
	  if ("WebSocket" in window){
		  client = Stomp.over(new SockJS("http://localhost:8080/websockets/"));
		  client.connect( "bob", "sw0rdf1sh", function(frame) {
	
	         // once connected...
	
	         subscription_id = client.subscribe("/topic/stats", function(msg) {
	        // handle messages for this subscription 
	       
	         var received_msg = jQuery.parseJSON(msg.body);
	           $("#time").text(received_msg.time);
	           $("#users").text(received_msg.users);
	           $("#uris").text(received_msg.uris);
	           $("#clicks").text(received_msg.clicks);
	           $("#lastRedirection").text(received_msg.lastRedirection);
	           $("#lastPetition").text(received_msg.lastPetition);
	           $("#used").text(received_msg.used);
	           $("#avaible").text(received_msg.available);
	
	           //Add the the new information to the array of memory
	           var da= new Date();
	           var str = da.getHours()+":"+da.getMinutes();
	           if (arrayMemmory.length < 20){
	             arrayMemmory.push([str,received_msg.used,received_msg.available]);
	           }else{
	              arrayMemmory.splice(1, 1);
	               arrayMemmory.push([str,received_msg.used,received_msg.available]);
	           }
	         
	          //Re-paint charts
	           drawChart();
	
	           
	            } );
	         
	           
	      });
	    }else{
	       // The browser doesn't support WebSocket
	       alert("WebSocket NOT supported by your Browser!");
	    }
	  }
	 }
	
	
	// START - GOOGLE STEPPED AREA CHART FOR MEMMORY USED & AVAIBLE
	//variables that websockets must replenish
	
	var arrayMemmory = [  ['Time',  'Used', 'Available']];
	
	
	
	function drawChart() {
		
			
		
	  var data = google.visualization.arrayToDataTable(arrayMemmory);
	
	  var options = {
	    title: 'System memory',
	    vAxis: {title: 'KBytes'},
	    isStacked: true
	  };
	
	  var chart = new google.visualization.SteppedAreaChart(document.getElementById('chart_div'));
	
	  chart.draw(data, options);
		
	}	
	var subscription_csv = null;
	$("#email-form").on("submit",function(e){
		var userkey = Math.random().toString().slice(2,11);
		var cantidad = 0;
		e.preventDefault();
		//Conect to csv topic for results on file upload
		if (subscription_csv != null){
			subscription_csv.unsubscribe();
		}
        subscription_csv = client.subscribe("/topic/uploadFile/"+userkey+"/", function(msg) {
	        // handle messages for this subscription 
          var received_msg = jQuery.parseJSON(msg.body);
          if (received_msg.correct){
        		 $("#resultadosubida ul").append("<li>"+received_msg.from+" -  <a href='"+received_msg.to+"'>"+received_msg.to+"</a></li>");	       
          }else{
        		 $("#resultadosubida ul").append("<li>"+received_msg.from+" -  <span style='color:red;'>Incorrect: "+received_msg.cause+"</span></li>");	       
          }
           
	      });
	$("#resultadosubida").html("<h3>Resultados del fichero:</h3><ul></ul>");
		$("#mensaje").html("<p>Cargando fichero... Espere por favor.</p>");
		var f = $(this);
		var formData = new FormData(document.getElementById("email-form"));
		$.ajax({
			url:"uploadFile/"+userkey,
			type:"post",
			dataType: "html",
			data: formData,
			cache: false,
			contentType: false,
			processData: false
		})
		.done(function(res){
			$("#mensaje").html("<h3>Dirección del recurso para obtener los resultados</h3><p><a target='_blank' href='"+res+"'>"+res+"</a><p>");
		});
	});
	
	WebSocketTest();
});