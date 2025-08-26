package com.androidrey.qrcodebuilder

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Hashtable

object QRGenerator {
    
    fun generateQRCode(
        text: String,
        width: Int = 512,
        height: Int = 512
    ): ImageBitmap? {
        return try {
            val writer = QRCodeWriter()
            val hints = Hashtable<EncodeHintType, String>()
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(
                        x, 
                        y, 
                        if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                    )
                }
            }
            
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun generateContactVCard(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        birthday: String = "",
        website: String = "",
        organization: String = "",
        jobTitle: String = ""
    ): String {
        return buildString {
            append("BEGIN:VCARD\n")
            append("VERSION:3.0\n")
            
            // Full name
            val fullName = "$firstName $lastName".trim()
            if (fullName.isNotBlank()) {
                append("FN:$fullName\n")
                append("N:$lastName;$firstName;;;\n")
            }
            
            // Required fields
            if (phone.isNotBlank()) append("TEL:$phone\n")
            
            // Optional fields
            if (email.isNotBlank()) append("EMAIL:$email\n")
            if (birthday.isNotBlank()) append("BDAY:$birthday\n")
            if (website.isNotBlank()) append("URL:$website\n")
            if (organization.isNotBlank()) append("ORG:$organization\n")
            if (jobTitle.isNotBlank()) append("TITLE:$jobTitle\n")
            
            append("END:VCARD")
        }
    }
}