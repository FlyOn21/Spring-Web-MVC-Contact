window.onload = function() {
    setTimeout(function() {
        const successMsg = document.querySelector('.success-msg', );
        const errorMsg = document.querySelector('.error-msg');
        if (successMsg) {
            // Start the fade-out transition
            successMsg.style.opacity = '0';

            // Wait for the transition to finish before setting display to 'none'
            setTimeout(function() {
                successMsg.style.display = 'none';
            }, 1000); // Wait an additional 1 second to match the transition duration
        }
        if (errorMsg) {
            errorMsg.style.opacity = '0';
            setTimeout(function() {
                errorMsg.style.display = 'none';
            }, 1000);
        }
    }, 5000); // Starts hiding after 5 seconds
};