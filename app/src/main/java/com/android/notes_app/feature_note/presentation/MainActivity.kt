package com.android.notes_app.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.android.notes_app.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.android.notes_app.feature_note.presentation.notes.NotesScreen
import com.android.notes_app.feature_note.presentation.util.Screen
import com.android.notes_app.ui.theme.Notes_AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Notes_AppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen
                    ) {
                        composable<Screen.NotesScreen> {
                            NotesScreen(navController = navController)
                        }
                        composable<Screen.AddEditNoteScreen> {
                            /*arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                            )*/
                            val color = it.toRoute<Screen.AddEditNoteScreen>() /*it.arguments?.getInt("noteColor") ?: -1*/
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color.noteColor
                            )
                        }
                    }
                }
            }
        }
    }
}
