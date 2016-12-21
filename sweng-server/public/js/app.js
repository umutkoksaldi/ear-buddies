(function($) {
  "use strict";

  $('a.page-scroll').bind('click', function(event) {
      var $anchor = $(this);
      $('html, body').stop().animate({
          scrollTop: ($($anchor.attr('href')).offset().top - 50)
      }, 1250, 'easeInOutExpo');
      event.preventDefault();
  });

  $('#mainNav').affix({
    offset: {
      top:100
    }
  })
})(jQuery)

$('.navbar-collapse ul li a').click(function() {
    $('.navbar-toggle:visible').click();
});

$('#noeCompany').on('shown.bs.modal', function () {
  $('#myInput').focus()
})
