// hotdevices.js
$(function () {
  function startTickerDevice() {
    setTimeout(function () {
      $('#ticker2 li:first').animate({ marginTop: '-55px' }, 1200, function () {
        $(this).detach().appendTo('#ticker2').removeAttr('style');
      });
      startTickerDevice();
    }, 3000);
  }

  startTickerDevice();
});
