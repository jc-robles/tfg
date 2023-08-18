function loadPopover() {
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
          let opts = {
            animation: false,
          }
        if (popoverTriggerEl.hasAttribute('data-bs-content-id')) {
            opts.content = document.getElementById(popoverTriggerEl.getAttribute('data-bs-content-id')).innerHTML;
            opts.html = true;
          }
      return new bootstrap.Popover(popoverTriggerEl, opts)
    })
}