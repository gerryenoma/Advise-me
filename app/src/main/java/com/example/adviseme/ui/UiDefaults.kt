package com.example.adviseme.ui

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Re‑usable colour palette for every OutlinedTextField.
 *
 * Usage inside any composable:
 * ```
 * OutlinedTextField(
 *     ...,
 *     colors = FieldColors        // just reference the property
 * )
 * ```
 */
val FieldColors: TextFieldColors
    @Composable
    get() = OutlinedTextFieldDefaults.colors(
        /* text */
        focusedTextColor        = Color.Black,
        unfocusedTextColor      = Color.Black,

        /* cursor */
        cursorColor             = Color.Black,

        /* placeholder */
        focusedPlaceholderColor   = Color.Black,
        unfocusedPlaceholderColor = Color.Black,

        /* label */
        focusedLabelColor       = Color.Black,
        unfocusedLabelColor     = Color.Black,

        /* border */
        focusedBorderColor      = Color.Black,
        unfocusedBorderColor    = Color.Black,

        /* background */
        focusedContainerColor   = Color.White,
        unfocusedContainerColor = Color.White
    )
