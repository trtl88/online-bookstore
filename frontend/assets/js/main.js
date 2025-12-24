// assets/js/main.js

document.addEventListener('DOMContentLoaded', function() {
    console.log("Bookstore Frontend Loaded");

    // Example: Add event listeners to all 'Add to Cart' buttons
    const addToCartButtons = document.querySelectorAll('.book-card .btn-submit');
    
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            // In the future, this will use fetch() to talk to the backend
            alert("This item has been added to your cart!");
        });
    });
});