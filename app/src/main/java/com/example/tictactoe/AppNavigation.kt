package com.example.tictactoe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// norāda dažādus ekrānus spēlē
enum class Screen {
    MENU, NAME_INPUT, GREETING, GAME
}

@Composable
fun TTTApp() {
    var screen by remember { mutableStateOf(Screen.MENU) }
    var vsComputer by remember { mutableStateOf(true) }
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }

    when (screen) {
        // galvenais izvēlnes ekrāns
        Screen.MENU -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Tic Tac Toe", // spēles nosaukums
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Text(
                    "Izvēlies spēles veidu", // teksts par spēles veida izvēli
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Button(
                    onClick = {
                        vsComputer = true
                        screen = Screen.NAME_INPUT // pāriet uz vārdu ievades ekrānu

                    },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Text("Viens spēlētājs")
                }

                Button( // poga, lai sāktu spēli diviem spēlētājiem
                    onClick = {
                        vsComputer = false
                        screen = Screen.NAME_INPUT // pāriet uz vārdu ievades ekrānu
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Text("Divi spēlētāji")
                }
            }
        }

        Screen.NAME_INPUT -> { // ekrāns, kur lietotāji ievada savus vārdus
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "ievadiet vārdu", // teksts, kas norāda spēlētājiem ievadīt savus vārdus
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = player1Name,
                    onValueChange = { player1Name = it },
                    label = { Text("1.spēlētāja vārds") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 8.dp)
                )

                if (!vsComputer) { // ja spēlē divi spēlētāji, tad pievieno arī 2. spēlētāja ievadi
                    OutlinedTextField(
                        value = player2Name,
                        onValueChange = { player2Name = it },
                        label = { Text("2.spēlētāja vārds") },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button( // sāk spēli, ja abi spēlētāji ir ievadījuši vārdus
                    onClick = {
                        if (player1Name.isNotBlank() && (vsComputer || player2Name.isNotBlank())) {
                            if (vsComputer) player2Name = "Dators"
                            screen = Screen.GREETING
                        }
                    },
                    enabled = player1Name.isNotBlank() && (vsComputer || player2Name.isNotBlank())
                ) {
                    Text("Sākt spēli") // teksts pogai
                }
            }
        }

        Screen.GREETING -> { // sveiciena ekrāns pirms spēles sākšanas
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Sveicināti!", // sveiciens spēlētājiem
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    "$player1Name vs $player2Name", // parāda spēlētāju vārdus
                    fontSize = 24.sp,
                    color = Color.Blue,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    "Sagatavojieties spēlei!",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button( // poga, lai sāktu pašu spēli
                    onClick = { screen = Screen.GAME },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Sākt spēli")
                }
            }
        }

        Screen.GAME -> { // spēles ekrāns
            TTTGameScreen(
                player1Name = player1Name,
                player2Name = player2Name,
                vsComputer = vsComputer,
                onBackToMenu = {
                    screen = Screen.MENU
                    player1Name = ""
                    player2Name = ""
                }
            )
        }
    }
}