/**
 * Copyright 2018 EuroSecon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * EXTERNAL COMPONENTS
 *
 * Apache PDFBox includes a number of components with separate copyright notices
 * and license terms. Your use of these components is subject to the terms and
 * conditions of the following licenses.
 *
 * Contributions made to the original PDFBox and FontBox projects:
 *
 * Copyright (c) 2002-2007, www.pdfbox.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of pdfbox; nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * Adobe Font Metrics (AFM) for PDF Core 14 Fonts
 *
 * This file and the 14 PostScript(R) AFM files it accompanies may be used,
 * copied, and distributed for any purpose and without charge, with or without
 * modification, provided that all copyright notices are retained; that the
 * AFM files are not distributed without this file; that all modifications
 * to this file or any of the AFM files are prominently noted in the modified
 * file(s); and that this paragraph is not modified. Adobe Systems has no
 * responsibility or obligation to support the use of the AFM files.
 *
 * CMaps for PDF Fonts (http://opensource.adobe.com/wiki/display/cmap/Downloads)
 *
 * Copyright 1990-2009 Adobe Systems Incorporated.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of Adobe Systems Incorporated nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 * PaDaF PDF/A preflight (http://sourceforge.net/projects/padaf)
 *
 * Copyright 2010 Atos Worldline SAS
 *
 * Licensed by Atos Worldline SAS under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Atos Worldline SAS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eusecom.samshopersung

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.eusecom.samshopersung.models.ZipObject
import com.eusecom.samshopersung.rxbus.RxBus
import com.eusecom.samshopersung.soap.soapekassa.EkassaResponseEnvelope
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL
 * template samfantozzi CashListKtFragment.kt
 */

class OrpFragment : BaseKtFragment() {

    private var mAdapter: OrpAdapter? = null
    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null
    private var mSubscription: CompositeSubscription? = null
    private var _disposables = CompositeDisposable()
    private var mDisposable: Disposable? = null

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    @Inject
    lateinit var  _rxBus: RxBus

