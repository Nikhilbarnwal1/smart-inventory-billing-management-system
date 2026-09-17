import { Link } from 'react-router-dom'

function Navbar() {
  return (
    <nav>
      <div className="navbar-brand">
        <h2>Smart Inventory & Billing</h2>
        <span>Management System</span>
      </div>

      <div className="navbar-links">
        <Link to="/">Dashboard</Link>
        <Link to="/products">Products</Link>
        <Link to="/purchases">Purchases</Link>
        <Link to="/sales">Sales</Link>
        <Link to="/customers">Customers</Link>
        <Link to="/suppliers">Suppliers</Link>
      </div>
    </nav>
  )
}

export default Navbar