import { useEffect, useState } from 'react'

function Sales() {
  const [sales, setSales] = useState([])
  const [customerSales, setCustomerSales] = useState([])
  const [products, setProducts] = useState([])
  const [customers, setCustomers] = useState([])

  const [showPreviousSales, setShowPreviousSales] = useState(false)
  const [showCustomerHistory, setShowCustomerHistory] = useState(false)

  const [selectedHistoryCustomer, setSelectedHistoryCustomer] =
    useState('')

  const [customerBalance, setCustomerBalance] = useState(null)
  const [customerSummary, setCustomerSummary] = useState(null)

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

  const loadCustomerHistory = (customerId) => {
    if (!customerId) {
      setCustomerSales([])
      setCustomerSummary(null)
      return
    }

    fetch(
      `http://localhost:8080/api/sales/customer/${customerId}`
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to load customer sales history')
        }

        return response.json()
      })
      .then((data) => {
        const sortedSales = [...data].sort(
          (a, b) =>
            new Date(b.date) - new Date(a.date)
        )

        setCustomerSales(sortedSales)
      })
      .catch((error) => {
        console.error('Error:', error)
        setCustomerSales([])
      })

    fetch(
      `http://localhost:8080/api/sales/customer/${customerId}/summary`
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to load customer summary')
        }

        return response.json()
      })
      .then((data) => {
        setCustomerSummary(data)
      })
      .catch((error) => {
        console.error('Error:', error)
        setCustomerSummary(null)
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

  const handleHistoryCustomerChange = (event) => {
    const customerId = event.target.value

    setSelectedHistoryCustomer(customerId)

    loadCustomerHistory(customerId)
  }

  const calculateTotalAmount = () => {
    const quantity = Number(form.quantity || 0)

    const sellingPrice =
      Number(form.sellingPrice || 0)

    const discountPercentage =
      Number(form.discountPercentage || 0)

    const cgstPercentage =
      Number(form.cgstPercentage || 0)

    const sgstPercentage =
      Number(form.sgstPercentage || 0)

    const subtotal =
      quantity * sellingPrice

    const discountAmount =
      subtotal * discountPercentage / 100

    const finalAmount =
      subtotal - discountAmount

    const cgstAmount =
      finalAmount * cgstPercentage / 100

    const sgstAmount =
      finalAmount * sgstPercentage / 100

    const totalAmount =
      finalAmount +
      cgstAmount +
      sgstAmount

    return totalAmount
  }

  const totalAmount = calculateTotalAmount()

  const calculateRemainingAmount = () => {
    const paidAmount =
      Number(form.paidAmount || 0)

    const remainingAmount =
      totalAmount - paidAmount

    return Math.max(0, remainingAmount)
  }

  const remainingAmount =
    calculateRemainingAmount()

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

      sellingPrice:
        Number(form.sellingPrice),

      discountPercentage:
        Number(form.discountPercentage || 0),

      cgstPercentage:
        Number(form.cgstPercentage || 0),

      sgstPercentage:
        Number(form.sgstPercentage || 0),

      paymentMethod:
        form.paymentMethod,

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
          const errorText =
            await response.text()

          throw new Error(
            errorText ||
            'Failed to create sale'
          )
        }

        return response.json()
      })
      .then((createdSale) => {
        setMessage(
          `Sale created successfully! Invoice #${createdSale.id}`
        )

        const selectedCustomerId =
          form.customerId

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
          loadCustomerBalance(
            selectedCustomerId
          )

          if (
            selectedHistoryCustomer ===
            selectedCustomerId
          ) {
            loadCustomerHistory(
              selectedCustomerId
            )
          }
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

  const formatDateHeading = (dateValue) => {
    if (!dateValue) {
      return 'Unknown Date'
    }

    const date = new Date(dateValue)

    if (isNaN(date.getTime())) {
      return dateValue
    }

    const today = new Date()

    const yesterday = new Date()

    yesterday.setDate(
      yesterday.getDate() - 1
    )

    const dateOnly = new Date(
      date.getFullYear(),
      date.getMonth(),
      date.getDate()
    )

    const todayOnly = new Date(
      today.getFullYear(),
      today.getMonth(),
      today.getDate()
    )

    const yesterdayOnly = new Date(
      yesterday.getFullYear(),
      yesterday.getMonth(),
      yesterday.getDate()
    )

    if (
      dateOnly.getTime() ===
      todayOnly.getTime()
    ) {
      return 'Today'
    }

    if (
      dateOnly.getTime() ===
      yesterdayOnly.getTime()
    ) {
      return 'Yesterday'
    }

    return date.toLocaleDateString(
      'en-IN',
      {
        day: '2-digit',
        month: 'long',
        year: 'numeric',
      }
    )
  }

  const formatTime = (dateValue) => {
    if (!dateValue) {
      return '-'
    }

    const date = new Date(dateValue)

    if (isNaN(date.getTime())) {
      return '-'
    }

    return date.toLocaleTimeString(
      'en-IN',
      {
        hour: '2-digit',
        minute: '2-digit',
      }
    )
  }

  const groupSalesByDate = (salesList) => {
    const groups = {}

    salesList.forEach((sale) => {
      const date = new Date(sale.date)

      const dateKey = isNaN(date.getTime())
        ? 'unknown'
        : date.toLocaleDateString(
            'en-CA'
          )

      if (!groups[dateKey]) {
        groups[dateKey] = []
      }

      groups[dateKey].push(sale)
    })

    return Object.entries(groups)
      .sort(([dateA], [dateB]) => {
        if (dateA === 'unknown') return 1
        if (dateB === 'unknown') return -1

        return (
          new Date(dateB) -
          new Date(dateA)
        )
      })
      .map(([dateKey, dateSales]) => ({
        dateKey,
        title:
          dateKey === 'unknown'
            ? 'Unknown Date'
            : formatDateHeading(
                dateSales[0].date
              ),
        sales: dateSales,
      }))
  }

  const previousSalesGroups =
    groupSalesByDate(sales)

  const customerSalesGroups =
    groupSalesByDate(customerSales)

  return (
    <div className="page">

      <h1>Sales</h1>

      <form
        className="sale-form"
        onSubmit={handleSubmit}
      >

        <label>
          Product

          <select
            name="productId"
            value={form.productId}
            onChange={handleProductChange}
            required
          >
            <option value="">
              Select Product
            </option>

            {products.map((product) => (
              <option
                key={product.id}
                value={product.id}
              >
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
            <option value="">
              Walk-in Customer
            </option>

            {customers.map((customer) => (
              <option
                key={customer.id}
                value={customer.id}
              >
                {customer.name}
              </option>
            ))}
          </select>
        </label>

        {customerBalance && (
          <div className="customer-balance-box">

            <strong>
              Current Customer Balance
            </strong>

            <div>
              Outstanding:
              ₹
              {Number(
                customerBalance.outstandingBalance ||
                0
              ).toFixed(2)}
            </div>

            {Number(
              customerBalance.advanceAmount ||
              0
            ) > 0 && (
              <div>
                Advance:
                ₹
                {Number(
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
            <option value="">
              Select Payment Method
            </option>

            <option value="CASH">
              Cash
            </option>

            <option value="UPI">
              UPI
            </option>

            <option value="CARD">
              Card
            </option>

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

      {/* Previous Sales */}

      <button
        type="button"
        className="sales-history-toggle-button"
        onClick={() =>
          setShowPreviousSales(
            !showPreviousSales
          )
        }
      >
        {showPreviousSales
          ? 'Hide Previous Sales'
          : 'Previous Sales'}
      </button>

      {showPreviousSales && (
        <div>

          {sales.length > 0 ? (

            previousSalesGroups.map(
              (group) => (
                <div
                  key={group.dateKey}
                  className="sales-date-group"
                >

                  <h2>
                    {group.title}
                  </h2>

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
                        <th>Time</th>
                      </tr>
                    </thead>

                    <tbody>

                      {group.sales.map(
                        (sale) => {

                          const product =
                            products.find(
                              (item) =>
                                item.id ===
                                sale.productId
                            )

                          return (
                            <tr
                              key={sale.id}
                            >

                              <td>
                                {sale.id}
                              </td>

                              <td>
                                {product?.name ||
                                  '-'}
                              </td>

                              <td>
                                {sale.customerName}
                              </td>

                              <td>
                                {sale.quantity}
                              </td>

                              <td>
                                ₹
                                {Number(
                                  sale.sellingPrice
                                ).toFixed(2)}
                              </td>

                              <td>
                                {Number(
                                  sale.discountPercentage ||
                                    0
                                ).toFixed(2)}
                                %
                              </td>

                              <td>
                                {Number(
                                  sale.cgstPercentage ||
                                    0
                                ).toFixed(2)}
                                %
                              </td>

                              <td>
                                {Number(
                                  sale.sgstPercentage ||
                                    0
                                ).toFixed(2)}
                                %
                              </td>

                              <td>
                                {sale.paymentMethod ||
                                  '-'}
                              </td>

                              <td>
                                ₹
                                {Number(
                                  sale.paidAmount ||
                                    0
                                ).toFixed(2)}
                              </td>

                              <td>
                                ₹
                                {Number(
                                  sale.pendingAmount ||
                                    0
                                ).toFixed(2)}
                              </td>

                              <td>
                                ₹
                                {Number(
                                  sale.grandTotal ||
                                    0
                                ).toFixed(2)}
                              </td>

                              <td>
                                {formatTime(
                                  sale.date
                                )}
                              </td>

                            </tr>
                          )
                        }
                      )}

                    </tbody>

                  </table>

                </div>
              )
            )

          ) : (

            <p>
              No previous sales found.
            </p>

          )}

        </div>
      )}

      {/* Customer Sales History */}

      <div
        className="customer-history-section"
        style={{
          marginTop: '30px',
        }}
      >

        <button
          type="button"
          className="sales-history-toggle-button"
          onClick={() =>
            setShowCustomerHistory(
              !showCustomerHistory
            )
          }
        >
          {showCustomerHistory
            ? 'Hide Customer Sales History'
            : 'Customer Sales History'}
        </button>

        {showCustomerHistory && (

          <div
            style={{
              marginTop: '20px',
            }}
          >

            <label>
              Select Customer

              <select
                value={
                  selectedHistoryCustomer
                }
                onChange={
                  handleHistoryCustomerChange
                }
              >
                <option value="">
                  Select Customer
                </option>

                {customers.map(
                  (customer) => (
                    <option
                      key={customer.id}
                      value={customer.id}
                    >
                      {customer.name}
                    </option>
                  )
                )}

              </select>

            </label>

            {customerSummary && (
              <div
                className="customer-balance-box"
                style={{
                  marginTop: '20px',
                }}
              >

                <strong>
                  Customer Sales Summary
                </strong>

                <div>
                  Total Orders:
                  {' '}
                  {customerSummary.totalOrders}
                </div>

                <div>
                  Total Quantity:
                  {' '}
                  {customerSummary.totalQuantity}
                </div>

                <div>
                  Total Sales:
                  ₹
                  {Number(
                    customerSummary.totalAmount ||
                      0
                  ).toFixed(2)}
                </div>

              </div>
            )}

            {selectedHistoryCustomer && (
              <div
                style={{
                  marginTop: '25px',
                }}
              >

                {customerSales.length > 0 ? (

                  customerSalesGroups.map(
                    (group) => (
                      <div
                        key={group.dateKey}
                        className="sales-date-group"
                      >

                        <h2>
                          {group.title}
                        </h2>

                        <table className="data-table">

                          <thead>
                            <tr>
                              <th>ID</th>
                              <th>Product</th>
                              <th>Quantity</th>
                              <th>Selling Price</th>
                              <th>Discount %</th>
                              <th>CGST %</th>
                              <th>SGST %</th>
                              <th>Payment Method</th>
                              <th>Paid Amount</th>
                              <th>Pending Amount</th>
                              <th>Grand Total</th>
                              <th>Time</th>
                            </tr>
                          </thead>

                          <tbody>

                            {group.sales.map(
                              (sale) => {

                                const product =
                                  products.find(
                                    (item) =>
                                      item.id ===
                                      sale.productId
                                  )

                                return (
                                  <tr
                                    key={sale.id}
                                  >

                                    <td>
                                      {sale.id}
                                    </td>

                                    <td>
                                      {product?.name ||
                                        '-'}
                                    </td>

                                    <td>
                                      {sale.quantity}
                                    </td>

                                    <td>
                                      ₹
                                      {Number(
                                        sale.sellingPrice
                                      ).toFixed(2)}
                                    </td>

                                    <td>
                                      {Number(
                                        sale.discountPercentage ||
                                          0
                                      ).toFixed(2)}
                                      %
                                    </td>

                                    <td>
                                      {Number(
                                        sale.cgstPercentage ||
                                          0
                                      ).toFixed(2)}
                                      %
                                    </td>

                                    <td>
                                      {Number(
                                        sale.sgstPercentage ||
                                          0
                                      ).toFixed(2)}
                                      %
                                    </td>

                                    <td>
                                      {sale.paymentMethod ||
                                        '-'}
                                    </td>

                                    <td>
                                      ₹
                                      {Number(
                                        sale.paidAmount ||
                                          0
                                      ).toFixed(2)}
                                    </td>

                                    <td>
                                      ₹
                                      {Number(
                                        sale.pendingAmount ||
                                          0
                                      ).toFixed(2)}
                                    </td>

                                    <td>
                                      ₹
                                      {Number(
                                        sale.grandTotal ||
                                          0
                                      ).toFixed(2)}
                                    </td>

                                    <td>
                                      {formatTime(
                                        sale.date
                                      )}
                                    </td>

                                  </tr>
                                )
                              }
                            )}

                          </tbody>

                        </table>

                      </div>
                    )
                  )

                ) : (

                  <p>
                    No sales found for this customer.
                  </p>

                )}

              </div>
            )}

          </div>

        )}

      </div>

    </div>
  )
}

export default Sales