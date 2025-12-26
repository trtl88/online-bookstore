// frontend/assets/js/cart.js
const CART_API = "http://localhost:8080/api/cart";

// 1. ADD TO CART (Used by buttons on Index page)
async function addToCart(isbn) {
  const user = JSON.parse(localStorage.getItem("user"));
  if (!user) {
    alert("Please login to buy books!");
    window.location.href = "login.html";
    return;
  }

  try {
    // POST /api/cart/add?username=...&isbn=...&quantity=1
    const res = await fetch(
      `${CART_API}/add?username=${user.username}&isbn=${isbn}&quantity=1`,
      {
        method: "POST",
      }
    );
    const text = await res.text();
    alert(text);
  } catch (err) {
    console.error(err);
    alert("Error adding to cart");
  }
}

// 2. LOAD CART PAGE
if (window.location.pathname.includes("cart.html")) {
  document.addEventListener("DOMContentLoaded", loadCart);
}

async function loadCart() {
  const user = JSON.parse(localStorage.getItem("user"));
  if (!user) return;

  const tbody = document.querySelector("tbody");
  tbody.innerHTML = ""; // Clear fake row

  try {
    const res = await fetch(`${CART_API}/${user.username}`);
    const items = await res.json();

    let grandTotal = 0;

    items.forEach((item) => {
      grandTotal += item.totalItemPrice;
      const row = `
                <tr>
                    <td>${item.title}</td>
                    <td>$${item.price}</td>
                    <td>${item.quantity}</td>
                    <td>$${item.totalItemPrice}</td>
                    <td><button class="btn-remove" onclick="removeItem('${item.bookIsbn}')">Remove</button></td>
                </tr>
            `;
      tbody.innerHTML += row;
    });

    // Update Total Text
    document.querySelector(
      ".cart-summary span"
    ).innerText = `$${grandTotal.toFixed(2)}`;
  } catch (err) {
    console.error(err);
  }
}

async function removeItem(isbn) {
  const user = JSON.parse(localStorage.getItem("user"));
  await fetch(`${CART_API}/remove?username=${user.username}&isbn=${isbn}`, {
    method: "DELETE",
  });
  location.reload(); // Refresh page
}