    private var paydocx = "0";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        Log.d("Fromfrg Order ", mViewModel.toString())

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_orp, container, false)

        mRecycler = rootView.findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = OrpAdapter(_rxBus)
        mAdapter?.setAbsserver(emptyList())
        mManager = LinearLayoutManager(context)
        mManager?.setReverseLayout(true)
        mManager?.setStackFromEnd(true)
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)
        PDFBoxResourceLoader.init(context)

    }

    private fun bind() {

        _disposables = CompositeDisposable()

        val tapEventEmitter = _rxBus.asFlowable().publish()

        _disposables
                .add(tapEventEmitter.subscribe { event ->
                    if (event is OrpFragment.ClickFobEvent) {
                        //andrejko showNewCashDocDialog()

                    }
                    if (event is Invoice) {

                        Log.d("onShortClickListenerFrg", event.nai)
                        getTodoDialog(event)


                    }

                })

        _disposables
                .add(tapEventEmitter.publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { taps ->
                    ///_showTapCount(taps.size()); OK
                })

        _disposables.add(tapEventEmitter.connect())

        mViewModel.loadEkasaSettingsToMvvm()

        mSubscription = CompositeSubscription()

        showProgressBar()
        mSubscription?.add(mViewModel.getMyInvoicesFromSqlServer("2")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerInvoices(it) })

        mSubscription?.add(mViewModel.getObservableEkasaPdfZip()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setUriPdf(it) })

        mSubscription?.add(mViewModel.getObservableDeleteEkasaDoc()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerInvoices(it) })

        mSubscription?.add(mViewModel.getObservableRegisterReceiptEkassaResponseXml()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setEkassaRegisterReceiptResponseXml(it) })

        mSubscription?.add(mViewModel.getObservableEkasaDocPaid()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerInvoices(it) })

        ActivityCompat.invalidateOptionsMenu(activity)
    }

    private fun unBind() {

        mViewModel.clearObservableDeleteEkasaDoc()
        mViewModel.clearObservableRegisterReceiptEkassaResponseXml()
        mViewModel.clearObservableEkasaDocPaid()
        mViewModel.clearObservableEkasaPDFZip()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }
        hideProgressBar()

    }

    private fun setEkassaRegisterReceiptResponseXml(responseEnvelop: EkassaResponseEnvelope) {

        hideProgressBar()
        if (responseEnvelop != null) {
            if (responseEnvelop.body.getRegisterReceiptResponse != null) {
                val processdate = responseEnvelop.body.getRegisterReceiptResponse.getHeader.getProcessDate.toString()
                val dokid = responseEnvelop.body.getRegisterReceiptResponse.getReceiptData.getId.toString()
                Log.d("Reg. receipt result", processdate + " " + dokid)
                mViewModel.emitEkasaDocPaid(paydocx);
                Toast.makeText(activity, processdate + " " + dokid, Toast.LENGTH_LONG).show()
            }else{
                val errcode = responseEnvelop.body.getRegisterReceiptFault.getEkasaErrorCode
                Toast.makeText(activity, "Error code " + errcode, Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun setUriPdf(file: File) {

        file.absoluteFile.toString()
        Log.d("file.absoluteFile ", file.absoluteFile.toString())
        val external = FileProvider.getUriForFile(activity,
                BuildConfig.APPLICATION_ID + ".provider", file)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(external, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        mViewModel.clearObservableEkasaPDFZip()

    }


    private fun setServerInvoices(invoices: List<Invoice>) {

        //var items: List<ProductItemKt> = invoices.get(0).items
        //Log.d("items.get(0).nat", items.get(0).nat)
        //Log.d("items.get(1).nat", items.get(1).nat)
        mAdapter?.setAbsserver(invoices)
        hideProgressBar()
    }



    class ClickFobEvent


    override fun onDestroy() {
        super.onDestroy()
        unBind()

    }

    override fun onResume() {
        super.onResume()
        bind()
    }

    override fun onPause() {
        super.onPause()
        unBind()
    }

    fun getTodoDialog(invoice: Invoice) {

        val inflater = LayoutInflater.from(activity)
        val textenter = inflater.inflate(R.layout.invoice_edit_dialog, null)

        val valuex = textenter.findViewById<View>(R.id.valuex) as TextView
        valuex.text = invoice.hod

        val builder = AlertDialog.Builder(activity)
        builder.setView(textenter).setTitle(getString(R.string.orpdoc) + " " + invoice.dok)

        builder.setItems(arrayOf<CharSequence>(getString(R.string.getekassa), getString(R.string.pdfdoc), getString(R.string.deletewholedoc))
        ) { dialog, which ->
            // The 'which' argument contains the index position
            // of the selected item
            when (which) {
                0 -> {
                    showGetEkassaDialog(invoice)
                }
                1 -> {
                    navigateToGetEkasaPdf(invoice)
                }
                2 -> {
                    showDeleteInvoiceDialog(invoice)
                }

            }
        }
        val dialog = builder.create()
        builder.show()

    }

    fun navigateToGetEkasaPdf(order: Invoice){
        showProgressBar()
        //order.uce="31100"
        //order.dok="730001"
        //order.drh="54"
        mViewModel.emitEkasaPdfZip(order)
        //mViewModel.emitEkasaPdf(order)

    }

    fun showDeleteInvoiceDialog(order: Invoice) {

        if (order.ksy == "0") {
            alert("", getString(R.string.deletewholedoc) + " " + order.dok) {
                yesButton { navigateToDeleteInvoice(order) }
                noButton {}
            }.show()
        }else{
            alert("", order.dok + " " + getString(R.string.docregistered)) {
                okButton {  }
            }.show()
        }

    }

    fun navigateToDeleteInvoice(order: Invoice){
        showProgressBar()
        mViewModel.emitDeleteEkasaDoc(order);

    }

    fun showGetEkassaDialog(order: Invoice) {

        if (order.ksy == "0") {
            alert("", getString(R.string.getekassafrom) + " " + order.dok) {
                yesButton { navigateToGetEkassa(order) }
                noButton {}
            }.show()
        }else{
            alert("", order.dok + " " + getString(R.string.docregistered)) {
                okButton {  }
            }.show()
        }


    }

    fun navigateToGetEkassa(order: Invoice){

        showProgressBar()
        paydocx=order.dok
        mViewModel.emitRegisterReceiptEkassaXml(order)

    }



}
