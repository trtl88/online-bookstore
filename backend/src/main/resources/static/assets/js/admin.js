// frontend/assets/js/admin.js
const REPORT_API = "http://localhost:8080/api/reports";

// Run on reports.html
if (window.location.pathname.includes("reports.html")) {
  document.addEventListener("DOMContentLoaded", async () => {
    // 1. Monthly Sales
    const salesRes = await fetch(`${REPORT_API}/sales/last-month`);
    const salesText = await salesRes.text();
    document.querySelector(".report-section div").innerText = salesText;

    // 2. Top Customers (Example of populating table)
    const custRes = await fetch(`${REPORT_API}/top-customers`);
    const customers = await custRes.json();

    // Find the table and clear fake rows, then loop like we did in cart.js...
    // (You can implement the table filling logic similar to cart.js)
  });
}
