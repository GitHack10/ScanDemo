package com.example.scandemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cards.pay.paycardsrecognizer.sdk.Card
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SCAN_CARD) {
            when (resultCode) {
                Activity.RESULT_OK -> {

                    val card: Card? = data?.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD)
                    val cardData = """
                    Card number: ${card?.cardNumber}
                    Card holder: ${card?.cardHolderName}
                    Card expiration date: ${card?.expirationDate}
                    """.trimIndent()

                    card?.let {
                        setScannedData(
                            cardNumber = card.cardNumber,
                            cardHolderName = card.cardHolderName,
                            expirationDate = card.expirationDate
                        )
                    }

                    Log.i(TAG, "Card info: $cardData")
                }
                Activity.RESULT_CANCELED -> {
                    Log.i(TAG, "Scan canceled")
                }
                else -> {
                    Log.i(TAG, "Scan failed")
                }
            }
        }
    }

    private fun init() {

        inputCardHolder.filters = arrayOf(InputFilter.AllCaps())

        scannerImageView.setOnClickListener {
            scanCard()
        }

        goToMapButton.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }

    private fun scanCard() {
        val intent: Intent = ScanCardIntent.Builder(this).build()
        startActivityForResult(intent, REQUEST_CODE_SCAN_CARD)
    }

    private fun setScannedData(cardNumber: String?, cardHolderName: String?, expirationDate: String?) {
        inputCardNumber.setText(cardNumber)
        inputCardHolder.setText(cardHolderName)
        inputCardExpirationMonth.setText(expirationDate?.split("/")?.first())
        inputCardExpirationYear.setText(expirationDate?.split("/")?.last())
    }

    companion object {
        private const val REQUEST_CODE_SCAN_CARD = 1
        private const val TAG = "SCAN_INFO"
    }
}
