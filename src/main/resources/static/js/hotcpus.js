// hotcpus.js
$(function () {
  function startTickerCpu() {
    setTimeout(function () {
      $('#ticker1 li:first').animate({ marginTop: '-55px' }, 1200, function () {
        $(this).detach().appendTo('#ticker1').removeAttr('style');
      });
      startTickerCpu();
    }, 3000);
  }

  startTickerCpu();
});
