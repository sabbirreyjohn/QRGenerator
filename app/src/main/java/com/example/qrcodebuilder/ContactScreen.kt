package com.example.qrcodebuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.regex.Pattern

@Composable
fun ContactScreen() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var organization by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var qrCodeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    
    // Form validation errors
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var websiteError by remember { mutableStateOf<String?>(null) }
    
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    // Auto-scroll to QR code when generation completes
    LaunchedEffect(qrCodeBitmap) {
        if (qrCodeBitmap != null) {
            coroutineScope.launch {
                listState.animateScrollToItem(3)
            }
        }
    }
    
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        
        item {
            // Enhanced Form Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Contact Information",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Required Fields Section
                    Text(
                        text = "Required Fields",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PremiumTextField(
                            value = firstName,
                            onValueChange = { 
                                firstName = it
                                firstNameError = if (it.isBlank()) "Required" else null
                            },
                            label = stringResource(R.string.hint_contact_first_name),
                            icon = Icons.Default.Person,
                            keyboardType = KeyboardType.Text,
                            placeholder = "John",
                            error = firstNameError,
                            modifier = Modifier.weight(1f)
                        )
                        
                        PremiumTextField(
                            value = lastName,
                            onValueChange = { 
                                lastName = it
                                lastNameError = if (it.isBlank()) "Required" else null
                            },
                            label = stringResource(R.string.hint_contact_last_name),
                            icon = Icons.Default.Person,
                            keyboardType = KeyboardType.Text,
                            placeholder = "Doe",
                            error = lastNameError,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    PremiumTextField(
                        value = phoneNumber,
                        onValueChange = { 
                            phoneNumber = it
                            phoneError = if (it.isBlank()) "Required" else validatePhone(it)
                        },
                        label = stringResource(R.string.hint_contact_phone),
                        icon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone,
                        placeholder = "+1 (555) 123-4567",
                        error = phoneError
                    )
                    
                    // Optional Fields Section
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Optional Fields",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    PremiumTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            emailError = if (it.isNotBlank()) validateEmail(it) else null
                        },
                        label = stringResource(R.string.hint_contact_email),
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        placeholder = "john.doe@example.com",
                        error = emailError
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PremiumTextField(
                            value = birthday,
                            onValueChange = { birthday = it },
                            label = stringResource(R.string.hint_contact_birthday),
                            icon = Icons.Default.Cake,
                            keyboardType = KeyboardType.Number,
                            placeholder = "YYYY-MM-DD",
                            modifier = Modifier.weight(1f)
                        )
                        
                        PremiumTextField(
                            value = website,
                            onValueChange = { 
                                website = it
                                websiteError = if (it.isNotBlank()) validateWebsite(it) else null
                            },
                            label = stringResource(R.string.hint_contact_website),
                            icon = Icons.Default.Language,
                            keyboardType = KeyboardType.Uri,
                            placeholder = "website.com",
                            error = websiteError,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PremiumTextField(
                            value = organization,
                            onValueChange = { organization = it },
                            label = stringResource(R.string.hint_contact_organization),
                            icon = Icons.Default.Business,
                            keyboardType = KeyboardType.Text,
                            placeholder = "Company Inc.",
                            modifier = Modifier.weight(1f)
                        )
                        
                        PremiumTextField(
                            value = jobTitle,
                            onValueChange = { jobTitle = it },
                            label = stringResource(R.string.hint_contact_job_title),
                            icon = Icons.Default.Work,
                            keyboardType = KeyboardType.Text,
                            placeholder = "Software Engineer",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Generate Button
                    Spacer(modifier = Modifier.height(8.dp))
                    FilledTonalButton(
                        onClick = {
                            // Validate required fields
                            firstNameError = if (firstName.isBlank()) "Required" else null
                            lastNameError = if (lastName.isBlank()) "Required" else null
                            phoneError = if (phoneNumber.isBlank()) "Required" else validatePhone(phoneNumber)
                            
                            if (hasValidRequiredData(firstName, lastName, phoneNumber, firstNameError, lastNameError, phoneError)) {
                                isGenerating = true
                                val vCardData = QRGenerator.generateContactVCard(
                                    firstName = firstName,
                                    lastName = lastName,
                                    phone = phoneNumber,
                                    email = email,
                                    birthday = birthday,
                                    website = website,
                                    organization = organization,
                                    jobTitle = jobTitle
                                )
                                qrCodeBitmap = QRGenerator.generateQRCode(vCardData)
                                isGenerating = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isGenerating,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.generate_qr),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
        
        item {
            // QR Code Display with Actions
            AnimatedVisibility(
                visible = qrCodeBitmap != null,
                enter = fadeIn() + scaleIn()
            ) {
                qrCodeBitmap?.let { bitmap ->
                    val vCardData = QRGenerator.generateContactVCard(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phoneNumber,
                        email = email,
                        birthday = birthday,
                        website = website,
                        organization = organization,
                        jobTitle = jobTitle
                    )
                    QRActionsCard(
                        qrBitmap = bitmap,
                        qrContent = vCardData,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType,
    placeholder: String,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (error == null && value.isNotBlank()) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (error == null) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error,
                focusedLabelColor = if (error == null) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                )
            ),
            isError = error != null
        )
        
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun ContactPreview(
    name: String,
    phone: String,
    email: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Contact Preview:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            
            if (name.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            if (phone.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            if (email.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

private fun validateEmail(email: String): String? {
    if (email.isBlank()) return null
    
    val emailPattern = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    )
    
    return if (!emailPattern.matcher(email).matches()) {
        "Please enter a valid email address"
    } else null
}

private fun validatePhone(phone: String): String? {
    if (phone.isBlank()) return null
    
    val phonePattern = Pattern.compile(
        "^[+]?[0-9\\s\\-()]{10,}$"
    )
    
    return if (!phonePattern.matcher(phone).matches()) {
        "Please enter a valid phone number"
    } else null
}

private fun hasValidRequiredData(
    firstName: String,
    lastName: String,
    phone: String,
    firstNameError: String?,
    lastNameError: String?,
    phoneError: String?
): Boolean {
    val hasRequiredData = firstName.isNotBlank() && lastName.isNotBlank() && phone.isNotBlank()
    val hasNoErrors = firstNameError == null && lastNameError == null && phoneError == null
    return hasRequiredData && hasNoErrors
}

private fun validateWebsite(website: String): String? {
    if (website.isBlank()) return null
    
    val formattedUrl = if (!website.startsWith("http://") && !website.startsWith("https://")) {
        "https://$website"
    } else {
        website
    }
    
    val urlPattern = Pattern.compile(
        "^https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
    )
    
    return if (!urlPattern.matcher(formattedUrl).matches()) {
        "Please enter a valid website URL"
    } else null
}

