// frontend/assets/js/auth.js
const API_URL = "http://localhost:8080/api/users";

// 1. SIGNUP LOGIC
const signupForm = document.getElementById("signup-form") || document.querySelector("form[action='#']");
if (signupForm && window.location.pathname.includes("signup.html")) {
  signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(signupForm);
    const entries = Object.fromEntries(formData.entries());

    // Map form fields (snake_case in HTML) to camelCase expected by backend
    const user = {
      username: entries.username,
      password: entries.password,
      firstName: entries.first_name || entries.firstName || '',
      lastName: entries.last_name || entries.lastName || '',
      email: entries.email || '',
      phoneNumber: entries.phone || entries.phoneNumber || '',
      shippingAddress: entries.address || entries.shippingAddress || '',
      isAdmin: false,
    };

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
const loginForm = document.getElementById("login-form") || document.querySelector("form[action='#']");
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

      // If backend returns empty body (login failed), avoid calling json()
      const text = await response.text();
      if (!text) {
        alert("Invalid Username or Password");
        return;
      }
      const user = JSON.parse(text);

      if (user) {
        // SAVE USER TO BROWSER STORAGE
        localStorage.setItem("user", JSON.stringify(user));
        alert("Login Successful!");

        // Redirect based on role (backend serializes boolean as 'admin')
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

// 4. NAV RENDER
function renderNav() {
  const nav = document.querySelector('.nav-links');
  if (!nav) return;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.username) {
      let html = '<a href="index.html">Home</a>' +
                 `<a href="cart.html">Cart (<span id="cart-count">0</span>)</a>` +
                 '<a href="edit_profile.html">Profile</a>' +
                 '<a href="#" onclick="logout()">Logout</a>';
      if (user.admin) {
        html += '<a href="admin_dashboard.html">Admin</a>' +
                '<a href="add_book.html">Add Book</a>' +
                '<a href="manage_users.html">Manage Users</a>';
      }
      nav.innerHTML = html;
    } else {
      nav.innerHTML = '<a href="index.html">Home</a>' +
                       '<a href="cart.html">Cart (<span id="cart-count">0</span>)</a>' +
                       '<a href="login.html" class="btn">Login</a>' +
                       '<a href="signup.html" class="btn">Sign Up</a>';
    }
  } catch (e) {
    console.error('renderNav error', e);
  }
}

// Make available globally and run on load
window.renderNav = renderNav;
document.addEventListener('DOMContentLoaded', renderNav);
