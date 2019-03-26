/**
 * 
 */

var data = [];
function initSignal() {
    var d1 = [[800, 0], [800, 18]];

//    for (var i = 0; i < 14; i += 0.5)
//
//        d1.push([i, Math.sin(i)]);

 

   var d2 = [[900, 0], [900, 18]];

 

    // a null signifies separate line segments

    var d3 = [[900, 0], [900, 6]];

    var d4 = [[918, 0], [918, 67]];
    
    var d5 = [[945, 0], [945, 6]];

    var d6 = [[933, 0], [933, 23]];
    
    
    $.plot($("#placeholder"), [d1,d2,d3,d4,d5,d6]);
  
}

function addSignal() {
	
	var frequence = $("input[name=frequence]").val();
	
	var RxLev = $("input[name=RxLev]").val();
	if(frequence >0 && RxLev>0) {

		var signal = [[frequence, 0], [frequence, RxLev]];
		data.push(signal);
	
		 $.plot($("#placeholder"), [signal, [[0,0],[0,1]]]);
	}
}