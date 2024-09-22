/*-----------------------------------------------------
   Homepage
------------------------------------------------------*/

if (document.querySelector('#hero')) {
  /*
   * Hero Carousel
   */
  const heroSlides = document.querySelectorAll('#hero .splide__slide');

  const heroSlidesTransitionEndHandler = (event) => {
    if (event.target !== event.currentTarget || event.propertyName !== 'opacity' || getComputedStyle(event.target).opacity !== '1') {
      return;
    }

    heroSlides.forEach((slide) => slide.classList.remove('opaque'));
  };

  const heroCarousel = new Splide('#hero', {
    autoplay: true,
    interval: 4000,
    type: 'fade',
    speed: 33,  // Faster transition, try reducing this value further if needed
    easing: 'ease-in-out',
    rewind: true,
    pagination: true,  
    arrows: true,
    breakpoints: {
      991: {
        arrows: false,  
        height: 'auto', 
      },
      414: {
        arrows: false,
        height: 'auto', 
        pagination: true, 
      },
    },
  });
  

  heroCarousel.on('arrows:mounted', (prev, next) => {
    prev.removeAttribute('aria-label');
    next.removeAttribute('aria-label');
  });

  heroCarousel.on('move', (newIndex, prevIndex) => {
    heroSlides[prevIndex].classList.add('opaque');
  });

  heroSlides.forEach((slide) => slide.addEventListener('transitionend', heroSlidesTransitionEndHandler));

  heroCarousel.mount();
}

/* CSS Adjustment to make hero images responsive */
const adjustHeroImageSize = () => {
  const heroImages = document.querySelectorAll('#hero .hero-image');
  heroImages.forEach((image) => {
    image.style.width = '25%';  // Make the image take full width of its container
    image.style.height = 'auto'; // Ensure the aspect ratio is maintained
    image.style.objectFit = 'cover'; // Scale the image proportionally
  });
};

// Call the function on page load to adjust hero images
document.addEventListener('DOMContentLoaded', adjustHeroImageSize);

/*-----------------------------------------------------
   Contact Us Page
------------------------------------------------------*/

if (document.querySelector('#contact')){
  const formEl = document.querySelector('#contact form');
  const dropZone = formEl.querySelector('.drop-zone');
  let errorMessageCount = 0;
  let isScrolling = false;

  formEl.addEventListener('invalid', (event) => {
    event.preventDefault();

    const formControl = event.target;
    const isInRadioOrCheckboxGroup = formControl.matches('[type="radio"], [type="checkbox"]') &&
      formEl.querySelectorAll(`:scope [name="${formControl.name}"]`).length > 1;
    const formControlMasterWrapper = formControl.closest(isInRadioOrCheckboxGroup ? 'fieldset' : 'div');
    let errorMessage = '';

    formEl.classList.add('validation-triggered');

    if (isInRadioOrCheckboxGroup) {
      switch (formControl.name) {
        case 'newsletter':
          errorMessage = 'Would you like to sign up for Big Star newsletter?';
          break;
        case 'favorite[]':
          errorMessage = 'Please select your favorite Big Star Collections';
          break;
      }
    } else {
      switch (formControl.id) {
        case 'contact-email':
          errorMessage = 'Valid email required';
          break;
        case 'user-name':
          errorMessage = 'Name is required';
          break;
        case 'user-password':
          errorMessage = 'Please write a message';
          break;
      }
    }

    if (formControl.hasAttribute('aria-describedby')) {
      document.getElementById(formControl.getAttribute('aria-describedby')).firstChild.textContent = `${errorMessage} `;
    } else {
      errorMessageCount++;
      formControl.setAttribute('aria-invalid', 'true');
      formControl.setAttribute('aria-describedby', `error-message-${errorMessageCount}`);
      formControlMasterWrapper.insertAdjacentHTML('beforeend', `<div id="error-message-${errorMessageCount}" class="error-message" role="alert">${errorMessage} <span aria-hidden="true">*</span></div>`);
      formControlMasterWrapper.classList.add('invalid-inside');
    }

    if (!isScrolling) {
      const firstInvalidFormControlWrapper = formEl.querySelector('.invalid-inside');
      if (firstInvalidFormControlWrapper.getBoundingClientRect().top < 0 || firstInvalidFormControlWrapper.getBoundingClientRect().bottom > window.innerHeight) {
        isScrolling = true;
        const scrollOffset = pageYOffset + firstInvalidFormControlWrapper.getBoundingClientRect().top;
        const fixedScrollOffset = scrollOffset.toFixed();

        const detectScrollCompletion = () => {
          if (pageYOffset.toFixed() === fixedScrollOffset) {
            isScrolling = false;
            window.removeEventListener('scroll', detectScrollCompletion);
            if (!mobileBreakpointMq.matches) {
              firstInvalidFormControlWrapper.querySelector('[aria-invalid="true"]').focus();
            }
          }
        };

        window.addEventListener('scroll', detectScrollCompletion);
        window.scrollTo({ top: scrollOffset, behavior: 'smooth' });
      }
    }
  }, true);

  const revalidate = (event) => {
    const formControl = event.target;
    const relatedRadiosOrCheckboxes = formControl.matches('[type="radio"], [type="checkbox"]') && formEl.querySelectorAll(`:scope [name="${formControl.name}"]`);
    const isInRadioOrCheckboxGroup = relatedRadiosOrCheckboxes.length > 1;
    const formControlMasterWrapper = formControl.closest(isInRadioOrCheckboxGroup ? 'fieldset' : 'div');

    if (!formEl.classList.contains('validation-triggered')) {
      return;
    }

    if (isInRadioOrCheckboxGroup) {
      if (!formControlMasterWrapper.classList.contains('invalid-inside')) {
        return;
      }

      if ([...relatedRadiosOrCheckboxes].some(radioOrCheckbox => radioOrCheckbox.checked)) {
        relatedRadiosOrCheckboxes.forEach(radioOrCheckbox => {
          radioOrCheckbox.removeAttribute('aria-invalid');
          radioOrCheckbox.removeAttribute('aria-describedby');
        });

        formControlMasterWrapper.querySelectorAll('.error-message').forEach(errorMessage => errorMessage.remove());
        formControlMasterWrapper.classList.remove('invalid-inside');
      }
    } else if (formControl.checkValidity()) {
      formControl.removeAttribute('aria-invalid');
      formControl.removeAttribute('aria-describedby');
      formControlMasterWrapper.querySelector('.error-message')?.remove();
      formControlMasterWrapper.classList.remove('invalid-inside');
    }
  };

  formEl.addEventListener('input', revalidate);
  formEl.addEventListener('blur', revalidate);
  formEl.addEventListener('change', revalidate);

  formEl.addEventListener('click', (event) => {
    const errorMessage = event.target.closest('.error-message');
    if (errorMessage) {
      errorMessage.closest('.invalid-inside').querySelector('[aria-invalid="true"]:not([type="radio"]):not([type="checkbox"])').focus();
    }
  });

  formEl.addEventListener('submit', (event) => {
    event.preventDefault();
    formEl.reportValidity();
  });

  // Drag and drop
  if (dropZone) {
    dropZone.addEventListener('dragover', (event) => {
      event.preventDefault();
      dropZone.classList.add('dragged-over');
    });

    dropZone.addEventListener('dragleave', () => dropZone.classList.remove('dragged-over'));

    dropZone.addEventListener('drop', (event) => {
      event.preventDefault();
      dropZone.classList.remove('dragged-over');
    });
  }
}