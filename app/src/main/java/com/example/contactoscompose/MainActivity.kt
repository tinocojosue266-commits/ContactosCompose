
package com.example.contactoscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.contactoscompose.ui.theme.ContactosComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContactosComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormularioContactos()
                }
            }
        }
    }
}

@Composable
fun FormularioContactos() {

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Personal") }
    var mostrarErrores by remember { mutableStateOf(false) }

    val contactos = remember { mutableStateListOf<Contacto>() }

    val nombreValido = ValidadorJava.nombreValido(nombre)
    val correoValido = ValidadorJava.correoValido(correo)
    val telefonoValido = ValidadorJava.telefonoValido(telefono)

    Scaffold { paddingScaffold ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingScaffold),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        text = "Registro de contactos",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = mostrarErrores && !nombreValido,
                        supportingText = {
                            if (mostrarErrores && !nombreValido) {
                                Text("El nombre es obligatorio")
                            }
                        }
                    )

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = mostrarErrores && !correoValido,
                        supportingText = {
                            if (mostrarErrores && !correoValido) {
                                Text("Introduce un correo válido")
                            }
                        }
                    )

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = {
                            if (it.length <= 8 && it.all(Char::isDigit)) {
                                telefono = it
                            }
                        },
                        label = { Text("Teléfono (8 dígitos)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = mostrarErrores && !telefonoValido,
                        supportingText = {
                            if (mostrarErrores && !telefonoValido) {
                                Text("El teléfono debe tener 8 dígitos")
                            }
                        }
                    )

                    Text(
                        text = "Tipo de contacto",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = tipo == "Personal",
                            onClick = { tipo = "Personal" }
                        )
                        Text("Personal")

                        RadioButton(
                            selected = tipo == "Trabajo",
                            onClick = { tipo = "Trabajo" }
                        )
                        Text("Trabajo")
                    }

                    Button(
                        onClick = {
                            mostrarErrores = true

                            if (nombreValido &&
                                correoValido &&
                                telefonoValido
                            ) {
                                contactos.add(
                                    Contacto(
                                        nombre = nombre.trim(),
                                        correo = correo.trim(),
                                        telefono = telefono,
                                        tipo = tipo
                                    )
                                )

                                nombre = ""
                                correo = ""
                                telefono = ""
                                tipo = "Personal"
                                mostrarErrores = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Agregar contacto")
                    }

                    Text(
                        text = "Contactos agregados: ${contactos.size}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    if (contactos.isEmpty()) {
                        Text("Todavía no hay contactos registrados.")
                    }
                }
            }

            items(contactos) { contacto ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = contacto.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text("Correo: ${contacto.correo}")
                        Text("Teléfono: ${contacto.telefono ?: "No indicado"}")
                        Text("Tipo: ${contacto.tipo}")
                    }
                }
            }
        }
    }
}
