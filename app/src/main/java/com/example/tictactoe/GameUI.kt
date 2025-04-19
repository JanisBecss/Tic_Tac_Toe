package com.example.tictactoe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

// uzvaras veidi
enum class Win {
    PLAYER1,
    PLAYER2,
    AI,
    DRAW
}

@Composable
fun TTTGameScreen(
    player1Name: String,
    player2Name: String,
    vsComputer: Boolean,
    onBackToMenu: () -> Unit
) {
    val playerTurn = remember { mutableStateOf(true) }
    val moves = remember { mutableStateListOf<Boolean?>(null, null, null, null, null, null, null, null, null) }
    val win = remember { mutableStateOf<Win?>(null) }

    // funkcija, kas tiek izsaukta, kad tiek uzspiests uz laukuma
    val onTap: (Offset) -> Unit = {
        if (win.value == null && (!vsComputer || playerTurn.value)) {
            val x = (it.x / 333).toInt() // kordinātes X, kas atbilst laukuma vietai
            val y = (it.y / 333).toInt() // Kordinātes Y, kas atbilst laukuma vietai
            val pos = y * 3 + x // pozīcija uz spēles laukuma
            if (pos in 0..8 && moves[pos] == null) { // ja šī pozīcija ir brīva
                moves[pos] = playerTurn.value
                win.value = checkEndGame(moves)// pārbauda vai spēle ir beigusies
                if (win.value == null) playerTurn.value = !playerTurn.value // maina spēlētāju, ja spēle turpinās
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Tic Tac Toe", // spēles nosaukums
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp)
        )

        Header(playerTurn.value, player1Name, player2Name) // spēlētāju nosaukumi un kārtas indikators


        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(16.dp)
                .weight(1f)
        ) {
            Board(moves, onTap) // veido spēles laukumu
        }


        val coroutineScope = rememberCoroutineScope()
        if (vsComputer && !playerTurn.value && win.value == null) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp)) // datora gājiena indikators
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    delay(1100L) // datora gājiens pēc 1.1 sek
                    while (true) {
                        val i = Random.nextInt(9) // nejauša pozīcija datora gājienam
                        if (moves[i] == null) {
                            moves[i] = false // dators spēlē "O"
                            playerTurn.value = true // mainām kārtu
                            win.value = checkEndGame(moves)// pārbaudām vai spēle ir beigusies
                            break
                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (win.value != null) { // ja ir uzvarētājs vai neizšķirts
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color(0xFFE0F7FA))
                        .padding(16.dp)
                ) {
                    val winnerText = when (win.value) {
                        Win.PLAYER1 -> "$player1Name uzvarēja!"
                        Win.PLAYER2, Win.AI -> "$player2Name uzvarēja!"
                        Win.DRAW -> "Neizšķirts!"
                        null -> ""
                    }

                    Text(
                        text = winnerText,
                        fontSize = 24.sp,
                        color = Color.Blue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                for (i in 0..8) moves[i] = null // atjauno spēli
                                win.value = null
                                playerTurn.value = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Atkārtot spēli")
                        }

                        Button(
                            onClick = onBackToMenu,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("Atgriezties sākumā")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header(playerTurn: Boolean, player1Name: String, player2Name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val color1 = if (playerTurn) Color.Blue else Color.LightGray // krāsa pirmajam spēlētājam
        val color2 = if (!playerTurn) Color.Red else Color.LightGray // Krāsa otrajam spēlētājam

        Box(
            modifier = Modifier
                .width(120.dp)
                .background(color1)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(player1Name) // pirmais spēlētājs
        }

        Spacer(modifier = Modifier.width(50.dp))

        Box(
            modifier = Modifier
                .width(120.dp)
                .background(color2)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(player2Name) // otrais spēlētājs
        }
    }
}

@Composable
fun Board(moves: List<Boolean?>, onTap: (Offset) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .background(Color.LightGray)
            .pointerInput(Unit) { detectTapGestures(onTap = onTap) }
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.height(2.dp).fillMaxWidth().background(Color.Black)) {}
            Row(modifier = Modifier.height(2.dp).fillMaxWidth().background(Color.Black)) {}
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black)) {}
            Column(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black)) {}
        }


        Column(modifier = Modifier.fillMaxSize()) {
            for (i in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (j in 0..2) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            getComposableFromMove(moves[i * 3 + j]) // "X" vai "O"
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getComposableFromMove(move: Boolean?) {
    when (move) {
        true -> Image(
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = "X",
            modifier = Modifier.fillMaxSize().padding(16.dp),
            colorFilter = ColorFilter.tint(Color.Blue) // "X" attēls
        )
        false -> Image(
            painter = painterResource(id = R.drawable.ic_o),
            contentDescription = "O",
            modifier = Modifier.fillMaxSize().padding(16.dp),
            colorFilter = ColorFilter.tint(Color.Red) // "O" attēls
        )
        null -> Spacer(modifier = Modifier.fillMaxSize()) // brīva vieta
    }
}

// funkcija, kas pārbauda, vai spēle ir beigusies un atgriež uzvarētāju
fun checkEndGame(m: List<Boolean?>): Win? {
    val lines = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    for (line in lines) {
        val (a, b, c) = line
        if (m[a] != null && m[a] == m[b] && m[b] == m[c]) {
            return if (m[a] == true) Win.PLAYER1 else Win.PLAYER2
        }
    }

    return if (m.all { it != null }) Win.DRAW else null // ja visi lauki ir aizpildīti, bet nav uzvarētāja, tad neizšķirts
}