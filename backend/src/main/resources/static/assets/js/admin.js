// frontend/assets/js/admin.js
const REPORT_API = "http://localhost:8080/api/reports";

// Run on reports.html
if (window.location.pathname.includes("reports.html")) {
  document.addEventListener("DOMContentLoaded", async () => {
    const monthlySalesEl = document.getElementById("monthly-sales-value");
    const dateForm = document.getElementById("sales-date-form");
    const dateInput = document.getElementById("report-date-input");
    const dateSalesResultEl = document.getElementById("date-sales-result");
    const topCustomersBody = document.getElementById("top-customers-body");
    const topBooksBody = document.getElementById("top-books-body");
    const restockCountForm = document.getElementById("restock-count-form");
    const restockIsbnInput = document.getElementById("restock-isbn-input");
    const restockCountResultEl = document.getElementById("restock-count-result");

    const setTableEmptyRow = (tbody, message, colSpan) => {
      if (!tbody) return;
      tbody.innerHTML = "";
      const tr = document.createElement("tr");
      const td = document.createElement("td");
      td.colSpan = colSpan;
      td.textContent = message;
      tr.appendChild(td);
      tbody.appendChild(tr);
    };

    const formatMoney = (value) => {
      const num = Number(value);
      if (Number.isFinite(num)) return `$${num.toFixed(2)}`;
      return String(value ?? "");
    };

    // 1) Monthly Sales
    if (monthlySalesEl) {
      try {
        const salesRes = await fetch(`${REPORT_API}/sales/last-month`);
        if (!salesRes.ok) throw new Error(`HTTP ${salesRes.status}`);
        const salesText = await salesRes.text();
        monthlySalesEl.textContent = salesText;
      } catch (e) {
        console.error("Failed to load last-month sales", e);
        monthlySalesEl.textContent = "Failed to load";
      }
    }

    // 2) Sales by Date
    if (dateForm && dateInput && dateSalesResultEl) {
      dateForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const date = (dateInput.value || "").trim();
        if (!date) {
          dateSalesResultEl.textContent = "Please select a date.";
          return;
        }

        dateSalesResultEl.textContent = "Loading...";

        try {
          const res = await fetch(
            `${REPORT_API}/sales/date?date=${encodeURIComponent(date)}`
          );
          if (!res.ok) throw new Error(`HTTP ${res.status}`);
          const text = await res.text();
          dateSalesResultEl.textContent = text;
        } catch (e) {
          console.error("Failed to load sales by date", e);
          dateSalesResultEl.textContent = "Failed to load";
        }
      });
    }

    // 3) Top Customers
    if (topCustomersBody) {
      try {
        const custRes = await fetch(`${REPORT_API}/top-customers`);
        if (!custRes.ok) throw new Error(`HTTP ${custRes.status}`);
        const customers = await custRes.json();

        topCustomersBody.innerHTML = "";

        if (!Array.isArray(customers) || customers.length === 0) {
          setTableEmptyRow(topCustomersBody, "No customer data", 2);
        } else {
          customers.forEach((c) => {
            const tr = document.createElement("tr");

            const tdName = document.createElement("td");
            tdName.textContent = c?.username ?? "";

            const tdSpent = document.createElement("td");
            tdSpent.textContent = formatMoney(c?.totalSpent);

            tr.appendChild(tdName);
            tr.appendChild(tdSpent);
            topCustomersBody.appendChild(tr);
          });
        }
      } catch (e) {
        console.error("Failed to load top customers", e);
        setTableEmptyRow(topCustomersBody, "Failed to load", 2);
      }
    }

    // 4) Top Books
    if (topBooksBody) {
      try {
        const booksRes = await fetch(`${REPORT_API}/top-books`);
        if (!booksRes.ok) throw new Error(`HTTP ${booksRes.status}`);
        const books = await booksRes.json();

        topBooksBody.innerHTML = "";

        if (!Array.isArray(books) || books.length === 0) {
          setTableEmptyRow(topBooksBody, "No book data", 2);
        } else {
          books.forEach((b) => {
            const tr = document.createElement("tr");

            const tdTitle = document.createElement("td");
            tdTitle.textContent = b?.title ?? "";

            const tdSold = document.createElement("td");
            tdSold.textContent = String(b?.totalCopiesSold ?? "");

            tr.appendChild(tdTitle);
            tr.appendChild(tdSold);
            topBooksBody.appendChild(tr);
          });
        }
      } catch (e) {
        console.error("Failed to load top books", e);
        setTableEmptyRow(topBooksBody, "Failed to load", 2);
      }
    }

    // 5) Replenishment order count for a specific book
    if (restockCountForm && restockIsbnInput && restockCountResultEl) {
      restockCountForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        // normalize to digits-only ISBN to match backend storage style
        let isbnRaw = (restockIsbnInput.value || "").trim();
        isbnRaw = isbnRaw.replace(/\D/g, "");
        if (!/^\d{13}$/.test(isbnRaw)) {
          restockCountResultEl.textContent = "Please enter a 13-digit ISBN.";
          return;
        }

        restockCountResultEl.textContent = "Loading...";

        try {
          const res = await fetch(
            `${REPORT_API}/restock-count?isbn=${encodeURIComponent(isbnRaw)}`
          );
          if (!res.ok) throw new Error(`HTTP ${res.status}`);
          const countText = await res.text();
          restockCountResultEl.textContent = `Total replenishment orders: ${countText}`;
        } catch (e) {
          console.error("Failed to load restock count", e);
          restockCountResultEl.textContent = "Failed to load";
        }
      });
    }
  });
}
