package com.example.myapp006jetpackcomposeminitodo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ToDoScreen(modifier: Modifier = Modifier) {
    // Stavy pro základní pole
    var jmeno by remember { mutableStateOf("") }
    var prijmeni by remember { mutableStateOf("") }
    var vek by remember { mutableStateOf("") }
    var bydliste by remember { mutableStateOf("") }

    // Stavy pro dodatečná pole (úkol: vymyslete další)
    var povolani by remember { mutableStateOf("") } // Další textové pole
    var zkusenosti by remember { mutableStateOf("") } // Další číselné pole

    // Stav pro zobrazení výsledku
    var zobrazitVysledek by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registrační formulář",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Jméno
        OutlinedTextField(
            value = jmeno,
            onValueChange = { jmeno = it },
            label = { Text("Jméno") },
            modifier = Modifier.fillMaxWidth()
        )

        // Příjmení
        OutlinedTextField(
            value = prijmeni,
            onValueChange = { prijmeni = it },
            label = { Text("Příjmení") },
            modifier = Modifier.fillMaxWidth()
        )

        // Věk (Číselný vstup)
        OutlinedTextField(
            value = vek,
            onValueChange = { 
                // Omezení: pouze číslice a maximálně 3 znaky
                if (it.all { char -> char.isDigit() } && it.length <= 3) {
                    vek = it
                }
            },
            label = { Text("Věk") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // Bydliště
        OutlinedTextField(
            value = bydliste,
            onValueChange = { bydliste = it },
            label = { Text("Bydliště") },
            modifier = Modifier.fillMaxWidth()
        )

        // DALŠÍ TEXTOVÉ POLE: Povolání
        OutlinedTextField(
            value = povolani,
            onValueChange = { povolani = it },
            label = { Text("Povolání (např. Programátor)") },
            modifier = Modifier.fillMaxWidth()
        )

        // DALŠÍ ČÍSELNÉ POLE S OMEZENÍM: Počet let praxe
        OutlinedTextField(
            value = zkusenosti,
            onValueChange = {
                // Omezení: pouze číslice a nesmí být víc než 99
                if (it.all { char -> char.isDigit() } && (it.toIntOrNull() ?: 0) < 100) {
                    zkusenosti = it
                }
            },
            label = { Text("Počet let praxe (0-99)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { zobrazitVysledek = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Odeslat data")
        }

        if (zobrazitVysledek) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Shrnutí údajů:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Celé jméno: $jmeno $prijmeni")
                    Text("Věk: $vek let")
                    Text("Bydliště: $bydliste")
                    Text("Povolání: $povolani")
                    Text("Praxe: $zkusenosti let")
                }
            }
        }
    }
}
