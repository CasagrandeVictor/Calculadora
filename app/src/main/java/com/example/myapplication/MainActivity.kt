package com.example.myapplication

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculadoraScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculadoraScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    // Variáveis de Estado (Lógica)
    var visor by remember { mutableStateOf("0") }
    var operandoAnterior by remember { mutableStateOf("") }
    var operacaoAtual by remember { mutableStateOf("") }
    var limparVisorNoProximoNumero by remember { mutableStateOf(false) }

    fun aoClicar(botao: String) {
        tocarSomClique(context)

        when (botao) {
            "C" -> {
                visor = "0"
                operandoAnterior = ""
                operacaoAtual = ""
            }
            "+", "-", "*", "/" -> {
                operandoAnterior = visor
                operacaoAtual = botao
                limparVisorNoProximoNumero = true
            }
            "=" -> {
                if (operandoAnterior.isNotEmpty() && operacaoAtual.isNotEmpty()) {
                    val num1 = operandoAnterior.toDoubleOrNull() ?: 0.0
                    val num2 = visor.toDoubleOrNull() ?: 0.0

                    val resultado = when (operacaoAtual) {
                        "+" -> num1 + num2
                        "-" -> num1 - num2
                        "*" -> num1 * num2
                        "/" -> if (num2 != 0.0) num1 / num2 else 0.0
                        else -> 0.0
                    }

                    visor = if (resultado % 1 == 0.0) {
                        resultado.toLong().toString()
                    } else {
                        resultado.toString()
                    }

                    operandoAnterior = ""
                    operacaoAtual = ""
                    limparVisorNoProximoNumero = true
                }
            }
            else -> {
                if (limparVisorNoProximoNumero || visor == "0") {
                    visor = botao
                    limparVisorNoProximoNumero = false
                } else {
                    visor += botao
                }
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = visor,
            fontSize = 80.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp),
            maxLines = 1
        )

        val botoes = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", "C", "=", "+")
        )

        botoes.forEach { linha ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                linha.forEach { simbolo ->
                    BotaoCalculadora(
                        simbolo = simbolo,
                        modifier = Modifier.weight(1f),
                        onClick = { aoClicar(simbolo) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BotaoCalculadora(simbolo: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(85.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50)
        )
    ) {
        Text(
            text = simbolo,
            fontSize = 32.sp,
            color = Color.White
        )
    }
}

//SOM DO CLIQUE
fun tocarSomClique(context: Context) {
    try {
        val mp = MediaPlayer.create(context, R.raw.clique)
        mp?.start()
        mp?.setOnCompletionListener { it.release() }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}