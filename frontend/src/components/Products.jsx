import { useEffect, useState } from 'react'

function Products() {
  const [products, setProducts] = useState([])

  const [form, setForm] = useState({
    name: '',
    category: '',
    purchasePrice: '',
    sellingPrice: '',
    stockQuantity: '',
  })

  const [editingId, setEditingId] = useState(null)

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

  useEffect(() => {
    loadProducts()
  }, [])

  const handleChange = (event) => {
    setForm({
      ...form,
      [event.target.name]: event.target.value,
    })
  }

  const handleSubmit = (event) => {
    event.preventDefault()

    const product = {
      name: form.name,
      category: form.category,
      purchasePrice: Number(form.purchasePrice),
      sellingPrice: Number(form.sellingPrice),
      stockQuantity: Number(form.stockQuantity),
    }

    const url = editingId
      ? `http://localhost:8080/api/products/${editingId}`
      : 'http://localhost:8080/api/products'

    const method = editingId ? 'PUT' : 'POST'

    fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(product),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to save product')
        }

        return response.json()
      })
      .then(() => {
        setForm({
          name: '',
          category: '',
          purchasePrice: '',
          sellingPrice: '',
          stockQuantity: '',
        })

        setEditingId(null)

        loadProducts()
      })
      .catch((error) => console.error('Error:', error))
  }

  const handleEdit = (product) => {
    setEditingId(product.id)

    setForm({
      name: product.name,
      category: product.category,
      purchasePrice: product.purchasePrice,
      sellingPrice: product.sellingPrice,
      stockQuantity: product.stockQuantity,
    })
  }

  const handleDelete = (id) => {
    if (!window.confirm('Are you sure you want to delete this product?')) {
      return
    }

    fetch(`http://localhost:8080/api/products/${id}`, {
      method: 'DELETE',
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to delete product')
        }

        loadProducts()
      })
      .catch((error) => console.error('Error:', error))
  }

  const handleCancelEdit = () => {
    setEditingId(null)

    setForm({
      name: '',
      category: '',
      purchasePrice: '',
      sellingPrice: '',
      stockQuantity: '',
    })
  }

  return (
    <div className="page">
      <h1>Products</h1>

      <form className="product-form" onSubmit={handleSubmit}>

        <label>
          Product Name
          <input
            type="text"
            name="name"
            placeholder="Enter product name"
            value={form.name}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Category
          <input
            type="text"
            name="category"
            placeholder="Enter category"
            value={form.category}
            onChange={handleChange}
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

        <label>
          Selling Price
          <input
            type="number"
            name="sellingPrice"
            placeholder="Enter selling price"
            value={form.sellingPrice}
            onChange={handleChange}
            min="0"
            step="0.01"
            required
          />
        </label>

        <label>
          Stock Quantity
          <input
            type="number"
            name="stockQuantity"
            placeholder="Enter stock quantity"
            value={form.stockQuantity}
            onChange={handleChange}
            min="0"
            required
          />
        </label>

        <button type="submit">
          {editingId ? 'Update Product' : 'Add Product'}
        </button>

        {editingId && (
          <button type="button" onClick={handleCancelEdit}>
            Cancel
          </button>
        )}

      </form>

      {products.length > 0 ? (
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Category</th>
              <th>Purchase Price</th>
              <th>Selling Price</th>
              <th>Stock</th>
              <th>Stock Value</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {products.map((product) => {
              const stockValue =
                product.stockQuantity * product.purchasePrice

              return (
                <tr key={product.id}>
                  <td>{product.id}</td>

                  <td>{product.name}</td>

                  <td>{product.category}</td>

                  <td>
                    ₹{Number(
                      product.purchasePrice
                    ).toFixed(2)}
                  </td>

                  <td>
                    ₹{Number(
                      product.sellingPrice
                    ).toFixed(2)}
                  </td>

                  <td>
                    {product.stockQuantity}
                  </td>

                  <td>
                    ₹{Number(
                      stockValue
                    ).toFixed(2)}
                  </td>

                  <td>
                    <button
                      type="button"
                      onClick={() => handleEdit(product)}
                    >
                      Edit
                    </button>

                    <button
                      type="button"
                      onClick={() => handleDelete(product.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      ) : (
        <p>No products found.</p>
      )}
    </div>
  )
}

export default Products