import { useEffect, useState } from 'react'

function Sales() {
  const [sales, setSales] = useState([])
  const [products, setProducts] = useState([])
  const [customers, setCustomers] = useState([])

  const [showPreviousSales, setShowPreviousSales] = useState(false)

  const [customerBalance, setCustomerBalance] = useState(null)

  const [message, setMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')

  const [form, setForm] = useState({
    productId: '',
    customerId: '',
    customerName: '',
    gstNumber: '',
    quantity: '',
    sellingPrice: '',
    discountPercentage: '',
    cgstPercentage: '',
    sgstPercentage: '',
    paymentMethod: '',
    paidAmount: '',
  })

  const loadSales = () => {
    fetch('http://localhost:8080/api/sales')
      .then((response) => response.json())
      .then((data) => setSales(data))
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

  const loadCustomers = () => {
    fetch('http://localhost:8080/api/customers')
      .then((response) => response.json())
      .then((data) => {
        const sortedCustomers = data.sort((a, b) =>
          a.name.localeCompare(b.name)
        )

        setCustomers(sortedCustomers)
      })
      .catch((error) => console.error('Error:', error))
  }

  const loadCustomerBalance = (customerId) => {
    if (!customerId) {
      setCustomerBalance(null)
      return
    }

    fetch(
      `http://localhost:8080/api/customer-balance/customer/${customerId}`
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to load customer balance')
        }

        return response.json()
      })
      .then((data) => {
        setCustomerBalance(data)
      })
      .catch((error) => {
        console.error('Error:', error)
        setCustomerBalance(null)
      })
  }

  useEffect(() => {
    loadSales()
    loadProducts()
    loadCustomers()
  }, [])

  const handleChange = (event) => {
    setForm({
      ...form,
      [event.target.name]: event.target.value,
    })
  }

  const handleProductChange = (event) => {
    const productId = event.target.value

    const product = products.find(
      (item) => item.id === Number(productId)
    )

    setForm({
      ...form,
      productId,
      sellingPrice: product
        ? product.sellingPrice
        : '',
    })
  }

  const handleCustomerChange = (event) => {
    const customerId = event.target.value

    const customer = customers.find(
      (item) => item.id === Number(customerId)
    )

    setForm({
      ...form,
      customerId,
      customerName: customer
        ? customer.name
        : '',
    })

    loadCustomerBalance(customerId)
  }

  const calculateTotalAmount = () => {
    const quantity = Number(form.quantity || 0)
    const sellingPrice = Number(form.sellingPrice || 0)
    const discountPercentage =
      Number(form.discountPercentage || 0)
    const cgstPercentage =
      Number(form.cgstPercentage || 0)
    const sgstPercentage =
      Number(form.sgstPercentage || 0)

    const subtotal = quantity * sellingPrice

    const discountAmount =
      subtotal * discountPercentage / 100

    const finalAmount =
      subtotal - discountAmount

    const cgstAmount =
      finalAmount * cgstPercentage / 100

    const sgstAmount =
      finalAmount * sgstPercentage / 100

    const totalAmount =
      finalAmount + cgstAmount + sgstAmount

    return totalAmount
  }

  const calculateRemainingAmount = () => {
    const paidAmount = Number(form.paidAmount || 0)
    const remainingAmount =
      totalAmount - paidAmount

    return Math.max(0, remainingAmount)
  }

  const handleSubmit = (event) => {
    event.preventDefault()

    setMessage('')
    setErrorMessage('')

    const sale = {
      productId: Number(form.productId),

      customerId: form.customerId
        ? Number(form.customerId)
        : null,

      customerName: form.customerName,

      gstNumber: form.gstNumber,

      quantity: Number(form.quantity),

      sellingPrice: Number(form.sellingPrice),

      discountPercentage:
        Number(form.discountPercentage || 0),

      cgstPercentage:
        Number(form.cgstPercentage || 0),

      sgstPercentage:
        Number(form.sgstPercentage || 0),

      paymentMethod: form.paymentMethod,

      paidAmount:
        Number(form.paidAmount || 0),
    }

    fetch('http://localhost:8080/api/sales', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(sale),
    })
      .then(async (response) => {
        if (!response.ok) {
          const errorText = await response.text()

          throw new Error(
            errorText || 'Failed to create sale'
          )
        }

        return response.json()
      })
      .then((createdSale) => {
        setMessage(
          `Sale created successfully! Invoice #${createdSale.id}`
        )

        const selectedCustomerId = form.customerId

        setForm({
          productId: '',
          customerId: '',
          customerName: '',
          gstNumber: '',
          quantity: '',
          sellingPrice: '',
          discountPercentage: '',
          cgstPercentage: '',
          sgstPercentage: '',
          paymentMethod: '',
          paidAmount: '',
        })

        loadSales()
        loadProducts()

        if (selectedCustomerId) {
          loadCustomerBalance(selectedCustomerId)
        } else {
          setCustomerBalance(null)
        }
      })
      .catch((error) => {
        console.error('Error:', error)

        setErrorMessage(
          `Failed to create sale: ${error.message}`
        )
      })
  }

  const totalAmount = calculateTotalAmount()
  const remainingAmount = calculateRemainingAmount()

  return (
    <div className="page">
      <h1>Sales</h1>

      <form className="sale-form" onSubmit={handleSubmit}>

        <label>
          Product
          <select
            name="productId"
            value={form.productId}
            onChange={handleProductChange}
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
          Customer
          <select
            name="customerId"
            value={form.customerId}
            onChange={handleCustomerChange}
          >
            <option value="">Walk-in Customer</option>

            {customers.map((customer) => (
              <option key={customer.id} value={customer.id}>
                {customer.name}
              </option>
            ))}
          </select>
        </label>

        {customerBalance && (
          <div className="customer-balance-box">
            <strong>Current Customer Balance</strong>

            <div>
              Outstanding:
              ₹{Number(
                customerBalance.outstandingBalance || 0
              ).toFixed(2)}
            </div>

            {Number(customerBalance.advanceAmount || 0) > 0 && (
              <div>
                Advance:
                ₹{Number(
                  customerBalance.advanceAmount
                ).toFixed(2)}
              </div>
            )}
          </div>
        )}

        <label>
          Customer Name
          <input
            type="text"
            name="customerName"
            placeholder="Enter customer name"
            value={form.customerName}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          GST Number
          <input
            type="text"
            name="gstNumber"
            placeholder="Optional"
            value={form.gstNumber}
            onChange={handleChange}
          />
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
          Selling Price
          <input
            type="number"
            name="sellingPrice"
            placeholder="Automatically filled"
            value={form.sellingPrice}
            onChange={handleChange}
            min="0"
            step="0.01"
            required
          />
        </label>

        <label>
          Discount %
          <input
            type="number"
            name="discountPercentage"
            placeholder="0"
            value={form.discountPercentage}
            onChange={handleChange}
            min="0"
            step="0.01"
          />
        </label>

        <label>
          CGST %
          <input
            type="number"
            name="cgstPercentage"
            placeholder="0"
            value={form.cgstPercentage}
            onChange={handleChange}
            min="0"
            step="0.01"
          />
        </label>

        <label>
          SGST %
          <input
            type="number"
            name="sgstPercentage"
            placeholder="0"
            value={form.sgstPercentage}
            onChange={handleChange}
            min="0"
            step="0.01"
          />
        </label>

        <label>
          Total Amount
          <input
            type="text"
            value={`₹${totalAmount.toFixed(2)}`}
            readOnly
          />
        </label>

        <label>
          Payment Method
          <select
            name="paymentMethod"
            value={form.paymentMethod}
            onChange={handleChange}
            required
          >
            <option value="">Select Payment Method</option>
            <option value="CASH">Cash</option>
            <option value="UPI">UPI</option>
            <option value="CARD">Card</option>
            <option value="BANK_TRANSFER">
              Bank Transfer
            </option>
            <option value="CREDIT">
              Credit / Pending
            </option>
          </select>
        </label>

        <label>
          Paid Amount
          <input
            type="number"
            name="paidAmount"
            placeholder="Enter amount paid"
            value={form.paidAmount}
            onChange={handleChange}
            min="0"
            step="0.01"
            required
          />
        </label>

        <label>
          Remaining Amount
          <input
            type="text"
            value={`₹${remainingAmount.toFixed(2)}`}
            readOnly
          />
        </label>

        <div className="sale-submit-section">
          <button type="submit">
            Create Sale
          </button>

          {message && (
            <div className="success-message">
              {message}
            </div>
          )}

          {errorMessage && (
            <div className="error-message">
              {errorMessage}
            </div>
          )}
        </div>

      </form>

      <button
        type="button"
        onClick={() =>
          setShowPreviousSales(!showPreviousSales)
        }
      >
        {showPreviousSales
          ? 'Hide Previous Sales'
          : 'Previous Sales'}
      </button>

      {showPreviousSales && (
        <>
          {sales.length > 0 ? (
            <table className="data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Product</th>
                  <th>Customer</th>
                  <th>Quantity</th>
                  <th>Selling Price</th>
                  <th>Discount %</th>
                  <th>CGST %</th>
                  <th>SGST %</th>
                  <th>Payment Method</th>
                  <th>Paid Amount</th>
                  <th>Pending Amount</th>
                  <th>Grand Total</th>
                  <th>Date</th>
                </tr>
              </thead>

              <tbody>
                {sales.map((sale) => {
                  const product = products.find(
                    (item) =>
                      item.id === sale.productId
                  )

                  return (
                    <tr key={sale.id}>
                      <td>{sale.id}</td>

                      <td>
                        {product?.name || '-'}
                      </td>

                      <td>
                        {sale.customerName}
                      </td>

                      <td>
                        {sale.quantity}
                      </td>

                      <td>
                        ₹{Number(
                          sale.sellingPrice
                        ).toFixed(2)}
                      </td>

                      <td>
                        {Number(
                          sale.discountPercentage || 0
                        ).toFixed(2)}%
                      </td>

                      <td>
                        {Number(
                          sale.cgstPercentage || 0
                        ).toFixed(2)}%
                      </td>

                      <td>
                        {Number(
                          sale.sgstPercentage || 0
                        ).toFixed(2)}%
                      </td>

                      <td>
                        {sale.paymentMethod || '-'}
                      </td>

                      <td>
                        ₹{Number(
                          sale.paidAmount || 0
                        ).toFixed(2)}
                      </td>

                      <td>
                        ₹{Number(
                          sale.pendingAmount || 0
                        ).toFixed(2)}
                      </td>

                      <td>
                        ₹{Number(
                          sale.grandTotal || 0
                        ).toFixed(2)}
                      </td>

                      <td>
                        {sale.date || '-'}
                      </td>
                    </tr>
                  )
                })}
              </tbody>
            </table>
          ) : (
            <p>No previous sales found.</p>
          )}
        </>
      )}
    </div>
  )
}

export default Sales