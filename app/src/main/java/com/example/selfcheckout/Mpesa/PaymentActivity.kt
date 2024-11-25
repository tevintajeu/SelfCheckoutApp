package com.example.selfcheckout.Mpesa

import android.app.AlertDialog
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.selfcheckout.BuildConfig
import com.example.selfcheckout.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var progressDialog: AlertDialog // Declare the progress dialog for UI feedback
    private lateinit var phoneNumber: String // Store the phone number entered by the user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        Log.d("PaymentActivity", "onCreate: PaymentActivity started.")

        // Initialize the progress dialog
        progressDialog = AlertDialog.Builder(this)
            .setTitle("Processing Payment")
            .setMessage("Please wait...")
            .setCancelable(false)
            .create()

        Log.d("PaymentActivity", "Progress dialog initialized.")

        // Reference the button and input field from the layout
        val payNowButton = findViewById<Button>(R.id.pay_button)
        val phoneNumberEditText = findViewById<EditText>(R.id.phone_number)

        // Set the click listener for the Pay Now button
        payNowButton.setOnClickListener {
            Log.d("PaymentActivity", "Pay Now button clicked.")

            // Get the phone number entered by the user
            phoneNumber = phoneNumberEditText.text.toString().trim()
            Log.d("PaymentActivity", "Phone number entered: $phoneNumber")

            if (phoneNumber.isNotEmpty()) {
                Log.d("PaymentActivity", "Valid phone number provided.")
                progressDialog.show() // Show the progress dialog
                fetchAccessToken() // Fetch access token and proceed
            } else {
                Log.w("PaymentActivity", "Empty phone number entered.")
                showError("Please enter a valid phone number.")
            }
        }
    }

    private fun fetchAccessToken() {
        Log.d("PaymentActivity", "Fetching access token.")

        // Prepare Basic Auth Header
        val consumerKey = BuildConfig.CONSUMER_KEY
        val consumerSecret = BuildConfig.CONSUMER_SECRET
        val credentials = "$consumerKey:$consumerSecret"
        val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        // Create API instance and call generateToken
        val api = MpesaService.retrofit.create(MpesaApi::class.java)
        api.generateToken(basicAuth).enqueue(object : Callback<AccessTokenResponse> {
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val accessToken = response.body()?.access_token
                    Log.d("MpesaAPI", "Access Token: $accessToken")

                    if (accessToken != null) {
                        // Trigger STK Push after successfully fetching token
                        initiateSTKPush(accessToken)
                    } else {
                        Log.e("MpesaAPI", "Access token is null.")
                        showError("Failed to retrieve access token.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MpesaAPI", "Failed to retrieve access token: ${response.message()} - $errorBody")
                    showError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("MpesaAPI", "Error fetching access token.", t)
                showError("Failure: ${t.message}")
            }
        })
    }

    private fun initiateSTKPush(accessToken: String) {
        Log.d("PaymentActivity", "Initiating STK Push.")

        val shortCode = "174379" // Your Mpesa Short Code
        val passkey = BuildConfig.PASSKEY

        // Generate Password and Timestamp
        val (password, timestamp) = generatePassword(shortCode, passkey)
        Log.d("PaymentActivity", "Generated password: $password and timestamp: $timestamp")

        // Create STK Push Request
        val stkPushRequest = StkPushRequest(
            BusinessShortCode = shortCode,
            Password = password,
            Timestamp = timestamp,
            TransactionType = "CustomerPayBillOnline",
            Amount = "1", // Test amount
            PartyA = phoneNumber,
            PartyB = shortCode,
            PhoneNumber = phoneNumber,
            CallBackURL = "https://webhook.site/your-unique-id", // Replace with actual callback URL
            AccountReference = "SelfCheckout",
            TransactionDesc = "Payment for goods"
        )
        Log.d("PaymentActivity", "CallBackURL being sent: ${stkPushRequest.CallBackURL}")

        Log.d("PaymentActivity", "STK Push Request: $stkPushRequest")

        // Make STK Push API Call
        val api = MpesaService.retrofit.create(MpesaApi::class.java)
        progressDialog.setMessage("Sending payment request...")
        progressDialog.show()

        api.initiateSTKPush("Bearer $accessToken", stkPushRequest).enqueue(object : Callback<StkPushResponse> {
            override fun onResponse(call: Call<StkPushResponse>, response: Response<StkPushResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val stkResponse = response.body()
                    Log.d("PaymentActivity", "CallBackURL being sent: ${stkPushRequest.CallBackURL}")
                    Log.d("MpesaAPI", "STK Push Response: ${stkResponse?.ResponseDescription}")
                    Toast.makeText(this@PaymentActivity, "Payment initiated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MpesaAPI", "STK Push failed: $errorBody")
                    showError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StkPushResponse>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("MpesaAPI", "Error during STK Push.", t)
                showError("Failure: ${t.message}")
            }
        })
    }

    private fun showError(message: String) {
        Log.e("PaymentActivity", "Error: $message")
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun generatePassword(shortCode: String, passkey: String): Pair<String, String> {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val passwordString = "$shortCode$passkey$timestamp"
        val password = Base64.encodeToString(passwordString.toByteArray(), Base64.NO_WRAP)
        return Pair(password, timestamp)
    }
}

