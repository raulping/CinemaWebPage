
var slideIndex = 1;

// Next/previous controls
var plusSlides = function(n) {
  showSlides(slideIndex += n);
}

// Thumbnail image controls
var currentSlide = function(n) {
  showSlides(slideIndex = n);
}

var showSlides = function(n) {
  var i;
  var slides = document.getElementsByClassName("mySlides");
  if (n > slides.length) {
      slideIndex = 1;
    }
  if (n < 1) {
      slideIndex = slides.length;
    }
  for (i = 0; i < slides.length; i++) {
      slides[i].style.display = "none";
  }
  slides[slideIndex-1].style.display = "block";
}

$(window).bind("load", function(){
    console.log("ready!");
    showSlides(slideIndex);
    
});