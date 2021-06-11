package com.nect.friendlymony.Model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataStripePayment {
    @SerializedName("payment_url")
    @Expose
    private String paymentUrl;

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
