 $(document).ready(function(){
	
	$('#btnA,#btnB,#btnC,#btnD').bind('mousedown',function(e)
	{
		$('#Main_Container').append('<img id="popup" src="media/images/popup_'+this.id+'.png" />');
		$("#popup").bind('mousedown', function(e) {
			$("#popup").remove();
		});
	});
	
});