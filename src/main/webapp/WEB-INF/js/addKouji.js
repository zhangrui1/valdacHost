/**
 * Created by Lsr on 2/17/15.
 */
$(document).ready(function(){
   $('.step-div').mouseup(function(obj){
       var current = obj.currentTarget;
       var id = $('#koujiId').val();
       if($(current).hasClass('first-step-complete')){
           window.location='/valdacHost/kouji/'+id+'/instruct';
       } else if($(current).hasClass('middle-step-complete')){
           window.location='/valdacHost/kouji/'+id+'/valve';
       } else if($(current).hasClass('last-step-complete')){
           window.location='/valdacHost/kouji/'+id+'/kiki';
       }
   });
});