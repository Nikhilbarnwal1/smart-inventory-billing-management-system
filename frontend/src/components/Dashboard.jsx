import { useEffect, useState } from 'react'

function Dashboard() {
  const [summary, setSummary] = useState(null)

  useEffect(() => {
    fetch('http://localhost:8080/api/dashboard/summary')
      .then((response) => response.json())
      .then((data) => setSummary(data))
      .catch((error) => console.error('Error:', error))
  }, [])

  return (
    <div className="dashboard">
      <h1>Dashboard</h1>

      {summary ? (
        <div className="dashboard-grid">

          <div className="dashboard-card">
            <h3>Total Products</h3>
            <p>{summary.totalProducts}</p>
          </div>

          <div className="dashboard-card">
            <h3>Total Stock</h3>
            <p>{summary.totalStockQuantity}</p>
          </div>

          <div className="dashboard-card">
            <h3>Inventory Value</h3>
            <p>₹{summary.totalInventoryValue}</p>
          </div>

          <div className="dashboard-card">
            <h3>Low Stock</h3>
            <p>{summary.lowStockProducts}</p>
          </div>

          <div className="dashboard-card">
            <h3>Total Sales</h3>
            <p>₹{summary.totalSales}</p>
          </div>

          <div className="dashboard-card">
            <h3>Total Purchases</h3>
            <p>₹{summary.totalPurchases}</p>
          </div>

          <div className="dashboard-card">
            <h3>Total Profit</h3>
            <p>₹{summary.totalProfit}</p>
          </div>

          <div className="dashboard-card">
            <h3>Total Customers</h3>
            <p>{summary.totalCustomers}</p>
          </div>

          <div className="dashboard-card">
            <h3>Total Suppliers</h3>
            <p>{summary.totalSuppliers}</p>
          </div>

        </div>
      ) : (
        <p>Loading dashboard...</p>
      )}
    </div>
  )
}

export default Dashboard