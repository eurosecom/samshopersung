package com.eusecom.samshopersung

import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL
 */

class OrderFragment : OrderBaseFragment() {

    override fun bindOrders() {
        closedorders = "0"
    }

    override fun getTodoDialog(invoice: Invoice) {

        if(invoice.fak.equals("0")){

            val inflater = LayoutInflater.from(activity)
            val textenter = inflater.inflate(R.layout.invoice_edit_dialog, null)

            val valuex = textenter.findViewById<View>(R.id.valuex) as TextView
            valuex.text = invoice.hod

            val builder = AlertDialog.Builder(activity)
            builder.setView(textenter).setTitle(getString(R.string.order) + " " + invoice.dok)

            builder.setItems(arrayOf<CharSequence>(getString(R.string.pdfdoc), getString(R.string.orderdetail)
                    , getString(R.string.deletewholedoc), getString(R.string.getinvoice), getString(R.string.movetoekassa))
            ) { dialog, which ->
                // The 'which' argument contains the index position
                // of the selected item
                when (which) {
                    0 -> {
                        navigateToGetPdf(invoice)
                    }
                    1 -> {
                        navigateOrderDetail(invoice)
                    }
                    2 -> {
                        showDeleteOrderDialog(invoice)
                    }
                    3 -> {
                        showGetInvoiceDialog(invoice)
                    }
                    4 -> {
                        showMoveToEkassaDialog(invoice)
                    }


                }
            }
            val dialog = builder.create()
            builder.show()
        }

    }


}
