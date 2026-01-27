window.addEventListener('load', () => {
  document.querySelectorAll('.total2_name a').forEach(el => {
    const lineHeight = parseFloat(getComputedStyle(el).lineHeight);
    const height = el.offsetHeight;
    const lines = Math.round(height / lineHeight);

    if (lines > 1) {
      el.style.fontSize = '18px';
    } else {
      el.style.fontSize = '22px';
    }
  });
});
