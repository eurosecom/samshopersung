package com.eusecom.samshopersung

import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.schedulers.Schedulers


/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL
 */

class OrderFragment : OrderBaseFragment() {

    override fun bindOrders() {

        mSubscription?.add(mViewModel.getMyOrdersFromSqlServer("0")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerOrders(it) })
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
                    , getString(R.string.deletewholedoc), getString(R.string.getinvoice), getString(R.string.getekassa))
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
                        showGetEkassaDialog(invoice)
                    }

                }
            }
            val dialog = builder.create()
            builder.show()
        }

    }


}
