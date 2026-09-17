import { useEffect, useState } from 'react'

function Customers() {
  const [customers, setCustomers] = useState([])

  const [form, setForm] = useState({
    name: '',
    phone: '',
    email: '',
    address: '',
  })

  const loadCustomers = () => {
    fetch('http://localhost:8080/api/customers')
      .then((response) => response.json())
      .then((data) => setCustomers(data))
      .catch((error) => console.error('Error:', error))
  }

  useEffect(() => {
    loadCustomers()
  }, [])

  const handleChange = (event) => {
    setForm({
      ...form,
      [event.target.name]: event.target.value,
    })
  }

  const handleSubmit = (event) => {
    event.preventDefault()

    const customer = {
      name: form.name,
      phone: form.phone,
      email: form.email,
      address: form.address,
    }

    fetch('http://localhost:8080/api/customers', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(customer),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to add customer')
        }

        return response.json()
      })
      .then(() => {
        setForm({
          name: '',
          phone: '',
          email: '',
          address: '',
        })

        loadCustomers()
      })
      .catch((error) => console.error('Error:', error))
  }

  return (
    <div className="page">
      <h1>Customers</h1>

      <form className="customer-form" onSubmit={handleSubmit}>

        <label>
          Customer Name
          <input
            type="text"
            name="name"
            placeholder="Enter customer name"
            value={form.name}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Phone
          <input
            type="text"
            name="phone"
            placeholder="Enter phone number"
            value={form.phone}
            onChange={handleChange}
          />
        </label>

        <label>
          Email
          <input
            type="email"
            name="email"
            placeholder="Enter email"
            value={form.email}
            onChange={handleChange}
          />
        </label>

        <label>
          Address
          <input
            type="text"
            name="address"
            placeholder="Enter address"
            value={form.address}
            onChange={handleChange}
          />
        </label>

        <button type="submit">
          Add Customer
        </button>

      </form>

      {customers.length > 0 ? (
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Phone</th>
              <th>Email</th>
              <th>Address</th>
            </tr>
          </thead>

          <tbody>
            {customers.map((customer) => (
              <tr key={customer.id}>
                <td>{customer.id}</td>
                <td>{customer.name}</td>
                <td>{customer.phone}</td>
                <td>{customer.email}</td>
                <td>{customer.address}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No customers found.</p>
      )}
    </div>
  )
}

export default Customers