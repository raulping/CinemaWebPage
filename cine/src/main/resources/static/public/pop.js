
var disp = function(){
    var step=1;
    var height = $(window).height();
    var y=document.getElementById("pop").offsetTop;
    var y2=document.getElementById("pop2").offsetTop;
    
    y = y +step;
    y2 = y2 +step;
    document.getElementById("pop").style.top= y + "px";
    document.getElementById("pop2").style.top= y2 + "px";
    if(y<height){
        setTimeout(disp, 25);
    }else{
        document.getElementById("pop").style.top= -100 + "px";
        document.getElementById("pop2").style.top= -100 + "px";
        setTimeout(disp, 250);
    } 
    
}

$(window).bind("load", function(){
    console.log("ready!");
    
    disp();
    
});