package com.example.gigi_ibuhamil.pages.beratbadanscreen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gigi_ibuhamil.ui.DaftarColor
import com.example.gigi_ibuhamil.ui.gradbg
import com.example.gigi_ibuhamil.util.AmountOrMessageVisualTransformation
import com.example.gigi_ibuhamil.util.SavedPreference
import com.example.gigi_ibuhamil.util.Screen
import java.math.RoundingMode

var result = 0.0
var bmi = "default"
@ExperimentalFoundationApi
@Composable
fun BeratBadanScreen(navController: NavController) = Box(
    modifier = Modifier
        .background(gradbg)
        .fillMaxSize()
) {
    Column {
        BeratBadanTitle(navController)
        Isi(navController)
    }
}

@Composable
fun BeratBadanTitle(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ) {
        Row(Modifier.padding(start = 10.dp)) {
            Text(
                text = "Perhitungan BMI",
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .weight(3f)
            )
        }
    }
}

@Composable
fun Isi(navController: NavController) {
    Column() {
        FormItems(navController)
    }
}
@Composable
fun FormItems(navController: NavController) {
    val context = LocalContext.current
    var TB by remember { mutableStateOf(TextFieldValue()) }
    var BB by remember { mutableStateOf(TextFieldValue()) }
    val BBStr : String = BB.text.toString()
    val TBStr : String = TB.text.toString()
    var BMI  = 0.0
    var dialogState by remember { mutableStateOf(false) }
    val mMaxLength1 = 3
    val mMaxLength2 = 4
    BoxWithConstraints(
        modifier = Modifier
            .padding(7.5.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DaftarColor)
        ){
            Column() {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(15.dp)
                        .verticalScroll(rememberScrollState())
                        .weight(1f, false)
                ) {
                    OutlinedTextField(
                        value = BB,
                        label = { Text(text = "Berat badan setelah hamil (Kg)", color = Color.White) },
                        onValueChange = {
                            if (it.text.length <= mMaxLength1) BB = it
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,),
                    )
                    OutlinedTextField(
                        value = TB,
                        label = { Text(text = "berapa tinggi badan setelah hamil (meter)", color = Color.White) },
                        singleLine = true,
                        visualTransformation = AmountOrMessageVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        onValueChange = {
                            if (it.text.length <= mMaxLength2) TB = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White),
                    )
                    Button(onClick = {
                        if(BB.text.isEmpty() || TB.text.isEmpty()){
                            Toast.makeText(context,
                                "Harap isi kedua isian dengan benar",
                                Toast.LENGTH_SHORT).show()
                        }else{
                            BMI = BBStr.toDouble() / (TBStr.toDouble() * TBStr.toDouble())

                            dialogState = true
                            ResultReturn(BMI)
                            if (result <= 18.4) {
                                BmiReturn("Underweight")
                            }else if(result >= 18.5 && result <= 25.0){
                                BmiReturn("Normal")
                            }else if(result >= 25.1 && result <= 27.0){
                                BmiReturn("Overweight")
                            }else if(result >= 27.1){
                                BmiReturn("Obesitas")
                            }
                        }
                        }) {
                        Text(text = "Hitung BMI", color = Color.White, fontSize = 15.sp)
                    }
                    if(dialogState == true){
                        AlertDialog(
                            modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                            onDismissRequest = { dialogState = false},
                            title = {
                                Text(
                                    fontSize = MaterialTheme.typography.h5.fontSize,
                                    textAlign = TextAlign.Center,
                                    text = "Hasil Perhitungan BMI"
                                )
                            },
                            text = {
                                if (result <= 18.4) {
                                    Text(
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        text = "${
                                            result.toBigDecimal().setScale(2, RoundingMode.UP)
                                                .toString()
                                        } \nUnderweight (Berat badan anda termasuk kedalam kategori kekurangan berat badan)"
                                    )
                                }else if(result >= 18.5 && result <= 25.0){
                                    Text(
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        text = "${
                                            result.toBigDecimal().setScale(2, RoundingMode.UP)
                                                .toString()
                                        } \nNormal (Berat badan anda termasuk kedalam kategori normal)"
                                    )
                                }else if(result >= 25.1 && result <= 27.0){
                                    Text(
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        text = "${
                                            result.toBigDecimal().setScale(2, RoundingMode.UP)
                                                .toString()
                                        } \nOverweight (Berat badan anda termasuk kedalam kategori kelebihan berat badan ringan)"
                                    )
                                }else if(result >= 27.1){
                                    Text(
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        text = "${
                                            result.toBigDecimal().setScale(2, RoundingMode.UP)
                                                .toString()
                                        } \nObesitas (Berat badan anda termasuk kedalam kategori kelebihan berat badan ringan)"
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    onClick = {
                                        SavedPreference.setBMI(context, bmi)
                                        dialogState = false
                                        ResultReturn(0.0)
                                        BmiReturn("Default")
                                        navController.navigate(Screen.BeratBadanScreen.route){popUpTo(0)}
                                    }) {
                                    Text(fontSize = 15.sp,text = "Mengulang proses perhitungan" )
                                }
                            },
                            dismissButton = {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    onClick = {
                                        SavedPreference.setBMI(context, bmi)
                                        dialogState = false
                                        ResultReturn(0.0)
                                        BmiReturn("Default")
                                        navController.navigate(Screen.PolaMakanScreen.route){popUpTo(0)}
                                    }) {
                                    Text(fontSize = 15.sp,text = "Lanjutkan Assessment")
                                }
                            }
                        )
                    }
                }

            }
        }
    }
}



fun ResultReturn(item: Double): Double {
    result = item
    return result
}
fun BmiReturn(item: String): String {
    bmi = item
    return bmi
}