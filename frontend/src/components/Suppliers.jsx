import { useEffect, useState } from 'react'

function Suppliers() {
  const [suppliers, setSuppliers] = useState([])

  const [form, setForm] = useState({
    name: '',
    phone: '',
    email: '',
    address: '',
  })

  const loadSuppliers = () => {
    fetch('http://localhost:8080/api/suppliers')
      .then((response) => response.json())
      .then((data) => {
        const sortedSuppliers = data.sort((a, b) =>
          a.name.localeCompare(b.name)
        )

        setSuppliers(sortedSuppliers)
      })
      .catch((error) => console.error('Error:', error))
  }

  useEffect(() => {
    loadSuppliers()
  }, [])

  const handleChange = (event) => {
    setForm({
      ...form,
      [event.target.name]: event.target.value,
    })
  }

  const handleSubmit = (event) => {
    event.preventDefault()

    const supplier = {
      name: form.name,
      phone: form.phone,
      email: form.email,
      address: form.address,
    }

    fetch('http://localhost:8080/api/suppliers', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(supplier),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to add supplier')
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

        loadSuppliers()
      })
      .catch((error) => console.error('Error:', error))
  }

  return (
    <div className="page">
      <h1>Suppliers</h1>

      <form className="supplier-form" onSubmit={handleSubmit}>

        <label>
          Supplier Name
          <input
            type="text"
            name="name"
            placeholder="Enter supplier name"
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
          Add Supplier
        </button>

      </form>

      {suppliers.length > 0 ? (
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
            {suppliers.map((supplier) => (
              <tr key={supplier.id}>
                <td>{supplier.id}</td>
                <td>{supplier.name}</td>
                <td>{supplier.phone}</td>
                <td>{supplier.email}</td>
                <td>{supplier.address}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No suppliers found.</p>
      )}
    </div>
  )
}

export default Suppliers