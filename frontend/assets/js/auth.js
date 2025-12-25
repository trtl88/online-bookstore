// frontend/assets/js/auth.js
const API_URL = "http://localhost:8080/api/users";

// 1. SIGNUP LOGIC
const signupForm = document.querySelector("form[action='#']"); // Targeting the form on signup.html
if (signupForm && window.location.pathname.includes("signup.html")) {
  signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(signupForm);
    const user = Object.fromEntries(formData.entries()); // Converts form to JSON object
    user.isAdmin = false; // Default to customer

    try {
      const response = await fetch(`${API_URL}/signup`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
      });
      const text = await response.text();
      alert(text);
      if (text.includes("Success")) window.location.href = "login.html";
    } catch (error) {
      console.error(error);
      alert("Error connecting to server.");
    }
  });
}

// 2. LOGIN LOGIC
const loginForm = document.querySelector("form[action='#']"); // Targeting login.html
if (loginForm && window.location.pathname.includes("login.html")) {
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(loginForm);
    const loginRequest = Object.fromEntries(formData.entries());

    try {
      const response = await fetch(`${API_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginRequest),
      });

      const user = await response.json(); // Returns User object or null
      if (user) {
        // SAVE USER TO BROWSER STORAGE
        localStorage.setItem("user", JSON.stringify(user));
        alert("Login Successful!");

        // Redirect based on role
        if (user.admin) {
          window.location.href = "admin_dashboard.html";
        } else {
          window.location.href = "index.html";
        }
      } else {
        alert("Invalid Username or Password");
      }
    } catch (error) {
      console.error(error);
      alert("Login Failed.");
    }
  });
}

// 3. LOGOUT LOGIC (Attach to any logout button)
function logout() {
  localStorage.removeItem("user");
  window.location.href = "index.html";
}
