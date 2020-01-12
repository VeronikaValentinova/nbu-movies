
// function test(){
//     alert("Mihaela");
// }

//$('#login-button').click(function

function showLoginForm(){
    $('#login-button').fadeOut("slow",function(){
      $("#container").fadeIn();
      TweenMax.from("#container", .4, { scale: 0, ease:Sine.easeInOut});
      TweenMax.to("#container", .4, { scale: 1, ease:Sine.easeInOut});
    });
  };
  //$(".close-btn").click(function
  function closeBtn(){
    TweenMax.from("#container", .4, { scale: 1, ease:Sine.easeInOut});
    TweenMax.to("#container", .4, { left:"0px", scale: 0, ease:Sine.easeInOut});
    $("#container, #forgotten-container").fadeOut(800, function(){
      $("#login-button").fadeIn(800);
    });
  };
  
  function closeSignUpBtn(){
    TweenMax.from("#container", .4, { scale: 1, ease:Sine.easeInOut});
    TweenMax.to("#container", .4, { left:"0px", scale: 0, ease:Sine.easeInOut});
    $("#container, #signup-container").fadeOut(800, function(){
      $("#login-button").fadeIn(800);
    });
  };

  /* Forgotten Password */
  //$('#forgotten').click(function()

  
  