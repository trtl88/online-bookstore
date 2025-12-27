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
      if (response.ok && text.includes("Success")) window.location.href = "login.html";
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

      // If login failed, show server-provided message or a generic one
      if (!response.ok) {
        const text = await response.text().catch(() => '');
        alert(text || 'Invalid Username or Password');
        return;
      }

      // Successful login -> parse JSON
      const user = await response.json();

      if (user) {
        // SAVE USER TO BROWSER STORAGE
        localStorage.setItem("user", JSON.stringify(user));
        // record login time to allow controlled server-restart logout
        try { localStorage.setItem('userLoginAt', String(Date.now())); } catch(e) {}
        alert("Login Successful!");

        // Redirect based on role (supports `admin` or `isAdmin`)
        if (user.admin || user.isAdmin) {
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
async function logout() {
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.username) {
      // Attempt to clear server-side shopping cart for this user before logging out
      await fetch(`http://localhost:8080/api/cart/clear?username=${encodeURIComponent(user.username)}`, {
        method: 'DELETE'
      }).catch(() => {});
    }
  } catch (e) {
    // ignore parse errors and continue logout
  }
  localStorage.removeItem("user");
  try { localStorage.removeItem('userLoginAt'); } catch(e) {}
  window.location.href = "index.html";
}

// 4. NAV RENDER
function renderNav() {
  const nav = document.querySelector('.nav-links');
  if (!nav) return;
    try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.username) {
      // Build nav so that Logout is always last
      let html = '<a href="index.html">Home</a>' +
                 `<a href="cart.html">Cart (<span id="cart-count">0</span>)</a>` +
                 '<a href="edit_profile.html">Profile</a>' +
                 '<a href="order_history.html">Order History</a>';
      if (user.admin || user.isAdmin) {
        html += '<a href="admin_dashboard.html">Admin</a>' +
                '<a href="add_book.html">Add Book</a>' +
                '<a href="manage_users.html">Manage Users</a>';
      }
      // Logout should be the final item
      html += '<a href="#" onclick="logout()">Logout</a>';
      nav.innerHTML = html;
      // update cart count immediately
      const countEl = document.getElementById('cart-count');
      if (countEl && user.username) {
        fetch(`http://localhost:8080/api/cart/${encodeURIComponent(user.username)}`)
          .then(r => r.json())
          .then(items => { countEl.textContent = items.length; })
          .catch(() => {});
      }
    } else {
      // Not logged in: hide Home, Cart, and Order History per request; show only auth links
      nav.innerHTML = '<a href="login.html" class="btn">Login</a>' +
                       '<a href="signup.html" class="btn">Sign Up</a>';
    }
  } catch (e) {
    console.error('renderNav error', e);
  }
}

// Make available globally and run on load
window.renderNav = renderNav;
document.addEventListener('DOMContentLoaded', renderNav);
