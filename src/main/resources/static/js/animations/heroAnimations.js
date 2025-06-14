document.addEventListener("DOMContentLoaded", function () { 
    // Animación del badge del hero
    anime({
      targets: '.animate-badge',
      scale: [0.95, 1],
      opacity: [0, 1],
      duration: 1200,
      easing: 'easeOutQuart'
    });

    // Animación del título principal
    anime({
      targets: '#heroTitle',
      translateY: [20, 0],
      opacity: [0, 1],
      duration: 1500,
      easing: 'easeOutQuart'
    });

    // Animación de la descripción
    anime({
      targets: '#heroDescription',
      translateY: [20, 0],
      opacity: [0, 1],
      delay: 300,
      duration: 1500,
      easing: 'easeOutQuart'
    });

    // Animación de los botones
    anime({
      targets: '#heroButtons a',
      translateX: [-20, 0],
      opacity: [0, 1],
      delay: anime.stagger(200),
      duration: 1200,
      easing: 'easeOutQuart'
    });

    // Animación de las tarjetas de acceso rápido
    anime({
      targets: '#quickAccessCards a',
      translateY: [30, 0],
      opacity: [0, 1],
      delay: anime.stagger(300),
      duration: 1800,
      easing: 'easeOutQuart'
    });

    // Animación de las imágenes en las tarjetas
    anime({
      targets: '.aspect-\\[4\\/3\\] img',
      scale: [0.98, 1],
      opacity: [0, 1],
      delay: anime.stagger(200),
      duration: 1500,
      easing: 'easeOutQuart'
    });

    // Animación del título centro de control
    anime({
      targets: ['#sectionTitle', '#sectionDescription'],
      translateY: [15, 0],
      opacity: [0, 1],
      delay: anime.stagger(200),
      duration: 1500,
      easing: 'easeOutQuart'
    });
}); 