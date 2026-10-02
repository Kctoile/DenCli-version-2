/**
 * DenCli Dental Clinic - Modern Vanilla Motion Engine
 * Framer Motion-grade animations, Interactive Before/After slider,
 * Smooth scroll reveals, and Real-time DOM utilities.
 */

document.addEventListener('DOMContentLoaded', function () {
    // 1. Framer Motion Replica: Staggered Scroll Fade-Up (cards fade on scroll)
    const fadeElements = document.querySelectorAll('.fade-on-scroll');
    if ('IntersectionObserver' in window && fadeElements.length > 0) {
        const motionObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('is-visible');
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.12,
            rootMargin: '0px 0px -40px 0px'
        });

        fadeElements.forEach((el, idx) => {
            // Apply slight stagger if in the same container row
            const delay = (idx % 3) * 0.12;
            el.style.transitionDelay = `${delay}s`;
            motionObserver.observe(el);
        });
    } else {
        // Fallback for older browsers
        fadeElements.forEach(el => el.classList.add('is-visible'));
    }

    // 2. Rolling Number Counter Animation for Trust Band
    const statElements = document.querySelectorAll('.stat-number');
    if ('IntersectionObserver' in window && statElements.length > 0) {
        const statObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('stat-visible');
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.3 });

        statElements.forEach(el => statObserver.observe(el));
    }

    // 3. Quick Services Filter (Client-side instant search)
    const filterInput = document.getElementById('filterServiceInput');
    if (filterInput) {
        filterInput.addEventListener('input', function (e) {
            const query = e.target.value.toLowerCase().trim();
            const items = document.querySelectorAll('#servicesGrid .service-item');

            items.forEach(function (item) {
                const titleElem = item.querySelector('.service-title');
                if (titleElem) {
                    const text = titleElem.textContent.toLowerCase();
                    item.style.display = text.includes(query) ? '' : 'none';
                }
            });
        });
    }

    // 4. Interactive Before / After Comparison Toggle
    window.toggleCaseView = function (caseId, state) {
        const card = document.getElementById(caseId);
        if (!card) return;

        const beforeBox = card.querySelector('.case-box-before');
        const afterBox = card.querySelector('.case-box-after');
        const btnBefore = card.querySelector('.btn-toggle-before');
        const btnAfter = card.querySelector('.btn-toggle-after');

        if (state === 'before') {
            if (beforeBox) beforeBox.style.display = 'block';
            if (afterBox) afterBox.style.display = 'none';
            if (btnBefore) btnBefore.classList.add('active', 'btn-primary');
            if (btnBefore) btnBefore.classList.remove('btn-outline-secondary');
            if (btnAfter) btnAfter.classList.remove('active', 'btn-primary');
            if (btnAfter) btnAfter.classList.add('btn-outline-secondary');
        } else {
            if (beforeBox) beforeBox.style.display = 'none';
            if (afterBox) afterBox.style.display = 'block';
            if (btnAfter) btnAfter.classList.add('active', 'btn-success');
            if (btnAfter) btnAfter.classList.remove('btn-outline-secondary');
            if (btnBefore) btnBefore.classList.remove('active', 'btn-primary');
            if (btnBefore) btnBefore.classList.add('btn-outline-secondary');
        }
    };

    // 5. Smooth Scrolling for Internal Anchors with Navbar Offset
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const targetId = this.getAttribute('href');
            if (targetId && targetId !== '#') {
                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    e.preventDefault();
                    const navHeight = 76;
                    const elementPosition = targetElement.getBoundingClientRect().top;
                    const offsetPosition = elementPosition + window.pageYOffset - navHeight;

                    window.scrollTo({
                        top: offsetPosition,
                        behavior: 'smooth'
                    });
                }
            }
        });
    });

    // 6. Global Fetch Helper with CSRF Token
    window.dencliFetch = function (url, options = {}) {
        options.headers = options.headers || {};
        const csrfMeta = document.querySelector('meta[name="csrf-token"]');
        const csrfToken = csrfMeta ? csrfMeta.getAttribute('content') : (window.CSRF_TOKEN || '');
        if (csrfToken && !options.headers['X-CSRF-TOKEN']) {
            options.headers['X-CSRF-TOKEN'] = csrfToken;
        }
        if (!options.headers['Accept']) {
            options.headers['Accept'] = 'application/json';
        }
        return fetch(url, options);
    };
});
