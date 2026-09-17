import { useEffect, useState } from 'react'

function Purchases() {
  const [purchases, setPurchases] = useState([])
  const [products, setProducts] = useState([])
  const [suppliers, setSuppliers] = useState([])

  const [form, setForm] = useState({
    productId: '',
    supplierId: '',
    quantity: '',
    purchasePrice: '',
  })

  const loadPurchases = () => {
    fetch('http://localhost:8080/api/purchases')
      .then((response) => response.json())
      .then((data) => setPurchases(data))
      .catch((error) => console.error('Error:', error))
  }

  const loadProducts = () => {
    fetch('http://localhost:8080/api/products')
      .then((response) => response.json())
      .then((data) => {
        const sortedProducts = data.sort((a, b) =>
          a.name.localeCompare(b.name)
        )

        setProducts(sortedProducts)
      })
      .catch((error) => console.error('Error:', error))
  }

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
    loadPurchases()
    loadProducts()
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

    const purchase = {
      productId: Number(form.productId),
      supplierId: form.supplierId
        ? Number(form.supplierId)
        : null,
      quantity: Number(form.quantity),
      purchasePrice: Number(form.purchasePrice),
    }

    fetch('http://localhost:8080/api/purchases', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(purchase),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to add purchase')
        }

        return response.json()
      })
      .then(() => {
        setForm({
          productId: '',
          supplierId: '',
          quantity: '',
          purchasePrice: '',
        })

        loadPurchases()
        loadProducts()
      })
      .catch((error) => console.error('Error:', error))
  }

  return (
    <div className="page">
      <h1>Purchases</h1>

      <form className="purchase-form" onSubmit={handleSubmit}>

        <label>
          Product
          <select
            name="productId"
            value={form.productId}
            onChange={handleChange}
            required
          >
            <option value="">Select Product</option>

            {products.map((product) => (
              <option key={product.id} value={product.id}>
                {product.name}
              </option>
            ))}
          </select>
        </label>

        <label>
          Supplier
          <select
            name="supplierId"
            value={form.supplierId}
            onChange={handleChange}
          >
            <option value="">No Supplier</option>

            {suppliers.map((supplier) => (
              <option key={supplier.id} value={supplier.id}>
                {supplier.name}
              </option>
            ))}
          </select>
        </label>

        <label>
          Quantity
          <input
            type="number"
            name="quantity"
            placeholder="Enter quantity"
            value={form.quantity}
            onChange={handleChange}
            min="1"
            required
          />
        </label>

        <label>
          Purchase Price
          <input
            type="number"
            name="purchasePrice"
            placeholder="Enter purchase price"
            value={form.purchasePrice}
            onChange={handleChange}
            min="0"
            step="0.01"
            required
          />
        </label>

        <button type="submit">
          Add Purchase
        </button>

      </form>

      {purchases.length > 0 ? (
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Product</th>
              <th>Supplier</th>
              <th>Quantity</th>
              <th>Purchase Price</th>
              <th>Total Amount</th>
              <th>Date</th>
            </tr>
          </thead>

          <tbody>
            {purchases.map((purchase) => {
              const totalAmount =
                purchase.quantity * purchase.purchasePrice

              return (
                <tr key={purchase.id}>
                  <td>{purchase.id}</td>

                  <td>
                    {products.find(
                      (product) =>
                        product.id === purchase.productId
                    )?.name || '-'}
                  </td>

                  <td>
                    {suppliers.find(
                      (supplier) =>
                        supplier.id === purchase.supplierId
                    )?.name || '-'}
                  </td>

                  <td>
                    {purchase.quantity}
                  </td>

                  <td>
                    ₹{Number(
                      purchase.purchasePrice
                    ).toFixed(2)}
                  </td>

                  <td>
                    ₹{Number(
                      totalAmount
                    ).toFixed(2)}
                  </td>

                  <td>
                    {purchase.date || '-'}
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      ) : (
        <p>No purchases found.</p>
      )}
    </div>
  )
}

export default Purchases